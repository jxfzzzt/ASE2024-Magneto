package com.car.utility.web.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.car.common.enums.ResEnum;
import com.car.common.enums.StsEnum;
import com.car.common.exception.BusinessException;
import com.car.common.res.ResultRes;
import com.car.common.utils.DateUtil;
import com.car.common.utils.UuidUtils;
import com.car.order.client.feign.PayOrderFeign;
import com.car.utility.client.enums.*;
import com.car.utility.client.request.pay.CreateOrderReq;
import com.car.utility.client.response.pay.CreateOrderRes;
import com.car.utility.client.response.pay.PayOrderRes;
import com.car.utility.web.common.constants.ConfigConstants;
import com.car.utility.web.common.constants.WechatKeyConstants;
import com.car.utility.web.common.constants.WechatPayConstants;
import com.car.utility.web.common.utils.AESUtils;
import com.car.utility.web.common.utils.AlipayConfig;
import com.car.utility.web.common.utils.DecodeUtil;
import com.car.utility.web.common.utils.weixin.ChannelContext;
import com.car.utility.web.common.utils.weixin.WXPayUtil;
import com.car.utility.web.common.utils.weixin.WeChatPayRequestUtil;
import com.car.utility.web.mapper.PayMapper;
import com.car.utility.web.model.PayOrder;
import com.car.utility.web.service.PayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Slf4j
@Service
public class PayServiceImpl implements PayService {

    @Autowired
    PayMapper payMapper;

    @Autowired
    ConfigConstants configConsts;


    @Autowired
    private PayOrderFeign payOrderFeign;

