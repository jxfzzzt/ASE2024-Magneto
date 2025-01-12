package com.car.account.web.service.person.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.car.account.client.enums.comm.TerminalEnum;
import com.car.account.client.enums.vehicle.VehicleUserTypeEnum;
import com.car.account.client.request.login.LoginReq;
import com.car.account.client.response.login.LoginRes;
import com.car.account.web.common.utils.ConfigConsts;
import com.car.account.web.common.utils.RandomUtil;
import com.car.account.web.mapper.store.StoreMapper;
import com.car.account.web.mapper.store.StoreUserMapper;
import com.car.account.web.mapper.technician.TechnicianMapper;
import com.car.account.web.mapper.vehicle.VehicleUserMapper;
import com.car.account.web.model.store.Store;
import com.car.account.web.model.store.StoreUser;
import com.car.account.web.model.technician.Technician;
import com.car.account.web.model.vehicle.VehicleUser;
import com.car.account.web.service.person.PersonService;
import com.car.common.enums.ResEnum;
import com.car.common.enums.StsEnum;
import com.car.common.enums.UserTypeEnum;
import com.car.common.exception.BusinessException;
import com.car.common.res.ResultRes;
import com.car.common.utils.*;
import com.car.common.utils.token.LoginToken;
import com.car.common.utils.token.TokenUtil;
import com.car.utility.client.feign.SmsFeign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author zhangyp
 * @date 2021/1/16 21:28
 */
@Slf4j
@Service
public class PersonServiceImpl implements PersonService {

    @Resource
    RedisUtils redisUtils;
    @Resource
    private StoreUserMapper storeUserMapper;
    @Resource
    private StoreMapper storeMapper;
    @Resource
    private TechnicianMapper technicianMapper;
    @Resource
    private VehicleUserMapper vehicleUserMapper;
    @Resource
    private SmsFeign smsFeign;

    @Autowired
    ConfigConsts configConsts;

    /**
     * 获取登陆验证码
     * @param accountName
     * @param request
     * @return
     */
    @Override
    public ResultRes getLoginCode(String accountName,String terminal, HttpServletRequest request) {
        if(StringUtils.isEmpty(TerminalEnum.enumOfDesc(terminal))){
            //非指定渠道，禁止读取验证码
            throw new BusinessException(ResEnum.LOGIN_TERMINAL_ERROR);
        }
        //检查当前用户操作频率，频率1分钟超过10次；直接拦截
        checkSendRate(request,"getLoginCode");

        //检查redis中是否已存在发送中的短信
        String cacheKey = String.format(Constants.LOGIN_VERIFICATION_CODE_CACHE_KEY, new Object[] { accountName });
        String redisCode = (String) redisUtils.get(cacheKey);
        if(!StringUtils.isEmpty(redisCode)){
            //提示请勿重复发送验证码
            throw new BusinessException(ResEnum.REPEAT_SEND_MSG_ERROR.getValue(),ResEnum.REPEAT_SEND_MSG_ERROR.getDesc());
        }
        String mobile = null;
        //检查用户名是否存在
        if(TerminalEnum.VEHICLE.getValue().equals(terminal)){
            //检查车主不验证是否已存在，如果不存在，则按照新建的流程。
            mobile = accountName;
        }else{
            //检查店铺/技师用户是否存在
            Technician technician = new Technician();
            technician.setMobile(accountName);
            technician.setSts(StsEnum.ACTIVE.getValue());
            technician = technicianMapper.selectOne(technician);
            if(!StringUtils.isEmpty(technician)){
                mobile = technician .getMobile();
            }else{
                //读取店铺账号信息
                StoreUser store = new StoreUser();
                store.setMobile(accountName);
                store.setSts(StsEnum.ACTIVE.getValue());
                store = storeUserMapper.selectOne(store);
                if(StringUtils.isEmpty(store)){
                    throw new BusinessException(ResEnum.LOGIN_ACCOUNT_ERROR);
                }
                mobile = store .getMobile();
            }
        }
        //读取登陆验证码
        String smsCode = RandomUtil.getRandom(6);
        if(configConsts.getSmsSwitch().equals("close")){
            smsCode = "888888";
        }
        log.info("当前登陆用户手机号：{}，验证码为：{}",mobile,smsCode);
        smsFeign.sendRegister(mobile,smsCode);
        //将验证码防止到redis中，有效期为1分钟
        redisUtils.set(cacheKey,smsCode,(long)Constants.LOGIN_VERIFICATION_CODE_TIME, TimeUnit.MINUTES);
        return ResultRes.success();
    }

