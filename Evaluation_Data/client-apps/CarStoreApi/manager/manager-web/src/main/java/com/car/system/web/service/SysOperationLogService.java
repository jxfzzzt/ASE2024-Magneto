package com.car.system.web.service;


import com.car.common.req.SysOperationLogReq;
import com.car.common.res.PageRes;
import com.car.common.res.ResultRes;
import com.car.system.client.request.log.QueryLogListReq;
import com.car.system.client.response.log.LogRes;

import java.util.List;

/**
 * @author
 * @date 2020/5/26 16:33
 */
public interface SysOperationLogService {

    /**
     * 查询操作日志
     * @param param
     * @return
     */
    PageRes<List<LogRes>> queryLogList(QueryLogListReq param);

    /**
     * 保存系统操作日志
     * @param log
     * @return
     */
    ResultRes<String> insertSysLog(SysOperationLogReq log);
}
