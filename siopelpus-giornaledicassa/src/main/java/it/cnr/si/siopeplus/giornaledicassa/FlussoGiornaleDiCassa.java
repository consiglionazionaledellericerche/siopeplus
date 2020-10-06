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

package it.cnr.si.siopeplus.giornaledicassa;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

import it.siopeplus.giornaledicassa.*;
import org.jvnet.jaxb2_commons.lang.JAXBToStringStrategy;
import org.jvnet.jaxb2_commons.lang.ToString2;
import org.jvnet.jaxb2_commons.lang.ToStringStrategy2;
import org.jvnet.jaxb2_commons.locator.ObjectLocator;


/**
 * <p>Java class for anonymous complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;choice&gt;
 *         &lt;sequence&gt;
 *           &lt;element ref="{http://www.w3.org/2000/09/xmldsig#}Signature"/&gt;
 *           &lt;element ref="{}testata_messaggio"/&gt;
 *           &lt;element ref="{}identificativo_flusso_BT"/&gt;
 *           &lt;element ref="{}pagina"/&gt;
 *           &lt;element ref="{}pagine_totali"/&gt;
 *           &lt;element ref="{}riferimento_ente" minOccurs="0"/&gt;
 *           &lt;element ref="{}esercizio"/&gt;
 *           &lt;element ref="{}data_riferimento_GdC"/&gt;
 *           &lt;element ref="{}informazioni_conto_evidenza" maxOccurs="unbounded"/&gt;
 *           &lt;element ref="{}saldo_complessivo_precedente" minOccurs="0"/&gt;
 *           &lt;element ref="{}totale_complessivo_entrate" minOccurs="0"/&gt;
 *           &lt;element ref="{}totale_complessivo_uscite" minOccurs="0"/&gt;
 *           &lt;element ref="{}saldo_complessivo_finale" minOccurs="0"/&gt;
 *           &lt;element ref="{}totali_esercizio" minOccurs="0"/&gt;
 *           &lt;element ref="{}totali_disponibilita_liquide" minOccurs="0"/&gt;
 *         &lt;/sequence&gt;
 *         &lt;sequence&gt;
 *           &lt;element ref="{}testata_messaggio"/&gt;
 *           &lt;element ref="{}identificativo_flusso_BT"/&gt;
 *           &lt;element ref="{}pagina"/&gt;
 *           &lt;element ref="{}pagine_totali"/&gt;
 *           &lt;element ref="{}riferimento_ente" minOccurs="0"/&gt;
 *           &lt;element ref="{}esercizio"/&gt;
 *           &lt;element ref="{}data_riferimento_GdC"/&gt;
 *           &lt;element ref="{}informazioni_conto_evidenza" maxOccurs="unbounded"/&gt;
 *           &lt;element ref="{}saldo_complessivo_precedente" minOccurs="0"/&gt;
 *           &lt;element ref="{}totale_complessivo_entrate" minOccurs="0"/&gt;
 *           &lt;element ref="{}totale_complessivo_uscite" minOccurs="0"/&gt;
 *           &lt;element ref="{}saldo_complessivo_finale" minOccurs="0"/&gt;
 *           &lt;element ref="{}totali_esercizio" minOccurs="0"/&gt;
 *           &lt;element ref="{}totali_disponibilita_liquide" minOccurs="0"/&gt;
 *           &lt;element ref="{http://www.w3.org/2000/09/xmldsig#}Signature"/&gt;
 *         &lt;/sequence&gt;
 *       &lt;/choice&gt;
 *       &lt;attribute name="Id" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "content"
})
@XmlRootElement(name = "flusso_giornale_di_cassa", namespace = "")
public class FlussoGiornaleDiCassa implements ToString2
{

    @XmlElement(name = "testata_messaggio", required = true)
    protected CtTestataMessaggio testataMessaggio;
    @XmlElement(name = "identificativo_flusso_BT", required = true)
    protected String identificativoFlussoBT;
    @XmlSchemaType(name = "nonNegativeInteger")
    protected Integer pagina;
    @XmlElement(name = "pagine_totali")
    @XmlSchemaType(name = "nonNegativeInteger")
    protected Integer pagineTotali;
    @XmlElement(name = "riferimento_ente")
    protected String riferimentoEnte;
    protected int esercizio;
    @XmlElement(name = "data_riferimento_GdC", required = true)
    protected XMLGregorianCalendar dataRiferimentoGdC;

    @XmlElement(name = "informazioni_conto_evidenza", required = true)
    protected List<InformazioniContoEvidenza> informazioniContoEvidenza;
    @XmlElement(name = "saldo_complessivo_precedente")
    protected BigDecimal saldoComplessivoPrecedente;
    @XmlElement(name = "totale_complessivo_entrate")
    protected BigDecimal totaleComplessivoEntrate;
    @XmlElement(name = "totale_complessivo_uscite")
    protected BigDecimal totaleComplessivoUscite;
    @XmlElement(name = "saldo_complessivo_finale")
    protected BigDecimal saldoComplessivoFinale;
    @XmlElement(name = "totali_esercizio")
    protected TotaliEsercizio totaliEsercizio;
    @XmlElement(name = "totali_disponibilita_liquide")
    protected TotaliDisponibilitaLiquide totaliDisponibilitaLiquide;
    @XmlElement(name = "Signature", namespace = "http://www.w3.org/2000/09/xmldsig#", required = true)
    protected SignatureType signature;
    @XmlAttribute(name = "Id")
    @XmlSchemaType(name = "anySimpleType")
    protected String id;

    /**
     * Gets the value of the testataMessaggio property.
     *
     * @return
     *     possible object is
     *     {@link CtTestataMessaggio }
     *
     */
    public CtTestataMessaggio getTestataMessaggio() {
        return testataMessaggio;
    }

    /**
     * Sets the value of the testataMessaggio property.
     *
     * @param value
     *     allowed object is
     *     {@link CtTestataMessaggio }
     *
     */
    public void setTestataMessaggio(CtTestataMessaggio value) {
        this.testataMessaggio = value;
    }

    /**
     * Gets the value of the identificativoFlussoBT property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getIdentificativoFlussoBT() {
        return identificativoFlussoBT;
    }

    /**
     * Sets the value of the identificativoFlussoBT property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setIdentificativoFlussoBT(String value) {
        this.identificativoFlussoBT = value;
    }

    /**
     * Gets the value of the pagina property.
     *
     * @return
     *     possible object is
     *     {@link Integer }
     *
     */
    public Integer getPagina() {
        return pagina;
    }

    /**
     * Sets the value of the pagina property.
     *
     * @param value
     *     allowed object is
     *     {@link Integer }
     *
     */
    public void setPagina(Integer value) {
        this.pagina = value;
    }

    /**
     * Gets the value of the pagineTotali property.
     *
     * @return
     *     possible object is
     *     {@link Integer }
     *
     */
    public Integer getPagineTotali() {
        return pagineTotali;
    }

    /**
     * Sets the value of the pagineTotali property.
     *
     * @param value
     *     allowed object is
     *     {@link Integer }
     *
     */
    public void setPagineTotali(Integer value) {
        this.pagineTotali = value;
    }

    /**
     * Gets the value of the riferimentoEnte property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getRiferimentoEnte() {
        return riferimentoEnte;
    }

    /**
     * Sets the value of the riferimentoEnte property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setRiferimentoEnte(String value) {
        this.riferimentoEnte = value;
    }

    /**
     * Gets the value of the esercizio property.
     *
     */
    public int getEsercizio() {
        return esercizio;
    }

    /**
     * Sets the value of the esercizio property.
     *
     */
    public void setEsercizio(int value) {
        this.esercizio = value;
    }

    /**
     * Gets the value of the dataInizioPeriodoRiferimento property.
     *
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *
     */
    public XMLGregorianCalendar getDataRiferimentoGdC() {
        return dataRiferimentoGdC;
    }

    /**
     * Sets the value of the dataInizioPeriodoRiferimento property.
     *
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *
     */
    public void setDataRiferimentoGdC(XMLGregorianCalendar value) {
        this.dataRiferimentoGdC = value;
    }

    /**
     * Gets the value of the informazioniContoEvidenza property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the informazioniContoEvidenza property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getInformazioniContoEvidenza().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link InformazioniContoEvidenza }
     *
     *
     */
    public List<InformazioniContoEvidenza> getInformazioniContoEvidenza() {
        if (informazioniContoEvidenza == null) {
            informazioniContoEvidenza = new ArrayList<InformazioniContoEvidenza>();
        }
        return this.informazioniContoEvidenza;
    }

    /**
     * Gets the value of the saldoComplessivoPrecedente property.
     *
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *
     */
    public BigDecimal getSaldoComplessivoPrecedente() {
        return saldoComplessivoPrecedente;
    }

    /**
     * Sets the value of the saldoComplessivoPrecedente property.
     *
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *
     */
    public void setSaldoComplessivoPrecedente(BigDecimal value) {
        this.saldoComplessivoPrecedente = value;
    }

    /**
     * Gets the value of the totaleComplessivoEntrate property.
     *
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *
     */
    public BigDecimal getTotaleComplessivoEntrate() {
        return totaleComplessivoEntrate;
    }

    /**
     * Sets the value of the totaleComplessivoEntrate property.
     *
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *
     */
    public void setTotaleComplessivoEntrate(BigDecimal value) {
        this.totaleComplessivoEntrate = value;
    }

    /**
     * Gets the value of the totaleComplessivoUscite property.
     *
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *
     */
    public BigDecimal getTotaleComplessivoUscite() {
        return totaleComplessivoUscite;
    }

    /**
     * Sets the value of the totaleComplessivoUscite property.
     *
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *
     */
    public void setTotaleComplessivoUscite(BigDecimal value) {
        this.totaleComplessivoUscite = value;
    }

    /**
     * Gets the value of the saldoComplessivoFinale property.
     *
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *
     */
    public BigDecimal getSaldoComplessivoFinale() {
        return saldoComplessivoFinale;
    }

    /**
     * Sets the value of the saldoComplessivoFinale property.
     *
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *
     */
    public void setSaldoComplessivoFinale(BigDecimal value) {
        this.saldoComplessivoFinale = value;
    }

    /**
     * Gets the value of the totaliEsercizio property.
     *
     * @return
     *     possible object is
     *     {@link TotaliEsercizio }
     *
     */
    public TotaliEsercizio getTotaliEsercizio() {
        return totaliEsercizio;
    }

    /**
     * Sets the value of the totaliEsercizio property.
     *
     * @param value
     *     allowed object is
     *     {@link TotaliEsercizio }
     *
     */
    public void setTotaliEsercizio(TotaliEsercizio value) {
        this.totaliEsercizio = value;
    }

    /**
     * Gets the value of the totaliDisponibilitaLiquide property.
     *
     * @return
     *     possible object is
     *     {@link TotaliDisponibilitaLiquide }
     *
     */
    public TotaliDisponibilitaLiquide getTotaliDisponibilitaLiquide() {
        return totaliDisponibilitaLiquide;
    }

    /**
     * Sets the value of the totaliDisponibilitaLiquide property.
     *
     * @param value
     *     allowed object is
     *     {@link TotaliDisponibilitaLiquide }
     *
     */
    public void setTotaliDisponibilitaLiquide(TotaliDisponibilitaLiquide value) {
        this.totaliDisponibilitaLiquide = value;
    }

    /**
     * Gets the value of the signature property.
     *
     * @return
     *     possible object is
     *     {@link SignatureType }
     *
     */
    public SignatureType getSignature() {
        return signature;
    }

    /**
     * Sets the value of the signature property.
     *
     * @param value
     *     allowed object is
     *     {@link SignatureType }
     *
     */
    public void setSignature(SignatureType value) {
        this.signature = value;
    }

    /**
     * Gets the value of the id property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setId(String value) {
        this.id = value;
    }

    public String toString() {
        final ToStringStrategy2 strategy = JAXBToStringStrategy.INSTANCE;
        final StringBuilder buffer = new StringBuilder();
        append(null, buffer, strategy);
        return buffer.toString();
    }

    public StringBuilder append(ObjectLocator locator, StringBuilder buffer, ToStringStrategy2 strategy) {
        strategy.appendStart(locator, this, buffer);
        appendFields(locator, buffer, strategy);
        strategy.appendEnd(locator, this, buffer);
        return buffer;
    }

    public StringBuilder appendFields(ObjectLocator locator, StringBuilder buffer, ToStringStrategy2 strategy) {
        {
            CtTestataMessaggio theTestataMessaggio;
            theTestataMessaggio = this.getTestataMessaggio();
            strategy.appendField(locator, this, "testataMessaggio", buffer, theTestataMessaggio, (this.testataMessaggio!= null));
        }
        {
            String theIdentificativoFlussoBT;
            theIdentificativoFlussoBT = this.getIdentificativoFlussoBT();
            strategy.appendField(locator, this, "identificativoFlussoBT", buffer, theIdentificativoFlussoBT, (this.identificativoFlussoBT!= null));
        }
        {
            Integer thePagina;
            thePagina = this.getPagina();
            strategy.appendField(locator, this, "pagina", buffer, thePagina, (this.pagina!= null));
        }
        {
            Integer thePagineTotali;
            thePagineTotali = this.getPagineTotali();
            strategy.appendField(locator, this, "pagineTotali", buffer, thePagineTotali, (this.pagineTotali!= null));
        }
        {
            String theRiferimentoEnte;
            theRiferimentoEnte = this.getRiferimentoEnte();
            strategy.appendField(locator, this, "riferimentoEnte", buffer, theRiferimentoEnte, (this.riferimentoEnte!= null));
        }
        {
            int theEsercizio;
            theEsercizio = this.getEsercizio();
            strategy.appendField(locator, this, "esercizio", buffer, theEsercizio, true);
        }
        {
            XMLGregorianCalendar theDataRiferimentoGdC;
            theDataRiferimentoGdC = this.getDataRiferimentoGdC();
            strategy.appendField(locator, this, "dataRiferimentoGdC", buffer, theDataRiferimentoGdC, (this.dataRiferimentoGdC!= null));
        }
        {
            List<InformazioniContoEvidenza> theInformazioniContoEvidenza;
            theInformazioniContoEvidenza = (((this.informazioniContoEvidenza!= null)&&(!this.informazioniContoEvidenza.isEmpty()))?this.getInformazioniContoEvidenza():null);
            strategy.appendField(locator, this, "informazioniContoEvidenza", buffer, theInformazioniContoEvidenza, ((this.informazioniContoEvidenza!= null)&&(!this.informazioniContoEvidenza.isEmpty())));
        }
        {
            BigDecimal theSaldoComplessivoPrecedente;
            theSaldoComplessivoPrecedente = this.getSaldoComplessivoPrecedente();
            strategy.appendField(locator, this, "saldoComplessivoPrecedente", buffer, theSaldoComplessivoPrecedente, (this.saldoComplessivoPrecedente!= null));
        }
        {
            BigDecimal theTotaleComplessivoEntrate;
            theTotaleComplessivoEntrate = this.getTotaleComplessivoEntrate();
            strategy.appendField(locator, this, "totaleComplessivoEntrate", buffer, theTotaleComplessivoEntrate, (this.totaleComplessivoEntrate!= null));
        }
        {
            BigDecimal theTotaleComplessivoUscite;
            theTotaleComplessivoUscite = this.getTotaleComplessivoUscite();
            strategy.appendField(locator, this, "totaleComplessivoUscite", buffer, theTotaleComplessivoUscite, (this.totaleComplessivoUscite!= null));
        }
        {
            BigDecimal theSaldoComplessivoFinale;
            theSaldoComplessivoFinale = this.getSaldoComplessivoFinale();
            strategy.appendField(locator, this, "saldoComplessivoFinale", buffer, theSaldoComplessivoFinale, (this.saldoComplessivoFinale!= null));
        }
        {
            TotaliEsercizio theTotaliEsercizio;
            theTotaliEsercizio = this.getTotaliEsercizio();
            strategy.appendField(locator, this, "totaliEsercizio", buffer, theTotaliEsercizio, (this.totaliEsercizio!= null));
        }
        {
            TotaliDisponibilitaLiquide theTotaliDisponibilitaLiquide;
            theTotaliDisponibilitaLiquide = this.getTotaliDisponibilitaLiquide();
            strategy.appendField(locator, this, "totaliDisponibilitaLiquide", buffer, theTotaliDisponibilitaLiquide, (this.totaliDisponibilitaLiquide!= null));
        }
        {
            SignatureType theSignature;
            theSignature = this.getSignature();
            strategy.appendField(locator, this, "signature", buffer, theSignature, (this.signature!= null));
        }
        {
            String theId;
            theId = this.getId();
            strategy.appendField(locator, this, "id", buffer, theId, (this.id!= null));
        }
        return buffer;
    }

}
