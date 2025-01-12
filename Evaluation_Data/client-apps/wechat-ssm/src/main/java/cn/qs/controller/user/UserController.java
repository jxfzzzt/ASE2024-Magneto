package cn.qs.controller.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.qs.bean.user.User;
import cn.qs.controller.AbstractSequenceController;
import cn.qs.service.BaseService;
import cn.qs.service.user.UserService;
import cn.qs.service.wechat.KindergartenService;
import cn.qs.utils.DefaultValue;
import cn.qs.utils.JSONResultUtil;
import cn.qs.utils.securty.MD5Utils;
import cn.qs.utils.system.MySystemUtils;
import cn.qs.utils.user.UserUtils;

@Controller
@RequestMapping("user")
public class UserController extends AbstractSequenceController<User> {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserService userService;

	@Autowired
	private KindergartenService KindergartenService;

	public String getViewBasePath() {
		return "user";
	}

	@RequestMapping("/addUser")
	public String add(String from, ModelMap map) {
		if (StringUtils.isNotBlank(from)) {
			map.addAttribute("from", from);
		}

		List<Map<String, Object>> kindergartens = KindergartenService.listNamesAndIds();
		map.addAttribute("kindergartens", kindergartens);

		return getViewPath("add");
	}

	@RequestMapping("updateUser")
	public String updateUser(Integer id, String from, ModelMap map, HttpServletRequest request) {
		if ("personal".equals(from)) {
			User user = (User) request.getSession().getAttribute("user");
			id = user.getId();
		} else {
			map.addAttribute("from", "admin");
		}

		List<Map<String, Object>> kindergartens = KindergartenService.listNamesAndIds();
		map.addAttribute("kindergartens", kindergartens);

		User user = userService.findById(id);
		map.addAttribute("user", user);

		return getViewPath("update");
	}

	@RequestMapping("doAddUser")
	@ResponseBody
	@Override
	public JSONResultUtil doAdd(User user, HttpServletRequest request) {
		if (user != null && "admin".equals(user.getUsername())) {
			return JSONResultUtil.error("您不能添加管理员用户");
		}

		User findUser = userService.findUserByUsername(user.getUsername());
		if (findUser != null) {
			return JSONResultUtil.error("用户已经存在");
		}

		// md5加密密码
		user.setPassword(MD5Utils.md5(user.getPassword()));
		if (StringUtils.isBlank(user.getRoles())) {
			user.setRoles("普通用户");
		}

		UserUtils.addDefaultWechatInfo(user);
		user.setProperty("from", "pc");

		userService.add(user);
		return JSONResultUtil.ok();
	}

	/**
	 * JSON形式的数据
	 * 
	 * @param user
	 * @param request
	 * @return
	 */
	@RequestMapping("doAddUserJSON")
	@ResponseBody
	public JSONResultUtil doAddUserJSON(@RequestBody User user, HttpServletRequest request) {
		if (user != null && "admin".equals(user.getUsername())) {
			return JSONResultUtil.error("您不能添加管理员用户");
		}

		User findUser = userService.findUserByUsername(user.getUsername());
		if (findUser != null) {
			return JSONResultUtil.error("用户已经存在");
		}

		user.setPassword(MD5Utils.md5(user.getPassword()));// md5加密密码
		if (StringUtils.isBlank(user.getRoles())) {
			user.setRoles("普通用户");
		}

		UserUtils.addDefaultWechatInfo(user);

		userService.add(user);
		return JSONResultUtil.ok();
	}

	/**
	 * Mybatis分页(重写方法)
	 * 
	 * @param condition
	 * @return
	 */
	@RequestMapping("page2")
	@ResponseBody
	@Override
	public PageInfo<User> page2(@RequestParam Map condition, HttpServletRequest request) {
		int pageNum = 1;
		if (StringUtils.isNotBlank(MapUtils.getString(condition, "pageNum"))) { // 如果不为空的话改变当前页号
			pageNum = MapUtils.getInteger(condition, "pageNum");
		}
		int pageSize = DefaultValue.PAGE_SIZE;
		if (StringUtils.isNotBlank(MapUtils.getString(condition, "pageSize"))) { // 如果不为空的话改变当前页大小
			pageSize = MapUtils.getInteger(condition, "pageSize");
		}

		// 开始分页
		PageHelper.startPage(pageNum, pageSize);
		List<User> users = new ArrayList<User>();
		try {
			users = userService.listByCondition(condition);
		} catch (Exception e) {
			LOGGER.error("getUsers error！", e);
		}
		PageInfo<User> pageInfo = new PageInfo<User>(users);

		return pageInfo;
	}

	@RequestMapping("detailLoginUser")
	@ResponseBody
	public JSONResultUtil<User> detailRest() {
		Integer id = MySystemUtils.getLoginUser().getId();
		User bean = getBaseService().findById(id);
		if (bean == null) {
			return new JSONResultUtil<>(false, "不存在", bean);
		}

		return new JSONResultUtil<>(true, "", bean);
	}

	@RequestMapping("updateLoginUser")
	@ResponseBody
	public JSONResultUtil<String> updateLoginUser(@RequestBody User user) {
		if (MySystemUtils.getLoginUser() == null) {
			return new JSONResultUtil<>(false, "请您登陆", "");
		}

		// 防止恶意修改角色
		user.setRoles(null);

		Integer id = MySystemUtils.getLoginUser().getId();
		user.setId(id);
		userService.update(user);
		return new JSONResultUtil<>(true, "修改成功", "");
	}

	@Override
	public BaseService<User, Integer> getBaseService() {
		return userService;
	}
}