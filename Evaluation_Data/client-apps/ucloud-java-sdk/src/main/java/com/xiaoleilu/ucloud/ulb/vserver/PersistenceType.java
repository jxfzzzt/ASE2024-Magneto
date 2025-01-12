package com.xiaoleilu.ucloud.ulb.vserver;

/**
 * VServer会话保持方式
 * @author Looly
 *
 */
public enum PersistenceType {
	/** 关闭会话保持 */
	None,
	/** 自动生成 */
	ServerInsert,
	/** 用户自定义 */
	UserDefined
}
