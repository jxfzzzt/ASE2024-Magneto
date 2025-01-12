package com.car.order.web.common.constants;

/**
 * @author
 * @since 2020-10-22
 */
public class TechnicianConstants {

	/**
	 * 技师导出模板
	 */
	public static String TECHNICIAN_EXPORT_TEMPLATE = "config/excel/technician/技师管理-技师记录_导出.xlsx";

	/**
	 * 车主端查询可被咨询的技师列表 截取姓名第一个字符拼接字符串"师傅"
	 */
	public static String TECHNICIAN_JOINT_STRING = "师傅";

	/**
	 * 全国技师提问金额 字典表uuid
	 */
	public static String TECHNICIAN_CONSULT_AMOUNT_DICT_UUID = "6006";

	/**
	 * 车友邦提问：普通 技师提问金额 字典表uuid
	 */
	public static String TECHNICIAN_CYB_GEN_AMOUNT_DICT_UUID = "5101";
	/**
	 * 车友邦提问：专家 技师提问金额 字典表uuid
	 */
	public static String TECHNICIAN_CYB_EXP_AMOUNT_DICT_UUID = "5102";

}
