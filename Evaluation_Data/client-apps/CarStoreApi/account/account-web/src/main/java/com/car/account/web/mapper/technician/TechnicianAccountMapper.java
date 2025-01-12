package com.car.account.web.mapper.technician;

import com.car.account.client.response.technician.TechnicianAccountRes;
import com.car.account.web.model.technician.TechnicianAccount;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;


/**
 * @author xlj
 */
@Repository
public interface TechnicianAccountMapper extends Mapper<TechnicianAccount> {


    /**
     * 查询技师账户信息详情
     * @param userUuid
     * @return
     */
    TechnicianAccountRes queryTechnicianAccountInfo(@Param("userUuid") String userUuid);

    /**
     * 修改技师订单技师信息
     * @param technicianAccount
     * @return
     */
    int updateTechnicianAccount(@Param("technicianAccount") TechnicianAccount technicianAccount);
}
