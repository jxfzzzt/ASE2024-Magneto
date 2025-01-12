package com.xiaoleilu.ucloud.ulb;

import com.xiaoleilu.ucloud.core.Action;

/**
 * 负载均衡 ULB API指令
 * @author Looly
 *
 */
public enum ULBAction implements Action{
	/** 创建负载均衡实例 */
    CreateULB,
    /** 删除负载均衡实例 */
    DeleteULB,
    /** 显示负载均衡实例信息 */
    DescribeULB,
    /** 修改负载均衡实例属性 */
    UpdateULBAttribute,
    
    /** 创建VServer实例 */
    CreateVServer,
    /** 删除VServer实例 */
    DeleteVServer,
    /** 修改VServer实例属性 */
    UpdateVServerAttribute,
    
    /** 添加ULB后端资源实例 */
    AllocateBackend,
    /** 释放ULB后端资源实例 */
    ReleaseBackend,
    /** 修改ULB后端资源实例(主机池)属性 */
    UpdateBackendAttribute,
    
    /** 添加SSL证书 */
    CreateSSL,
    /** 删除SSL证书 */
    DeleteSSL,
    /** 将SSL证书绑定到VServer */
    BindSSL,
    /** 显示SSL证书信息 */
    DescribeSSL,
    
    /** 创建内容转发策略组 */
    CreatePolicyGroup,
    /** 删除内容转发策略组 */
    DeletePolicyGroup,
    /** 显示内容转发策略组详情 */
    DescribePolicyGroup,
    /** 修改内容转发策略组配置信息 */
    UpdatePolicyGroupAttribute,
    /** 创建内容转发策略 */
    CreatePolicy,
    /** 删除内容转发策略 */
    DeletePolicy

}
