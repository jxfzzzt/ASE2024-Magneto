package cn.qs.utils.web;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class WebUtils {

	private WebUtils() {
		throw new AssertionError();
	}

	public static String getIpAddress() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();

		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}

		if (StringUtils.isBlank(ip)) {
			ip = "127.0.0.1";
		}

		return ip;
	}

	public static void sendRedirect(HttpServletRequest request, HttpServletResponse response, String url)
			throws IOException {

		if (isAjax(request)) {
			writeJsToResponse(response, "window.location='" + request.getContextPath() + url + "';");
			return;
		}

		response.sendRedirect(url);
	}

	public static String encodeFileName(String namePrefix, String nameSufix) {
		String name = namePrefix + DateFormatUtils.format(new Date(), "yyyy-MM-dd") + "." + nameSufix;

		try {
			name = URLEncoder.encode(name, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return DateFormatUtils.format(new Date(), "yyyy-MM-dd") + "." + nameSufix;
		}

		return name;
	}

	/**
	 * 根据请求头是否携带X-Requested-With参数判断是否是ajax请求
	 * 
	 * @param httpRequest
	 * @return
	 */
	public static boolean isAjax(HttpServletRequest httpRequest) {
		return "XMLHttpRequest".equals(httpRequest.getHeader("X-Requested-With"));
	}

	public static void writeJsToResponse(HttpServletResponse response, String content) {
		writeToResponse(response, "text/javascript", "UTF-8", content);
	}

	public static void writeToResponse(HttpServletResponse response, String contentType, String encoding,
			String content) {

		response.setContentType(contentType);

		if (StringUtils.isNotBlank(encoding)) {
			response.setCharacterEncoding(encoding);
		}

		try {
			response.getWriter().write(content);
			response.flushBuffer();
		} catch (IOException ignored) {
			// ignore
		}
	}

}
