package com.car.order.web.service.front.instance.impl;

import com.car.account.client.feign.StoreFegin;
import com.car.account.client.feign.TechnicianFegin;
import com.car.account.client.feign.VehicleFegin;
import com.car.account.client.response.store.StoreDetailRes;
import com.car.account.client.response.technician.TechnicianRes;
import com.car.account.client.response.vehicle.vehicleUser.VehicleUserRes;
import com.car.common.enums.OrderPrefixEnum;
import com.car.common.enums.ResEnum;
import com.car.common.enums.StsEnum;
import com.car.common.enums.UserTypeEnum;
import com.car.common.exception.BusinessException;
import com.car.common.res.PageRes;
import com.car.common.res.ResultRes;
import com.car.common.utils.*;
import com.car.order.client.enums.consult.OrderTypeEnum;
import com.car.order.client.enums.front.QueryStateEnum;
import com.car.order.client.enums.goods.EvaluateStsEnum;
import com.car.order.client.enums.goods.OrderStsEnum;
import com.car.order.client.request.order.instance.QueryOrderCaseFrontListReq;
import com.car.order.client.request.order.order.AddOrderInfoReq;
import com.car.order.client.response.order.instance.*;
import com.car.order.web.dto.OrderCaseDto;
import com.car.order.web.mapper.instance.OrderCaseDetailMapper;
import com.car.order.web.mapper.instance.OrderCaseFrontMapper;
import com.car.order.web.mapper.instance.OrderCaseMapper;
import com.car.order.web.mapper.technician.TechnicianCaseImgMapper;
import com.car.order.web.mapper.technician.TechnicianCaseMapper;
import com.car.order.web.model.instance.OrderCase;
import com.car.order.web.model.instance.OrderCaseDetail;
import com.car.order.web.model.technician.cases.TechnicianCase;
import com.car.order.web.service.front.instance.OrderCaseFrontService;
import com.car.order.web.service.order.OrderInfoService;
import com.codingapi.txlcn.tc.annotation.TxcTransaction;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author zhouz
 * @date 2020/12/31
 */
@Slf4j
@Service
public class OrderCaseFrontServiceImpl implements OrderCaseFrontService {

    @Autowired
    OrderCaseFrontMapper orderCaseFrontMapper;
    @Resource
    private TechnicianCaseMapper technicianCaseMapper;

    @Autowired
    private TechnicianCaseImgMapper technicianCaseImgMapper;

    @Resource
    private OrderCaseMapper orderCaseMapper;
    @Resource
    private OrderCaseDetailMapper orderCaseDetailMapper;
    @Resource
    VehicleFegin vehicleFegin;
    @Resource
    TechnicianFegin technicianFegin;
    @Resource
    StoreFegin  storeFegin;

    @Autowired
    private OrderInfoService orderInfoService;


    /**
     * 查询案例订单列表
     * @param param
     * @return
     */
    @Override
    public PageRes<List<OrderCaseFrontListRes>> queryOrderCaseList(QueryOrderCaseFrontListReq param) {
        log.debug("查询案例订单列表");
        PageHelper.startPage(param.getPageNum(), param.getPageSize());
        List<OrderCaseFrontListRes> orderCaseInfoList = new ArrayList<>();
        OrderCaseDto orderCaseDto = new OrderCaseDto();
        if (QueryStateEnum.UNPAID.getValue().equals(param.getState())) {
            //查询待付款订单
            orderCaseDto.setOrderSts(OrderStsEnum.UNPAID.getValue());
        } else if (QueryStateEnum.STAY_SERVICE.getValue().equals(param.getState())) {
            //查询待服务订单（已支付订单与未服务订单）
            PageInfo<OrderCaseFrontListRes> pageInfo = new PageInfo<>(orderCaseInfoList);
            return PageRes.success(orderCaseInfoList, param.getPageSize(), (int) pageInfo.getTotal(), pageInfo.getPages());
        } else if (QueryStateEnum.STAY_EVALUATE.getValue().equals(param.getState())) {
            //查询待评价订单
            orderCaseDto.setOrderSts(OrderStsEnum.HAVE_PAID.getValue());
            orderCaseDto.setEvaluateSts(EvaluateStsEnum.NO_COMMENT.getValue());
        } else if (QueryStateEnum.REFUND.getValue().equals(param.getState())) {
            //查询退款订单
            List<Integer> list = new ArrayList<>();
            list.add(OrderStsEnum.A_REFUND_OF.getValue());
            list.add(OrderStsEnum.REFUND_SUCCESS.getValue());
            list.add(OrderStsEnum.REFUND_FAILURE.getValue());
            orderCaseDto.setOrderStsList(list);
        }
        //判断登录用户是技师或车主（默认车主）
        if (UserTypeEnum.technician.getType().equals(param.getUserType())) {
            //技师
            orderCaseDto.setTechnicianUuid(TokenHelper.getUserUuid());
        } else {
            //车主/店铺
            orderCaseDto.setCarOwnerUuid(TokenHelper.getUserUuid());
        }
        orderCaseInfoList = orderCaseFrontMapper.queryOrderCaseInfoList(orderCaseDto);
        PageInfo<OrderCaseFrontListRes> pageInfo = new PageInfo<>(orderCaseInfoList);
        return PageRes.success(orderCaseInfoList, pageInfo.getPageSize(), (int) pageInfo.getTotal(), pageInfo.getPages());
    }