    /**
     * 统一创建支付订单接口
     * @param param
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultRes<CreateOrderRes> createPayOrder(CreateOrderReq param) {
        if(ChannelTypeEnum.enumOfDesc(param.getChannelType()) == null){
            throw new BusinessException(ResEnum.API_ERROR.getValue(),"channelType param is fail");
        }
        if(PaymentTypeEnum.enumOfDesc(param.getPaymentType()) == null){
            throw new BusinessException(ResEnum.API_ERROR.getValue(),"paymentType param is fail");
        }
        PayOrder order = getOrderByOrderNo(param.getOrderNo());
        if(StringUtils.isEmpty(order)){
            //创建订单
            order = insertPayOrder(param);
        }else{
            //支付成功，不在继续进行支持
            if (OrderStatusEnum.SUCCESS_PAYMENT.getValue().equals(order.getOrderStatus())) {
                return ResultRes.error(PayResEnum.ERROR_CODE_10024.getValue(),PayResEnum.ERROR_CODE_10024.getValue());
            }
            order.setPayAmount(param.getPayAmount());
            order.setChannelType(param.getChannelType());
            payMapper.updateByPrimaryKey(order);
            //修改订单UUID，防止重复
            String newUuid = UuidUtils.getUuid();
            log.info("由于订单已存在，重新调整订单流水ID，当前新流水ID:{}，旧流水ID：{}",newUuid,newUuid);
            payMapper.updatePayOrderUuid(order.getUuid(),newUuid);
            order.setUuid(newUuid);
        }

        ChannelContext channelContext = new ChannelContext();
        channelContext.setCreateOrderParam(param);
        channelContext.setPayOrder(order);
        if(ChannelTypeEnum.weixin.getValue().equalsIgnoreCase(order.getChannelType())){
            String paymentType = order.getPaymentType();
            // 1.创单请求参数
            Object createOrderRequest = makeCreateOrderRequest(channelContext);
            log.info("创建订单请求参数： >> {}", JSON.toJSONString(createOrderRequest));
            // 2.创单返回结果
            Object createOrderResult = createOrder(createOrderRequest);
            log.info("创建订单返回结果： >>{}",  JSON.toJSONString(createOrderResult));
            // 3.创单结果解析
            return makeCreateOrderResponse(createOrderResult,order);
        }if(ChannelTypeEnum.alipay.getValue().equalsIgnoreCase(order.getChannelType())){
            //创建支付宝订单
            return makeCreateAliPayOrderResponse(channelContext);
        }else{
            throw new BusinessException(PayResEnum.ERROR_CODE_10025.getValue(),PayResEnum.ERROR_CODE_10025.getDesc());
        }
    }

    /**
     * 创建支付宝订单
     * @param channelContext
     * @return
     */
    private ResultRes<CreateOrderRes> makeCreateAliPayOrderResponse(ChannelContext channelContext){
        CreateOrderReq createOrderParam = channelContext.getCreateOrderParam();
        PayOrder order = channelContext.getPayOrder();
        // 商户订单号，商户网站订单系统中唯一订单号，必填
        String out_trade_no = order.getUuid();
        // 订单名称，必填
        String subject = createOrderParam.getGoodsName();
        // 付款金额，必填
        String total_amount= String.valueOf(createOrderParam.getPayAmount());
        // 商品描述，可空
        String body = createOrderParam.getGoodsDesc();
        // 超时时间 可空
        String timeout_express="10m";
        // 销售产品码 必填
        String product_code="QUICK_WAP_WAY";
        /**********************/
        // SDK 公共请求类，包含公共请求参数，以及封装了签名与验签，开发者无需关注签名与验签
        //调用RSA签名方式
        AlipayClient client = new DefaultAlipayClient(AlipayConfig.URL, AlipayConfig.APP_ID, AlipayConfig.PKCS8, AlipayConfig.FORMAT, AlipayConfig.CHARSET, AlipayConfig.ALIPAY_PUBLIC_KEY,AlipayConfig.SIGNTYPE);
        AlipayTradeWapPayRequest alipay_request=new AlipayTradeWapPayRequest();
        // 封装请求支付信息
        AlipayTradeWapPayModel model=new AlipayTradeWapPayModel();
        model.setOutTradeNo(out_trade_no);
        model.setSubject(subject);
        model.setTotalAmount(total_amount);
        model.setBody(body);
        model.setTimeoutExpress(timeout_express);
        model.setProductCode(product_code);
        alipay_request.setBizModel(model);
        // 设置异步通知地址
        alipay_request.setNotifyUrl(configConsts.getAlipay_payNotifyUrl());
        // 设置同步地址
        alipay_request.setReturnUrl(createOrderParam.getReturnUrl());
        //返回输出前端数据
        String form = null;
        try {
            form = client.pageExecute(alipay_request).getBody();
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        CreateOrderRes createOrderRes = new CreateOrderRes();
        createOrderRes.setBody(form);
        createOrderRes.setOrderNo(order.getTenantOrderNo());
        createOrderRes.setPayOrderNo(order.getUuid());
        return ResultRes.success(createOrderRes);
    }


    /**
     *
     * @param paraMap
     * @return
     */
    @Override
    public String weixinBack(Map<String, String> paraMap) {
        if (paraMap == null) {
            return "responseMap can't be null/empty";
        }
        if (!WechatPayConstants.SUCCESS.equals(paraMap.get(WechatKeyConstants.RETURN_CODE))
                || !WechatPayConstants.SUCCESS.equals(paraMap.get(WechatKeyConstants.RESULT_CODE))) {
            log.error(
                    "getPaymentResultFromNotifycation failure, return_code : {}, return_msg : {},result_code : {},err_code :{},err_code_des :{}",
                    paraMap.get(WechatKeyConstants.RETURN_CODE), paraMap.get(WechatKeyConstants.RETURN_MSG),
                    paraMap.get(WechatKeyConstants.RESULT_CODE), paraMap.get(WechatKeyConstants.ERR_CODE),
                    paraMap.get(WechatKeyConstants.ERR_CODE_DES));
            return WechatPayConstants.FAIL_BACK_STRING;
        }
        String outTradeNo = paraMap.get(WechatKeyConstants.OUT_TRADE_NO);
        if (org.apache.commons.lang.StringUtils.isBlank(outTradeNo)) {
            log.info("微信异步通知outTradeNo isBlank");
            return WechatPayConstants.FAIL_BACK_STRING;
        }
        //查询订单；数据校验
        PayOrder payOrder = payMapper.selectByPrimaryKey(outTradeNo);
        if (payOrder == null) {
            log.info("{}微信异步通知，paymentChannelOrder is null", outTradeNo);
            return WechatPayConstants.FAIL_BACK_STRING;
        }
        String backString = checkPayOrderData(payOrder, outTradeNo, paraMap);
        if (!StringUtils.isEmpty(backString)) {
            return backString;
        }
        if (org.apache.commons.lang.StringUtils.isNotBlank(paraMap.get(WechatKeyConstants.TIME_END))) {
            String timeEnd = paraMap.get(WechatKeyConstants.TIME_END);
            String channelOrderDate = timeEnd.substring(0, 8);
            String channelOrderTime = timeEnd.substring(8);
            payOrder.setChannelOrderDate(channelOrderDate);
            payOrder.setChannelOrderTime(channelOrderTime);
        }
        // 获取渠道交易订单号
        payOrder.setChannelOrderNo(paraMap.get(WechatKeyConstants.TRANSACTION_ID));
        if (WechatPayConstants.SUCCESS.equals(paraMap.get(WechatKeyConstants.RESULT_CODE))) {
            payOrder.setOrderStatus(Integer.valueOf(OrderStatusEnum.SUCCESS_PAYMENT.getValue()));
            payOrder.setChannelOrderCode(paraMap.get(WechatKeyConstants.RETURN_CODE));
            payOrder.setChannelOrderDesc(paraMap.get(WechatKeyConstants.RETURN_MSG));
            payOrder.setPayOrderCode(PayResEnum.ERROR_CODE_10011.getValue());
            payOrder.setPayOrderDesc(PayResEnum.ERROR_CODE_10011.getDesc());

        } else {
            payOrder.setOrderStatus(Integer.valueOf(OrderStatusEnum.FAILURE_PAYMENT.getValue()));
            payOrder.setChannelOrderCode(paraMap.get(WechatKeyConstants.ERR_CODE));
            payOrder.setChannelOrderDesc(paraMap.get(WechatKeyConstants.ERR_CODE_DES));
            //将订单状态修改为支付失败
            log.error("支付失败返回内容：{}", JSONArray.toJSONString(payOrder));
        }
        payOrder.setLastUpdatedTime(new Date());
        payMapper.updateByPrimaryKey(payOrder);

        if(OrderStatusEnum.SUCCESS_PAYMENT.getValue().equals(payOrder.getOrderStatus())){
            ResultRes<String> resultRes = payOrderFeign.updateOrderPaySts(payOrder.getTenantOrderNo());
            if (!resultRes.isSuccess()) {
                log.error("支付成功调用updateOrderPaySts接口失败返回：{}", JSONArray.toJSONString(resultRes));
                throw new BusinessException(ResEnum.PAY_BACK_ERROR);
            }
        }
        //修改订单状态
        return WechatPayConstants.SUCCESS_BACK_STRING;
    }

    /**
     * 根据订单号查询支付记录
     * @param orderNo
     * @return
     */
    @Override
    public ResultRes<PayOrderRes> queryPayByOrderNo(String orderNo) {
        PayOrder search = new PayOrder();
        search.setTenantOrderNo(orderNo);
        PayOrder payOrder = payMapper.selectOne(search);

        PayOrderRes res = new PayOrderRes();
        if(!StringUtils.isEmpty(payOrder)){
            BeanUtils.copyProperties(payOrder,res);
        }
        return ResultRes.success(res);
    }

    /**
     * 统一退款订单接口
     * @param orderNo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultRes orderRefund(String orderNo) {
        PayOrder search = new PayOrder();
        search.setTenantOrderNo(orderNo);
        PayOrder order = payMapper.selectOne(search);
        if(StringUtils.isEmpty(order) || !(OrderStatusEnum.SUCCESS_PAYMENT.getValue().equals(order.getOrderStatus()))){
            log.error("通过订单编码查询支付流水未匹配到已支付的订单，请求参数：{}",orderNo);
            throw new BusinessException(PayResEnum.ERROR_CODE_10004.getValue(),PayResEnum.ERROR_CODE_10004.getDesc());
        }
        if(ChannelTypeEnum.weixin.getValue().equalsIgnoreCase(order.getChannelType())){
            // 1.创单请求参数
            Object createOrderRequest = makeRefundRequest(order);
            log.info("创建退款订单请求参数： >> {}", JSON.toJSONString(createOrderRequest));
            // 2.创单返回结果
            Object refundResult = orderRefundRequest(createOrderRequest);
            log.info("创建退款订单返回结果： >>{}",  JSON.toJSONString(refundResult));
            // 3.创单结果解析
            return makeOrderRefundResponse(refundResult,order);
        }
        return null;
    }

    /**
     * 微信退款
     * @param paraMap
     * @return
     */
    @Override
    //@Transactional(rollbackFor = Exception.class)
    public String weChartBackRefund(Map<String, String> paraMap) {
        if (paraMap == null) {
            return "responseMap can't be null/empty。";
        }
        if (!WechatPayConstants.SUCCESS.equals(paraMap.get(WechatKeyConstants.RETURN_CODE))) {
            return WechatPayConstants.FAIL_BACK_STRING;
        }
        String responseXml = DecodeUtil.decryptData(paraMap.get(WechatKeyConstants.REQ_INFO));
        if(StringUtils.isEmpty(responseXml)){
            return WechatPayConstants.FAIL_BACK_STRING;
        }
        Map<String, String> rootMap = null;
        try{
            rootMap = WXPayUtil.xmlToMap(responseXml);
        }catch (Exception ex){
            ex.printStackTrace();
            log.error("微信退款--->解析req_info消息体异常，异常原因为：{}",ex.getMessage());
            return WechatPayConstants.FAIL_BACK_STRING;
        }
        String out_trade_no = rootMap.get(WechatKeyConstants.OUT_TRADE_NO);
        //根据支付流水订单号查询支付信息
        PayOrder order = payMapper.selectByPrimaryKey(out_trade_no);
        if(StringUtils.isEmpty(order)){
            log.error("微信退款--->根据支付流水订单号：{}未匹配到订单信息",out_trade_no);
            return WechatPayConstants.FAIL_BACK_STRING;
        }
        order.setRefundSerial(rootMap.get("refund_id"));
        order.setOrderStatus(OrderStatusEnum.SUCCESS_REFUND.getValue());
        payMapper.updateByPrimaryKey(order);

        //TODO 通知业务平台，当前已退款完成
        //portalFeign.refundNotice(order.getTenantOrderNo());
        return WechatPayConstants.SUCCESS_BACK_STRING;
    }

    /**
     * 支付宝支付下单异步回调
     * @param request
     * @return
     */
    @Override
    public String aliPayBack(HttpServletRequest request) {
        try{
            Map<String,String> params = new HashMap<String,String>();
            Map requestParams = request.getParameterMap();
            for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
                String name = (String) iter.next();
                String[] values = (String[]) requestParams.get(name);
                String valueStr = "";
                for (int i = 0; i < values.length; i++) {
                    valueStr = (i == values.length - 1) ? valueStr + values[i]
                            : valueStr + values[i] + ",";
                }
                //乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
                //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
                params.put(name, valueStr);
            }
            log.info("params is : {}", JSONArray.toJSONString(params));
            //商户订单号
            String out_trade_no = request.getParameter("out_trade_no");
            boolean verify_result = AlipaySignature.rsaCheckV1(params, AlipayConfig.ALIPAY_PUBLIC_KEY, AlipayConfig.CHARSET, "RSA2");
            if(verify_result) {
                // 查询订单
                PayOrder payOrder = payMapper.selectByPrimaryKey(out_trade_no);
                if (payOrder == null) {
                    log.info("{}微信异步通知，paymentChannelOrder is null", out_trade_no);
                    return WechatPayConstants.FAIL_BACK_STRING;
                }
                payOrder.setOrderStatus(Integer.valueOf(OrderStatusEnum.SUCCESS_PAYMENT.getValue()));
                payOrder.setPayOrderCode(PayResEnum.ERROR_CODE_10011.getValue());
                payOrder.setPayOrderDesc(PayResEnum.ERROR_CODE_10011.getDesc());
                payOrder.setLastUpdatedTime(new Date());
                payMapper.updateByPrimaryKey(payOrder);

                ResultRes<String> resultRes = payOrderFeign.updateOrderPaySts(payOrder.getTenantOrderNo());
                if (!resultRes.isSuccess()) {
                    log.error("支付成功调用updateOrderPaySts接口失败返回：{}", JSONArray.toJSONString(resultRes));
                    throw new BusinessException(ResEnum.PAY_BACK_ERROR);
                }
                return WechatPayConstants.SUCCESS.toLowerCase();
            }else{
                log.error("支付宝异步通知签名认证失败！！！");
                return WechatPayConstants.FAIL;
            }
        }catch (Exception ex){
            ex.printStackTrace();
            return WechatPayConstants.FAIL;
        }
    }

