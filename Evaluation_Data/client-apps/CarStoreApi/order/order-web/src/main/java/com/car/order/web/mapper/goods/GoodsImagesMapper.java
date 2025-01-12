package com.car.order.web.mapper.goods;

import com.car.order.web.model.goods.GoodsDetail;
import com.car.order.web.model.goods.GoodsImages;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author zhouz
 * @date 2020/12/22
 */
@Repository
public interface GoodsImagesMapper extends Mapper<GoodsImages> {

    /**
     * 批量新增商品相关图片
     * @param insertList
     */
    void batchInsertGoodsImages(@Param("insertList") List<GoodsImages> insertList);

    /**
     * 删除商品时逻辑删除商品相关图片
     * @param goodsUuid 商品唯一标识
     * @param userId 操作人
     * @return
     */
    int deleteGoodsImages(@Param("goodsUuid") String goodsUuid, @Param("userId") String userId);

    /**
     * 查询商品图片列表
     * @param goodsId
     * @return
     */
    List<GoodsImages> queryListByGoodsId(@Param("goodsId") String goodsId);

}
