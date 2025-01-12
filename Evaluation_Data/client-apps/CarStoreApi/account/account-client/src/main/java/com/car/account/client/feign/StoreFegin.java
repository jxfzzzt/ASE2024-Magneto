package com.car.account.client.feign;

import com.car.account.client.request.store.UpdateStoreAccountReq;
import com.car.account.client.response.store.StoreDetailRes;
import com.car.common.res.ResultRes;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * @author zhangyp
 * @date 2021/1/21 21:39
 */
@FeignClient(value = "account")
public interface StoreFegin {

    /**
     * 根据登录用户token查询店铺详情
     * @return
     */
    @GetMapping(value = "/store/queryStoreDetailByUser")
    ResultRes<StoreDetailRes> queryStoreDetailByUser();

    /**
     * 查询店铺详情
     * @param uuid
     * @return
     */
    @GetMapping(value = "/store/queryStoreDetail/{uuid}")
    ResultRes<StoreDetailRes> queryStoreDetail(@PathVariable(name = "uuid") String uuid);

    /**
     * 修改店铺账户信息
     * @param req
     * @return
     */
    @PostMapping(value = "/storeAccount/updateStoreAccount")
    ResultRes<String> updateStoreAccount(@RequestBody @Valid UpdateStoreAccountReq req);
}
