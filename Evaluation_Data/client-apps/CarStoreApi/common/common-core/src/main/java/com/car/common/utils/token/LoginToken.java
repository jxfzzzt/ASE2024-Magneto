package com.car.common.utils.token;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author linjiang.xie
 * @date 2020/3/27 14:57
 */
public class LoginToken {
    public static final String LOGIN_REDIS_PREFIX = "login.phone";
    public static final String LOGOUT_REDIS_PREFIX = "logout.phone";
    /**
     * tokenID
     */
    private String uuid;
    /**
     * 用户ID
     */
    private String userUuid;
    /**
     * 用户名称
     */
    private String userName;

    /**
     * 用户手机
     */
    private String userMobile;

    /**
     * 用户类型
     */
    private Integer userType;
    /**
     * 登陆终端  manage    /
     */
    private String loginTerminal = "no-terminal";

    /**
     * 登陆ip地址  manage    /
     */
    private String loginIpAddr;

    /**
     * 角色id
     */
    private List<String> roleIds;
    /**
     * 有效期/秒
     */
    private Integer expireTime = 21600;
    /**
     * 登陆时间
     */
    private Date loginTime;
    /**
     * 扩张属性
     */
    private Map<String, String> extraInfo = new HashMap();

    public String cacheKey() {
        return LOGIN_REDIS_PREFIX + ":" + this.loginTerminal + ":" + this.uuid;
    }

    public String logoutCacheKey() {
        return LOGOUT_REDIS_PREFIX + ":" + this.loginTerminal + ":" + this.uuid;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUserUuid() {
        return userUuid;
    }

    public void setUserUuid(String userUuid) {
        this.userUuid = userUuid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLoginTerminal() {
        return loginTerminal;
    }

    public void setLoginTerminal(String loginTerminal) {
        this.loginTerminal = loginTerminal;
    }

    public Integer getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Integer expireTime) {
        this.expireTime = expireTime;
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    public Map<String, String> getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(Map<String, String> extraInfo) {
        this.extraInfo = extraInfo;
    }

    public String getLoginIpAddr() {
        return loginIpAddr;
    }

    public void setLoginIpAddr(String loginIpAddr) {
        this.loginIpAddr = loginIpAddr;
    }

    public List<String> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(List<String> roleIds) {
        this.roleIds = roleIds;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }


}
