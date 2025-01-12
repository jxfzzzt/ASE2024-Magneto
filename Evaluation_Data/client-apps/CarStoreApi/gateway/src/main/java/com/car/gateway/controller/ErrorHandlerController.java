package com.car.gateway.controller;

import com.netflix.zuul.context.RequestContext;
import com.car.common.res.ResultRes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * @author xlj
 */
@Slf4j
@RestController
public class ErrorHandlerController implements ErrorController {

    @Override
    public String getErrorPath() {
        return "/error";
    }

    @RequestMapping("error")
    public ResultRes<String> error(HttpServletResponse response) {
        String errorMsg = "500";
        RequestContext ctx = RequestContext.getCurrentContext();
        Throwable e = ctx.getThrowable();
        if (e != null) {
            errorMsg = e.getMessage();
            while (e != null) {
                errorMsg = e.getMessage();
                e = e.getCause();
            }
            log.info("errorMsg: {}", errorMsg);
        }

        return ResultRes.error("系统异常[" + errorMsg + "]");
    }
}
