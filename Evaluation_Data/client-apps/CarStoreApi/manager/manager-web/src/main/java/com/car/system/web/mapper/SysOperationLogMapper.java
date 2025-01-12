package com.car.system.web.mapper;



import com.car.common.datasource.model.SysOperationLog;
import com.car.system.client.request.log.QueryLogListReq;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface SysOperationLogMapper extends Mapper<SysOperationLog> {

    /**
     * 查询系统操作日志记录
     * @param param
     * @return
     */
    List<SysOperationLog> queryLogList(QueryLogListReq param);

    /**
     * 根据ID集合查询List信息
     * @param logList
     * @return
     */
    List<SysOperationLog> queryLogDetailListByUuidList(@Param("logList") List<SysOperationLog> logList);
}
