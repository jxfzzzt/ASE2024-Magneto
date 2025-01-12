package cn.qs.utils.craw;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.qs.controller.user.UserController;

public class JsoupCrawUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

	private static Map<String, String> COOKIES = new HashMap<String, String>();

	public static String requestURL(String url) {
		return requestURL(url, null, null);
	}

	/**
	 * 发送http请求
	 * 
	 * @param url
	 *            地址
	 * @param cookiesParam
	 *            参数
	 * @param data
	 *            数据
	 * @return
	 */
	public static String requestURL(String url, Map<String, String> cookiesParam, Map<String, String> data) {
		if (StringUtils.isBlank(url)) {
			return "";
		}

		try {
			Connection connect = Jsoup.connect(url);
			if (MapUtils.isNotEmpty(cookiesParam)) {
				COOKIES.putAll(cookiesParam);
			}
			if (MapUtils.isNotEmpty(COOKIES)) {
				connect.cookies(COOKIES);
			}

			if (MapUtils.isNotEmpty(data)) {
				connect.data(data);
			}

			Response response = connect.ignoreContentType(true).execute();
			if (MapUtils.isNotEmpty(response.cookies())) {
				COOKIES.putAll(response.cookies());
			}

			String responseInfo = response.body();
			return responseInfo;
		} catch (IOException e) {
			LOGGER.error("请求 url ->{} error", url, e);
			return "";
		}
	}

	/**
	 * 根据标签提取内容
	 * 
	 * @param content
	 * @param tag
	 * @return
	 */
	public static List<String> extractContentByTag(String content, String tag) {
		List<String> result = new ArrayList<>();
		if (StringUtils.isBlank(content)) {
			return result;
		}

		Document doc = Jsoup.parse(content);
		Elements links = doc.getElementsByTag(tag);
		if (links == null || links.isEmpty()) {
			return result;
		}

		for (Element ele : links) {
			result.add(ele.text());
		}

		return result;
	}

}
