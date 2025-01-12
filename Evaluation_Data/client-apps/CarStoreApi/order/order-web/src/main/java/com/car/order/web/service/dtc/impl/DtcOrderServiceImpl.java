package com.car.order.web.service.dtc.impl;

import com.alibaba.fastjson.JSON;
import com.car.account.client.feign.StoreFegin;
import com.car.account.client.feign.StoreUserFeign;
import com.car.account.client.feign.TechnicianFegin;
import com.car.account.client.response.store.StoreDetailRes;
import com.car.account.client.response.store.StoreUserRes;
import com.car.account.client.response.technician.TechnicianRes;
import com.car.common.enums.OrderPrefixEnum;
import com.car.common.enums.ResEnum;
import com.car.common.enums.StsEnum;
import com.car.common.enums.UserTypeEnum;
import com.car.common.exception.BusinessException;
import com.car.common.res.PageRes;
import com.car.common.res.ResultRes;
import com.car.common.utils.*;
import com.car.common.utils.token.LoginToken;
import com.car.order.client.enums.consult.OrderTypeEnum;
import com.car.order.client.enums.goods.OrderStsEnum;
import com.car.order.client.enums.goods.PayMethodEnum;
import com.car.order.client.request.dtc.AddDtcOrderReq;
import com.car.order.client.request.dtc.QueryDtcOrderListReq;
import com.car.order.client.request.order.order.AddOrderInfoReq;
import com.car.order.client.response.dtc.QueryDtcInfoRes;
import com.car.order.client.response.dtc.QueryDtcOrderInfoRes;
import com.car.order.client.response.dtc.QueryDtcOrderListRes;
import com.car.order.client.response.order.goods.OrderGoodsListRes;
import com.car.order.web.common.constants.ConfigConsts;
import com.car.order.web.common.constants.Constants;
import com.car.order.web.dto.dtc.DtcIssuerInfoDto;
import com.car.order.web.mapper.dtc.DtcContentMapper;
import com.car.order.web.mapper.dtc.DtcMapper;
import com.car.order.web.mapper.dtc.DtcOrderDetailMapper;
import com.car.order.web.mapper.dtc.DtcOrderMapper;
import com.car.order.web.model.dtc.Dtc;
import com.car.order.web.model.dtc.DtcContent;
import com.car.order.web.model.dtc.DtcOrder;
import com.car.order.web.model.dtc.DtcOrderDetail;
import com.car.order.web.service.dtc.DtcOrderService;
import com.car.order.web.service.order.OrderInfoService;
import com.car.system.client.feign.SysUserFeign;
import com.car.system.client.response.user.UserDetailRes;
import com.codingapi.txlcn.tc.annotation.TxcTransaction;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.netflix.discovery.converters.Auto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Intellij IDEA.
 *
 * @author:  cjw
 * Date:  2021/2/17
 */
@Slf4j
@Service
public class DtcOrderServiceImpl implements DtcOrderService {

    @Autowired
    private DtcMapper dtcMapper;

    @Autowired
    private DtcContentMapper dtcContentMapper;

    @Autowired
    private DtcOrderMapper dtcOrderMapper;

    @Autowired
    private TechnicianFegin technicianFegin;

    @Autowired
    private StoreFegin storeFegin;

    @Autowired
    private StoreUserFeign storeUserFeign;

    @Autowired
    private SysUserFeign sysUserFeign;

    @Autowired
    private DtcOrderDetailMapper dtcOrderDetailMapper;

    @Autowired
    private OrderInfoService orderInfoService;

    @Autowired
    private ConfigConsts configConsts;


    /**
     * 根据id查询详情
     * @param uuid
     * @return
     */
    @Override
    public ResultRes<QueryDtcOrderInfoRes> getById(String uuid) {
        if (StringUtils.isEmpty(uuid)) {
            log.error("uuid不能为空");
            throw new BusinessException(ResEnum.LACK_PARAMETER);
        }
        QueryDtcOrderInfoRes res = dtcOrderMapper.getById(uuid);
        QueryDtcOrderInfoRes queryRes = new QueryDtcOrderInfoRes();
        //当前登录用户类型
        Integer userType = TokenHelper.getUserType();
        if (!StringUtils.isEmpty(userType)) {
            if (null != res) {
                //修改查看次数
                DtcOrder dtcOrder = new DtcOrder();
                dtcOrder.setUuid(uuid);
                Integer readCount = null == res.getReadCount() ? 0 : res.getReadCount();
                dtcOrder.setReadCount(readCount + 1);
                int updateDtcOrder = dtcOrderMapper.updateByPrimaryKeySelective(dtcOrder);
                if (updateDtcOrder <= 0) {
                    log.error("修改dtcOrder信息失败，修改订单为：{}", uuid);
                    throw new BusinessException(ResEnum.UPDATE_DB_ERROR);
                }
                //检测订单当前登订单查看次数
                queryRes = checkDtcOrder(res);
            }
        } else {
            BeanUtils.copyProperties(res, queryRes);
        }
        return ResultRes.success(queryRes);
    }

