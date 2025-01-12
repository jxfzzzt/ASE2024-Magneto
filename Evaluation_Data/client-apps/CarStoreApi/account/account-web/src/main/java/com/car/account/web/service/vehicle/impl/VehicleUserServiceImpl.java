package com.car.account.web.service.vehicle.impl;


import com.car.account.client.enums.vehicle.VehicleUserTypeEnum;
import com.car.account.client.request.vehicle.VehicleReq;
import com.car.account.client.request.vehicle.VehicleUserListReq;
import com.car.account.client.request.vehicle.VehicleUserReq;
import com.car.account.client.response.vehicle.vehicleUser.QueryOrderAmountRes;
import com.car.account.client.response.vehicle.vehicleUser.QueryVehicleUserCountRes;
import com.car.account.client.response.vehicle.vehicleUser.VehicleRes;
import com.car.account.client.response.vehicle.vehicleUser.VehicleUserRes;
import com.car.account.web.common.constants.VehicleConstants;
import com.car.account.web.mapper.vehicle.VehicleMapper;
import com.car.account.web.mapper.vehicle.VehicleUserMapper;
import com.car.account.web.model.vehicle.Vehicle;
import com.car.account.web.model.vehicle.VehicleUser;
import com.car.account.web.service.person.PersonService;
import com.car.account.web.service.vehicle.VehicleService;
import com.car.account.web.service.vehicle.VehicleUserService;
import com.car.common.enums.CheckStatusEnum;
import com.car.common.enums.ResEnum;
import com.car.common.enums.StsEnum;
import com.car.common.exception.BusinessException;
import com.car.common.res.PageRes;
import com.car.common.res.ResultRes;
import com.car.common.utils.DateUtil;
import com.car.common.utils.ExcelUtils;
import com.car.common.utils.ExceptionUtils;
import com.car.common.utils.TokenHelper;
import com.codingapi.txlcn.tc.annotation.TxcTransaction;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * @author xlj
 */
@Slf4j
@Service
public class VehicleUserServiceImpl implements VehicleUserService {

    @Autowired
    VehicleUserMapper vehicleUserMapper;

    @Autowired
    VehicleMapper vehicleMapper;

    @Autowired
    VehicleService vehicleService;

    @Autowired
    private PersonService personService;

    /**
     * 查询用户数（注册用户/车主数）
     * @return
     */
    @Override
    public ResultRes<QueryVehicleUserCountRes> queryVehicleUserCount() {
        QueryVehicleUserCountRes res = new QueryVehicleUserCountRes();
        //查询注册用户数
        VehicleUser user = new VehicleUser();
        user.setSts(StsEnum.ACTIVE.getValue());
        user.setUserType(VehicleUserTypeEnum.REGISTER.getValue());
        res.setRegisterCount(vehicleUserMapper.selectCount(user));
        //查询车主数
        user.setUserType(VehicleUserTypeEnum.OWNER.getValue());
        res.setOwnerCount(vehicleUserMapper.selectCount(user));
        return ResultRes.success(res);
    }

    /**
     * 车主信息列表
     * @param param
     * @return
     */
    @Override
    public PageRes<List<VehicleUserRes>> queryList(VehicleUserListReq param) {
        PageHelper.startPage(param.getPageNum(), param.getPageSize());
        List<VehicleUser> userList = vehicleUserMapper.queryList(param);
        PageInfo<VehicleUser> pageInfo = new PageInfo<>(userList);
        //对象转化
        List<VehicleUserRes> resList = convertToRes(userList);
        return PageRes.success(resList,pageInfo.getPageSize(),(int) pageInfo.getTotal(),pageInfo.getPages());
    }

    /**
     * 查询登陆车主的详情信息
     * @return
     */
    @Override
    public ResultRes<VehicleUserRes> queryDetailByToken() {
        return queryDetail(TokenHelper.getUserUuid());
    }

