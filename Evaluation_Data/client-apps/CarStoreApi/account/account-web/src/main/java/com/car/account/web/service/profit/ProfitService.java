package com.car.account.web.service.profit;

import com.car.account.client.request.profit.AddProfitReq;
import com.car.account.client.response.account.AccountRes;
import com.car.account.client.response.account.QueryQuizCaseCarCountRes;
import com.car.account.client.response.profit.AccountAmtRes;
import com.car.common.res.ResultRes;

/**
 * @author zhangyp
 * @date 2021/1/27 21:42
 */
public interface ProfitService {

    /**
     * 查询账户信息
     * @return
     */
    ResultRes<AccountRes> queryAccount();

    /**
     * 用户资金账户
     * @param params
     * @return
     */
    ResultRes<AccountAmtRes> queryProfitClassify();


    /**
     * 查询我的提问、案例、车辆数量
     * @return
     */
    ResultRes<QueryQuizCaseCarCountRes> queryQuizCaseCarCount();

    /**
     * 添加账户流水
     * @param addProfitReq
     * @return
     */
    ResultRes<String> addProfit(AddProfitReq addProfitReq);



}
