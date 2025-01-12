package com.github.woki.payments.adyen.model;

import com.github.woki.payments.adyen.PublicApi;
import com.github.woki.payments.adyen.ToStringStyle;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
@PublicApi
public class ForexQuote implements Serializable {
    private int basePoints;
    private Date validTill;
    private String account;
    private String accountType;
    private Amount baseAmount;
    private Amount buy;
    private Amount interbank;
    private String reference;
    private Amount sell;
    private String signature;
    private String source;
    private String type;

    @PublicApi
    public ForexQuote() {
    }

    @PublicApi
    public int getBasePoints() {
        return basePoints;
    }

    @PublicApi
    public void setBasePoints(int basePoints) {
        this.basePoints = basePoints;
    }

    @PublicApi
    public Date getValidTill() {
        return validTill;
    }

    @PublicApi
    public void setValidTill(Date validTill) {
        this.validTill = validTill;
    }

    @PublicApi
    public String getAccount() {
        return account;
    }

    @PublicApi
    public void setAccount(String account) {
        this.account = account;
    }

    @PublicApi
    public String getAccountType() {
        return accountType;
    }

    @PublicApi
    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    @PublicApi
    public Amount getBaseAmount() {
        return baseAmount;
    }

    @PublicApi
    public void setBaseAmount(Amount baseAmount) {
        this.baseAmount = baseAmount;
    }

    @PublicApi
    public Amount getBuy() {
        return buy;
    }

    @PublicApi
    public void setBuy(Amount buy) {
        this.buy = buy;
    }

    @PublicApi
    public Amount getInterbank() {
        return interbank;
    }

    @PublicApi
    public void setInterbank(Amount interbank) {
        this.interbank = interbank;
    }

    @PublicApi
    public String getReference() {
        return reference;
    }

    @PublicApi
    public void setReference(String reference) {
        this.reference = reference;
    }

    @PublicApi
    public Amount getSell() {
        return sell;
    }

    @PublicApi
    public void setSell(Amount sell) {
        this.sell = sell;
    }

    @PublicApi
    public String getSignature() {
        return signature;
    }

    @PublicApi
    public void setSignature(String signature) {
        this.signature = signature;
    }

    @PublicApi
    public String getSource() {
        return source;
    }

    @PublicApi
    public void setSource(String source) {
        this.source = source;
    }

    @PublicApi
    public String getType() {
        return type;
    }

    @PublicApi
    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).append("basePoints", basePoints).append("validTill", validTill).append("account", account)
                .append("accountType", accountType).append("baseAmount", baseAmount).append("buy", buy).append("interbank", interbank)
                .append("reference", reference).append("sell", sell).append("signature", signature).append("source", source).append("type", type).toString();
    }
}
