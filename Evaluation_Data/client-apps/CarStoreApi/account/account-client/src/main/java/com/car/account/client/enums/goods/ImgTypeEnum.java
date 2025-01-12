package com.car.account.client.enums.goods;

import lombok.Getter;

/**
 * @author zhouz.
 * @date 2020/12/22
 */
@Getter
public enum ImgTypeEnum {


    /**
     * 商品图片类型
     */
    MAIN_GRAPH(0, "主图"),

    OTHER(1, "其他"),

    DESCRIBE(2,"描述")
    ;
    private Integer value;
    private String desc;

    ImgTypeEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static String enumOfDesc(String value) {
        for (ImgTypeEnum enums :values()) {
            if (enums.value.equals(value)) {
                return enums.desc;
            }
        }
        return null;
    }
}
