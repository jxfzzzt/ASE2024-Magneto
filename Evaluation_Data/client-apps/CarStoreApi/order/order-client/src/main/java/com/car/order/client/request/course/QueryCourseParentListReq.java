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
@ApiModel(value = "QueryCourseParentListReq", description = "查询列表信息请求参数VO")
public class QueryCourseParentListReq extends PageReq {

    @ApiModelProperty(value = "教程名称", name = "courseTitle")
    private String courseTitle;
}
