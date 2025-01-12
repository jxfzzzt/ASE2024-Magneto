package com.car.order.web.model.addr;

import com.car.common.datasource.model.BaseModelInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * 收货地址维护表
 *
 * @author zhangyp
 * @createDate 2021-01-14
 */
@Data
@Table(name = "receive_addr")
@EqualsAndHashCode(callSuper = true)
public class ReceiveAddr extends BaseModelInfo {
    /**
     * 用户主键
     */
    @Column(name = "user_id")
    private String userId;

    /**
     * 省份
     */
    @Column(name = "province")
    private String province;
    @Column(name = "province_name")
    private String provinceName;

    /**
     * 市/区
     */
    @Column(name = "city")
    private String city;
    @Column(name = "city_name")
    private String cityName;

    /**
     * 区
     */
    @Column(name = "area")
    private String area;
    @Column(name = "area_name")
    private String areaName;

    /**
     * 标记家/公司/其他
     */
    @Column(name = "sign")
    private String sign;

    /**
     * 联系人
     */
    @Column(name = "contactor")
    private String contactor;

    /**
     * 联系电话
     */
    @Column(name = "phone")
    private String phone;

    /**
     * 是否默认地址0是1否
     */
    @Column(name = "default_flag")
    private Integer defaultFlag;

    /**
     * 详细地址
     */
    @Column(name = "addr")
    private String addr;
}