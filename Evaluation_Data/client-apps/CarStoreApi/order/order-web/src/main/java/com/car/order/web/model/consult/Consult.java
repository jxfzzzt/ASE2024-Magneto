package com.car.order.web.model.consult;

import com.car.common.datasource.model.BaseModelInfo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author zhouz
 * @date 2021/1/2
 */
@Data
@Table(name = "consult")
public class Consult extends BaseModelInfo {

    /**
     * 咨询标题
     */
    @Column(name = "title")
    private String title;

    /**
     * 技师uuid
     */
    @Column(name = "technician_uuid")
    private String technicianUuid;

    /**
     * 技师姓名
     */
    @Column(name = "technician_name")
    private String technicianName;

    /**
     * 技师手机号
     */
    @Column(name = "technician_mobile")
    private String technicianMobile;

    /**
     * 技师头像
     */
    @Column(name = "technician_img_url")
    private String technicianImgUrl;

    /**
     * 车主uuid
     */
    @Column(name = "car_owner_uuid")
    private String carOwnerUuid;

    /**
     * 车主姓名
     */
    @Column(name = "car_owner_name")
    private String carOwnerName;

    /**
     * 车主手机号
     */
    @Column(name = "car_owner_mobile")
    private String carOwnerMobile;

    /**
     * 车主头像
     */
    @Column(name = "car_owner_img_url")
    private String carOwnerImgUrl;

    /**
     * 答复状态 0 未答复 1 已答复
     */
    @Column(name = "answer_sts")
    private Integer answerSts;

    /**
     * 答复时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Column(name = "answer_time")
    private Date answerTime;

    /**
     * 支咨询审核状态 0 待审核 1 审核通过 2 审核驳回
     */
    @Column(name = "consult_check_sts")
    private Integer consultCheckSts;

    /**
     * 回答审核状态 0 待审核 1 审核通过 2 审核驳回
     */
    @Column(name = "answer_check_sts")
    private Integer answerCheckSts;

    /**
     * 采纳结果 0 满意 1 不满意
     */
    @Column(name = "accept_result")
    private Integer acceptResult;

    /**
     * 咨询类型  1：技师提问  2：全国技师提问
     */
    @Column(name = "consult_type")
    private Integer consultType;


    /**
     * 车辆品牌
     */
    @Column(name = "vehicle_brand")
    private String vehicleBrand;

    /**
     * 车型类型
     */
    @Column(name = "vehicle_model")
    private String vehicleModel;

    /**
     * owneruuid类型 1:车主，2：技师，3:店铺
     */
    @Column(name = "car_owner_Type")
    private Integer carOwnerType;
}
