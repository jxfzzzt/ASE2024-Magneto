package com.car.utility.web.common.constants;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 常量对象
 * @author 谢林江
 *
 */
@Component
@Data
@ConfigurationProperties(prefix="email")
public class EmailConstants {

    /**
     * 主机
     */
    private String host;
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;

    /**
     * 邮箱端口
     */
    private String port;

    /**
     * 登陆邮件标题
     */
    private String sendLoginSubject;
    /**
     * 登陆邮件消息体
     */
    private String sendLoginSendHtml;
}
