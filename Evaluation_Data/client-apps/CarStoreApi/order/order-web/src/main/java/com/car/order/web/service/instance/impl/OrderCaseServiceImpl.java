package com.car.order.web.service.instance.impl;

import com.car.common.res.PageRes;
import com.car.common.res.ResultRes;
import com.car.common.utils.ExcelUtils;
import com.car.common.utils.ExceptionUtils;
import com.car.order.client.enums.goods.OrderStsEnum;
import com.car.order.client.enums.goods.PayMethodEnum;
import com.car.order.client.request.order.instance.QueryOrderCaseListReq;
import com.car.order.client.response.order.CarOwnerInfoRes;
import com.car.order.client.response.order.TechnicianInfoRes;
import com.car.order.client.response.order.driving.AfterSaleInfoRes;
import com.car.order.client.response.order.instance.OrderCaseDetailRes;
import com.car.order.client.response.order.instance.OrderCaseInfoListRes;
import com.car.order.client.response.order.instance.OrderDetailRes;
import com.car.order.web.common.constants.Constants;
import com.car.order.web.dto.OrderCaseDto;
import com.car.order.web.mapper.instance.OrderCaseMapper;
import com.car.order.web.service.instance.OrderCaseService;
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
public class OrderCaseServiceImpl implements OrderCaseService {

    @Autowired
    OrderCaseMapper orderCaseMapper;


    /**
     * 查询案例订单列表
     * @param param
     * @return
     */
    @Override
    public PageRes<List<OrderCaseInfoListRes>> queryOrderCaseList(QueryOrderCaseListReq param) {
        log.debug("查询案例订单列表");
        PageHelper.startPage(param.getPageNum(), param.getPageSize());
        List<OrderCaseInfoListRes> orderCaseInfoList = orderCaseMapper.queryOrderCaseInfoList(param);
        PageInfo<OrderCaseInfoListRes> pageInfo = new PageInfo<>(orderCaseInfoList);
        return PageRes.success(orderCaseInfoList, pageInfo.getPageSize(), (int) pageInfo.getTotal(), pageInfo.getPages());
    }

    /**
     * 查询案例订单详情
     * @param uuid
     * @return
     */
    @Override
    public ResultRes<OrderCaseDetailRes> queryOrderCaseDetail(String uuid) {
        log.debug("查询案例订单详情 uuid {}",uuid);
        OrderCaseDetailRes orderCaseInfoRes = null;
        OrderCaseDto orderCaseDto = orderCaseMapper.queryOrderCaseDetail(uuid);
        if (!StringUtils.isEmpty(orderCaseDto)){
            orderCaseInfoRes = new OrderCaseDetailRes();
            orderCaseInfoRes.setUuid(orderCaseDto.getUuid());
            orderCaseInfoRes.setEvaluateSts(orderCaseDto.getEvaluateSts());
            orderCaseInfoRes.setTechnicianScore(orderCaseDto.getTechnicianScore());
            orderCaseInfoRes.setCaseInfoListRes(orderCaseDto.getCaseInfoListRes());

            OrderDetailRes orderInfoRes = new OrderDetailRes();
            BeanUtils.copyProperties(orderCaseDto,orderInfoRes);
            orderCaseInfoRes.setOrderDetailRes(orderInfoRes);

            TechnicianInfoRes technicianInfoRes = new TechnicianInfoRes();
            BeanUtils.copyProperties(orderCaseDto,technicianInfoRes);
            orderCaseInfoRes.setTechnicianInfoRes(technicianInfoRes);

            CarOwnerInfoRes carOwnerInfoRes = new CarOwnerInfoRes();
            BeanUtils.copyProperties(orderCaseDto,carOwnerInfoRes);
            orderCaseInfoRes.setCarOwnerInfoRes(carOwnerInfoRes);

            if (!StringUtils.isEmpty(orderCaseDto.getRefundSts())) {
                AfterSaleInfoRes afterSaleInfoRes = new AfterSaleInfoRes();
                BeanUtils.copyProperties(orderCaseDto,afterSaleInfoRes);
                orderCaseInfoRes.setAfterSaleInfoRes(afterSaleInfoRes);
            }
        }
        return ResultRes.success(orderCaseInfoRes);
    }

    /**
     * 案例订单信息导出
     * @param exportReq
     * @param response
     */
    @Override
    public void exportOrderDrivingList(QueryOrderCaseListReq exportReq, HttpServletResponse response) {
        log.debug("案例订单信息导出");
        try {
            List<OrderCaseInfoListRes> orderCaseInfoList = orderCaseMapper.queryOrderCaseInfoList(exportReq);
            //读取模板文件
            InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream(Constants.ORDER_INSTANCE_INFO_EXPORT_TEMPLATE);
            //设置空行默认属性
            List<OrderCaseInfoListRes> excelList = ExcelUtils.setFieldValue(orderCaseInfoList);
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
                OrderCaseInfoListRes exportDto = excelList.get(rowIndex - 2);
                ExcelUtils.setCell(row,cellStyle1,0,rowIndex-1);
                ExcelUtils.setCell(row,cellStyle,1,exportDto.getOrderNum());
                ExcelUtils.setCell(row,cellStyle,2, exportDto.getCaseName());
                ExcelUtils.setCell(row,cellStyle,3,exportDto.getCaseNum());

                ExcelUtils.setCell(row,cellStyle,4,exportDto.getCreatedTime());
                ExcelUtils.setCell(row,cellStyle,5,exportDto.getTechnicianName());
                ExcelUtils.setCell(row,cellStyle,6, exportDto.getTechnicianMobile());
                ExcelUtils.setCell(row,cellStyle,7, "¥ "+exportDto.getOrderAmount());
                ExcelUtils.setCell(row,cellStyle,8, exportDto.getCarOwnerName());
                ExcelUtils.setCell(row,cellStyle,9, exportDto.getCarOwnerMobile());
                ExcelUtils.setCell(row,cellStyle,10, PayMethodEnum.enumOfDesc(exportDto.getPayType()));
                ExcelUtils.setCell(row,cellStyle,11, OrderStsEnum.enumOfDesc(exportDto.getOrderSts()));
            }
            ExcelUtils.responseWrite(wb,response, Constants.ORDER_INSTANCE_INFO_EXPORT_TEMPLATE);
        } catch (Exception ex){
            log.error("案例订单信息导出异常，异常原因：{}", ExceptionUtils.stackTraceToString(ex));
        }
    }
}
