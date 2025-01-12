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
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Willian Oki &lt;willian.oki@gmail.com&gt;
 */
@SuppressWarnings("serial")
@PublicApi
public class PaymentResponse extends Error implements Serializable {
    private Map<String, String> additionalData = new HashMap<>();
    private String authCode;
    private Amount dccAmount;
    private String dccSignature;
    private FraudResult fraudResult;
    private String issuerUrl;
    private String md;
    private String paRequest;
    private String pspReference;
    private String refusalReason;
    private ResultCode resultCode;

    @PublicApi
    public PaymentResponse() {
    }

    @PublicApi
    public String getPspReference() {
        return pspReference;
    }

    @PublicApi
    public void setPspReference(String pspReference) {
        this.pspReference = pspReference;
    }

    @PublicApi
    public ResultCode getResultCode() {
        return resultCode;
    }

    @PublicApi
    public void setResultCode(ResultCode resultCode) {
        this.resultCode = resultCode;
    }

    @PublicApi
    public String getAuthCode() {
        return authCode;
    }

    @PublicApi
    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    @PublicApi
    public String getRefusalReason() {
        return refusalReason;
    }

    @PublicApi
    public void setRefusalReason(String refusalReason) {
        this.refusalReason = refusalReason;
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
    public String getPaRequest() {
        return paRequest;
    }

    @PublicApi
    public void setPaRequest(String paRequest) {
        this.paRequest = paRequest;
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
    public Amount getDccAmount() {
        return dccAmount;
    }

    @PublicApi
    public void setDccAmount(Amount dccAmount) {
        this.dccAmount = dccAmount;
    }

    @PublicApi
    public String getDccSignature() {
        return dccSignature;
    }

    @PublicApi
    public void setDccSignature(String dccSignature) {
        this.dccSignature = dccSignature;
    }

    @PublicApi
    public FraudResult getFraudResult() {
        return fraudResult;
    }

    @PublicApi
    public void setFraudResult(FraudResult fraudResult) {
        this.fraudResult = fraudResult;
    }

    @PublicApi
    public String getIssuerUrl() {
        return issuerUrl;
    }

    @PublicApi
    public void setIssuerUrl(String issuerUrl) {
        this.issuerUrl = issuerUrl;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).append("additionalData", additionalData).append("authCode", authCode).append("dccAmount", dccAmount)
                .append("dccSignature", dccSignature).append("fraudResult", fraudResult).append("issuerUrl", issuerUrl).append("md", md)
                .append("paRequest", paRequest).append("pspReference", pspReference).append("refusalReason", refusalReason).append("resultCode", resultCode).toString();
    }
}
