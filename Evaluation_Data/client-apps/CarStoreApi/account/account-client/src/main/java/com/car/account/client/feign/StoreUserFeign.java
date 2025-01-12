package com.car.account.client.feign;

import com.car.account.client.response.store.StoreUserRes;
import com.car.common.res.ResultRes;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Created by Intellij IDEA.
 *
 * @author:  cjw
 * Date:  2021/2/19
 */
@FeignClient(value = "account")
public interface StoreUserFeign {


    /**
     * 查询店铺联系人详情
     * @param storeUserUuid
     * @return
     */
    @GetMapping(value = "/store/queryStoreUserInfo/{storeUserUuid}")
    ResultRes<StoreUserRes> queryStoreUserInfo(@PathVariable(name = "storeUserUuid") String storeUserUuid);
}
