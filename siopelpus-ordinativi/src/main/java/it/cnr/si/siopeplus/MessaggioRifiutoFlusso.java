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

//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.12.13 at 08:05:02 PM CET 
//


package it.cnr.si.siopeplus;

import it.siopeplus.CtErrore;
import it.siopeplus.CtTestataMessaggio;
import it.siopeplus.SignatureType;
import org.jvnet.jaxb2_commons.lang.JAXBToStringStrategy;
import org.jvnet.jaxb2_commons.lang.ToString2;
import org.jvnet.jaxb2_commons.lang.ToStringStrategy2;
import org.jvnet.jaxb2_commons.locator.ObjectLocator;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{http://www.w3.org/2000/09/xmldsig#}Signature"/&gt;
 *         &lt;element name="testata_messaggio" type="{}ctTestata_messaggio"/&gt;
 *         &lt;element name="identificativo_flusso" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="identificativo_flusso_BT" type="{}sTId_flusso_BT"/&gt;
 *         &lt;element name="esercizio" type="{}stEsercizio"/&gt;
 *         &lt;element name="errore" type="{}ctErrore" maxOccurs="unbounded"/&gt;
 *       &lt;/sequence&gt;
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
    "signature",
    "testataMessaggio",
    "identificativoFlusso",
    "identificativoFlussoBT",
    "esercizio",
    "errore"
})
@XmlRootElement(name = "messaggio_rifiuto_flusso")
public class MessaggioRifiutoFlusso implements ToString2, EsitoFlusso
{

    @XmlElement(name = "testata_messaggio", required = true)
    protected CtTestataMessaggio testataMessaggio;
    @XmlElement(name = "identificativo_flusso", required = true)
    protected String identificativoFlusso;
    @XmlElement(name = "identificativo_flusso_BT", required = true)
    protected String identificativoFlussoBT;
    @XmlSchemaType(name = "nonNegativeInteger")
    protected int esercizio;
    @XmlElement(required = true)
    protected List<CtErrore> errore;
    @XmlElement(name = "Signature", namespace = "http://www.w3.org/2000/09/xmldsig#", required = true)
    protected SignatureType signature;
    @XmlAttribute(name = "Id")
    @XmlSchemaType(name = "anySimpleType")
    protected String id;

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
     * Gets the value of the identificativoFlusso property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdentificativoFlusso() {
        return identificativoFlusso;
    }

    /**
     * Sets the value of the identificativoFlusso property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdentificativoFlusso(String value) {
        this.identificativoFlusso = value;
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
     * Gets the value of the errore property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the errore property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getErrore().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CtErrore }
     * 
     * 
     */
    public List<CtErrore> getErrore() {
        if (errore == null) {
            errore = new ArrayList<CtErrore>();
        }
        return this.errore;
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
            SignatureType theSignature;
            theSignature = this.getSignature();
            strategy.appendField(locator, this, "signature", buffer, theSignature, (this.signature!= null));
        }
        {
            CtTestataMessaggio theTestataMessaggio;
            theTestataMessaggio = this.getTestataMessaggio();
            strategy.appendField(locator, this, "testataMessaggio", buffer, theTestataMessaggio, (this.testataMessaggio!= null));
        }
        {
            String theIdentificativoFlusso;
            theIdentificativoFlusso = this.getIdentificativoFlusso();
            strategy.appendField(locator, this, "identificativoFlusso", buffer, theIdentificativoFlusso, (this.identificativoFlusso!= null));
        }
        {
            String theIdentificativoFlussoBT;
            theIdentificativoFlussoBT = this.getIdentificativoFlussoBT();
            strategy.appendField(locator, this, "identificativoFlussoBT", buffer, theIdentificativoFlussoBT, (this.identificativoFlussoBT!= null));
        }
        {
            int theEsercizio;
            theEsercizio = this.getEsercizio();
            strategy.appendField(locator, this, "esercizio", buffer, theEsercizio, true);
        }
        {
            List<CtErrore> theErrore;
            theErrore = (((this.errore!= null)&&(!this.errore.isEmpty()))?this.getErrore():null);
            strategy.appendField(locator, this, "errore", buffer, theErrore, ((this.errore!= null)&&(!this.errore.isEmpty())));
        }
        {
            String theId;
            theId = this.getId();
            strategy.appendField(locator, this, "id", buffer, theId, (this.id!= null));
        }
        return buffer;
    }

    @Override
    public boolean isRifiutato() {
        return true;
    }
}
