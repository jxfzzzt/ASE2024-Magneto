package com.car.order.client.request.course;

import com.car.common.req.PageReq;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by Intellij IDEA.
 *
 * @author:  cjw
 * Date:  2021/2/21
 */
@Data
@ApiModel(value = "QueryCourseListReq", description = "查询课程列表请求VO")
public class QueryCourseListReq extends PageReq {

	@ApiModelProperty(value = "课程讲师", name = "courseLecturer")
	private String courseLecturer;

	@ApiModelProperty(value = "课程名称", name = "courseTitle")
	private String courseTitle;

	@ApiModelProperty(value = "对应教程uuid", name = "courseParentUuid")
	private String courseParentUuid;

	@ApiModelProperty(value = "最新课程: 0不是，1是", name = "latestCourse")
	private Integer latestCourse;

}
