package cn.qs.mapper.wechat.custom;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import cn.qs.bean.wechat.Pay;

@Mapper
public interface PayCustomMapper {

	@Select("select pay.*,kindergarten.server,kindergarten.version,kindergarten.amount from pay inner join kindergarten on kindergarten.id = pay.kindergartenId where pay.id = #{id}")
	Map<String, Object> detail(Integer id);

	/**
	 * 组合条件查询(XML中编写SQL)
	 * 
	 * @param condition
	 * @return
	 */
	List<Pay> listByCondition(Map condition);

	List<Map<String, Object>> listMap(Map<String, Object> condition);

}
