package com.xiaoleilu.ucloud.ufile;

import com.xiaoleilu.ucloud.core.Action;

/**
 * 对象存储 UFile API指令
 * @author Looly
 *
 */
public enum UFileAction implements Action{
	/** 创建Bucket */
    CreateBucket,
    /** 获取Bucket的描述信息 */
    DescribeBucket,
    /** 设置Bucket的属性 */
    UpdateBucket,
    /** 删除Bucket */
    DeleteBucket,
    /** 获取Bucket的文件列表 */
    GetFileList,
}
