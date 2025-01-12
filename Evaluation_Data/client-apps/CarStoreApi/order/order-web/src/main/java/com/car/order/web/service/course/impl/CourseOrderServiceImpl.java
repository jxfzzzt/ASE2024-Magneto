package com.car.order.web.service.course.impl;

import com.alibaba.fastjson.JSON;
import com.car.account.client.feign.StoreUserFeign;
import com.car.account.client.response.store.StoreUserRes;
import com.car.common.enums.*;
import com.car.common.exception.BusinessException;
import com.car.common.res.PageRes;
import com.car.common.res.ResultRes;
import com.car.common.utils.*;
import com.car.common.utils.token.LoginToken;
import com.car.order.client.enums.consult.OrderTypeEnum;
import com.car.order.client.enums.course.CourseTypeEnum;
import com.car.order.client.enums.course.PurchaseTypeEnum;
import com.car.order.client.enums.goods.OrderStsEnum;
import com.car.order.client.enums.goods.PayMethodEnum;
import com.car.order.client.request.course.AddCourseOrderReq;
import com.car.order.client.request.course.QueryCourseOrderListReq;
import com.car.order.client.request.order.order.AddOrderInfoReq;
import com.car.order.client.response.course.QueryCourseInfoRes;
import com.car.order.client.response.course.QueryCourseOrderInfoRes;
import com.car.order.client.response.course.QueryCourseOrderListRes;
import com.car.order.web.common.constants.Constants;
import com.car.order.web.mapper.course.CourseMapper;
import com.car.order.web.mapper.course.CourseOrderDetailMapper;
import com.car.order.web.mapper.course.CourseOrderMapper;
import com.car.order.web.model.course.CourseOrder;
import com.car.order.web.model.course.CourseOrderDetail;
import com.car.order.web.service.course.CourseOrderService;
import com.car.order.web.service.order.OrderInfoService;
import com.codingapi.txlcn.tc.annotation.TxcTransaction;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jdk.nashorn.internal.parser.Token;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by Intellij IDEA.
 *
 * @author:  cjw
 * Date:  2021/2/21
 */
@Slf4j
@Service
public class CourseOrderServiceImpl implements CourseOrderService {

    @Autowired
    private CourseOrderMapper courseOrderMapper;

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private StoreUserFeign storeUserFeign;

    @Autowired
    private CourseOrderDetailMapper courseOrderDetailMapper;

    @Autowired
    private OrderInfoService orderInfoService;

    /**
     * 根据id查询课程订单详情
     * @param uuid
     * @return
     */
    @Override
    public ResultRes<QueryCourseOrderInfoRes> getById(String uuid) {
        if (StringUtils.isEmpty(uuid)) {
            log.error("查询课程订单详情uuid不能为空");
            throw new BusinessException(ResEnum.LACK_PARAMETER);
        }
        QueryCourseOrderInfoRes res = courseOrderMapper.getById(uuid);
        return ResultRes.success(res);
    }

