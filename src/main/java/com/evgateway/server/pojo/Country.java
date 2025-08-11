package com.evgateway.server.pojo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "country_currency")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Country extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "name")
    private String countryName;

    @Column(name = "countryCode", length = 2, nullable = false)
    private String countryCode;

    @Column(name = "isdCode", length = 6, nullable = false)
    private String isdCode;

    @Temporal(TemporalType.DATE)
    private Date creationDate = new Date();

    @Column(name = "currencyCode")
    private String currencyCode;

    @Column(name = "currencySymbol")
    private String currencySymbol;

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getIsdCode() {
        return isdCode;
    }

    public void setIsdCode(String isdCode) {
        this.isdCode = isdCode;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }

    @Override
    public String toString() {
        return "Country [countryName=" + countryName + ", countryCode=" + countryCode + ", isdCode=" + isdCode
                + ", creationDate=" + creationDate + ", currencyCode=" + currencyCode + ", currencySymbol="
                + currencySymbol + "]";
    }

}