    @Override
    public ResultRes<LoginRes> userLogin(LoginReq param, HttpServletRequest request) {
        if(StringUtils.isEmpty(TerminalEnum.enumOfDesc(param.getTerminal()))){
            //非指定渠道
            throw new BusinessException(ResEnum.LOGIN_TERMINAL_ERROR);
        }
        
        log.info("当前登陆参数为：{}",JSONObject.toJSONString(param));
        
        //验证登陆参数
        validUserLogin(param);
        //验证登陆密码是否正确
        String accountName = param.getAccountName();
        LoginRes tokenRes = null;
        if(TerminalEnum.VEHICLE.getValue().equals(param.getTerminal())){
            //检查车主账户是否存在
            VehicleUser vehicle = getVehicleUser(accountName);
            if(StringUtils.isEmpty(vehicle)){
                //创建用户信息
                vehicle = new VehicleUser();
                vehicle.setUuid(UuidUtils.getUuid());
                //默认为注册用户
                vehicle.setUserType(VehicleUserTypeEnum.REGISTER.getValue());
                vehicle.setMobile(accountName);
                vehicle.setSts(StsEnum.ACTIVE.getValue());
                vehicle.setCreatedTime(new Date());
                vehicleUserMapper.insert(vehicle);
            }
            tokenRes = getLoginTokenRes(vehicle.getUuid(),vehicle.getUserName(),vehicle.getMobile(),UserTypeEnum.vehicle.getType());
        }else{
            //检查店铺/技师用户是否存在
            Technician technician = getTechnician(accountName);
            if(!StringUtils.isEmpty(technician)){
                tokenRes = getLoginTokenRes(technician.getUuid(),technician.getUserName(),technician.getMobile(),UserTypeEnum.technician.getType());
                tokenRes.setCheckSts(technician.getCheckSts());
            }else{
                //读取店铺账号信息
                StoreUser storeUser = getStoreUser(accountName);
                if(StringUtils.isEmpty(storeUser)){
                    throw new BusinessException(ResEnum.LOGIN_ACCOUNT_ERROR);
                }
                //读取店铺信息
                Store store = getStoreDetailByUuid(storeUser.getStoreUuid());
                if(StringUtils.isEmpty(store)){
                    throw new BusinessException(ResEnum.STORE_INFO_NOT_BY_USER);
                }
                tokenRes = getLoginTokenRes(storeUser.getUuid(),storeUser.getUserName(),storeUser.getMobile(),UserTypeEnum.store.getType());
                tokenRes.setCheckSts(store.getCheckSts());
            }
        }
        
        log.info("登录完成，返回token：{}",tokenRes.getToken());
        return ResultRes.success(tokenRes);
    }

    /**
     * 根据登陆账号获取店铺信息
     * @param accountName
     * @return
     */
    private StoreUser getStoreUser(String accountName){
        StoreUser store = new StoreUser();
        store.setMobile(accountName);
        store.setSts(StsEnum.ACTIVE.getValue());
        store = storeUserMapper.selectOne(store);
        return store;
    }


    /**
     * 根据登陆账号获取技师信息
     * @param accountName
     * @return
     */
    private Technician getTechnician(String accountName){
        Technician technician = new Technician();
        technician.setMobile(accountName);
        technician.setSts(StsEnum.ACTIVE.getValue());
        technician = technicianMapper.selectOne(technician);
        return technician;
    }

    /**
     * 根据登陆账号获取车主信息
     * @param accountName
     * @return
     */
    private VehicleUser getVehicleUser(String accountName){
        VehicleUser vehicle = new VehicleUser();
        vehicle.setMobile(accountName);
        vehicle.setSts(StsEnum.ACTIVE.getValue());
        vehicle = vehicleUserMapper.selectOne(vehicle);
        return vehicle;
    }

