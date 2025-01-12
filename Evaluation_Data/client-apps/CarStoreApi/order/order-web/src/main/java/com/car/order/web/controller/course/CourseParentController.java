package com.car.order.web.controller.course;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.car.common.annotation.SysOperLog;
import com.car.common.enums.OperEnum;
import com.car.common.res.PageRes;
import com.car.common.res.ResultRes;
import com.car.order.client.request.course.AddCourseParentReq;
import com.car.order.client.request.course.QueryCourseParentListReq;
import com.car.order.client.response.course.QueryCourseParentInfoRes;
import com.car.order.client.response.course.QueryCourseParentListRes;
import com.car.order.web.service.course.CourseParentService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by Intellij IDEA.
 *
 * @author:  cjw
 * Date:  2021/2/21
 */
@Slf4j
@RestController
@RequestMapping("/courseParent")
@Api(value = "CourseParentController", tags = "课程分类管理")
public class CourseParentController {

	@Autowired
	private CourseParentService courseParentService;

	@GetMapping("/getById/{uuid}")
	@ApiOperation(value = "根据id查询详情", nickname = "getById")
	@SysOperLog(operModul = "课程分类管理", operType = OperEnum.SELECT, operDesc = "根据id查询详情")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "特权标识", required = false, dataType = "String", paramType = "header") })
	public ResultRes<QueryCourseParentInfoRes> getById(@PathVariable(name = "uuid", required = true) String uuid) {
		return courseParentService.getById(uuid);
	}

	@PostMapping("/add")
	@ApiOperation(value = "新增教程分类信息", nickname = "add")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "特权标识", required = false, dataType = "String", paramType = "header") })
	@SysOperLog(operModul = "课程分类管理", operType = OperEnum.ADD, operDesc = "新增教程分类信息")
	public ResultRes<String> add(@RequestBody @Validated AddCourseParentReq req) {
		return courseParentService.add(req);
	}

	@PutMapping("/updateById/{uuid}")
	@ApiOperation(value = "根据id修改课程分类信息", nickname = "updateById")
	@SysOperLog(operModul = "课程分类管理", operType = OperEnum.UPDATE, operDesc = "根据id修改课程分类信息")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "特权标识", required = false, dataType = "String", paramType = "header") })
	public ResultRes<String> updateById(@RequestBody @Validated AddCourseParentReq req,
			@PathVariable(value = "uuid") String uuid) {
		return courseParentService.updateById(req, uuid);
	}

	@DeleteMapping(value = "/deleteById/{uuid}")
	@ApiOperation(value = "删除课程分类信息", nickname = "deleteById")
	@SysOperLog(operModul = "课程分类管理", operType = OperEnum.DELETE, operDesc = "删除课程分类信息")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String", paramType = "header") })
	public ResultRes<String> deleteById(@PathVariable("uuid") String uuid) {
		return courseParentService.deleteById(uuid);
	}

	@RequestMapping(value = "/list", method = RequestMethod.POST)
	@ApiOperation(value = "分页列表", nickname = "list")
	@SysOperLog(operModul = "课程分类管理", operType = OperEnum.SELECT, operDesc = "分页列表")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "特权标识", required = false, dataType = "String", paramType = "header") })
	public PageRes<List<QueryCourseParentListRes>> list(@RequestBody QueryCourseParentListReq req) {
		return courseParentService.list(req);
	}

	@RequestMapping(value = "/listNewest", method = RequestMethod.POST)
	@ApiOperation(value = "分页列表:最新分类", nickname = "listNewest")
	@SysOperLog(operModul = "课程分类管理:最新分类", operType = OperEnum.SELECT, operDesc = "分页列表:最新分类")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "特权标识", required = false, dataType = "String", paramType = "header") })
	public PageRes<List<QueryCourseParentListRes>> listNewest(@RequestBody QueryCourseParentListReq req) {
		return courseParentService.listNewest(req);
	}

	@RequestMapping(value = "/listGeneral", method = RequestMethod.POST)
	@ApiOperation(value = "分页列表:普通分类", nickname = "listGeneral")
	@SysOperLog(operModul = "课程分类管理:普通分类", operType = OperEnum.SELECT, operDesc = "分页列表:普通分类")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "特权标识", required = false, dataType = "String", paramType = "header") })
	public PageRes<List<QueryCourseParentListRes>> listGeneral(@RequestBody QueryCourseParentListReq req) {
		return courseParentService.listGeneral(req);
	}

}
