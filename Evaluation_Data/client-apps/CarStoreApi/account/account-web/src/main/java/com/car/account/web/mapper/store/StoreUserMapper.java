package com.car.account.web.mapper.store;

import com.car.account.client.request.store.StoreUserReq;
import com.car.account.client.response.store.StoreUserRes;
import com.car.account.web.model.store.StoreUser;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author zhouz
 * @date 2020/12/19
 */
@Repository
public interface StoreUserMapper extends Mapper<StoreUser> {

    /**
     * 批量新增店铺相关联系人
     * @param storeUserList
     */
    void batchInsertStoreUser(@Param("insertList") List<StoreUser> storeUserList);

    /**
     * 删除店铺时逻辑删除店铺相关联系人
     * @param storeUuid
     */
    int deleteStoreUser(String storeUuid);

    /**
     * 修改店铺信息时根据店铺uuid物理删除店铺相关联系人
     * @param storeUuid
     */
    void deleteStoreUserByStoreUuid(String storeUuid);

    /**
     * 批量删除店铺联系人用户信息
     * @param storeUserUuidList
     * @return
     */
    int batchDeleteStoreUser(@Param("storeUserUuidList") List<String> storeUserUuidList);

    /**
     * 批量修改店铺联系人信息
     * @param storeUser
     * @param userName
     * @return
     */
    int batchUpdateStoreUser(@Param("storeUser") StoreUserReq storeUser, @Param("userName") String userName);


    /**
     *  根据店铺联系人uuid查询店铺联系人详情
     * @param storeUserUuid
     * @return
     */
    StoreUserRes getById(@Param("storeUserUuid") String storeUserUuid);

    /**
     * 根据店铺uuid查询该店铺所有联系人uuid
     * @param storeUuid
     * @return
     */
    List<String> queryStoreUserUuid(@Param("storeUuid") String storeUuid);
}
