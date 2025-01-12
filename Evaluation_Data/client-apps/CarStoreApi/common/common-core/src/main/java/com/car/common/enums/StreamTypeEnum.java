package com.car.common.enums;

/**
 * 收益类型
 * @author zhangyp
 * @date 2021/1/27 21:33
 */
public enum StreamTypeEnum {

    /**
     * 收入
     */
    IN(0),

    /**
     * 支出
     */
    OUT(1)
    ;

    private Integer type;

    StreamTypeEnum(Integer type) {
        this.type = type;
    }

    public Integer getType() {
        return type;
    }
}
