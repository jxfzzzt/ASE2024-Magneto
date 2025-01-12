package com.car.system.web.model;

import com.car.common.datasource.model.BaseModelInfo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * @author linjiang.xie
 * @date 2020/2/18 11:32
 */
@Data
@Table(name = "sys_dict")
public class SysDict extends BaseModelInfo {
    /**
     * 类型
     */
    @Column(name = "lable_type")
    private String lableType;

    /**
     * 类型描述
     */
    @Column(name = "lable_type_desc")
    private String lableTypeDesc;

    /**
     * 编号(传递后端值)
     */
    @Column(name = "lable_code")
    private String lableCode;

    /**
     * 数值
     */
    @Column(name = "lable_value")
    private String lableValue;

    /**
     * 描述(页面展示)
     */
    @Column(name = "lable_desc")
    private String lableDesc;

    /**
     * 描述英文
     */
    @Column(name = "lable_desc_en")
    private String lableDescEn;

    /**
     * 排序(类型列表排序)
     */
    @Column(name = "sort_num")
    private Integer sortNum;



}
