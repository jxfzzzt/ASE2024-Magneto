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
import com.neovisionaries.i18n.CountryCode;

/**
 * @author Willian Oki &lt;willian.oki@gmail.com&gt;
 */
@PublicApi
public final class AddressBuilder {
    private AddressBuilder() {
        // utility
    }

    @PublicApi
    public static INumber street(String street) {
        return new Builder(street);
    }

    @PublicApi
    public interface INumber {
        @PublicApi
        IPostalCode numberOrName(String number);
    }

    @PublicApi
    public interface IPostalCode {
        @PublicApi
        ICity postalCode(String postalCode);
    }

    @PublicApi
    public interface ICity {
        @PublicApi
        IState city(String city);
    }

    @PublicApi
    public interface IState {
        @PublicApi
        ICountry state(String state);
    }

    @PublicApi
    public interface ICountry {
        @PublicApi
        IBuilder country(CountryCode country);
    }

    @PublicApi
    public interface IBuilder {
        Address build();
    }

    private static final class Builder implements IBuilder, INumber, IPostalCode, ICity, IState, ICountry {
        private Address address;

        Builder(String street) {
            address = new Address();
            address.setStreet(street);
        }

        @Override
        public Address build() {
            return address;
        }

        @Override
        public IBuilder country(CountryCode country) {
            address.setCountry(country);
            return this;
        }

        @Override
        public ICountry state(String state) {
            address.setStateOrProvince(state);
            return this;
        }

        @Override
        public IState city(String city) {
            address.setCity(city);
            return this;
        }

        @Override
        public IPostalCode numberOrName(String number) {
            address.setHouseNumberOrName(number);
            return this;
        }

        @Override
        public ICity postalCode(String postalCode) {
            address.setPostalCode(postalCode);
            return this;
        }
    }
}
