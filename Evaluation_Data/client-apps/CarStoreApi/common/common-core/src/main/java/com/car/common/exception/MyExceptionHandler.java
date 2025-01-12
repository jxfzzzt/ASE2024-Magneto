package com.car.common.exception;



import com.car.common.enums.ResEnum;
import com.car.common.res.ResultRes;
import com.car.common.utils.ExceptionUtils;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author HeLing
 * 全局异常处理器
 *
 */
@ControllerAdvice
@Slf4j
public class MyExceptionHandler {

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public ResultRes<String> errorHandler(Exception ex) {
        log.error("系统执行异常,异常消息为："+ExceptionUtils.stackTraceToString(ex));
        return ResultRes.error(ResEnum.SYSTEM_ERROR.getValue(),ResEnum.SYSTEM_ERROR.getDesc());
    }

    @ExceptionHandler(value = BusinessException.class)
    @ResponseBody
    public ResultRes<String> handleBusinessException(BusinessException e) {
        val res = new ResultRes<String>();
        res.setCode(e.getCode());
        res.setMsg(e.getMsg());
        return res;
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseBody
    public ResultRes<String> handleBusinessException(MethodArgumentNotValidException e) {
        val res = new ResultRes<String>();
        res.setCode(ResEnum.SYSTEM_ERROR.getValue());
        res.setMsg(e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        return res;
    }

}
