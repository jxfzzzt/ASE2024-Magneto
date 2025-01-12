package com.car.account.web.mapper.store;

import com.car.account.client.response.store.QueryAreaStoreListRes;
import com.car.account.client.response.store.StoreRelateDetailRes;
import com.car.account.client.response.store.StoreTechnicianRelateDetailRes;
import com.car.account.client.response.store.StoreTechnicianRelateListRes;
import com.car.account.web.model.store.StoreTechnicianRelate;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author zhouz
 * @date 2021/1/24
 */
@Repository
public interface StoreTechnicianRelateMapper extends Mapper<StoreTechnicianRelate> {

    /**
     * 查询店铺技师关联列表
     * @param storeUuid
     * @return
     */
    List<StoreTechnicianRelateListRes> queryStoreTechnicianRelateList(String storeUuid );

    /**
     * 根据区域查询店铺列表
     * @param areaId
     * @return
     */
    List<QueryAreaStoreListRes> queryStoreListByArea(@Param("areaId") String areaId );

    /**
     * 查询技师关联店铺详情
     * @return
     */
    StoreRelateDetailRes queryStoreTechnicianRelateDetailRes(String technicianUuid);
}
