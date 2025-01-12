package com.car.order.web.dto;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author zhouz
 * @date 2020/12/31
 */
@Data
public class OrderDrivingDto {

    private String uuid;

    private String orderNum;

    private Integer serviceType;

    private String orderAmount;

    private Integer orderSts;

    private Integer payType;

    private String endTime;

    private String startPlace;

    private String endPlace;

    private String technicianUuid;

    private String technicianName;

    private String technicianMobile;

    private String carOwnerUuid;

    private String carOwnerArea;

    private String carOwnerName;

    private String carOwnerMobile;

    private String alipayAccount;

    private Integer refundSts;

    private String afterSaleCause;

    private List<String> afterSaleImgList;

    private Integer evaluateSts;

    private BigDecimal technicianScore;
}
