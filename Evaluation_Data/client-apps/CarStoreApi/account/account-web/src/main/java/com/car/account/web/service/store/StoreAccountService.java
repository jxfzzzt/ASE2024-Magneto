package com.car.account.web.service.store;

import com.car.account.client.request.store.UpdateStoreAccountReq;
import com.car.common.res.ResultRes;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * Created by Intellij IDEA.
 *
 * @author:  cjw
 * Date:  2021/2/24
 */
public interface StoreAccountService {


    /**
     * 修改店铺账户信息
     * @param req
     * @return
     */
    ResultRes<String> updateStoreAccount(UpdateStoreAccountReq req);
}
