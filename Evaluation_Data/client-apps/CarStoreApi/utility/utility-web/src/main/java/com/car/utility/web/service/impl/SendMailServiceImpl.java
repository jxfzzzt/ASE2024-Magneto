package com.car.utility.web.service.impl;

import com.car.common.res.ResultRes;
import com.car.common.utils.ExceptionUtils;
import com.car.utility.web.common.constants.ConfigConstants;
import com.car.utility.web.common.constants.EmailConstants;
import com.car.utility.web.service.SendMailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.Message.RecipientType;
import javax.mail.internet.*;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author xlj
 */
@Service
@Slf4j
public class SendMailServiceImpl implements SendMailService {

    private final static Pattern pattern = Pattern.compile("(\\$\\{)([\\w]+)(\\})");

    @Autowired
    private ConfigConstants configConstants;

    @Autowired
    private EmailConstants emailConstants;

    /**
     * 邮件发送
     * @param subject   邮件主题
     * @param sendHtml  发送内容
     * @param receiveUser   收件人,多人用逗号隔开
     * @param ccUser    抄送人,多人用逗号隔开
     * @param bccUser   密送人,多人用逗号隔开
     * @param attachment    附件
     * @return
     */
    @Override
    public ResultRes sendMail(String subject, String sendHtml, String receiveUser, String ccUser, String bccUser, File attachment) {
        try {
            // 创建邮件配置
            Properties props = new Properties();
            // 使用的协议（JavaMail规范要求）
            props.setProperty("mail.transport.protocol", "smtp");
            // 发件人的邮箱的 SMTP 服务器地址
            //服务器地址
            props.setProperty("mail.smtp.host", emailConstants.getHost());
            // 发件人的邮箱的 SMTP 服务器地址端口
            //服务器端口
            props.setProperty("mail.smtp.port", emailConstants.getPort());
            //邮件导入包名称
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            // 需要请求认证
            props.setProperty("mail.smtp.auth", "true");
            // 开启ssl
            props.setProperty("mail.smtp.ssl.enable", "true");
            // 根据邮件配置创建会话，注意session别导错包
            Session session = Session.getDefaultInstance(props);
            // 开启debug模式，可以看到更多详细的输入日志
            session.setDebug(false);
            // 根据会话创建邮件
            MimeMessage msg = new MimeMessage(session);
            // address邮件地址, personal邮件昵称, charset编码方式
            InternetAddress fromAddress = new InternetAddress(emailConstants.getUsername(), emailConstants.getPassword(), "utf-8");
            // 设置发送邮件方
            msg.setFrom(fromAddress);
            // 多个收件人写法
            InternetAddress[] receiveAddress = InternetAddress.parse(receiveUser);
            // 设置邮件的抄送人员
            msg.setRecipients(RecipientType.TO, receiveAddress);
            //增加CC功能(抄送)
            if (ccUser != null) {
                // 设置邮件的密送人员
                InternetAddress[] ccAddress = InternetAddress.parse(ccUser);
                msg.addRecipients(RecipientType.CC, ccAddress);
            }
            //增加BCC功能(密送)
            if (bccUser != null) {
                InternetAddress[] bccAddress = InternetAddress.parse(bccUser);
                msg.addRecipients(RecipientType.BCC, bccAddress);
            }
            // 设置邮件标题
            msg.setSubject(subject, "utf-8");
            // 向multipart对象中添加邮件的各个部分内容，包括文本内容和附件
            Multipart multipart = new MimeMultipart();
            // 添加邮件正文
            BodyPart contentPart = new MimeBodyPart();
            contentPart.setContent(sendHtml, "text/html;charset=UTF-8");
            multipart.addBodyPart(contentPart);
            // 添加附件的内容
            if (attachment != null) {
                BodyPart attachmentBodyPart = new MimeBodyPart();
                DataSource source = new FileDataSource(attachment);
                attachmentBodyPart.setDataHandler(new DataHandler(source));
                // MimeUtility.encodeWord可以避免文件名乱码
                attachmentBodyPart.setFileName(MimeUtility.encodeWord(attachment.getName()));
                multipart.addBodyPart(attachmentBodyPart);
            }
            // 将multipart对象放到message中
            msg.setContent(multipart);
            msg.saveChanges();

            // 获取传输通道
            Transport transport = session.getTransport();
            // 使用邮箱地址、邮箱账号、邮箱密码连接邮箱服务器
            transport.connect(emailConstants.getHost(), emailConstants.getUsername(), emailConstants.getPassword());
            // 连接，并发送邮件
            transport.sendMessage(msg, msg.getAllRecipients());
            transport.close();
            return ResultRes.success();
        } catch (Exception e) {
            log.error("邮件发送失败，异常信息：{}",ExceptionUtils.stackTraceToString(e));
            return ResultRes.error(ExceptionUtils.stackTraceToString(e));
        }
    }

    /**
     * 登陆确认验证码
     * @param email
     * @param code
     * @return
     */
    @Override
    @Async
    public ResultRes<String> sendLogin(String email, String code) {
        String sendHtml = emailConstants.getSendLoginSendHtml();
        Map<String,String> kvs = new HashMap<>();
        kvs.put("${code}",code);
        return sendMail(emailConstants.getSendLoginSubject(),parse(sendHtml,kvs),email,null,null,null);
    }



    /**
     * 替换字符串
     * @param content
     * @param kvs
     * @return
     */
    private String parse(String content, Map<String,String> kvs){
        Matcher m = pattern.matcher(content);
        StringBuffer sr = new StringBuffer();
        while(m.find()){
            String group = m.group();
            m.appendReplacement(sr, kvs.get(group));
        }
        m.appendTail(sr);
        return sr.toString();
    }
}
