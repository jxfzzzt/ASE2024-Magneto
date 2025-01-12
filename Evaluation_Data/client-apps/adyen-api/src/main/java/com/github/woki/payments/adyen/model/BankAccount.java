package com.github.woki.payments.adyen.model;

import com.github.woki.payments.adyen.PublicApi;
import com.github.woki.payments.adyen.ToStringStyle;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

@SuppressWarnings("serial")
@PublicApi
public class BankAccount implements Serializable {
    private String bankAccountNumber;
    private String bankLocationId;
    private String bankName;
    private String bic;
    private String countryCode = "US";
    private String iban;
    private String ownerName;

    @PublicApi
    public BankAccount() {
    }

    @PublicApi
    public String getBankAccountNumber() {
        return bankAccountNumber;
    }

    @PublicApi
    public void setBankAccountNumber(String bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber;
    }

    @PublicApi
    public String getBankLocationId() {
        return bankLocationId;
    }

    @PublicApi
    public void setBankLocationId(String bankLocationId) {
        this.bankLocationId = bankLocationId;
    }

    @PublicApi
    public String getOwnerName() {
        return ownerName;
    }

    @PublicApi
    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    @PublicApi
    public String getCountryCode() {
        return countryCode;
    }

    @PublicApi
    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    @PublicApi
    public String getIban() {
        return iban;
    }

    @PublicApi
    public void setIban(String iban) {
        this.iban = iban;
    }

    @PublicApi
    public String getBic() {
        return bic;
    }

    @PublicApi
    public void setBic(String bic) {
        this.bic = bic;
    }

    @PublicApi
    public String getBankName() {
        return bankName;
    }

    @PublicApi
    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).append("bankAccountNumber", bankAccountNumber).append("bankLocationId", bankLocationId)
                .append("bankName", bankName).append("bic", bic).append("countryCode", countryCode).append("iban", iban).append("ownerName", ownerName).toString();
    }
}
