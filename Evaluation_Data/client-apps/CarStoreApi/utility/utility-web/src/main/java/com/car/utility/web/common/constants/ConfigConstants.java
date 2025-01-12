package com.car.utility.web.common.constants;

import com.car.utility.web.common.constants.property.LocalConfig;
import com.car.utility.web.common.constants.property.ObsConfig;
import com.car.utility.web.common.constants.property.OssConfig;
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
public class ConfigConstants {

    /**
     * 上传图片最大限制
     */
    private Integer uploadFileSizeLimit;
    /**
     * 文件上传渠道oss:阿里云   obs: 华为云  local: 本地
     */
    private String fileChannel;
    /**
     * obs渠道上传参数对象
     */
    private ObsConfig obs;
    /**
     * oss渠道上传参数对象
     */
    private OssConfig oss;

    private LocalConfig local;
    /**
     * 文件资源访问前缀
     */
    private String fileUrlPrefix;

    //微信支付
    private String weixin_gzh_appid;
    private String weixin_appid;
    private String weixin_mch_id;
    private String weixin_weixinCert;
    private String weixin_signKey;
    private String weixin_aeskey;
    //微信支付回调地址
    private String weixin_payNotifyUrl;
    //微信支付统一下单接口
    private String weixin_unifiedUrl;
    //微信支付统一退款接口
    private String weixin_orderRefundUrl;
    //支付宝支付回调地址
    private String alipay_payNotifyUrl;

}
