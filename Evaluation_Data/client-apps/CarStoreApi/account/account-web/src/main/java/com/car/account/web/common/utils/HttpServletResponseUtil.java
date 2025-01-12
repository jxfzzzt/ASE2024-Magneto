package com.car.account.web.common.utils;

import com.car.common.enums.ResEnum;
import com.car.common.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * @description:
 * @author: zj430
 * @date: Created in 2020/5/24 16:33
 * @version: 导出公共返回流
 * @modified By:
 */
@Slf4j
public class HttpServletResponseUtil {

    public static void responseWrite(HSSFWorkbook wb, HttpServletResponse response,String fileName){
        log.info("记录生成文件名：{}",fileName);
        OutputStream os = null;
        response.reset();
        try {
            response.setCharacterEncoding("UTF-8");
            response.setHeader("content-Type", "application/vnd.ms-excel");
            response.addHeader("Content-Disposition", "attachment;filename=" + new String((fileName + ".xls").getBytes("UTF-8"), "ISO-8859-1"));
            os = response.getOutputStream();
            wb.write(os);
            if (os != null) {
                os.close();
            }
        } catch (IOException e) {
            log.error("生成列表名单导出错误，错误原因：{}", e);
            throw new BusinessException(ResEnum.DOWN_LOAD_FILE_ERROR.getValue(), ResEnum.DOWN_LOAD_FILE_ERROR.getDesc());
        }
    }


    public static void responseWriteZip(File zipFile, HttpServletResponse response, String fileName){
        log.info("记录生成文件名：{}",fileName);
        OutputStream os = null;
        response.reset();
        try {
            response.setCharacterEncoding("UTF-8");
            response.setHeader("content-Type", "application/octet-stream");
            response.addHeader("Content-Disposition", "attachment;filename=" + new String((fileName + ".zip").getBytes("UTF-8"), "ISO-8859-1"));

            InputStream input = new FileInputStream(zipFile);
            writeFile(response,input);
            boolean b = FileUtils.deleteQuietly(zipFile);
            log.info("删除压缩文件>>>path:{},flag:{}",zipFile.getAbsolutePath(),b);
        } catch (IOException e) {
            log.error("生成列表名单导出错误，错误原因：{}", e);
            throw new BusinessException(ResEnum.DOWN_LOAD_FILE_ERROR.getValue(), ResEnum.DOWN_LOAD_FILE_ERROR.getDesc());
        }
    }

    public static void writeFile(HttpServletResponse resp, InputStream inputStream) {
        OutputStream out = null;
        try {
            out = resp.getOutputStream();
            int len = 0;
            byte[] b = new byte[1024];
            while ((len = inputStream.read(b)) != -1) {
                out.write(b, 0, len);
            }
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }

                if(inputStream != null){
                    inputStream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
