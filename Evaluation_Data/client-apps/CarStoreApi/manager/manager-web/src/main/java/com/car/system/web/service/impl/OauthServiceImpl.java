package com.car.system.web.service.impl;


import com.car.common.enums.ResEnum;
import com.car.common.enums.StatusEnum;
import com.car.common.exception.BusinessException;
import com.car.common.res.ResultRes;
import com.car.common.utils.BCryptPwdUtils;
import com.car.system.web.service.OauthService;
import com.car.system.web.service.SysUserService;
import com.car.system.web.common.constants.AdminConstants;
import com.car.common.utils.RedisUtils;
import com.car.common.utils.TokenHelper;
import com.car.common.utils.UuidUtils;
import com.car.common.utils.token.LoginToken;
import com.car.common.utils.token.TokenUtil;
import com.car.system.client.enums.LoginChannelEnum;
import com.car.system.client.request.oauth.QueryTokenReq;
import com.car.system.client.response.oauth.QueryTokenRes;
import com.car.system.web.common.PlantFormProperties;
import com.car.system.web.model.SysUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class OauthServiceImpl implements OauthService {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    RedisUtils redisUtils;

    @Autowired
    PlantFormProperties plantFormProperties;


    @Override
    public ResultRes<QueryTokenRes> queryToken(QueryTokenReq queryTokenReq, HttpServletRequest request) {
        if(StringUtils.isEmpty(queryTokenReq.getVerificationCode())){
            //验证码不匹配，返回错误信息
            throw new BusinessException(ResEnum.VERIFICATION_CODE_ERROR.getValue(),ResEnum.VERIFICATION_CODE_ERROR.getDesc());
        }
        String cacheKey = String.format(AdminConstants.LOGIN_VERIFICATION_CODE_CACHE_KEY, new Object[] { queryTokenReq.getVerificationCode().toUpperCase() });
        String redisCode = String.valueOf(redisUtils.get(cacheKey));
        log.info("用户登陆验证码为：{}，redis中获取的验证码为：{}",queryTokenReq.getVerificationCode(),redisCode);
        if(StringUtils.isEmpty(queryTokenReq.getVerificationCode()) || StringUtils.isEmpty(redisCode)){
            //未传入验证码或redis中不存在该验证码
            throw new BusinessException(ResEnum.VERIFICATION_CODE_ERROR.getValue(),ResEnum.VERIFICATION_CODE_ERROR.getDesc());
        }

        if(!queryTokenReq.getVerificationCode().toUpperCase().equals(redisCode.toUpperCase())){
            //验证码不匹配，返回错误信息
            throw new BusinessException(ResEnum.VERIFICATION_CODE_ERROR.getValue(),ResEnum.VERIFICATION_CODE_ERROR.getDesc());
        }
        //删除当前Redis缓存数据
        redisUtils.remove(cacheKey);
        //登录成功，获取token
        SysUser user =  sysUserService.getUserByName(queryTokenReq.getUsername());
        if(StringUtils.isEmpty(user)){
            log.error("通过用户名：{}未匹配对应数据信息");
            throw new BusinessException(ResEnum.LOGIN_USER_EXISTS.getValue(),ResEnum.LOGIN_USER_EXISTS.getDesc());
        }
        //匹配验证原始密码是否正确
        if(!BCryptPwdUtils.matchesBcryptPwd(queryTokenReq.getPassword(),user.getPassword())){
            log.error("用户密码匹配不一致，页面原始密码为：{}",queryTokenReq.getPassword());
            throw new BusinessException(ResEnum.LOGIN_USER_ERROR.getValue(),ResEnum.LOGIN_USER_ERROR.getDesc());
        }
        if(StatusEnum.PROHIBIT.getValue().equals(user.getStatus())){
            log.error("用户密码匹配不一致，页面原始密码为：{}",queryTokenReq.getPassword());
            throw new BusinessException(ResEnum.LOGIN_USER_PROHIBIT.getValue(),ResEnum.LOGIN_USER_PROHIBIT.getDesc());
        }
        // 生成token
        LoginToken loginToken = new LoginToken();
        if (plantFormProperties.isSingleLogin()){
            loginToken.setUuid(user.getUuid());
        }else {
            loginToken.setUuid(UuidUtils.getUuid());
        }
        loginToken.setRoleIds(Arrays.asList(user.getRoleUuid()));
        loginToken.setLoginIpAddr(request.getRemoteAddr());
        loginToken.setUserUuid(user.getRoleUuid());
        loginToken.setUserName(user.getUsername());
        loginToken.setLoginTerminal(LoginChannelEnum.MANAGE.getValue());
        loginToken.setLoginTime(new Date());
        // 生成token
        String token = TokenUtil.createToken(loginToken);
        // 登陆成功token存入redis
        redisUtils.set(loginToken.cacheKey(), token, (long)loginToken.getExpireTime(), TimeUnit.MINUTES);
        //封装返回参数
        QueryTokenRes tokenRes = new QueryTokenRes();
        tokenRes.setToken(token);
        tokenRes.setExpiresIn(loginToken.getExpireTime());
        tokenRes.setUuid(user.getUuid());
        return ResultRes.success(tokenRes);
    }

    /**
     * 退出登陆
     * @return
     */
    @Override
    public ResultRes exitLogin() {
        LoginToken token = TokenHelper.getLoginToken();
        // 该token 在 redis中的剩余有效时间
        long remainingTime = redisUtils.getExpireTime(token.cacheKey(), TimeUnit.SECONDS);
        // redis中保存一个登出信息
        String logoutKey = token.logoutCacheKey();
        //执行退出登陆
        redisUtils.remove(token.cacheKey());
        boolean b = redisUtils.set(logoutKey, "", remainingTime, TimeUnit.SECONDS);
        if (true){
            return ResultRes.success();
        }else {
            return ResultRes.error(ResEnum.EXIT_ERROR.getValue(),ResEnum.EXIT_ERROR.getDesc());
        }
    }
}
