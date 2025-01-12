package com.car.common.exception;

import com.car.common.enums.ResEnum;
import lombok.Data;

/**
 * @author xlj
 */
@Data
public class BusinessException extends RuntimeException {
    private String code;
    private String msg;

    public BusinessException(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public BusinessException(String msg) {
        this.msg = msg;
    }

    public BusinessException(ResEnum resEnum) {
        //默认系统异常编码
        this.code = resEnum.getValue();
        this.msg = resEnum.getDesc();
    }

    public BusinessException() {
        //默认系统异常编码
        this.code = ResEnum.SYSTEM_ERROR.getValue();
        //默认系统异常描述
        this.msg = ResEnum.SYSTEM_ERROR.getDesc();
    }
}
