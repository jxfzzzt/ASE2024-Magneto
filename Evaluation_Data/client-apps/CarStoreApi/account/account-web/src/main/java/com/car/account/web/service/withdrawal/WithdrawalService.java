package com.car.account.web.service.withdrawal;

import com.car.account.client.request.goods.AddGoodsReq;
import com.car.account.client.request.goods.UpdateGoodsReq;
import com.car.account.client.request.withdrawal.AddWithdrawalReq;
import com.car.account.client.request.withdrawal.QueryWithdrawalListReq;
import com.car.account.client.request.withdrawal.UpdateWithdrawalReq;
import com.car.account.client.response.withdrawal.QueryWithdrawalListRes;
import com.car.account.client.response.withdrawal.WithdrawalRes;
import com.car.common.res.PageRes;
import com.car.common.res.ResultRes;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author zhouz
 * @date 2020/12/26
 */
public interface WithdrawalService {

    /**
     * 添加提现
     * @param addWithdrawalReq
     * @return
     */
    ResultRes<String> addWithdrawal(AddWithdrawalReq addWithdrawalReq);


    /**
     * 删除提现记录
     * @param uuid
     * @return
     */
    ResultRes<String> deleteWithdrawal(String uuid);
    /**
     * 提现审核
     * @param updateWithdrawalReq
     * @return
     */
    ResultRes<String> checkWithdrawal(UpdateWithdrawalReq updateWithdrawalReq);

    /**
     * 查询提现列表
     * @param param
     * @return
     */
    PageRes<List<QueryWithdrawalListRes>> queryWithdrawalList(QueryWithdrawalListReq param);
    /**
     * 查询提现详情
     * @param uuid
     * @return
     */
    ResultRes<WithdrawalRes> queryWithdrawalDetailByUuid(String uuid);

    /**
     * 提现信息导出
     * @param exportReq
     * @param response
     */
    void exportWithdrawalList(QueryWithdrawalListReq exportReq, HttpServletResponse response);
}
