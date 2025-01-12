package com.car.system.web.common.constants;


/**
 * @author HeLing
 * @since 2020-10-22
 */
public class AdminConstants {

    /**
     * 图片限制大小2M
     */
    public static final Long PERSON_IMAGE_SIZE_LIMIT = 2 * 1024 * 1024L;

    /**
     * 验证码有效期
     */
    public static final long LOGIN_VERIFICATION_CODE_TIME = 10;
    /**
     * 验证码
     */
    public static final String LOGIN_VERIFICATION_CODE_CACHE_KEY = "car:manage:verificationCode:%s";

    /**
     * 默认的超级管理员账号
     */
    public static final String DEFAULT_SUPPER_ACCESS_UUID = "0";


    /**
     * 默认菜单根节点标识
     */
    public static final String DEFAULT_MENU_ROOT_NODE = "-1";

}
