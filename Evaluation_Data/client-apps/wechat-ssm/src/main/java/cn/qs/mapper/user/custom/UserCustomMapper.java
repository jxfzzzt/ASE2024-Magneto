package cn.qs.mapper.user.custom;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserCustomMapper {

	/**
	 * 统一修改幼儿园名称(修改幼儿园名称后执行此操作)
	 * 
	 * @param srcValue
	 * @param destValue
	 */
	@Update(value = "update user set remark1 = #{destValue} where remark1 = #{srcValue}")
	void updateRemark1(@Param("srcValue") String srcValue, @Param("destValue") String destValue);
}
