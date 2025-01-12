package cn.qs.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.qs.service.BaseService;
import cn.qs.utils.DefaultValue;
import cn.qs.utils.JSONResultUtil;

/**
 * 所以控制层的基类
 * 
 * @author Administrator
 *
 */
public abstract class AbstractController<T, E extends Serializable> {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractController.class);

	/**
	 * 页面所在的目录(由子类实现)
	 * 
	 * @return
	 */
	public abstract String getViewBasePath();

	/**
	 * service，由具体的Action注入对应实现
	 * 
	 * @return
	 */
	public abstract BaseService<T, E> getBaseService();

	/**
	 * 生成带basePath的页面路径
	 * 
	 * @param path
	 * @return
	 */
	public String getViewPath(String path) {
		return getViewBasePath() + "/" + path;
	}

	@RequestMapping("add")
	public String add() {
		return getViewPath("add");
	}

	@RequestMapping("doAdd")
	@ResponseBody
	public JSONResultUtil doAdd(T bean, HttpServletRequest request) {
		getBaseService().add(bean);
		return JSONResultUtil.ok();
	}

	@RequestMapping("list")
	public String list() {
		return getViewPath("list");
	}

	/**
	 * SpringDataJPA分页(接收普通参数返回page对象)
	 * 
	 * @param condition
	 * @param request
	 * @return
	 */
	@RequestMapping("page")
	@ResponseBody
	public Page<T> page(@RequestParam Map condition, HttpServletRequest request) {
		int pageNum = 1;
		if (StringUtils.isNotBlank(MapUtils.getString(condition, "pageNum"))) { // 如果不为空的话改变当前页号
			pageNum = MapUtils.getInteger(condition, "pageNum");
		}
		int pageSize = DefaultValue.PAGE_SIZE;
		if (StringUtils.isNotBlank(MapUtils.getString(condition, "pageSize"))) { // 如果不为空的话改变当前页大小
			pageSize = MapUtils.getInteger(condition, "pageSize");
		}

		condition.put("pageNum", pageNum - 1);
		condition.put("pageSize", pageSize);

		Page<T> pages = null;
		// 开始分页
		try {
			pages = getBaseService().pageByCondition(condition);
		} catch (Exception e) {
			LOGGER.error("SpringDataJPA page error, viewbasePath : {}", getViewBasePath(), e);
		}

		System.out.println(JSONObject.toJSONString(pages));
		return pages;
	}

	/**
	 * SpringDataJPA分页(接收JSON参数，返回JSON信息，封装到工具类中)
	 * 
	 * @param condition
	 * @param request
	 * @return
	 * @throws InterruptedException
	 */
	@RequestMapping("pageJSON")
	@ResponseBody
	public JSONResultUtil<Page<T>> pageJSON(@RequestBody Map condition) throws InterruptedException {
		int pageNum = 1;
		if (StringUtils.isNotBlank(MapUtils.getString(condition, "pageNum"))) { // 如果不为空的话改变当前页号
			pageNum = MapUtils.getInteger(condition, "pageNum");
		}
		int pageSize = DefaultValue.PAGE_SIZE;
		if (StringUtils.isNotBlank(MapUtils.getString(condition, "pageSize"))) { // 如果不为空的话改变当前页大小
			pageSize = MapUtils.getInteger(condition, "pageSize");
		}

		condition.put("pageNum", pageNum - 1);
		condition.put("pageSize", pageSize);

		Page<T> pages = null;
		// 开始分页
		try {
			pages = getBaseService().pageByCondition(condition);
		} catch (Exception e) {
			LOGGER.error("SpringDataJPA page error, viewbasePath : {}", getViewBasePath(), e);
		}

		return new JSONResultUtil<Page<T>>(true, "ok", pages);
	}

	/**
	 * mybatis分页(接收JSON参数，返回JSON工具类对象)
	 * 
	 * @param condition
	 * @param request
	 * @return
	 */
	@RequestMapping("pageJSON2")
	@ResponseBody
	public JSONResultUtil<PageInfo<T>> pageJSON2(@RequestBody(required = false) Map condition) {
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
		List<T> users = new ArrayList<T>();
		try {
			users = getBaseService().listByCondition(condition);
		} catch (Exception e) {
			LOGGER.error("getUsers error！", e);
		}
		PageInfo<T> pageInfo = new PageInfo<T>(users);

		return new JSONResultUtil<PageInfo<T>>(true, "ok", pageInfo);
	}

	/**
	 * mybatis分页(接收普通表单参数，返回PageInfo对象)
	 * 
	 * @param condition(参数是JSON形式数据)
	 * @param request
	 * @return
	 */
	@RequestMapping("page2")
	@ResponseBody
	public PageInfo<T> page2(@RequestParam Map<String, Object> condition, HttpServletRequest request) {
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
		List<T> beans = new ArrayList<T>();
		try {
			beans = getBaseService().listByCondition(condition);
		} catch (Exception e) {
			LOGGER.error("getUsers error！", e);
		}

		PageInfo<T> pageInfo = new PageInfo<T>(beans);
		return pageInfo;
	}

	@RequestMapping("delete")
	@ResponseBody
	public JSONResultUtil delete(E id) {
		getBaseService().delete(id);
		return JSONResultUtil.ok();
	}

	@RequestMapping("update")
	public String update(E id, ModelMap map, HttpServletRequest request) {
		T bean = getBaseService().findById(id);
		map.addAttribute("bean", bean);
		return getViewPath("update");
	}

	@RequestMapping("doUpdate")
	@ResponseBody
	public JSONResultUtil doUpdate(T bean) {
		getBaseService().update(bean);
		return JSONResultUtil.ok();
	}

	@RequestMapping("detail/{id}")
	@ResponseBody
	public JSONResultUtil<T> detailRest(@PathVariable() E id) {
		T bean = getBaseService().findById(id);
		if (bean == null) {
			return new JSONResultUtil<>(false, "不存在", bean);
		}

		return new JSONResultUtil<>(true, "", bean);
	}

	@RequestMapping("detail")
	public String detail(E id, ModelMap map, HttpServletRequest request) {
		T bean = getBaseService().findById(id);
		map.addAttribute("bean", bean);
		return getViewPath("detail");
	}
	
}
