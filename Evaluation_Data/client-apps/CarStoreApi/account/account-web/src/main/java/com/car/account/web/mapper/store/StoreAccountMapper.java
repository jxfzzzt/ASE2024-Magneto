package com.car.account.web.mapper.store;

import com.car.account.web.model.store.StoreAccount;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

/**
 * @author zhouz
 * @date 2020/12/26
 */
@Repository
public interface StoreAccountMapper extends Mapper<StoreAccount> {

    /**
     * 删除店铺时逻辑删除店铺相关账号
     * @param storeUuid
     */
    int deleteStoreAccountByStoreUuid(String storeUuid);


    /**
     * 根据店铺id查询店铺账户信息
     * @param storeUuid
     * @return
     */
    StoreAccount selectStoreAccount(@Param("storeUuid") String storeUuid);

    /**
     *  修改店铺账户信息
     * @param updateStoreAccount
     * @return
     */
    int updateStoreAccount(@Param("updateStoreAccount") StoreAccount updateStoreAccount);
}
