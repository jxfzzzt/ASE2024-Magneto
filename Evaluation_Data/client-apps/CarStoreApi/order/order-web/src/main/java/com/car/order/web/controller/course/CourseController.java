package com.car.order.web.controller.course;

import com.car.common.annotation.SysOperLog;
import com.car.common.enums.OperEnum;
import com.car.common.res.PageRes;
import com.car.common.res.ResultRes;
import com.car.order.client.request.course.AddCourseReq;
import com.car.order.client.request.course.QueryCourseListReq;
import com.car.order.client.response.course.QueryCourseInfoRes;
import com.car.order.client.response.course.QueryCourseListRes;
import com.car.order.web.service.course.CourseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Intellij IDEA.
 *
 * @author:  cjw
 * Date:  2021/2/21
 */
@Slf4j
@RestController
@RequestMapping("/course")
@Api(value = "CourseController", tags = "课程管理")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @GetMapping("/getById/{uuid}")
    @ApiOperation(value = "根据id查询详情", nickname = "getById")
    @SysOperLog(operModul = "课程管理", operType = OperEnum.SELECT, operDesc = "根据id查询详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "特权标识", required = false, dataType = "String" ,paramType="header")
    })
    public ResultRes<QueryCourseInfoRes> getById(@PathVariable(name = "uuid" ,required = true) String uuid) {
        return courseService.getById(uuid);
    }

    @PostMapping("/add")
    @ApiOperation(value = "新增课程信息", nickname = "add")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "特权标识", required = false, dataType = "String" ,paramType="header")
    })
    @SysOperLog(operModul = "课程管理", operType = OperEnum.ADD, operDesc = "新增课程信息")
    public ResultRes<String> add(@RequestBody @Validated AddCourseReq req) {
        return courseService.add(req);
    }

    @PutMapping("/updateById/{uuid}")
    @ApiOperation(value = "根据id修改课程信息", nickname = "updateById")
    @SysOperLog(operModul = "课程管理", operType = OperEnum.UPDATE, operDesc = "根据id修改课程信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "特权标识", required = false, dataType = "String" ,paramType="header")
    })
    public ResultRes<String> updateById(@RequestBody @Validated AddCourseReq req, @PathVariable(value = "uuid") String uuid) {
        return courseService.updateById(req, uuid);
    }

    @DeleteMapping(value = "/deleteById/{uuid}")
    @ApiOperation(value = "删除课程信息", nickname = "deleteById")
    @SysOperLog(operModul = "课程管理", operType = OperEnum.DELETE, operDesc = "删除课程信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String", paramType = "header")
    })
    public ResultRes<String> deleteById(@PathVariable("uuid") String uuid) {
        return courseService.deleteById(uuid);
    }

    @RequestMapping(value = "/list",method = RequestMethod.POST)
    @ApiOperation(value = "分页列表", nickname = "list")
    @SysOperLog(operModul = "课程管理", operType = OperEnum.SELECT, operDesc = "分页列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "特权标识", required = false, dataType = "String" ,paramType="header")
    })
    public PageRes<List<QueryCourseListRes>> list(@RequestBody QueryCourseListReq req) {
        return courseService.list(req);
    }


}
