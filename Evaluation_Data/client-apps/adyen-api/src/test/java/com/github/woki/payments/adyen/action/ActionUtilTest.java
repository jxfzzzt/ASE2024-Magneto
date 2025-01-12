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
package com.github.woki.payments.adyen.action;

import com.github.woki.payments.adyen.APService;
import com.github.woki.payments.adyen.APUtil;
import com.github.woki.payments.adyen.ClientConfig;
import com.github.woki.payments.adyen.model.*;
import io.advantageous.boon.json.JsonFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.util.Currency;

import static com.github.woki.payments.adyen.model.ShopperInteraction.Ecommerce;
import static io.advantageous.boon.core.Maps.map;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Willian Oki &lt;willian.oki@gmail.com&gt;
 */
public class ActionUtilTest {
    private PaymentResponse paymentResponse;
    private ModificationResponse modificationResponse;

    @Before
    public void setUp() {
        paymentResponse = new PaymentResponse();
        paymentResponse.setAdditionalData(map("test-ad-k1", "test-ad-v1", "test-ad-k2", "test-ad-v2"));
        paymentResponse.setAuthCode("test-authcode");
        paymentResponse.setDccAmount(new Amount(Currency.getInstance("USD"), 1000L));
        paymentResponse.setDccSignature("test-dcc-signature");
        FraudResult fr = new FraudResult();
        fr.setAccountScore("test-fr-score");
        paymentResponse.setFraudResult(fr);
        paymentResponse.setIssuerUrl("test-issuer-url");
        paymentResponse.setMd("test-md");
        paymentResponse.setPaRequest("test-pa-request");
        paymentResponse.setPspReference("tst-psp-reference");
        paymentResponse.setRefusalReason("test-refusal-reason");
        paymentResponse.setResultCode(ResultCode.Authorised);
        paymentResponse.setErrorCode(100);
        paymentResponse.setErrorType("test-error");
        paymentResponse.setMessage("test-message");
        paymentResponse.setStatus(101);
        modificationResponse = new ModificationResponse();
        modificationResponse.setAdditionalData(map("test-ad-k1", "test-ad-v1", "test-ad-k2", "test-ad-v2"));
        modificationResponse.setPspReference("tst-psp-reference");
        modificationResponse.setResponse("[test-modification-received]");
        modificationResponse.setErrorCode(100);
        modificationResponse.setMessage("test-message");
        modificationResponse.setStatus(101);
    }

    @Test
    public void testCreatePost() throws Exception {
        ClientConfig config = new ClientConfig(APUtil.TEST_ENDPOINT);
        Request request = ActionUtil.createPost(APService.AUTHORISATION, config, null);
        assertNotNull(request);
        request = ActionUtil.createPost(APService.AUTHORISATION, config, "hello");
        assertNotNull(request);
        PaymentRequest pr = PaymentRequestBuilder.merchantAccount("testAccount").amount(new Amount(Currency.getInstance("USD"), 10000L)).card(CardBuilder.number
                ("4111111111111111").cvc("123").expiry(2016, 6).holder("Johnny Tester").build()).reference("ref001").additionalDataEntry("returnUrl", "http://www.adyen.com")
                .shopper(NameBuilder.first("Johnny").last("Tester").build(), "willian.oki@gmail.com", "127.0.0.1", "Test/DAPI/Authorisation/PayULatam", Ecommerce).build();
        request = ActionUtil.createPost(APService.AUTHORISATION, config, pr);
        assertNotNull(request);
    }

