package com.car.account.web.common.config;


import com.car.common.utils.TokenHelper;
import com.car.common.utils.token.Baggages;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Vector;

@WebFilter(urlPatterns={"/*"})
public final class CrossDomainFilter implements Filter {
	private static final String VAL_ACCESS_CONTROL_ALLOW_ORIGIN = "*";
	private static final String VAL_ACCESS_CONTROL_ALLOW_HEADERS = new StringBuilder(
			"Origin,X-Requested-With,Content-Type,Accept,X-Cookie")
			.toString();
	private static final String VAL_ACCESS_CONTROL_ALLOW_METHODS = "GET,POST,PUT,DELETE,OPTIONS,HEAD,PATCH";

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		HttpServletRequest httpRequest = (HttpServletRequest) request;

		httpResponse.addHeader("Access-Control-Allow-Origin", VAL_ACCESS_CONTROL_ALLOW_ORIGIN);
		httpResponse.addHeader("Access-Control-Allow-Headers", VAL_ACCESS_CONTROL_ALLOW_HEADERS);
		httpResponse.addHeader("Access-Control-Allow-Methods", VAL_ACCESS_CONTROL_ALLOW_METHODS);

		if ("application/x-www-form-urlencoded".equals(httpRequest.getHeader("content-type"))) {
			httpRequest = new CrossRequestWrapper(httpRequest);
		}
		//增加token验证动态读取验证功能，主要防止Feign调用时，token数据丢失
		String token = httpRequest.getHeader(TokenHelper.TOKEN);
		if(!StringUtils.isEmpty(token)){
			Baggages.setToken(token);
		}
		chain.doFilter(httpRequest, response);
	}

	@Override
	public void destroy() {

	}

	private class CrossRequestWrapper extends HttpServletRequestWrapper {
		private CrossRequestWrapper(HttpServletRequest httpRequest) {
			super(httpRequest);
		}

		@Override
		public String getHeader(String name) {
			if ("content-type".equals(name.toLowerCase())) {
				return "application/json";
			}
			return super.getHeader(name);
		}

		@Override
		public Enumeration<String> getHeaders(String name) {
			if ("content-type".equals(name.toLowerCase())) {
				return new Vector<String>(Arrays.asList("application/json")).elements();
			}
			return super.getHeaders(name);
		}


		@Override
		public Enumeration<String> getHeaderNames() {
			return super.getHeaderNames();
		}

		@Override
		public String getContentType() {
			return "application/json";
		}
	}

}
