package com.car.common.utils;

/**
 * @author: xlj
 * @Description:
 * @Date: Create By 12:59
 */
public class Constants {

	/**
	 * 当前支付是否关闭
	 */
	public static final String PRICE_ON_OFF_UUID = "0";

	/**
	 * 查询父节点所有地区
	 */
	public static final String SYS_AREA_QUERY_LIST_BY_PARENT_CACHE_KEY = "sysArea:queryListByParent:cacheKey:%s";
	/**
	 * 查询所有区域信息缓存Key
	 */
	public static final String SYS_AREA_QUERY_LIST_CACHE_KEY = "sysArea:queryList:cacheKey:%s";

	/**
	 * 拼接前缀字符串
	 */
	public static final String SUFFIX_SPLITOR = ".";

	/**
	 *  根据类型查询字典
	 */
	public static final String queryDictListByType_cache_key = "sysDictionary:queryListByType:cacheKey:%s";

	/**
	 *  根据类型查询字典
	 */
	public static final String queryDictDetailByUuid_cache_key = "sysDictionary:queryDictDetailByUuid:cacheKey:%s";

	/**
	 * 用户数据序列号
	 */
	public static final String MOBILE_SERIAL_CACHE_KEY = "yanxin:car:portal:mobile:serial:%s";

	/**
	 * 用户数据序列号总数
	 */
	public static final String MOBILE_SERIAL_TOTAL_CACHE_KEY = "yanxin:car:portal:mobile:serialTotal";

	/**
	 * redis 内容前缀 method_key
	 */
	public static final String contents = "yanxin:car:portal:content:%s:%s";

	/**
	 * 验证码(邮箱/短信)
	 */
	public static final String LOGIN_VERIFICATION_CODE_CACHE_KEY = "yanxin:car:portal:login:verificationCode:%s";

	/**
	 * 校验验证码次数 key 值 accountName_code
	 */
	public static final String VERIFICATION_CODE_TIMES_CACHE_KEY = "yanxin:car:portal:login:verificationCodeTimes:%s_%s";

	/**
	 * 地区编码和名字
	 */
	public static final String AREA_CODE_RELATE_AREA_NAME = "yanxin:car:addr:areaCode:areaCode_%s";

	/**
	 * 商品分类
	 */
	public static final String GOODS_CLASSIFY_CODE_NAME = "yanxin:car:classify:classifyCode:classifyCode_%s";

	/**
	 * 验证码有效期时间5分钟
	 */
	public static final Integer LOGIN_VERIFICATION_CODE_TIME = 5;

	/**
	 * 操作频率阈值
	 */
	public static final Integer OPERATING_FREQUENCY = 50;
}
