package com.car.common.utils;/**
 * Created by fengwenyuan on 2020/2/20.
 */

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.List;

/**
 * @ClassName ExcelConfig
 * @Description:
 * @Author fengwenyuan
 * @Date 2020/2/20
 * @Version V1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@NonNull
public class ExcelConfig<T> {

    /**
     * 头部list
     */
    private List<String> header;
    /**
     * sheet名称
     */
    private String sheetName;
    /**
     * 单元格宽度
     */
    private short defaultWidth = 20;
    /**
     * 单元格高度
     */
    private short defaultHeight = 2000;

    /**
     * 页码条数
     */
    private int pageNumber=1000;

    /**
     * 图片位置，在第几个单元格，从0开始
     */
    private short imagePosition;

    /**
     * 数据集
     */
    private List<T> mapList;

}
