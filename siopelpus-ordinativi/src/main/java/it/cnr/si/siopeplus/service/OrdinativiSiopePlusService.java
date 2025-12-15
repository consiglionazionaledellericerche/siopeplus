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

package it.cnr.si.siopeplus.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.cnr.si.siopeplus.exception.SIOPEPlusServiceUnavailable;
import it.cnr.si.siopeplus.custom.ObjectFactory;
import it.cnr.si.siopeplus.model.*;
import org.apache.commons.io.IOUtils;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.core5.http.HttpStatus;
import org.apache.hc.core5.http.io.entity.InputStreamEntity;
import org.apache.hc.core5.net.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class OrdinativiSiopePlusService extends CommonsSiopePlusService {
    private transient static final Logger logger = LoggerFactory.getLogger(OrdinativiSiopePlusService.class);

    private final String a2a;
    private final String uniuo;
    public final String urlFlusso;
    public final String urlACK;
    public final String urlEsito;
    private final String urlEsitoApplicativo;

    public String getA2a() {
        return a2a;
    }

    public String getUniuo() {
        return uniuo;
    }

    public OrdinativiSiopePlusService(String a2a, String uniuo, String urlFlusso, String urlACK, String urlEsito, String urlEsitoApplicativo) {
        this.a2a = a2a;
        this.uniuo = uniuo;
        this.urlFlusso = urlFlusso;
        this.urlACK = urlACK;
        this.urlEsito = urlEsito;
        this.urlEsitoApplicativo = urlEsitoApplicativo;
    }

    public String getURL(Esito esito) {
        switch (esito) {
            case ACK:
                return urlACK;
            case ESITO:
                return urlEsito;
            case ESITOAPPLICATIVO:
                return urlEsitoApplicativo;
            default:
                return urlACK;
        }
    }

    public Risultato postFlusso(InputStream input) throws SIOPEPlusServiceUnavailable {
        CloseableHttpClient client = null;
        try {
            client = getHttpClient();

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            final ZipOutputStream zos = new ZipOutputStream(out);
            ZipEntry zipEntryChild = new ZipEntry("flusso.xml");
            zos.putNextEntry(zipEntryChild);
            IOUtils.copyLarge(input, zos);
            zos.close();
            InputStream inputZIP = new ByteArrayInputStream(out.toByteArray());

            URIBuilder builder = new URIBuilder(urlFlusso);
            HttpPost httpPost = new HttpPost(builder.build());
            httpPost.setHeader("Content-Type", APPLICATION_ZIP);
            httpPost.setHeader("Accept", APPLICATION_JSON_UTF8);

            final InputStreamEntity inputStreamEntity = new InputStreamEntity(inputZIP, null);
            httpPost.setEntity(inputStreamEntity);
            httpPost.setHeader("Transfer-Encoding", "chunked");

            try (CloseableHttpResponse response = client.execute(httpPost)) {
                if (response.getCode() != HttpStatus.SC_CREATED) {
                    if (response.getCode() == HttpStatus.SC_SERVICE_UNAVAILABLE)
                        throw new SIOPEPlusServiceUnavailable();
                    logger.error(response.getReasonPhrase());
                    throw new RuntimeException("Cannot send flusso error code: " + response.getCode());
                }
                Gson gson = new GsonBuilder().setDateFormat(pattern).create();
                return gson.fromJson(IOUtils.toString(response.getEntity().getContent(), "UTF-8"), Risultato.class);
            }
        } catch (URISyntaxException | KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException | UnrecoverableKeyException | KeyManagementException e) {
            throw new RuntimeException(e);
        } finally {
            Optional.ofNullable(client)
                    .ifPresent(httpClient -> {
                        try {
                            httpClient.close();
                        } catch (IOException e) {
                            logger.error("CANNOT CLOSE HTTPCLIENT");
                        }
                    });
        }
    }

    public List<Risultato> getAllMessaggi(Esito esito, LocalDateTime dataDa, LocalDateTime dataA, Boolean download, Integer pagina) {
        List<Risultato> risultatoList = new ArrayList<Risultato>();
        final Lista listaMessaggi = getListaMessaggi(esito, dataDa, dataA, download, pagina, 0);
        risultatoList.addAll(
                Optional.ofNullable(listaMessaggi)
                        .flatMap(lista -> Optional.ofNullable(lista.getRisultati()))
                        .orElse(Collections.emptyList())
        );
        if (listaMessaggi.getNumPagine() > 1) {
            for (int i = 2; i <= listaMessaggi.getNumPagine(); i++) {
                risultatoList.addAll(
                        Optional.ofNullable(getListaMessaggi(esito, dataDa, dataA, download, i, 0))
                                .flatMap(lista -> Optional.ofNullable(lista.getRisultati()))
                                .orElse(Collections.emptyList())
                );
            }
        }
        return risultatoList;
    }

    private Lista getListaMessaggi(Esito esito, LocalDateTime dataDa, LocalDateTime dataA, Boolean download, Integer pagina, Integer iterate) {
        CloseableHttpClient client = null;
        try {
            client = getHttpClient();
            URIBuilder builder = new URIBuilder(getURL(esito));
            Optional.ofNullable(dataDa)
                    .ifPresent(date -> builder.addParameter(getDataDa(esito), dataDa.format(formatter)));
            Optional.ofNullable(dataA)
                    .ifPresent(date -> builder.addParameter(getDataA(esito), dataA.format(formatter)));
            Optional.ofNullable(download)
                    .ifPresent(aBoolean -> builder.addParameter(Parameter.download.name(), aBoolean.toString()));
            Optional.ofNullable(pagina)
                    .ifPresent(integer -> builder.addParameter(Parameter.pagina.name(), integer.toString()));
            final URI uri = builder.build();
            HttpGet httpGet = new HttpGet(uri);
            httpGet.setHeader("Accept", APPLICATION_JSON_UTF8);

            try (CloseableHttpResponse response = client.execute(httpGet)) {
                if (response.getCode() != HttpStatus.SC_OK) {
                    logger.error("ERROR SIOPE+ for LISTA MESSAGGI {} ITERATE {}", response.getReasonPhrase(), iterate);
                    if (response.getCode() == TOO_MANY_REQUEST) {
                        try {
                            TimeUnit.SECONDS.sleep(TIMEOUT);
                            if (iterate < 10) {
                                return getListaMessaggi(esito, dataDa, dataA, download, pagina, ++iterate);
                            } else {
                                logger.error("ERROR SIOPE+ CANNOT ITERATE THAN 10");
                                return new Lista();
                            }
                        } catch (InterruptedException e) {
                            logger.error("ERROR SIOPE+ for LISTA MESSAGGI", e);
                        }
                    }
                    throw new RuntimeException("Error connecting to " + uri + " with response code: " + response.getCode());
                }
                Gson gson = new GsonBuilder().setDateFormat(pattern).create();
                return gson.fromJson(IOUtils.toString(response.getEntity().getContent(), "UTF-8"), Lista.class);
            }
        } catch (URISyntaxException | KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException | UnrecoverableKeyException | KeyManagementException e) {
            throw new RuntimeException(e);
        } finally {
            Optional.ofNullable(client)
                    .ifPresent(httpClient -> {
                        try {
                            httpClient.close();
                        } catch (IOException e) {
                            logger.error("CANNOT CLOSE HTTPCLIENT");
                        }
                    });
        }
    }

    public <T extends Object> MessaggioXML<T> getLocation(String location, Class<T> clazz) {
        try {
            return getLocation(location, clazz, JAXBContext.newInstance(
                    it.siopeplus.ObjectFactory.class,
                    it.cnr.si.siopeplus.custom.ObjectFactory.class), new AtomicInteger(0));
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    public void validateFlussoOrdinativi(InputStream xml) throws SAXException, IOException {
        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = factory.newSchema(this.getClass().getResource("/xsd/OPI_FLUSSO_ORDINATIVI_V_1_6_0.xsd"));
        Validator validator = schema.newValidator();
        validator.validate(new StreamSource(xml));
    }
}