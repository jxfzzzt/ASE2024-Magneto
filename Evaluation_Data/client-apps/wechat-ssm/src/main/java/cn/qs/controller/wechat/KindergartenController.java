package cn.qs.controller.wechat;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import cn.qs.bean.wechat.Kindergarten;
import cn.qs.controller.AbstractSequenceController;
import cn.qs.service.BaseService;
import cn.qs.service.wechat.KindergartenService;
import cn.qs.utils.JSONResultUtil;

@Controller
@RequestMapping("kindergarten")
public class KindergartenController extends AbstractSequenceController<Kindergarten> {

	private static final Logger LOGGER = LoggerFactory.getLogger(KindergartenController.class);

	@Autowired
	private KindergartenService kindergartenService;

	@Override
	public String getViewBasePath() {
		return "kindergarten";
	}

	@Override
	public BaseService<Kindergarten, Integer> getBaseService() {
		return kindergartenService;
	}

	@RequestMapping("listNamesAndIds")
	@ResponseBody
	public JSONResultUtil<List<Map<String, Object>>> listNamesAndIds() {
		// 开始分页
		List<Map<String, Object>> result = kindergartenService.listNamesAndIds();
		return new JSONResultUtil<List<Map<String, Object>>>(true, "", result);
	}

	@RequestMapping("detailByName")
	@ResponseBody
	public JSONResultUtil<Kindergarten> detailByName(@RequestBody Map<String, String> condition) {
		LOGGER.debug("detailByName condition: {}", condition);
		Kindergarten kindergarten = kindergartenService.findByName(MapUtils.getString(condition, "name", ""));
		if (kindergarten == null) {
			return new JSONResultUtil<>(false, "幼儿园不存在", kindergarten);
		}

		return new JSONResultUtil<>(true, "", kindergarten);
	}
}
