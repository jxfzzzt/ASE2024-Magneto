package com.car.order.client.request.order.order;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * Created by Intellij IDEA.
 *
 * @author:  cjw
 * Date:  2021/2/9
 */
@Data
@ApiModel(value="PayReq",description="支付订单请求VO对象")
public class PayReq {

    @ApiModelProperty(value = "订单uuid")
    @NotBlank(message = "请输入订单uuid")
    private String orderUuid;

    @ApiModelProperty(value = "场景信息")
    private JSONObject sceneInfo;

    @ApiModelProperty(value = "支付渠道  微信 ：weixin   支付宝：alipay")
    @NotBlank(message = "请输入支付渠道")
    private String channelType;

    @ApiModelProperty(value = "支付方式  JSAPI(小程序/微信), NATIVE(PC扫码支付), MWEB(H5浏览器)")
    @NotBlank(message = "请输入支付方式")
    private String paymentType;

    @ApiModelProperty(value = "支付宝支付返回地址，需传入")
    private String returnUrl;
}
