package com.car.order.web.service.order.impl;

import com.alibaba.fastjson.JSON;
import com.car.common.enums.ResEnum;
import com.car.common.enums.StsEnum;
import com.car.common.enums.UserTypeEnum;
import com.car.common.exception.BusinessException;
import com.car.common.res.ResultRes;
import com.car.common.utils.TokenHelper;
import com.car.common.utils.UuidUtils;
import com.car.order.client.enums.consult.OrderTypeEnum;
import com.car.order.client.enums.goods.OrderStsEnum;
import com.car.order.client.enums.order.ConfirmTypeEnum;
import com.car.order.client.enums.order.WhetherAgreeRefundEnum;
import com.car.order.client.request.order.order.AddOrderInfoReq;
import com.car.order.client.request.order.order.ConfirmOrderReq;
import com.car.order.client.request.order.order.OrderWhetherAgreeRefundReq;
import com.car.order.web.mapper.consult.ConsultOrderMapper;
import com.car.order.web.mapper.goods.OrderGoodsMapper;
import com.car.order.web.mapper.instance.OrderCaseMapper;
import com.car.order.web.mapper.order.OrderInfoMapper;
import com.car.order.web.model.consult.ConsultOrder;
import com.car.order.web.model.goods.OrderGoods;
import com.car.order.web.model.instance.OrderCase;
import com.car.order.web.model.order.OrderInfo;
import com.car.order.web.service.order.OrderAccountService;
import com.car.order.web.service.order.OrderInfoService;
import com.codingapi.txlcn.tc.annotation.TxcTransaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
 * Created by Intellij IDEA.
 *
 * @author:  cjw
 * Date:  2021/2/8
 */
@Slf4j
@Service
public class OrderInfoServiceImpl implements OrderInfoService {

    @Autowired
    private OrderInfoMapper orderInfoMapper;

    @Autowired
    private OrderGoodsMapper orderGoodsMapper;

    @Autowired
    private ConsultOrderMapper consultOrderMapper;

    @Autowired
    private OrderCaseMapper orderCaseMapper;

    @Autowired
    private OrderAccountService orderAccountService;

    /**
     * 新增订单
     * @param addOrderInfoReq
     * @return
     */
    @Override
    public ResultRes<String> addOrder(AddOrderInfoReq addOrderInfoReq) {
        String userName = TokenHelper.getUserName();
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setUuid(UuidUtils.getUuid());
        orderInfo.setOrderType(addOrderInfoReq.getOrderType());
        orderInfo.setOrderUuid(addOrderInfoReq.getOrderUuid());
        orderInfo.setSts(StsEnum.ACTIVE.getValue());
        orderInfo.setPaySts(OrderStsEnum.UNPAID.getValue());
        orderInfo.setCreatedBy(userName);
        orderInfo.setCreatedTime(new Date());
        int addOrderNum = orderInfoMapper.insert(orderInfo);
        if (addOrderNum <= 0) {
            log.error("新增信息失败，参数为：{}", JSON.toJSONString(addOrderNum));
            throw new BusinessException(ResEnum.INSERT_DB_ERROR);
        }
        return ResultRes.success(orderInfo.getOrderUuid());
    }

