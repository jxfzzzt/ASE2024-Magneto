package com.car.common.utils;

import com.car.common.enums.OrderPrefixEnum;

/**
 * @author zhangyp
 * @date 2021/1/24 17:36
 */
public class OrderUtils {

    private static IdWorker idWorker;

    /**
     * 生成订单号
     * @param orderPrefixEnum
     * @return
     */
    public static String GenOrderNo(OrderPrefixEnum orderPrefixEnum){

        if(null == idWorker){
            idWorker = new IdWorker(1, 1);
        }
        return orderPrefixEnum.getType() + idWorker.nextId();
    }
}
