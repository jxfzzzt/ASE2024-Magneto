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
public class ModificationResponse extends Error implements Serializable {
    private Map<String, String> additionalData = new HashMap<>();
    private String pspReference;
    private String response;

    @PublicApi
    public ModificationResponse() {
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
    public String getResponse() {
        return response;
    }

    @PublicApi
    public void setResponse(String response) {
        this.response = response;
    }

    @PublicApi
    public Map<String, String> getAdditionalData() {
        return additionalData;
    }

    @PublicApi
    public void setAdditionalData(Map<String, String> additionalData) {
        this.additionalData = additionalData;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).append("additionalData", additionalData).append("pspReference", pspReference)
                .append("response", response).toString();
    }
}
