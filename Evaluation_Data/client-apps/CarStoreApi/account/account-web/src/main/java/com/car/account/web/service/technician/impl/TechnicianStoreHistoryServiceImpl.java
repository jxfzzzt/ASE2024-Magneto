package com.car.account.web.service.technician.impl;


import com.car.account.client.response.storehistory.QueryStoreHistoryRes;
import com.car.account.web.mapper.technician.TechnicianStoreHistoryMapper;
import com.car.account.web.service.technician.TechnicianStoreHistoryService;
import com.car.common.res.ResultRes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;


/**
 * @author xlj
 */
@Slf4j
@Service
public class TechnicianStoreHistoryServiceImpl implements TechnicianStoreHistoryService {

    @Autowired
    TechnicianStoreHistoryMapper technicianStoreHistoryMapper;

    /**
     * 查询技师关联店铺历史记录信息
     * @param technicianUuid
     * @return
     */
    @Override
    public ResultRes<List<QueryStoreHistoryRes>> queryStoreHistory(String technicianUuid) {
        return ResultRes.success(technicianStoreHistoryMapper.queryStoreHistory(technicianUuid));
    }

    /**
     * 查询技师最近关联店铺
     * @param technicianUuid
     * @return
     */
    @Override
    public ResultRes<QueryStoreHistoryRes> queryLastStoreHistory(String technicianUuid) {
        List<QueryStoreHistoryRes> historyList = technicianStoreHistoryMapper.queryStoreHistory(technicianUuid);
        if(CollectionUtils.isEmpty(historyList)){
            return ResultRes.success();
        }
        return ResultRes.success(historyList.get(0));
    }
}
