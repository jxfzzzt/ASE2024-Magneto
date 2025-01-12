package com.car.account.web.mapper.withdrawal;

import com.car.account.client.response.withdrawal.WithdrawalDetailRes;
import com.car.account.web.model.store.StoreUser;
import com.car.account.web.model.withdrawal.WithdrawalDetail;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author zhouz
 * @date 2020/12/26
 */
@Repository
public interface WithdrawalDetailMapper extends Mapper<WithdrawalDetail> {

    /**
     * 批量新增提现详情
     * @param withdrawalDetailList
     */
    void batchInsertWithdrawalDetail(@Param("insertList") List<WithdrawalDetail> withdrawalDetailList);

    /**
     * 查询提现与待入账金额分类详情
     * @param withdrawalUuid
     * @return
     */
    List<WithdrawalDetailRes> queryWithdrawalDetailByWithdrawalUuid(String withdrawalUuid);

    /**
     * 删除提现详情
     * @param withdrawalUuid
     * @return
     */
    int deleteWithdrawalDetail(String withdrawalUuid);
}
