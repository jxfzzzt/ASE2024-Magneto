package com.car.utility.web.controller;

import com.alibaba.fastjson.JSONArray;
import com.car.common.res.ResultRes;
import com.car.utility.client.enums.SmsEnum;
import com.car.utility.web.service.SendSmsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 邮件发送服务
 * @author xlj
 */
@RestController
@RequestMapping("/sms")
@Slf4j
@Api(value = "SmsController", tags = "短信发送")
public class SmsController {

    @Autowired
    SendSmsService sendSmsService;



    /**
     * 登陆确认验证码
     * @return
     */
    @PostMapping(value = "sendRegister")
    @ApiOperation(value = "用户注册验证码", nickname = "sendRegister")
    public ResultRes<String> sendRegister(@RequestParam("phones") String phones, @RequestParam("code") String code) {
        log.info("SMS----->用户注册验证码--->phones:{}----->code:{}",phones,code);
        Map<String,String> map = new HashMap<String,String>();
        map.put("code",code);
        return sendSmsService.sendSms(phones, SmsEnum.REGISTER.getSmsCode(), JSONArray.toJSONString(map));
    }

    /**
     * 店铺审核成功短信
     * @return
     */
    @PostMapping(value = "sendStoreCheckSuccess")
    @ApiOperation(value = "店铺审核成功短信", nickname = "sendStoreCheckSuccess")
    public ResultRes<String> sendStoreCheckSuccess(@RequestParam("phones") String phones, @RequestParam("storeName") String storeName) {
        log.info("SMS----->店铺审核成功短信--->phone:{}----->storeName:{}",phones,storeName);
        Map<String,String> map = new HashMap<String,String>();
        map.put("storeName",storeName);
        return sendSmsService.sendSms(phones, SmsEnum.StoreCheckSuccess.getSmsCode(), JSONArray.toJSONString(map));
    }

    /**
     * 店铺审核驳回短信
     * @return
     */
    @PostMapping(value = "sendStoreCheckReject")
    @ApiOperation(value = "店铺审核驳回短信", nickname = "sendStoreCheckReject")
    public ResultRes<String> sendStoreCheckReject(@RequestParam("phones") String phones, @RequestParam("storeName") String storeName
            , @RequestParam("content") String content) {
        log.info("SMS----->店铺审核成功短信--->phones:{}----->storeName:{}----->content：",phones,storeName,content);
        Map<String,String> map = new HashMap<String,String>();
        map.put("storeName",storeName);
        map.put("content",content);
        return sendSmsService.sendSms(phones, SmsEnum.StoreCheckReject.getSmsCode(), JSONArray.toJSONString(map));
    }

    /**
     * 技师审核成功短信
     * @return
     */
    @PostMapping(value = "sendTechnicianCheckSuccess")
    @ApiOperation(value = "技师审核成功短信", nickname = "sendTechnicianCheckSuccess")
    public ResultRes<String> sendTechnicianCheckSuccess(@RequestParam("phones") String phones) {
        log.info("SMS----->技师审核成功短信--->phone:{}",phones);
        Map<String,String> map = new HashMap<String,String>();
        return sendSmsService.sendSms(phones, SmsEnum.TechnicianCheckSuccess.getSmsCode(), JSONArray.toJSONString(map));
    }

    /**
     * 技师审核驳回短信
     * @return
     */
    @PostMapping(value = "sendTechnicianCheckReject")
    @ApiOperation(value = "技师审核驳回短信", nickname = "sendTechnicianCheckReject")
    public ResultRes<String> sendTechnicianCheckReject(@RequestParam("phones") String phones, @RequestParam("content") String content) {
        log.info("SMS----->技师审核成功短信--->phones:{}----->content:{}",phones,content);
        Map<String,String> map = new HashMap<String,String>();
        map.put("content",content);
        return sendSmsService.sendSms(phones, SmsEnum.TechnicianCheckReject.getSmsCode(), JSONArray.toJSONString(map));
    }

}

