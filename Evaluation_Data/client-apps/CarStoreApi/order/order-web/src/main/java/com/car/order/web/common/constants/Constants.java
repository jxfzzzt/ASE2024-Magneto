package com.car.order.web.common.constants;


/**
 * @author
 * @since 2020-10-22
 */
public class Constants {

    /**
     * 图片限制大小2M
     */
    public static final Long PERSON_IMAGE_SIZE_LIMIT = 2 * 1024 * 1024L;

    public static String ORDER_GOODS_INFO_EXPORT_TEMPLATE = "config/excel/goods/订单管理-商品记录_导出.xlsx";

    public static String ORDER_DRIVING_INFO_EXPORT_TEMPLATE = "config/excel/driving/订单管理-代驾记录_导出.xlsx";

    public static String ORDER_INSTANCE_INFO_EXPORT_TEMPLATE = "config/excel/instance/订单管理-案例记录_导出.xlsx";

    public static String ORDER_CONSULT_INFO_EXPORT_TEMPLATE = "config/excel/consult/订单管理-咨询记录_导出.xlsx";

    public static String ORDER_DTC_INFO_EXPORT_TEMPLATE = "config/excel/dtc/订单管理-DTC故障_导出.xlsx";

    public static String ORDER_COURSE_INFO_EXPORT_TEMPLATE = "config/excel/course/订单管理-课程订单_导出.xlsx";
    /**
     * 技师案例内容审核拼接
     */
    public static final String TECHNICIAN_CASE_CONTENT_CHECK_KEY = "故障现象:%s <br> 诊断过程与思路:%s <br> 结论总结:%s";

    /**
     * 价格默认值
     */
    public static final String TWO_BIG_DECIMAL = "0.00";

    /**
     * 支付创建订单时，商品描述
     */
    public static final String PAY_GOODS_DESC = "嘟一家线上商品购买";

    /**
     * 支付创建订单时，共享技师，商品名称
     */
    public static final String PAY_SHARE_TECHNICIAN_NAME = "共享技师";

    /**
     * 支付创建订单时，商品描述
     */
    public static final String PAY_SCENE_NAME = "线上下单";


    /**
     * 咨询平台佣金
     */
    public static final String CONSULT_COMMISSION = "consult_commission";

    /**
     * 旁听平台佣金
     */
    public static final String AUDIT_COMMISSION = "audit_commission";

    /**
     * 案例平台佣金
     */
    public static final String CASE_COMMISSION = "case_commission";

    /**
     * 技师昵称后缀
     */
    public static final String TECHNICIAN_NICK_NAME = "师傅";

    /**
     * 车主昵称后缀
     */
    public static final String VEHICLE_NICK_NAME = "车主";

    /**
     * goods_parent表共享工位二级uuid
     */
    public static final String SHARE_STATION_UUID = "1006001";

    /**
     * 共享工位平台服务费
     */
    public static final String SHARE_STATION_SERVICE_MONEY = "share_station_service_money";

    /**
     * 共享技师平台服务费
     */
    public static final String PLATFORM_MONEY_TYPE = "platform_money_type";

}
