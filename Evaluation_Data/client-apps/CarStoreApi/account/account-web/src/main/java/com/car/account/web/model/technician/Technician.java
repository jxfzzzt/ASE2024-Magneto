package com.car.account.web.model.technician;

import com.car.common.datasource.model.BaseModelInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * 技师信息
 * @author xlj
 * @date 2020/12/19
 */
@Data
@Table(name = "technician")
public class Technician extends BaseModelInfo {

    /**
     * 用户名
     */
    @Column(name = "user_name")
    private String userName;

    /**
     * 手机号码
     */
    @Column(name = "mobile")
    private String mobile;

    /**
     * 证件类型
     */
    @Column(name = "certificate_type")
    private String certificateType;

    /**
     * 证件号码
     */
    @Column(name = "certificate_num")
    private String certificateNum;

    /**
     * 地址-省,对应地区表uuid
     */
    @Column(name = "address_province")
    private String addressProvince;

    /**
     * 地址-市,对应地区表uuid
     */
    @Column(name = "address_city")
    private String addressCity;

    /**
     * 公司地址-县镇,对应地区表uuid
     */
    @Column(name = "address_county")
    private String addressCounty;

    /**
     * 地址详情
     */
    @Column(name = "address_detail")
    private String addressDetail;

    /**
     * 地址详情经度
     */
    @Column(name = "address_longitude")
    private Float addressLongitude;

    /**
     * 地址详情纬度
     */
    @Column(name = "address_latitude")
    private Float addressLatitude;

    /**
     * 技术类型，对应字典表
     */
    @Column(name = "technology_type")
    private String technologyType;

    /**
     * 直属亲戚手机
     */
    @Column(name = "relative_mobile")
    private String relativeMobile;

    /**
     * 驾驶证正面
     */
    @Column(name = "driver_license_url")
    private String driverLicenseUrl;

    /**
     * 驾驶证反面
     */
    @Column(name = "driver_license_back_url")
    private String driverLicenseBackUrl;

    /**
     * 身份证正面
     */
    @Column(name = "identity_card_url")
    private String identityCardUrl;

    /**
     * 身份证反面
     */
    @Column(name = "identity_card_back_url")
    private String identityCardBackUrl;

    /**
     * 无犯罪记录证明
     */
    @Column(name = "no_crime_url")
    private String noCrimeUrl;
    /**
     * 健康检查证明
     */
    @Column(name = "health_check_url")
    private String healthCheckUrl;

    /**
     * 技师等级:国家等级鉴定 对应字典表uuid
     */
    @Column(name = "state_verification")
    private String stateVerification;

    /**
     * 技师等级:主机厂认证 对应字典表uuid
     */
    @Column(name = "host_authentication")
    private String hostAuthentication;

    /**
     * 审核状态(0:待审核 1:审核通过 2:审核驳回)
     */
    @Column(name = "check_sts")
    private Integer checkSts;

    /**
     * 驳回详情
     */
    @Column(name = "reject_detail")
    private String rejectDetail;

    /**
     * 关联店铺
     */
    @Column(name = "relation_store_uuid")
    private String relationStoreUuid;

    /**
     * 订单总数
     */
    @Column(name = "order_count")
    private Integer orderCount ;

    /**
     * 案例总数
     */
    @Column(name = "case_count")
    private Integer caseCount;

    /**
     * 问答总数
     */
    @Column(name = "qa_count")
    private Integer qaCount;

    /**
     * 现场支持总数
     */
    @Column(name = "support_count")
    private Integer supportCount;

    /**
     * 评分总次数
     */
    @Column(name = "score_count")
    private Integer scoreCount;

    /**
     * 综合评分
     */
    @Column(name = "score")
    private BigDecimal score;

    @Column(name = "working_year")
    private Integer workingYear;

    /**
     * 问答金额
     */
    @Column(name = "answer_amt")
    private BigDecimal answerAmt;

    @Column(name = "answer_sort_weight")
    private Integer answerSortWeight;

    /**
     * 技师头像链接地址
     */
    @Column(name = "photo_img_url")
    private String photoImgUrl;
    
    /**
     * 车友邦技能等级标签(0=普通,1=专家)
     */
    @Column(name = "cyb_auth")
    private Integer cybAuth;
}
