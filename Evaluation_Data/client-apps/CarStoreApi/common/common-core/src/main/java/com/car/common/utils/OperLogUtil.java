package com.car.common.utils;

import com.alibaba.fastjson.JSONArray;
import com.car.common.annotation.SysOperLog;
import com.car.common.req.SysOperationLogReq;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author linjiang.xie
 * @date 2020/12/19 16:58
 */
public class OperLogUtil {
    /**
     * 封装获取请求参数
     * @param joinPoint
     * @param keys
     * @param request
     * @return
     */
    public static SysOperationLogReq getOperationLogModel(JoinPoint joinPoint,Object keys,HttpServletRequest request){
        SysOperationLogReq operationLog = new SysOperationLogReq();
        // 从切面织入点处通过反射机制获取织入点处的方法
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        // 获取切入点所在的方法
        Method method = signature.getMethod();
        // 获取操作
        SysOperLog opLog = method.getAnnotation(SysOperLog.class);
        if (opLog != null) {
            // 操作模块
            operationLog.setOperModul(opLog.operModul());
            // 操作类型
            operationLog.setOperType(opLog.operType().getMsg());
            // 操作描述
            operationLog.setOperDesc(opLog.operDesc());
        }
        // 获取请求的类名
        String className = joinPoint.getTarget().getClass().getName();
        // 获取请求的方法名
        String methodName = method.getName();
        methodName = className + "." + methodName;
        // 请求方法
        operationLog.setOperMethod(methodName);
        // 请求的参数
        operationLog.setOperRequParam(getParams(joinPoint,request));
        if(keys != null){
            operationLog.setOperRespParam(JSONArray.toJSONString(keys));
        }
        //获取当前登陆用户名  保存操作日志
        operationLog.setCreatedBy(TokenHelper.getUserName());
        operationLog.setOperIp(IpUtils.getRequestIp(request));
        operationLog.setOperUri(request.getRequestURI());
        return operationLog;
    }

    /**
     * 获取请求参数
     * @param joinPoint
     * @param request
     * @return
     */
    public static String getParams(JoinPoint joinPoint,HttpServletRequest request){
        String params = null;
        try{
            Object[] args = joinPoint.getArgs();
            if(args != null){
                params = JSONArray.toJSONString(joinPoint.getArgs()[0]);
            }
            if(StringUtils.isEmpty(params)){
                Map<String, String> rtnMap = convertMap(request.getParameterMap());
                params = JSONArray.toJSONString(rtnMap);
            }
        }catch (Exception ex){
            params = "获取参数异常，异常原因为："+ex.getMessage();
        }
        return params;
    }

    /**
     * 转换request 请求参数
     *
     * @param paramMap request获取的参数数组
     */
    public static Map<String, String> convertMap(Map<String, String[]> paramMap) {
        Map<String, String> rtnMap = new HashMap<String, String>();
        for (String key : paramMap.keySet()) {
            rtnMap.put(key, paramMap.get(key)[0]);
        }
        return rtnMap;
    }
}
