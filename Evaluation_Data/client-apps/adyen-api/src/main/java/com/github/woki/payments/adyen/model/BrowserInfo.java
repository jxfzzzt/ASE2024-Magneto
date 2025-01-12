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
public class BrowserInfo implements Serializable {
    private String userAgent;
    private String acceptHeader;

    @PublicApi
    public BrowserInfo() {
    }

    @PublicApi
    public BrowserInfo(String userAgent, String acceptHeader) {
        this.userAgent = userAgent;
        this.acceptHeader = acceptHeader;
    }

    @PublicApi
    public String getUserAgent() {
        return userAgent;
    }

    @PublicApi
    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    @PublicApi
    public String getAcceptHeader() {
        return acceptHeader;
    }

    @PublicApi
    public void setAcceptHeader(String acceptHeader) {
        this.acceptHeader = acceptHeader;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).append("userAgent", userAgent).append("acceptHeader", acceptHeader).toString();
    }
}
