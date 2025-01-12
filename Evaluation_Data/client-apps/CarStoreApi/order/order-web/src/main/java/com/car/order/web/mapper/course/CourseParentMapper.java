package com.car.order.web.mapper.course;

import com.car.order.client.request.course.QueryCourseParentListReq;
import com.car.order.client.response.course.QueryCourseParentInfoRes;
import com.car.order.client.response.course.QueryCourseParentListRes;
import com.car.order.web.model.course.CourseParent;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * Created by Intellij IDEA.
 *
 * @author:  cjw
 * Date:  2021/2/21
 */
@Repository
public interface CourseParentMapper extends Mapper<CourseParent> {

	/**
	 * 根据id查询详情
	 * @param uuid
	 * @return
	 */
	QueryCourseParentInfoRes getById(@Param("uuid") String uuid);

	/**
	 * 查询课程分类列表
	 * @param req
	 * @return
	 */
	List<QueryCourseParentListRes> list(@Param("req") QueryCourseParentListReq req);

	/**
	 * 查询课程分类列表:最新的
	 * @param req
	 * @return
	 */
	List<QueryCourseParentListRes> listNewest(@Param("req") QueryCourseParentListReq req);

	/**
	 * 查询课程分类列表：一般的
	 * @param req
	 * @return
	 */
	List<QueryCourseParentListRes> listGeneral(@Param("req") QueryCourseParentListReq req);

	/**
	 * 查询最新的前N条教程
	 * @param newCourseParenNum
	 * @return
	 */
	List<QueryCourseParentListRes> queryCourseParentLimit(@Param("newCourseParenNum") Integer newCourseParenNum);
}
