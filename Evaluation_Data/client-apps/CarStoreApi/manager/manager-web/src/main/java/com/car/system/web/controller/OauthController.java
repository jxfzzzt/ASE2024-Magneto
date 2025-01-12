package com.car.system.web.controller;

import com.car.common.res.ResultRes;
import com.car.common.utils.RedisUtils;
import com.car.common.utils.TokenHelper;
import com.car.common.utils.token.LoginToken;
import com.car.system.web.service.OauthService;
import com.car.system.web.common.constants.AdminConstants;
import com.car.system.client.request.oauth.QueryTokenReq;
import com.car.system.client.response.oauth.QueryTokenRes;
import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * 系统用户
 */
@Slf4j
@Api(value = "OauthController", tags = "系统登录授权")
@RequestMapping(value = "oauth", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RestController
public class OauthController{

    @Autowired
    private OauthService oauthService;

    @Autowired
    RedisUtils redisUtils;
    /**
     * 用户登录
     * @param queryTokenReq
     * @return
     */
    @RequestMapping(value = "/userLogin",method = RequestMethod.POST)
    @ApiOperation(value = "用户登录", nickname = "userLogin")
    public ResultRes<QueryTokenRes> userLogin(@RequestBody @Valid QueryTokenReq queryTokenReq,HttpServletRequest request){
        return oauthService.queryToken(queryTokenReq,request);
    }

    /**
     * 获取token对象
     * @return
     */
    @RequestMapping(value = "/getTokenUser",method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String" ,paramType="header")
    })
    @ApiOperation(value = "获取token对象", nickname = "getToken")
    public ResultRes<LoginToken> getTokenUser(){
        return ResultRes.success(TokenHelper.getLoginToken());
    }


    /**
     * 退出登录
     * @return
     */
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String" ,paramType="header")
    })
    @RequestMapping(value = "/exitLogin",method = RequestMethod.GET)
    @ApiOperation(value = "退出登录", nickname = "exitLogin")
    public ResultRes exitLogin(){
        return oauthService.exitLogin();
    }

    /**
     * 获取验证码
     * @return
     */
    @RequestMapping(value = "/queryVerificationCode",method = RequestMethod.GET)
    @ApiOperation(value = "获取验证码", nickname = "queryVerificationCode")
    public void queryVerificationCode(HttpServletRequest request, HttpServletResponse response){
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        // 三个参数分别为宽、高、位数
        SpecCaptcha specCaptcha = new SpecCaptcha(130, 48, 5);

        // 设置类型，纯数字、纯字母、字母数字混合
        specCaptcha.setCharType(Captcha.TYPE_DEFAULT);

        // 生成的验证码
        String code = specCaptcha.text();

        //输出前端页面
        specCaptcha.out(output);

        //将code设置到Redis中
        String cacheKey = String.format(AdminConstants.LOGIN_VERIFICATION_CODE_CACHE_KEY, new Object[] { code.toUpperCase() });
        redisUtils.set(cacheKey,code, AdminConstants.LOGIN_VERIFICATION_CODE_TIME, TimeUnit.MINUTES);
        //将图片放置到内存中
        log.info("获取验证码Value is {}",code);

        //输出图片对象
        response.setContentType("image/png");
        //浏览器不要缓存
        response.setDateHeader("expries", -1);
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "no-cache");
        try {
            ServletOutputStream out = response.getOutputStream();
            output.writeTo(out);
        } catch (IOException e) {
            log.error("content",e);
        }
    }

}
