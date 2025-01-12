package com.car.order.web.service.maintain.impl;

import com.alibaba.fastjson.JSON;
import com.car.common.enums.CheckStatusEnum;
import com.car.common.enums.ResEnum;
import com.car.common.enums.StsEnum;
import com.car.common.exception.BusinessException;
import com.car.common.res.PageRes;
import com.car.common.res.ResultRes;
import com.car.common.utils.TokenHelper;
import com.car.common.utils.UuidUtils;
import com.car.order.client.request.dtc.CheckDtcReq;
import com.car.order.client.request.maintain.AddMaintainReq;
import com.car.order.client.request.maintain.QueryMaintainListReq;
import com.car.order.client.response.dtc.QueryDtcListRes;
import com.car.order.client.response.maintain.QueryMaintainRes;
import com.car.order.web.mapper.maintain.MaintainMapper;
import com.car.order.web.model.dtc.Dtc;
import com.car.order.web.model.maintain.Maintain;
import com.car.order.web.service.maintain.MaintainService;
import com.codingapi.txlcn.tc.annotation.TxcTransaction;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @program: car-service
 * @description: 养护管理
 * @author: niushuaixiang
 * @create: 2021-03-18 19:12
 */
@Slf4j
@Service
public class MaintainServiceImpl implements MaintainService {
    @Autowired
    MaintainMapper maintainMapper;

    @Override
    public ResultRes<QueryMaintainRes> getById(String uuid) {
        if (StringUtils.isEmpty(uuid)) {
            log.error("uuid不能为空");
            throw new BusinessException(ResEnum.LACK_PARAMETER);
        }

        return ResultRes.success(maintainMapper.getById(uuid));
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    @TxcTransaction
    public ResultRes<String> add(AddMaintainReq req) {
        String userUuid = TokenHelper.getUserUuid();
        Maintain maintain = new Maintain();

        maintain.setUuid(UuidUtils.getUuid());
        maintain.setAttachSys(req.getAttachSys());
        maintain.setBrandUuid(req.getBrandUuid());
        maintain.setMaintainCheckSts(req.getMaintainCheckSts());
        maintain.setMaintainContent(req.getMaintainContent());
        maintain.setMaintainCover(req.getMaintainCover());
        maintain.setMaintainTitle(req.getMaintainTitle());
        maintain.setCarModelUuid(req.getCarModelUuid());
        maintain.setIssuerUuid(userUuid);
        maintain.setLastUpdatedTime(new Date());
        maintain.setLastUpdatedBy(TokenHelper.getUserName());
        maintain.setSts(StsEnum.ACTIVE.getValue());
        maintain.setCreatedTime(new Date());
        maintain.setMaintainCheckSts(CheckStatusEnum.CHECK_PENDING.getValue());
        maintain.setCreatedBy(TokenHelper.getUserName());
        int insertDtcNum = maintainMapper.insert(maintain);
        if (insertDtcNum <= 0) {
            log.error("新增dtc故障信息失败，请求参数为：{}", JSON.toJSONString(maintain));
            throw new BusinessException(ResEnum.INSERT_DB_ERROR);
        }
        return ResultRes.success(maintain.getUuid());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @TxcTransaction
    public ResultRes<String> updateById(AddMaintainReq maintainReq, String uuid) {
        if (StringUtils.isEmpty(uuid)) {
            log.error("修改dtc信息，uuid不能为空");
            throw new BusinessException(ResEnum.LACK_PARAMETER);
        }
        Maintain updateMaintain = new Maintain();

        //用户名称
        String userName = TokenHelper.getUserName();
        updateMaintain.setAttachSys(maintainReq.getAttachSys());
        updateMaintain.setBrandUuid(maintainReq.getBrandUuid());
        updateMaintain.setMaintainCheckSts(maintainReq.getMaintainCheckSts());
        updateMaintain.setMaintainContent(maintainReq.getMaintainContent());
        updateMaintain.setMaintainCover(maintainReq.getMaintainCover());
        updateMaintain.setMaintainTitle(maintainReq.getMaintainTitle());
        updateMaintain.setMaintainCheckSts(maintainReq.getMaintainCheckSts());
        updateMaintain.setCarModelUuid(maintainReq.getCarModelUuid());
        updateMaintain.setMaintainRemarks(maintainReq.getMaintainRemarks());
        updateMaintain.setUuid(uuid);
        updateMaintain.setLastUpdatedTime(new Date());
        updateMaintain.setLastUpdatedBy(userName);

        //修改dtc相关信息
        int dtcUpdateNum = maintainMapper.updateByPrimaryKeySelective(updateMaintain);
        if (dtcUpdateNum <= 0) {
            log.error("修改dtc故障信息失败，请求参数为：{}, uuid:{}", JSON.toJSONString(maintainMapper), uuid);
            throw new BusinessException(ResEnum.UPDATE_DB_ERROR);
        }
        return ResultRes.success(uuid);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @TxcTransaction
    public ResultRes<String> deleteById(String uuid) {
        if (StringUtils.isEmpty(uuid)) {
            log.error("删除dtc信息，uuid不能为空！");
            throw new BusinessException(ResEnum.LACK_PARAMETER);
        }

        //用户信息姓名
        String userName = TokenHelper.getUserName();
        //删除dtc信息
        int deleteDtcNum = maintainMapper.deleteMaintainInfo(uuid, userName);
        if (deleteDtcNum <= 0) {
            log.error("删除dtc信息失败");
            throw new BusinessException(ResEnum.PAY_ERROR);
        }
        return ResultRes.success();
    }

    @Override
    public PageRes<List<QueryMaintainRes>> list(QueryMaintainListReq req) {
        PageHelper.startPage(req.getPageNum(), req.getPageSize());
        List<QueryMaintainRes> list = maintainMapper.queryMaintainList(req);
        PageInfo<QueryMaintainRes> pageInfo = new PageInfo<>(list);
        return PageRes.success(list, pageInfo.getPageSize(), (int) pageInfo.getTotal(), pageInfo.getPages());
    }


}