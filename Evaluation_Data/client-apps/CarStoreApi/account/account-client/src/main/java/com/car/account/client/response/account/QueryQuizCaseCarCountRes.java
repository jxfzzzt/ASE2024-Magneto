package com.car.account.client.response.account;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by Intellij IDEA.
 *
 * @author:  cjw
 * Date:  2021/1/31
 */
@Data
@ApiModel(value = "QueryQuizCaseCarCountRes", description = "查询我的提问、案例、车辆数量返回VO")
public class QueryQuizCaseCarCountRes {

    @ApiModelProperty(value = "我的提问数量", name = "quizCount")
    private Integer quizCount;

    @ApiModelProperty(value = "我的案例数量", name = "caseCount")
    private Integer caseCount;

    @ApiModelProperty(value = "我的车辆数量", name = "carCount")
    private Integer carCount;
}
