package com.car.order.web.service.course;

import com.car.common.res.PageRes;
import com.car.common.res.ResultRes;
import com.car.order.client.request.course.AddCourseParentReq;
import com.car.order.client.request.course.QueryCourseParentListReq;
import com.car.order.client.response.course.QueryCourseParentInfoRes;
import com.car.order.client.response.course.QueryCourseParentListRes;
import java.util.List;

/**
 * Created by Intellij IDEA.
 *
 * @author:  cjw
 * Date:  2021/2/21
 */
public interface CourseParentService {

	/**
	 * 根据id查询详情
	 * @param uuid
	 * @return
	 */
	ResultRes<QueryCourseParentInfoRes> getById(String uuid);

	/**
	 * 新增教程分类信息
	 * @param req
	 * @return
	 */
	ResultRes<String> add(AddCourseParentReq req);

	/**
	 * 根据id修改课程分类信息
	 * @param req
	 * @param uuid
	 * @return
	 */
	ResultRes<String> updateById(AddCourseParentReq req, String uuid);

	/**
	 * 删除课程分类信息
	 * @param uuid
	 * @return
	 */
	ResultRes<String> deleteById(String uuid);

	/**
	 * 分页列表
	 * @param req
	 * @return
	 */
	PageRes<List<QueryCourseParentListRes>> list(QueryCourseParentListReq req);

	/**
	 * 分页列表：最新分类
	 * @param req
	 * @return
	 */
	public PageRes<List<QueryCourseParentListRes>> listNewest(QueryCourseParentListReq req);

	/**
	 * 分页列表:一般分类
	 * @param req
	 * @return
	 */
	public PageRes<List<QueryCourseParentListRes>> listGeneral(QueryCourseParentListReq req);
}
