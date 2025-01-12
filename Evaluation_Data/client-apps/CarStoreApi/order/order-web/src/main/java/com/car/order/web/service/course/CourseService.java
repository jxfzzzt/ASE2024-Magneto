package com.car.order.web.service.course;

import com.car.common.res.PageRes;
import com.car.common.res.ResultRes;
import com.car.order.client.request.course.AddCourseReq;
import com.car.order.client.request.course.QueryCourseListReq;
import com.car.order.client.response.course.QueryCourseInfoRes;
import com.car.order.client.response.course.QueryCourseListRes;

import java.util.List;

/**
 * Created by Intellij IDEA.
 *
 * @author:  cjw
 * Date:  2021/2/21
 */
public interface CourseService {

    /**
     * 根据id查询详情
     * @param uuid
     * @return
     */
    ResultRes<QueryCourseInfoRes> getById(String uuid);


    /**
     * 新增课程信息
     * @param req
     * @return
     */
    ResultRes<String> add(AddCourseReq req);


    /**
     * 根据id修改课程信息
     * @param req
     * @param uuid
     * @return
     */
    ResultRes<String> updateById(AddCourseReq req, String uuid);

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
    PageRes<List<QueryCourseListRes>> list(QueryCourseListReq req);
}