    @Test
    public void testHandlePaymentResponse() throws Exception {
        HttpResponse response = new BasicHttpResponse(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, null), null, null);
        HttpEntity entity = new BasicHttpEntity();
        ((BasicHttpEntity) entity).setContent(new ByteArrayInputStream(JsonFactory.toJson(paymentResponse).getBytes()));
        response.setEntity(entity);
        PaymentResponse pr = ActionUtil.handlePaymentResponse(response);
        assertTrue(pr.getAuthCode().equals("test-authcode"));
        assertTrue(pr.getStatus() == 101);
        assertTrue(pr.getMessage().equals("test-message"));
        response = new BasicHttpResponse(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 400, null), null, null);
        entity = new BasicHttpEntity();
        ((BasicHttpEntity) entity).setContent(new ByteArrayInputStream(JsonFactory.toJson(paymentResponse).getBytes()));
        response.setEntity(entity);
        pr = ActionUtil.handlePaymentResponse(response);
        assertTrue(pr.getAuthCode().equals("test-authcode"));
        assertTrue(pr.getStatus() == 101);
        assertTrue(pr.getMessage().equals("test-message"));
        response = new BasicHttpResponse(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 422, null), null, null);
        entity = new BasicHttpEntity();
        ((BasicHttpEntity) entity).setContent(new ByteArrayInputStream(JsonFactory.toJson(paymentResponse).getBytes()));
        response.setEntity(entity);
        pr = ActionUtil.handlePaymentResponse(response);
        assertTrue(pr.getAuthCode().equals("test-authcode"));
        assertTrue(pr.getStatus() == 101);
        assertTrue(pr.getMessage().equals("test-message"));
        response = new BasicHttpResponse(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 401, null), null, null);
        entity = new BasicHttpEntity();
        ((BasicHttpEntity) entity).setContent(new ByteArrayInputStream(JsonFactory.toJson(paymentResponse).getBytes()));
        response.setEntity(entity);
        pr = ActionUtil.handlePaymentResponse(response);
        assertTrue(pr.getStatus() == 401);
        assertTrue(pr.getMessage().equals("Authentication required"));
        response = new BasicHttpResponse(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 403, null), null, null);
        entity = new BasicHttpEntity();
        ((BasicHttpEntity) entity).setContent(new ByteArrayInputStream(JsonFactory.toJson(paymentResponse).getBytes()));
        response.setEntity(entity);
        pr = ActionUtil.handlePaymentResponse(response);
        assertTrue(pr.getStatus() == 403);
        assertTrue(pr.getMessage().equals("Insufficient permission to process request"));
        response = new BasicHttpResponse(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 404, null), null, null);
        entity = new BasicHttpEntity();
        ((BasicHttpEntity) entity).setContent(new ByteArrayInputStream(JsonFactory.toJson(paymentResponse).getBytes()));
        response.setEntity(entity);
        pr = ActionUtil.handlePaymentResponse(response);
        assertTrue(pr.getStatus() == 404);
        assertTrue(pr.getMessage().equals("Service not found"));
        response = new BasicHttpResponse(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 500, null), null, null);
        entity = new BasicHttpEntity();
        ((BasicHttpEntity) entity).setContent(new ByteArrayInputStream(JsonFactory.toJson(paymentResponse).getBytes()));
        response.setEntity(entity);
        pr = ActionUtil.handlePaymentResponse(response);
        assertTrue(pr.getStatus() == 500);
        assertTrue(pr.getMessage().equals("Unexpected error"));
    }

    @Test
    public void testHandleModificationResponse() throws Exception {
        HttpResponse response = new BasicHttpResponse(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, null), null, null);
        HttpEntity entity = new BasicHttpEntity();
        ((BasicHttpEntity) entity).setContent(new ByteArrayInputStream(JsonFactory.toJson(modificationResponse).getBytes()));
        response.setEntity(entity);
        ModificationResponse mr = ActionUtil.handleModificationResponse(response);
        assertTrue(mr.getResponse().equals("[test-modification-received]"));
        assertTrue(mr.getStatus() == 101);
        assertTrue(mr.getMessage().equals("test-message"));
        response = new BasicHttpResponse(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 400, null), null, null);
        entity = new BasicHttpEntity();
        ((BasicHttpEntity) entity).setContent(new ByteArrayInputStream(JsonFactory.toJson(modificationResponse).getBytes()));
        response.setEntity(entity);
        mr = ActionUtil.handleModificationResponse(response);
        assertTrue(mr.getResponse().equals("[test-modification-received]"));
        assertTrue(mr.getStatus() == 101);
        assertTrue(mr.getMessage().equals("test-message"));
        response = new BasicHttpResponse(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 422, null), null, null);
        entity = new BasicHttpEntity();
        ((BasicHttpEntity) entity).setContent(new ByteArrayInputStream(JsonFactory.toJson(modificationResponse).getBytes()));
        response.setEntity(entity);
        mr = ActionUtil.handleModificationResponse(response);
        assertTrue(mr.getResponse().equals("[test-modification-received]"));
        assertTrue(mr.getStatus() == 101);
        assertTrue(mr.getMessage().equals("test-message"));
        response = new BasicHttpResponse(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 401, null), null, null);
        entity = new BasicHttpEntity();
        ((BasicHttpEntity) entity).setContent(new ByteArrayInputStream(JsonFactory.toJson(modificationResponse).getBytes()));
        response.setEntity(entity);
        mr = ActionUtil.handleModificationResponse(response);
        assertTrue(mr.getStatus() == 401);
        assertTrue(mr.getMessage().equals("Authentication required"));
        response = new BasicHttpResponse(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 403, null), null, null);
        entity = new BasicHttpEntity();
        ((BasicHttpEntity) entity).setContent(new ByteArrayInputStream(JsonFactory.toJson(modificationResponse).getBytes()));
        response.setEntity(entity);
        mr = ActionUtil.handleModificationResponse(response);
        assertTrue(mr.getStatus() == 403);
        assertTrue(mr.getMessage().equals("Insufficient permission to process request"));
        response = new BasicHttpResponse(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 404, null), null, null);
        entity = new BasicHttpEntity();
        ((BasicHttpEntity) entity).setContent(new ByteArrayInputStream(JsonFactory.toJson(modificationResponse).getBytes()));
        response.setEntity(entity);
        mr = ActionUtil.handleModificationResponse(response);
        assertTrue(mr.getStatus() == 404);
        assertTrue(mr.getMessage().equals("Service not found"));
        response = new BasicHttpResponse(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 500, null), null, null);
        entity = new BasicHttpEntity();
        ((BasicHttpEntity) entity).setContent(new ByteArrayInputStream(JsonFactory.toJson(modificationResponse).getBytes()));
        response.setEntity(entity);
        mr = ActionUtil.handleModificationResponse(response);
        assertTrue(mr.getStatus() == 500);
        assertTrue(mr.getMessage().equals("Unexpected error"));
    }
}