package com.car.order.web.service.order.impl;

import com.alibaba.fastjson.JSON;
import com.car.account.client.feign.*;
import com.car.account.client.request.platform.PlatformStreamReq;
import com.car.account.client.request.platform.SelectPlatformReq;
import com.car.account.client.request.profit.AddProfitReq;
import com.car.account.client.request.store.UpdateStoreAccountReq;
import com.car.account.client.request.technician.UpdateTechnicianAccountReq;
import com.car.account.client.response.platform.PlatformStreamRes;
import com.car.account.client.response.store.StoreUserRes;
import com.car.common.enums.ResEnum;
import com.car.common.enums.StreamTypeEnum;
import com.car.common.enums.StsEnum;
import com.car.common.enums.UserTypeEnum;
import com.car.common.exception.BusinessException;
import com.car.common.res.ResultRes;
import com.car.order.client.enums.consult.OrderTypeEnum;
import com.car.order.client.enums.goods.OrderStsEnum;
import com.car.order.client.enums.goods.SceneOrderStsEnum;
import com.car.order.client.enums.platform.PlatformClassifyEnum;
import com.car.order.client.enums.sharetechnicianorder.ShareTechnicianOrderEnum;
import com.car.order.web.mapper.goods.OrderGoodsMapper;
import com.car.order.web.mapper.scene.SceneOrderMapper;
import com.car.order.web.mapper.sharetechnicianorder.ShareTechnicianOrderMapper;
import com.car.order.web.model.goods.OrderGoods;
import com.car.order.web.model.scene.SceneOrder;
import com.car.order.web.model.sharetechnicianorder.ShareTechnicianOrder;
import com.car.order.web.service.order.OrderAccountService;
import com.codingapi.txlcn.tc.annotation.TxcTransaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @program: car-service
 * @description:
 * @author: niushuaixiang
 * @create: 2021-04-16 18:29
 */
@Slf4j
@Service
public class OrderAccountServiceImpl implements OrderAccountService {

    @Autowired
    private PlatformStreamFeign platformStreamFeign;

    @Autowired
    private ProfitStreamFeign profitStreamFeign;

    @Autowired
    private StoreFegin storeFegin;
    @Autowired
    private TechnicianFegin technicianFegin;
    @Autowired
    private StoreUserFeign storeUserFeign;
    @Autowired
    OrderGoodsMapper orderGoodsMapper;
    @Autowired
    SceneOrderMapper sceneOrderMapper;
    @Autowired
    ShareTechnicianOrderMapper shareTechnicianOrderMapper;
    /**
     * 店铺入账，平台出账
     * @param orderUuid
     * @param orderType
     */

    @Override
    @Transactional(rollbackFor = Exception.class)
    @TxcTransaction
    public void orderGoodsBranchAccount(String orderUuid , Integer orderType){
        OrderGoods orderGoodsSelect = new OrderGoods();
        orderGoodsSelect.setUuid(orderUuid);
        orderGoodsSelect.setSts(StsEnum.ACTIVE.getValue());
        OrderGoods orderGoods = orderGoodsMapper.selectOne(orderGoodsSelect);
        if (!StringUtils.isEmpty(orderGoods.getStoreUuid())) {

            PlatformStreamRes platformStreamRes = getPlatformStreamRes(orderType, orderGoods.getOrderNum());
            if (platformStreamRes ==null){
                throw new BusinessException(ResEnum.NON_EXISTENT);

            }
            if(orderGoods.getAmtExpress()!=null && BigDecimal.ZERO.compareTo(orderGoods.getAmtExpress())!= 0 ) {
                //平台补贴
                PlatformStreamReq platformStreamReq = new PlatformStreamReq();
                platformStreamReq.setAmt(orderGoods.getAmtExpress());
                platformStreamReq.setClassify(PlatformClassifyEnum.SUBSIDY.getValue());
                platformStreamReq.setOrderNo(orderGoods.getOrderNum());
                platformStreamReq.setOrderType(orderType);
                platformStreamReq.setStreamType(StreamTypeEnum.OUT.getType());
                platformStreamFeign.addPlatfrom(platformStreamReq);
                platformStreamRes.setAmt(platformStreamRes.getAmt().add(orderGoods.getAmtExpress()));
            }

            addProfit(orderGoods.getOrderNum(), orderType, platformStreamRes.getAmt(), orderGoods.getStoreUuid(), UserTypeEnum.store.getType(), StreamTypeEnum.IN.getType());
            //修改店铺账户信息
            UpdateStoreAccountReq req = new UpdateStoreAccountReq();
            req.setStoreUuid(orderGoods.getStoreUuid());
            req.setOrderAmount(platformStreamRes.getAmt());
            ResultRes<String> resultRes = storeFegin.updateStoreAccount(req);
            if (!resultRes.isSuccess()) {
                log.error("修改店铺账户信息，请求参数为：{}", JSON.toJSONString(req));
                throw new BusinessException(ResEnum.UPDATE_DB_ERROR);
            }
        }

        orderGoods.setOrderSts(OrderStsEnum.COMPLETED.getValue());
        orderGoods.setLastUpdatedTime(new Date());
        int updateOrderGoodsNum = orderGoodsMapper.updateByPrimaryKeySelective(orderGoods);
        if (updateOrderGoodsNum <= 0) {
            log.error("修改维修保养订单信息支付状态失败，请求参数为：{} ", JSON.toJSONString(orderGoods));
            throw new BusinessException(ResEnum.UPDATE_DB_ERROR);
        }


    }

