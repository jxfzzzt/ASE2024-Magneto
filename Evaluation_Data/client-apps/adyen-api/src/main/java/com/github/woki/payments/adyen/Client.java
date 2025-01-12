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

import com.github.woki.payments.adyen.action.*;
import com.github.woki.payments.adyen.model.ModificationRequest;
import com.github.woki.payments.adyen.model.ModificationResponse;
import com.github.woki.payments.adyen.model.PaymentRequest;
import com.github.woki.payments.adyen.model.PaymentResponse;

import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * @author Willian Oki &lt;willian.oki@gmail.com&gt;
 */
@PublicApi
public final class Client implements IClient {
    private ClientConfig config;

    private Client() {
        // disable default constructor
    }

    @PublicApi
    public interface IBuilder {
        @PublicApi
        IBuilder timeout(long connectionTimeout, long readTimeout);

        @PublicApi
        IBuilder connectionTimeout(long timeout);

        @PublicApi
        IBuilder readTimeout(long timeout);

        @PublicApi
        IBuilder extraParameters(Map<String, String> extraParameters);

        @PublicApi
        IBuilder proxyConfig(String config);

        @PublicApi
        IBuilder encryptionKey(String encryptionKey);

        @PublicApi
        IBuilder addExtraParameter(String key, String value);

        Client build();
    }

    @PublicApi
    public static IAccount endpoint(String endpoint) {
        return new Builder(endpoint);
    }

    @PublicApi
    public interface IAccount {
        @PublicApi
        IBuilder credentials(String username, String password);
    }

    private final static class Builder implements IAccount, IBuilder {
        private Client instance = new Client();

        @PublicApi
        private Builder() {
            // disable default constructor
        }

        Builder(String endpoint) {
            instance.config = new ClientConfig(endpoint);
        }

        @Override
        public IBuilder timeout(long connectionTimeout, long readTimeout) {
            instance.config.setConnectionTimeout((int) connectionTimeout);
            instance.config.setSocketTimeout((int) readTimeout);
            return this;
        }

        @Override
        public IBuilder connectionTimeout(long timeout) {
            instance.config.setConnectionTimeout((int) timeout);
            return this;
        }

        @Override
        public IBuilder readTimeout(long timeout) {
            instance.config.setSocketTimeout((int) timeout);
            return this;
        }

        @Override
        public IBuilder extraParameters(Map<String, String> extraParameters) {
            instance.config.setExtraParameters(extraParameters);
            return this;
        }

        @Override
        public IBuilder proxyConfig(String config) {
            instance.config.setProxyConfig(config);
            return this;
        }

        @Override
        public IBuilder encryptionKey(String encryptionKey) {
            instance.config.setEncryptionKey(encryptionKey);
            return this;
        }

        @Override
        public IBuilder addExtraParameter(String key, String value) {
            instance.config.addExtraParameter(key, value);
            return this;
        }

        @Override
        public Client build() {
            return instance;
        }

        @Override
        public IBuilder credentials(String username, String password) {
            instance.config.setUsername(username);
            instance.config.setPassword(password);
            return this;
        }
    }

    @Override
    public ClientConfig getClientConfig() {
        return config;
    }

    @Override
    public PaymentResponse authorise(@NotNull PaymentRequest request) {
        return Authorise.execute(config, request, false);
    }

    @Override
    public PaymentResponse authorise3ds(@NotNull PaymentRequest request) {
        return Authorise.execute(config, request, true);
    }

    @Override
    public PaymentResponse verifyBin(@NotNull PaymentRequest request) {
        return Authorise.execute(config, request, false);
    }

    @Override
    public ModificationResponse capture(@NotNull ModificationRequest request) {
        return Capture.execute(config, request);
    }

    @Override
    public ModificationResponse cancel(@NotNull ModificationRequest request) {
        return Cancel.execute(config, request);
    }

    @Override
    public ModificationResponse refund(@NotNull ModificationRequest request) {
        return Refund.execute(config, request);
    }

    @Override
    public ModificationResponse cancelOrRefund(@NotNull ModificationRequest request) {
        return CancelOrRefund.execute(config, request);
    }
}
