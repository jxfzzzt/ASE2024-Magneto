package com.xiaoleilu.ucloud.core.enums;

import com.xiaoleilu.ucloud.core.Param.Name;


/**
 * 公共参数名枚举<br>
 * 此枚举包括一些API参数中共用的参数名，例如公钥、私钥、指令名称、数据中心等
 * @author Looly
 *
 */
public enum PubName implements Name{
	
	/* ----------------------------- 公共参数 ----------------------------- */
	/** 公钥 */
	PublicKey,
	/** 签名 */
	Signature,
	
	/** 指令名称 */
	Action,
	/** 数据中心 */
	Region,
	
	/** 数据偏移量，默认为0 */
	Offset,
	/** 返回数据长度，默认为20 */
	Limit,
	
	/** 密码 */
	Password
}
