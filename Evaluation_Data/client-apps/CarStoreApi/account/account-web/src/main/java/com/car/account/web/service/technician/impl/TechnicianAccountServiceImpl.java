package com.car.account.web.service.technician.impl;

import com.alibaba.fastjson.JSON;
import com.car.account.client.request.technician.AddAndUpdateTechnicianAccountReq;
import com.car.account.client.request.technician.UpdateTechnicianAccountReq;
import com.car.account.client.response.technician.TechnicianAccountRes;
import com.car.account.web.mapper.technician.TechnicianAccountMapper;
import com.car.account.web.model.technician.TechnicianAccount;
import com.car.account.web.service.technician.TechnicianAccountService;
import com.car.common.enums.ResEnum;
import com.car.common.enums.StsEnum;
import com.car.common.exception.BusinessException;
import com.car.common.res.ResultRes;
import com.car.common.utils.DigitUtils;
import com.car.common.utils.TokenHelper;
import com.car.common.utils.UuidUtils;
import com.codingapi.txlcn.tc.annotation.TxcTransaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Intellij IDEA.
 *
 * @author:  cjw
 * Date:  2021/2/1
 */
@Slf4j
@Service
public class TechnicianAccountServiceImpl implements TechnicianAccountService {

    @Autowired
    private TechnicianAccountMapper technicianAccountMapper;

    /**
     * 查询技师账户信息
     * @return
     */
    @Override
    public ResultRes<TechnicianAccountRes> queryTechnicianAccountInfo() {
        //获取用户uuid
        String userUuid = TokenHelper.getUserUuid();
        TechnicianAccountRes technicianAccountRes = technicianAccountMapper.queryTechnicianAccountInfo(userUuid);
        return ResultRes.success(technicianAccountRes);
    }

    /**
     * 新增/修改账户信息
     * @param req
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @TxcTransaction
    public ResultRes<String> addAndUpdateTechnicianAccount(AddAndUpdateTechnicianAccountReq req) {
        String userUuid = TokenHelper.getUserUuid();
        String userName = TokenHelper.getUserName();

        TechnicianAccount technicianAccount = new TechnicianAccount();
        technicianAccount.setTechnicianUuid(userUuid);
        technicianAccount.setSts(StsEnum.ACTIVE.getValue());
        technicianAccount = technicianAccountMapper.selectOne(technicianAccount);

        if (StringUtils.isEmpty(technicianAccount)) {
            //新增
            technicianAccount = new TechnicianAccount();
            technicianAccount.setTechnicianUuid(userUuid);
            BeanUtils.copyProperties(req,technicianAccount);
            technicianAccount.setUuid(UuidUtils.getUuid());
            technicianAccount.setSts(StsEnum.ACTIVE.getValue());
            technicianAccount.setCreatedBy(userName);
            technicianAccount.setCreatedTime(new Date());
            int addNum = technicianAccountMapper.insert(technicianAccount);
            if (addNum <= 0) {
                log.error("新增技师账户信息失败，参数为：{}", JSON.toJSONString(technicianAccount));
                throw new BusinessException(ResEnum.INSERT_DB_ERROR);
            }
        } else {
            //修改
            BeanUtils.copyProperties(req,technicianAccount);
            technicianAccount.setLastUpdatedBy(userName);
            technicianAccount.setLastUpdatedTime(new Date());
            int updateNum = technicianAccountMapper.updateByPrimaryKeySelective(technicianAccount);
            if (updateNum <= 0) {
                log.error("修改技师账户信息失败，参数为：{}", JSON.toJSONString(technicianAccount));
                throw new BusinessException(ResEnum.UPDATE_DB_ERROR);
            }
        }
        return ResultRes.success(technicianAccount.getUuid());
    }

    /**
     *修改技师账户信息
     * @param req
     */
    @Override
    public ResultRes<String> updateTechnicianAccount(UpdateTechnicianAccountReq req) {
        //查询技师账户信息
        TechnicianAccountRes technicianAccountRes = technicianAccountMapper.queryTechnicianAccountInfo(req.getTechnicianUuid());
        if (null == technicianAccountRes) {
            log.error("未查询到相关技师账户信息");
            throw new BusinessException(ResEnum.NON_EXISTENT);
        }
        //账户现有余额
        BigDecimal accountAmount = DigitUtils.add(technicianAccountRes.getAccountAmount(), req.getOrderAmount());
        //账户累计余额
        BigDecimal totalAmount = DigitUtils.add(technicianAccountRes.getTotalAmount(), req.getOrderAmount());
        //待提现累计金额
        BigDecimal waitAmount = DigitUtils.add(technicianAccountRes.getWaitAmount(), req.getOrderAmount());
        TechnicianAccount technicianAccount = new TechnicianAccount();
        technicianAccount.setTechnicianUuid(req.getTechnicianUuid());
        technicianAccount.setAccountAmount(accountAmount);
        technicianAccount.setTotalAmount(totalAmount);
        technicianAccount.setWaitAmount(waitAmount);
        technicianAccount.setLastUpdatedTime(new Date());
        technicianAccount.setLastUpdatedBy(TokenHelper.getUserName());
        int updateNum = technicianAccountMapper.updateTechnicianAccount(technicianAccount);
        if (updateNum <= 0) {
            log.error("修改技师账户信息失败，修改参数为：{}", JSON.toJSONString(technicianAccount));
            throw new BusinessException(ResEnum.UPDATE_DB_ERROR);
        }
        return ResultRes.success(technicianAccountRes.getUuid());
    }
}
