package com.car.order.web.service.front.goods.impl;

import com.car.account.client.feign.StoreUserFeign;
import com.car.account.client.response.store.StoreUserRes;
import com.car.common.enums.ResEnum;
import com.car.common.enums.UserTypeEnum;
import com.car.common.exception.BusinessException;
import com.car.common.res.PageRes;
import com.car.common.res.ResultRes;
import com.car.common.utils.IdWorker;
import com.car.common.utils.TokenHelper;
import com.car.order.client.enums.consult.OrderTypeEnum;
import com.car.order.client.enums.front.GoodsOrderTypeEnum;
import com.car.order.client.enums.front.QueryStateEnum;
import com.car.order.client.enums.goods.OrderStsEnum;
import com.car.order.client.enums.goods.ServiceStsEnum;
import com.car.order.client.enums.score.ScoreTypeEnum;
import com.car.order.client.enums.sharetechnicianorder.ShareTechnicianOrderEnum;
import com.car.order.client.request.order.front.QueryOrderStsNumReq;
import com.car.order.client.request.order.goods.QueryOrderGoodsFrontListReq;
import com.car.order.client.response.order.front.QueryOrderStsNumRes;
import com.car.order.client.response.order.goods.OrderGoodsDetailFrontRes;
import com.car.order.client.response.order.goods.OrderGoodsFrontListRes;
import com.car.order.client.response.order.goods.OrderGoodsFrontRes;
import com.car.order.web.common.constants.Constants;
import com.car.order.web.dto.OrderGoodsDto;
import com.car.order.web.mapper.consult.OrderConsultFrontMapper;
import com.car.order.web.mapper.goods.GoodsDetailMapper;
import com.car.order.web.mapper.goods.OrderGoodsFrontMapper;
import com.car.order.web.mapper.instance.OrderCaseFrontMapper;
import com.car.order.web.mapper.score.ScoreInfoMapper;
import com.car.order.web.mapper.sharetechnicianorder.ShareTechnicianOrderMapper;
import com.car.order.web.model.goods.GoodsDetail;
import com.car.order.web.model.sharetechnicianorder.ShareTechnicianOrder;
import com.car.order.web.service.front.goods.OrderGoodsFrontService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhouz
 * @date 2020/12/30
 */
@Slf4j
@Service
public class OrderGoodsFrontServiceImpl implements OrderGoodsFrontService {

    @Autowired
    OrderGoodsFrontMapper orderGoodsFrontMapper;

    @Autowired
    private GoodsDetailMapper goodsDetailMapper;

    @Autowired
    private OrderConsultFrontMapper orderConsultFrontMapper;

    @Autowired
    private OrderCaseFrontMapper orderCaseFrontMapper;

    @Autowired
    private ScoreInfoMapper scoreInfoMapper;

    @Autowired
    StoreUserFeign storeUserFeign;

    @Autowired
    ShareTechnicianOrderMapper shareTechnicianOrderMapper;



