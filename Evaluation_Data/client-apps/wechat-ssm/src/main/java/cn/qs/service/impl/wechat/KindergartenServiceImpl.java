package cn.qs.service.impl.wechat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.GenericPropertyMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.QPageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.qs.bean.wechat.Kindergarten;
import cn.qs.mapper.BaseMapper;
import cn.qs.mapper.user.custom.UserCustomMapper;
import cn.qs.mapper.wechat.KindergartenMapper;
import cn.qs.service.impl.AbastractBaseSequenceServiceImpl;
import cn.qs.service.wechat.KindergartenService;
import cn.qs.utils.BeanUtils;

@Service
@Transactional
public class KindergartenServiceImpl extends AbastractBaseSequenceServiceImpl<Kindergarten>
		implements KindergartenService {

	@Autowired
	private KindergartenMapper kindergartenMapper;

	@Autowired
	private UserCustomMapper userCustomMapper;

	@Override
	public BaseMapper<Kindergarten, Integer> getBaseMapper() {
		return kindergartenMapper;
	}

	@Override
	public void update(Kindergarten t) {
		// 根据ID查询
		Object propertyValue = BeanUtils.getProperty(t, "id");
		Kindergarten systemBean = getBaseMapper().findOne((Integer) propertyValue);
		if (systemBean != null) {
			// 改了幼儿园名称的情况
			if (!systemBean.getName().equals(t.getName())) {
				userCustomMapper.updateRemark1(systemBean.getName(), t.getName());
			}

			BeanUtils.copyProperties(systemBean, t);
		} else {
			return;
		}

		getBaseMapper().save(systemBean);
	}

	@Override
	public List<Map<String, Object>> listNamesAndIds() {
		// 构造请求参数，页号从0开始。
		int pageNum = 0;
		int pageSize = 1000;
		Pageable pageRequest = new QPageRequest(pageNum, pageSize);
		Page<Kindergarten> page = kindergartenMapper.findAll(pageRequest);

		List<Map<String, Object>> result = new ArrayList<>();
		if (page != null && CollectionUtils.isNotEmpty(page.getContent())) {
			Map<String, Object> tmpMap = null;

			for (Kindergarten tmp : page.getContent()) {
				tmpMap = new HashMap<>();
				// tmpMap.put("key", tmp.getId());

				// 直接使用幼儿园名称，所以这里key和value都返回幼儿园
				tmpMap.put("key", tmp.getName());
				tmpMap.put("value", tmp.getName());

				result.add(tmpMap);
			}
		}

		return result;
	}

	@Override
	public Page<Kindergarten> pageByCondition(Map condition) {
		// 构造请求参数，页号从0开始。
		int pageNum = MapUtils.getInteger(condition, "pageNum", 0);
		int pageSize = MapUtils.getInteger(condition, "pageSize", 0);
		Pageable pageRequest = new QPageRequest(pageNum, pageSize);

		// 根据条件查询:
		String keywords = MapUtils.getString(condition, "keywords", "");
		if (StringUtils.isNotBlank(keywords)) {
			Kindergarten kindergarten = new Kindergarten();
			kindergarten.setCreatetime(null);
			kindergarten.setCreator(null);
			kindergarten.setName(keywords);
			/*
			 * kindergarten.setAddress(keywords);
			 * kindergarten.setVersion(keywords);
			 * kindergarten.setServer(keywords);
			 */

			GenericPropertyMatcher contains = ExampleMatcher.GenericPropertyMatchers.contains();
			ExampleMatcher matcher = ExampleMatcher.matching().withMatcher("name", contains);
			// .withMatcher("address", contains).withMatcher("server",
			// contains).withMatcher("version", contains);// 查询username包含修改user
			Example<Kindergarten> example = Example.of(kindergarten, matcher);

			return getBaseMapper().findAll(example, pageRequest);
		}

		return getBaseMapper().findAll(pageRequest);
	}

	@Override
	public Kindergarten findByName(String name) {
		return kindergartenMapper.findByName(name);
	}

}