    /**
     * 查询平台入账，并插入出账金额
     * @param orderType
     * @param orderNum
     * @return
     */
    @Override
    public PlatformStreamRes getPlatformStreamRes(Integer orderType, String  orderNum) {

        SelectPlatformReq selectPlatformReq = new SelectPlatformReq();
        selectPlatformReq.setClassify(PlatformClassifyEnum.ACCOUNT.getValue());
        selectPlatformReq.setOrderNo(orderNum);
        selectPlatformReq.setStreamType(StreamTypeEnum.IN.getType());
        selectPlatformReq.setOrderType(orderType);
        PlatformStreamRes platformStreamRes = platformStreamFeign.selectPlatfrom(selectPlatformReq).getData();
        if(platformStreamRes == null){
            log.error("没有平台入账订单编号orderNum==="+orderNum +"订单类型为orderType==="+orderType );
            return null;
        }
        PlatformStreamReq platformStreamReq = new PlatformStreamReq();
        BeanUtils.copyProperties(platformStreamRes,platformStreamReq);
        platformStreamReq.setStreamType(StreamTypeEnum.OUT.getType());
        //log.error("getPlatformStreamRes----------------platformStreamRes"+platformStreamReq.toString());
        platformStreamFeign.addPlatfrom(platformStreamReq);
        return platformStreamRes;
    }

