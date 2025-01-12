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
public class ModificationRequest implements Serializable {
    private Map<String, String> additionalData = new HashMap<>();
    private String authorisationCode;
    private String merchantAccount;
    private Amount modificationAmount;
    private String originalReference;
    private String reference;

    @PublicApi
    public ModificationRequest() {
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
    public String getOriginalReference() {
        return originalReference;
    }

    @PublicApi
    public void setOriginalReference(String originalReference) {
        this.originalReference = originalReference;
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
    public Map<String, String> getAdditionalData() {
        return additionalData;
    }

    @PublicApi
    public void setAdditionalData(Map<String, String> additionalData) {
        this.additionalData = additionalData;
    }

    @PublicApi
    public String getAuthorisationCode() {
        return authorisationCode;
    }

    @PublicApi
    public void setAuthorisationCode(String authorisationCode) {
        this.authorisationCode = authorisationCode;
    }

    @PublicApi
    public Amount getModificationAmount() {
        return modificationAmount;
    }

    @PublicApi
    public void setModificationAmount(Amount modificationAmount) {
        this.modificationAmount = modificationAmount;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).append("additionalData", additionalData).append("authorisationCode", authorisationCode)
                .append("merchantAccount", merchantAccount).append("modificationAmount", modificationAmount).append("originalReference", originalReference)
                .append("reference", reference).toString();
    }
}
