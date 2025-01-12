package com.car.account.web.mapper.withdrawal;

import com.car.account.client.request.withdrawal.QueryWithdrawalListReq;
import com.car.account.client.response.withdrawal.QueryWithdrawalListRes;
import com.car.account.client.response.withdrawal.WithdrawalRes;
import com.car.account.web.model.withdrawal.Withdrawal;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author zhouz
 * @date 2020/12/26
 */
@Repository
public interface WithdrawalMapper extends Mapper<Withdrawal> {


    /**
     * 查询提现列表
     * @param param
     * @return
     */
    List<QueryWithdrawalListRes> queryWithdrawalList(QueryWithdrawalListReq param);
    /**
     * 店铺提现详情
     * @param uuid
     * @return
     */
    WithdrawalRes queryWithdrawalDetailByUuid(String uuid);
   /* *//**
     * 店铺提现详情
     * @param uuid
     * @return
     *//*
    WithdrawalRes queryStoreWithdrawalByUuid(String uuid);

    *//**
     * 技师提现详情
     * @param uuid
     * @return
     *//*
    WithdrawalRes queryTechnicianWithdrawalByUuid(String uuid);*/

}
