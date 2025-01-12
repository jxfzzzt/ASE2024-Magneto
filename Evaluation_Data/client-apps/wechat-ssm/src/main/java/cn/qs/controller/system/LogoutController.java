package cn.qs.controller.system;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.qs.utils.JSONResultUtil;

/**
 * 退出登陆
 * 
 * @author Administrator
 *
 */
@Controller
public class LogoutController {
	@RequestMapping("logout")
	public String logout(HttpSession session) {
		session.removeAttribute("user");
		return "redirect:/login.html";
	}

	@RequestMapping("logoutJson")
	public JSONResultUtil<String> logoutJson(HttpSession session) {
		session.removeAttribute("user");
		return new JSONResultUtil<>(true, "notLogin");
	}
}
