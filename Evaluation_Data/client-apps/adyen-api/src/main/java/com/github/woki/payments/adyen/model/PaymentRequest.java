/*
 * Copyright 2015 Willian Oki
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */
package com.github.woki.payments.adyen.model;

import com.github.woki.payments.adyen.PublicApi;
import com.github.woki.payments.adyen.ToStringStyle;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Willian Oki &lt;willian.oki@gmail.com&gt;
 */
@SuppressWarnings("serial")
@PublicApi
public class PaymentRequest implements Serializable {
    private Amount additionalAmount;
    private Map<String, String> additionalData = new HashMap<>();
    private Amount amount;
    private BankAccount bankAccount;
    private Address billingAddress;
    private BrowserInfo browserInfo;
    private int captureDelayHours;
    private Card card;
    private Date dateOfBirth;
    private ForexQuote dccQuote;
    private Address deliveryAddress;
    private String deliveryDate;
    private String deviceFingerprint;
    private int fraudOffset;
    private Installments installments;
    private int mcc;
    private String md;
    private String merchantAccount;
    private String merchantOrderReference;
    private ThreeDSecureData mpiData;
    private String orderReference;
    private String paResponse;
    private Recurring recurring;
    private String reference;
    private String selectedBrand;
    private String selectedRecurringDetailReference;
    private String sessionId;
    private String shopperEmail;
    private String shopperIP;
    private ShopperInteraction shopperInteraction;
    private String shopperLocale;
    private Name shopperName;
    private String shopperReference;
    private String shopperStatement;
    private String socialSecurityNumber;
    private String telephoneNumber;

    @PublicApi
    public PaymentRequest() {
    }

    @PublicApi
    public String getMerchantAccount() {
        return merchantAccount;
    }

    @PublicApi
    public void setMerchantAccount(String merchantAccount) {
        this.merchantAccount = merchantAccount;
    }

    @PublicApi
    public Amount getAmount() {
        return amount;
    }

