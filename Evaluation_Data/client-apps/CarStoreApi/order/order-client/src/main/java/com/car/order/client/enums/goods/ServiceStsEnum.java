package com.car.order.client.enums.goods;

import com.car.order.client.enums.front.QueryStateEnum;
import lombok.Getter;

/**
 * Created by Intellij IDEA.
 *
 * @author:  cjw
 * Date:  2021/1/24
 */
@Getter
public enum ServiceStsEnum {

    /**
     * 服务状态
     */
    NOT_SERVICE(0, "未服务"),
    ALREADY_SERVICE(1, "已服务");

    private Integer value;
    private String desc;

    ServiceStsEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static String enumOfDesc(Integer value) {
        for (ServiceStsEnum enums :values()) {
            if (enums.value.equals(value)) {
                return enums.desc;
            }
        }
        return null;
    }
}
