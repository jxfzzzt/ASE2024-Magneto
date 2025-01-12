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
public class Error implements Serializable {
    private int status;
    private int errorCode;
    private String message;
    private String errorType;

    @PublicApi
    public Error() {
    }

    @PublicApi
    public Error(int status, int errorCode, String message, String errorType) {
        this.status = status;
        this.errorCode = errorCode;
        this.message = message;
        this.errorType = errorType;
    }

    @PublicApi
    public int getStatus() {
        return status;
    }

    @PublicApi
    public void setStatus(int status) {
        this.status = status;
    }

    @PublicApi
    public int getErrorCode() {
        return errorCode;
    }

    @PublicApi
    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    @PublicApi
    public String getMessage() {
        return message;
    }

    @PublicApi
    public void setMessage(String message) {
        this.message = message;
    }

    @PublicApi
    public String getErrorType() {
        return errorType;
    }

    @PublicApi
    public void setErrorType(String errorType) {
        this.errorType = errorType;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).append("status", status).append("errorCode", errorCode).append("message", message)
                .append("errorType", errorType).toString();
    }
}
