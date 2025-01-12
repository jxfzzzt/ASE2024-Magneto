package cn.qs.utils.weixin;

import cn.qs.utils.file.PropertiesFileUtils;

public class WeixinConstants {

	public static String APPID = PropertiesFileUtils.getPropertyValue("application.properties", "WX_APPID");

	public static String MCHID = PropertiesFileUtils.getPropertyValue("application.properties", "WX_MCHID");

	public static String API_KEY = PropertiesFileUtils.getPropertyValue("application.properties", "WX_API_KEY");

	public static String APP_SECRET = PropertiesFileUtils.getPropertyValue("application.properties", "WX_APP_SECRET");

	public static String AUTH_REDIRECT_URL = PropertiesFileUtils.getPropertyValue("application.properties",
			"WX_AUTH_REDIRECT_URL");

	public static String ROLE_ADMIN_REDIRECTURL = PropertiesFileUtils.getPropertyValue("application.properties",
			"WX_ROLE_ADMIN_REDIRECTURL");

	public static String ROLE_PLAIN_REDIRECTURL = PropertiesFileUtils.getPropertyValue("application.properties",
			"WX_ROLE_PLAIN_REDIRECTURL");

	public static String PAY_SUCCESS_NOTIFY_URL = PropertiesFileUtils.getPropertyValue("application.properties",
			"WX_PAY_SUCCESS_NOTIFY_URL");
}
