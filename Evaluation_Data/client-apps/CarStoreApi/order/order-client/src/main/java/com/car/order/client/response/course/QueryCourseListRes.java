package com.car.order.client.response.course;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

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
@ApiModel(value = "QueryCourseListRes", description = "查看课程列表返回VO")
public class QueryCourseListRes {

	@ApiModelProperty(value = "uuid", name = "uuid")
	private String uuid;

	@ApiModelProperty(value = "课程名称", name = "courseTitle")
	private String courseTitle;

	@ApiModelProperty(value = "课程讲师", name = "courseLecturer")
	private String courseLecturer;

	@ApiModelProperty(value = "课程销量", name = "courseSalesVolume")
	private Integer courseSalesVolume;

	@ApiModelProperty(value = "课程分类uuid", name = "courseParentUuid")
	private String courseParentUuid;

	@ApiModelProperty(value = "课程分类名称", name = "courseParentUuidName")
	private String courseParentUuidName;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@ApiModelProperty(value = "创建时间", name = "createdTime")
	private Date createdTime;

	@ApiModelProperty(value = "课程封面", name = "courseCover")
	private String courseCover;

	@ApiModelProperty(value = "最新课程 0不是，1是", name = "latestCourse")
	private Integer latestCourse;
}
