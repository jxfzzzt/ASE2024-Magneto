package com.car.order.client.request.course;

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
@ApiModel(value = "AddCourseParentReq", description = "新增课程分类VO")
public class AddCourseParentReq {

    @ApiModelProperty(value = "教程名称", name = "courseTitle")
    private String courseTitle;

    @ApiModelProperty(value = "教程封面图片url", name = "courseCover")
    private String courseCover;

    @ApiModelProperty(value = "教程描述图文", name = "courseDesc")
    private String courseDesc;

}
