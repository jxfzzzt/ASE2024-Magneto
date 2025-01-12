package cn.qs.utils.craw;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.qs.controller.user.UserController;

/**
 * 爬取图片的工具类
 * 
 * @author Administrator
 *
 */
public class JsoupDownloadImageUtils {

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	/**
	 * 下载图片
	 * 
	 * @param imageUrl
	 *            图片地址
	 * @param cookies
	 *            携带的cookie
	 * @param savedPath
	 *            要保存的本地路径
	 */
	public static void downloadImage(String imageUrl, Map<String, String> cookies, String savedPath) {
		if (StringUtils.isBlank(imageUrl)) {
			return;
		}

		try {
			Connection connect = Jsoup.connect(imageUrl);
			if (MapUtils.isNotEmpty(cookies)) {
				connect.cookies(cookies);
			}
			Response response = connect.ignoreContentType(true).execute();

			OutputStream outputStream = new FileOutputStream(savedPath);
			// 利用IOutiks拷贝文件，简单快捷
			IOUtils.copy(response.bodyStream(), outputStream);
			logger.info("downloadImage sucess, url: {}, savedPath: {}", imageUrl, savedPath);
		} catch (IOException e) {
			logger.error("downloadImage error, url: {}", imageUrl, e);
		}
	}

	public static void main(String[] args) {
		String url = "http://192.168.0.200:8090/static/images/login/backimages/35.jpg?rnd=0.08977820226381938";
		downloadImage(url, null, "D:/1.png");

		String requestURL = JsoupCrawUtils.requestURL("https://www.cnblogs.com/qlqwjy/p/8899232.html");
		System.out.println(requestURL);
	}
}