    /**
     * 现场下单分账，和修改账户
     * @param orderUuid
     * @param orderType
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @TxcTransaction
    public void sceneOrderBranchAccount(String orderUuid , Integer orderType){
        SceneOrder sceneOrder = new SceneOrder();
        sceneOrder.setUuid(orderUuid);
        sceneOrder.setSts(StsEnum.ACTIVE.getValue());
        sceneOrder = sceneOrderMapper.selectOne(sceneOrder);
        if (null != sceneOrder) {
            addProfit(sceneOrder.getOrderNum(), OrderTypeEnum.SCENE_SERVICE.getValue(), sceneOrder.getTotalAmount(), sceneOrder.getBuyerUuid(), sceneOrder.getConfirmType(), StreamTypeEnum.IN.getType());

            PlatformStreamRes platformStreamRes = getPlatformStreamRes(orderType, sceneOrder.getOrderNum());
            if (platformStreamRes ==null){
                return;
            }
            if(sceneOrder.getConfirmType().equals(UserTypeEnum.technician.getType())){
                //修改技师账户信息
                UpdateTechnicianAccountReq req = new UpdateTechnicianAccountReq();
                req.setTechnicianUuid(sceneOrder.getBuyerUuid());
                req.setOrderAmount(platformStreamRes.getAmt());
                ResultRes<String> resultRes = technicianFegin.updateTechnicianAccount(req);
                if (!resultRes.isSuccess()) {
                    log.error("查询技师账户信息，请求参数为：{}", JSON.toJSONString(req));
                    throw new BusinessException(ResEnum.UPDATE_DB_ERROR);
                }
            }else if(sceneOrder.getConfirmType().equals(UserTypeEnum.store.getType())){
                //修改店铺账户信息
                ResultRes<StoreUserRes> resResultRes = storeUserFeign.queryStoreUserInfo(sceneOrder.getBuyerUuid());
                if (resResultRes.isSuccess()) {
                    if (null != resResultRes.getData()) {
                        UpdateStoreAccountReq req = new UpdateStoreAccountReq();
                        req.setStoreUuid(resResultRes.getData().getStoreUuid());
                        req.setOrderAmount(platformStreamRes.getAmt());
                        ResultRes<String> resultRes = storeFegin.updateStoreAccount(req);
                        if (!resultRes.isSuccess()) {
                            log.error("修改店铺账户信息，请求参数为：{}", JSON.toJSONString(req));
                            throw new BusinessException(ResEnum.UPDATE_DB_ERROR);
                        }
                    }
                }

            }

            sceneOrder.setOrderSts(SceneOrderStsEnum.COMPLETED.getValue());
            sceneOrder.setLastUpdatedTime(new Date());
            int updateNum = sceneOrderMapper.updateByPrimaryKeySelective(sceneOrder);
            if (updateNum <= 0) {
                log.error("修改现场下单订单信息支付状态失败，请求参数为：{} ", JSON.toJSONString(sceneOrder));
                throw new BusinessException(ResEnum.UPDATE_DB_ERROR);
            }
        }
    }


    /**
     * 共享技师，技师入账，平台出账
     * @param orderUuid
     * @param orderType
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @TxcTransaction
    public void shareTechnicianOrderBranchAccount(String orderUuid , Integer orderType){
        log.error("shareTechnicianOrderBranchAccount----------------"+orderUuid+"-----"+orderType);
        ShareTechnicianOrder shareTechnicianOrder = shareTechnicianOrderMapper.queryOrderShareTechnicianOrderInfo(orderUuid);
        UpdateTechnicianAccountReq req = new UpdateTechnicianAccountReq();
        req.setTechnicianUuid(shareTechnicianOrder.getTechnicianUuid());
        req.setOrderAmount(shareTechnicianOrder.getReservationMoney());
        ResultRes<String> resultRes = technicianFegin.updateTechnicianAccount(req);
        if (!resultRes.isSuccess()) {
            log.error("查询技师账户信息，请求参数为：{}", JSON.toJSONString(req));
            throw new BusinessException(ResEnum.UPDATE_DB_ERROR);
        }

        PlatformStreamRes platformStreamRes = getPlatformStreamRes(orderType, shareTechnicianOrder.getOrderNum());

        if (platformStreamRes ==null){
            return;
        }
        log.error("shareTechnicianOrderBranchAccount----------------platformStreamRes"+platformStreamRes.toString());
        addProfit(shareTechnicianOrder.getOrderNum(), orderType,platformStreamRes.getAmt() , shareTechnicianOrder.getTechnicianUuid(), UserTypeEnum.technician.getType(), StreamTypeEnum.IN.getType());
        shareTechnicianOrder.setOrderStatus(ShareTechnicianOrderEnum.CancelTheOrder.getValue());
        shareTechnicianOrder.setLastUpdatedTime(new Date());
        int updateNum = shareTechnicianOrderMapper.updateByPrimaryKeySelective(shareTechnicianOrder);
        if (updateNum <= 0) {
            log.error("修改预定技师订单信息支付状态失败，请求参数为：{} ", JSON.toJSONString(shareTechnicianOrder));
            throw new BusinessException(ResEnum.UPDATE_DB_ERROR);
        }
    }


    /**
     * 添加账户流水
     * @param orderNo
     * @param orderType
     * @param amt
     * @param userUid
     * @param userType
     * @param streamType
     */
    @Override
    public void addProfit(String orderNo, Integer orderType, BigDecimal amt, String userUid, Integer userType, Integer streamType) {
        AddProfitReq addProfitReq = new AddProfitReq();
        addProfitReq.setAmt(amt);
        addProfitReq.setUserType(userType);
        addProfitReq.setUserUuid(userUid);
        addProfitReq.setClassify(orderType);
        addProfitReq.setOrderNo(orderNo);
        addProfitReq.setStreamType(streamType);
        profitStreamFeign.addProfit(addProfitReq);
    }
}