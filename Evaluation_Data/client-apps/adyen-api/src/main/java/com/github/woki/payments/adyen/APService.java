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

/**
 * @author Willian Oki &lt;willian.oki@gmail.com&gt;
 */
public enum APService {
    AUTHORISATION("/pal/servlet/Payment/v18/authorise"),
    AUTHORISATION_3D("/pal/servlet/Payment/v18/authorise3d"),
    CAPTURE("/pal/servlet/Payment/v18/capture"),
    REFUND("/pal/servlet/Payment/v18/refund"),
    CANCEL("/pal/servlet/Payment/v18/cancel"),
    CANCEL_OR_REFUND("/pal/servlet/Payment/v18/cancelOrRefund")
    ;

    final String path;

    APService(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
