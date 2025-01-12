package cn.qs.controller.weixin;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSONObject;

import cn.qs.bean.user.User;
import cn.qs.bean.user.WechatUser;
import cn.qs.service.user.UserService;
import cn.qs.utils.DefaultValue;
import cn.qs.utils.HttpUtils;
import cn.qs.utils.JSONResultUtil;
import cn.qs.utils.securty.MD5Utils;
import cn.qs.utils.system.MySystemUtils;
import cn.qs.utils.weixin.WeixinConstants;
import cn.qs.utils.weixin.WeixinInterfaceUtils;
import cn.qs.utils.weixin.auth.WeixinJSAPISignUtils;

@Controller
@RequestMapping("weixin/auth")
public class WeixinAuthController {

	private static final Logger logger = org.slf4j.LoggerFactory.getLogger(WeixinAuthController.class);

	@Autowired
	private UserService userService;

	/**
	 * 首页，跳转到index.html,index.html有一个连接会访问下面的login方法
	 * 
	 * @return
	 */
	@RequestMapping("/index")
	public String index(ModelMap map) {
		// 注意 URL 一定要动态获取，不能 hardcode
		String url = "http://4de70c98.ngrok.io/weixin/auth/index.html";
		Map<String, String> signers = WeixinJSAPISignUtils.sign(WeixinInterfaceUtils.getJsapiTicket(), url);

		map.put("signers", signers);
		return "weixinauth/index";
	}

	@RequestMapping("/getJsapiSigner")
	@ResponseBody
	public JSONResultUtil<Map<String, String>> getJsapiSigner(
			@RequestBody(required = false) Map<String, Object> condition) {

		String url = MapUtils.getString(condition, "url");
		Map<String, String> signers = WeixinJSAPISignUtils.sign(WeixinInterfaceUtils.getJsapiTicket(), url);

		signers.put("appId", WeixinConstants.APPID);
		logger.info("signers: {}", signers);

		return new JSONResultUtil<Map<String, String>>(true, "ok", signers);
	}

	/**
	 * (一)微信授权：重定向到授权页面
	 * 
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping("/login")
	public String authorize() throws UnsupportedEncodingException {
		// 回调地址必须在公网可以访问
		String recirectUrl = URLEncoder.encode(WeixinConstants.AUTH_REDIRECT_URL, "UTF-8");

		// 授权地址
		String url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect";
		url = url.replace("APPID", WeixinConstants.APPID).replace("REDIRECT_URI", recirectUrl);
		logger.info("url: {}", url);

		// 参数替换之后重定向到授权地址
		return "redirect:" + url;
	}

	/**
	 * (二)用户同意授权; (三)微信会自动重定向到该页面并携带参数code和state用于换取access_token和openid; (四)
	 * 用access_token和openid获取用户信息(五)如果有必要可以进行登录，两种:第一种是直接拿微信号登录；
	 * 第二种是根据openid和nickname获取账号进行登录
	 * 
	 * @param code
	 * @param state
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping("/calback")
	public String calback(String code, String state) throws UnsupportedEncodingException {
		// 获取access_token和openid
		try {
			String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
			url = url.replace("APPID", WeixinConstants.APPID).replace("SECRET", WeixinConstants.APP_SECRET)
					.replace("CODE", code);
			String doGet = HttpUtils.doGet(url);

			if (StringUtils.isNotBlank(doGet)) {
				JSONObject parseObject = JSONObject.parseObject(doGet);

				// 获取两个参数之后获取用户信息
				String accessToken = parseObject.getString("access_token");
				String openid = parseObject.getString("openid");
				String getUserInfoURL = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
				getUserInfoURL = getUserInfoURL.replace("ACCESS_TOKEN", accessToken).replace("OPENID", openid);
				String doGet2 = HttpUtils.doGet(getUserInfoURL);
				logger.debug("userInfo: {}", doGet2);

				// 用获取到的用户信息进行自己体系的登录
				if (StringUtils.isNotBlank(doGet2)) {
					WechatUser user = JSONObject.parseObject(doGet2, WechatUser.class);
					logger.debug("user: {}", user);

					return doLoginWithWechatUser(user);
				}
			}
		} catch (Exception e) {
			logger.error("登录错误", e);
		}

		logger.info("登录失败了");
		return "error";
	}

	private String doLoginWithWechatUser(WechatUser wechatUser) {
		if (wechatUser == null || StringUtils.isBlank(wechatUser.getOpenid())) {
			return "获取信息错误";
		}

		String openid = wechatUser.getOpenid();
		User findUserByUsername = userService.findUserByUsername(openid);
		if (findUserByUsername == null) {
			User user = new User();
			user.setUsername(openid);
			user.setPassword(MD5Utils.md5(openid));
			user.setRoles(DefaultValue.ROLE_PLAIN_USER);
			user.setSex("1".equals(wechatUser.getSex()) ? "男" : "女");
			user.setProperty("from", "wechat");

			String address = "";
			if (StringUtils.isNotBlank(wechatUser.getCountry())) {
				address += wechatUser.getCountry();
			}
			if (StringUtils.isNotBlank(wechatUser.getProvince())) {
				address += wechatUser.getProvince();
			}
			if (StringUtils.isNotBlank(wechatUser.getCity())) {
				address += wechatUser.getCity();
			}
			user.setWechataddress(address);
			user.setWechatnickname(wechatUser.getNickname());
			user.setWechatphoto(wechatUser.getHeadimgurl());

			// 设置第一次登陆的优惠金额
			user.setCoupon(NumberUtils.toFloat(MySystemUtils.getProperty("coupon", "0")));

			logger.debug("create user", user);
			userService.add(user);
			findUserByUsername = userService.findUserByUsername(openid);
		} else {
			logger.debug("已经存在的账户, {}", findUserByUsername);
		}

		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		HttpSession session = request.getSession();
		session.setAttribute("user", findUserByUsername);

		// 登录成功之后后台进行跳转
		String redirectUrl = "";
		if (DefaultValue.ROLE_SYSYEM.equals(findUserByUsername.getUsername())) {
			redirectUrl = "redirect:" + WeixinConstants.ROLE_ADMIN_REDIRECTURL;
		} else {
			redirectUrl = "redirect:" + WeixinConstants.ROLE_PLAIN_REDIRECTURL;
		}

		return redirectUrl;
	}
}
