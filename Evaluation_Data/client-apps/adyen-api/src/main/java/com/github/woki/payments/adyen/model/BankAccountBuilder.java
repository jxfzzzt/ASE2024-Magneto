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
public final class BankAccountBuilder {
    private BankAccountBuilder() {
        // utility
    }

    @PublicApi
    public static ILocationId accountNumber(String accountNumber) {
        return new Builder(accountNumber);
    }

    @PublicApi
    public interface ILocationId {
        @PublicApi
        IBankName locationId(String locationId);
    }

    @PublicApi
    public interface IBankName {
        @PublicApi
        IBic bankName(String name);
    }

    @PublicApi
    public interface IBic {
        @PublicApi
        ICountryCode bic(String bic);
    }

    @PublicApi
    public interface ICountryCode {
        @PublicApi
        IIban countryCode(String countryCode);
    }

    @PublicApi
    public interface IIban {
        @PublicApi
        IOwnerName iban(String iban);
    }

    @PublicApi
    public interface IOwnerName {
        @PublicApi
        IBuilder owner(String owner);
    }

    @PublicApi
    public interface IBuilder {
        BankAccount build();
    }

    private static final class Builder implements IBuilder, ILocationId, IBankName, IBic, ICountryCode, IIban, IOwnerName {
        private BankAccount account;

        Builder(String accountNumber) {
            account = new BankAccount();
            account.setBankAccountNumber(accountNumber);
        }

        @Override
        public IBic bankName(String name) {
            account.setBankName(name);
            return this;
        }

        @Override
        public ICountryCode bic(String bic) {
            account.setBic(bic);
            return this;
        }

        @Override
        public BankAccount build() {
            return account;
        }

        @Override
        public IIban countryCode(String countryCode) {
            account.setCountryCode(countryCode);
            return this;
        }

        @Override
        public IOwnerName iban(String iban) {
            account.setIban(iban);
            return this;
        }

        @Override
        public IBankName locationId(String locationId) {
            account.setBankLocationId(locationId);
            return this;
        }

        @Override
        public IBuilder owner(String owner) {
            account.setOwnerName(owner);
            return this;
        }
    }
}