    /**
     * 查询车主详情
     * @param uuid
     * @return
     */
    @Override
    public ResultRes<VehicleUserRes> queryDetail(String uuid) {
        VehicleUser vehicleUser = vehicleUserMapper.selectByPrimaryKey(uuid);
        if(StringUtils.isEmpty(vehicleUser)){
            throw new BusinessException(ResEnum.NON_EXISTENT);
        }
        VehicleUserRes vehicleUserRes = new VehicleUserRes();
        BeanUtils.copyProperties(vehicleUser,vehicleUserRes);
        //查询当前用户名下车辆信息
        List<Vehicle> vehicleList = vehicleMapper.queryListByUser(vehicleUser.getUuid());
        List<VehicleRes> vehicleResList = new ArrayList<>();
        for (Vehicle vehicle : vehicleList) {
            VehicleRes vehicleRes = new VehicleRes();
            BeanUtils.copyProperties(vehicle,vehicleRes);
            vehicleResList.add(vehicleRes);
        }
        vehicleUserRes.setVehicleList(vehicleResList);
        return ResultRes.success(vehicleUserRes);
    }

    /**
     * 编辑车主信息
     * @param param
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @TxcTransaction
    public ResultRes editVehicleUser(VehicleUserReq param) {
        VehicleUser vehicleUser = vehicleUserMapper.selectByPrimaryKey(param.getUuid());
        if(StringUtils.isEmpty(vehicleUser)){
            throw new BusinessException(ResEnum.NON_EXISTENT);
        }
        //验证手机号码是否存在
        boolean mobileFlag = checkMobile(param.getUuid(),param.getMobile());
        if(!mobileFlag){
            throw new BusinessException(ResEnum.MOBILE_EXIST_ERROR);
        }
        //验证证件号码是否存在
        boolean certificateFlag = checkCertificateNum(param.getUuid(),param.getCertificateNum());
        if(!certificateFlag){
            throw new BusinessException(ResEnum.ID_CARD_EXIST_ERROR);
        }
        //判断是否修改了手机号，如果修改了手机号，删除之前token
        if (!(vehicleUser.getMobile().equals(param.getMobile()))) {
            personService.exitLoginByUserUuid(param.getUuid());
        }
        //同步修改用户编辑后的值
        updateVehicleUser(vehicleUser,param);
        //同步修改用户车辆信息
        updateBoundCar(vehicleUser,param);
        return ResultRes.success(vehicleUser.getUuid());
    }

    /**
     * 车主信息导出
     * @param exportReq
     * @param response
     */
    @Override
    public void exportVehicleList(VehicleUserListReq exportReq, HttpServletResponse response) {
        try{
            List<VehicleUser> userList = vehicleUserMapper.queryList(exportReq);
            //对象转化
            List<VehicleUserRes> resList = convertToRes(userList);
            //读取模板文件
            InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream(VehicleConstants.VEHICLE_USER_EXPORT_TEMPLATE);
            //设置空行默认属性
            List<VehicleUserRes> excelList = ExcelUtils.setFieldValue(resList);
            Workbook wb = new XSSFWorkbook(resourceAsStream);
            Sheet sheet = wb.getSheetAt(0);
            //从第三行开始写入
            int firstRowIndex = sheet.getFirstRowNum()+2;
            for (int rowIndex = firstRowIndex; rowIndex < excelList.size()+2; rowIndex++) {
                //行样式
                Row rowStyle = (rowIndex % 2) == 0?sheet.getRow(2): sheet.getRow(3);
                //单列样式
                CellStyle cellStyle = ExcelUtils.getExcelFormat(rowStyle.getCell(1));
                CellStyle cellStyle1 = ExcelUtils.getExcelFormat(rowStyle.getCell(0));
                Row row = sheet.getRow(rowIndex);
                if(row == null){
                    row = sheet.createRow(rowIndex);
                }
                row.setHeight(rowStyle.getHeight());
                VehicleUserRes exportDto = excelList.get(rowIndex - 2);
                ExcelUtils.setCell(row,cellStyle1,0,rowIndex-1);
                ExcelUtils.setCell(row,cellStyle,1,exportDto.getUserName());
                ExcelUtils.setCell(row,cellStyle,2,exportDto.getMobile());
                ExcelUtils.setCell(row,cellStyle,3,exportDto.getAddressProvinceName());
                ExcelUtils.setCell(row,cellStyle,4,exportDto.getAddressCityName());
                ExcelUtils.setCell(row,cellStyle,5,exportDto.getAddressDetail());
                ExcelUtils.setCell(row,cellStyle,6,exportDto.getVehicleList().size());
                //车牌号
                StringBuffer plateNumbers = new StringBuffer();
                StringBuffer vehicleBrandNames = new StringBuffer();
                StringBuffer vehicleModelNames =new StringBuffer();
                StringBuffer licenseRegisterDates = new StringBuffer();
                StringBuffer emissionLevelsNames = new StringBuffer();
                StringBuffer fuelTypeNames = new StringBuffer();
                List<VehicleRes> vehicleResList = exportDto.getVehicleList();
                for (VehicleRes vehicle : vehicleResList) {
                    plateNumbers.append(vehicle.getPlateNumber()).append("/");
                    vehicleBrandNames.append(vehicle.getVehicleBrandName()).append("/");
                    vehicleModelNames.append(vehicle.getVehicleModelName()).append("/");
                    licenseRegisterDates.append(vehicle.getLicenseRegisterDate()).append("/");
                    emissionLevelsNames.append(vehicle.getEmissionLevelsName()).append("/");
                    fuelTypeNames.append(vehicle.getFuelTypeName()).append("/");
                }
                ExcelUtils.setCell(row,cellStyle,7,plateNumbers.toString());
                ExcelUtils.setCell(row,cellStyle,8,vehicleBrandNames.toString());
                ExcelUtils.setCell(row,cellStyle,9,vehicleModelNames.toString());
                ExcelUtils.setCell(row,cellStyle,10,licenseRegisterDates.toString());
                ExcelUtils.setCell(row,cellStyle,11,emissionLevelsNames.toString());
                ExcelUtils.setCell(row,cellStyle,12,fuelTypeNames.toString());
                ExcelUtils.setCell(row,cellStyle,13, DateUtil.dateToStr(exportDto.getCreatedTime(), DateUtil.YYYY_MM_DD_HH_MM_SS));
            }
            ExcelUtils.responseWrite(wb,response, VehicleConstants.VEHICLE_USER_EXPORT_TEMPLATE);
        }catch (Exception ex){
            log.error("车主信息导出异常，异常原因：{}", ExceptionUtils.stackTraceToString(ex));
        }
    }

