package com.car.system.web.service.impl;

import com.car.common.req.SysOperationLogReq;
import com.car.system.web.service.SysOperationLogService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.car.common.datasource.model.SysOperationLog;
import com.car.common.enums.StsEnum;
import com.car.common.res.PageRes;
import com.car.common.res.ResultRes;
import com.car.common.utils.UuidUtils;
import com.car.system.client.request.log.QueryLogListReq;
import com.car.system.client.response.log.LogRes;
import com.car.system.web.mapper.SysOperationLogMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class SysOperationLogServiceImpl implements SysOperationLogService {

    @Autowired
    SysOperationLogMapper sysOperationLogMapper;

    /**
     * 查询系统操作日志记录
     * @param param
     * @return
     */
    @Override
    public PageRes<List<LogRes>> queryLogList(QueryLogListReq param) {
        PageHelper.startPage(param.getPageNum(), param.getPageSize());
        //只查询对应的主键ID数据，减少检索性能。
        List<SysOperationLog> logList = sysOperationLogMapper.queryLogList(param);
        PageInfo<SysOperationLog> pageInfo = new PageInfo<>(logList);
        List<LogRes> logResList = new ArrayList<LogRes>();
        if(!CollectionUtils.isEmpty(logList)){
            //通过分页主键ID查询指定数据信息
            List<SysOperationLog> operationList = sysOperationLogMapper.queryLogDetailListByUuidList(logList);
            for (SysOperationLog log: operationList) {
                LogRes res = new LogRes();
                BeanUtils.copyProperties(log,res);
                logResList.add(res);
            }
        }
        return PageRes.success(logResList,pageInfo.getPageSize(),(int) pageInfo.getTotal(),pageInfo.getPages());
    }

    /**
     * 保存系统操作日志
     * @param param
     * @return
     */
    @Override
    public ResultRes<String> insertSysLog(SysOperationLogReq param) {
        SysOperationLog sysOperationLog = new SysOperationLog();
        BeanUtils.copyProperties(param,sysOperationLog);
        sysOperationLog.setUuid(UuidUtils.getUuid());
        sysOperationLog.setSts(StsEnum.ACTIVE.getValue());
        sysOperationLog.setCreatedTime(new Date());
        sysOperationLogMapper.insert(sysOperationLog);
        return ResultRes.success();
    }
}
