package com.car.order.web.service.driving.impl;

import com.car.common.res.PageRes;
import com.car.common.res.ResultRes;
import com.car.common.utils.ExcelUtils;
import com.car.common.utils.ExceptionUtils;
import com.car.order.client.enums.driving.ServiceTypeEnum;
import com.car.order.client.enums.goods.OrderStsEnum;
import com.car.order.client.enums.goods.PayMethodEnum;
import com.car.order.client.request.order.driving.QueryOrderDrivingListReq;
import com.car.order.client.response.order.driving.AfterSaleInfoRes;
import com.car.order.client.response.order.CarOwnerInfoRes;
import com.car.order.client.response.order.driving.OrderInfoRes;
import com.car.order.client.response.order.TechnicianInfoRes;
import com.car.order.client.response.order.driving.*;
import com.car.order.web.common.constants.Constants;
import com.car.order.web.dto.OrderDrivingDto;
import com.car.order.web.mapper.driving.OrderDrivingMapper;
import com.car.order.web.service.driving.OrderDrivingService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.List;

/**
 * @author zhouz
 * @date 2020/12/31
 */
@Slf4j
@Service
public class OrderDrivingServiceImpl implements OrderDrivingService {

    @Autowired
    OrderDrivingMapper orderDrivingMapper;


    /**
     * 查询代驾订单列表
     * @param param
     * @return
     */
    @Override
    public PageRes<List<OrderDrivingInfoListRes>> queryOrderDrivingList(QueryOrderDrivingListReq param) {
        log.debug("查询商品订单列表");
        PageHelper.startPage(param.getPageNum(), param.getPageSize());
        List<OrderDrivingInfoListRes> orderDrivingList = orderDrivingMapper.queryOrderDrivingList(param);
        PageInfo<OrderDrivingInfoListRes> pageInfo = new PageInfo<>(orderDrivingList);
        return PageRes.success(orderDrivingList, pageInfo.getPageSize(), (int) pageInfo.getTotal(), pageInfo.getPages());
    }

    /**
     * 查询代驾订单详情
     * @param uuid
     * @return
     */
    @Override
    public ResultRes<OrderDrivingInfoRes> queryOrderDrivingDetail(String uuid) {
        log.debug("查询代驾订单详情 uuid {}",uuid);
        OrderDrivingInfoRes orderDrivingInfoRes = null;
        OrderDrivingDto orderDrivingDto = orderDrivingMapper.queryOrderDrivingDetail(uuid);
        if (!StringUtils.isEmpty(orderDrivingDto)){
            orderDrivingInfoRes = new OrderDrivingInfoRes();
            orderDrivingInfoRes.setUuid(orderDrivingDto.getUuid());
            orderDrivingInfoRes.setEvaluateSts(orderDrivingDto.getEvaluateSts());
            orderDrivingInfoRes.setTechnicianScore(orderDrivingDto.getTechnicianScore());

            OrderInfoRes orderInfoRes = new OrderInfoRes();
            BeanUtils.copyProperties(orderDrivingDto,orderInfoRes);
            orderDrivingInfoRes.setOrderInfoRes(orderInfoRes);

            TechnicianInfoRes technicianInfoRes = new TechnicianInfoRes();
            BeanUtils.copyProperties(orderDrivingDto,technicianInfoRes);
            orderDrivingInfoRes.setTechnicianInfoRes(technicianInfoRes);

            CarOwnerInfoRes carOwnerInfoRes = new CarOwnerInfoRes();
            BeanUtils.copyProperties(orderDrivingDto,carOwnerInfoRes);
            orderDrivingInfoRes.setCarOwnerInfoRes(carOwnerInfoRes);

            if (!StringUtils.isEmpty(orderDrivingDto.getRefundSts())) {
                AfterSaleInfoRes afterSaleInfoRes = new AfterSaleInfoRes();
                BeanUtils.copyProperties(orderDrivingDto,afterSaleInfoRes);
                orderDrivingInfoRes.setAfterSaleInfoRes(afterSaleInfoRes);
            }
        }
        return ResultRes.success(orderDrivingInfoRes);
    }

    /**
     * 代驾订单信息导出
     * @param exportReq
     * @param response
     */
    @Override
    public void exportOrderDrivingList(QueryOrderDrivingListReq exportReq, HttpServletResponse response) {
        log.debug("代驾订单信息导出");
        try {
            List<OrderDrivingInfoListRes> orderDrivingList = orderDrivingMapper.queryOrderDrivingList(exportReq);
            //读取模板文件
            InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream(Constants.ORDER_DRIVING_INFO_EXPORT_TEMPLATE);
            //设置空行默认属性
            List<OrderDrivingInfoListRes> excelList = ExcelUtils.setFieldValue(orderDrivingList);
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
                OrderDrivingInfoListRes exportDto = excelList.get(rowIndex - 2);
                ExcelUtils.setCell(row,cellStyle1,0,rowIndex-1);
                ExcelUtils.setCell(row,cellStyle,1,exportDto.getOrderNum());
                ExcelUtils.setCell(row,cellStyle,2, ServiceTypeEnum.enumOfDesc(exportDto.getServiceType()));
                ExcelUtils.setCell(row,cellStyle,3,exportDto.getTechnicianName());

                ExcelUtils.setCell(row,cellStyle,4,exportDto.getTechnicianMobile());
                ExcelUtils.setCell(row,cellStyle,5,exportDto.getCreatedTime());
                ExcelUtils.setCell(row,cellStyle,6, exportDto.getEndTime());
                ExcelUtils.setCell(row,cellStyle,7, "¥ "+exportDto.getOrderAmount());
                ExcelUtils.setCell(row,cellStyle,8, exportDto.getCarOwnerMobile());
                ExcelUtils.setCell(row,cellStyle,9, PayMethodEnum.enumOfDesc(exportDto.getPayType()));
                ExcelUtils.setCell(row,cellStyle,10, OrderStsEnum.enumOfDesc(exportDto.getOrderSts()));
            }
            ExcelUtils.responseWrite(wb,response, Constants.ORDER_DRIVING_INFO_EXPORT_TEMPLATE);
        } catch (Exception ex){
            log.error("代驾订单信息导出异常，异常原因：{}", ExceptionUtils.stackTraceToString(ex));
        }
    }
}
