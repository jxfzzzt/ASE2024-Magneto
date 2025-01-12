package com.magneto.report;

import cn.hutool.http.HtmlUtil;
import com.magneto.config.ClientProjectProperty;
import com.magneto.config.ProjectContext;
import com.magneto.fuzz.result.FuzzChainResultWrapper;
import com.magneto.fuzz.result.FuzzResult;
import com.magneto.fuzz.result.InvokeMethodResult;
import com.magneto.util.ReflectionUtil;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class ReportGenerator {

    public abstract void generate(ProjectContext context, List<FuzzChainResultWrapper> fuzzChainResultList) throws Exception;

    protected static Map<String, String> getInvokeResultMap(FuzzResult fuzzResult) {
        InvokeMethodResult invokeResult = fuzzResult.getInvokeResult();
        Throwable validateThrow = fuzzResult.getValidateThrow();
        Map<String, String> invokeResultMap = new HashMap<>();
        Object returnValue = invokeResult.getReturnValue();
        Throwable throwValue = invokeResult.getThrowValue();
        if (returnValue != null) {
            invokeResultMap.put("Return Value", HtmlUtil.escape(returnValue.toString()));
        } else {
            invokeResultMap.put("Return Value", "null");
        }

        if (throwValue != null) {
            invokeResultMap.put("Throw Exception", HtmlUtil.escape(throwValue.toString()));
        } else {
            invokeResultMap.put("Throw Exception", "null");
        }

        if (validateThrow != null) {
            invokeResultMap.put("Verified Result", HtmlUtil.escape(validateThrow.toString()));
        } else {
            invokeResultMap.put("Verified Result", null);
        }
        return invokeResultMap;
    }

    protected Map<String, String> getFieldMap(Object object) {
        Map<String, String> fieldMap = new HashMap<>();

        if (object == null) {
            fieldMap.put("value", "null");
            return fieldMap;
        }

        String className = object.getClass().getName();
        Set<String> clientProjectClassSet = ClientProjectProperty.getClientProjectClassSet();

        if (clientProjectClassSet.contains(className)) {
            for (Field field : object.getClass().getDeclaredFields()) {
                if (Modifier.isFinal(field.getModifiers()) && Modifier.isStatic(field.getModifiers())) continue;

                try {
                    Object fieldValue = ReflectionUtil.getFieldValue(object, field.getName());
                    if (fieldValue == null) {
                        fieldMap.put(field.getName(), "null");
                    } else {
                        fieldMap.put(field.getName(), HtmlUtil.escape(fieldValue.toString()));
                    }
                } catch (Throwable ignored) {
                }
            }
        } else {
            if (object instanceof InputStream) {
                fieldMap.put("value", HtmlUtil.escape("[ InputStream ]") + " (Refer to the Exploit for detailed information)");
            } else if (object instanceof File) {
                fieldMap.put("value", HtmlUtil.escape("[ File: " + object.toString() + " ]") + " (Refer to the Exploit for detailed information)");
            } else {
                fieldMap.put("value", HtmlUtil.escape(object.toString()));
            }
        }
        return fieldMap;
    }

}
