package com.xiaoleilu.ucloud.ufile;

import com.xiaoleilu.ucloud.core.Param.Name;

/**
 * UFile中使用的参数名
 * @author Looly
 *
 */
public enum UFileName implements Name{
	/** 请求的授权签名 */
	Authorization,
	/** Bucket的名称 */
	BucketName,
	/** Bucket访问类型，public或private; 默认为private */
	Type,
	/** 分片下载的文件范围 */
	Range,
	/** Bucket的自定义域名 */
	Domain
}
