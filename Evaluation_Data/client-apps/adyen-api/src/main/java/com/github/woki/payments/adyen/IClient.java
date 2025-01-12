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


import com.github.woki.payments.adyen.error.APSAccessException;
import com.github.woki.payments.adyen.model.ModificationRequest;
import com.github.woki.payments.adyen.model.ModificationResponse;
import com.github.woki.payments.adyen.model.PaymentRequest;
import com.github.woki.payments.adyen.model.PaymentResponse;

/**
 * @author Willian Oki &lt;willian.oki@gmail.com&gt;
 */
@PublicApi
public interface IClient {
    /**
     * @return the {@link ClientConfig}
     */
    @PublicApi
    ClientConfig getClientConfig();
    /**
     * @param request the request
     *
     * @return the response
     *
     * @throws APSAccessException on communication error
     */
    @PublicApi
    PaymentResponse authorise(final PaymentRequest request);
    /**
     * @param request the request
     *
     * @return the response
     *
     * @throws APSAccessException on communication error
     */
    @PublicApi
    PaymentResponse authorise3ds(final PaymentRequest request);
    /**
     * @param request the request
     *
     * @return the response
     *
     * @throws APSAccessException on communication error
     */
    @PublicApi
    PaymentResponse verifyBin(final PaymentRequest request);
    /**
     * @param request the request
     *
     * @return the response
     *
     * @throws APSAccessException on communication error
     */
    @PublicApi
    ModificationResponse capture(ModificationRequest request);
    /**
     * @param request the request
     *
     * @return the response
     *
     * @throws APSAccessException on communication error
     */
    @PublicApi
    ModificationResponse cancel(ModificationRequest request);
    /**
     * @param request the request
     *
     * @return the response
     *
     * @throws APSAccessException on communication error
     */
    @PublicApi
    ModificationResponse refund(ModificationRequest request);
    /**
     * @param request the request
     *
     * @return the response
     *
     * @throws APSAccessException on communication error
     */
    @PublicApi
    ModificationResponse cancelOrRefund(ModificationRequest request);
}