    /**
     * 转化成输出对象
     * @param userList
     * @return
     */
    private List<VehicleUserRes> convertToRes(List<VehicleUser> userList){
        List<VehicleUserRes> resList = new ArrayList<>();
        for (VehicleUser user : userList) {
            VehicleUserRes vehicleUserRes = new VehicleUserRes();
            BeanUtils.copyProperties(user,vehicleUserRes);
            //查询当前用户名下车辆信息
            List<Vehicle> vehicleList = vehicleMapper.queryListByUser(user.getUuid());
            List<VehicleRes> vehicleResList = new ArrayList<>();
            for (Vehicle vehicle : vehicleList) {
                VehicleRes vehicleRes = new VehicleRes();
                BeanUtils.copyProperties(vehicle,vehicleRes);
                vehicleResList.add(vehicleRes);
            }
            vehicleUserRes.setVehicleList(vehicleResList);
            resList.add(vehicleUserRes);
        }
        return resList;
    }

    /**
     * 修改车辆信息
     * @param vehicleUser
     * @param param
     */
    private void updateBoundCar(VehicleUser vehicleUser,VehicleUserReq param){
        List<VehicleReq> vehicleList = param.getVehicleList();
        for (VehicleReq vehicleReq : vehicleList ) {
            //根据ID查询车辆信息
            Vehicle vehicle = vehicleMapper.selectByPrimaryKey(vehicleReq.getUuid());
            if(StringUtils.isEmpty(vehicle)){
                log.error("根据车辆ID：{}未匹配到车辆信息",vehicleReq.getUuid());
                throw new BusinessException(ResEnum.NON_EXISTENT);
            }
            //判断当前车辆是否属于当前车主，防止有人非法篡改数据
            if(!vehicle.getVehicleUserUuid().equals(vehicleUser.getUuid())){
                log.error("当前车辆ID：{}不属于用户ID:{}，系统抛出异常",vehicle.getUuid(),vehicleUser.getUuid());
                throw new BusinessException(ResEnum.VEHICLE_CROSS_RIGHTS_ERROR);
            }
            //检查车牌是否重复
            boolean plateNumberFlag = vehicleService.checkPlateNumber(vehicle.getUuid(),vehicleReq.getPlateNumber());
            if(!plateNumberFlag){
                throw new BusinessException(ResEnum.PLATE_NUMBER_EXIST_ERROR);
            }
            //更新车辆属性信息
            this.updateVehicle(vehicle,vehicleReq);
            BeanUtils.copyProperties(param,vehicle);
            vehicleMapper.updateByPrimaryKey(vehicle);
        }
    }

