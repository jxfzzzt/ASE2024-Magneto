package com.car.account.web.service.person;

import com.car.account.client.request.login.LoginReq;
import com.car.account.client.response.login.LoginRes;
import com.car.common.res.ResultRes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author zhangyp
 * @date 2021/1/16 21:10
 */
public interface PersonService {

    /**
     * 获取登陆验证码
     * @param accountName
     * @return
     */
    ResultRes getLoginCode(String accountName,String terminal, HttpServletRequest request);

    /**
     * 用户登陆
     * @param param
     * @return
     */
    ResultRes<LoginRes> userLogin(LoginReq param, HttpServletRequest request);

    /**
     * 切换角色
     * @return
     */
    ResultRes<LoginRes> switchRole();
    /**
     * 退出登陆
     * @return
     */
    ResultRes exitLogin();

    /**
     * 根据用户ID集合批量退出登录
     * @param userList
     * @return
     */
    ResultRes exitLoginByUserId(List<String> userList);

    /**
     * 根据用户ID批量退出登录
     * @param userUuid
     * @return
     */
    ResultRes exitLoginByUserUuid(String userUuid);

}
