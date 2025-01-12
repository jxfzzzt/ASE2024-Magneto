package com.car.order.web.service.sence.impl;

import com.alibaba.fastjson.JSON;
import com.car.account.client.feign.StoreFegin;
import com.car.account.client.feign.StoreUserFeign;
import com.car.account.client.feign.TechnicianFegin;
import com.car.account.client.feign.VehicleFegin;
import com.car.account.client.request.technician.UpdateTechnicianAccountReq;
import com.car.account.client.response.store.StoreDetailRes;
import com.car.account.client.response.store.StoreUserRes;
import com.car.account.client.response.technician.TechnicianRes;
import com.car.account.client.response.vehicle.config.ConfigRes;
import com.car.account.client.response.vehicle.vehicleUser.VehicleUserRes;
import com.car.common.enums.OrderPrefixEnum;
import com.car.common.enums.ResEnum;
import com.car.common.enums.StsEnum;
import com.car.common.enums.UserTypeEnum;
import com.car.common.exception.BusinessException;
import com.car.common.res.PageRes;
import com.car.common.res.ResultRes;
import com.car.common.utils.OrderUtils;
import com.car.common.utils.TokenHelper;
import com.car.common.utils.UuidUtils;
import com.car.common.utils.token.LoginToken;
import com.car.order.client.enums.consult.OrderTypeEnum;
import com.car.order.client.enums.goods.OrderStsEnum;
import com.car.order.client.enums.goods.SceneOrderStsEnum;
import com.car.order.client.enums.order.DoorOrServiceEnum;
import com.car.order.client.enums.order.ImageTypeEnum;
import com.car.order.client.enums.scene.GrabOrdersStsEnum;
import com.car.order.client.request.order.order.AddOrderInfoReq;
import com.car.order.client.request.order.order.ConfirmOrderReq;
import com.car.order.client.request.scene.*;
import com.car.order.client.response.scene.QuerySceneOrderInfoRes;
import com.car.order.client.response.scene.QuerySceneOrderListRes;
import com.car.order.web.dto.LaAndLoDto;
import com.car.order.web.dto.scene.SceneOrderDto;
import com.car.order.web.mapper.scene.SceneOrderDtcImagesMapper;
import com.car.order.web.mapper.scene.SceneOrderMapper;
import com.car.order.web.mapper.scene.SceneOrderServicesMapper;
import com.car.order.web.mapper.scene.SceneOrderTechnicianMapper;
import com.car.order.web.model.scene.SceneOrder;
import com.car.order.web.model.scene.SceneOrderDtcImages;
import com.car.order.web.model.scene.SceneOrderServices;
import com.car.order.web.model.scene.SceneOrderTechnician;
import com.car.order.web.service.order.OrderInfoService;
import com.car.order.web.service.sence.SceneOrderService;
import com.car.system.client.feign.SystemFeign;
import com.car.system.client.response.dict.DictionaryRes;
import com.codingapi.txlcn.tc.annotation.TxcTransaction;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * @author cjw
 */
@Slf4j
@Service
public class SceneOrderServiceImpl implements SceneOrderService {

    @Autowired
    private SceneOrderMapper sceneOrderMapper;

    @Autowired
    private SystemFeign systemFeign;

    @Autowired
    private VehicleFegin vehicleFegin;

    @Autowired
    private SceneOrderDtcImagesMapper sceneOrderDtcImagesMapper;

    @Autowired
    private OrderInfoService orderInfoService;

    @Autowired
    private TechnicianFegin technicianFegin;

    @Autowired
    private StoreUserFeign storeUserFeign;

    @Autowired
    private StoreFegin storeFegin;
    @Autowired
    private SceneOrderTechnicianMapper sceneOrderTechnicianMapper;

    @Autowired
    private SceneOrderServicesMapper sceneOrderServicesMapper;

