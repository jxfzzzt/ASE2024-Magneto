package cn.qs.controller.test;

import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.MapUtils;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.qs.utils.JSONResultUtil;

@RequestMapping("/test")
@RestController
public class TestController {

	@PostMapping("/test")
	public JSONResultUtil<String> Test(@RequestBody String condition) {
		System.out.println(condition);

		return new JSONResultUtil<String>(false, null, "error");
	}

	@GetMapping("/get")
	public Map<String, Object> get(@RequestParam Map<String, Object> condition, HttpServletRequest request) {
		Enumeration<String> headerNames = request.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			String header = (String) headerNames.nextElement();
			String value = request.getHeader(header);
			System.out.println(header + "\t" + value);
		}

		if (MapUtils.isEmpty(condition)) {
			condition = new LinkedHashMap<>();
			condition.put("param", null);
		}

		return condition;
	}

	@PostMapping("/getJSON")
	public String getJSON(@RequestBody String param) {
		System.out.println(param);
		return param;
	}

	@GetMapping("/getCookie")
	public String getCookie(@CookieValue(value = "cookie1", required = false) String cookie,
			HttpServletRequest request) {

		System.out.println("cookie1: " + cookie);
		System.out.println("=====================");

		Enumeration<String> headerNames = request.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			String header = (String) headerNames.nextElement();
			String value = request.getHeader(header);
			System.out.println(header + "\t" + value);
		}

		return cookie;
	}

	@GetMapping("/setCookie")
	public String setCookie(HttpServletRequest request, HttpServletResponse response) {
		Cookie cookie2 = new Cookie("cookie1", "value1");
		cookie2.setPath("/");
		cookie2.setHttpOnly(true);
		cookie2.setSecure(true);
		response.addCookie(cookie2);

		Cookie cookie3 = new Cookie("cookie2", "value2");
		cookie3.setPath("/");
		response.addCookie(cookie3);

		return "";
	}

	@GetMapping("/getHeader")
	public JSONResultUtil<String> getHeader(@RequestHeader("x-header1") String header1,
			@RequestHeader("x-header2") String header2) {

		System.out.println(header1 + " " + header2);
		return new JSONResultUtil(true, header1 + " " + header2);
	}

}