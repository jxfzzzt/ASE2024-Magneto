package com.car.order.web.service.course;

import com.car.account.client.request.technician.TechnicianListReq;
import com.car.common.res.PageRes;
import com.car.common.res.ResultRes;
import com.car.order.client.request.course.AddCourseOrderReq;
import com.car.order.client.request.course.AddCourseReq;
import com.car.order.client.request.course.QueryCourseListReq;
import com.car.order.client.request.course.QueryCourseOrderListReq;
import com.car.order.client.response.course.QueryCourseInfoRes;
import com.car.order.client.response.course.QueryCourseListRes;
import com.car.order.client.response.course.QueryCourseOrderInfoRes;
import com.car.order.client.response.course.QueryCourseOrderListRes;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by Intellij IDEA.
 *
 * @author:  cjw
 * Date:  2021/2/21
 */
public interface CourseOrderService {

    /**
     * 根据id查询课程订单详情
     * @param uuid
     * @return
     */
    ResultRes<QueryCourseOrderInfoRes> getById(String uuid);


    /**
     * 新增课程信息
     * @param req
     * @return
     */
    ResultRes<String> add(AddCourseOrderReq req);

    /**
     * 分页列表
     * @param req
     * @return
     */
    PageRes<List<QueryCourseOrderListRes>> list(QueryCourseOrderListReq req);

    /**
     * 课程订单信息导出
     * @param exportReq
     * @param response
     */
    void exportCourseOrderList(QueryCourseOrderListReq exportReq, HttpServletResponse response);
}
