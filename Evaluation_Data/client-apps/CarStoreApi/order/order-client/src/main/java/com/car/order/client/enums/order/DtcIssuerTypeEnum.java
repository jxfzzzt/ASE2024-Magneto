package com.car.order.client.enums.order;

import com.car.common.enums.StsEnum;
import com.car.common.enums.UserTypeEnum;
import lombok.Getter;

/**
 * @author zhouz
 * @date 2021/2/19
 */
@Getter
public enum DtcIssuerTypeEnum {

    ADMIN(0,"后台发布"),

    TECHNICIAN(2,"技师发布"),

    STORE(3,"店铺发布")
    ;

    private Integer type;

    private String desc;

    DtcIssuerTypeEnum(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public static String enumOfDesc(Integer value) {
        for (DtcIssuerTypeEnum enums :
                values()) {
            if (enums.type.equals(value) ) {
                return enums.desc;
            }
        }
        return null;
    }
    public Integer getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }
}
