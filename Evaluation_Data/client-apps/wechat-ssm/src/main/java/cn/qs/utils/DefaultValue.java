package cn.qs.utils;

/**
 * 默认值
 * 
 * @author Administrator
 *
 */
public class DefaultValue {

	/**
	 * 每页数量
	 */
	public static final int PAGE_SIZE = 6;

	/**
	 * 移动端token的默认失效时间
	 */
	public static final int TOKEN_DEFAULT_TIME = 2;

	/**
	 * 移动端token的更新时间(也就是如果更新时间+当前时间大于预计失效时间就更新token)
	 */
	public static final int TOKEN_UPDATE_TIME = 1;

	/**
	 * 系统管理员觉
	 */
	public static final String ROLE_SYSYEM = "系统管理员";

	/**
	 * 普通用户
	 */
	public static final String ROLE_PLAIN_USER = "普通用户";

	/**
	 * 默认的用户头像(用于app中头像显示)
	 */
	public static String DEFAULT_USER_WECHAT_PHOTO = "http://thirdwx.qlogo.cn/mmopen/vi_32/eyzvWHuvEwzP5zAcxahq1PXMfAqOfMOB7b90zru3OSSyxkp0UibibIFATgyUHnPxkloswOxs7GIH5pWF0pYC70lw/132";

}
