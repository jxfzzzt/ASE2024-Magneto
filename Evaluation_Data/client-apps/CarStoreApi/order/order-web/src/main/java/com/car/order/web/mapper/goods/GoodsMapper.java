package com.car.order.web.mapper.goods;

import com.car.order.web.model.goods.Goods;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

/**
 * @author zhouz
 * @date 2020/12/22
 */
@Repository
public interface GoodsMapper extends Mapper<Goods> {


    /**
     * 根据店铺uuid查询店铺是否存在
     * @param uuid
     * @return
     */
    Integer queryStoreIsExistByUuid(String uuid);
}
