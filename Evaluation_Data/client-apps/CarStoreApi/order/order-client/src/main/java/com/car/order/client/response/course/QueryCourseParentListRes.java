package com.car.order.client.response.course;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * Created by Intellij IDEA.
 *
 * @author:  cjw
 * Date:  2021/2/21
 */
@Data
@ApiModel(value = "QueryCourseParentInfoRes", description = "查询课程分类列表返回VO")
public class QueryCourseParentListRes {

    @ApiModelProperty(value = "uuid", name = "uuid")
    private String uuid;

    @ApiModelProperty(value = "教程名称", name = "courseTitle")
    private String courseTitle;

    @ApiModelProperty(value = "教程封面图片url", name = "courseCover")
    private String courseCover;

    @ApiModelProperty(value = "教程描述图文", name = "courseDesc")
    private String courseDesc;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "创建时间", name = "createdTime")
    private Date createdTime;

    @ApiModelProperty(value = "是否是最新的数据，true：是， false：否", name = "isNewest")
    private boolean newest;

}
