package com.car.account.web.mapper.goods;

import com.car.account.web.model.goods.GoodsDetail;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;
@Repository
public interface GoodsDetailMapper extends Mapper<GoodsDetail> {

    int disableGoodsDetailByGoodsId(@Param("goodsId") String goodsId,@Param("userId") String userId);
}