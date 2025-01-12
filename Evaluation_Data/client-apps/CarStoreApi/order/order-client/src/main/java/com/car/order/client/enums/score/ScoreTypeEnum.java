package com.car.order.client.enums.score;

import com.car.order.client.enums.front.GoodsOrderTypeEnum;
import lombok.Getter;

/**
 * Created by Intellij IDEA.
 *
 * @author:  cjw
 * Date:  2021/1/30
 */
@Getter
public enum ScoreTypeEnum {

    /**
     *打分类型1商品2服务3店铺
     */
    GOODS(1, "商品"),
    SERVICE(2, "服务"),
    STORE(3, "店铺"),
    ;

    private Integer value;
    private String desc;

    ScoreTypeEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static String enumOfDesc(Integer value) {
        for (ScoreTypeEnum enums :values()) {
            if (enums.value.equals(value)) {
                return enums.desc;
            }
        }
        return null;
    }
}
