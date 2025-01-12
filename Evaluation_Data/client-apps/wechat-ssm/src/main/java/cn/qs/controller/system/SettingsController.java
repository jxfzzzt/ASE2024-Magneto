package cn.qs.controller.system;

import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.qs.utils.JSONResultUtil;
import cn.qs.utils.format.ArithUtils;
import cn.qs.utils.system.MySystemUtils;

@RequestMapping("settings")
@RestController
public class SettingsController {

	private static final Logger logger = LoggerFactory.getLogger(SettingsController.class);

	@RequestMapping("getProperty")
	public JSONResultUtil<Float> getProperty(String key) {
		String propertyValue = MySystemUtils.getProperty(key);
		float amount = NumberUtils.toFloat(propertyValue, 0);
		Float formatedAmount = ArithUtils.format(amount, 2);
		return new JSONResultUtil<Float>(true, "ok", formatedAmount);
	}

	@RequestMapping("setProperty")
	public JSONResultUtil<Float> setProperty(String key, Float value) {
		MySystemUtils.setProperty(key, String.valueOf(value));
		return new JSONResultUtil<Float>(true, "ok", value);
	}

}
