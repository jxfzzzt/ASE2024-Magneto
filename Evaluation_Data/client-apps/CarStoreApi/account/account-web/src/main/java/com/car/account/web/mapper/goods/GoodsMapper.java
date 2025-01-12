package com.car.account.web.mapper.goods;

import com.car.account.client.request.goods.QueryGoodsClassifyReq;
import com.car.account.client.request.goods.QueryGoodsListReq;
import com.car.account.client.response.goods.GoodsRes;
import com.car.account.web.model.goods.Goods;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author zhouz
 * @date 2020/12/22
 */
@Repository
public interface GoodsMapper extends Mapper<Goods> {

    /**
     * 查询商品列表
     * @param param
     * @return
     */
    List<GoodsRes> queryGoodsList(QueryGoodsListReq param);

    /**
     * 查询商品分类列表
     * @param param
     * @return
     */
    List<Goods> queryGoodsClassify(QueryGoodsClassifyReq param);

    /**
     * 查询店铺所有的分类
     * @param storeUuid
     * @return
     */
    List<String> selectClassifyByStoreUuid(String storeUuid);
}
