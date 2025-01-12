package com.car.order.client.request.course;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * Created by Intellij IDEA.
 *
 * @author:  cjw
 * Date:  2021/2/22
 */
@Data
@ApiModel(value = "AddCourseOrderReq", description = "新增课程订单请求VO")
public class AddCourseOrderReq {

    @ApiModelProperty(value = "课程uuid", name = "courseUuid")
    @NotBlank(message = "请输入课程uuid")
    private String courseUuid;
}
