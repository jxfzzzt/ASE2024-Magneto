package com.car.account.client.response.technician;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author zhangyp
 * @date 2021/1/21 1:56
 */
@Data
@ApiModel
public class TechnicianAnswerRes {

	@ApiModelProperty("技师uuid")
	private String uuid;

	@ApiModelProperty("头像图片地址")
	private String photoImgUrl;

	@ApiModelProperty("技师名称")
	private String name;

	@ApiModelProperty("技师类型")
	private String technologyType;

	@ApiModelProperty("评分")
	private BigDecimal score;

	@ApiModelProperty("问答总数")
	private Integer qaCount;

	@ApiModelProperty("问答费用")
	private BigDecimal answerAmt;

	@ApiModelProperty("品牌列表")
	private List<TechnicianBrandRes> brandResList;

	@ApiModelProperty("车友邦技能等级标签(0=普通,1=专家)")
	private Integer cybAuth;
}
