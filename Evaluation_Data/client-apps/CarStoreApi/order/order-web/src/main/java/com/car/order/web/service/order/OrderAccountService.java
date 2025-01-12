package com.car.order.web.service.order;

import com.car.account.client.response.platform.PlatformStreamRes;

import java.math.BigDecimal;

/**
 * @program: car-service
 * @description:
 * @author: niushuaixiang
 * @create: 2021-04-16 18:27
 */
public interface OrderAccountService {

    /**
     * 共享技师，技师入账，平台出账
     * @param orderUuid
     * @param orderType
     */
    public void shareTechnicianOrderBranchAccount(String orderUuid ,Integer orderType);


    /**
     * 现场下单分账，和修改账户
     * @param orderUuid
     * @param orderType
     */
    public void sceneOrderBranchAccount(String orderUuid ,Integer orderType);

    /**
     * 店铺入账，平台出账
     * @param orderUuid
     * @param orderType
     */
    public void orderGoodsBranchAccount(String orderUuid ,Integer orderType);

    /**
     * 查询平台入账，并插入出账金额
     * @param orderType
     * @param orderNum
     * @return
     */
    public PlatformStreamRes getPlatformStreamRes(Integer orderType, String  orderNum);

    /**
     * 添加账户流水
     * @param orderNo
     * @param orderType
     * @param amt
     * @param userUid
     * @param userType
     * @param streamType
     */
    public void addProfit(String orderNo, Integer orderType, BigDecimal amt, String userUid, Integer userType, Integer streamType);
}