    /**
     * 解析支付请求消息体
     * @param refundResult
     * @param order
     * @return
     */
    private ResultRes makeOrderRefundResponse(Object refundResult, PayOrder order) {
        Map<String, String> responseMap = null;
        try {
            responseMap = WXPayUtil.xmlToMap((String) refundResult);
        } catch (Exception e1) {
            order.setRefundCode(RefundCodeEnum.ERROR_CODE_10006.getCode());
            order.setRefundDesc(RefundCodeEnum.ERROR_CODE_10006.getDesc());
            payMapper.updateByPrimaryKey(order);
            return ResultRes.error(RefundCodeEnum.ERROR_CODE_10006.getCode(),RefundCodeEnum.ERROR_CODE_10006.getDesc());
        }
        if (!"SUCCESS".equals(responseMap.get(WechatKeyConstants.RETURN_CODE))) {
            // 通信失败
            order.setRefundCode(RefundCodeEnum.ERROR_CODE_10006.getCode());
            order.setRefundDesc(RefundCodeEnum.ERROR_CODE_10006.getDesc());
            payMapper.updateByPrimaryKey(order);
            return ResultRes.error(RefundCodeEnum.ERROR_CODE_10006.getCode(),RefundCodeEnum.ERROR_CODE_10006.getDesc());
        }
        String resSign = null;
        String responseSign = responseMap.get(WechatKeyConstants.SIGN);
        try {
            resSign = WXPayUtil.getSign(responseMap, WechatKeyConstants.SIGN, AESUtils.aesDecrypt(configConsts.getWeixin_signKey(),configConsts.getWeixin_aeskey()));
        } catch (Exception e) {
            log.error("------------->  sign failure", e.getMessage(), e);
            order.setRefundCode(RefundCodeEnum.ERROR_CODE_10007.getCode());
            order.setRefundDesc(RefundCodeEnum.ERROR_CODE_10007.getDesc());
            payMapper.updateByPrimaryKey(order);
            return ResultRes.error(RefundCodeEnum.ERROR_CODE_10007.getCode(),RefundCodeEnum.ERROR_CODE_10007.getDesc());
        }
        if (!responseSign.equals(resSign)) {
            // 验签失败
            log.error("orderRefund sign not match,parameter sign : {} ,local sign : {}",responseMap.get(WechatKeyConstants.SIGN), resSign);
            order.setRefundCode(RefundCodeEnum.ERROR_CODE_10008.getCode());
            order.setRefundDesc(RefundCodeEnum.ERROR_CODE_10008.getDesc());
            payMapper.updateByPrimaryKey(order);
            return ResultRes.error(RefundCodeEnum.ERROR_CODE_10008.getCode(),RefundCodeEnum.ERROR_CODE_10008.getDesc());
        }
        order.setRefundCode(RefundCodeEnum.ERROR_CODE_10008.getCode());
        order.setRefundDesc(RefundCodeEnum.ERROR_CODE_10008.getDesc());
        order.setOrderStatus(OrderStatusEnum.WAIT_REFUND.getValue());
        payMapper.updateByPrimaryKey(order);
        return ResultRes.success();
    }

