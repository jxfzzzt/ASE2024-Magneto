package cn.qs.service.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.QPageRequest;

import cn.qs.mapper.BaseMapper;
import cn.qs.service.BaseService;
import cn.qs.utils.BeanUtils;

/**
 * 提供一个默认的实现
 * 
 * @author Administrator
 *
 * @param <T>
 * @param <E>
 */
public abstract class AbastractBaseServiceImpl<T, E extends Serializable> implements BaseService<T, E> {

	/**
	 * 子类来实现该方法
	 * 
	 * @return
	 */
	public abstract BaseMapper<T, E> getBaseMapper();

	@Override
	public void add(T t) {
		getBaseMapper().save(t);
	}

	@Override
	public void delete(E id) {
		getBaseMapper().delete(id);
	}

	@Override
	public void update(T t) {
		// 根据ID查询
		Object propertyValue = BeanUtils.getProperty(t, "id");
		T systemBean = getBaseMapper().findOne((E) propertyValue);
		if (systemBean != null) {
			BeanUtils.copyProperties(systemBean, t);
		} else {
			return;
		}

		getBaseMapper().save(systemBean);
	}

	@Override
	public T findById(E id) {
		return getBaseMapper().findOne(id);
	}

	/**
	 * Mybatis专用的分页查询，子类可以重写
	 */
	@Override
	public List<T> listByCondition(Map<String, Object> condition) {
		return null;
	}

	@Override
	public Page<T> pageByCondition(Map condition) {
		// 构造请求参数，页号从0开始。
		int pageNum = MapUtils.getInteger(condition, "pageNum", 0);
		int pageSize = MapUtils.getInteger(condition, "pageSize", 0);
		Pageable pageRequest = new QPageRequest(pageNum, pageSize);

		Page<T> page = getBaseMapper().findAll(pageRequest);
		return page;
	}

}
