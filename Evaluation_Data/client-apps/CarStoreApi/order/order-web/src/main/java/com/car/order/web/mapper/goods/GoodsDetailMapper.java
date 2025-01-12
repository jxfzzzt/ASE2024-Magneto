package com.car.order.web.mapper.goods;

import com.car.order.web.model.goods.GoodsDetail;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface GoodsDetailMapper extends Mapper<GoodsDetail> {

    int disableGoodsDetailByGoodsId(@Param("goodsId") String goodsId, @Param("userId") String userId);

    /**
     * 查询商品明细列表
     * @param goodsId
     * @return
     */
    List<GoodsDetail> queryListByGoodsId(@Param("goodsId") String goodsId);

}