    /**
     * 新增DTC订单信息
     * @param req
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @TxcTransaction
    public ResultRes<String> addOrder(AddDtcOrderReq req) {
        //用户类型
        Integer userType = TokenHelper.getUserType();
        //用户uuid
        String userUuid = TokenHelper.getUserUuid();
        if (UserTypeEnum.store.getType().equals(userType)) {
            //当为店铺时，根据当前登录店铺联系人查询店铺uuid
            ResultRes<StoreUserRes> resResultRes = storeUserFeign.queryStoreUserInfo(userUuid);
            if (resResultRes.isSuccess()) {
                userUuid = resResultRes.getData().getStoreUuid();
            } else {
                log.error("根据当前登录联系人查询店铺uuid失败，请求uuid为：{}", userUuid);
                throw new BusinessException();
            }
        }
        //查询是否购买并且订单查看次数不大于3
        DtcOrder dtcOrder = dtcOrderMapper.queryPurchaseOrder(userUuid, req.getDtcUuid());
        if (null != dtcOrder) {
            log.error("已存在订单，无法创建");
            return ResultRes.success(dtcOrder.getUuid());
        }
        //查询Dtc故障信息
        Dtc dtc = queryDtcInfo(req.getDtcUuid());
        //新增dtc订单信息
        String dtcOrderUuid = insertDtcOrder(req, dtc);
        //新增dtc订单详情信息
        insertDtcOrderDetail(dtcOrderUuid, dtc);
        return ResultRes.success(dtcOrderUuid);
    }

    /**
     * 分页列表
     * @param req
     * @return
     */
    @Override
    public PageRes<List<QueryDtcOrderListRes>> list(QueryDtcOrderListReq req) {
        PageHelper.startPage(req.getPageNum(), req.getPageSize());

        //当前登录用户userUuid
        String userUuid = TokenHelper.getUserUuid();

        //当前登录用户类型
        Integer userType = TokenHelper.getUserType();
        List<QueryDtcOrderListRes> list = new ArrayList<>();
        if (null == userType) {
            //后台查询
            list = dtcOrderMapper.list(req, null);
        } else if (UserTypeEnum.technician.getType().equals(userType)) {
            //技师查询
            list = dtcOrderMapper.list(req, userUuid);
        } else if (UserTypeEnum.store.getType().equals(userType)) {
            //店铺查询
            //根据登录联系人信息查询店铺信息
            ResultRes<StoreUserRes> resResultRes = storeUserFeign.queryStoreUserInfo(userUuid);
            if (resResultRes.isSuccess()) {
                list = dtcOrderMapper.list(req, resResultRes.getData().getStoreUuid());
            }
        }
        PageInfo<QueryDtcOrderListRes> pageInfo = new PageInfo<>(list);
        return PageRes.success(list, pageInfo.getPageSize(), (int) pageInfo.getTotal(), pageInfo.getPages());
    }

