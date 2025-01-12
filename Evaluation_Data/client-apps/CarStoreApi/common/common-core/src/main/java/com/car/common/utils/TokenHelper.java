package com.car.common.utils;

import com.alibaba.fastjson.JSONArray;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.car.common.utils.token.Baggages;
import com.car.common.utils.token.LoginToken;
import com.car.common.utils.token.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * @author linjiang.xie
 * @date 2020/3/27 17:36
 */
@Slf4j
public class TokenHelper {
    public static final String TOKEN = "token";

    /**
     * 从header 中 token 中获取用户uuid
     *
     * @return
     */
    public static String getUserUuid() {
        LoginToken loginToken = getLoginToken();
        if (loginToken != null) {
            return loginToken.getUserUuid();
        }
        return "";
    }

    /**
     * 从header 中 token 中获取用户手机号
     *
     * @return
     */
    public static String getUserName() {
        LoginToken loginTokenInfo = getLoginToken();
        if (loginTokenInfo != null) {
            return loginTokenInfo.getUserName();
        }
        return "";
    }

    /**
     * 获取用户类型
     * @return
     */
    public static Integer getUserType(){
        LoginToken loginTokenInfo = getLoginToken();
        if (loginTokenInfo != null) {
            return loginTokenInfo.getUserType();
        }
        return -1;
    }

    /**
     * 从请求头中获取token，并解析为 LoginTokenInfo
     * @return
     */
    public static LoginToken getLoginToken() {
        String token = RequestUtil.getRequestHeaderParam(TOKEN);
        if (StringUtils.isEmpty(token)) {
            token = Baggages.getToken();
            if(StringUtils.isEmpty(token)){
                log.info("请求头中token为空");
                return null;
            }
        }
        LoginToken loginTokenInfo;
        try {
            loginTokenInfo = TokenUtil.verifyToken(token);
        } catch (JWTVerificationException e) {
            log.error("解析token异常", e);
            return null;
        }
        return loginTokenInfo;
    }

    public static void main(String[] args) {
        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJXQUlDLVVTRVIgZm9yIG5vLXRlcm1pbmFsIiwiYXVkIjoibm8tdGVybWluYWwiLCJpc3MiOiJXQUlDLVVTRVIiLCJsb2dpblRva2VuIjoie1wiZXhwaXJlVGltZVwiOjIxNjAwLFwiZXh0cmFJbmZvXCI6e30sXCJsb2dpblRlcm1pbmFsXCI6XCJuby10ZXJtaW5hbFwiLFwibG9naW5UaW1lXCI6MTYxNDUzMzE4NzEyNSxcInVzZXJNb2JpbGVcIjpcIjEzNTIzMjY4MTUwXCIsXCJ1c2VyTmFtZVwiOlwi56iL6YeR5LyfXCIsXCJ1c2VyVHlwZVwiOjIsXCJ1c2VyVXVpZFwiOlwiMTYyNjkxMGM3MDdjNDcxZThjZTgxYWY1N2U0MDkyYmZcIixcInV1aWRcIjpcIjE2MjY5MTBjNzA3YzQ3MWU4Y2U4MWFmNTdlNDA5MmJmXCJ9IiwiZXhwIjoxNjE1ODI5MTg3LCJpYXQiOjE2MTQ1MzMxODd9.AE7THw-imvGjwIJKcsHBrDUEVRYvwzdMWWYsuXUJ_Y8";
        LoginToken loginToken = TokenUtil.verifyToken(token);
        if (loginToken != null) {
            System.out.println(loginToken.getUserUuid());
            System.out.println(loginToken.getUserMobile());
        }
    }
}
