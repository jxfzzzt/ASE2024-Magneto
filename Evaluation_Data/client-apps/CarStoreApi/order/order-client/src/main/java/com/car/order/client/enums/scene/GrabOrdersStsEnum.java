package com.car.order.client.enums.scene;

import lombok.Getter;

/**
 * Created by Intellij IDEA.
 *
 * @author:  cjw
 * Date:  2021/3/6
 */
@Getter
public enum GrabOrdersStsEnum {
    /**
     * 抢单状态 0未抢，1已抢
     */
    not_grab(0,"未抢"),
    grab(1,"已抢"),
    RELEASE(2, "本人发布"),
    ;
    private Integer value;
    private String desc;

    GrabOrdersStsEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static String enumOfDesc(Integer value) {
        for (GrabOrdersStsEnum enums :values()) {
            if (enums.value.equals(value)) {
                return enums.desc;
            }
        }
        return null;
    }
}
