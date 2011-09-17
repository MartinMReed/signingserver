//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.0.2-b01-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.09.17 at 04:45:09 PM CDT 
//


package org.hardisonbrewing.schemas.model;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SigningConfiguration complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SigningConfiguration">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="tracker" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="salt" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="privateKey" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="authorities">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="authority" type="{http://hardisonbrewing.org/schemas/model}SigningAuthority" maxOccurs="unbounded"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="cods">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="cod" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SigningConfiguration", propOrder = {
    "tracker",
    "salt",
    "privateKey",
    "authorities",
    "cods"
})
public class SigningConfiguration {

    @XmlElement(required = true)
    protected String tracker;
    @XmlElement(required = true)
    protected String salt;
    @XmlElement(required = true)
    protected String privateKey;
    @XmlElement(required = true)
    protected SigningConfiguration.Authorities authorities;
    @XmlElement(required = true)
    protected SigningConfiguration.Cods cods;

    /**
     * Gets the value of the tracker property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTracker() {
        return tracker;
    }

    /**
     * Sets the value of the tracker property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTracker(String value) {
        this.tracker = value;
    }

    /**
     * Gets the value of the salt property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSalt() {
        return salt;
    }

    /**
     * Sets the value of the salt property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSalt(String value) {
        this.salt = value;
    }

    /**
     * Gets the value of the privateKey property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPrivateKey() {
        return privateKey;
    }

    /**
     * Sets the value of the privateKey property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPrivateKey(String value) {
        this.privateKey = value;
    }

    /**
     * Gets the value of the authorities property.
     * 
     * @return
     *     possible object is
     *     {@link SigningConfiguration.Authorities }
     *     
     */
    public SigningConfiguration.Authorities getAuthorities() {
        return authorities;
    }

    /**
     * Sets the value of the authorities property.
     * 
     * @param value
     *     allowed object is
     *     {@link SigningConfiguration.Authorities }
     *     
     */
    public void setAuthorities(SigningConfiguration.Authorities value) {
        this.authorities = value;
    }

    /**
     * Gets the value of the cods property.
     * 
     * @return
     *     possible object is
     *     {@link SigningConfiguration.Cods }
     *     
     */
    public SigningConfiguration.Cods getCods() {
        return cods;
    }

    /**
     * Sets the value of the cods property.
     * 
     * @param value
     *     allowed object is
     *     {@link SigningConfiguration.Cods }
     *     
     */
    public void setCods(SigningConfiguration.Cods value) {
        this.cods = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="authority" type="{http://hardisonbrewing.org/schemas/model}SigningAuthority" maxOccurs="unbounded"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "authority"
    })
    public static class Authorities {

        @XmlElement(required = true)
        protected List<SigningAuthority> authority;

        /**
         * Gets the value of the authority property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the authority property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getAuthority().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link SigningAuthority }
         * 
         * 
         */
        public List<SigningAuthority> getAuthority() {
            if (authority == null) {
                authority = new ArrayList<SigningAuthority>();
            }
            return this.authority;
        }

    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="cod" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "cod"
    })
    public static class Cods {

        @XmlElement(required = true)
        protected List<String> cod;

        /**
         * Gets the value of the cod property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the cod property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getCod().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link String }
         * 
         * 
         */
        public List<String> getCod() {
            if (cod == null) {
                cod = new ArrayList<String>();
            }
            return this.cod;
        }

    }

}
