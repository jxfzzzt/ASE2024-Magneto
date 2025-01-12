package com.car.account.web.service.store;

import com.car.account.client.request.store.*;
import com.car.account.client.response.store.QueryAreaStoreListRes;
import com.car.account.client.response.store.StoreTechnicianRelateDetailRes;
import com.car.account.client.response.store.StoreTechnicianRelateListRes;
import com.car.common.res.PageRes;
import com.car.common.res.ResultRes;

import java.util.List;

/**
 * @author zhouz
 * @date 2021/1/24
 */
public interface StoreTechnicianRelateService {

    /**
     * 技师申请关联店铺
     * @param addStoreTechnicianRelateReq
     * @return
     */
    ResultRes<Boolean> addTechnicianToStore(AddStoreTechnicianRelateReq addStoreTechnicianRelateReq);

    /**
     * 店铺审核技师关联申请
     * @param checkStoreTechnicianRelateReq
     * @return
     */
    ResultRes<Boolean> checkStoreTechnicianRelate(CheckStoreTechnicianRelateReq checkStoreTechnicianRelateReq);

    /**
     * 店铺审核技师关联申请
     * @param uuid
     * @return
     */
    ResultRes<Boolean> storeUnbindTechnician(String uuid);

    /**
     * 查询技师关联店铺详情
     * @return
     */
    ResultRes<StoreTechnicianRelateDetailRes> queryStoreTechnicianRelateDetail();

    /**
     * 查询店铺技师关联列表
     * @param queryStoreTechnicianRelateListReq
     * @return
     */
    PageRes<List<StoreTechnicianRelateListRes>> queryStoreTechnicianRelateList(QueryStoreTechnicianRelateListReq queryStoreTechnicianRelateListReq);

    /**
     * 根据区域查询店铺列表
     * * @param queryAreaStoreListReq
     * @return
     */
    PageRes<List<QueryAreaStoreListRes>> queryStoreListByArea(QueryAreaStoreListReq queryAreaStoreListReq);
}
