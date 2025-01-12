package com.car.system.web.service;

import com.car.common.res.ResultRes;
import com.car.system.client.request.oauth.QueryTokenReq;
import com.car.system.client.response.oauth.QueryTokenRes;

import javax.servlet.http.HttpServletRequest;


/**
 * @author XIELINJIANG
 */
public interface OauthService {

    /**
     * 获取登陆Token
     * @param queryTokenReq
     * @param request
     * @return
     */
    ResultRes<QueryTokenRes> queryToken(QueryTokenReq queryTokenReq, HttpServletRequest request);

    /**
     * 退出登陆
     * @return
     */
    ResultRes exitLogin();
}
