package cn.qs.service.impl.wechat;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.qs.bean.wechat.Pay;
import cn.qs.mapper.BaseMapper;
import cn.qs.mapper.wechat.PayMapper;
import cn.qs.mapper.wechat.custom.PayCustomMapper;
import cn.qs.service.impl.AbastractBaseSequenceServiceImpl;
import cn.qs.service.wechat.PayService;
import cn.qs.utils.DefaultValue;
import cn.qs.utils.system.MySystemUtils;

@Service
@Transactional
public class PayServiceImpl extends AbastractBaseSequenceServiceImpl<Pay> implements PayService {

	@Autowired
	private PayMapper payMapper;

	@Autowired
	private PayCustomMapper payCustomMapper;

	@Override
	public BaseMapper<Pay, Integer> getBaseMapper() {
		return payMapper;
	}

	@Override
	public Map<String, Object> detail(Integer id) {
		return payCustomMapper.detail(id);
	}

	@Override
	public List<Pay> listByCondition(Map<String, Object> condition) {
		// 普通用户只能查自己创建的
		if (!DefaultValue.ROLE_SYSYEM.equals(MySystemUtils.getLoginUser().getRoles())) {
			condition.put("creator", MySystemUtils.getLoginUser().getUsername());
		}

		return payCustomMapper.listByCondition(condition);
	}

	@Override
	public Pay findByOrderId(String orderId) {
		return payMapper.findByOrderId(orderId);
	}

	@Override
	public List<Map<String, Object>> listMap(Map<String, Object> condition) {
		// 普通用户只能查自己创建的
		if (!DefaultValue.ROLE_SYSYEM.equals(MySystemUtils.getLoginUser().getRoles())) {
			condition.put("creator", MySystemUtils.getLoginUser().getUsername());
		}

		return payCustomMapper.listMap(condition);
	}
}
