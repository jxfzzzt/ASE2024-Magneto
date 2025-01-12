package com.car.account.web.common.utils;


import com.car.common.enums.LoginChannelEnum;
import com.car.common.utils.TokenHelper;

/**
 * @author zhangyp
 * @date 2020/5/13 23:43
 */
public class Utils {

    public static String getLoginTerminal(){
        String loginTerminal = (null != TokenHelper.getLoginToken()) ? TokenHelper.getLoginToken().getLoginTerminal() : LoginChannelEnum.WEB_PC.getValue();
        return loginTerminal;
    }
}
