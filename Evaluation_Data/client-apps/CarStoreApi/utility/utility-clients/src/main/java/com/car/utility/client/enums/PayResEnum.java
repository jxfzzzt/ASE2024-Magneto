package com.car.utility.client.enums;

import lombok.Getter;

/**
 * VO输出数据状态枚举
 * @author suhaibo
 */
@Getter
public enum PayResEnum {

    /**
     * 统一返回枚举
     */
    ERROR_CODE_10001("110001", "原支付订单不存在"),
    ERROR_CODE_10002("110002", "原支付订单支付未成功"),
    ERROR_CODE_10003("110003", "该笔订单已退款成功"),
    ERROR_CODE_10004("110004", "该笔订单已存在,请勿重新提交"),
    ERROR_CODE_10005("110005", "微信渠道退款申请失败"),
    ERROR_CODE_10006("110006", "退款失败"),
    ERROR_CODE_10007("110007", "渠道验签接口异常"),
    ERROR_CODE_10008("110008", "退款中"),
    ERROR_CODE_10009("110009", "渠道下单接口请求异常"),
    ERROR_CODE_10010("110010", "交易待支付"),
    ERROR_CODE_10011("110011", "交易成功"),
    ERROR_CODE_10012("110012", "交易失败"),
    ERROR_CODE_10013("110013", "交易结果未知"),
    ERROR_CODE_10014("110014", "交易已关闭"),
    ERROR_CODE_10015("110015", "退款成功"),
    ERROR_CODE_10016("110016", "未查询到信息"),
    ERROR_CODE_10017("110017", "币种类型不能为空"),
    ERROR_CODE_10018("110018", "支付订单号不能为空"),
    ERROR_CODE_10019("110019", "退款订单号不能为空"),
    ERROR_CODE_10020("110020", "金额请求参数错误"),
    ERROR_CODE_10021("110021", "请求参数无效"),
    ERROR_CODE_10022("110022", "请求参数长度错误"),
    ERROR_CODE_10023("110023", "租户回调地址不能为空"),
    ERROR_CODE_10024("110024", "该订单已支付成功"),
    ERROR_CODE_10025("110025", "支付渠道类型错误"),




    ;
    private String value;
    private String desc;
    PayResEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

}
