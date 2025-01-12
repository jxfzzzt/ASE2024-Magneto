package com.car.common.datasource.model;

import lombok.Data;

import javax.persistence.Column;

/**
 * 系统操作日志表
 */
@Data
public class SysOperationLog extends BaseModelInfo {

    /**
     * 操作模块
     */
    @Column(name = "oper_modul")
    private String operModul;
    /**
     * 操作类型
     */
    @Column(name = "oper_type")
    private String operType;
    /**
     * 操作描述
     */
    @Column(name = "oper_desc")
    private String operDesc;

    /**
     * 请求方法
     */
    @Column(name = "oper_method")
    private String operMethod;

    /**
     * 请求参数
     */
    @Column(name = "oper_requ_param")
    private String operRequParam;

    /**
     * 返回参数
     */
    @Column(name = "oper_resp_param")
    private String operRespParam;

    /**
     * 请求IP
     */
    @Column(name = "oper_ip")
    private String operIp;

    /**
     * 请求URI
     */
    @Column(name = "oper_uri")
    private String operUri;



}
