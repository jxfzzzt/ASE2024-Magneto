package com.car.account.web.mapper.store;

import com.car.account.web.model.store.StoreBrand;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author zhouz
 * @date 2021/1/20
 */
@Repository
public interface StoreBrandMapper extends Mapper<StoreBrand> {

    /**
     * 根据店铺uuid查询绑定的品牌列表
     * @param storeUuid
     * @return
     */
    List<String> queryBrandByStoreUuid(String storeUuid);

}