    /**
     * 新增课程订单信息
     * @param req
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @TxcTransaction
    public ResultRes<String> add(AddCourseOrderReq req) {
        //当前用户人用户名
        String userName = TokenHelper.getUserName();
        //当前用户人uuid
        String userUuid = TokenHelper.getUserUuid();
        //查询课程信息详情
        QueryCourseInfoRes queryCourseInfoRes = courseMapper.getById(req.getCourseUuid());
        if (null == queryCourseInfoRes) {
            log.error("未查询到相关课程信息详情，查询课程uuid为：{}", req.getCourseUuid());
            throw new BusinessException(ResEnum.NON_EXISTENT);
        }
        //查询是否已存在订单
        int orderCount = courseOrderMapper.queryIsPurchase(queryCourseInfoRes.getUuid(), userUuid);
        if (orderCount > 0) {
            //是否购买该课程
            log.error("已购买该课程");
            throw new BusinessException(ResEnum.COURSE_ORDER_PURCHASED);
        }
        //新增课程订单信息
        String courseOrderUuid = insertOrderCourse(userName, req, queryCourseInfoRes);
        //新增课程订单详情信息
        insertCourseOrderContent(courseOrderUuid, userName, queryCourseInfoRes);
        return ResultRes.success(courseOrderUuid);
    }

    /**
     * 分页列表
     * @param req
     * @return
     */
    @Override
    public PageRes<List<QueryCourseOrderListRes>> list(QueryCourseOrderListReq req) {
        PageHelper.startPage(req.getPageNum(), req.getPageSize());
        //用户类型
        Integer userType = TokenHelper.getUserType();
        //用户uuid
        String userUuid = null;
        if (null != userType) {
            //查询店铺联系人详情信息
            userUuid = queryStoreUserInfo(userType, TokenHelper.getUserUuid());
        }
        List<QueryCourseOrderListRes> resList = courseOrderMapper.list(req, userUuid);
        PageInfo<QueryCourseOrderListRes> pageInfo = new PageInfo<>(resList);
        return PageRes.success(resList, pageInfo.getPageSize(), (int) pageInfo.getTotal(), pageInfo.getPages());
    }

