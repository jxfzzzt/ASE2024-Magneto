package com.car.common.enums;

import lombok.Getter;

/**
 * VO输出数据状态枚举
 *
 * @author suhaibo
 */
@Getter
public enum ResEnum {

	/**
	 * 统一返回枚举
	 */
	SUCCESS("0000", "success"), LACK_PARAMETER("0001", "缺少必要参数"), NON_EXISTENT("0002", "未匹配到对应数据"),
	INSERT_DB_ERROR("10003", "新增数据受影响行数为0！"), UPDATE_DB_ERROR("10004", "修改数据受影响行数为0！"),
	DELETE_DB_ERROR("10005", "删除数据受影响行数为0！"), DB_ERROR("9001", "数据库异常"), NET_ERROR("9002", "内部网络异常"),
	API_ERROR("9003", "外部接口请求错误"), DOWN_LOAD_FILE_ERROR("9004", "下载文件错误"),

	EXCEL_SIZE_FAIL("9005", "Excel大小超出范围"), EXCEL_DATA_NULL("9006", "导出数据为空"), SYSTEM_ERROR("9999", "内部系统错误"),

	OPERATION_FREQUENTLY_ERROR("9996", "操作过于频繁，请稍后重试"), REQUEST_NULL_ERROR("9006", "入参VO对象NULL异常"),
	VALID_PARAM_ERROR("9001", "参数缺失"), CODE_EXIST("9008", "当前code码已存在"),

	MICRO_SERVICE_UNAVAILABLE("40001", "微服务不可用"), TOKEN_ERROR("40002", "invalid Token"),
	TOKEN_REMOTE_LOGIN("4003", "账号已在其他设备登录"), LICENSE_ERROR("4004", "license授权已过期"),
	EMPTY_TOKEN("40005", "empty Token"), EXPIRE_TOKEN("40006", "登录已过期，请重新登录"), PERMISSION_DENIED("40007", "权限不足"),

	/**
	 * 系统管理模块
	 */
	DELETE_SUPPER_ADMIN_ERROR("10014", "超级管理员账号不可删除！"), LOGIN_USER_EXIST("10013", "用户名已存在！"),
	ORIGINAL_PASSWORD_ERROR("10012", "旧密码输入错误，请重新输入！"), VERIFICATION_CODE_ERROR("10010", "验证码错误！"),
	LOGIN_USER_ERROR("10001", "密码错误，请重新输入或联系管理员！"), LOGIN_USER_EXISTS("10002", "账号不存在！"),
	LOGIN_USER_PROHIBIT("10003", "登录失败，该账号已被禁用！"), EXIT_ERROR("10004", "退出登录失败"),
	AUTHORITY_EXIST_DATA("10020", "此权限已被其他角色使用！"), ADD_UPDATE_ROLE_FAIL("10011", "角色名称已存在，请重新输入！"),
	ROLE_USER_EXIST("10015", "该角色已有用户使用，无法删除！"), AREA_NAME_TYPE_ERROR("10003", "匹配多条名称地区"),
	DIC_LAB_VALUE_ERROR("10021", "字典名称查询异常"), LOGIN_TERMINAL_ERROR("10022", "登陆终端异常"),
	LOGIN_ACCOUNT_ERROR("10023", "该账号未注册，请先注册后在登陆！"), REPEAT_SEND_MSG_ERROR("10024", "请勿重复发送验证码！"),

	/**
	 * Utility模块，采用20000数字编码
	 */
	UPLOAD_IMAGE_ERROR("20001", "图片上传失败"), IMAGE_UPLOAD_LIMIT("20002", "图片上传大小限制"),
	IMAGE_PRESERVE_ERROR("20003", "图像保存错误"), FILE_TYPE_ERROR("20004", "该文件类型不支持上传"),
	FILE_PARAM_ERROR("20005", "缺少文件类型参数"),

	/**
	 * Store模块，采用30000数字编码
	 */
	STORE_NAME_EXIST("30001", "店铺名称已存在"), MOBILE_BINDING_STORE("30002", "该手机号已绑定店铺"),
	CHECK_REJECTED("30003", "请填写驳回原因"), STORE_USER_ADMIN_NOT_MORE("30004", "店铺联系人管理员人数不能为多个"),
	NOT_STORE_CONTACT("30005", "请填写店铺联系人"), STORE_CONTACT_NOT_EXIST("30006", "未定位到店铺联系人信息"),
	NOT_STORE_BRAND("30007", "请填写店铺维修品牌"), STORE_NO_ACCOUNT_AMT("30008", "店铺资金账户缺失"),
	STORE_INVALID_TYPE("30009", "非店铺类型用户"), STORE_INFO_NOT_BY_USER("30010", "根据店铺联系人未定位到店铺信息"),
	POSITION_ERROR("30011", "地址输入错误"),

