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

import java.util.Date;
import java.util.UUID;

/**
 * @author Willian Oki &lt;willian.oki@gmail.com&gt;
 */
@PublicApi
public final class APUtil {
    private APUtil() {
        // utility
    }

    @PublicApi
    public static final String TEST_ENDPOINT = "https://pal-test.adyen.com";
    @PublicApi
    public static final String LIVE_ENDPOINT = "https://pal-live.adyen.com";

    @PublicApi
    public enum ReferenceType {
        DATE, TIMESTAMP, UUID
    }

    @PublicApi
    public static String reference(ReferenceType type) {
        String retval = null;
        switch (type) {
            case TIMESTAMP:
                retval = String.valueOf(System.currentTimeMillis());
                break;
            case UUID:
                retval = UUID.randomUUID().toString();
                break;
            case DATE:
                retval = new Date().toString();
                break;
        }
        return retval;
    }
}
