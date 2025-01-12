package com.car.account.web.model.vehicle;

import com.car.common.datasource.model.BaseModelInfo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * @author zhouz
 * @date 2020/12/19
 */
@Data
@Table(name = "vehicle_user")
public class VehicleUser extends BaseModelInfo {

    /**
     * 用户类型 1：注册用户  2：车主
     */
    @Column(name = "user_type")
    private Integer userType;

    /**
     * 用户名
     */
    @Column(name = "user_name")
    private String userName;

    /**
     * 证件类型,对应字典表uuid
     */
    @Column(name = "certificate_type")
    private String certificateType;

    /**
     * 手机号码
     */
    @Column(name = "mobile")
    private String mobile;

    /**
     * 证件号码
     */
    @Column(name = "certificate_num")
    private String certificateNum;

    /**
     * 地址-省 对应地区表uuid
     */
    @Column(name = "address_province")
    private String addressProvince;

    /**
     * 地址-市,对应地区表uuid
     */
    @Column(name = "address_city")
    private String addressCity;

    /**
     * 详细地址
     */
    @Column(name = "address_detail")
    private String addressDetail;

    /**
     * 开户名
     */
    @Column(name = "account_name")
    private String accountName;

    /**
     * 开户银行,对应字典表uuid
     */
    @Column(name = "deposit_bank")
    private String depositBank;

    /**
     * 支行名称
     */
    @Column(name = "sub_branch_name")
    private String subBranchName;

    /**
     * 银行卡号
     */
    @Column(name = "card_numbers")
    private String cardNumbers;

    /**
     * 支付宝账号
     */
    @Column(name = "alipay_account")
    private String alipayAccount;

    /**
     * 证件类型名称
     */
    @Transient
    private String certificateTypeName;

    /**
     * 省份名称
     */
    @Transient
    private String addressProvinceName;

    /**
     * 城市名称
     */
    @Transient
    private String addressCityName;

    @Column(name = "photo_img_url")
    private String photoImgUrl;

    /**
     * 纬度
     */
    @Column(name = "latitude")
    private Float latitude;

    /**
     * 经度
     */
    @Column(name = "longitude")
    private Float longitude;
}
