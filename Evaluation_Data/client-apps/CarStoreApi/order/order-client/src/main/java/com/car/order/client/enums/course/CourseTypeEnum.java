package com.car.order.client.enums.course;

import com.car.order.client.enums.driving.ServiceTypeEnum;
import lombok.Getter;

/**
 * Created by Intellij IDEA.
 *
 * @author:  cjw
 * Date:  2021/2/23
 */
@Getter
public enum CourseTypeEnum {

    /**
     * 课程类型 0:直播 1:图文
     */
    LICE_BROADCAST(0, "直播"),
    IMAGE_TEXT(1, "图文")
    ;
    private Integer value;
    private String desc;

    CourseTypeEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static String enumOfDesc(Integer value) {
        for (CourseTypeEnum enums :values()) {
            if (enums.value.equals(value)) {
                return enums.desc;
            }
        }
        return null;
    }
}
