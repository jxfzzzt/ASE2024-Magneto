package com.car.utility.web.service;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;

import java.util.Map;

/**
 * @program: car-service
 * @description:
 * @author: niushuaixiang
 * @create: 2021-07-16 17:12
 */
public interface JiguangSmsService {



    public PushResult jpushAndroid(String msg,String title);


    public PushResult jpushIOS(String msg,String title) ;


    public PushResult jpushAll(String msg,String title,String userId) ;



}