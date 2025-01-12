package com.car.order.web.mapper.course;

import com.car.order.client.request.course.QueryCourseListReq;
import com.car.order.client.request.course.QueryCourseOrderListReq;
import com.car.order.client.response.course.QueryCourseInfoRes;
import com.car.order.client.response.course.QueryCourseListRes;
import com.car.order.client.response.course.QueryCourseOrderInfoRes;
import com.car.order.client.response.course.QueryCourseOrderListRes;
import com.car.order.web.model.course.Course;
import com.car.order.web.model.course.CourseOrder;
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
public interface CourseOrderMapper extends Mapper<CourseOrder> {


    /**
     * 根据id查询课程订单详情
     * @param uuid
     * @return
     */
    QueryCourseOrderInfoRes getById(@Param("uuid") String uuid);


    /**
     * 查询课程分类列表
     * @param req
     * @return
     */
    List<QueryCourseOrderListRes> list(@Param("req") QueryCourseOrderListReq req, @Param("userUuid") String userUuid);

    /**
     * 查询该课程是否已购买
     * @param courseUuid
     * @param userUuid
     * @return
     */
    int queryIsPurchase(@Param("courseUuid") String courseUuid, @Param("userUuid") String userUuid);
}
