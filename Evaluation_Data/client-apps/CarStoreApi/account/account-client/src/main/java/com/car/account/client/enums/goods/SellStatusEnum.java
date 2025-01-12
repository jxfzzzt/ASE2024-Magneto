package com.car.account.client.enums.goods;

import lombok.Getter;

/**
 * @author zhouz
 * @date 2020/12/22
 */
@Getter
public enum SellStatusEnum {


    /**
     * 商品销售状态 下架
     */
    REPERTORY(0, "库存中"),
    /**
     * 上架
     */
    ON_SALE(1, "出售中")
    ;
    private Integer value;
    private String desc;

    SellStatusEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static String enumOfDesc(Integer value) {
        for (SellStatusEnum enums :values()) {
            if (enums.value.equals(value)) {
                return enums.desc;
            }
        }
        return null;
    }
}
