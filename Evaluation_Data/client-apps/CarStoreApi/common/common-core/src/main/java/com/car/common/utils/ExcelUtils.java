package com.car.common.utils;

import com.car.common.enums.ResEnum;
import com.car.common.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.springframework.util.StringUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author xlj
 */
@Slf4j
public class ExcelUtils {


    /**
     * excel设置格式
     */
    public static CellStyle getExcelFormat(Cell cell){
        return cell.getCellStyle();
    }

    /**
     * 统一返回输出
     * @param wb
     * @param response
     * @param fileName
     */
    public static void responseWrite(Workbook wb, HttpServletResponse response, String fileName){
        log.info("记录生成文件名：{}",fileName);
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setHeader("Content-Disposition", "attachment;fileName="+ new String(fileName.getBytes("UTF-8"),"ISO-8859-1"));
            ServletOutputStream responseOutputStream = response.getOutputStream();
            wb.write(responseOutputStream);
            responseOutputStream.flush();
            responseOutputStream.close();
        } catch (IOException e) {
            log.error("生成列表名单导出错误，错误原因：{}", e);
            throw new BusinessException(ResEnum.DOWN_LOAD_FILE_ERROR.getValue(), ResEnum.DOWN_LOAD_FILE_ERROR.getDesc());
        }
    }

    /**
     * 根据模板名字自动追加日期命名
     * @param templateName
     * @return
     */
    public static String getFileName(String templateName){
        String fileName = templateName.substring(0,templateName.lastIndexOf("."))+DateUtil.dateToStr(new Date(),DateUtil.YYYY_MM_DD_HH_MM_SS)+templateName.substring(templateName.lastIndexOf("."),templateName.length());
        return fileName;
    }


    /**
     * 设置单元格内容
     * @param row
     * @param cellStyle
     * @param cellNum
     * @param value
     */
    public static void setCell(Row row,CellStyle cellStyle,Integer cellNum,String value){
        Cell cell1 = row.createCell(cellNum);
        cell1.setCellStyle(cellStyle);
        if(StringUtils.isEmpty(value)){
            cell1.setCellValue("-");
        }else {
            cell1.setCellValue(value);
        }

    }

    /**
     * 设置单元格内容
     * @param row
     * @param cellStyle
     * @param cellNum
     * @param value
     */
    public static void setCell(Row row,CellStyle cellStyle,Integer cellNum,Integer value){
        Cell cell1 = row.createCell(cellNum);
        cell1.setCellStyle(cellStyle);
        cell1.setCellValue(value);
    }

    /**
     * 设置表头格式
     *
     * @param header
     * @param style
     * @param titleRow
     */
    public static void headerFromat(List<String> header, CellStyle style, XSSFRow titleRow) {
        for (int i = 0; i < header.size(); i++) {
            XSSFCell cell = titleRow.createCell(i);
            //背景色
            style.setFillForegroundColor(IndexedColors.SKY_BLUE.getIndex());
            style.setBorderBottom(BorderStyle.THIN);
            // 下边框
            style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
            style.setBorderLeft(BorderStyle.THIN);
            // 左边框
            style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
            style.setBorderRight(BorderStyle.THIN);
            // 右边框
            style.setRightBorderColor(IndexedColors.BLACK.getIndex());
            style.setBorderTop(BorderStyle.THIN);
            // 上边框
            style.setTopBorderColor(IndexedColors.BLACK.getIndex());
            //自动换行
            style.setWrapText(true);
            //cell 是 HSSFCell 对象
            cell.setCellStyle(style);
            cell.setCellValue(header.get(i));
        }
    }



    public static  <K>  List<K> setFieldValue(List<K> list) throws  IllegalAccessException {
        for (K k : list) {
            Field[] fields = k.getClass().getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                fields[i].setAccessible(true);
                Object value = fields[i].get(k);
                if ("java.util.Date".equals(fields[i].getType().getTypeName())){
                    //日期类型不处理
                    continue;
                }
                if ("java.math.BigDecimal".equals(fields[i].getType().getTypeName())){
                    //BigDecimal类型不处理
                    continue;
                }
                if ("java.lang.Integer".equals(fields[i].getType().getTypeName())){
                    //Integer类型不处理
                    continue;
                }
                if ("java.util.List".equals(fields[i].getType().getTypeName())){
                    //List类型不处理
                    continue;
                }
                if ("java.lang.Float".equals(fields[i].getType().getTypeName())){
                    //Float类型不处理
                    continue;
                }
                if (StringUtils.isEmpty(value)){
                    fields[i].set(k,"-");
                }
            }
        }
        return list;
    }

    public static String getSheetValue(Sheet sheet,int i,int j){
        Row row ;
        if(isMergedRegion(sheet,i,j)){
            return getMergedRegionValue(sheet,i,j);
        }else{
            row = sheet.getRow(i);
            return getCellValue(row.getCell(j));
        }
    }


    /**
     * 获取单元格的值
     * @param cell
     * @return
     */
    public static String getCellValue(Cell cell){
        if(cell == null){
            return "";
        }
        return cell.getStringCellValue();
    }


    /**
     * 合并单元格处理,获取合并行
     * @param sheet
     * @return List<CellRangeAddress>
     */
    public static List<CellRangeAddress> getCombineCell(Sheet sheet)
    {
        List<CellRangeAddress> list = new ArrayList<>();
        //获得一个 sheet 中合并单元格的数量
        int sheetmergerCount = sheet.getNumMergedRegions();
        //遍历所有的合并单元格
        for(int i = 0; i<sheetmergerCount;i++)
        {
            //获得合并单元格保存进list中
            CellRangeAddress ca = sheet.getMergedRegion(i);
            list.add(ca);
        }
        return list;
    }

    private static int getRowNum(List<CellRangeAddress> listCombineCell,Cell cell,Sheet sheet){
        int xr = 0;
        int firstC = 0;
        int lastC = 0;
        int firstR = 0;
        int lastR = 0;
        for(CellRangeAddress ca:listCombineCell)
        {
            //获得合并单元格的起始行, 结束行, 起始列, 结束列
            firstC = ca.getFirstColumn();
            lastC = ca.getLastColumn();
            firstR = ca.getFirstRow();
            lastR = ca.getLastRow();
            if(cell.getRowIndex() >= firstR && cell.getRowIndex() <= lastR)
            {
                if(cell.getColumnIndex() >= firstC && cell.getColumnIndex() <= lastC)
                {
                    xr = lastR;
                }
            }

        }
        return xr;

    }
    /**
     * 判断单元格是否为合并单元格，是的话则将单元格的值返回
     * @param listCombineCell 存放合并单元格的list
     * @param cell 需要判断的单元格
     * @param sheet sheet
     * @return
     */
    public static String isCombineCell(List<CellRangeAddress> listCombineCell,Cell cell,Sheet sheet)
            throws Exception{
        int firstC = 0;
        int lastC = 0;
        int firstR = 0;
        int lastR = 0;
        String cellValue = null;
        for(CellRangeAddress ca:listCombineCell)
        {
            //获得合并单元格的起始行, 结束行, 起始列, 结束列
            firstC = ca.getFirstColumn();
            lastC = ca.getLastColumn();
            firstR = ca.getFirstRow();
            lastR = ca.getLastRow();
            if(cell.getRowIndex() >= firstR && cell.getRowIndex() <= lastR)
            {
                if(cell.getColumnIndex() >= firstC && cell.getColumnIndex() <= lastC)
                {
                    Row fRow = sheet.getRow(firstR);
                    Cell fCell = fRow.getCell(firstC);
                    cellValue = getCellValue(fCell);
                    break;
                }
            }
            else
            {
                cellValue = "";
            }
        }
        return cellValue;
    }

    /**
     * 获取合并单元格的值
     * @param sheet
     * @param row
     * @param column
     * @return
     */
    public static String getMergedRegionValue(Sheet sheet ,int row , int column){
        int sheetMergeCount = sheet.getNumMergedRegions();

        for(int i = 0 ; i < sheetMergeCount ; i++){
            CellRangeAddress ca = sheet.getMergedRegion(i);
            int firstColumn = ca.getFirstColumn();
            int lastColumn = ca.getLastColumn();
            int firstRow = ca.getFirstRow();
            int lastRow = ca.getLastRow();

            if(row >= firstRow && row <= lastRow){
                if(column >= firstColumn && column <= lastColumn){
                    Row fRow = sheet.getRow(firstRow);
                    Cell fCell = fRow.getCell(firstColumn);
                    return getCellValue(fCell) ;
                }
            }
        }

        return null ;
    }


    /**
     * 判断指定的单元格是否是合并单元格
     * @param sheet
     * @param row 行下标
     * @param column 列下标
     * @return
     */
    private static boolean isMergedRegion(Sheet sheet,int row ,int column) {
        int sheetMergeCount = sheet.getNumMergedRegions();
        for (int i = 0; i < sheetMergeCount; i++) {
            CellRangeAddress range = sheet.getMergedRegion(i);
            int firstColumn = range.getFirstColumn();
            int lastColumn = range.getLastColumn();
            int firstRow = range.getFirstRow();
            int lastRow = range.getLastRow();
            if(row >= firstRow && row <= lastRow){
                if(column >= firstColumn && column <= lastColumn){
                    return true;
                }
            }
        }
        return false;
    }
}


