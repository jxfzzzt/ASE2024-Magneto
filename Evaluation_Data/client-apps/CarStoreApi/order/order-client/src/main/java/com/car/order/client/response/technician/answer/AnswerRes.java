package com.car.order.client.response.technician.answer;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author zhangyp
 * @date 2021/1/29 2:03
 */
@Data
@ApiModel
public class AnswerRes {

    /**
     * 咨询uuid
     */
    @ApiModelProperty(value="咨询uuid",name="consultUuid")
    private String consultUuid;
    /**
     * 咨询描述
     */
    @ApiModelProperty(value="咨询描述",name="consultDesc")
    private String consultDesc;

    /**
     * 回答描述
     */
    @ApiModelProperty(value="回答描述",name="answerDesc")
    private String answerDesc;


    @ApiModelProperty(value="咨询图片地址",name="consultImgUrlList")
    private List<String> consultImgUrlList;

    @ApiModelProperty(value="咨询图片地址",name="answerImgUrlList")
    private List<String> answerImgUrlList;
    /**
     * 咨询标题
     */
    @ApiModelProperty(value="咨询标题",name="title")
    private String title;







    /**
     * 技师uuid
     */
    @ApiModelProperty(value="技师uuid",name="technicianUuid")
    private String technicianUuid;

    /**
     * 技师姓名
     */
    @ApiModelProperty(value="技师姓名",name="technicianName")
    private String technicianName;

    /**
     * 技师头像
     */
    @ApiModelProperty(value="技师头像",name="technicianImgUrl")
    private String technicianImgUrl;

    /**
     * 车主姓名
     */
    @ApiModelProperty(value="车主姓名",name="carOwnerName")
    private String carOwnerName;


    /**
     * 车主uuid
     */
    @ApiModelProperty(value="车主uuid",name="carOwnerUuid")
    private String carOwnerUuid;

    /**
     * 车主头象
     */
    @ApiModelProperty(value="车主头象",name="carOwnerImgUrl")
    private String carOwnerImgUrl;

    /**
     * 答复状态 0 未答复 1 已答复
     */
    @ApiModelProperty(value="答复状态 0 未答复 1 已答复",name="answerSts")
    private Boolean answerSts;

    /**
     * 答复时间
     */
    @ApiModelProperty(value="答复时间",name="answerTime")
    private Date answerTime;

    /**
     * 咨询审核状态 0 待审核 1 审核通过 2 审核驳回
     */
    @ApiModelProperty(value="咨询审核状态 0 待审核 1 审核通过 2 审核驳回",name="consultCheckSts")
    private Short consultCheckSts;

    /**
     * 回答审核状态 0 待审核 1 审核通过 2 审核驳回
     */
    @ApiModelProperty(value="回答审核状态 0 待审核 1 审核通过 2 审核驳回",name="answerCheckSts")
    private Short answerCheckSts;

    /**
     * 采纳结果 0 满意 1 不满意
     */
    @ApiModelProperty(value="采纳结果 0 满意 1 不满意",name="acceptResult")
    private Boolean acceptResult;

    /**
     * 备注/咨询审核状态驳回原因
     */
    @ApiModelProperty(value="备注/咨询审核状态驳回原因",name="consultRemarks")
    private String consultRemarks;

    /**
     * 备注/回答审核状态驳回原因
     */
    @ApiModelProperty(value="备注/回答审核状态驳回原因",name="answerRemarks")
    private String answerRemarks;
}
