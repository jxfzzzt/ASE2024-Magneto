package com.car.utility.web.common.utils.weixin;

import com.car.utility.web.common.constants.PayConstants;
import com.car.utility.web.common.constants.WechatKeyConstants;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.Map;

/**
 *
 * @author zhangweijie
 *
 */
public class WeChatPayRequestUtil {
	private static final Logger logger = LoggerFactory.getLogger(WeChatPayRequestUtil.class);

	private static final int DEFAULT_CONNECTTIMEOUT_MS = 10000;

	private static final int DEFAULT_READTIMEOUT_MS = 10000;

	/**
	 * 请求，只请求一次
	 */
	public String requestOnce(String weixinPayApiUrl, String data, int connectTimeoutMs, int readTimeoutMs,
			boolean useCert, byte[] weixinCert) throws SAXException, IOException, ParserConfigurationException,
			KeyManagementException, NoSuchAlgorithmException {
		try {
			Map<String, String> xmlToMap = WXPayUtil.xmlToMap(data);
			String mchId = xmlToMap.get(WechatKeyConstants.MCH_ID);
			return request(weixinPayApiUrl, data, DEFAULT_CONNECTTIMEOUT_MS, DEFAULT_READTIMEOUT_MS, useCert,
					weixinCert, mchId);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return null;
		}

	}

	/**
	 * 请求，只请求一次，不做重试
	 *
	 * @param data
	 * @param connectTimeoutMs
	 * @param readTimeoutMs
	 * @param useCert
	 *            是否使用证书，针对退款、撤销等操作
	 * @return
	 * @throws Exception
	 */
	public String request(String weixinPayApiUrl, String data, int connectTimeoutMs, int readTimeoutMs, boolean useCert,
			byte[] weixinCert, String mchId) throws Exception {
		HttpEntity httpEntity = httpEntityResponse(weixinPayApiUrl, data, connectTimeoutMs, readTimeoutMs, useCert,
				weixinCert, mchId);
		return EntityUtils.toString(httpEntity, "UTF-8");

	}

	private HttpEntity httpEntityResponse(String weixinPayApiUrl, String data, int connectTimeoutMs, int readTimeoutMs,
			boolean useCert, byte[] weixinCert, String mchId)
			throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException,
			UnrecoverableKeyException, KeyManagementException, ClientProtocolException {
		BasicHttpClientConnectionManager connManager;
		if (useCert) {
			// 证书
			char[] password = mchId.toCharArray();
			//InputStream certStream = new ByteArrayInputStream(weixinCert);
			InputStream certStream = getClass().getClassLoader().getResourceAsStream(PayConstants.WEIXIN_APICLIENT_CERT);
			KeyStore ks = KeyStore.getInstance("PKCS12");
			ks.load(certStream, password);

			// 实例化密钥库 & 初始化密钥工厂
			KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
			kmf.init(ks, password);

			// 创建 SSLContext
			SSLContext sslContext = SSLContext.getInstance("TLS");
			sslContext.init(kmf.getKeyManagers(), null, new SecureRandom());

			SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext,
					new String[] { "TLSv1" }, null, new DefaultHostnameVerifier());

			connManager = new BasicHttpClientConnectionManager(RegistryBuilder.<ConnectionSocketFactory>create()
					.register("http", PlainConnectionSocketFactory.getSocketFactory())
					.register("https", sslConnectionSocketFactory).build(), null, null, null);
		} else {
			connManager = new BasicHttpClientConnectionManager(
					RegistryBuilder.<ConnectionSocketFactory>create()
							.register("http", PlainConnectionSocketFactory.getSocketFactory())
							.register("https", SSLConnectionSocketFactory.getSocketFactory()).build(),
					null, null, null);
		}

		HttpClient httpClient = HttpClientBuilder.create().setConnectionManager(connManager).build();
		HttpPost httpPost = new HttpPost(weixinPayApiUrl);

		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(readTimeoutMs)
				.setConnectTimeout(connectTimeoutMs).build();
		httpPost.setConfig(requestConfig);

		StringEntity postEntity = new StringEntity(data, "UTF-8");
		httpPost.addHeader("Content-Type", "text/xml");
		httpPost.addHeader("User-Agent", WXPayConstants.USER_AGENT + " " + mchId);
		httpPost.setEntity(postEntity);

		HttpResponse httpResponse = httpClient.execute(httpPost);
		HttpEntity httpEntity = httpResponse.getEntity();
		return httpEntity;
	}

	public byte[] requestOnceResonseByte(String weixinPayApiUrl, String data, int connectTimeoutMs, int readTimeoutMs,
			boolean useCert, byte[] weixinCert) throws Exception {
		Map<String, String> xmlToMap = WXPayUtil.xmlToMap(data);
		String mchId = xmlToMap.get(WechatKeyConstants.MCH_ID);
		HttpEntity httpEntity = httpEntityResponse(weixinPayApiUrl, data, connectTimeoutMs, readTimeoutMs, useCert,
				weixinCert, mchId);
		return EntityUtils.toByteArray(httpEntity);
	}
}
