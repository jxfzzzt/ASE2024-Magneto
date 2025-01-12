package com.car.account.web.service.store;

import com.car.account.client.request.store.*;
import com.car.account.client.response.comment.CommentStaticsRes;
import com.car.account.client.response.goods.ext.StoreGoodsClassifyRes;
import com.car.account.client.response.store.*;
import com.car.account.web.model.store.Store;
import com.car.common.res.PageRes;
import com.car.common.res.ResultRes;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author zhouz
 * @date 2020/12/19
 */
public interface StoreService {

    /**
     * 添加店铺
     * @param addStoreReq
     * @return
     */
    ResultRes<String> addStore(AddStoreReq addStoreReq);

    /**
     * 删除店铺
     * @param uuid
     * @return
     */
    ResultRes<String> deleteStore(String uuid);

    /**
     * 修改店铺
     * @param updateStoreReq
     * @return
     */
    ResultRes<String> updateStore(UpdateStoreReq updateStoreReq);

    /**
     * 修改店铺账户信息
     * @param storeAccountReq
     * @returns
     */
    ResultRes<String> updateStoreAccount(StoreAccountReq storeAccountReq);


    /**
     * 查询店铺列表
     * @param param
     * @return
     */
    PageRes<List<QueryStoreListRes>> queryStoreList(QueryStoreListReq param);

    /**
     * 查询店铺详情
     * @param uuid
     * @return
     */
    ResultRes<StoreDetailRes> queryStoreDetail(String uuid);

    /**
     * 根据token查询店铺详情
     * @param
     * @return
     */
    ResultRes<StoreDetailRes> queryStoreDetail();

    /**
     * 店铺信息导出
     * @param exportReq
     * @param response
     */
    void exportStoreList(QueryStoreListReq exportReq, HttpServletResponse response);

    /**
     * 查询店铺服务种类
     * @param storeUuid
     * @return
     */
    List<String> queryStoreServices(String storeUuid);

    /**
     * 店铺评分统计
     * @param storeUuid
     * @return
     */
    CommentStaticsRes queryStoreCommentStatics(String storeUuid);
    /**
     * 根据登录人获取店铺信息
     * @return
     */
    Store getStore();


    /**
     * 查询店铺商品分类及所有商品列表
     * @param storeUuid
     * @return
     */
    List<StoreGoodsClassifyRes> queryStoreGoodsClassifyRes(String storeUuid);

    /**
     * 查询店铺账户信息
     * @return
     */
    ResultRes<StoreAccountRes> queryStoreAccount();

    /**
     * 根据店铺联系人uuid查询店铺联系人详情
     * @param storeUserUuid
     * @return
     */
    ResultRes<StoreUserRes> queryStoreUserInfo(String storeUserUuid);

    /**
     * 查询共享店铺列表
     * @return
     */
    ResultRes<List<QueryShareStoreListRes>> queryShareStoreList(QueryShareStoreListReq param);
}
