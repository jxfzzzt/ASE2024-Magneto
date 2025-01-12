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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * @author Willian Oki &lt;willian.oki@gmail.com&gt;
 */
@PublicApi
public final class PaymentRequestBuilder {
    private static final DateFormat DELIVERY_DATE_FMTR = new SimpleDateFormat("yyyy-MM-dd'T'00:00:00.000'Z'");

    private PaymentRequestBuilder() {
        // utility
    }

    @PublicApi
    public static IAmount merchantAccount(String account) {
        return new Builder(account);
    }

    @PublicApi
    public interface IAmount {
        @PublicApi
        IBuilder amount(Amount amount);
    }

    @PublicApi
    public interface IBuilder {
        @PublicApi
        IBuilder additionalAmount(Amount amount);

        @PublicApi
        IBuilder additionalDataEntry(String key, String value);

        @PublicApi
        IBuilder additionalData(Map<String, String> fields);

        @PublicApi
        IBuilder bankAccount(BankAccount bankAccount);

        @PublicApi
        IBuilder billingAddress(Address address);

        @PublicApi
        IBuilder browserInfo(BrowserInfo info);

        @PublicApi
        IBuilder browserInfo(String userAgent, String acceptHeader);

        @PublicApi
        IBuilder captureDelayHours(int captureDelayHours);

        @PublicApi
        IBuilder card(Card card);

        @PublicApi
        IBuilder dccQuote(ForexQuote dccQuote);

        @PublicApi
        IBuilder deliveryAddress(Address deliveryAddress);

        @PublicApi
        IBuilder deliveryDate(Date date);

        @PublicApi
        IBuilder deviceFingerprint(String deviceFingerprint);

        @PublicApi
        IBuilder fraudOffset(int fraudOffset);

        @PublicApi
        IBuilder installments(int value);

        @PublicApi
        IBuilder mcc(int mcc);

        @PublicApi
        IBuilder merchantOrderReference(String reference);

        @PublicApi
        IBuilder mpiData(ThreeDSecureData mpiData);

        @PublicApi
        IBuilder orderReference(String orderReference);

        @PublicApi
        IBuilder recurring(Recurring recurring);

        @PublicApi
        IBuilder reference(String reference);

        @PublicApi
        IBuilder selectedBrand(String brand);

        @PublicApi
        IBuilder selectedRecurringDetailReference(String selectedRecurringDetailReference);

        @PublicApi
        IBuilder sessionId(String sessionId);

        @PublicApi
        IBuilder shopperDateOfBirth(Date dateOfBirth);

        @PublicApi
        IBuilder shopperEmail(String email);

        @PublicApi
        IBuilder shopperIP(String ip);

        @PublicApi
        IBuilder shopperInteraction(ShopperInteraction interaction);

        @PublicApi
        IBuilder shopperLocale(String shopperLocale);

        @PublicApi
        IBuilder shopperReference(String shopperReference);

        @PublicApi
        IBuilder shopperName(Name name);

        @PublicApi
        IBuilder shopperStatement(String shopperStatement);

        @PublicApi
        IBuilder shopperSsn(String shopperSsn);

        @PublicApi
        IBuilder shopperTelephoneNumber(String shopperTelephoneNumber);

        @PublicApi
        IBuilder shopper(Name name, String email, String ip, String reference, ShopperInteraction interaction);

        @PublicApi
        IBuilder shopper(Name name, Date birth, String email, String ip, String reference, String ssn, String telephone, ShopperInteraction interaction, String locale, String
                statement);

        @PublicApi
        IBuilder md(String md);

        @PublicApi
        IBuilder paResponse(String paResponse);
        PaymentRequest build();
    }

    private static final class Builder implements IAmount, IBuilder {
        private PaymentRequest request;

        Builder(String merchantAccount) {
            request = new PaymentRequest();
            request.setMerchantAccount(merchantAccount);
        }

        @Override
        public IBuilder card(Card card) {
            request.setCard(card);
            return this;
        }

        @Override
        public IBuilder shopperDateOfBirth(Date dateOfBirth) {
            request.setDateOfBirth(dateOfBirth);
            return this;
        }

        @Override
        public IBuilder dccQuote(ForexQuote dccQuote) {
            request.setDccQuote(dccQuote);
            return this;
        }

        @Override
        public IBuilder deliveryAddress(Address deliveryAddress) {
            request.setDeliveryAddress(deliveryAddress);
            return this;
        }

        @Override
        public IBuilder reference(String reference) {
            request.setReference(reference);
            return this;
        }

        @Override
        public IBuilder shopperEmail(String email) {
            request.setShopperEmail(email);
            return this;
        }

        @Override
        public IBuilder shopperIP(String ip) {
            request.setShopperIP(ip);
            return this;
        }

        @Override
        public IBuilder shopperReference(String reference) {
            request.setShopperReference(reference);
            return this;
        }

