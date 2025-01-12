package com.car.gateway.filter;

import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.car.common.enums.ResEnum;
import com.car.common.res.ResultRes;
import com.car.common.utils.DateUtil;
import com.car.common.utils.RedisUtils;
import com.car.common.utils.token.Baggages;
import com.car.common.utils.token.EncryptUtil;
import com.car.common.utils.token.LoginToken;
import com.car.common.utils.token.TokenUtil;
import com.car.gateway.common.config.properties.PlantFormProperties;
import com.car.gateway.common.constant.Constants;
import com.car.gateway.service.RoleMenuUrlService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.List;


/**
 * @author HeLing
 * @date 2020-10-21
 */
@Slf4j
@Component
public class GateWayPreFilter extends ZuulFilter {

    private static SimpleDateFormat dtf = DateUtil.YYYY_MM_DD_HH_MM_SS;


    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private RoleMenuUrlService roleMenuUrlService;

    @Autowired
    private PlantFormProperties plantFormProperties;


    /**
     * @return pre：路由之前   routing：路由之时   post： 路由之后   error：发送错误调用
     */
    @Override
    public String filterType() {
        return "pre";
    }

    /**
     * @return 过滤的顺序
     */
    @Override
    public int filterOrder() {
        return 0;
    }

    /**
     * @return shouldFilter：这里可以写逻辑判断，是否要过滤，true,永远过滤。
     */
    @Override
    public boolean shouldFilter() {
        RequestContext ctx = RequestContext.getCurrentContext();
        String url = ctx.getRequest().getRequestURL().toString();
        //如果是已配置不需过滤的url，允许通过
        if (plantFormProperties.getRouterConfigProperties().getNoFilterUrl().stream()
                .anyMatch(item -> url.contains(item.replaceAll(Constants.URL_COMMON_CHAR, "")))) {
            log.info("no need to intercept");
            return false;
        }
        return true;
    }

    /**
     * @return 过滤器的具体逻辑，包括查sql，nosql去判断该请求到底有没有权限访问
     */
    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        log.info("method: {}, URL: {}", request.getMethod(), request.getRequestURL().toString());
        try {
            // 利用网关的私钥进行加密，在请求头中添加字段
            String mes = EncryptUtil.encrypt(plantFormProperties.getRouterConfigProperties().getPrivateKey(), request.getRequestURI());
            ctx.addZuulRequestHeader("gatewayHeader", mes);
        } catch (Exception e) {
            log.error("网关私钥加密异常{}", e.getMessage());
        }
        setResponseAttribute(ctx.getResponse());
        //需要权限校验的请求   校验token
        String token = request.getHeader(Constants.TOKEN);
        Object o = parseToken(ctx, token, request);
        if (StringUtils.isEmpty(o)) {
            return null;
        }
        //将token放置到链路中，方便其他项目能从上下文中进行引用
        Baggages.setToken(token);
        log.info("网关通过，当前链路中的Token为：{}",Baggages.getToken());
        return null;
    }

    private Object parseToken(RequestContext ctx, String token, HttpServletRequest request) {
        //验证token，如果不存在，禁止访问
        if (StringUtils.isEmpty(token)) {
            log.error("请求{}token为空", request.getRequestURL());
            ResultRes error = ResultRes.error(ResEnum.EMPTY_TOKEN.getValue(), ResEnum.EMPTY_TOKEN.getDesc());
            buildResponse(ctx, error, HttpStatus.UNAUTHORIZED.value());
            return null;
        }
        //解析token
        LoginToken loginToken;
        try {
            loginToken = TokenUtil.verifyToken(token);
        } catch (JWTVerificationException e) {
            ResultRes error = ResultRes.error(ResEnum.TOKEN_ERROR.getValue(), ResEnum.TOKEN_ERROR.getDesc());
            buildResponse(ctx, error, HttpStatus.UNAUTHORIZED.value());
            log.error("解析异常的token:{}", token);
            return null;
        }
        // 判断该token是否已登出
        String logoutValue = redisUtils.getString(loginToken.logoutCacheKey());
        if (!StringUtils.isEmpty(logoutValue)) {
            log.info("该token已被登出，请重新登录");
            ResultRes error = ResultRes.error(ResEnum.EXPIRE_TOKEN.getValue(), ResEnum.EXPIRE_TOKEN.getDesc());
            buildResponse(ctx, error, HttpStatus.UNAUTHORIZED.value());
            return null;
        }
        // 判断是否该用户是否已登录
        String redisSavedToken = redisUtils.getString(loginToken.cacheKey());
        if (StringUtils.isEmpty(redisSavedToken)) {
            log.warn("token：用户{}-登录{}，redis中没有token缓存，请注意检查", loginToken.getUserName(), loginToken.getLoginTerminal());
            ResultRes error = ResultRes.error(ResEnum.TOKEN_ERROR.getValue(), ResEnum.TOKEN_ERROR.getDesc());
            buildResponse(ctx, error, HttpStatus.UNAUTHORIZED.value());
            return null;
        }
        // 如果redis中保存的token和请求中的token不一致，则表示账号已经重新登录，该token已经被顶掉了
        if (!token.equals(redisSavedToken)) {
            LoginToken newLogin;
            try {
                newLogin = TokenUtil.verifyToken(redisSavedToken);
                ResultRes resultData = new ResultRes(ResEnum.TOKEN_REMOTE_LOGIN.getValue(), "您的账号于" + dtf.format(newLogin.getLoginTime()) + "在另一台设备(ip:" + newLogin.getLoginIpAddr() + ")上登录，如果这不是你的操作，请尽快重新登录并修改密码!");
                buildResponse(ctx, resultData, HttpStatus.UNAUTHORIZED.value());
                return null;
            } catch (JWTVerificationException e) {
                log.error("解析验证redis中的token异常", e);
            }
        }
        if (plantFormProperties.isEnablePermissionVerify()) {
            //校验用户是否有访问该url的权限
            ResultRes<List<String>> roleMenuUrls = roleMenuUrlService.getRoleMenuUrls(loginToken.getRoleIds());
            if (roleMenuUrls.getData().stream().noneMatch(item -> request.getRequestURI().contains(item.replaceAll(Constants.URL_COMMON_CHAR, "")))) {
                ResultRes resultData = new ResultRes(ResEnum.PERMISSION_DENIED.getValue(), ResEnum.PERMISSION_DENIED.getDesc());
                buildResponse(ctx, resultData, HttpStatus.OK.value());
                return null;
            }
        }
        return "success";
    }

    private void setResponseAttribute(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "GET,POST,DELETE,PUT,OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "token,terminalType,stationUuid,Content-Type");
        response.setHeader("Access-Control-Expose-Headers", "*");
        response.setHeader("Access-Control-Max-Age", "86400");
        response.setContentType("application/json; charset=utf-8");
        response.setCharacterEncoding("UTF-8");
    }

    /**
     * 构建返回信息
     *
     * @param ctx        请求上下文
     * @param resultData 返回结果
     * @param httpStatue http请求状态
     */
    private void buildResponse(RequestContext ctx, ResultRes resultData, int httpStatue) {
        String body;
        if (resultData == null) {
            body = "OK";
        } else {
            body = JSONObject.toJSONString(resultData);
        }
        ctx.setResponseBody(body);
        ctx.setResponseStatusCode(httpStatue);
        ctx.setSendZuulResponse(false);
    }
}
