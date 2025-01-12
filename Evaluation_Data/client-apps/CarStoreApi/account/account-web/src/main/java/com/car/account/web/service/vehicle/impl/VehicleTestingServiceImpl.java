package com.car.account.web.service.vehicle.impl;

import com.alibaba.fastjson.JSON;
import com.car.account.client.request.vehicle.AddVehicleTestingReq;
import com.car.account.client.request.vehicle.VehicleTestingDetailReq;
import com.car.account.client.response.vehicle.vehicleUser.QueryVehicleTesting;
import com.car.account.web.mapper.vehicle.VehicleTestingMapper;
import com.car.account.web.model.vehicle.VehicleTesting;
import com.car.account.web.service.vehicle.VehicleTestingService;
import com.car.common.enums.ResEnum;
import com.car.common.enums.StsEnum;
import com.car.common.exception.BusinessException;
import com.car.common.res.ResultRes;
import com.car.common.utils.TokenHelper;
import com.car.common.utils.UuidUtils;
import com.codingapi.txlcn.tc.annotation.TxcTransaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @program: car-service
 * @description:
 * @author: niushuaixiang
 * @create: 2021-04-08 19:16
 */
@Slf4j
@Service
public class VehicleTestingServiceImpl implements VehicleTestingService {
    @Autowired
    VehicleTestingMapper vehicleTestingMapper;


    @Override
    @Transactional(rollbackFor = Exception.class)
    @TxcTransaction
    public ResultRes<String> addVehicleTesting(AddVehicleTestingReq addVehicleTestingRes) {
        List<VehicleTestingDetailReq> vehicleTestingDetailReqs = addVehicleTestingRes.getList();
        if (CollectionUtils.isEmpty(vehicleTestingDetailReqs)) {
            log.error("内容缺失");
            throw new BusinessException(ResEnum.ERROR_GOODS_EMPTY);
        }
        String userName = TokenHelper.getUserName();
        String uuid = UuidUtils.getUuid();
        VehicleTesting vehicleTesting = new VehicleTesting();
        vehicleTesting.setUuid(uuid);
        vehicleTesting.setVehicleUserId(addVehicleTestingRes.getVehicleUserId());
        vehicleTesting.setTechnicianUuid(TokenHelper.getUserUuid());
        vehicleTesting.setRemarks(addVehicleTestingRes.getRemarks());
        vehicleTesting.setContent(JSON.toJSONString(addVehicleTestingRes.getList()));
        vehicleTesting.setSts(StsEnum.ACTIVE.getValue());
        vehicleTesting.setCreatedBy(userName);
        vehicleTesting.setCreatedTime(new Date());
        vehicleTestingMapper.insert(vehicleTesting);
        return ResultRes.success(vehicleTesting.getUuid());
    }

    @Override
    public ResultRes<QueryVehicleTesting> queryVehicleTesting() {

        VehicleTesting vehicleTesting = vehicleTestingMapper.queryVehicleTesting();

        if(vehicleTesting == null ) {
            return ResultRes.success();
        }
        QueryVehicleTesting queryVehicleTesting = new QueryVehicleTesting();
        BeanUtils.copyProperties(vehicleTesting, queryVehicleTesting);

        return ResultRes.success(queryVehicleTesting);
    }
}