    /**
     *新增现场订单
     * @param req
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @TxcTransaction
    public ResultRes<String> addSceneOrder(AddSceneOrderReq req) {
        if (null == req) {
            log.error("新增现场订单请求参数为空：{}", JSON.toJSONString(req));
            throw new BusinessException(ResEnum.LACK_PARAMETER);
        }
        //查询当前登录用户uuid
        String userUuid = TokenHelper.getUserUuid();
        //查询当前登录用户名称
        String userName = TokenHelper.getUserName();
        //获取当前登录用户手机号
        String mobile = null;
        LoginToken loginToken = TokenHelper.getLoginToken();
        if (null != loginToken) {
            mobile = loginToken.getUserMobile();
        }
        //新增现场订单
        String sceneOrderUuid = insertSceneOrder(req, userName, mobile, userUuid);

        //新增dtc图片
        if (!CollectionUtils.isEmpty(req.getDtcImageList())) {
            insertSceneOrderDtcImg(req.getDtcImageList(), userName, sceneOrderUuid, ImageTypeEnum.DTC.getValue());
        }
        //新增故障描述图片
        if (!CollectionUtils.isEmpty(req.getDtcImageList())) {
            insertSceneOrderDtcImg(req.getFaultDescImageList(), userName, sceneOrderUuid,ImageTypeEnum.FAULT_DESC.getValue());
        }

        return ResultRes.success(sceneOrderUuid);
    }


    /**
     *现场订单-描述
     * @param req
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @TxcTransaction
    public ResultRes<String> sceneOrderDescribe(SceneOrderDescribeReq req) {
        //获取当前登录用户uuid
        String userUuid = TokenHelper.getUserUuid();
        //获取当前登录用户名称
        String userName = TokenHelper.getUserName();
        //获取当前登录用户手机号
        String phone = null;
        LoginToken loginToken = TokenHelper.getLoginToken();
        if (null != loginToken) {
            phone = loginToken.getUserMobile();
        }
        SceneOrderTechnician sceneOrderTechnician = new SceneOrderTechnician();
        sceneOrderTechnician.setDesc(req.getDescribe());
        sceneOrderTechnician.setOrderUuid(req.getSceneOrderUuid());
        sceneOrderTechnician.setTechnicianMobile(phone);
        sceneOrderTechnician.setTechnicianUuid(userUuid);
        sceneOrderTechnician.setTechnicianName(userName);
        sceneOrderTechnicianMapper.insert(sceneOrderTechnician);
        SceneOrder sceneOrder = new SceneOrder();
        sceneOrder.setUuid(req.getSceneOrderUuid());
        SceneOrder sceneOrderSelect = sceneOrderMapper.selectOne(sceneOrder);
        if (null == sceneOrderSelect) {

            log.error("未查询到相关信息，订单uuid为：{}", req.getSceneOrderUuid());
            throw new BusinessException(ResEnum.NON_EXISTENT);
        }

        if (req.getType().equals(DoorOrServiceEnum.SHOW_UP.getValue())){
            sceneOrder.setInDoorUuid(sceneOrderTechnician.getUuid());
            insertSceneOrderDtcImg(req.getImageList(), userName, sceneOrderTechnician.getUuid(),ImageTypeEnum.SHOW_UP.getValue());
        }else if(req.getType().equals(DoorOrServiceEnum.SERVICE_END.getValue()))
        {
            sceneOrder.setServiceEndUuId(sceneOrderTechnician.getUuid());
            insertSceneOrderDtcImg(req.getImageList(), userName, sceneOrderTechnician.getUuid(),ImageTypeEnum.SERVICE_END.getValue());
        }
        sceneOrderMapper.updateByPrimaryKeySelective(sceneOrder);
        return  ResultRes.success(sceneOrder.getUuid());
    }

    /**
     * 现场订单--客户确认
     * @param req
     * @return
     */
    @Override
    public ResultRes<String> sceneOrderConfirm(SceneOrderConfirmReq req){
        SceneOrder sceneOrder = new SceneOrder();
        sceneOrder.setUuid(req.getSceneOrderUuid());
        SceneOrder sceneOrderSelect = sceneOrderMapper.selectOne(sceneOrder);
        if (null == sceneOrderSelect) {
            log.error("未查询到相关信息，订单uuid为：{}", req.getSceneOrderUuid());
            throw new BusinessException(ResEnum.NON_EXISTENT);
        }
        if (req.getType().equals(DoorOrServiceEnum.SHOW_UP.getValue())){
            if(StringUtils.isEmpty(sceneOrderSelect.getInDoorUuid())){
                log.error("技师未上门：{}", req.getSceneOrderUuid());
                throw new BusinessException("9999","技师未上门!");
            }
            sceneOrderSelect.setOrderSts(SceneOrderStsEnum.SUBMIT_PLAN.getValue());
        }else if(req.getType().equals(DoorOrServiceEnum.SERVICE_END.getValue()))
        {
            if(StringUtils.isEmpty(sceneOrderSelect.getServiceEndUuId())){
                log.error("技师未完成服务：{}", req.getSceneOrderUuid());
                throw new BusinessException("9999","技师未完成服务!");
            }
            sceneOrderSelect.setOrderSts(SceneOrderStsEnum.SERVICE_END.getValue());
        }
        sceneOrderMapper.updateByPrimaryKeySelective(sceneOrder);
        return ResultRes.success(req.getSceneOrderUuid());
    }