    private IdWorker idWorker = new IdWorker(1, 1);
    /**
     * 查询维修保养订单
     * @param param
     * @return
     */
    @Override
    public PageRes<List<OrderGoodsFrontListRes>> queryOrderGoodsList(QueryOrderGoodsFrontListReq param) {
        log.debug("查询维修保养订单");
        PageHelper.startPage(param.getPageNum(), param.getPageSize());
        OrderGoodsDto orderGoodsDto = new OrderGoodsDto();
        if (QueryStateEnum.UNPAID.getValue().equals(param.getState())) {
            //查询待付款订单
            orderGoodsDto.setOrderSts(OrderStsEnum.UNPAID.getValue());
        } else if (QueryStateEnum.STAY_SERVICE.getValue().equals(param.getState())) {
            //查询待服务订单（已支付订单与未服务订单）
            orderGoodsDto.setOrderSts(OrderStsEnum.HAVE_PAID.getValue());
            orderGoodsDto.setServiceSts(ServiceStsEnum.NOT_SERVICE.getValue());
        } else if (QueryStateEnum.STAY_EVALUATE.getValue().equals(param.getState())) {
            //查询待评价订单
            orderGoodsDto.setOrderSts(OrderStsEnum.HAVE_PAID.getValue());
            orderGoodsDto.setServiceSts(ServiceStsEnum.ALREADY_SERVICE.getValue());
        } else if (QueryStateEnum.REFUND.getValue().equals(param.getState())) {
            //查询退款订单
            List<Integer> list = new ArrayList<>();
            list.add(OrderStsEnum.A_REFUND_OF.getValue());
            list.add(OrderStsEnum.REFUND_SUCCESS.getValue());
            list.add(OrderStsEnum.REFUND_FAILURE.getValue());
            orderGoodsDto.setOrderStsList(list);
        }
        if(UserTypeEnum.store.getType().equals(TokenHelper.getUserType())){
            ResultRes<StoreUserRes> storeUserRes = storeUserFeign.queryStoreUserInfo(TokenHelper.getUserUuid());
            if(!storeUserRes.isSuccess()){
                throw new BusinessException(ResEnum.STORE_CONTACT_NOT_EXIST);
            }
            StoreUserRes storeUser = storeUserRes.getData();
            orderGoodsDto.setStoreUuid(storeUser.getStoreUuid());
        }else if(UserTypeEnum.vehicle.getType().equals(TokenHelper.getUserType())){
            orderGoodsDto.setUserUuid(TokenHelper.getUserUuid());
        }
        List<OrderGoodsFrontListRes> orderGoodsList = orderGoodsFrontMapper.queryOrderGoodsList(orderGoodsDto);
        if (!CollectionUtils.isEmpty(orderGoodsList)) {
            //判断订单类型
            orderGoodsList.forEach(orderGoods -> {
                Integer orderType = this.goodsOrderType(orderGoods.getAmtExpress(), orderGoods.getAmtService());
                //查询订单详情信息
                orderGoods.setOrderType(orderType);
            });
        }
        PageInfo<OrderGoodsFrontListRes> pageInfo = new PageInfo<>(orderGoodsList);
        return PageRes.success(orderGoodsList, pageInfo.getPageSize(), (int) pageInfo.getTotal(), pageInfo.getPages());
    }

    /**
     * 查询维修保养订单详情
     * @param uuid
     * @return
     */
    @Override
    public ResultRes<OrderGoodsFrontRes> queryOrderGoodsDetail(String uuid) {
        log.debug("查询维修保养订单详情");
        OrderGoodsFrontRes orderGoodsFrontRes = orderGoodsFrontMapper.queryOrderGoods(uuid);
        if (null != orderGoodsFrontRes) {
            //获取订单类型
            Integer orderType = this.goodsOrderType(orderGoodsFrontRes.getAmtExpress(), orderGoodsFrontRes.getAmtService());
            //查询商品评分
            BigDecimal goodsScore = scoreInfoMapper.queryScore(orderGoodsFrontRes.getUuid(), ScoreTypeEnum.GOODS.getValue());
            //查询服务评分
            BigDecimal serviceScore = scoreInfoMapper.queryScore(orderGoodsFrontRes.getUuid(), ScoreTypeEnum.SERVICE.getValue());
            orderGoodsFrontRes.setOrderType(orderType);
            orderGoodsFrontRes.setGoodsScore(goodsScore);
            orderGoodsFrontRes.setServiceScore(serviceScore);
            //查询商品明细列表
            List<GoodsDetail> goodsDetailList = goodsDetailMapper.queryListByGoodsId(orderGoodsFrontRes.getGoodsUuid());
            if (!CollectionUtils.isEmpty(goodsDetailList)) {
                List<OrderGoodsDetailFrontRes> orderGoodsDetailFrontResList = new ArrayList<>();
                goodsDetailList.forEach(goodsDetail -> {
                    OrderGoodsDetailFrontRes orderGoodsDetailFrontRes = new OrderGoodsDetailFrontRes();
                    orderGoodsDetailFrontRes.setUuid(goodsDetail.getUuid());
                    orderGoodsDetailFrontRes.setGoodsUuid(goodsDetail.getGoodsUuid());
                    orderGoodsDetailFrontRes.setGoodsDetailName(goodsDetail.getName());
                    orderGoodsDetailFrontRes.setGoodsDetailNum(goodsDetail.getNum());
                    orderGoodsDetailFrontRes.setGoodsDetailAmt(goodsDetail.getAmt());
                    orderGoodsDetailFrontRes.setGoodsDetailActAmt(goodsDetail.getActAmt());
                    orderGoodsDetailFrontResList.add(orderGoodsDetailFrontRes);
                });
                orderGoodsFrontRes.setOrderGoodsDetailFrontRes(orderGoodsDetailFrontResList);
            }
        }

        return ResultRes.success(orderGoodsFrontRes);
    }

