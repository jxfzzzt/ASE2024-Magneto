package com.car.utility.web.service;

import com.car.common.res.ResultRes;

/**
 *
 */
public interface SendSmsService {

    /**
     * 发送短信
     * @param phone 手机号码
     * @param smsCode   短信模板编码
     * @param content   填充内容
     * @return
     */
    ResultRes<String> sendSms(String phone, String smsCode, String content);
}
