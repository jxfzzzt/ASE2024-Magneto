package com.car.account.web.service.vehicle.impl;


import com.car.account.client.enums.vehicle.VehicleUserTypeEnum;
import com.car.account.client.request.vehicle.VehicleReq;
import com.car.account.client.response.consult.ConsultRes;
import com.car.account.client.response.vehicle.vehicleUser.VehicleRes;
import com.car.account.web.mapper.vehicle.VehicleMapper;
import com.car.account.web.mapper.vehicle.VehicleUserMapper;
import com.car.account.web.model.vehicle.Vehicle;
import com.car.account.web.model.vehicle.VehicleUser;
import com.car.account.web.service.vehicle.VehicleService;
import com.car.common.enums.ResEnum;
import com.car.common.enums.StsEnum;
import com.car.common.exception.BusinessException;
import com.car.common.req.PageReq;
import com.car.common.res.PageRes;
import com.car.common.res.ResultRes;
import com.car.common.utils.TokenHelper;
import com.car.common.utils.UuidUtils;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * @author xlj
 */
@Slf4j
@Service
public class VehicleServiceImpl implements VehicleService {


    @Autowired
    VehicleMapper vehicleMapper;

    @Autowired
    VehicleUserMapper vehicleUserMapper;


    /**
     * 查询我的车辆列表
     * @param
     * @return
     */
    @Override
    public ResultRes<List<VehicleRes>> queryMyVehicle() {
        List<Vehicle> vehicleList = vehicleMapper.queryListByUser(TokenHelper.getUserUuid());
        List<VehicleRes> vehicleResList = new ArrayList<>();
        for (Vehicle vehicle : vehicleList) {
            VehicleRes vehicleRes = new VehicleRes();
            BeanUtils.copyProperties(vehicle,vehicleRes);
            vehicleResList.add(vehicleRes);
        }
        return ResultRes.success(vehicleResList);
    }

    /**
     * 查询我的车辆详情
     * @param vehicleUuid
     * @return
     */
    @Override
    public ResultRes<VehicleRes> queryVehicleDetail(String vehicleUuid) {
        Vehicle vehicle = vehicleMapper.selectByPrimaryKey(vehicleUuid);
        if(StringUtils.isEmpty(vehicle)){
            throw new BusinessException(ResEnum.NON_EXISTENT);
        }
        VehicleRes vehicleRes = new VehicleRes();
        BeanUtils.copyProperties(vehicle,vehicleRes);
        return ResultRes.success(vehicleRes);
    }

    /**
     * 添加用户车辆信息
     * @param param
     * @return
     */
    @Override
    public ResultRes<String> addMyVehicle(VehicleReq param) {
        //检查车牌号是否存在
        if(!checkPlateNumber(null,param.getPlateNumber())){
            throw new BusinessException(ResEnum.PLATE_NUMBER_EXIST_ERROR);
        }
        //新增车辆信息入库
        Vehicle vehicle = new Vehicle();
        BeanUtils.copyProperties(param,vehicle);
        vehicle.setUuid(UuidUtils.getUuid());
        vehicle.setVehicleUserUuid(TokenHelper.getUserUuid());
        vehicle.setSts(StsEnum.ACTIVE.getValue());
        vehicle.setCreatedBy(TokenHelper.getUserName());
        vehicle.setCreatedTime(new Date());
        vehicleMapper.insert(vehicle);

        //将用户身份更新成为车主
        VehicleUser user = vehicleUserMapper.selectByPrimaryKey(TokenHelper.getUserUuid());
        if(VehicleUserTypeEnum.REGISTER.getValue().equals(user.getUserType())){
            user.setUserType(VehicleUserTypeEnum.OWNER.getValue());
            user.setLastUpdatedTime(new Date());
            vehicleUserMapper.updateByPrimaryKey(user);
        }
        return ResultRes.success(vehicle.getUuid());
    }

    /**
     * 检查车牌是否重复
     * @param vehicleUuid
     * @param plateNumber
     * @return
     */
    @Override
    public boolean checkPlateNumber(String vehicleUuid, String plateNumber) {
        Vehicle search = new Vehicle();
        search.setPlateNumber(plateNumber);
        search.setSts(StsEnum.ACTIVE.getValue());
        Vehicle vehicle1 = vehicleMapper.selectOne(search);
        if(!StringUtils.isEmpty(vehicle1)){
            if(StringUtils.isEmpty(vehicleUuid)){
                return false;
            }else{
                if(!vehicle1.getUuid().equals(vehicleUuid)){
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 编辑用户车辆信息
     * @param param
     * @return
     */
    @Override
    public ResultRes<String> editMyVehicle(VehicleReq param) {
        //根据ID查询车辆信息
        Vehicle vehicle = vehicleMapper.selectByPrimaryKey(param.getUuid());
        if(StringUtils.isEmpty(vehicle)){
            log.error("根据车辆ID：{}未匹配到车辆信息",param.getUuid());
            throw new BusinessException(ResEnum.NON_EXISTENT);
        }
        //判断当前车辆是否属于当前车主，防止有人非法篡改数据
        if(!vehicle.getVehicleUserUuid().equals(TokenHelper.getUserUuid())){
            log.error("当前车辆ID：{}不属于用户ID:{}，系统抛出异常",vehicle.getUuid(),TokenHelper.getUserUuid());
            throw new BusinessException(ResEnum.VEHICLE_CROSS_RIGHTS_ERROR);
        }
        //检查车牌是否重复
        boolean plateNumberFlag = this.checkPlateNumber(vehicle.getUuid(),param.getPlateNumber());
        if(!plateNumberFlag){
            throw new BusinessException(ResEnum.PLATE_NUMBER_EXIST_ERROR);
        }
        //验证通过，同步修改车辆信息
        BeanUtils.copyProperties(param,vehicle);
        vehicle.setLastUpdatedBy(TokenHelper.getUserName());
        vehicle.setLastUpdatedTime(new Date());
        vehicleMapper.updateByPrimaryKey(vehicle);
        return ResultRes.success(vehicle.getUuid());
    }

    /**
     * 删除我的车辆信息
     * @param vehicleUuid
     * @return
     */
    @Override
    public ResultRes<String> deleteMyVehicle(String vehicleUuid) {
        //根据ID查询车辆信息
        Vehicle vehicle = vehicleMapper.selectByPrimaryKey(vehicleUuid);
        if(StringUtils.isEmpty(vehicle)){
            log.error("根据车辆ID：{}未匹配到车辆信息",vehicleUuid);
            throw new BusinessException(ResEnum.NON_EXISTENT);
        }
        //判断当前车辆是否属于当前车主，防止有人非法篡改数据
        if(!vehicle.getVehicleUserUuid().equals(TokenHelper.getUserUuid())){
            log.error("当前车辆ID：{}不属于用户ID:{}，系统抛出异常",vehicle.getUuid(),TokenHelper.getUserUuid());
            throw new BusinessException(ResEnum.VEHICLE_CROSS_RIGHTS_ERROR);
        }
        vehicle.setSts(StsEnum.INVALID.getValue());
        vehicle.setLastUpdatedBy(TokenHelper.getUserName());
        vehicle.setLastUpdatedTime(new Date());
        vehicleMapper.updateByPrimaryKey(vehicle);
        return ResultRes.success(vehicleUuid);
    }

    @Override
    public PageRes<List<ConsultRes>> queryConsultList(PageReq pageReq) {

        String userUuid = TokenHelper.getUserUuid();
        Integer pageNum = pageReq.getPageNum();
        Integer pageSize = pageReq.getPageSize();

        PageHelper.startPage(pageNum,pageSize);


        return null;
    }
}
