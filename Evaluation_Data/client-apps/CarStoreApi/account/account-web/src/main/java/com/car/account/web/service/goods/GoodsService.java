package com.car.account.web.service.goods;

import com.car.account.client.request.goods.*;
import com.car.account.client.response.goods.CalGoodsRes;
import com.car.account.client.response.goods.GoodsRes;
import com.car.account.client.response.goods.sub.GoodsDetailRes;
import com.car.common.res.PageRes;
import com.car.common.res.ResultRes;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author zhouz
 * @date 2020/12/22
 */
public interface GoodsService {

    /**
     * 添加商品
     * @param addGoodsReq
     * @return
     */
    ResultRes<String> addGoods(AddGoodsReq addGoodsReq);

    /**
     * 删除商品
     * @param goodId 商品唯一标识
     * @return
     */
    ResultRes<String> deleteGoods(String goodId);

    /**
     * 修改商品
     * @param updateGoodsReq
     * @return
     */
    ResultRes<GoodsRes> updateGoods(UpdateGoodsReq updateGoodsReq);

    /**
     * 查询商品列表
     * @param param
     * @return
     */
    PageRes<List<GoodsRes>> queryGoodsList(QueryGoodsListReq param);

    /**
     * 查询商品信息
     * @param uuid 商品主键
     * @return
     */
    ResultRes<GoodsRes> queryGoods(String uuid);


    /**
     * 商品信息导出
     * @param exportReq
     * @param response
     */
    void exportGoodsList(QueryGoodsListReq exportReq, HttpServletResponse response);

    /**
     * 计算商品订单信息
     * @param params
     * @return
     */
    CalGoodsRes calGoods(CalGoodsReq params);

}