	/**
	 * vehicle模块，采用40000数字编码
	 */
	MOBILE_EXIST_ERROR("40001", "手机号码已存在"), ID_CARD_EXIST_ERROR("40002", "证件号码已存在"),
	VEHICLE_CROSS_RIGHTS_ERROR("40003", "存在非车主名下的车辆信息"), PLATE_NUMBER_EXIST_ERROR("40004", "车牌号已存在"),
	VEHICLE_OWNER_NOT_EXIST("40005", "车主不存在"),

	VEHICLE_NOT_SUPPORT_SWITCH("40006", "车主不支持角色切换"), VEHICLE_NOT_ACCOUNT_AMT("40007", "不支持车主账户金额查询"),

	/**
	 * 商品模块，采用50000数字编码
	 */
	NOT_ADD_MAIN_GRAPH("50001", "当前商品未添加主图"), MORE_MAIN_GRAPH("50002", "当前商品存在多张主图"), NOT_STORE("50003", "店铺不存在"),
	ERROR_GOODS_EMPTY("50004", "物料缺失"),

	/**
	 * 内容模块，采用60000数字编码
	 */
	INVALID_CHECK_STS("60001", "无效的审核状态入参"),

	/**
	 * 订单模块，采用70000数字编码
	 */
	INVALID_PAY_TYPE("70001", "无效的支付类型入参"), INVALID_ORDER_TYPE("70002", "无效的订单类型入参"),
	INVALID_ACCEPT_RESULT("70003", "无效的回答结果状态入参"), PAY_ERROR("7004", "支付失败"), PAY_BACK_ERROR("7005", "支付回调失败"),
	DTC_NOT_VEHICLE_ISSUER("7006", "车主无权限发布DTC故障"), INVALID_ORDER_AMOUNT("7007", "无效得订单金额入参"),
	INSERT_ORDER_ERROR("7008", "生成预约技师订单信息错误"), NOT_ORDER_ERROR("7009", "无效的订单信息"),
	LOGIN_TERMINAL_UPDATE_ERROR("7010", "更新订单登录终端错误"), TERMINAL_UPDATE_ERROR("7011", "车主已发起退款"),

	/**
	 * 提现模块，采用80000数字编码
	 */
	USER_ROLE_TYPE_ERROR("80001", "提现用户角色类型输入错误"), WITHDRAWAL_TYPE_ERROR("80002", "提现类型输入错误"),
	EMPTY_WITHDRAWAL_DETAIL("80003", "请输入提现详情"),

	/**
	 * 用户相关 采用 90000
	 */
	ADDR_NOT_EXIST("90001", "用户地址不存在"), GOODS_NOT_EXIST("90031", "商品不存在"), GOODS_DETAIL_NOT_EXIST("90031", "物料不存在"),
	GOODS_NOT_ENOUGH("90032", "库存不足"), USER_TYPE_NOT_EXIST("90033", "用户类型非法"),
	RECEIVE_ADDR_NOT_EXIST("90034", "未定位到收货地址"),

	/**
	 * 技师相关 用10000
	 */
	TECHNICIAN_NOT_EXIST("11000", "技师不存在"), TECHNICIAN_NO_ACCOUNT_AMT("11001", "技师资金账户缺失"),
	TECHNICIAN_NO_CONSUL_ORDER("11002", "技师uuid与咨询订单技师uuid不匹配"),
	TECHNICIAN_DRIVING_NO_IMG("11003", "代驾技师驾驶证正反面图片、健康证图片、无犯罪记录图片必填"),
	TECHNICIAN_NO_LEVEL("11004", "请输入技师等级,国家等级鉴定与主机厂认证两者必须至少上传其一"),
	TECHNICIAN_NO_STATE_LEVEL_IMG("11005", "请输入国家等级鉴定证书图片"), TECHNICIAN_NO_HOST_LEVEL_IMG("11007", "请输入主机厂认证证书图片"),
	TECHNICIAN_NO_ANSWER_AMT("11006", "请输入技师问答金额"), RESERVATION_MONEY_ID_ERROR("11007", "技师预约费用值错误"),
	PLATFORM_MONEY_ID_ERROR("11008", "平台服务费用值错误"), CONSULT_ORDER_SNAP_UP_ERROR("11009", "该订单已被其他技师抢单"),
	SUPPORT_ONLY_TECHNICIAN_ERROR("11010", "该订单仅支持技师抢单"),

	/**
	 * dtc订单
	 */
	DTC_ORDER_PURCHASE("12001", "请购买此DTC故障信息！"),

	/**
	 * 现场下单订单
	 */
	SCENE_ORDER_ONESELF_ERROR("13001", "此订单为本人订单"),

	/**
	 * 课程教育
	 */
	COURSE_ORDER_PURCHASED("14001", "该课程已购买"),

	/**
	 * 团购
	 */
	INCONSISTENT_DELIVERY_METHODS("15001", "团中的商品配送方式不一致"),;

	private String value;
	private String desc;

	ResEnum(String value, String desc) {
		this.value = value;
		this.desc = desc;
	}

}
