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

import org.apache.http.HttpHost;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Willian Oki &lt;willian.oki@gmail.com&gt;
 */
public class ClientConfigTest {
    private static final String PROXY_CONFIG = "prxyusr:prxypass@prxyhost:8888";
    private static final String PROXY_CONFIG_2 = "62.63.4.10:8888";

    @Test
    public void testClientConfigCreation() throws Exception {
        ClientConfig config = new ClientConfig(APUtil.TEST_ENDPOINT);
        Assert.assertTrue(config.getEndpointHost().equals(HttpHost.create(APUtil.TEST_ENDPOINT)));
        Assert.assertTrue(config.getEndpointPort(APService.AUTHORISATION).equals(APUtil.TEST_ENDPOINT + APService.AUTHORISATION.getPath()));
        Assert.assertTrue(config.getEndpointPort(APService.AUTHORISATION_3D).equals(APUtil.TEST_ENDPOINT + APService.AUTHORISATION_3D.getPath()));
        Assert.assertTrue(config.getEndpointPort(APService.CANCEL).equals(APUtil.TEST_ENDPOINT + APService.CANCEL.getPath()));
        Assert.assertTrue(config.getEndpointPort(APService.CANCEL_OR_REFUND).equals(APUtil.TEST_ENDPOINT + APService.CANCEL_OR_REFUND.getPath()));
        Assert.assertTrue(config.getEndpointPort(APService.CAPTURE).equals(APUtil.TEST_ENDPOINT + APService.CAPTURE.getPath()));
        Assert.assertTrue(config.getEndpointPort(APService.REFUND).equals(APUtil.TEST_ENDPOINT + APService.REFUND.getPath()));
        Assert.assertTrue(config.getConnectionTimeout() == 0);
        Assert.assertTrue(config.getSocketTimeout() == 0);
        Assert.assertFalse(config.hasProxy());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testClientConfigCreationFailure1() throws Exception {
        ClientConfig config = new ClientConfig("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testClientConfigCreationFailure2() throws Exception {
        ClientConfig config = new ClientConfig(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testClientConfigCreationFailure3() throws Exception {
        ClientConfig config = new ClientConfig("blah-blah");
    }

    @Test
    public void testClientConfigProxyStuff() throws Exception {
        ClientConfig config = new ClientConfig(APUtil.TEST_ENDPOINT);
        config.setProxyConfig(PROXY_CONFIG);
        Assert.assertTrue(config.hasProxy());
        Assert.assertTrue(config.isProxyAuthenticated());
        Assert.assertTrue(config.getProxyUsername().equals("prxyusr"));
        Assert.assertTrue(config.getProxyPassword().equals("prxypass"));
        Assert.assertTrue(config.getProxyHost().equals(HttpHost.create("prxyhost:8888")));
    }

    @Test
    public void testClientConfigProxyStuff2() throws Exception {
        ClientConfig config = new ClientConfig(APUtil.TEST_ENDPOINT);
        config.setProxyConfig(PROXY_CONFIG_2);
        Assert.assertTrue(config.hasProxy());
        Assert.assertFalse(config.isProxyAuthenticated());
        Assert.assertTrue(config.getProxyUsername() == null);
        Assert.assertTrue(config.getProxyPassword() == null);
        Assert.assertTrue(config.getProxyHost().equals(HttpHost.create(PROXY_CONFIG_2)));
    }

    @Test
    public void testToString() {
        ClientConfig config = new ClientConfig(APUtil.TEST_ENDPOINT);
        config.setProxyConfig(PROXY_CONFIG);
        System.out.println(config);
    }
}