    /**
     * 查询订单各状态数量
     * @param req
     * @return
     */
    @Override
    public ResultRes<QueryOrderStsNumRes> queryOrderStsNum(QueryOrderStsNumReq req) {
        //已完成数量
        Integer completedNum = 0;
        //未付款数量
        Integer unpaidNum = 0;
        //已付款未完成数量
        Integer paidNotCompleteNum = 0;
        //已退款数量
        Integer refunded = 0;
        //用户uuid
        String userUuid = TokenHelper.getUserUuid();
        if (OrderTypeEnum.GOOD.getValue().equals(req.getOrderType())) {
            //维修保养订单
            String storeUuid = null;
            if(UserTypeEnum.store.getType().equals(TokenHelper.getUserType())){
                ResultRes<StoreUserRes> storeUserRes = storeUserFeign.queryStoreUserInfo(TokenHelper.getUserUuid());
                if(!storeUserRes.isSuccess()){
                    throw new BusinessException(ResEnum.STORE_CONTACT_NOT_EXIST);
                }
                StoreUserRes storeUser = storeUserRes.getData();
                storeUuid = storeUser.getStoreUuid();
                //当用户角色为店铺的时候，不需要根据用户ID进行查询
                userUuid = null;
            }
            //已完成
            completedNum = orderGoodsFrontMapper.queryOrderStsNum(OrderStsEnum.COMPLETED.getValue(), null, userUuid,storeUuid);
            //未付款
            unpaidNum = orderGoodsFrontMapper.queryOrderStsNum(OrderStsEnum.UNPAID.getValue(), null, userUuid,storeUuid);
            //已付款未完成
            paidNotCompleteNum = orderGoodsFrontMapper.queryOrderStsNum(OrderStsEnum.HAVE_PAID.getValue(), ServiceStsEnum.NOT_SERVICE.getValue(), userUuid,storeUuid);
            //已退款
            refunded = orderGoodsFrontMapper.queryOrderStsNum(OrderStsEnum.REFUND_SUCCESS.getValue(), null, userUuid,storeUuid);
        } else if (OrderTypeEnum.CONSULT.getValue().equals(req.getOrderType())) {
            //线上咨询订单
            String technicianUuid = null;
            if(UserTypeEnum.technician.getType().equals(TokenHelper.getUserType())){
                technicianUuid = TokenHelper.getUserUuid();
                //清空用户ID参数，技师角色不需要根据用户ID查询
                userUuid = null;
            }
            //已完成
            completedNum = orderConsultFrontMapper.queryOrderStsNum(OrderStsEnum.HAVE_PAID.getValue(), ServiceStsEnum.ALREADY_SERVICE.getValue(), userUuid,technicianUuid);
            //未付款
            unpaidNum = orderConsultFrontMapper.queryOrderStsNum(OrderStsEnum.UNPAID.getValue(), null, userUuid,technicianUuid);
            //已付款未完成
            paidNotCompleteNum = orderConsultFrontMapper.queryOrderStsNum(OrderStsEnum.HAVE_PAID.getValue(), ServiceStsEnum.NOT_SERVICE.getValue(), userUuid,technicianUuid);
            //已退款
            refunded = orderConsultFrontMapper.queryOrderStsNum(OrderStsEnum.REFUND_SUCCESS.getValue(), null, userUuid,technicianUuid);
        } else if (OrderTypeEnum.EXAMPLE.getValue().equals(req.getOrderType())) {
            //技师uuid
            String technicianUuid = null;
            //车主uuid
            String carOwnerUuid = null;
            //判断登录用户为技师或车主（默认为车主）
            if (UserTypeEnum.technician.getType().equals(TokenHelper.getUserType())) {
                //技师
                technicianUuid = userUuid;
            } else {
                //车主
                carOwnerUuid = userUuid;
            }
            //案例订单
            //已完成
            completedNum = orderCaseFrontMapper.queryOrderStsNum(OrderStsEnum.HAVE_PAID.getValue(), technicianUuid, carOwnerUuid);
            //未付款
            unpaidNum = orderCaseFrontMapper.queryOrderStsNum(OrderStsEnum.UNPAID.getValue(), technicianUuid, carOwnerUuid);
            //已付款未完成(案例支付即完成，不存在已付款未完成情况)
            //已退款
            refunded = orderCaseFrontMapper.queryOrderStsNum(OrderStsEnum.REFUND_SUCCESS.getValue(), technicianUuid, carOwnerUuid);
        }else if (OrderTypeEnum.SHARED_TECHNICIAN.getValue().equals(req.getOrderType())) {
            //技师uuid
            String technicianUuid = TokenHelper.getUserUuid();

            //已完成
            List<Integer> orderStatus=new ArrayList<>();
            orderStatus.add(ShareTechnicianOrderEnum.CancelTheOrder.getValue());
            completedNum = shareTechnicianOrderMapper.queryOrderStsNum(orderStatus, null,technicianUuid);
            //未付款
            orderStatus.clear();
            orderStatus.add(ShareTechnicianOrderEnum.WaitingForOrder.getValue());
            unpaidNum = shareTechnicianOrderMapper.queryOrderStsNum(orderStatus, null,technicianUuid);
            //已付款未完成
            orderStatus.clear();
            orderStatus.add(ShareTechnicianOrderEnum.WaitingForOrder.getValue());
            orderStatus.add(ShareTechnicianOrderEnum.PendingOrder.getValue());
            paidNotCompleteNum = shareTechnicianOrderMapper.queryOrderStsNum(orderStatus, null,technicianUuid);
            //已退款
            orderStatus.clear();
            orderStatus.add(ShareTechnicianOrderEnum.Refunded.getValue());
            refunded = shareTechnicianOrderMapper.queryOrderStsNum(orderStatus, null,technicianUuid);
        }
        QueryOrderStsNumRes queryOrderStsNumRes = new QueryOrderStsNumRes();
        queryOrderStsNumRes.setCompletedNum(completedNum);
        queryOrderStsNumRes.setUnpaidNum(unpaidNum);
        queryOrderStsNumRes.setPaidNotCompleteNum(paidNotCompleteNum);
        queryOrderStsNumRes.setRefunded(refunded);
        return ResultRes.success(queryOrderStsNumRes);
    }


    /**
     * 判断维修保养订单类型
     * @param amtExpress 快递费
     * @param amtService 服务费
     * @return
     */
    private Integer goodsOrderType(BigDecimal amtExpress, BigDecimal amtService) {
        //订单类型（ 0 线上直接发货，无需服务， 1 购买商品且需要服务， 2 无商品，仅线下服务）
        Integer orderType;
        BigDecimal bigDecimal = new BigDecimal(Constants.TWO_BIG_DECIMAL);
        if (null != amtExpress && !(bigDecimal.equals(amtExpress))) {
            //快递费不为空并且快递费不为0.00，则为线上直接发货，无需服务
            orderType = GoodsOrderTypeEnum.NOT_SERVICE.getValue();
        } else if (null != amtService && !(bigDecimal.equals(amtService))) {
            //服务费不为空并且服务费不为0.00，则为购买商品且需要服务
            orderType = GoodsOrderTypeEnum.SERVICE.getValue();
        } else {
            //无商品，仅线下服务
            orderType = GoodsOrderTypeEnum.NOT_GOODS.getValue();
        }
        return orderType;
    }
}
