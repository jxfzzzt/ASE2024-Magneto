package com.car.account.web.controller.store;

import com.car.account.client.request.store.AddStoreReq;
import com.car.account.client.request.store.UpdateStoreAccountReq;
import com.car.account.web.service.store.StoreAccountService;
import com.car.common.annotation.SysOperLog;
import com.car.common.enums.OperEnum;
import com.car.common.res.ResultRes;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

/**
 * Created by Intellij IDEA.
 *
 * @author:  cjw
 * Date:  2021/2/24
 */
@Slf4j
@Api(value = "StoreAccountController", tags = "店铺账户管理")
@RequestMapping(value = "/storeAccount", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RestController
public class StoreAccountController {

    @Autowired
    private StoreAccountService storeAccountService;

    @PostMapping(value = "/updateStoreAccount")
    @SysOperLog(operModul = "店铺账户管理", operType = OperEnum.UPDATE, operDesc = "修改店铺账户信息")
    @ApiIgnore
    public ResultRes<String> updateStoreAccount(@RequestBody @Valid UpdateStoreAccountReq req){
        return storeAccountService.updateStoreAccount(req);
    }
}
