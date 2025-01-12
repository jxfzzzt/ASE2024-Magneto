package com.car.account.web.model.store;

import com.car.common.datasource.model.BaseModelInfo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * @author zhouz
 * @date 2020/12/19
 */
@Data
@Table(name = "store")
public class Store extends BaseModelInfo {

    /**
     * 店铺名称
     */
    @Column(name = "store_name")
    private String storeName;

    /**
     * 店铺类型,对应字典表uuid
     */
    @Column(name = "store_type")
    private String storeType;

    /**
     * 品牌
     */
    @Column(name = "brand")
    private String brand;
    /**
     * 公司名称
     */
    @Column(name = "company_name")
    private String companyName;

    /**
     * 公司地址-省,对应地区表uuid
     */
    @Column(name = "company_address_province")
    private String companyAddressProvince;

    /**
     * 公司地址-市,对应地区表uuid
     */
    @Column(name = "company_address_city")
    private String companyAddressCity;

    /**
     * 公司地址-县镇,对应地区表uuid
     */
    @Column(name = "company_address_county")
    private String companyAddressCounty;

    /**
     * 公司地址-详细信息
     */
    @Column(name = "company_address_detail")
    private String companyAddressDetail;

    /**
     * 审核状态,对应字典表(0:待审核 1:审核通过 2:审核驳回)
     */
    @Column(name = "check_sts")
    private Integer checkSts;

    /**
     * 驳回详情
     */
    @Column(name = "reject_detail")
    private String rejectDetail;

    /**
     * 营业时间
     */
    @Column(name = "on_time_arr")
    private String onTimeArr;

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

    /**
     * 平台服务费率
     */
    @Column(name="platform_fee")
    private BigDecimal platformFee;

}
