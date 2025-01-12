package com.car.system.web.common.constants;

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
@ConfigurationProperties(prefix="config")
public class ConfigConsts {

    /**
     * 默认用户密码
     */
    public String defaultUserPwd;

    /**
     * 默认超级用户UUID
     */
    public String defaultSupperUserUuid;

    /**
     * 接口请求日志开关(open/close)
     */
    public String requestLogSwitch;

}
