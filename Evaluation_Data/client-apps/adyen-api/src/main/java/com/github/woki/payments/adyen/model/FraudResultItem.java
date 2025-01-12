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

/**
 * @author Willian Oki &lt;willian.oki@gmail.com&gt;
 */
@SuppressWarnings("serial")
@PublicApi
public class FraudResultItem implements Serializable {
    private String accountScore;
    private String checkId;
    private String name;

    @PublicApi
    public FraudResultItem() {
    }

    public FraudResultItem(String name, String checkId, String accountScore) {
        this.accountScore = accountScore;
        this.checkId = checkId;
        this.name = name;
    }

    @PublicApi
    public String getAccountScore() {
        return accountScore;
    }

    @PublicApi
    public void setAccountScore(String accountScore) {
        this.accountScore = accountScore;
    }

    @PublicApi
    public String getCheckId() {
        return checkId;
    }

    @PublicApi
    public void setCheckId(String checkId) {
        this.checkId = checkId;
    }

    @PublicApi
    public String getName() {
        return name;
    }

    @PublicApi
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).append("accountScore", accountScore).append("checkId", checkId).append("name", name).toString();
    }
}