    @Override
    public ResultRes<LoginRes> switchRole() {
        Integer userType = TokenHelper.getLoginToken().getUserType();
        String mobile = TokenHelper.getLoginToken().getUserMobile();
        if(UserTypeEnum.store.getType().equals(userType)){
            //切换技师 校验是否存在技师的角色
            Technician technician = getTechnician(mobile);
            if(StringUtils.isEmpty(technician)){
                throw new BusinessException(ResEnum.TECHNICIAN_NOT_EXIST);
            }
            LoginRes tokenRes  = getLoginTokenRes(technician.getUuid(),technician.getUserName(),TokenHelper.getLoginToken().getUserMobile(),UserTypeEnum.technician.getType());
            tokenRes.setCheckSts(technician.getCheckSts());
            return ResultRes.success(tokenRes);
        }else if(UserTypeEnum.technician.getType().equals(userType)){
            //切换店铺
            StoreUser storeUser = getStoreUser(mobile);
            if(StringUtils.isEmpty(storeUser)){
                throw new BusinessException(ResEnum.STORE_CONTACT_NOT_EXIST);
            }
            //读取店铺信息
            Store store = getStoreDetailByUuid(storeUser.getStoreUuid());
            if(StringUtils.isEmpty(store)){
                throw new BusinessException(ResEnum.STORE_INFO_NOT_BY_USER);
            }

            LoginRes tokenRes  = getLoginTokenRes(storeUser.getUuid(),storeUser.getUserName(),TokenHelper.getLoginToken().getUserMobile(),UserTypeEnum.store.getType());
            tokenRes.setCheckSts(store.getCheckSts());
            return ResultRes.success(tokenRes);
        }else {

            log.error("用户类型不支持切换角色,userType:{}",userType);
            throw new BusinessException(ResEnum.VEHICLE_NOT_SUPPORT_SWITCH);
        }
    }

    /**
     * 用户退出登陆
     * @return
     */
    @Override
    public ResultRes exitLogin() {
        ResultRes res = exitLoginByUserId(TokenHelper.getUserUuid());
        return res;
    }

    /**
     * 根据用户ID批量退出登陆
     * @param userList
     * @return
     */
    @Override
    public ResultRes exitLoginByUserId(List<String> userList) {
        if(!CollectionUtils.isEmpty(userList)){
            userList.stream().forEach(item ->{
                exitLoginByUserId(item);
            });
        }
        return ResultRes.success();
    }

    /**
     * 根据用户uuid退出登录
     * @param userUuid
     * @return
     */
    @Override
    public ResultRes exitLoginByUserUuid(String userUuid) {
        return exitLoginByUserId(userUuid);
    }

    /**
     * 根据店铺ID查询店铺信息
     * @param uuid
     * @return
     */
    public Store getStoreDetailByUuid(String uuid) {
        Store storeDetail = new Store();
        storeDetail.setUuid(uuid);
        storeDetail.setSts(StsEnum.ACTIVE.getValue());
        storeDetail = storeMapper.selectOne(storeDetail);
        return storeDetail;
    }

    /**
     * 根据用户ID退出登陆
     * @param userUuid
     * @return
     */
    private ResultRes exitLoginByUserId(String userUuid){
        LoginToken token = new LoginToken();
        token.setUuid(userUuid);
        // 该token 在 redis中的剩余有效时间
        long remainingTime = redisUtils.getExpireTime(token.cacheKey(), TimeUnit.SECONDS);
        // redis中保存一个登出信息
        String logoutKey = token.logoutCacheKey();
        //执行退出登陆
        redisUtils.remove(token.cacheKey());
        boolean b = redisUtils.set(logoutKey, "", remainingTime, TimeUnit.SECONDS);
        if (true){
            return ResultRes.success();
        }else {
            return ResultRes.error(ResEnum.EXIT_ERROR.getValue(),ResEnum.EXIT_ERROR.getDesc());
        }
    }

    private LoginRes getLoginTokenRes(String personUuid,String personName,String personMobile,Integer personType){
        return getLoginTokenRes(personUuid,personName,personType,personMobile,null);
    }

