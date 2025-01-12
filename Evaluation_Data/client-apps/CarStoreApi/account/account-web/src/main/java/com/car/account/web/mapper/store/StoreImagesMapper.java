package com.car.account.web.mapper.store;

import com.car.account.web.model.store.StoreImages;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author zhouz
 * @date 2020/12/19
 */
@Repository
public interface StoreImagesMapper extends Mapper<StoreImages> {

    /**
     * 批量新增店铺相关图片
     * @param insertList
     */
    void batchInsertStoreImages(@Param("insertList") List<StoreImages> insertList);

    /**
     * 修改时物理删除店铺相关图片
     * @param storeUuid
     * @param imageType
     */
    void deleteStoreImagesByParam(@Param("storeUuid") String storeUuid, @Param("imageType") Integer imageType);

    /**
     * 删除店铺时逻辑删除图片
     * @param storeUuid
     */
    void deleteStoreImagesByStoreUuid(@Param("storeUuid") String storeUuid);
}