    /**
     * 分页查询我的dtc订单列表
     * @param req
     * @return
     */
    @Override
    public PageRes<List<QueryDtcOrderListRes>> myList(QueryDtcOrderListReq req) {
        PageHelper.startPage(req.getPageNum(), req.getPageSize());

        //当前登录用户userUuid
        String userUuid = TokenHelper.getUserUuid();

        //当前登录用户类型
        Integer userType = TokenHelper.getUserType();
        List<QueryDtcOrderListRes> list = new ArrayList<>();
        if (null == userType) {
            //后台查询
            list = dtcOrderMapper.myList(req, null);
        } else if (UserTypeEnum.technician.getType().equals(userType)) {
            //技师查询
            list = dtcOrderMapper.myList(req, userUuid);
        } else if (UserTypeEnum.store.getType().equals(userType)) {
            //店铺查询
            //根据登录联系人信息查询店铺信息
            ResultRes<StoreUserRes> resResultRes = storeUserFeign.queryStoreUserInfo(userUuid);
            if (resResultRes.isSuccess()) {
                list = dtcOrderMapper.myList(req, resResultRes.getData().getStoreUuid());
            }
        }
        if (!CollectionUtils.isEmpty(list)) {
            for (QueryDtcOrderListRes res : list) {
                Integer readCount = null == res.getReadCount() ? 0 : res.getReadCount();
                res.setReadCount(configConsts.getDtcOrderNum() - readCount);
            }
        }
        PageInfo<QueryDtcOrderListRes> pageInfo = new PageInfo<>(list);
        return PageRes.success(list, pageInfo.getPageSize(), (int) pageInfo.getTotal(), pageInfo.getPages());
    }

    /**
     * DTC订单信息导出
     * @param exportReq
     * @param response
     */
    @Override
    public void exportOrderDtcList(QueryDtcOrderListReq exportReq, HttpServletResponse response) {
        log.debug("DTC订单信息导出");
        try {
            List<QueryDtcOrderListRes> list = dtcOrderMapper.list(exportReq, null);
            //读取模板文件
            InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream(Constants.ORDER_DTC_INFO_EXPORT_TEMPLATE);
            //设置空行默认属性
            List<QueryDtcOrderListRes> excelList = ExcelUtils.setFieldValue(list);
            Workbook wb = new XSSFWorkbook(resourceAsStream);
            Sheet sheet = wb.getSheetAt(0);
            //从第三行开始写入
            int firstRowIndex = sheet.getFirstRowNum()+2;
            for (int rowIndex = firstRowIndex; rowIndex < excelList.size()+2; rowIndex++) {
                //行样式
                Row rowStyle = (rowIndex % 2) == 0?sheet.getRow(2): sheet.getRow(3);
                //单列样式
                CellStyle cellStyle = ExcelUtils.getExcelFormat(rowStyle.getCell(1));
                CellStyle cellStyle1 = ExcelUtils.getExcelFormat(rowStyle.getCell(0));
                Row row = sheet.getRow(rowIndex);
                if(row == null){
                    row = sheet.createRow(rowIndex);
                }
                row.setHeight(rowStyle.getHeight());
                QueryDtcOrderListRes exportDto = excelList.get(rowIndex - 2);
                ExcelUtils.setCell(row,cellStyle1,0,rowIndex-1);
                ExcelUtils.setCell(row,cellStyle,1,exportDto.getBuyerName());
                ExcelUtils.setCell(row,cellStyle,2,exportDto.getBuyerMobile());
                ExcelUtils.setCell(row,cellStyle,3,exportDto.getDtcCode());

                ExcelUtils.setCell(row,cellStyle,4,exportDto.getCreatedTime());
                ExcelUtils.setCell(row,cellStyle,5,OrderStsEnum.enumOfDesc(exportDto.getOrderSts()));
                ExcelUtils.setCell(row,cellStyle,6,"¥ "+exportDto.getOrderAmount());
                ExcelUtils.setCell(row,cellStyle,7, "DTC查询");
                ExcelUtils.setCell(row,cellStyle,8, PayMethodEnum.enumOfDesc(exportDto.getPayType()));
            }
            ExcelUtils.responseWrite(wb,response, Constants.ORDER_DTC_INFO_EXPORT_TEMPLATE);
        } catch (Exception ex){
            log.error("商品订单信息导出异常，异常原因：{}", ExceptionUtils.stackTraceToString(ex));
        }
    }

    /**
     * 根据id查询订单状态
     * @param uuid
     * @return
     */
    @Override
    public ResultRes<Boolean> queryOrderSts(String uuid) {
        if (StringUtils.isEmpty(uuid)) {
            log.error("根据id查询订单状态，uuid不能为空：{}", uuid);
            throw new BusinessException(ResEnum.LACK_PARAMETER);
        }
        Boolean flag = false;
        DtcOrder dtcOrderSelect = new DtcOrder();
        dtcOrderSelect.setUuid(uuid);
        DtcOrder dtcOrder = dtcOrderMapper.selectOne(dtcOrderSelect);
        if (null == dtcOrder) {
            log.error("查询订单状态为空，查询uuid为：{}", uuid);
            throw new BusinessException(ResEnum.NON_EXISTENT.getValue());
        }
        if (OrderStsEnum.HAVE_PAID.getValue().equals(dtcOrder.getOrderSts())) {
            flag = true;
        }
        return ResultRes.success(flag);
    }