    /**
     * 课程订单导出
     * @param exportReq
     * @param response
     */
    @Override
    public void exportCourseOrderList(QueryCourseOrderListReq exportReq, HttpServletResponse response) {
        try{
            List<QueryCourseOrderListRes> resList = courseOrderMapper.list(exportReq, null);
            //读取模板文件
            InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream(Constants.ORDER_COURSE_INFO_EXPORT_TEMPLATE);
            //设置空行默认属性
            List<QueryCourseOrderListRes> excelList = ExcelUtils.setFieldValue(resList);
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
                QueryCourseOrderListRes exportDto = excelList.get(rowIndex - 2);
                ExcelUtils.setCell(row,cellStyle1,0,rowIndex-1);
                ExcelUtils.setCell(row,cellStyle,1,exportDto.getBuyerName());
                ExcelUtils.setCell(row,cellStyle,2,exportDto.getBuyerMobile());
                ExcelUtils.setCell(row,cellStyle,3,exportDto.getCourseTitle());
                ExcelUtils.setCell(row,cellStyle,4,DateUtil.dateToStr(exportDto.getCreatedTime(), DateUtil.YYYY_MM_DD_HH_MM_SS));
                ExcelUtils.setCell(row,cellStyle,5,exportDto.getCourseLecturer());
                BigDecimal orderAmount = (null != exportDto.getOrderAmount()) ? exportDto.getOrderAmount() : BigDecimal.ZERO;
                ExcelUtils.setCell(row,cellStyle,6,"¥ " + orderAmount);
                ExcelUtils.setCell(row,cellStyle,7, CourseTypeEnum.enumOfDesc(exportDto.getCourseType()));
                ExcelUtils.setCell(row,cellStyle,8, exportDto.getPayType() == null ? "" : PayMethodEnum.enumOfDesc(exportDto.getPayType()));
            }
            ExcelUtils.responseWrite(wb,response, Constants.ORDER_COURSE_INFO_EXPORT_TEMPLATE);
        }catch (Exception ex){
            log.error("课程订单信息导出异常，异常原因：{}", ExceptionUtils.stackTraceToString(ex));
        }
    }

    /**
     * 新增课程订单信息
     * @param userName
     * @param req
     * @return
     */
    private String insertOrderCourse (String userName, AddCourseOrderReq req, QueryCourseInfoRes queryCourseInfoRes) {
        //用户uuid
        String userUuid = TokenHelper.getUserUuid();
        //用户类型
        Integer userType = TokenHelper.getUserType();
        //用户手机号
        String mobile = null;
        LoginToken loginToken = TokenHelper.getLoginToken();
        if (null != loginToken) {
            mobile = loginToken.getUserMobile();
        }
        //查询店铺联系人详情信息
        userUuid = queryStoreUserInfo(userType, userUuid);
        CourseOrder courseOrder = new CourseOrder();
        courseOrder.setUuid(UuidUtils.getUuid());
        courseOrder.setCourseUuid(req.getCourseUuid());
        courseOrder.setCourseType(queryCourseInfoRes.getCourseType());
        courseOrder.setCourseLecturer(queryCourseInfoRes.getCourseLecturer());
        courseOrder.setCourseTitle(queryCourseInfoRes.getCourseTitle());
        courseOrder.setCourseCover(queryCourseInfoRes.getCourseCover());
        courseOrder.setOrderNum(OrderUtils.GenOrderNo(OrderPrefixEnum.KC));
        courseOrder.setOrderAmount(queryCourseInfoRes.getCourseAmount());
        courseOrder.setOrderSts(OrderStsEnum.UNPAID.getValue());
        courseOrder.setBuyerUuid(userUuid);
        courseOrder.setBuyerName(userName);
        courseOrder.setBuyerMobile(mobile);
        courseOrder.setSts(StsEnum.ACTIVE.getValue());
        courseOrder.setCreatedBy(userName);
        courseOrder.setCreatedTime(new Date());
        int insertNum = courseOrderMapper.insert(courseOrder);
        if (insertNum <= 0 ) {
            log.error("新增课程订单信息失败，请求参数为：{}", JSON.toJSONString(courseOrder));
            throw new BusinessException(ResEnum.INSERT_DB_ERROR);
        }
        //新增order_info信息
        AddOrderInfoReq addOrderInfoReq = new AddOrderInfoReq();
        addOrderInfoReq.setOrderType(OrderTypeEnum.COURSE.getValue());
        addOrderInfoReq.setOrderUuid(courseOrder.getUuid());
        orderInfoService.addOrder(addOrderInfoReq);
        return courseOrder.getUuid();
    }

    /**
     * 新增课程订单详情信息
     * @param courseOrderUuid
     * @param userName
     * @param queryCourseInfoRes
     */
    private void insertCourseOrderContent (String courseOrderUuid, String userName, QueryCourseInfoRes queryCourseInfoRes) {
        CourseOrderDetail courseOrderDetail = new CourseOrderDetail();
        courseOrderDetail.setUuid(UuidUtils.getUuid());
        courseOrderDetail.setOrderUuid(courseOrderUuid);
        courseOrderDetail.setCourseIntro(queryCourseInfoRes.getCourseIntro());
        courseOrderDetail.setCourseContent(queryCourseInfoRes.getCourseContent());
        courseOrderDetail.setCourseTime(queryCourseInfoRes.getCourseTime());
        courseOrderDetail.setCourseUrl(queryCourseInfoRes.getCourseUrl());
        courseOrderDetail.setSts(StsEnum.ACTIVE.getValue());
        courseOrderDetail.setCreatedBy(userName);
        courseOrderDetail.setCreatedTime(new Date());
        int insertNum = courseOrderDetailMapper.insert(courseOrderDetail);
        if (insertNum <= 0 ) {
            log.error("新增课程订单详情信息失败，请求参数为：{}", JSON.toJSONString(courseOrderDetail));
            throw new BusinessException(ResEnum.INSERT_DB_ERROR);
        }
    }


    /**
     * 根据当前登录店铺联系人查询店铺联系人详情信息
     * @param userType
     * @param userUuid
     * @return
     */
    private String queryStoreUserInfo (Integer userType, String userUuid) {
        if (UserTypeEnum.store.getType().equals(userType)) {
            //当为店铺联系人时，获取店铺uuid
            ResultRes<StoreUserRes> resResultRes = storeUserFeign.queryStoreUserInfo(userUuid);
            if (!resResultRes.isSuccess()) {
                log.error("未查询到相关店铺联系人详情信息，查询uuid为：{}", userUuid);
                throw new BusinessException(ResEnum.NON_EXISTENT);
            } else {
                userUuid = resResultRes.getData().getStoreUuid();
            }
        }
        return userUuid;
    }
}
