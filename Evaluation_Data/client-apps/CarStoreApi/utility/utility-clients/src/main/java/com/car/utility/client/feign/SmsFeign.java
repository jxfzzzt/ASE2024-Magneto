package com.car.utility.client.feign;


import com.alibaba.fastjson.JSONArray;
import com.car.common.constant.ServiceNameConstant;
import com.car.common.res.ResultRes;
import com.car.utility.client.enums.SmsEnum;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

/**
 * @author
 * @date 2020/6/15 11:47
 */
@FeignClient(value = ServiceNameConstant.UTILITY)
public interface SmsFeign {

    /**
     * 登陆确认验证码
     * @param phone
     * @param code
     * @return
     */
    @PostMapping(value = "sms/sendRegister")
    public ResultRes<String> sendRegister(@RequestParam("phones") String phone, @RequestParam("code") String code);

    /**
     * 店铺审核成功短信
     * @param phone
     * @param storeName
     * @return
     */
    @PostMapping(value = "sms/sendStoreCheckSuccess")
    public ResultRes<String> sendStoreCheckSuccess(@RequestParam("phones") String phone, @RequestParam("storeName") String storeName);

    /**
     * 店铺审核驳回短信
     * @param phone
     * @param storeName
     * @param content
     * @return
     */
    @PostMapping(value = "sms/sendStoreCheckReject")
    public ResultRes<String> sendStoreCheckReject(@RequestParam("phones") String phone,
                                                  @RequestParam("storeName") String storeName,
                                                  @RequestParam("content") String content);

    /**
     * 技师审核成功短信
     * @param phones
     * @return
     */
    @PostMapping(value = "sms/sendTechnicianCheckSuccess")
    public ResultRes<String> sendTechnicianCheckSuccess(@RequestParam("phones") String phones);

    /**
     * 技师审核驳回短信
     * @param phones
     * @param content
     * @return
     */
    @PostMapping(value = "sms/sendTechnicianCheckReject")
    public ResultRes<String> sendTechnicianCheckReject(@RequestParam("phones") String phones, @RequestParam("content") String content);
}