    /**
     * 更新车辆属性信息
     * @param vehicle
     * @param vehicleReq
     */
    private void updateVehicle(Vehicle vehicle, VehicleReq vehicleReq) {
        BeanUtils.copyProperties(vehicleReq,vehicle);
        vehicle.setLastUpdatedBy(TokenHelper.getUserName());
        vehicle.setLastUpdatedTime(new Date());
        vehicleMapper.updateByPrimaryKey(vehicle);
    }

    /**
     * 更新车主属性信息
     * @param vehicleUser
     * @param param
     */
    private void updateVehicleUser(VehicleUser vehicleUser,VehicleUserReq param){
        vehicleUser.setAccountName(param.getAccountName());
        vehicleUser.setAddressCity(param.getAddressCity());
        vehicleUser.setAddressDetail(param.getAddressDetail());
        vehicleUser.setAddressProvince(param.getAddressProvince());
        vehicleUser.setAlipayAccount(param.getAlipayAccount());
        vehicleUser.setCardNumbers(param.getCardNumbers());
        vehicleUser.setCertificateNum(param.getCertificateNum());
        vehicleUser.setCertificateType(param.getCertificateType());
        vehicleUser.setDepositBank(param.getDepositBank());
        vehicleUser.setMobile(param.getMobile());
        vehicleUser.setSubBranchName(param.getSubBranchName());
        vehicleUser.setUserName(param.getUserName());
        vehicleUser.setLastUpdatedBy(TokenHelper.getUserName());
        vehicleUser.setLastUpdatedTime(new Date());
        vehicleUserMapper.updateByPrimaryKey(vehicleUser);
    }



    /**
     * 验证证件号码是否存在
     * @param uuid
     * @return
     */
    private boolean checkCertificateNum(String uuid,String certificateNum) {
        //根据手机号码查询有效的用户信息
        VehicleUser search = new VehicleUser();
        search.setCertificateNum(certificateNum);
        search.setSts(StsEnum.ACTIVE.getValue());
        VehicleUser user = vehicleUserMapper.selectOne(search);
        return checkUserIsExist(uuid,user);
    }

    /**
     * 验证手机号码是否存在
     * @param uuid
     * @return
     */
    private boolean checkMobile(String uuid,String mobile) {
        //根据手机号码查询有效的用户信息
        VehicleUser search = new VehicleUser();
        search.setMobile(mobile);
        search.setSts(StsEnum.ACTIVE.getValue());
        VehicleUser user = vehicleUserMapper.selectOne(search);
        return checkUserIsExist(uuid,user);
    }

    /**
     * 检查用户是否存在
     * @param uuid
     * @param user
     * @return
     */
    private boolean checkUserIsExist(String uuid,VehicleUser user){
        if(!StringUtils.isEmpty(user)){
            if(StringUtils.isEmpty(uuid)){
                //返回已存在用户
                return false;
            }else{
                //存在用户，判断是否为当前用户ID
                if(!user.getUuid().equals(uuid)){
                    //返回重复状态
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 查询车主订单金额简要信息
     * @param uuid
     * @return
     */
    @Override
    public ResultRes<QueryOrderAmountRes> queryOrderAmount(String uuid) {
        //TODO 后续补充该逻辑，用于管理平台->车主管理->车主详情
        QueryOrderAmountRes res = new QueryOrderAmountRes();
        res.setOrderCount(5);
        res.setPayTotalMoney("7431.52");
        res.setRemainingMoney("52.66");
        return ResultRes.success(res);
    }
}
