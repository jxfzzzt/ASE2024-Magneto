package com.car.order.web.model.dtc;

import com.car.common.datasource.model.BaseModelInfo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * Created by Intellij IDEA.
 *
 * @author:  cjw
 * Date:  2021/2/17
 */
@Data
@Table(name = "dtc_order")
public class DtcOrder extends BaseModelInfo {

    /**
     * dtc故障uuid
     */
    @Column(name = "dtc_uuid")
    private String dtcUuid;

    /**
     * 订单编号
     */
    @Column(name = "order_num")
    private String orderNum;

    /**
     * 订单金额
     */
    @Column(name = "order_amount")
    private BigDecimal orderAmount;

    /**
     * 支付方式 0 微信支付 1 支付宝支付
     */
    @Column(name = "pay_type")
    private Integer payType;

    /**
     * 订单状态 0 待支付 1 已支付 2: 已取消 3:退款中  4:退款成功  5:退款失败
     */
    @Column(name = "order_sts")
    private Integer orderSts;

    /**
     * 查看次数,最多可看3次,超过3次重新购买
     */
    @Column(name = "read_count")
    private Integer readCount;

    /**
     * 购买者uuid
     */
    @Column(name = "buyer_uuid")
    private String buyerUuid;

    /**
     * 购买者姓名
     */
    @Column(name = "buyer_name")
    private String buyerName;

    /**
     * 购买者手机号码
     */
    @Column(name = "buyer_mobile")
    private String buyerMobile;

    /**
     * 发布者uuid
     */
    @Column(name = "issuer_uuid")
    private String issuerUuid;

    /**
     * 发布者姓名
     */
    @Column(name = "issuer_name")
    private String issuerName;

    /**
     * 发布者手机号码
     */
    @Column(name = "issuer_mobile")
    private String issuerMobile;

    /**
     * 发布人类型：0：后台发布，1：技师 ，2：店铺
     */
    @Column(name = "dtc_issuer_type")
    private Integer dtcIssuerType;

}
