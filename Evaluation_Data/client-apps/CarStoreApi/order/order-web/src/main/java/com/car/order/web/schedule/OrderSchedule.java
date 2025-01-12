package com.car.order.web.schedule;

import com.alibaba.fastjson.JSON;
import com.car.account.client.feign.PlatformStreamFeign;
import com.car.account.client.feign.ProfitStreamFeign;
import com.car.account.client.feign.StoreFegin;
import com.car.account.client.feign.TechnicianFegin;
import com.car.account.client.request.platform.PlatformStreamReq;
import com.car.account.client.request.platform.SelectPlatformReq;
import com.car.account.client.request.profit.AddProfitReq;
import com.car.account.client.request.store.UpdateStoreAccountReq;
import com.car.account.client.request.technician.UpdateTechnicianAccountReq;
import com.car.account.client.response.platform.PlatformStreamRes;
import com.car.common.enums.*;
import com.car.common.exception.BusinessException;
import com.car.common.res.ResultRes;
import com.car.order.client.enums.consult.OrderTypeEnum;
import com.car.order.client.enums.front.GoodsOrderTypeEnum;
import com.car.order.client.enums.goods.OrderStsEnum;
import com.car.order.client.enums.platform.PlatformClassifyEnum;
import com.car.order.client.enums.sharetechnicianorder.ShareTechnicianOrderEnum;
import com.car.order.web.mapper.goods.OrderGoodsMapper;
import com.car.order.web.mapper.order.OrderInfoMapper;
import com.car.order.web.mapper.scene.SceneOrderMapper;
import com.car.order.web.mapper.sharetechnicianorder.ShareTechnicianOrderMapper;
import com.car.order.web.model.goods.OrderGoods;
import com.car.order.web.model.order.OrderInfo;
import com.car.order.web.model.scene.SceneOrder;
import com.car.order.web.model.sharetechnicianorder.ShareTechnicianOrder;
import com.car.order.web.service.order.OrderAccountService;
import com.car.order.web.service.pay.PayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @program: car-service
 * @description:
 * @author: niushuaixiang
 * @create: 2021-04-16 14:12
 */
@Slf4j
@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling
public class OrderSchedule {

    @Autowired
    OrderInfoMapper orderInfoMapper;

    @Autowired
    OrderAccountService orderAccountService;


    @Scheduled(cron = "0 */1 * * * *")
    public void updateAccountOrder() {
        List<OrderInfo> list = orderInfoMapper.orderGoodsBySts(OrderStsEnum.HAVE_PAID.getValue());
        for (OrderInfo info:list){
            try {
                boolean over = false;
                if (OrderTypeEnum.GOOD.getValue().equals(info.getOrderType())) {
                    //维修保养订单分账
                    orderAccountService.orderGoodsBranchAccount(info.getOrderUuid(), info.getOrderType());
                    over= true;
                } else if (OrderTypeEnum.SCENE.getValue().equals(info.getOrderType())) {
                    //修改现场下单订单为已完成并从平台划账给店铺

                    orderAccountService.sceneOrderBranchAccount(info.getOrderUuid(), info.getOrderType());
                    over= true;
                } else if (OrderTypeEnum.SHARED_TECHNICIAN.getValue().equals(info.getOrderType())) {
                    //共享技师订单分账
                    orderAccountService.shareTechnicianOrderBranchAccount(info.getOrderUuid(), info.getOrderType());
                    over= true;

                }
                if(over) {
                    OrderInfo orderInfoUpdate = new OrderInfo();
                    orderInfoUpdate.setOrderUuid(info.getOrderUuid());
                    orderInfoUpdate.setPaySts(OrderStsEnum.COMPLETED.getValue());
                    orderInfoUpdate.setLastUpdatedTime(new Date());
                    int orderInfoUpdateNum = orderInfoMapper.updateRefund(orderInfoUpdate);
                    if (orderInfoUpdateNum <= 0) {
                        log.error("修改orderInfo订单信息支付状态失败，请求参数为：{}", JSON.toJSONString(orderInfoUpdate));
                        throw new BusinessException(ResEnum.UPDATE_DB_ERROR);
                    }
                }
            }catch (Exception e){
                log.error("订单异常！！编号+======"+info.getOrderUuid());
            }
        }

    }


}