    /**
     * 根据dtc故障uuid查询相关信息
     * @param dtcUuid
     * @return
     */
    private Dtc queryDtcInfo (String dtcUuid) {
        Dtc dtcSelect = new Dtc();
        dtcSelect.setUuid(dtcUuid);
        dtcSelect.setSts(StsEnum.ACTIVE.getValue());
        Dtc dtc = dtcMapper.selectOne(dtcSelect);
        if (null == dtc) {
            log.error("查询dtc故障信息为空");
            throw new BusinessException(ResEnum.NON_EXISTENT);
        }
        return dtc;
    }

    /**
     * 查询发布人信息
     * @param dtcIssuerUuid
     * @param userType
     */
    private DtcIssuerInfoDto queryDtcIssuerInfo (String dtcIssuerUuid, Integer userType) {
        if (StringUtils.isEmpty(dtcIssuerUuid)) {
            log.error("发布人uuid不能为空");
            throw new BusinessException(ResEnum.LACK_PARAMETER);
        }
        DtcIssuerInfoDto dtcIssuerInfoDto = new DtcIssuerInfoDto();
        if (UserTypeEnum.technician.getType().equals(userType)) {
            //查询技师相关信息
            ResultRes<TechnicianRes> resResultRes = technicianFegin.queryTechnicianDetail(dtcIssuerUuid);
            if (resResultRes.isSuccess()) {
                TechnicianRes technicianRes = resResultRes.getData();
                dtcIssuerInfoDto.setDtcIssuerName(technicianRes.getUserName());
                dtcIssuerInfoDto.setDtcIssuerMobile(technicianRes.getMobile());
            }
        } else if (UserTypeEnum.store.getType().equals(userType)) {
            //查询店铺相关信息
            ResultRes<StoreDetailRes> resResultRes = storeFegin.queryStoreDetail(dtcIssuerUuid);
            if (resResultRes.isSuccess()) {
                StoreDetailRes storeDetailRes = resResultRes.getData();
                dtcIssuerInfoDto.setDtcIssuerName(storeDetailRes.getStoreName());
            }
        } else {
            //查询后台相关信息
            ResultRes<UserDetailRes> resResultRes = sysUserFeign.queryUserDetail(dtcIssuerUuid);
            if (resResultRes.isSuccess()) {
                UserDetailRes userDetailRes = resResultRes.getData();
                dtcIssuerInfoDto.setDtcIssuerName(userDetailRes.getName());
                dtcIssuerInfoDto.setDtcIssuerMobile(userDetailRes.getPhone());
            }
        }
        return dtcIssuerInfoDto;
    }

