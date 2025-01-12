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

import com.github.woki.payments.adyen.action.CSEUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.http.HttpHost;
import org.apache.http.client.utils.URIUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Willian Oki &lt;willian.oki@gmail.com&gt;
 */
public class ClientConfig {
    private HttpHost endpointHost;
    private int connectionTimeout;
    private int socketTimeout;
    private String proxyConfig;
    private HttpHost proxyHost;
    private String endpoint;
    private String username;
    private String password;
    private String proxyUsername, proxyPassword;
    private Map<String, String> extraParameters = new HashMap<>();
    private String encryptionKey;
    private Cipher aesCipher;
    private Cipher rsaCipher;

    private static final Logger LOG = LoggerFactory.getLogger(ClientConfig.class);
    private final static Pattern PROXY_CONFIG_PATTERN = Pattern.compile("(.*):(.*)@([a-zA-Z0-9\\.:]+):(\\d+)|([a-zA-Z0-9\\.:]+):(\\d+)");

    /**
     * Constructor
     *
     * @param endpoint the endpoint; {@link APUtil#TEST_ENDPOINT} / {@link APUtil#LIVE_ENDPOINT}
     *
     * @throws IllegalArgumentException on invalid URI
     */
    public ClientConfig(String endpoint) {
        if (StringUtils.isBlank(endpoint)) {
            throw new IllegalArgumentException("Invalid endpoint: " + endpoint);
        }
        endpointHost = URIUtils.extractHost(URI.create(endpoint));
        if (endpointHost == null) {
            throw new IllegalArgumentException("Invalid endpoint: " + endpoint);
        }
        this.endpoint = endpoint;
    }

    /**
     * connectionTimeout (millisecs) see http.connection.timeout (httpclient). 0 (default) means no timeout (blocking).
     *
     * @return the connection timeout
     */
    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    /**
     * readTimeout (millisecs) see http.socket.timeout (httpclient). 0 (default) means no timeout (blocking).
     *
     * @return the read timeout
     */
    public int getSocketTimeout() {
        return socketTimeout;
    }

    void setSocketTimeout(int socketTimeout) {
        this.socketTimeout = socketTimeout;
    }

    public String getEndpointPort(APService service) {
        return endpoint + service.getPath();
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return extra parameters map
     */
    public Map<String, String> getExtraParameters() {
        return extraParameters;
    }

    /**
     * @param extraParameters the extra parameters map to set
     */
    void setExtraParameters(Map<String, String> extraParameters) {
        this.extraParameters = extraParameters;
    }

    /**
     * @param key the extra parameter key
     * @param value the extra parameter value
     */
    void addExtraParameter(String key, String value) {
        extraParameters.put(key, value);
    }

    /**
     * Proxy's Hostname/IP, port and credentials formatted as follow:
     * <p>
     *     [user:password@]host:port<br/>
     *     E.g.: prxyusr:prxypass@proxy:8888, prxyusr:prxypass@127.0.0.1:8888, proxy:8888, ...
     * </p>
     *
     * @param proxyConfig the specification
     */
    void setProxyConfig(String proxyConfig) {
        this.proxyConfig = proxyConfig;
    }

    public HttpHost getEndpointHost() {
        return endpointHost;
    }

    public boolean hasProxy() {
        return getProxyHost() != null;
    }

    public HttpHost getProxyHost() {
        if (proxyHost == null && proxyConfig != null) {
            Matcher matcher = PROXY_CONFIG_PATTERN.matcher(proxyConfig);
            if (matcher.matches() && matcher.groupCount() == 6) {
                if (matcher.group(1) == null) {
                    proxyHost = HttpHost.create(matcher.group(5) + ":" + matcher.group(6));
                } else {
                    proxyUsername = matcher.group(1);
                    proxyPassword = matcher.group(2);
                    proxyHost = HttpHost.create(matcher.group(3) + ":" + matcher.group(4));
                }
            }
        }
        return proxyHost;
    }

    public boolean isProxyAuthenticated() {
        return proxyUsername != null;
    }

    public String getProxyUsername() {
        return hasProxy() ? proxyUsername : null;
    }

    public String getProxyPassword() {
        return hasProxy() ? proxyPassword : null;
    }

    public String getEncryptionKey() {
        return encryptionKey;
    }

    void setEncryptionKey(String encryptionKey) {
        this.encryptionKey = encryptionKey;
    }

    public Cipher getAesCipher() {
        if (StringUtils.isNotBlank(encryptionKey) && aesCipher == null) {
            try {
                aesCipher = CSEUtil.aesCipher();
            } catch (Exception e) {
                LOG.warn("Could not instantiate an AES Cipher", e);
            }
        }
        return aesCipher;
    }

    public Cipher getRsaCipher() {
        if (StringUtils.isNotBlank(encryptionKey) && rsaCipher == null) {
            try {
                rsaCipher = CSEUtil.rsaCipher(encryptionKey);
            } catch (Exception e) {
                LOG.warn("Could not instantiate an RSA Cipher. encryptionKey: {}", encryptionKey, e);
            }
        }
        return rsaCipher;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).append("endpointHost", endpointHost).append("connectionTimeout", connectionTimeout)
                .append("socketTimeout", socketTimeout).append("proxyConfig", proxyConfig).append("proxyHost", proxyHost).append("endpoint", endpoint)
                .append("username", username).append("password", password).append("proxyUsername", proxyUsername).append("proxyPassword", proxyPassword)
                .append("extraParameters", extraParameters).append("encryptionKey", encryptionKey).toString();
    }
}
