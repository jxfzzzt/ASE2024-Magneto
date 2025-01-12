package com.car.order.web.mapper.course;

import com.car.order.client.request.course.QueryCourseListReq;
import com.car.order.client.request.course.QueryCourseParentListReq;
import com.car.order.client.response.course.QueryCourseInfoRes;
import com.car.order.client.response.course.QueryCourseListRes;
import com.car.order.client.response.course.QueryCourseParentInfoRes;
import com.car.order.client.response.course.QueryCourseParentListRes;
import com.car.order.web.model.course.Course;
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
public interface CourseMapper extends Mapper<Course> {


    /**
     * 根据id查询详情
     * @param uuid
     * @return
     */
    QueryCourseInfoRes getById(@Param("uuid") String uuid);


    /**
     * 查询课程分类列表
     * @param req
     * @return
     */
    List<QueryCourseListRes> list(@Param("req") QueryCourseListReq req);
}
