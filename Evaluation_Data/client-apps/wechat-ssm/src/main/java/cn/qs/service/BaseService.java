package cn.qs.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;

/**
 * 公共的service接口
 * 
 * @author Administrator
 *
 * @param <T>
 *            bean实体
 * @param <E>
 *            ID类型
 */
public interface BaseService<T, E extends Serializable> {

	void add(T t);

	void delete(E id);

	void update(T t);

	T findById(E id);

	List<T> listByCondition(Map<String, Object> condition);

	Page<T> pageByCondition(Map<String, Object> condition);

}
