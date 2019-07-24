/*
 * Copyright (C) 2019  Consiglio Nazionale delle Ricerche
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package it.cnr.si.siopeplus;

import it.cnr.si.siopeplus.exception.SIOPEPlusServiceUnavailable;
import it.cnr.si.siopeplus.model.Esito;
import it.cnr.si.siopeplus.model.Risultato;
import it.cnr.si.siopeplus.service.OrdinativiSiopePlusService;
import it.cnr.si.firmadigitale.firma.arss.ArubaSignServiceClient;
import it.cnr.si.firmadigitale.firma.arss.ArubaSignServiceException;
import it.cnr.si.firmadigitale.firma.arss.stub.XmlSignatureType;
import it.siopeplus.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.PropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Stream;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
@TestPropertySource("classpath:application-test.properties")
public class OrdinativiSiopePlusTest {
    @Value("${sign.username}")
    private String signUsername;
    @Value("${sign.password}")
    private String signPassword;
    @Value("${sign.otp}")
    private String signOTP;

    private static final DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    @Autowired
    private OrdinativiSiopePlusService ordinativiSiopePlusService;
    @Autowired
    private AbstractEnvironment environment;
    @Autowired
    private ApplicationContext applicationContext;

    static boolean validateAgainstXSD(InputStream xml, InputStream xsd) {
        try {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new StreamSource(xsd));
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(xml));
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    @Test
    public void downloadACK() {
        final List<Risultato> lista = ordinativiSiopePlusService.getAllMessaggi(
                Esito.ACK,
                LocalDateTime.of(2019,6, 1,5,0),
                null,
                true,
                null);
        Assert.notNull(lista);
        Optional.ofNullable(lista)
                .orElse(Collections.emptyList())
                .stream()
                .forEach(risultato -> {
                    final MessaggioAckSiope messaggioAckSiope =
                            ordinativiSiopePlusService.getLocation(risultato.getLocation(), MessaggioAckSiope.class).getObject();
                    Assert.notNull(messaggioAckSiope);
                });
    }

    @Test
    public void downloadEsito() {
        final List<Risultato> lista = ordinativiSiopePlusService.getAllMessaggi(
                Esito.ESITO,
                LocalDateTime.of(2019,6, 1,5,0),
                null,
                true,
                null);
        Assert.notNull(lista);
        Optional.ofNullable(lista)
                .orElse(Collections.emptyList())
                .stream()
                .forEach(risultato -> {
                    final MessaggioRicezioneFlusso messaggioRicezioneFlusso =
                            ordinativiSiopePlusService.getLocation(risultato.getLocation(), MessaggioRicezioneFlusso.class).getObject();
                    Assert.notNull(messaggioRicezioneFlusso);
                });
    }

    @Test
    public void downloadEsitoApplicativo() {
        final List<Risultato> lista = ordinativiSiopePlusService.getAllMessaggi(
                Esito.ESITOAPPLICATIVO,
                LocalDateTime.of(2019,6, 1,5,0),
                null,
                true,
                null);
        Assert.notNull(lista);
        Optional.ofNullable(lista)
                .orElse(Collections.emptyList())
                .stream()
                .forEach(risultato -> {
                    final MessaggiEsitoApplicativo messaggiEsitoApplicativo =
                            ordinativiSiopePlusService.getLocation(risultato.getLocation(), MessaggiEsitoApplicativo.class).getObject();
                    Assert.notNull(messaggiEsitoApplicativo);
                });
    }

    @Test
    public void postFLUSSO() throws JAXBException, IOException, DatatypeConfigurationException, ArubaSignServiceException, SIOPEPlusServiceUnavailable {
        final InputStream inputStream = generaFlusso();
        final Risultato risultato = ordinativiSiopePlusService.postFlusso(inputStream);
        Assert.notNull(risultato);
    }

    private InputStream generaFlusso() throws JAXBException, IOException, DatatypeConfigurationException, ArubaSignServiceException {

        LocalDateTime date = LocalDateTime.now();

        final it.siopeplus.ObjectFactory objectFactory = new it.siopeplus.ObjectFactory();
        FlussoOrdinativi flussoOrdinativi = objectFactory.createFlussoOrdinativi();

        final CtTestataFlusso testataFlusso = objectFactory.createCtTestataFlusso();
        testataFlusso.setCodiceABIBT("01005");
        testataFlusso.setRiferimentoEnte(environment.getProperty("codice.a2a"));
        testataFlusso.setIdentificativoFlusso(LocalDateTime.now().getYear() + "-TEST-" + LocalDateTime.now().toEpochSecond(ZoneOffset.UTC) + "-I");
        testataFlusso.setDataOraCreazioneFlusso(DatatypeFactory.newInstance().newXMLGregorianCalendar(formatterTime.format(date)));
        testataFlusso.setCodiceEnte(environment.getProperty("codice.uni.uo"));
        testataFlusso.setCodiceEnteBT(environment.getProperty("codice.ente.bt"));
        testataFlusso.setCodiceTramiteEnte(environment.getProperty("codice.tramite.ente"));
        testataFlusso.setCodiceTramiteBT(environment.getProperty("codice.tramite.ente.bt"));
        testataFlusso.setDescrizioneEnte("Consiglio Nazionale delle Ricerche");
        testataFlusso.setCodiceIstatEnte("000713516000000");
        testataFlusso.setCodiceFiscaleEnte("80054330586");
        flussoOrdinativi.getContent().add(objectFactory.createTestataFlusso(testataFlusso));

        flussoOrdinativi.getContent().add(objectFactory.createEsercizio(2018));

        final Mandato mandato = objectFactory.createMandato();
        mandato.setTipoOperazione("INSERIMENTO");
        mandato.setNumeroMandato(101047);
        mandato.setDataMandato(DatatypeFactory.newInstance().newXMLGregorianCalendar(formatterDate.format(date)));
        mandato.setImportoMandato(BigDecimal.valueOf(1150.00));
        mandato.setContoEvidenza("1");

        Mandato.InformazioniBeneficiario informazioniBeneficiario = objectFactory.createMandatoInformazioniBeneficiario();
        informazioniBeneficiario.setProgressivoBeneficiario(1);
        informazioniBeneficiario.setImportoBeneficiario(BigDecimal.valueOf(1150.00));
        informazioniBeneficiario.setTipoPagamento("CASSA");
        informazioniBeneficiario.setDestinazione("LIBERA");

        Mandato.InformazioniBeneficiario.Classificazione classificazione = objectFactory.createMandatoInformazioniBeneficiarioClassificazione();
        classificazione.setCodiceCgu("7019999999");
        classificazione.setImporto(BigDecimal.valueOf(1150.00));
        CtClassificazioneDatiSiopeUscite ctClassificazioneDatiSiopeUscite = objectFactory.createCtClassificazioneDatiSiopeUscite();

        ctClassificazioneDatiSiopeUscite.getTipoDebitoSiopeNcAndCodiceCigSiopeOrMotivoEsclusioneCigSiope().add(StTipoDebitoNonCommerciale.NON_COMMERCIALE);

        classificazione.setClassificazioneDatiSiopeUscite(ctClassificazioneDatiSiopeUscite);
        informazioniBeneficiario.getClassificazione().add(classificazione);

        Mandato.InformazioniBeneficiario.Bollo bollo = objectFactory.createMandatoInformazioniBeneficiarioBollo();
        bollo.setAssoggettamentoBollo("ESENTE BOLLO");
        bollo.setCausaleEsenzioneBollo("ESENTE -  Pagamenti effettuati con accred. in c/c bancario o postale (art. 7 tabella D.P.R. 642/72)");
        informazioniBeneficiario.setBollo(bollo);

        Mandato.InformazioniBeneficiario.Spese spese = objectFactory.createMandatoInformazioniBeneficiarioSpese();
        spese.setSoggettoDestinatarioDelleSpese("A CARICO BENEFICIARIO");
        informazioniBeneficiario.setSpese(spese);

        Beneficiario beneficiario = objectFactory.createBeneficiario();
        beneficiario.setAnagraficaBeneficiario("PARDINI ANTONELLA");
        beneficiario.setIndirizzoBeneficiario("VIA ARGINE VECCHIO, 357");
        beneficiario.setCapBeneficiario("56019");
        beneficiario.setLocalitaBeneficiario("VECCHIANO");
        beneficiario.setProvinciaBeneficiario("PI");
        informazioniBeneficiario.setBeneficiario(beneficiario);

        informazioniBeneficiario.setCausale("Pagamento netto per mancata corresponsione retribuzione di novembre 2018 a Pardini Antonella, matr");
        mandato.getInformazioniBeneficiario().add(informazioniBeneficiario);

        flussoOrdinativi.getContent().add(objectFactory.createMandato(mandato));

        final Reversale reversale = objectFactory.createReversale();
        reversale.setTipoOperazione("INSERIMENTO");
        reversale.setNumeroReversale(25383);
        reversale.setDataReversale(DatatypeFactory.newInstance().newXMLGregorianCalendar(formatterDate.format(date)));
        reversale.setImportoReversale(BigDecimal.valueOf(167.36));
        reversale.setContoEvidenza("1");

        Reversale.InformazioniVersante informazioniVersante = objectFactory.createReversaleInformazioniVersante();
        informazioniVersante.setProgressivoVersante(1);
        informazioniVersante.setImportoVersante(BigDecimal.valueOf(167.36));
        informazioniVersante.setTipoRiscossione("CASSA");
        informazioniVersante.setTipoEntrata("INFRUTTIFERO");
        informazioniVersante.setDestinazione("LIBERA");

        Reversale.InformazioniVersante.Classificazione classificazione1 = objectFactory.createReversaleInformazioniVersanteClassificazione();
        classificazione1.setCodiceCge("9010102001");
        classificazione1.setImporto(BigDecimal.valueOf(167.36));
        CtClassificazioneDatiSiopeEntrate ctClassificazioneDatiSiopeEntrate = objectFactory.createCtClassificazioneDatiSiopeEntrate();
        ctClassificazioneDatiSiopeEntrate.getContent().add(
                objectFactory.createCtClassificazioneDatiSiopeEntrateTipoDebitoSiopeNc(StTipoDebitoNonCommerciale.NON_COMMERCIALE)
        );
        classificazione1.setClassificazioneDatiSiopeEntrate(ctClassificazioneDatiSiopeEntrate);
        informazioniVersante.getClassificazione().add(classificazione1);

        Reversale.InformazioniVersante.Bollo bollo1 = objectFactory.createReversaleInformazioniVersanteBollo();
        bollo1.setAssoggettamentoBollo("ESENTE BOLLO");
        bollo1.setCausaleEsenzioneBollo("ESENTE -  Pagamenti effettuati con accred. in c/c bancario o postale (art. 7 tabella D.P.R. 642/72)");
        informazioniVersante.setBollo(bollo1);

        final Versante versante = objectFactory.createVersante();
        versante.setAnagraficaVersante("Sigma Aldrich s.r.l.");
        informazioniVersante.setVersante(versante);

        informazioniVersante.setCausale("REVERSALE IVA");
        reversale.getInformazioniVersante().add(informazioniVersante);

        flussoOrdinativi.getContent().add(objectFactory.createReversale(reversale));

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        JAXBContext jc = JAXBContext.newInstance("it.siopeplus");
        Marshaller jaxbMarshaller = jc.createMarshaller();
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.FALSE);
        jaxbMarshaller.marshal(flussoOrdinativi, byteArrayOutputStream);

        String out = new String(byteArrayOutputStream.toByteArray(), StandardCharsets.UTF_8);
        out = out.replace("</flusso_ordinativi>", "\n</flusso_ordinativi>");

        ArubaSignServiceClient client = new ArubaSignServiceClient();
        client.setProps(Stream.generate(environment.getPropertySources().iterator()::next)
                .filter(propertySource -> propertySource.getName().contains("application-test.properties"))
                .findAny()
                .map(PropertySource::getSource)
                .filter(Properties.class::isInstance)
                .map(Properties.class::cast)
                .orElseThrow(() -> new IllegalArgumentException()));
        byte[] contentSigned = client.xmlSignature(signUsername, signPassword, signOTP, out.getBytes(), XmlSignatureType.XMLENVELOPED);

        Assert.isTrue(validateAgainstXSD(
                new ByteArrayInputStream(contentSigned),
                applicationContext.getResource ("classpath:xsd/OPI_FLUSSO_ORDINATIVI_V_1_3_1.xsd").getInputStream())
        );
        return new ByteArrayInputStream(contentSigned);
    }
}
