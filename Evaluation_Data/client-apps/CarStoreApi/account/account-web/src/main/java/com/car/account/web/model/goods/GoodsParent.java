package com.car.account.web.model.goods;

import com.car.common.datasource.model.BaseModelInfo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * @author zhouz
 * @date 2020/12/23
 */
@Data
@Table(name = "goods_parent")
public class GoodsParent extends BaseModelInfo {

    /**
     * 商品分组名称
     */
    @Column(name = "group_name")
    private String groupName;

    /**
     * 商品分组拼音名称
     */
    @Column(name = "group_name_en")
    private String groupNameEn;

    /**
     * 父组id
     */
    @Column(name = "parent_id")
    private String parentId;

    /**
     * 平台补贴
     */
    @Column(name = "sys_subsidy")
    private BigDecimal sysSubsidy;
}