    /**
     * 执行微信退款
     * @param refundRequest
     * @return
     */
    public Object orderRefundRequest(Object refundRequest) {
        WeChatPayRequestUtil weChatPayRequestUtil = new WeChatPayRequestUtil();
        if (refundRequest == null) {
            return null;
        }
        String response = null;
        String xmlInfo = (String) refundRequest;
        try {
            // 需要微信双向证书
            String cert = AESUtils.aesDecrypt(configConsts.getWeixin_weixinCert(),configConsts.getWeixin_aeskey());
            response = weChatPayRequestUtil.requestOnce(configConsts.getWeixin_orderRefundUrl(), xmlInfo, 10000, 10000, true, null);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
        return response;
    }

    /**
     * 创建退款请求参数
     * @param order
     * @return
     */
    private String makeRefundRequest(PayOrder order) {
        order.setRefundSerial(UuidUtils.getUuid());
        // 读取配置文件微信证书
        Map<String, String> addtion = new HashMap<>();
        addtion.put(WechatKeyConstants.APPID, AESUtils.aesDecrypt(configConsts.getWeixin_appid(),configConsts.getWeixin_aeskey()));
        addtion.put(WechatKeyConstants.MCH_ID, AESUtils.aesDecrypt(configConsts.getWeixin_mch_id(),configConsts.getWeixin_aeskey()));
        addtion.put(WechatKeyConstants.NONCE_STR, WXPayUtil.generateNonceStr());
        addtion.put(WechatKeyConstants.OUT_TRADE_NO, order.getUuid());
        addtion.put(WechatKeyConstants.SIGN_TYPE, WechatKeyConstants.MD5);
        addtion.put(WechatKeyConstants.TRANSACTION_ID, order.getChannelOrderNo());
        addtion.put(WechatKeyConstants.OUT_REFUND_NO, order.getRefundSerial());
        addtion.put(WechatKeyConstants.TOTAL_FEE, new BigDecimal(order.getPayAmount().toString()).multiply(new BigDecimal(100)).setScale(0, BigDecimal.ROUND_DOWN).toString());
        addtion.put(WechatKeyConstants.REFUND_FEE,new BigDecimal(order.getPayAmount().toString()).multiply(new BigDecimal(100)).setScale(0, BigDecimal.ROUND_DOWN).toString());
        addtion.put(WechatKeyConstants.NOTIFY_URL, configConsts.getWeixin_payNotifyUrl()+"/refund");
        String xmlInfo = null;
        String sign = WXPayUtil.getSign(addtion, null, AESUtils.aesDecrypt(configConsts.getWeixin_signKey(),configConsts.getWeixin_aeskey()));
        addtion.put(WechatKeyConstants.SIGN, sign);
        try {
            xmlInfo = WXPayUtil.mapToXml(addtion);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return xmlInfo;
    }

    /**
     *
     * @param payOrder
     * @param outTradeNo
     * @param paraMap
     * @return
     */
    private String checkPayOrderData(PayOrder payOrder, String outTradeNo, Map<String, String> paraMap) {

        // 检查订单状态
        if (OrderStatusEnum.SUCCESS_PAYMENT.getValue().equals(payOrder.getOrderStatus())
                || OrderStatusEnum.FAILURE_PAYMENT.getValue().equals(payOrder.getOrderStatus())) {
            log.info("{},微信异步通知, paymentOrder is over,OrderStatus={}", outTradeNo, payOrder.getOrderStatus());
            return WechatPayConstants.FAIL_BACK_STRING;
        }
        BigDecimal totalFee = new BigDecimal(paraMap.get(WechatKeyConstants.TOTAL_FEE)).divide(new BigDecimal(100));
        BigDecimal payAmount = payOrder.getPayAmount() == null ? new BigDecimal(0) : payOrder.getPayAmount();
        if (totalFee.subtract(payAmount).doubleValue() != 0) {
            log.error("getPaymentResultFromNotification totalFee not match, totalFee : {}, payAmount : {}",
                    totalFee.doubleValue(), payAmount.doubleValue());
            return WechatPayConstants.FAIL_BACK_STRING;
        }
        // 获取payOrder中tenantCode,channelType,paymentType
        String weixinSignKey = AESUtils.aesDecrypt(configConsts.getWeixin_signKey(),configConsts.getWeixin_aeskey());
        String mchId = AESUtils.aesDecrypt(configConsts.getWeixin_mch_id(),configConsts.getWeixin_aeskey());
        // 获取微信异步sign
        String sign = paraMap.get(WechatKeyConstants.SIGN);
        String resSign = WXPayUtil.getSign(paraMap, WechatKeyConstants.SIGN, weixinSignKey);
        // 验证签名
        if (!sign.equals(resSign)) {
            log.error("getPaymentResultFromNotification sign not match, parameter sign : {}, local sign : {}", sign,
                    resSign);
            // 返回结果通知
            return WechatPayConstants.SIGN_NOT_MATCH_BACK_STRING;
        }
        String partner = paraMap.get(WechatKeyConstants.MCH_ID);// 商户号
        // 验证商户号是否一致
        if (!mchId.equals(partner)) {
            log.error(
                    "getPaymentResultFromNotification partner not match, parameter partner : {},local partner : {}",
                    partner, mchId);
            // 返回结果通知
            return WechatPayConstants.PARTNER_NOT_MATCH_BACK_STRING;
        }
        return null;
    }

    /**
     *
     * @param createOrderResult
     * @return
     */
    private ResultRes<CreateOrderRes> makeCreateOrderResponse(Object createOrderResult,PayOrder order) {
        if (createOrderResult == null) {
            return ResultRes.error(PayResEnum.ERROR_CODE_10009.getValue(),PayResEnum.ERROR_CODE_10009.getDesc());
        }
        try{
            Map<String, String> responseMap = WXPayUtil.xmlToMap((String) createOrderResult);
            if (!"SUCCESS".equals(responseMap.get(WechatKeyConstants.RETURN_CODE))) {
                order.setChannelOrderCode(responseMap.get(WechatKeyConstants.RETURN_CODE));
                order.setChannelOrderDesc(responseMap.get(WechatKeyConstants.RETURN_MSG));
                order.setPayOrderCode(PayResEnum.ERROR_CODE_10009.getValue());
                order.setPayOrderDesc(PayResEnum.ERROR_CODE_10009.getDesc());
                order.setOrderStatus(0);
                payMapper.updateByPrimaryKey(order);
                return ResultRes.error(PayResEnum.ERROR_CODE_10009.getValue(),PayResEnum.ERROR_CODE_10009.getDesc());
            }
            if (!"SUCCESS".equals(responseMap.get(WechatKeyConstants.RESULT_CODE))) {
                log.error("unifiedOrder failure,response:{}", JSON.toJSONString(responseMap));
                order.setChannelOrderCode(responseMap.get(WechatKeyConstants.ERR_CODE));
                order.setChannelOrderDesc(responseMap.get(WechatKeyConstants.ERR_CODE_DES));
                order.setPayOrderCode(PayResEnum.ERROR_CODE_10009.getValue());
                order.setPayOrderDesc(PayResEnum.ERROR_CODE_10009.getDesc());
                order.setOrderStatus(OrderStatusEnum.WAIT_PAYMENT.getValue());
                payMapper.updateByPrimaryKey(order);
                return ResultRes.error(PayResEnum.ERROR_CODE_10009.getValue(),PayResEnum.ERROR_CODE_10009.getDesc());
            }
            String responseSign = responseMap.get(WechatKeyConstants.SIGN);
            String paySignKey =AESUtils.aesDecrypt(configConsts.getWeixin_signKey(),configConsts.getWeixin_aeskey());
            String sign = WXPayUtil.getSign(responseMap, WechatKeyConstants.SIGN, paySignKey);
            if (!responseSign.equals(sign)) {
                log.error("unifiedOrder sign not match, parameter sign :{},local sign :{}",
                        responseMap.get(WechatKeyConstants.SIGN), sign);
                order.setPayOrderCode(PayResEnum.ERROR_CODE_10007.getValue());
                order.setPayOrderDesc(PayResEnum.ERROR_CODE_10007.getDesc());
                order.setOrderStatus(OrderStatusEnum.WAIT_PAYMENT.getValue());
                payMapper.updateByPrimaryKey(order);
                return ResultRes.error(PayResEnum.ERROR_CODE_10007.getValue(),PayResEnum.ERROR_CODE_10007.getDesc());
            }

            // 获取签名，responseMap
            getDiffirentSign(responseMap, paySignKey ,order);

            order.setChannelOrderCode(responseMap.get(WechatKeyConstants.RESULT_CODE));
            order.setPayOrderCode(PayResEnum.ERROR_CODE_10010.getValue());
            order.setPayOrderDesc(PayResEnum.ERROR_CODE_10010.getDesc());
            order.setOrderStatus(Integer.valueOf(OrderStatusEnum.WAIT_PAYMENT.getValue()));
            order.setBody(JSON.toJSONString(responseMap));
            payMapper.updateByPrimaryKey(order);

            CreateOrderRes createOrderResponse = new CreateOrderRes();
            createOrderResponse.setOrderNo(order.getTenantOrderNo());
            createOrderResponse.setBody(JSON.toJSONString(responseMap));
            createOrderResponse.setPayOrderNo(order.getUuid());
            createOrderResponse.setMwebUrl(responseMap.get(WechatKeyConstants.MWEB_URL));
            createOrderResponse.setCodeUrl(responseMap.get(WechatKeyConstants.CODE_URL));
            createOrderResponse.setNonceStr(responseMap.get(WechatKeyConstants.NONCE_STR));
            createOrderResponse.setPartnerId(responseMap.get(WechatKeyConstants.MCH_ID));
            createOrderResponse.setPrepayId(responseMap.get(WechatKeyConstants.PREPAY_ID));
            createOrderResponse.setTimeStamp(responseMap.get(WechatKeyConstants.TIMESTAMP));
            createOrderResponse.setPaySign(responseMap.get(WechatKeyConstants.SIGN));
            createOrderResponse.setAppKey(responseMap.get(WechatKeyConstants.SIGN));
            return ResultRes.success(createOrderResponse);
        }catch (Exception e){
            e.printStackTrace();
            log.error(e.getMessage());
            return ResultRes.error();
        }
    }


    private void getDiffirentSign(Map<String, String> responseMap, String paySignKey,PayOrder order) {
        Map<String, String> dataMap = new HashMap<>();
        String tradeType = responseMap.get(WechatKeyConstants.TRADE_TYPE);
        String mchId = responseMap.get(WechatKeyConstants.MCH_ID);
        String nonceStr = responseMap.get(WechatKeyConstants.NONCE_STR);
        String appId = responseMap.get(WechatKeyConstants.APPID);
        String prepayId = responseMap.get(WechatKeyConstants.PREPAY_ID);
        String mwebUrl = responseMap.get(WechatKeyConstants.MWEB_URL);
        String timestamp = System.currentTimeMillis() / 1000 + "";
        if (WechatPayConstants.TradeType.APP.toString().equalsIgnoreCase(tradeType)) {
            dataMap.put(WechatKeyConstants.PACKAGE, "Sign=WXPay");
            dataMap.put(WechatKeyConstants.AppKeyValue.timestamp.toString(), timestamp);
            dataMap.put(WechatKeyConstants.AppKeyValue.partnerid.toString(), mchId);
            dataMap.put(WechatKeyConstants.AppKeyValue.appid.toString(), appId);
            dataMap.put(WechatKeyConstants.AppKeyValue.prepayid.toString(), prepayId);
            dataMap.put(WechatKeyConstants.AppKeyValue.noncestr.toString(), nonceStr);
            responseMap.put(WechatKeyConstants.WX_PACKAGE, "Sign=WXPay");
            responseMap.put(WechatKeyConstants.TIMESTAMP, timestamp);
        } else if (WechatPayConstants.TradeType.MWEB.toString().equalsIgnoreCase(tradeType)) {
            dataMap.put(WechatKeyConstants.H5KeyValue.mch_id.toString(), mchId);
            dataMap.put(WechatKeyConstants.H5KeyValue.nonce_str.toString(), nonceStr);
            dataMap.put(WechatKeyConstants.H5KeyValue.app_id.toString(), appId);
            dataMap.put(WechatKeyConstants.H5KeyValue.prepay_id.toString(), prepayId);
            dataMap.put(WechatKeyConstants.H5KeyValue.mweb_url.toString(), mwebUrl);
        }else if (WechatPayConstants.TradeType.JSAPI.toString().equalsIgnoreCase(tradeType)) {
            if(PaymentTypeEnum.JSAPI_GZH.getValue().equals(order.getPaymentType())){
                dataMap.put("appId", AESUtils.aesDecrypt(configConsts.getWeixin_gzh_appid(),configConsts.getWeixin_aeskey()));
            }else{
                dataMap.put("appId", AESUtils.aesDecrypt(configConsts.getWeixin_appid(),configConsts.getWeixin_aeskey()));
            }
            dataMap.put("nonceStr", nonceStr);
            dataMap.put(WechatKeyConstants.PACKAGE, "prepay_id="+prepayId);
            dataMap.put("signType","MD5");
            dataMap.put(WechatKeyConstants.TIMESTAMP, timestamp);
        }
        String sign = WXPayUtil.getSign(dataMap, null, paySignKey);
        responseMap.put(WechatKeyConstants.SIGN, sign);
        responseMap.put(WechatKeyConstants.TIMESTAMP,timestamp);
    }

    /**
     * 创建订单
     * @return
     */
    public Object createOrder(Object createOrderRequest){
        WeChatPayRequestUtil weChatPayRequestUtil = new WeChatPayRequestUtil();
        if (createOrderRequest == null) {
            return null;
        }
        String response = null;
        String xmlInfo = (String) createOrderRequest;
        try {
            response = weChatPayRequestUtil.requestOnce(configConsts.getWeixin_unifiedUrl(), xmlInfo, 5000, 5000, false, null);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return response;
    }

    /**
     * 创建微信请求参数
     * @return
     */
    private Object makeCreateOrderRequest(ChannelContext channelContext) {
        PayOrder order = channelContext.getPayOrder();
        CreateOrderReq createOrderParam = channelContext.getCreateOrderParam();
        HashMap<String, String> map = new HashMap<>();
        String tempPaymentType = createOrderParam.getPaymentType();
        if(PaymentTypeEnum.JSAPI_GZH.getValue().equals(tempPaymentType)){
            map.put(WechatKeyConstants.APPID, AESUtils.aesDecrypt(configConsts.getWeixin_gzh_appid(),configConsts.getWeixin_aeskey()));
            createOrderParam.setPaymentType(PaymentTypeEnum.JSAPI.getValue());
        }else{
            map.put(WechatKeyConstants.APPID, AESUtils.aesDecrypt(configConsts.getWeixin_appid(),configConsts.getWeixin_aeskey()));
        }
        map.put(WechatKeyConstants.MCH_ID, AESUtils.aesDecrypt(configConsts.getWeixin_mch_id(),configConsts.getWeixin_aeskey()));
        map.put(WechatKeyConstants.NONCE_STR, WXPayUtil.generateNonceStr());
        map.put(WechatKeyConstants.BODY, createOrderParam.getGoodsDesc());
        map.put(WechatKeyConstants.OUT_TRADE_NO, order.getUuid());
        map.put(WechatKeyConstants.TOTAL_FEE, new BigDecimal(order.getPayAmount().toString()).multiply(new BigDecimal(100)).setScale(0, BigDecimal.ROUND_DOWN).toString());
        map.put(WechatKeyConstants.SPBILL_CREATE_IP, createOrderParam.getClientIp());
        map.put(WechatKeyConstants.NOTIFY_URL, configConsts.getWeixin_payNotifyUrl());
        map.put(WechatKeyConstants.TRADE_TYPE, createOrderParam.getPaymentType());
        map.put(WechatKeyConstants.SIGN_TYPE, WechatPayConstants.SignType.MD5.toString());
        if(PaymentTypeEnum.MWEB.getValue().equals(createOrderParam.getPaymentType())){
            map.put(WechatKeyConstants.SCENE_INFO, createOrderParam.getSceneInfo()==null?null:createOrderParam.getSceneInfo().toJSONString());
        }
        if(PaymentTypeEnum.JSAPI.getValue().equals(createOrderParam.getPaymentType())){
            map.put(WechatKeyConstants.OPENID, createOrderParam.getOpenId());
        }
        String xmlInfo = null;
        String sign = WXPayUtil.getSign(map, null, AESUtils.aesDecrypt(configConsts.getWeixin_signKey(),configConsts.getWeixin_aeskey()));
        map.put(WechatKeyConstants.SIGN, sign);
        try {
            xmlInfo = WXPayUtil.mapToXml(map);
            log.info("微信支付请求参数为：{}",xmlInfo);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return xmlInfo;
    }



    /**
     * 创建订单
     * @param param
     * @return
     */
    private PayOrder insertPayOrder(CreateOrderReq param) {
        PayOrder payOrder = new PayOrder();
        payOrder.setGoodsName(param.getGoodsName());
        payOrder.setTenantOrderNo(param.getOrderNo());
        payOrder.setPayAmount(param.getPayAmount());
        payOrder.setChannelType(param.getChannelType());
        payOrder.setPaymentType(param.getPaymentType());
        payOrder.setPayOrderDate(DateUtil.dateToStr(new Date(),DateUtil.YYYY_MM_DD));
        payOrder.setPayOrderTime(DateUtil.dateToStr(new Date(),DateUtil.HH_MM_SS));
        payOrder.setTenantOrderDate(param.getOrderTime().substring(0, 8));
        payOrder.setTenantOrderTime(param.getOrderTime().substring(8));
        payOrder.setOrderStatus(Integer.valueOf(OrderStatusEnum.WAIT_PAYMENT.getValue()));
        payOrder.setUuid(UuidUtils.getUuid());
        payOrder.setClearState(Integer.valueOf(ClearStatusEnum.UN_CLEAT.getValue()));
        payOrder.setPayIsNotify(NotifyTypeEnum.UN_NOTIFY.getValue());
        payOrder.setPayQueryCount(0);
        payOrder.setPayNotifyCount(0);
        payOrder.setSts(StsEnum.ACTIVE.getValue());
        payOrder.setCreatedTime(new Date());
        payMapper.insert(payOrder);
        return payOrder;
    }

    /**
     * 根据订单号查询订单流水
     * @param orderNo
     * @return
     */
    private PayOrder getOrderByOrderNo(String orderNo){
        PayOrder search = new PayOrder();
        search.setTenantOrderNo(orderNo);
        search.setSts(StsEnum.ACTIVE.getValue());
        PayOrder order = payMapper.selectOne(search);
        return order;
    }

}
