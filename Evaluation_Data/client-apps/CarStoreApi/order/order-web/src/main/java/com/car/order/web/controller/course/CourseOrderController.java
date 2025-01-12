package com.car.order.web.controller.course;

import com.car.common.annotation.SysOperLog;
import com.car.common.enums.OperEnum;
import com.car.common.res.PageRes;
import com.car.common.res.ResultRes;
import com.car.order.client.request.course.AddCourseOrderReq;
import com.car.order.client.request.course.QueryCourseOrderListReq;
import com.car.order.client.response.course.QueryCourseOrderInfoRes;
import com.car.order.client.response.course.QueryCourseOrderListRes;
import com.car.order.web.service.course.CourseOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by Intellij IDEA.
 *
 * @author:  cjw
 * Date:  2021/2/21
 */
@Slf4j
@RestController
@RequestMapping("/courseOrder")
@Api(value = "CourseOrderController", tags = "课程订单管理")
public class CourseOrderController {

    @Autowired
    private CourseOrderService courseOrderService;

    @GetMapping("/getById/{courseOrderUuid}")
    @ApiOperation(value = "根据id查询订单详情", nickname = "getById")
    @SysOperLog(operModul = "课程订单管理", operType = OperEnum.SELECT, operDesc = "根据id查询订单详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "特权标识", required = false, dataType = "String" ,paramType="header")
    })
    public ResultRes<QueryCourseOrderInfoRes> getById(@PathVariable(name = "courseOrderUuid" ,required = true) String courseOrderUuid) {
        return courseOrderService.getById(courseOrderUuid);
    }

    @PostMapping("/add")
    @ApiOperation(value = "新增课程订单信息", nickname = "add")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "特权标识", required = false, dataType = "String" ,paramType="header")
    })
    @SysOperLog(operModul = "课程订单管理", operType = OperEnum.ADD, operDesc = "新增课程订单信息")
    public ResultRes<String> add(@RequestBody @Validated AddCourseOrderReq req) {
        return courseOrderService.add(req);
    }

    @RequestMapping(value = "/list",method = RequestMethod.POST)
    @ApiOperation(value = "分页列表", nickname = "list")
    @SysOperLog(operModul = "课程订单管理", operType = OperEnum.SELECT, operDesc = "分页列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "特权标识", required = false, dataType = "String" ,paramType="header")
    })
    public PageRes<List<QueryCourseOrderListRes>> list(@RequestBody QueryCourseOrderListReq req) {
        return courseOrderService.list(req);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", dataType = "String" ,paramType="header")
    })
    @SysOperLog(operDesc = "课程订单管理", operType = OperEnum.DOWNLOAD, operModul = "课程订单导出")
    @ApiOperation(value = "课程订单导出", nickname = "exportCourseOrderList")
    @RequestMapping(value = "/exportCourseOrderList", method = RequestMethod.POST)
    public void exportCourseOrderList(@RequestBody QueryCourseOrderListReq exportReq, HttpServletResponse response) {
        courseOrderService.exportCourseOrderList(exportReq,response);
    }


}
