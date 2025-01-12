package com.car.order.client.enums.course;

/**
 * @program: car-service
 * @description:
 * @author: niushuaixiang
 * @create: 2021-03-17 14:04
 */
public enum LatestCourseEnum {

    /**
     * 最新课程 0不是，1是
     */
    YES(0, "不是"),
    NO(1, "是")
    ;
    private Integer value;
    private String desc;

    LatestCourseEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static String enumOfDesc(Integer value) {
        for (LatestCourseEnum enums :values()) {
            if (enums.value.equals(value)) {
                return enums.desc;
            }
        }
        return null;
    }
}