    /**
     * 查询案例订单详情
     * @param uuid
     * @return
     */
    @Override
    public ResultRes<OrderCaseFrontRes> queryOrderCaseDetail(String uuid) {
        log.debug("查询案例订单详情 uuid {}",uuid);
        OrderCaseFrontRes orderCaseFrontRes = orderCaseFrontMapper.queryOrderCaseDetail(uuid);
        if (!StringUtils.isEmpty(orderCaseFrontRes)){
            List<String> caseImagesList = technicianCaseImgMapper.queryCaseImages(orderCaseFrontRes.getCaseUuid());
            orderCaseFrontRes.setCaseImgUrl(caseImagesList);
        }
        return ResultRes.success(orderCaseFrontRes);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @TxcTransaction
    public ResultRes<String> order(String caseUuid) {

        String userUuid = TokenHelper.getUserUuid();
        Integer userType = TokenHelper.getUserType();
        String userName = TokenHelper.getUserName();
        if(!UserTypeEnum.vehicle.getType().equals(userType)){
            throw new BusinessException(ResEnum.VEHICLE_OWNER_NOT_EXIST);
        }
        //查询当前车主是否存在未支付的订单信息
        OrderCase orderCaseSelect = orderCaseFrontMapper.queryOrderCaseInfo(caseUuid, userUuid);
        if (null != orderCaseSelect) {
            return ResultRes.success(orderCaseSelect.getUuid());
        }
        ResultRes<VehicleUserRes> resV = vehicleFegin.queryDetail(userUuid);
        if(!resV.isSuccess()){

            throw new BusinessException(resV.getCode(),resV.getMsg());
        }

        TechnicianCase technicianCase = technicianCaseMapper.selectByPrimaryKey(caseUuid);
        if(null == technicianCase){

            log.error("未定位到案例信息>>>caseUuid:{}",caseUuid);
            throw new BusinessException(ResEnum.NON_EXISTENT);
        }

        String technicianUuid = technicianCase.getTechnicianUuid();
        ResultRes<TechnicianRes> resT = technicianFegin.queryTechnicianDetail(technicianUuid);
        if(!resT.isSuccess()){

            throw new BusinessException(resT.getCode(),resT.getMsg());
        }

        TechnicianRes t = resT.getData();

        BigDecimal amt = technicianCase.getAmt();


        VehicleUserRes v = resV.getData();
        String orderUuid = UuidUtils.getUuid();
        String orderNo = OrderUtils.GenOrderNo(OrderPrefixEnum.AL);
        OrderCase orderCase = new OrderCase();
        orderCase.setSts(StsEnum.ACTIVE.getValue());
        orderCase.setUuid(orderUuid);
        orderCase.setOrderNum(orderNo);
        orderCase.setCreatedBy(userName);
        orderCase.setCreatedTime(new Date());
        orderCase.setOrderAmount(amt);
        orderCase.setReceivableAmount(amt);

        orderCase.setCaseUuid(caseUuid);

        orderCase.setOrderSts(OrderStsEnum.UNPAID.getValue());
        orderCase.setTechnicianUuid(technicianUuid);
        orderCase.setTechnicianName(t.getUserName());
        orderCase.setTechnicianMobile(t.getMobile());
        orderCase.setCarOwnerType(userType);
        orderCase.setCarOwnerUuid(v.getUuid());
        orderCase.setCarOwnerMobile(v.getMobile());
        orderCase.setCarOwnerName(v.getUserName());

        orderCaseMapper.insert(orderCase);

        OrderCaseDetail detail = new OrderCaseDetail();
        detail.setUuid(UuidUtils.getUuid());
        detail.setOrderUuid(orderUuid);
        detail.setCaseUuid(caseUuid);
        detail.setCaseName(technicianCase.getTitle());
        detail.setSts(StsEnum.ACTIVE.getValue());
        detail.setCreatedBy(userName);
        detail.setCreatedTime(new Date());

        orderCaseDetailMapper.insert(detail);

        //新增order_info信息
        AddOrderInfoReq addOrderInfoReq = new AddOrderInfoReq();
        addOrderInfoReq.setOrderType(OrderTypeEnum.EXAMPLE.getValue());
        addOrderInfoReq.setOrderUuid(orderUuid);
        orderInfoService.addOrder(addOrderInfoReq);

        return ResultRes.success(orderUuid);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    @TxcTransaction
    public ResultRes<String> orderTwo(String caseUuid) {

        String userUuid = TokenHelper.getUserUuid();
        Integer userType = TokenHelper.getUserType();
        String userName = TokenHelper.getUserName();



        //查询当前商铺或技师是否存在未支付的订单信息
        OrderCase orderCaseSelect = orderCaseFrontMapper.queryOrderCaseInfo(caseUuid, userUuid);
        if (null != orderCaseSelect) {
            return ResultRes.success(orderCaseSelect.getUuid());
        }
        ResultRes  res  = null;
        if(UserTypeEnum.technician.getType().equals(userType)) {
            res = technicianFegin.queryTechnicianDetail(userUuid);

        }else if(UserTypeEnum.store.getType().equals(userType)){
            res = storeFegin.queryStoreDetailByUser();

        }
        if (res == null || !res.isSuccess()) {
            log.error("未定位到商铺/技师信息>>>caseUuid:{}",caseUuid);
            throw new BusinessException(ResEnum.NON_EXISTENT);
        }

        TechnicianCase technicianCase = technicianCaseMapper.selectByPrimaryKey(caseUuid);
        if(null == technicianCase){

            log.error("未定位到案例信息>>>caseUuid:{}",caseUuid);
            throw new BusinessException(ResEnum.NON_EXISTENT);
        }

        String technicianUuid = technicianCase.getTechnicianUuid();
        ResultRes<TechnicianRes> resT = technicianFegin.queryTechnicianDetail(technicianUuid);
        if(!resT.isSuccess()){

            throw new BusinessException(resT.getCode(),resT.getMsg());
        }

        TechnicianRes t = resT.getData();

        BigDecimal amt = technicianCase.getAmt();



        String orderUuid = UuidUtils.getUuid();
        String orderNo = OrderUtils.GenOrderNo(OrderPrefixEnum.AL);
        OrderCase orderCase = new OrderCase();
        orderCase.setSts(StsEnum.ACTIVE.getValue());
        orderCase.setUuid(orderUuid);
        orderCase.setOrderNum(orderNo);
        orderCase.setCreatedBy(userName);
        orderCase.setCreatedTime(new Date());
        orderCase.setOrderAmount(amt);
        orderCase.setReceivableAmount(amt);
        orderCase.setCarOwnerType(userType);
        orderCase.setCaseUuid(caseUuid);

        orderCase.setOrderSts(OrderStsEnum.UNPAID.getValue());
        orderCase.setTechnicianUuid(technicianUuid);
        orderCase.setTechnicianName(t.getUserName());
        orderCase.setTechnicianMobile(t.getMobile());


        orderCase.setCarOwnerUuid(userUuid);
        orderCase.setCarOwnerMobile(TokenHelper.getLoginToken().getUserMobile());
        orderCase.setCarOwnerName(userName);


        orderCaseMapper.insert(orderCase);

        OrderCaseDetail detail = new OrderCaseDetail();
        detail.setUuid(UuidUtils.getUuid());
        detail.setOrderUuid(orderUuid);
        detail.setCaseUuid(caseUuid);
        detail.setCaseName(technicianCase.getTitle());
        detail.setSts(StsEnum.ACTIVE.getValue());
        detail.setCreatedBy(userName);
        detail.setCreatedTime(new Date());

        orderCaseDetailMapper.insert(detail);

        //新增order_info信息
        AddOrderInfoReq addOrderInfoReq = new AddOrderInfoReq();
        addOrderInfoReq.setOrderType(OrderTypeEnum.EXAMPLE.getValue());
        addOrderInfoReq.setOrderUuid(orderUuid);
        orderInfoService.addOrder(addOrderInfoReq);

        return ResultRes.success(orderUuid);
    }
}
