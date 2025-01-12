package com.car.account.web.service.addr.impl;

import com.alibaba.fastjson.JSON;
import com.car.account.client.request.addr.ModifyAddrReq;
import com.car.account.client.request.addr.ReceiveAddrReq;
import com.car.account.client.response.addr.ReceiveAddrRes;
import com.car.account.web.mapper.addr.ReceiveAddrMapper;
import com.car.account.web.model.addr.ReceiveAddr;
import com.car.account.web.service.addr.AddrService;
import com.car.common.enums.ResEnum;
import com.car.common.enums.StsEnum;
import com.car.common.exception.BusinessException;
import com.car.common.utils.Constants;
import com.car.common.utils.RedisUtils;
import com.car.common.utils.TokenHelper;
import com.car.common.utils.UuidUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author zhangyp
 * @date 2021/1/14 23:17
 */
@Slf4j
@Service
public class AddrServiceImpl implements AddrService {

    @Autowired
    private ReceiveAddrMapper receiveAddrMapper;

    @Autowired
    private RedisUtils redisUtils;

    @Override
    public ReceiveAddrRes addAddr(ReceiveAddrReq params) {
        ReceiveAddr data = new ReceiveAddr();
        BeanUtils.copyProperties(params,data);
        data.setProvinceName(locationVal(data.getProvince()));
        data.setCityName(locationVal(data.getCity()));
        data.setAreaName(locationVal(data.getArea()));

        String userUuid = TokenHelper.getUserUuid();
        String userName = org.springframework.util.StringUtils.isEmpty(TokenHelper.getUserName()) ? TokenHelper.getLoginToken().getUserMobile() : TokenHelper.getUserName();

        if(null == userUuid){

            throw new BusinessException(ResEnum.EXPIRE_TOKEN);
        }

        data.setUuid(UuidUtils.getUuid());
        data.setUserId(userUuid);
        data.setSts(StsEnum.ACTIVE.getValue());
        data.setCreatedTime(new Date());
        data.setCreatedBy(userName);

        int insert = receiveAddrMapper.insert(data);
        if(0 == insert){

            log.error("新增地址失败>>>params:{}", JSON.toJSONString(params));
            throw new BusinessException(ResEnum.DB_ERROR);
        }

        ReceiveAddrRes res = new ReceiveAddrRes();
        BeanUtils.copyProperties(data,res);
        return res;
    }

    @Override
    public ReceiveAddrRes updateAddr(ModifyAddrReq params) {

        //校验地址合法性
        String userUuid = TokenHelper.getUserUuid();
        String uuid = params.getUuid();
        ReceiveAddr data = new ReceiveAddr();
        data.setUserId(userUuid);
        data.setUuid(uuid);
        List<ReceiveAddr> list = receiveAddrMapper.queryAddrList(data);
        if(null == list || list.isEmpty()){

            log.error("未定位到用户收货地址>>>data:{}",JSON.toJSONString(data));
            throw new BusinessException(ResEnum.ADDR_NOT_EXIST);
        }

        BeanUtils.copyProperties(params,data);
        data.setUserId(userUuid);
        data.setLastUpdatedBy(TokenHelper.getUserName());
        data.setLastUpdatedTime(new Date());

        data.setProvinceName(locationVal(data.getProvince()));
        data.setCityName(locationVal(data.getCity()));
        data.setAreaName(locationVal(data.getArea()));

        int i = receiveAddrMapper.updateByPrimaryKeySelective(data);
        if(0 == i){

            log.error("更新收货地址失败>>>params:{}",JSON.toJSONString(data));
            throw new BusinessException(ResEnum.UPDATE_DB_ERROR);
        }

        ReceiveAddrRes dst = new ReceiveAddrRes();
        BeanUtils.copyProperties(data,dst);
        return dst;
    }

    @Override
    public void disableAddr(String uuid) {

        String userUuid = TokenHelper.getUserUuid();
        ReceiveAddr data = new ReceiveAddr();
        data.setUserId(userUuid);
        data.setUuid(uuid);
        List<ReceiveAddr> list = receiveAddrMapper.queryAddrList(data);
        if(null == list || list.isEmpty()){

            log.error("未定位到用户收货地址>>>params:{}",JSON.toJSONString(data));
            throw new BusinessException(ResEnum.ADDR_NOT_EXIST);
        }

        ReceiveAddr receiveAddr = list.get(0);
        receiveAddr.setSts(StsEnum.INVALID.getValue());
        receiveAddr.setLastUpdatedTime(new Date());
        receiveAddr.setLastUpdatedBy(TokenHelper.getUserName());
        int i = receiveAddrMapper.updateByPrimaryKey(receiveAddr);
        if(0 == i){

            log.error("更新地址失败>>>params:{}",JSON.toJSONString(receiveAddr));
            throw new BusinessException(ResEnum.UPDATE_DB_ERROR);
        }

        return;
    }

    @Override
    public List<ReceiveAddrRes> listAddr() {

        String userUuid = TokenHelper.getUserUuid();
        ReceiveAddr data = new ReceiveAddr();
        data.setUserId(userUuid);

        List<ReceiveAddr> list = receiveAddrMapper.queryAddrList(data);
        List<ReceiveAddrRes> dst = new ArrayList<>();
        if(null != list && !list.isEmpty()){

            list.stream().forEach(s ->{

                ReceiveAddrRes t = new ReceiveAddrRes();
                BeanUtils.copyProperties(s,t);
                dst.add(t);
            });

            return dst;
        }
        return dst;
    }

    @Override
    public ReceiveAddrRes queryAddrDetail(String uuid) {

        String userUuid = TokenHelper.getUserUuid();
        ReceiveAddr data = new ReceiveAddr();
        data.setUuid(uuid);
        data.setUserId(userUuid);

        List<ReceiveAddr> list = receiveAddrMapper.queryAddrList(data);
        if(null == list || list.isEmpty()){

            log.error("未定位到用户收货地址信息>>>params:{}",JSON.toJSONString(data));
            throw new BusinessException(ResEnum.ADDR_NOT_EXIST);
        }

        ReceiveAddrRes dst = new ReceiveAddrRes();
        ReceiveAddr receiveAddr = list.get(0);
        BeanUtils.copyProperties(receiveAddr,dst);
        return dst;
    }
    /**
     * 定位区域中文描述
     * @param areaCode
     * @return
     */
    private String locationVal(String areaCode){

        if(StringUtils.isNotBlank(areaCode)){
            String key = String.format(Constants.AREA_CODE_RELATE_AREA_NAME,areaCode);

            String val = redisUtils.getString(key);
            if(StringUtils.isNotBlank(val)){

                return val;
            }else{
                String areaName = receiveAddrMapper.selectAreaNameByAreaCode(areaCode);
                if(StringUtils.isNotBlank(areaName)){

                    redisUtils.set(key,areaName,5L, TimeUnit.MINUTES);
                    return areaName;
                }
            }
        }
        return null;
    }
}
