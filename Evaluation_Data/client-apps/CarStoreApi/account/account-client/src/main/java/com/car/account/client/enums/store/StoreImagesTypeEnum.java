package com.car.account.client.enums.store;

import lombok.Getter;

/**
 * @author zhouz
 * @date 2020/12/19
 */
@Getter
public enum StoreImagesTypeEnum {

    /**
     * 店铺图片类型枚举:1 营业执照  2 门店图片
     */
    BUSINESS_IMG(1, "营业执照"),
    SHOP_IMG(2, "门店图片"),
    OTHER_IMG(3, "其他图片");
    private Integer value;
    private String desc;

    StoreImagesTypeEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static String enumOfDesc(Integer value) {
        for (StoreImagesTypeEnum enums :values()) {
            if (enums.value.equals(value)) {
                return enums.desc;
            }
        }
        return null;
    }
}
