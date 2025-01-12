package com.car.utility.web.service.impl;

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
import com.car.utility.web.service.JiguangSmsService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: car-service
 * @description:
 * @author: niushuaixiang
 * @create: 2021-07-16 17:12
 */

@Service
public class JiguangSmsServiceImpl implements JiguangSmsService {

    public static final String APP_KEY = "b2da0fc3e515ad53ccdb9661";
    public static final String MASTER_SECRET = "d16c1707e46b8b4544a5cf9c";



    @Override
    public  PushResult jpushAndroid(String msg,String title) {
        Map param = new HashMap();
        param.put("msg",msg);
        param.put("title",title);
        //创建JPushClient(极光推送的实例)
        JPushClient jpushClient = new JPushClient(MASTER_SECRET, APP_KEY);
        //推送的关键,构造一个payload
        PushPayload payload = PushPayload.newBuilder()
                .setPlatform(Platform.android())//指定android平台的用户
                .setAudience(Audience.all())//你项目中的所有用户
                //.setAudience(Audience.registrationId(param.get("id")))//registrationId指定用户
                .setNotification(Notification.android(msg, title,param))
                //发送内容
                .setOptions(Options.newBuilder().setApnsProduction(false).build())
                //这里是指定开发环境,不用设置也没关系
                .setMessage(Message.content(msg))//自定义信息
                .build();
        PushResult pu = null;
        try {
            pu = jpushClient.sendPush(payload);
        } catch (APIConnectionException e) {
            e.printStackTrace();
        } catch (APIRequestException e) {
            e.printStackTrace();
        }
        return pu;
    }


    @Override
    public  PushResult jpushIOS(String msg,String title) {
        //创建JPushClient
        Map param = new HashMap();
        param.put("msg",msg);
        param.put("title",title);
        JPushClient jpushClient = new JPushClient(MASTER_SECRET, APP_KEY);
        PushPayload payload = PushPayload.newBuilder()
                .setPlatform(Platform.ios())//ios平台的用户
                .setAudience(Audience.all())//所有用户
                //.setAudience(Audience.registrationId(param.get("id")))//registrationId指定用户
                .setNotification(Notification.newBuilder()
                        .addPlatformNotification(IosNotification.newBuilder()
                                .setAlert(msg)
                                .setBadge(+1)
                                .setSound("happy")//这里是设置提示音(更多可以去官网看看)
                                .addExtras(param)
                                .build())
                        .build())
                .setOptions(Options.newBuilder().setApnsProduction(false).build())
                .setMessage(Message.newBuilder().setMsgContent(msg).addExtras(param).build())//自定义信息
                .build();
        PushResult pu = null;
        try {
            pu = jpushClient.sendPush(payload);
        } catch (APIConnectionException e) {
            e.printStackTrace();
        } catch (APIRequestException e) {
            e.printStackTrace();
        }
        return pu;
    }


    @Override
    public  PushResult jpushAll(String msg,String title,String userId) {
        //创建JPushClient
        Map param = new HashMap();
        param.put("msg",msg);
        param.put("title",title);
        JPushClient jpushClient = new JPushClient(MASTER_SECRET, APP_KEY);
        //创建option
        PushPayload payload = PushPayload.newBuilder()
                .setPlatform(Platform.all())  //所有平台的用户
                .setAudience(Audience.alias(userId))//registrationId指定用户
                .setNotification(Notification.newBuilder()
                        .addPlatformNotification(IosNotification.newBuilder() //发送ios
                                .setAlert(msg) //消息体
                                .setBadge(+1)
                                .setSound("happy") //ios提示音
                                .addExtras(param) //附加参数
                                .build())
                        .addPlatformNotification(AndroidNotification.newBuilder() //发送android
                                .addExtras(param) //附加参数
                                .setAlert(msg) //消息体
                                .build())
                        .build())
                .setOptions(Options.newBuilder().setApnsProduction(true).build())//指定开发环境 true为生产模式 false 为测试模式 (android不区分模式,ios区分模式)
                .setMessage(Message.newBuilder().setMsgContent(msg).addExtras(param).build())//自定义信息
                .build();
        PushResult pu = null;
        try {
            pu = jpushClient.sendPush(payload);
            System.out.println(pu.toString());
        } catch (APIConnectionException e) {
            e.printStackTrace();
        } catch (APIRequestException e) {
            e.printStackTrace();
        }
        return pu;
    }


}

