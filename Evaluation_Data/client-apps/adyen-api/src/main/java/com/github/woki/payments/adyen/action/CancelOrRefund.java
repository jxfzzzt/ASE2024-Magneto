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
package com.github.woki.payments.adyen.action;

import com.github.woki.payments.adyen.APService;
import com.github.woki.payments.adyen.ClientConfig;
import com.github.woki.payments.adyen.error.APSAccessException;
import com.github.woki.payments.adyen.model.ModificationRequest;
import com.github.woki.payments.adyen.model.ModificationResponse;
import org.apache.http.client.fluent.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;

/**
 * @author Willian Oki &lt;willian.oki@gmail.com&gt;
 */
public class CancelOrRefund {
    private static final Logger LOG = LoggerFactory.getLogger(CancelOrRefund.class);

    private static Request createRequest(ClientConfig config, ModificationRequest request) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("config: {}, request: {}", config, request);
        }
        Request retval = ActionUtil.createPost(APService.CANCEL_OR_REFUND, config, request);
        if (LOG.isDebugEnabled()) {
            LOG.debug("retval: {}", retval);
        }
        return retval;
    }

    public static ModificationResponse execute(@NotNull ClientConfig config, @NotNull ModificationRequest request) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("config: {}, request: {}", config, request);
        }
        ModificationResponse retval;
        try {
            retval = ActionUtil.executeModification(createRequest(config, request), config);
        } catch (Exception e) {
            LOG.error("cancelOrRefund", e);
            throw new APSAccessException("cancelOrRefund", e);
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("retval: {}", retval);
        }
        return retval;
    }
}
