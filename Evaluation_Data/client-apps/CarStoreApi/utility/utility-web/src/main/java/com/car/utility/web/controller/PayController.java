package com.car.utility.web.controller;

import com.alibaba.fastjson.JSONArray;
import com.car.common.res.ResultRes;
import com.car.utility.client.request.pay.CreateOrderReq;
import com.car.utility.client.response.pay.CreateOrderRes;
import com.car.utility.client.response.pay.PayOrderRes;
import com.car.utility.web.common.constants.WechatPayConstants;
import com.car.utility.web.common.utils.weixin.WXPayUtil;
import com.car.utility.web.service.PayService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.BufferedOutputStream;
import java.util.Map;

/**
 * 地区管理
 * @author xlj
 */
@Slf4j
@Api(value = "PayController", tags = "支付管理")
@RequestMapping(value = "/pay", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RestController
public class PayController {

    @Autowired
    private PayService payService;


    /**
     * 统一创建支付订单接口
     * @return
     */
    @RequestMapping(value = "/createPayOrder",method = RequestMethod.POST)
    @ApiOperation(value = "统一创建支付订单接口", nickname = "createPayOrder")
    public ResultRes<CreateOrderRes> createPayOrder(@RequestBody @Valid CreateOrderReq param){
        log.info("统一创建支付订单接口请求参数：{}", JSONArray.toJSONString(param));
        return payService.createPayOrder(param);
    }

    @RequestMapping(value = "/orderRefund/{orderNo}",method = RequestMethod.POST)
    @ApiOperation(value = "统一退款订单接口", nickname = "orderRefund")
    public ResultRes orderRefund(@PathVariable("orderNo") String orderNo){
        log.info("统一退款订单接口请求参数：{}",orderNo);
        return payService.orderRefund(orderNo);
    }

    @RequestMapping(value = "/queryPayByOrderNo/{orderNo}",method = RequestMethod.GET)
    @ApiOperation(value = "根据订单号查询支付记录", nickname = "queryPayByOrderNo")
    public ResultRes<PayOrderRes> queryPayByOrderNo(@PathVariable(name = "orderNo") String orderNo){
        return payService.queryPayByOrderNo(orderNo);
    }

    /**
     * 微信支付下单异步回调
     * @return
     */
    @RequestMapping(value = "/weChartBack",method = RequestMethod.POST)
    @ApiOperation(value = "微信支付下单异步回调", nickname = "weChartBack")
    public String weChartBack(@RequestBody String xml){
        log.info("微信支付下单异步回调接受消息:{}",xml);
        String resultMsg = WechatPayConstants.FAIL_BACK_STRING;
        try{
            Map<String, String> paraMap = WXPayUtil.xmlToMap(xml);
            resultMsg = payService.weixinBack(paraMap);
            log.info("微信支付下单异步回调返回消息:{}",resultMsg);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return resultMsg;
    }

    /**
     * 支付宝支付下单异步回调
     * @param request
     * @return
     */
    @PostMapping(value = "/aliPayBack")
    public String aliPayBack(HttpServletRequest request) {
        log.info("支付宝下单异步回调接口入参:{}", JSONArray.toJSONString(request.getParameterMap()));
        try {
            return payService.aliPayBack(request);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return WechatPayConstants.FAIL;
    }


    @RequestMapping(value = "/weChartBack/refund",method = RequestMethod.POST)
    @ApiOperation(value = "微信支付退款异步回调", nickname = "weChartBack")
    public void weChartBackRefund(@RequestBody String xml, HttpServletResponse response) throws Exception{
        log.info("微信支付退款异步回调接受消息:{}",xml);
        String resultMsg = WechatPayConstants.FAIL_BACK_STRING;
        try{
            Map<String, String> paraMap = WXPayUtil.xmlToMap(xml);
            resultMsg = payService.weChartBackRefund(paraMap);
            log.info("微信支付退款异步回调返回消息:{}",resultMsg);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        BufferedOutputStream out = new BufferedOutputStream(
                response.getOutputStream());
        out.write(resultMsg.getBytes());
        out.flush();
        out.close();
    }

}