        @Override
        public IBuilder shopperInteraction(ShopperInteraction interaction) {
            request.setShopperInteraction(interaction);
            return this;
        }

        @Override
        public IBuilder shopperLocale(String shopperLocale) {
            request.setShopperLocale(shopperLocale);
            return null;
        }

        @Override
        public IBuilder amount(Amount amount) {
            request.setAmount(amount);
            return this;
        }

        @Override
        public PaymentRequest build() {
            return request;
        }

        @Override
        public IBuilder fraudOffset(int offset) {
            request.setFraudOffset(offset);
            return this;
        }

        @Override
        public IBuilder mcc(int mcc) {
            request.setMcc(mcc);
            return this;
        }

        @Override
        public IBuilder merchantOrderReference(String reference) {
            request.setMerchantOrderReference(reference);
            return this;
        }

        @Override
        public IBuilder mpiData(ThreeDSecureData mpiData) {
            request.setMpiData(mpiData);
            return this;
        }

        @Override
        public IBuilder orderReference(String orderReference) {
            request.setOrderReference(orderReference);
            return this;
        }

        @Override
        public IBuilder recurring(Recurring recurring) {
            request.setRecurring(recurring);
            return this;
        }

        @Override
        public IBuilder selectedBrand(String brand) {
            request.setSelectedBrand(brand);
            return this;
        }

        @Override
        public IBuilder selectedRecurringDetailReference(String selectedRecurringDetailReference) {
            request.setSelectedRecurringDetailReference(selectedRecurringDetailReference);
            return this;
        }

        @Override
        public IBuilder sessionId(String sessionId) {
            request.setSessionId(sessionId);
            return this;
        }

        @Override
        public IBuilder additionalDataEntry(String key, String value) {
            request.addAdditionalDataEntry(key, value);
            return this;
        }

        @Override
        public IBuilder additionalData(Map<String, String> fields) {
            request.setAdditionalData(fields);
            return this;
        }

        @Override
        public IBuilder bankAccount(BankAccount bankAccount) {
            request.setBankAccount(bankAccount);
            return this;
        }

        @Override
        public IBuilder browserInfo(String userAgent, String acceptHeader) {
            request.setBrowserInfo(new BrowserInfo(userAgent, acceptHeader));
            return this;
        }

        @Override
        public IBuilder captureDelayHours(int captureDelayHours) {
            request.setCaptureDelayHours(captureDelayHours);
            return this;
        }

        @Override
        public IBuilder browserInfo(BrowserInfo info) {
            request.setBrowserInfo(info);
            return this;
        }

        @Override
        public IBuilder billingAddress(Address address) {
            request.setBillingAddress(address);
            return this;
        }

        @Override
        public IBuilder additionalAmount(Amount amount) {
            request.setAdditionalAmount(amount);
            return this;
        }

        @Override
        public IBuilder installments(int value) {
            request.setInstallments(new Installments(value));
            return this;
        }

        @Override
        public IBuilder shopperName(Name name) {
            request.setShopperName(name);
            return this;
        }

        @Override
        public IBuilder deliveryDate(Date date) {
            request.setDeliveryDate(DELIVERY_DATE_FMTR.format(date));
            return this;
        }

        @Override
        public IBuilder deviceFingerprint(String deviceFingerprint) {
            request.setDeviceFingerprint(deviceFingerprint);
            return this;
        }

        @Override
        public IBuilder shopperStatement(String shopperStatement) {
            request.setShopperStatement(shopperStatement);
            return this;
        }

        @Override
        public IBuilder shopperSsn(String shopperSsn) {
            request.setSocialSecurityNumber(shopperSsn);
            return this;
        }

        @Override
        public IBuilder shopperTelephoneNumber(String shopperTelephoneNumber) {
            request.setTelephoneNumber(shopperTelephoneNumber);
            return this;
        }

        @Override
        public IBuilder shopper(Name name, String email, String ip, String reference, ShopperInteraction interaction) {
            return shopper(name, null, email, ip, reference, null, null, interaction, null, null);
        }

        @Override
        public IBuilder shopper(Name name, Date birth, String email, String ip, String reference, String ssn, String telephone, ShopperInteraction interaction, String locale,
                                String statement) {
            request.setShopperName(name);
            request.setDateOfBirth(birth);
            request.setShopperEmail(email);
            request.setShopperIP(ip);
            request.setShopperReference(reference);
            request.setSocialSecurityNumber(ssn);
            request.setTelephoneNumber(telephone);
            request.setShopperInteraction(interaction);
            request.setShopperLocale(locale);
            request.setShopperStatement(statement);
            return this;
        }

        @Override
        public IBuilder md(String md) {
            request.setMd(md);
            return this;
        }

        @Override
        public IBuilder paResponse(String paResponse) {
            request.setPaResponse(paResponse);
            return this;
        }
    }
}
