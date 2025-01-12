package com.car.order.client.response.technicianappointment;

import com.car.order.client.response.technicianappointment.ShareTechnicianOrderRes;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.rmi.Naming;
import java.util.List;

/**
 * @author zhoujian
 * @PACKAGE_NAME: com.car.order.client.request.technicianappointment
 * @NAME: ShareTechnicianOrderInfoRes
 * @DATE: 2021/3/5 22:56
 */
@Data
@ApiModel(value = "ShareTechnicianOrderInfoRes",description = "订单详情出参返回")
public class ShareTechnicianOrderInfoRes extends ShareTechnicianOrderRes {

    @ApiModelProperty(value = "技师姓名")
    private String name;

    @ApiModelProperty(value = "技师头像")
    private String photoImgUrl;

    @ApiModelProperty(value = "技师类型")
    private String technologyType;

    @ApiModelProperty(value = "技师类型名称")
    private String technologyTypeName;

    @ApiModelProperty(value = "技师工龄")
    private Integer workingYear;

    @ApiModelProperty(value = "技师预约次数")
    private Integer shareNum;

    @ApiModelProperty(value = "技师维修品牌", name = "brandList")
    private List<TechnicianBrandRes> brandList;

    @ApiModelProperty(value = "技师联系电话")
    private String technicianPhone;

    @ApiModelProperty(value = "车主联系电话")
    private String carOwnerPhone;


}
