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
import com.neovisionaries.i18n.CountryCode;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * @author Willian Oki &lt;willian.oki@gmail.com&gt;
 */
@SuppressWarnings("serial")
@PublicApi
public class Address implements Serializable {
    private String street;
    private String houseNumberOrName;
    private String postalCode;
    private String city;
    private String stateOrProvince;
    private CountryCode country;

    @PublicApi
    public Address() {
    }

    @PublicApi
    public String getStreet() {
        return street;
    }

    @PublicApi
    public void setStreet(String street) {
        this.street = street;
    }

    @PublicApi
    public String getHouseNumberOrName() {
        return houseNumberOrName;
    }

    @PublicApi
    public void setHouseNumberOrName(String houseNumberOrName) {
        this.houseNumberOrName = houseNumberOrName;
    }

    @PublicApi
    public String getPostalCode() {
        return postalCode;
    }

    @PublicApi
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    @PublicApi
    public String getCity() {
        return city;
    }

    @PublicApi
    public void setCity(String city) {
        this.city = city;
    }

    @PublicApi
    public String getStateOrProvince() {
        return stateOrProvince;
    }

    @PublicApi
    public void setStateOrProvince(String stateOrProvince) {
        this.stateOrProvince = stateOrProvince;
    }

    @PublicApi
    public CountryCode getCountry() {
        return country;
    }

    @PublicApi
    public void setCountry(CountryCode country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).append("street", street).append("houseNumberOrName", houseNumberOrName).append("postalCode", postalCode)
                .append("city", city).append("stateOrProvince", stateOrProvince).append("country", country).toString();
    }
}
