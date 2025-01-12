package com.car.account.web.dto.profit;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author zhangyp
 * @date 2021/1/31 12:05
 */
@Data
public class ClassifyProfitDto {


    /**
     * 总收益
     */
    private BigDecimal totalAmt;

    /**
     * 分类
     */
    private Integer classify;
}
