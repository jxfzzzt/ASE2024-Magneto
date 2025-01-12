package com.car.account.web.service.store.impl;

import com.alibaba.fastjson.JSON;
import com.car.account.client.request.store.UpdateStoreAccountReq;
import com.car.account.web.mapper.store.StoreAccountMapper;
import com.car.account.web.model.store.StoreAccount;
import com.car.account.web.service.store.StoreAccountService;
import com.car.common.enums.ResEnum;
import com.car.common.exception.BusinessException;
import com.car.common.res.ResultRes;
import com.car.common.utils.DigitUtils;
import com.car.common.utils.TokenHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Intellij IDEA.
 *
 * @author:  cjw
 * Date:  2021/2/24
 */
@Slf4j
@Service
public class StoreAccountServiceImpl implements StoreAccountService {

    @Autowired
    private StoreAccountMapper storeAccountMapper;
    /**
     * 修改店铺账户信息
     * @param req
     * @return
     */
    @Override
    public ResultRes<String> updateStoreAccount(UpdateStoreAccountReq req) {
        //查询店铺账户信息
        StoreAccount storeAccount = storeAccountMapper.selectStoreAccount(req.getStoreUuid());

        if (null == storeAccount) {
            log.error("未查询到相关店铺账户信息，店铺uuid：{}", req.getStoreUuid());
            throw new BusinessException(ResEnum.NON_EXISTENT);
        }
       // log.error("storeAccount，"+storeAccount.toString());
        //账户现有余额
        BigDecimal accountAmount = DigitUtils.add(storeAccount.getAccountAmount(), req.getOrderAmount());
        //历史累计金额
        BigDecimal totalAmount = DigitUtils.add(storeAccount.getAccountAmount(), req.getOrderAmount());
        //待体现累计金额
        BigDecimal waitAmount = DigitUtils.add(storeAccount.getWaitAmount(), req.getOrderAmount());

        StoreAccount updateStoreAccount = new StoreAccount();
        updateStoreAccount.setStoreUuid(req.getStoreUuid());
        updateStoreAccount.setAccountAmount(accountAmount);
        updateStoreAccount.setTotalAmount(totalAmount);
        updateStoreAccount.setWaitAmount(waitAmount);
        updateStoreAccount.setLastUpdatedBy(TokenHelper.getUserName());
        updateStoreAccount.setLastUpdatedTime(new Date());
        int updateNum = storeAccountMapper.updateStoreAccount(updateStoreAccount);
        if (updateNum <= 0) {
            log.error("修改店铺账户失败，请求参数为：{}", JSON.toJSONString(updateStoreAccount));
            throw new BusinessException(ResEnum.UPDATE_DB_ERROR);
        }
        return ResultRes.success(storeAccount.getUuid());
    }
}
