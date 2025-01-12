package com.car.utility.web.service;

import com.car.common.res.ResultRes;

import java.io.File;

/**
 *
 * @author xlj
 */
public interface SendMailService {

    /**
     * 邮件发送
     * @param subject   邮件主题
     * @param sendHtml  发送内容
     * @param receiveUser   收件人
     * @param ccUser    抄送人
     * @param bccUser   密送人
     * @param attachment    附件
     * @return
     */
    public ResultRes sendMail(String subject, String sendHtml, String receiveUser, String ccUser, String bccUser, File attachment);

    /**
     * 登陆确认验证码
     * @param email
     * @param code
     * @return
     */
    ResultRes<String> sendLogin(String email, String code);

}