    /**
     * 提交方案
     * @param req
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @TxcTransaction
    public ResultRes<String> sceneSubmitPlan(AddSceneOrderServiceReq req){

        SceneOrder sceneOrder = new SceneOrder();
        sceneOrder.setUuid(req.getSceneOrderUuid());
        SceneOrder sceneOrderSelect = sceneOrderMapper.selectOne(sceneOrder);
        if (null == sceneOrderSelect) {
            log.error("未查询到相关信息，订单uuid为：{}", req.getSceneOrderUuid());
            throw new BusinessException(ResEnum.NON_EXISTENT);
        }
        if ( !sceneOrderSelect.getOrderSts().equals(SceneOrderStsEnum.SUBMIT_PLAN.getValue())) {
            log.error("订单状态异常，订单uuid为：{}", req.getSceneOrderUuid());
            throw new BusinessException("9999","订单状态异常!");
        }

        sceneOrder.setOrderSts(SceneOrderStsEnum.WAIT_PAYMENT.getValue());
        sceneOrderMapper.updateByPrimaryKeySelective(sceneOrder);

        SceneOrderServices sceneOrderServices = new SceneOrderServices();
        BeanUtils.copyProperties(req, sceneOrderServices);
        //基本检查费用
        BigDecimal basicInspectAmount = new BigDecimal(StringUtils.isEmpty(queryDictName(req.getBasicInspectAmountUuid())) ? BigDecimal.ZERO.toString() : queryDictName(req.getBasicInspectAmountUuid()));
        //相关线路检查费用
        BigDecimal lineInspectAmount = new BigDecimal(StringUtils.isEmpty(queryDictName(req.getLineInspectAmountUuid())) ? BigDecimal.ZERO.toString() : queryDictName(req.getLineInspectAmountUuid()));
        //诊断仪使用费
        BigDecimal diagnosisInstrumentAmount = new BigDecimal(StringUtils.isEmpty(queryDictName(req.getDiagnosisInstrumentAmountUuid())) ? BigDecimal.ZERO.toString() : queryDictName(req.getDiagnosisInstrumentAmountUuid()));
        //相关线路检查费用
        BigDecimal carSheetMetalAmount = new BigDecimal(StringUtils.isEmpty(queryDictName(req.getCarSheetMetalAmountUuid())) ? BigDecimal.ZERO.toString() : queryDictName(req.getCarSheetMetalAmountUuid()));
        //车辆油漆修复费用
        BigDecimal carPaintRepairAmount = new BigDecimal(StringUtils.isEmpty(queryDictName(req.getCarPaintRepairAmountUuid())) ? BigDecimal.ZERO.toString() : queryDictName(req.getCarPaintRepairAmountUuid()));
        //其他费用费
        BigDecimal otherAmount = new BigDecimal(StringUtils.isEmpty(queryDictName(req.getOtherAmountUuid())) ? BigDecimal.ZERO.toString() : queryDictName(req.getOtherAmountUuid()));
        //平台订单服务费
        BigDecimal orderServiceAmount = new BigDecimal(StringUtils.isEmpty(queryDictName(req.getOrderServiceAmountUuid())) ? BigDecimal.ZERO.toString() : queryDictName(req.getOrderServiceAmountUuid()));
        sceneOrderServices.setOrderNum(sceneOrderSelect.getOrderNum());
        sceneOrderServices.setBasicInspectAmount(basicInspectAmount);
        sceneOrderServices.setLineInspectAmount(lineInspectAmount);
        sceneOrderServices.setDiagnosisInstrumentAmount(diagnosisInstrumentAmount);
        sceneOrderServices.setCarSheetMetalAmount(carSheetMetalAmount);
        sceneOrderServices.setCarPaintRepairAmount(carPaintRepairAmount);
        sceneOrderServices.setOtherAmount(otherAmount);
        sceneOrderServices.setOrderServiceAmount(orderServiceAmount);
        sceneOrderServices.setOrderSts(OrderStsEnum.UNPAID.getValue());
        int insertNum = sceneOrderServicesMapper.insert(sceneOrderServices);
        if (insertNum <= 0) {
            log.error("新增现场订单失败，新增参数为：{}", JSON.toJSONString(req));
            throw new BusinessException(ResEnum.INSERT_DB_ERROR);
        }
        AddOrderInfoReq addOrderInfoReq = new AddOrderInfoReq();
        addOrderInfoReq.setUuid(UuidUtils.getUuid());
        addOrderInfoReq.setOrderType(OrderTypeEnum.SCENE_SERVICE.getValue());
        addOrderInfoReq.setOrderUuid(sceneOrderServices.getUuid());
        orderInfoService.addOrder(addOrderInfoReq);

        return  ResultRes.success(sceneOrderServices.getUuid());
    }

    /**
     * 查询现场订单列表
     * @param req
     * @return
     */
    @Override
    public PageRes<List<QuerySceneOrderListRes>> querySceneOrderList(QuerySceneOrderListReq req) {
        PageHelper.startPage(req.getPageNum(), req.getPageSize());
        //获取当前登录用户信息
        String userUuid = TokenHelper.getUserUuid();
        //查询登录人经纬度
        LaAndLoDto laAndLoDto = queryUserLaAndLo(userUuid);
        List<QuerySceneOrderListRes> querySceneOrderListResList = new ArrayList<>();
        if (GrabOrdersStsEnum.not_grab.getValue().equals(req.getQueryType())) {
            //查询未抢现场订单
            querySceneOrderListResList = sceneOrderMapper.querySceneOrderList(req, null, GrabOrdersStsEnum.not_grab.getValue(), null, laAndLoDto, OrderStsEnum.HAVE_PAID.getValue());
        } else if (GrabOrdersStsEnum.grab.getValue().equals(req.getQueryType())) {
            //查询用户已抢订单
            querySceneOrderListResList = sceneOrderMapper.querySceneOrderList(req, null, GrabOrdersStsEnum.grab.getValue(), userUuid, laAndLoDto, null);
        } else if (GrabOrdersStsEnum.RELEASE.getValue().equals(req.getQueryType())) {
            //查询用户发布订单
            querySceneOrderListResList = sceneOrderMapper.querySceneOrderList(req, userUuid, null, null, laAndLoDto, null);
        } else {
            //查询全部
            querySceneOrderListResList = sceneOrderMapper.querySceneOrderList(req, null, null, null, laAndLoDto, null);
        }

        PageInfo<QuerySceneOrderListRes> pageInfo = new PageInfo<>(querySceneOrderListResList);
        List<QuerySceneOrderListRes> resList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(querySceneOrderListResList)) {
            for (QuerySceneOrderListRes res : querySceneOrderListResList) {
                Boolean flag = false;
                if (userUuid.equals(res.getIssuerUuid())) {
                    flag = true;
                }
                res.setDistance(res.getDistance() / 1000);
                res.setIsOneself(flag);
                resList.add(res);
            }
        }
        return PageRes.success(resList, pageInfo.getPageSize(), (int) pageInfo.getTotal(), pageInfo.getPages());
    }

    /**
     * 查看现场订单详情
     * @param uuid
     * @return
     */
    @Override
    public ResultRes<QuerySceneOrderInfoRes> querySceneOrderInfo(String uuid) {
        if (StringUtils.isEmpty(uuid)) {
            log.info("查询现场的订单详情uuid不能为空");
            throw new BusinessException(ResEnum.LACK_PARAMETER);
        }
        //获取当前登录人uuid
        String userUuid = TokenHelper.getUserUuid();
        QuerySceneOrderInfoRes res = new QuerySceneOrderInfoRes();
        SceneOrderDto sceneOrder = sceneOrderMapper.querySceneOrderInfo(uuid);
        //现场下单dtc图片
        List<String> dtcImageList = new ArrayList<>();
        //现场订单技师相关dtc图片
        List<String> technicianDtcImgList = new ArrayList<>();
        Boolean flag = false;
        if (null != sceneOrder) {
            BeanUtils.copyProperties(sceneOrder, res);
            //变速器一级
            if (!StringUtils.isEmpty(sceneOrder.getTransmissionOneLevelUuid())) {
                res.setTransmissionOneLevel(queryDictName(sceneOrder.getTransmissionOneLevelUuid()));
            }
            //变速器二级
            if (!StringUtils.isEmpty(sceneOrder.getTransmissionTwoLevelUuid())) {
                res.setTransmissionTwoLevel(queryDictName(sceneOrder.getTransmissionTwoLevelUuid()));
            }
            //发动机排量
            if (!StringUtils.isEmpty(sceneOrder.getEngineDisplacementUuid())) {
                res.setEngineDisplacement(queryDictName(sceneOrder.getEngineDisplacementUuid()));
            }
            //驱动方式
            if (!StringUtils.isEmpty(sceneOrder.getDrivingModeUuid())) {
                res.setDrivingMode(queryDictName(sceneOrder.getDrivingModeUuid()));
            }
            //增压系统
            if (!StringUtils.isEmpty(sceneOrder.getBoosterSystemUuid())) {
                res.setBoosterSystem(queryDictName(sceneOrder.getBoosterSystemUuid()));
            }
            //维修类型
            if (!StringUtils.isEmpty(sceneOrder.getRepairTypeUuid())) {
                res.setRepairType(queryDictName(sceneOrder.getRepairTypeUuid()));
            }
            //查询现场下单订单dtc图片
            dtcImageList = sceneOrderDtcImagesMapper.queryList(sceneOrder.getUuid());
            //查询现场下单订单dtc图片
            technicianDtcImgList = sceneOrderDtcImagesMapper.queryList(sceneOrder.getUuid());
            if (userUuid.equals(sceneOrder.getIssuerUuid())) {
                flag = true;
            }
        }
        res.setIsOneself(flag);
        res.setDtcImageList(dtcImageList);
        res.setTechnicianDtcImgList(technicianDtcImgList);
        return ResultRes.success(res);
    }

    /**
     * 现场订单抢单
     * @param sceneOrderUuid
     * @return
     */
    @Override
    public ResultRes<String> grabbingOrders(String sceneOrderUuid) {
        if (StringUtils.isEmpty(sceneOrderUuid)) {
            log.error("现场订单抢单时，订单uuid不能为空");
            throw new BusinessException(ResEnum.LACK_PARAMETER);
        }
        //获取当前登录用户uuid
        String userUuid = TokenHelper.getUserUuid();
        //获取当前登录用户名称
        String userName = TokenHelper.getUserName();
        LoginToken loginToken = TokenHelper.getLoginToken();
        //获取当前登录用户手机号
        String phone = null;
        if (null != loginToken) {
            phone = loginToken.getUserMobile();
        }
        //查询现场订单详情
        SceneOrder querySceneOrder = new SceneOrder();
        querySceneOrder.setUuid(sceneOrderUuid);
        SceneOrder sceneOrderSelect = sceneOrderMapper.selectOne(querySceneOrder);
        if (null != sceneOrderSelect) {
            if (userUuid.equals(sceneOrderSelect.getIssuerUuid())) {
                log.error("此订单为本人订单，禁止抢单，订单uuid为：{}", sceneOrderUuid);
                throw new BusinessException(ResEnum.SCENE_ORDER_ONESELF_ERROR);
            }
        } else {
            log.error("未查询到相关信息，订单uuid为：{}", sceneOrderUuid);
            throw new BusinessException(ResEnum.NON_EXISTENT);
        }
        if(sceneOrderSelect.getGrabbingOrdersSts().equals(GrabOrdersStsEnum.grab.getValue())){
            log.error("该订单已经被抢，订单uuid为：{}", sceneOrderUuid);
            throw new BusinessException("9999","该订单已经被抢！");
        }
        SceneOrder sceneOrder = new SceneOrder();
        sceneOrder.setUuid(sceneOrderUuid);
        sceneOrder.setBuyerUuid(userUuid);
        sceneOrder.setBuyerName(userName);
        sceneOrder.setBuyerMobile(phone);
        sceneOrder.setGrabbingOrdersSts(GrabOrdersStsEnum.grab.getValue());
        sceneOrder.setOrderSts(SceneOrderStsEnum.UNPAID.getValue());
        sceneOrder.setLastUpdatedBy(userName);
        sceneOrder.setLastUpdatedTime(new Date());
        sceneOrder.setConfirmType(TokenHelper.getUserType());

        AddOrderInfoReq addOrderInfoReq = new AddOrderInfoReq();
        addOrderInfoReq.setUuid(UuidUtils.getUuid());
        addOrderInfoReq.setOrderType(OrderTypeEnum.SCENE.getValue());
        addOrderInfoReq.setOrderUuid(sceneOrder.getUuid());
        orderInfoService.addOrder(addOrderInfoReq);
        int updateNum = sceneOrderMapper.updateByPrimaryKeySelective(sceneOrder);
        if (updateNum <= 0 ) {
            log.error("抢单失败，请求参数为：{}", JSON.toJSONString(sceneOrder));
            throw new BusinessException(ResEnum.UPDATE_DB_ERROR);
        }
        return ResultRes.success(sceneOrderUuid);
    }

    /**
     * 新增现场订单
     * @param req
     * @return
     */
    private String insertSceneOrder (AddSceneOrderReq req, String userName, String mobile, String userUuid) {
        //查询当前登录人经纬度
        LaAndLoDto laAndLoDto = queryUserLaAndLo(userUuid);
        SceneOrder sceneOrder = new SceneOrder();
        BeanUtils.copyProperties(req, sceneOrder);
        sceneOrder.setUuid(UuidUtils.getUuid());
        sceneOrder.setOrderNum(OrderUtils.GenOrderNo(OrderPrefixEnum.XC));
        sceneOrder.setBrandName(queryVehicleConfig(req.getBrandUuid()));
        sceneOrder.setCarModelName(queryVehicleConfig(req.getCarModelUuid()));

        sceneOrder.setIssuerUuid(userUuid);
        sceneOrder.setIssuerName(userName);
        sceneOrder.setIssuerMobile(mobile);
        sceneOrder.setLatitude(laAndLoDto.getLatitude());
        sceneOrder.setLongitude(laAndLoDto.getLongitude());
        sceneOrder.setGrabbingOrdersSts(GrabOrdersStsEnum.not_grab.getValue());
        sceneOrder.setSts(StsEnum.ACTIVE.getValue());
        sceneOrder.setOrderSts(SceneOrderStsEnum.HAVE_PAID.getValue());
        sceneOrder.setCreatedBy(userName);
        sceneOrder.setCreatedTime(new Date());

        int insertNum = sceneOrderMapper.insert(sceneOrder);
        if (insertNum <= 0) {
            log.error("新增现场订单失败，新增参数为：{}", JSON.toJSONString(sceneOrder));
            throw new BusinessException(ResEnum.INSERT_DB_ERROR);
        }
        return sceneOrder.getUuid();
    }

    /**
     * 查询当前登录人经纬度信息
     * @param userUuid
     * @return
     */
    private LaAndLoDto queryUserLaAndLo (String userUuid) {
        //当前登录用户用户类型
        Integer userType = TokenHelper.getUserType();
        LaAndLoDto laAndLoDto = new LaAndLoDto();
        if (UserTypeEnum.vehicle.getType().equals(userType)) {
            //查询车主信息
            ResultRes<VehicleUserRes> resResultRes = vehicleFegin.queryDetail(userUuid);
            if (resResultRes.isSuccess()) {
                if (null != resResultRes.getData()) {
                    laAndLoDto.setLongitude(resResultRes.getData().getLongitude());
                    laAndLoDto.setLatitude(resResultRes.getData().getLatitude());
                }
            }
        } else if (UserTypeEnum.technician.getType().equals(userType)) {
            //查询技师信息
            ResultRes<TechnicianRes> resResultRes = technicianFegin.queryTechnicianDetail(userUuid);
            if (resResultRes.isSuccess()) {
                if (null != resResultRes.getData()) {
                    if (null != resResultRes.getData().getAddressLatitude()) {
                        laAndLoDto.setLatitude(resResultRes.getData().getAddressLatitude());
                    }
                    if (null != resResultRes.getData().getAddressLongitude()) {
                        laAndLoDto.setLongitude(resResultRes.getData().getAddressLongitude());
                    }


                }
            }
        } else if (UserTypeEnum.store.getType().equals(userType)) {
            //查询店铺联系人信息
            ResultRes<StoreUserRes> resResultRes = storeUserFeign.queryStoreUserInfo(userUuid);
            if (resResultRes.isSuccess()) {
                if (null != resResultRes.getData()) {
                    //查询店铺信息
                    ResultRes<StoreDetailRes> storeDetailResResultRes = storeFegin.queryStoreDetail(resResultRes.getData().getStoreUuid());
                    if (storeDetailResResultRes.isSuccess()) {
                        if (null != storeDetailResResultRes.getData()) {
                            if (null != storeDetailResResultRes.getData().getLongitude()) {
                                laAndLoDto.setLongitude(storeDetailResResultRes.getData().getLongitude());
                            }
                            if (null != storeDetailResResultRes.getData().getLatitude()) {
                                laAndLoDto.setLatitude(storeDetailResResultRes.getData().getLatitude());
                            }

                        }
                    }
                }
            }
        }
        return laAndLoDto;
    }

    /**
     * 根据字典uuid，查询字典名称
     * @return
     */
    private String queryDictName (String dictUuid) {
        String dictName = null;
        if (StringUtils.isEmpty(dictUuid)) {
            return dictName;
        }
        //根据字典uuid查询字典信息
        ResultRes<DictionaryRes> resResultRes = systemFeign.queryByUuid(dictUuid);
        if (resResultRes.isSuccess()) {
            if (null != resResultRes.getData()) {
                dictName = resResultRes.getData().getLableDesc();
            }
        }
        return dictName;
    }


    /**
     * 根据uuid查询车辆类型/品牌/型号配置表配置名称
     * @param vehicleConfigUuid
     * @return
     */
    private String queryVehicleConfig(String vehicleConfigUuid){
        String vehicleConfigName = null;
        if (StringUtils.isEmpty(vehicleConfigUuid)) {
            return vehicleConfigName;
        }
        ResultRes<ConfigRes> resResultRes = vehicleFegin.queryConfig(vehicleConfigUuid);
        if (resResultRes.isSuccess()) {
            if (null != resResultRes.getData()) {
                vehicleConfigName = resResultRes.getData().getConfigName();
            }
        }
        return vehicleConfigName;
    }

    /**
     * dtc图片新增
     * @param dtcImageList
     */
    @Override
    public void insertSceneOrderDtcImg (List<String> dtcImageList, String userName, String relationUuid,Integer type) {
        for (String dtcImgUrl : dtcImageList) {
            SceneOrderDtcImages sceneOrderDtcImages = new SceneOrderDtcImages();
            sceneOrderDtcImages.setUuid(UuidUtils.getUuid());
            sceneOrderDtcImages.setRelationUuid(relationUuid);
            sceneOrderDtcImages.setDtcImageUrl(dtcImgUrl);
            sceneOrderDtcImages.setType(type);
            sceneOrderDtcImages.setSts(StsEnum.ACTIVE.getValue());
            sceneOrderDtcImages.setCreatedBy(userName);
            sceneOrderDtcImages.setCreatedTime(new Date());
            int insertNum = sceneOrderDtcImagesMapper.insert(sceneOrderDtcImages);
            if (insertNum <= 0) {
                log.info("新增现场订单dtc图片失败，请求参数为：{}", JSON.toJSONString(sceneOrderDtcImages));
                throw new BusinessException(ResEnum.INSERT_DB_ERROR);
            }
        }
    }

    /**
     * 完成现场订单
     * @param sceneOrderUuid
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @TxcTransaction
    public ResultRes<String> completeOrder(String sceneOrderUuid) {
        if (StringUtils.isEmpty(sceneOrderUuid)) {
            log.error("完成现场订单时订单uuid不能为空");
            throw new BusinessException(ResEnum.LACK_PARAMETER);
        }
        ConfirmOrderReq confirmOrderReq = new ConfirmOrderReq();
        confirmOrderReq.setOrderType(OrderTypeEnum.SCENE.getValue());
        confirmOrderReq.setOrderUuid(sceneOrderUuid);
        orderInfoService.confirmOrder(confirmOrderReq);

       /* SceneOrder sceneOrderUpdate = new SceneOrder();
        sceneOrderUpdate.setUuid(sceneOrderUuid);
        sceneOrderUpdate.setOrderSts(OrderStsEnum.COMPLETED.getValue());
        Integer updateNum = sceneOrderMapper.updateByPrimaryKeySelective(sceneOrderUpdate);
        if (updateNum <= 0) {
            log.error("修改订单完成状态失败，请求uuid为：{}", sceneOrderUuid);
            throw new BusinessException(ResEnum.UPDATE_DB_ERROR);
        }
        //查询现场下单订单信息
        SceneOrder sceneOrderSelect = new SceneOrder();
        sceneOrderSelect.setUuid(sceneOrderUuid);
        sceneOrderSelect.setSts(StsEnum.ACTIVE.getValue());
        SceneOrder sceneOrder = sceneOrderMapper.selectOne(sceneOrderSelect);
        if (null != sceneOrder) {
            if (!StringUtils.isEmpty(sceneOrder.getBuyerUuid())) {
                //修改技师账户信息
                this.updateTechnicianAccount(sceneOrder.getBuyerUuid(), sceneOrder.getTotalAmount());
            }
        }*/


        return ResultRes.success(sceneOrderUuid);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @TxcTransaction
    public ResultRes<String> cancelOrderOrder(String sceneOrderUuid){
        return ResultRes.success(sceneOrderUuid);
    }

    /**
     * 修改技师账户信息
     * @param storeUuid
     * @param orderAccount
     */
    private void updateTechnicianAccount (String storeUuid, BigDecimal orderAccount) {
        UpdateTechnicianAccountReq req = new UpdateTechnicianAccountReq();
        req.setTechnicianUuid(storeUuid);
        req.setOrderAmount(orderAccount);
        ResultRes<String> resultRes = technicianFegin.updateTechnicianAccount(req);
        if (!resultRes.isSuccess()) {
            log.error("修改技师账户信息，请求参数为：{}", JSON.toJSONString(req));
            throw new BusinessException(ResEnum.UPDATE_DB_ERROR);
        }
    }
}
