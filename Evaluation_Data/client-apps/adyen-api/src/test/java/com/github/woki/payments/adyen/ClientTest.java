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
package com.github.woki.payments.adyen;

import com.github.woki.payments.adyen.APUtil.ReferenceType;
import com.github.woki.payments.adyen.error.APSAccessException;
import com.github.woki.payments.adyen.model.*;
import org.junit.Test;

import java.util.Currency;

import static com.github.woki.payments.adyen.APUtil.reference;

/**
 * @author Willian Oki &lt;willian.oki@gmail.com&gt;
 */
public class ClientTest {
    private static final String PUBKEY_TEXT_ERR1 = "foo";
    private static final String PUBKEY_TEXT_ERR2 = "1|2";

    @Test(expected = APSAccessException.class)
    public void testClientCSEError() {
        Client client = Client
                .endpoint(APUtil.TEST_ENDPOINT)
                .credentials("merchant", "password")
                .encryptionKey(PUBKEY_TEXT_ERR1)
                .build();
        client.authorise(PaymentRequestBuilder
                .merchantAccount("mrchntacct")
                .amount(new Amount(Currency.getInstance("EUR"), 1000L))
                .card(CardBuilder
                        .number("4111111111111111")
                        .cvc("737")
                        .expiry(2016, 6)
                        .holder("Johnny Tester Visa")
                        .build())
                .reference(reference(ReferenceType.UUID))
                .shopper(NameBuilder
                        .first("Willian")
                        .last("Oki")
                        .build(), "willian.oki@gmail.com", "127.0.0.1", "Test/DAPI/Authorisation/Willian Oki", ShopperInteraction.Ecommerce)
                .build());
    }

    @Test(expected = APSAccessException.class)
    public void testClientCSEError2() {
        Client client = Client.endpoint(APUtil.TEST_ENDPOINT).credentials("merchant", "password").encryptionKey(PUBKEY_TEXT_ERR2).build();
        client.authorise(PaymentRequestBuilder.merchantAccount("mrchntacct").amount(new Amount(Currency.getInstance("EUR"), 1000L))
                .card(CardBuilder.number("4111111111111111").cvc("737").expiry(2016, 6).holder("Johnny Tester Visa").build())
                .reference(reference(ReferenceType.UUID)).shopper(NameBuilder.first("Willian").last("Oki").build(),
                        "willian.oki@gmail.com", "127.0.0.1", "Test/DAPI/Authorisation/Willian Oki", ShopperInteraction.Ecommerce).build());
    }
}
