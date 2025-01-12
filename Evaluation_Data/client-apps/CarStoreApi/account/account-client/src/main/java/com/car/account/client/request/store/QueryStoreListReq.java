package com.car.account.client.request.store;

import com.car.common.req.PageReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author zhouz
 * @date 2020/12/19
 */
@ApiModel(value="QueryStoreListReq",description="查询店铺列表VO对象")
@Data
public class QueryStoreListReq extends PageReq {

    @ApiModelProperty(value = "店铺名称",name = "storeName")
    private String storeName;

    @ApiModelProperty(value = "店铺类型,101加盟店,102 4S店,103合作伙伴,104旗舰店",name = "storeType")
    private String storeType;

    @ApiModelProperty(value = "店铺类型,101加盟店,102 4S店,103合作伙伴,104旗舰店",name = "storeTypeList")
    private List<String> storeTypeList;

    @ApiModelProperty(value = "联系人姓名",name = "contactsName")
    private String contactsName;

    @ApiModelProperty(value = "管理员手机号",name = "mobile")
    private String mobile;


    @ApiModelProperty(value = "区域Uuid",name = "areaUuid")
    private String areaUuid;

    @ApiModelProperty(value = "品牌uuid",name = "brandUuid")
    private String brandUuid;

    @ApiModelProperty(value = "大类",name = "levelOne")
    private String levelOne;

    @ApiModelProperty(value = "小类",name = "levelTwo")
    private String levelTwo;

    @ApiModelProperty(value = "排序方式",name = "orderType")
    private String orderType;


}