    /**
     * 获取登陆返回输出对象
     * @param personUuid
     * @param personName
     * @param personType
     * @param loginChannel
     * @return
     */
    private LoginRes getLoginTokenRes(String personUuid,String personName,Integer personType,String personMobile,String loginChannel){
        LoginToken loginToken = new LoginToken();
        loginToken.setUuid(personUuid);
        loginToken.setUserUuid(personUuid);
        loginToken.setUserName(StringUtils.isEmpty(personName) ? personUuid : personName);
        loginToken.setLoginTime(new Date());
        loginToken.setUserMobile(personMobile);
        loginToken.setUserType(personType);
        // 生成token
        String token = TokenUtil.createToken(loginToken);
        // 登陆成功token存入redis
        redisUtils.set(loginToken.cacheKey(), token, (long)loginToken.getExpireTime(), TimeUnit.MINUTES);
        //封装返回参数
        LoginRes tokenRes = new LoginRes();
        tokenRes.setToken(token);
        tokenRes.setExpires_in(loginToken.getExpireTime());
        tokenRes.setUserType(personType);
        tokenRes.setUuid(personUuid);
        return tokenRes;
    }


    /**
     * 校验用户登录
     * @param param
     */
    private void validUserLogin(LoginReq param){
        //校验验证码
        String cacheKey = String.format(Constants.LOGIN_VERIFICATION_CODE_CACHE_KEY, new Object[] { param.getAccountName() });
        String redisCode = String.valueOf(redisUtils.get(cacheKey));
        if(StringUtils.isEmpty(redisCode)){
            throw new BusinessException(ResEnum.VERIFICATION_CODE_ERROR.getValue(),ResEnum.VERIFICATION_CODE_ERROR.getDesc());
        }
        //验证码1分钟内 最多校验 6 次
        Long validExpireTime = 60L;
        String validCodeKey = String.format(Constants.VERIFICATION_CODE_TIMES_CACHE_KEY,param.getAccountName(),redisCode);
        log.info(">>>>>>>>>>>>>>validCodeKey:{}",validCodeKey);
        Object validCodeKeyNum = redisUtils.get(validCodeKey);
        if(null == validCodeKeyNum){
            final String maxNum = "6";
            redisUtils.set(validCodeKey,maxNum,validExpireTime,TimeUnit.SECONDS);
        }else{
            Integer leftNum = Integer.valueOf(validCodeKeyNum.toString());
            if(leftNum < 1){
                throw new BusinessException(ResEnum.OPERATION_FREQUENTLY_ERROR);
            }else{
                redisUtils.set(validCodeKey,String.valueOf((leftNum-1)),validExpireTime,TimeUnit.SECONDS);
            }
        }
        //判断验证码是否正确
        if(!redisCode.equals(param.getCode())){
            throw new BusinessException(ResEnum.VERIFICATION_CODE_ERROR.getValue(),ResEnum.VERIFICATION_CODE_ERROR.getDesc());
        }
        //删除验证码
        redisUtils.remove(cacheKey);

    }

    /**
     * 检查短信、登陆相关的发送披露
     * @param request
     * @param methodName
     */
    public void checkSendRate(HttpServletRequest request,String methodName){
        String userIp = IpUtils.getRequestIp(request);
        String cacheKey = String.format(Constants.contents, methodName,userIp);
        Object o = redisUtils.get(cacheKey);
        String cacheCount = (null != o) ? (String) o : "";
        if(StringUtils.isEmpty(cacheCount)){
            //缓存中不存在该值，将数值进行初始化,缓存1分钟
            redisUtils.set(cacheKey,String.valueOf(1),1L, TimeUnit.MINUTES);
            log.info("----------------"+String.valueOf(o));
        }else if(Integer.valueOf(cacheCount) >= Constants.OPERATING_FREQUENCY){
            //提示操作频繁，请稍后重试
            throw new BusinessException(ResEnum.OPERATION_FREQUENTLY_ERROR);
        }else{
            redisUtils.set(cacheKey,String.valueOf(Integer.valueOf(cacheCount) + 1),1L, TimeUnit.MINUTES);
        }
    }
}
