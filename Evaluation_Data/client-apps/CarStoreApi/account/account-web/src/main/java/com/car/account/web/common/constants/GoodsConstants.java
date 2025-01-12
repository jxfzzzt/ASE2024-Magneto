package com.car.account.web.common.constants;


/**
 * @author
 * @since 2020-10-22
 */
public class GoodsConstants {

    /**
     * 图片限制大小2M
     */
    public static final Long PERSON_IMAGE_SIZE_LIMIT = 2 * 1024 * 1024L;

    /**
     * 商品导出模板
     */
    public static String GOODS_INFO_EXPORT_TEMPLATE= "config/excel/goods/商品管理-商品列表_导出.xlsx";

    /**
     * 查询当前父节点下所有分组
     */
    public static final String GOODS_PARENT_QUERY_LIST_BY_PARENT_CACHE_KEY = "goodsParent:queryListByParent:cacheKey:%s";
    /**
     * 查询所有父节点信息缓存Key
     */
    public static final String GOODS_PARENT_QUERY_LIST_CACHE_KEY = "goodsParent:queryList:cacheKey";

}