    @PublicApi
    public void setAmount(Amount amount) {
        this.amount = amount;
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
    public String getShopperIP() {
        return shopperIP;
    }

    @PublicApi
    public void setShopperIP(String shopperIP) {
        this.shopperIP = shopperIP;
    }

    @PublicApi
    public String getShopperEmail() {
        return shopperEmail;
    }

    @PublicApi
    public void setShopperEmail(String shopperEmail) {
        this.shopperEmail = shopperEmail;
    }

    @PublicApi
    public String getShopperReference() {
        return shopperReference;
    }

    @PublicApi
    public void setShopperReference(String shopperReference) {
        this.shopperReference = shopperReference;
    }

    @PublicApi
    public long getFraudOffset() {
        return fraudOffset;
    }

    @PublicApi
    public void setFraudOffset(int fraudOffset) {
        this.fraudOffset = fraudOffset;
    }

    @PublicApi
    public int getMcc() {
        return mcc;
    }

    @PublicApi
    public void setMcc(int mcc) {
        this.mcc = mcc;
    }

    @PublicApi
    public String getMerchantOrderReference() {
        return merchantOrderReference;
    }

    @PublicApi
    public void setMerchantOrderReference(String merchantOrderReference) {
        this.merchantOrderReference = merchantOrderReference;
    }

    @PublicApi
    public String getSelectedBrand() {
        return selectedBrand;
    }

    @PublicApi
    public void setSelectedBrand(String selectedBrand) {
        this.selectedBrand = selectedBrand;
    }

    @PublicApi
    public ShopperInteraction getShopperInteraction() {
        return shopperInteraction;
    }

    @PublicApi
    public void setShopperInteraction(ShopperInteraction shopperInteraction) {
        this.shopperInteraction = shopperInteraction;
    }

    @PublicApi
    public Card getCard() {
        return card;
    }

    @PublicApi
    public void setCard(Card card) {
        this.card = card;
    }

    @PublicApi
    public Map<String, String> getAdditionalData() {
        return additionalData;
    }

    @PublicApi
    public void setAdditionalData(Map<String, String> additionalData) {
        this.additionalData = additionalData;
    }

    @PublicApi
    public void addAdditionalDataEntry(String key, String value) {
        if (StringUtils.isNotBlank(key)) {
            additionalData.put(key, value);
        }
    }

    @PublicApi
    public BrowserInfo getBrowserInfo() {
        return browserInfo;
    }

    @PublicApi
    public void setBrowserInfo(BrowserInfo browserInfo) {
        this.browserInfo = browserInfo;
    }

    @PublicApi
    public Address getBillingAddress() {
        return billingAddress;
    }

    @PublicApi
    public void setBillingAddress(Address address) {
        this.billingAddress = address;
    }

    @PublicApi
    public Amount getAdditionalAmount() {
        return additionalAmount;
    }

    @PublicApi
    public Installments getInstallments() {
        return installments;
    }

    @PublicApi
    public void setInstallments(Installments installments) {
        this.installments = installments;
    }

    @PublicApi
    public void setAdditionalAmount(Amount additionalAmount) {
        this.additionalAmount = additionalAmount;
    }

    @PublicApi
    public BankAccount getBankAccount() {
        return bankAccount;
    }

    @PublicApi
    public void setBankAccount(BankAccount bankAccount) {
        this.bankAccount = bankAccount;
    }

    @PublicApi
    public String getShopperLocale() {
        return shopperLocale;
    }

    @PublicApi
    public void setShopperLocale(String shopperLocale) {
        this.shopperLocale = shopperLocale;
    }

    @PublicApi
    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    @PublicApi
    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    @PublicApi
    public Name getShopperName() {
        return shopperName;
    }

    @PublicApi
    public void setShopperName(Name shopperName) {
        this.shopperName = shopperName;
    }

    @PublicApi
    public String getDeliveryDate() {
        return deliveryDate;
    }

    @PublicApi
    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    @PublicApi
    public Address getDeliveryAddress() {
        return deliveryAddress;
    }

    @PublicApi
    public void setDeliveryAddress(Address deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    @PublicApi
    public String getShopperStatement() {
        return shopperStatement;
    }

    @PublicApi
    public void setShopperStatement(String shopperStatement) {
        this.shopperStatement = shopperStatement;
    }

    @PublicApi
    public String getSocialSecurityNumber() {
        return socialSecurityNumber;
    }

    @PublicApi
    public void setSocialSecurityNumber(String socialSecurityNumber) {
        this.socialSecurityNumber = socialSecurityNumber;
    }

    @PublicApi
    public int getCaptureDelayHours() {
        return captureDelayHours;
    }

    @PublicApi
    public void setCaptureDelayHours(int captureDelayHours) {
        this.captureDelayHours = captureDelayHours;
    }

    @PublicApi
    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    @PublicApi
    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    @PublicApi
    public String getDeviceFingerprint() {
        return deviceFingerprint;
    }

    @PublicApi
    public void setDeviceFingerprint(String deviceFingerprint) {
        this.deviceFingerprint = deviceFingerprint;
    }

    @PublicApi
    public ThreeDSecureData getMpiData() {
        return mpiData;
    }

    @PublicApi
    public void setMpiData(ThreeDSecureData mpiData) {
        this.mpiData = mpiData;
    }

    @PublicApi
    public Recurring getRecurring() {
        return recurring;
    }

    @PublicApi
    public void setRecurring(Recurring recurring) {
        this.recurring = recurring;
    }

    @PublicApi
    public ForexQuote getDccQuote() {
        return dccQuote;
    }

    @PublicApi
    public void setDccQuote(ForexQuote dccQuote) {
        this.dccQuote = dccQuote;
    }

    @PublicApi
    public String getSelectedRecurringDetailReference() {
        return selectedRecurringDetailReference;
    }

    @PublicApi
    public void setSelectedRecurringDetailReference(String selectedRecurringDetailReference) {
        this.selectedRecurringDetailReference = selectedRecurringDetailReference;
    }

    @PublicApi
    public String getSessionId() {
        return sessionId;
    }

    @PublicApi
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    @PublicApi
    public String getOrderReference() {
        return orderReference;
    }

    @PublicApi
    public void setOrderReference(String orderReference) {
        this.orderReference = orderReference;
    }

    @PublicApi
    public String getMd() {
        return md;
    }

    @PublicApi
    public void setMd(String md) {
        this.md = md;
    }

    @PublicApi
    public String getPaResponse() {
        return paResponse;
    }

    @PublicApi
    public void setPaResponse(String paResponse) {
        this.paResponse = paResponse;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).append("additionalAmount", additionalAmount).append("additionalData", additionalData)
                .append("amount", amount).append("bankAccount", bankAccount).append("billingAddress", billingAddress).append("browserInfo", browserInfo)
                .append("captureDelayHours", captureDelayHours).append("card", card).append("dateOfBirth", dateOfBirth).append("dccQuote", dccQuote)
                .append("deliveryAddress", deliveryAddress).append("deliveryDate", deliveryDate).append("deviceFingerprint", deviceFingerprint)
                .append("fraudOffset", fraudOffset).append("installments", installments).append("mcc", mcc).append("md", md)
                .append("merchantAccount", merchantAccount).append("merchantOrderReference", merchantOrderReference)
                .append("mpiData", mpiData).append("orderReference", orderReference).append("paResponse", paResponse).append("recurring", recurring)
                .append("reference", reference).append("selectedBrand", selectedBrand).append("selectedRecurringDetailReference", selectedRecurringDetailReference)
                .append("sessionId", sessionId).append("shopperEmail", shopperEmail).append("shopperIP", shopperIP).append("shopperInteraction", shopperInteraction)
                .append("shopperLocale", shopperLocale).append("shopperName", shopperName).append("shopperReference", shopperReference)
                .append("shopperStatement", shopperStatement).append("socialSecurityNumber", socialSecurityNumber).append("telephoneNumber", telephoneNumber).toString();
    }
}
