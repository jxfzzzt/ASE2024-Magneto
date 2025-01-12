package com.car.order.web.dto;

import com.car.order.client.response.order.instance.CaseInfoRes;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author zhouz
 * @date 2020/12/31
 */
@Data
public class OrderCaseDto {

    private String uuid;

    private Integer evaluateSts;

    private BigDecimal technicianScore;

    private String orderNum;

    private String orderAmount;

    private String receivableAmount;

    private Integer orderSts;

    private List<Integer> orderStsList;

    private Integer payType;

    private String orderRemark;

    private String carOwnerUuid;

    private String carOwnerArea;

    private String carOwnerName;

    private String carOwnerMobile;

    private String alipayAccount;

    private String technicianUuid;

    private String technicianName;

    private String technicianMobile;

    private Integer refundSts;

    private String afterSaleCause;

    private List<String> afterSaleImgList;

    private List<CaseInfoRes> caseInfoListRes;
}
