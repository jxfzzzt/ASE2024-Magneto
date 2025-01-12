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

/**
 * @author Willian Oki &lt;willian.oki@gmail.com&gt;
 */
@PublicApi
public final class ModificationRequestBuilder {
    private ModificationRequestBuilder() {
        // utility
    }

    @PublicApi
    public static IOriginalReference merchantAccount(String account) {
        return new Builder(account);
    }

    @PublicApi
    public interface IOriginalReference {
        @PublicApi
        IBuilder originalReference(String reference);
    }

    @PublicApi
    public interface IBuilder {
        @PublicApi
        IBuilder additionalData(String key, String value);
        @PublicApi
        IBuilder authorisationCode(String code);
        @PublicApi
        IBuilder reference(String reference);
        @PublicApi
        IBuilder modificationAmount(Amount amount);
        ModificationRequest build();
    }

    private static final class Builder implements IOriginalReference, IBuilder {
        private ModificationRequest request;

        Builder(String merchantAccount) {
            request = new ModificationRequest();
            request.setMerchantAccount(merchantAccount);
        }

        @Override
        public IBuilder reference(String reference) {
            request.setReference(reference);
            return this;
        }

        @Override
        public ModificationRequest build() {
            return request;
        }

        @Override
        public IBuilder originalReference(String reference) {
            request.setOriginalReference(reference);
            return this;
        }

        @Override
        public IBuilder modificationAmount(Amount amount) {
            request.setModificationAmount(amount);
            return this;
        }

        @Override
        public IBuilder authorisationCode(String code) {
            request.setAuthorisationCode(code);
            return this;
        }

        @Override
        public IBuilder additionalData(String key, String value) {
            request.getAdditionalData().put(key, value);
            return this;
        }
    }
}
