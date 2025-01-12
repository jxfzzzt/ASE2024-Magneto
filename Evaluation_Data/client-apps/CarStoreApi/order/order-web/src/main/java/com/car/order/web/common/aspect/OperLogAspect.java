package com.car.order.web.common.aspect;


import com.alibaba.fastjson.JSONArray;
import com.car.common.req.SysOperationLogReq;
import com.car.common.utils.ExceptionUtils;
import com.car.common.utils.OperLogUtil;
import com.car.system.client.feign.SystemFeign;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 切面处理类，操作日志异常日志记录处理
 * @author linjiang.xie
 * @date 2019/12/12 15:00
 */
@Aspect
@Component
@Slf4j
public class OperLogAspect {

    @Autowired
    SystemFeign systemFeign;

    /**
     * 设置操作日志切入点 记录操作日志 在注解的位置切入代码
     */
    @Pointcut("@annotation(com.car.common.annotation.SysOperLog)")
    public void operLogPointCut() {
    }

    /**
     * 正常返回通知，拦截用户操作日志，连接点正常执行完成后执行， 如果连接点抛出异常，则不会执行
     * @param joinPoint 切入点
     * @param keys      返回结果
     */
    @AfterReturning(value = "operLogPointCut()", returning = "keys")
    public void saveOperLog(JoinPoint joinPoint, Object keys) {
        // 获取RequestAttributes
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        // 从获取RequestAttributes中获取HttpServletRequest的信息
        HttpServletRequest request = (HttpServletRequest) requestAttributes
                .resolveReference(RequestAttributes.REFERENCE_REQUEST);

        try {
            SysOperationLogReq operationLog = OperLogUtil.getOperationLogModel(joinPoint,keys,request);
            systemFeign.insertSysLog(operationLog);
        } catch (Exception e) {
            log.error("保存操作日志执行异常，原因：{}", ExceptionUtils.stackTraceToString(e));
        }
    }

    /**
     * 获取请求参数
     * @param joinPoint
     * @param request
     * @return
     */
    public String getParams(JoinPoint joinPoint,HttpServletRequest request){
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
    public Map<String, String> convertMap(Map<String, String[]> paramMap) {
        Map<String, String> rtnMap = new HashMap<String, String>();
        for (String key : paramMap.keySet()) {
            rtnMap.put(key, paramMap.get(key)[0]);
        }
        return rtnMap;
    }
}