    /**
     * 新增订单信息
     * @param req
     * @param dtc
     * @return
     */
    private String insertDtcOrder (AddDtcOrderReq req, Dtc dtc) {
        //用户类型
        Integer userType = TokenHelper.getUserType();
        //用户uuid
        String userUuid = TokenHelper.getUserUuid();
        //店铺用户下单需判断是否为店铺,店铺用户需存储店铺ID
        if(UserTypeEnum.store.getType().equals(userType)){
            ResultRes<StoreUserRes> userResResultRes = storeUserFeign.queryStoreUserInfo(userUuid);
            if(StringUtils.isEmpty(userResResultRes) || !userResResultRes.isSuccess()){
                throw new BusinessException(ResEnum.NON_EXISTENT);
            }
            userUuid = userResResultRes.getData().getStoreUuid();
        }

        //用户user
        String userName = TokenHelper.getUserName();
        //用户手机号
        String mobile = null;
        LoginToken loginToken = TokenHelper.getLoginToken();
        if (null != loginToken) {
            mobile = loginToken.getUserMobile();
        }
        //查询发布人信息
        DtcIssuerInfoDto dtcIssuerInfoDto = queryDtcIssuerInfo(dtc.getDtcIssuerUuid(), userType);
        DtcOrder dtcOrderInsert = new DtcOrder();
        dtcOrderInsert.setUuid(UuidUtils.getUuid());
        dtcOrderInsert.setDtcUuid(dtc.getUuid());
        dtcOrderInsert.setDtcIssuerType(dtc.getDtcIssuerType());
        dtcOrderInsert.setOrderNum(OrderUtils.GenOrderNo(OrderPrefixEnum.DTC));
        dtcOrderInsert.setOrderAmount(dtc.getDtcAmount());
        dtcOrderInsert.setOrderSts(OrderStsEnum.UNPAID.getValue());
        dtcOrderInsert.setBuyerUuid(userUuid);
        dtcOrderInsert.setBuyerName(userName);
        dtcOrderInsert.setBuyerMobile(mobile);
        dtcOrderInsert.setIssuerUuid(dtc.getDtcIssuerUuid());
        dtcOrderInsert.setIssuerName(dtcIssuerInfoDto.getDtcIssuerName());
        dtcOrderInsert.setIssuerMobile(dtcIssuerInfoDto.getDtcIssuerMobile());
        dtcOrderInsert.setReadCount(0);
        dtcOrderInsert.setSts(StsEnum.ACTIVE.getValue());
        dtcOrderInsert.setCreatedBy(userName);
        dtcOrderInsert.setCreatedTime(new Date());
        int insertNum = dtcOrderMapper.insert(dtcOrderInsert);
        if (insertNum <= 0) {
            log.error("新增dtc订单失败，请求参数为：{}", JSON.toJSONString(dtcOrderInsert));
            throw new BusinessException(ResEnum.INSERT_DB_ERROR);
        }
        //新增order_info信息
        AddOrderInfoReq addOrderInfoReq = new AddOrderInfoReq();
        addOrderInfoReq.setOrderType(OrderTypeEnum.DTC.getValue());
        addOrderInfoReq.setOrderUuid(dtcOrderInsert.getUuid());
        orderInfoService.addOrder(addOrderInfoReq);
        return dtcOrderInsert.getUuid();
    }

    /**
     * 新增dtc订单详情信息
     * @param dtcOrderUuid
     * @param dtc
     * @return
     */
    private void insertDtcOrderDetail (String dtcOrderUuid, Dtc dtc) {
        //用户user
        String userName = TokenHelper.getUserName();
        //查询dtc详细信息
        DtcContent dtcContent = dtcContentMapper.queryDtcUuidInfo(dtc.getUuid());
        DtcOrderDetail dtcOrderDetail = new DtcOrderDetail();
        dtcOrderDetail.setUuid(UuidUtils.getUuid());
        dtcOrderDetail.setOrderUuid(dtcOrderUuid);
        dtcOrderDetail.setDtcCode(dtc.getDtcCode());
        dtcOrderDetail.setDtcBrandUuid(dtc.getDtcBrandUuid());
        dtcOrderDetail.setDtcDefinition(dtc.getDtcDefinition());
        dtcOrderDetail.setDtcExplain(dtcContent.getDtcExplain());
        dtcOrderDetail.setDtcReasons(dtcContent.getDtcReasons());
        dtcOrderDetail.setDtcDiagnose(dtcContent.getDtcDiagnose());
        dtcOrderDetail.setSts(StsEnum.ACTIVE.getValue());
        dtcOrderDetail.setCreatedBy(userName);
        dtcOrderDetail.setCreatedTime(new Date());
        int insertNum = dtcOrderDetailMapper.insert(dtcOrderDetail);
        if (insertNum <= 0) {
            log.error("新增dtc订单失败，请求参数为：{}", JSON.toJSONString(dtcOrderDetail));
            throw new BusinessException(ResEnum.INSERT_DB_ERROR);
        }
    }

    /**
     * 检测订单当前登订单查看次数
     * @param res
     */
    private QueryDtcOrderInfoRes checkDtcOrder (QueryDtcOrderInfoRes res) {
        QueryDtcOrderInfoRes queryRes = new QueryDtcOrderInfoRes();
        //查询订单详情
        QueryDtcOrderInfoRes queryDtcOrderInfoRes = dtcOrderMapper.getById(res.getUuid());
        if (configConsts.getDtcOrderNum() <= queryDtcOrderInfoRes.getReadCount()) {
            queryRes.setIsPay(false);

        } else {
            res.setIsPay(true);
            BeanUtils.copyProperties(res, queryRes);
        }
        return queryRes;
    }

}
