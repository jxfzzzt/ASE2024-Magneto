package cn.qs.utils.weixin.auth;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import cn.qs.utils.weixin.WeixinInterfaceUtils;

public class WeixinJSAPISignUtils {

	public static void main(String[] args) {
		// 注意 URL 一定要动态获取，不能 hardcode
		String url = "http://8fbb6757.ngrok.io/weixinauth/index.html";
		Map<String, String> ret = sign(WeixinInterfaceUtils.getJsapiTicket(), url);
		for (Map.Entry entry : ret.entrySet()) {
			System.out.println(entry.getKey() + ", " + entry.getValue());
		}
	}

	/**
	 * 签名
	 * 
	 * @param jsapiTicket
	 *            jsapiTicket
	 * @param url
	 *            调用接口的当前URL(不包含#以及后面部分)
	 * @return
	 */
	public static Map<String, String> sign(String jsapiTicket, String url) {
		Map<String, String> ret = new HashMap<String, String>();
		String nonce_str = create_nonce_str();
		String timestamp = getTimestamp();
		String signatureString;
		String signature = "";

		// 注意这里参数名必须全部小写，且必须有序(必须这样签名)==签名用的noncestr和timestamp必须与wx.config中的nonceStr和timestamp相同。签名用的url必须是调用JS接口页面的完整URL。
		signatureString = "jsapi_ticket=" + jsapiTicket + "&noncestr=" + nonce_str + "&timestamp=" + timestamp + "&url="
				+ url;

		try {
			MessageDigest crypt = MessageDigest.getInstance("SHA-1");
			crypt.reset();
			crypt.update(signatureString.getBytes("UTF-8"));
			signature = byteToHex(crypt.digest());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		ret.put("url", url);
		ret.put("jsapi_ticket", jsapiTicket);
		ret.put("nonceStr", nonce_str);
		ret.put("timestamp", timestamp);
		ret.put("signature", signature);

		return ret;
	}

	private static String byteToHex(final byte[] hash) {
		Formatter formatter = new Formatter();
		for (byte b : hash) {
			formatter.format("%02x", b);
		}
		String result = formatter.toString();
		formatter.close();
		return result;
	}

	private static String create_nonce_str() {
		return UUID.randomUUID().toString();
	}

	public static String getTimestamp() {
		return Long.toString(System.currentTimeMillis() / 1000);
	}
}
