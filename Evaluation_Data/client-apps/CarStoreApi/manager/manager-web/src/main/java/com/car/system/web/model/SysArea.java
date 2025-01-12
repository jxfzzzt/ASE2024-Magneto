package com.car.system.web.model;

import com.car.common.datasource.model.BaseModelInfo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * @author xlj
 */
@Data
@Table(name = "sys_area")
public class SysArea extends BaseModelInfo {

    /**
     * 地区编码
     */
    @Column(name = "area_code")
    private String areaCode;

    /**
     * 地区名
     */
    @Column(name = "area_name")
    private String areaName;

    /**
     * 地区名拼音
     */
    @Column(name = "area_name_en")
    private String areaNameEn;

    /**
     * 地区级别（1:省份province,2:市city）
     */
    @Column(name = "area_type")
    private Integer areaType;
    /**
     * 城市编码
     */
    @Column(name = "city_code")
    private String cityCode;

    /**
     * 地区上级编码
     */
    @Column(name = "parent_code")
    private String parentCode;

    /**
     * 地区父节点
     */
    @Column(name = "parent_uuid")
    private String parentUuid;

    @Column(name = "letter")
    private String letter;

}