    /**
     * 确认订单
     * @param req
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @TxcTransaction
    public ResultRes<String> confirmOrder(ConfirmOrderReq req) {
        //当前登录用户名称
        String userName = TokenHelper.getUserName();
        //log.error("confirmOrder----------------"+req.toString());
        OrderInfo orderInfo = orderInfoMapper.queryOrderInfo(req.getOrderUuid());

        if (OrderTypeEnum.SCENE.getValue().equals(orderInfo.getOrderType())) {
            //现场下单确认
            orderAccountService.sceneOrderBranchAccount(orderInfo.getOrderUuid(),orderInfo.getOrderType());
        } else if (OrderTypeEnum.SHARED_TECHNICIAN.getValue().equals(orderInfo.getOrderType())) {
            //共享技师确认
            orderAccountService.shareTechnicianOrderBranchAccount(orderInfo.getOrderUuid(),orderInfo.getOrderType());
        } else if (OrderTypeEnum.GOOD.getValue().equals(orderInfo.getOrderType())) {
            orderAccountService.orderGoodsBranchAccount(orderInfo.getOrderUuid(),orderInfo.getOrderType());
        }

        //修改order_info订单为订单完成
        OrderInfo orderInfoUpdate = new OrderInfo();
        orderInfoUpdate.setOrderUuid(req.getOrderUuid());
        orderInfoUpdate.setPaySts(OrderStsEnum.COMPLETED.getValue());
        orderInfoUpdate.setLastUpdatedTime(new Date());
        orderInfoUpdate.setLastUpdatedBy(userName);
        int updateOrderInfoNum = orderInfoMapper.updateRefund(orderInfoUpdate);
        if (updateOrderInfoNum <= 0) {
            log.error("修改订单order_info订单失败，请求参数为：{}", JSON.toJSONString(orderInfoUpdate));
            throw new BusinessException(ResEnum.UPDATE_DB_ERROR);
        }
        return ResultRes.success(req.getOrderUuid());
    }

    /**
     * 订单退款
     * @param orderUuid
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @TxcTransaction
    public ResultRes<String> orderRefund(String orderUuid) {
        if (StringUtils.isEmpty(orderUuid)) {
            log.error("订单uuid不能为空");
            throw new BusinessException(ResEnum.LACK_PARAMETER.getValue());
        }
        //当前登录用户姓名
        String userName = TokenHelper.getUserName();
        OrderInfo orderInfoSelect = new OrderInfo();
        orderInfoSelect.setOrderUuid(orderUuid);
        //查询订单Order_info表中信息
        OrderInfo orderInfo = orderInfoMapper.selectOne(orderInfoSelect);
        if (StringUtils.isEmpty(orderInfo)) {
            throw new BusinessException(ResEnum.NON_EXISTENT);
        }
        int updateNum = 0;
        if (OrderTypeEnum.CONSULT.getValue().equals(orderInfo.getOrderType())) {
            //线上咨询订单退款
            ConsultOrder consultOrder = new ConsultOrder();
            consultOrder.setUuid(orderUuid);
            consultOrder.setOrderSts(OrderStsEnum.A_REFUND_OF.getValue());
            consultOrder.setLastUpdatedTime(new Date());
            consultOrder.setLastUpdatedBy(userName);
            updateNum = consultOrderMapper.updateByPrimaryKeySelective(consultOrder);

        } else if (OrderTypeEnum.EXAMPLE.getValue().equals(orderInfo.getOrderType())) {
            //案例订单退款
            OrderCase orderCase = new OrderCase();
            orderCase.setUuid(orderUuid);
            orderCase.setLastUpdatedTime(new Date());
            orderCase.setLastUpdatedBy(userName);
            orderCase.setOrderSts(OrderStsEnum.A_REFUND_OF.getValue());
            updateNum = orderCaseMapper.updateByPrimaryKeySelective(orderCase);
        } else if (OrderTypeEnum.AUDITOR.getValue().equals(orderInfo.getOrderType())) {
            //旁听订单退款
            ConsultOrder consultOrder = new ConsultOrder();
            consultOrder.setUuid(orderUuid);
            consultOrder.setOrderSts(OrderStsEnum.A_REFUND_OF.getValue());
            consultOrder.setLastUpdatedTime(new Date());
            consultOrder.setLastUpdatedBy(userName);
            updateNum = consultOrderMapper.updateByPrimaryKeySelective(consultOrder);
        } else {
            //维修保养订单退款
            OrderGoods orderGoods = new OrderGoods();
            orderGoods.setUuid(orderUuid);
            orderGoods.setOrderSts(OrderStsEnum.A_REFUND_OF.getValue());
            orderGoods.setLastUpdatedTime(new Date());
            orderGoods.setLastUpdatedBy(userName);
            updateNum = orderGoodsMapper.updateByPrimaryKeySelective(orderGoods);

        }
        if (updateNum <= 0) {
            log.error("订单信息修改失败");
            throw new BusinessException(ResEnum.UPDATE_DB_ERROR);
        }
        //修改order_info订单为退款中
        OrderInfo orderInfoUpdate = new OrderInfo();
        orderInfoUpdate.setOrderUuid(orderUuid);
        orderInfoUpdate.setPaySts(OrderStsEnum.A_REFUND_OF.getValue());
        orderInfoUpdate.setLastUpdatedTime(new Date());
        orderInfoUpdate.setLastUpdatedBy(userName);
        int updateOrderInfoNum = orderInfoMapper.updateRefund(orderInfoUpdate);
        if (updateOrderInfoNum <= 0) {
            log.error("修改订单order_info订单失败，请求参数为：{}", JSON.toJSONString(orderInfoUpdate));
            throw new BusinessException(ResEnum.UPDATE_DB_ERROR);
        }
        return ResultRes.success(orderUuid);
    }

    /**
     * 是否同意退款
     * @param req
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @TxcTransaction
    public ResultRes<String> orderWhetherAgreeRefund(OrderWhetherAgreeRefundReq req) {
        //当前登录用户姓名
        String userName = TokenHelper.getUserName();
        //订单uuid
        String orderUuid = req.getOrderUuid();
        OrderInfo orderInfoSelect = new OrderInfo();
        orderInfoSelect.setOrderUuid(orderUuid);
        //查询订单Order_info表中信息
        OrderInfo orderInfo = orderInfoMapper.selectOne(orderInfoSelect);
        int updateNum = 0;
        if (OrderTypeEnum.CONSULT.getValue().equals(orderInfo.getOrderType())) {
            //线上咨询是否订单退款
            ConsultOrder consultOrder = new ConsultOrder();
            consultOrder.setUuid(orderUuid);
            if (WhetherAgreeRefundEnum.AGREE.getValue().equals(req.getWhetherRefund())) {
                consultOrder.setOrderSts(OrderStsEnum.REFUND_SUCCESS.getValue());
            } else {
                consultOrder.setOrderSts(OrderStsEnum.REFUND_FAILURE.getValue());
            }

            consultOrder.setLastUpdatedTime(new Date());
            consultOrder.setLastUpdatedBy(userName);
            updateNum = consultOrderMapper.updateByPrimaryKeySelective(consultOrder);

        } else if (OrderTypeEnum.EXAMPLE.getValue().equals(orderInfo.getOrderType())) {
            //案例订单退款
            OrderCase orderCase = new OrderCase();
            orderCase.setUuid(orderUuid);
            orderCase.setLastUpdatedTime(new Date());
            orderCase.setLastUpdatedBy(userName);

            if (WhetherAgreeRefundEnum.AGREE.getValue().equals(req.getWhetherRefund())) {
                orderCase.setOrderSts(OrderStsEnum.REFUND_SUCCESS.getValue());
            } else {
                orderCase.setOrderSts(OrderStsEnum.REFUND_FAILURE.getValue());
            }
            updateNum = orderCaseMapper.updateByPrimaryKeySelective(orderCase);
        } else if (OrderTypeEnum.AUDITOR.getValue().equals(orderInfo.getOrderType())) {
            //旁听订单退款
            ConsultOrder consultOrder = new ConsultOrder();
            consultOrder.setUuid(orderUuid);
            if (WhetherAgreeRefundEnum.AGREE.getValue().equals(req.getWhetherRefund())) {
                consultOrder.setOrderSts(OrderStsEnum.REFUND_SUCCESS.getValue());
            } else {
                consultOrder.setOrderSts(OrderStsEnum.REFUND_FAILURE.getValue());
            }
            consultOrder.setLastUpdatedTime(new Date());
            consultOrder.setLastUpdatedBy(userName);
            updateNum = consultOrderMapper.updateByPrimaryKeySelective(consultOrder);
        } else {
            //维修保养订单退款
            OrderGoods orderGoods = new OrderGoods();
            orderGoods.setUuid(orderUuid);
            if (WhetherAgreeRefundEnum.AGREE.getValue().equals(req.getWhetherRefund())) {
                orderGoods.setOrderSts(OrderStsEnum.REFUND_SUCCESS.getValue());
                orderGoods.setRefundSts(WhetherAgreeRefundEnum.AGREE.getValue());
            } else {
                orderGoods.setOrderSts(OrderStsEnum.REFUND_FAILURE.getValue());
                orderGoods.setRefundSts(WhetherAgreeRefundEnum.NOT_AGREE.getValue());
            }

            orderGoods.setLastUpdatedTime(new Date());
            orderGoods.setLastUpdatedBy(userName);
            updateNum = orderGoodsMapper.updateByPrimaryKeySelective(orderGoods);

        }
        if (updateNum <= 0) {
            log.error("订单信息修改失败");
            throw new BusinessException(ResEnum.UPDATE_DB_ERROR);
        }
        //修改order_info订单为退款中
        OrderInfo orderInfoUpdate = new OrderInfo();
        orderInfoUpdate.setOrderUuid(orderUuid);
        if (WhetherAgreeRefundEnum.AGREE.getValue().equals(req.getWhetherRefund())) {
            orderInfoUpdate.setPaySts(OrderStsEnum.REFUND_SUCCESS.getValue());
        } else {
            orderInfoUpdate.setPaySts(OrderStsEnum.REFUND_FAILURE.getValue());
        }
        orderInfoUpdate.setLastUpdatedTime(new Date());
        orderInfoUpdate.setLastUpdatedBy(userName);
        int updateOrderInfoNum = orderInfoMapper.updateRefund(orderInfoUpdate);
        if (updateOrderInfoNum <= 0) {
            log.error("修改订单order_info订单失败，请求参数为：{}", JSON.toJSONString(orderInfoUpdate));
            throw new BusinessException(ResEnum.UPDATE_DB_ERROR);
        }
        return ResultRes.success(orderUuid);
    }


    /**
     * 根据id查询订单状态详情
     * @param orderInfoUuid
     * @return
     */
    @Override
    public ResultRes<Boolean> queryOrderSts(String orderInfoUuid) {
        if (StringUtils.isEmpty(orderInfoUuid)) {
            log.error("根据id查询订单状态，uuid不能为空：{}", orderInfoUuid);
            throw new BusinessException(ResEnum.LACK_PARAMETER);
        }
        Boolean flag = false;
        OrderInfo orderInfoSelect = new OrderInfo();
        orderInfoSelect.setOrderUuid(orderInfoUuid);
        OrderInfo orderInfo = orderInfoMapper.selectOne(orderInfoSelect);
        if (null == orderInfo) {
            log.error("查询订单状态为空，查询uuid为：{}", orderInfoUuid);
            throw new BusinessException(ResEnum.NON_EXISTENT.getValue());
        }
        if (OrderStsEnum.HAVE_PAID.getValue().equals(orderInfo.getPaySts())) {
            flag = true;
        }
        return ResultRes.success(flag);
    }

}
