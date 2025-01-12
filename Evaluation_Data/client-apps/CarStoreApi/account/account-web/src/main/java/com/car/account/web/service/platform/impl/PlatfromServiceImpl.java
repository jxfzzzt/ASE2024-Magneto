package com.car.account.web.service.platform.impl;


import com.car.account.client.request.platform.PlatformStreamReq;
import com.car.account.client.request.platform.SelectPlatformReq;
import com.car.account.client.response.platform.PlatformStreamRes;
import com.car.account.web.mapper.platform.PlatformStreamMapper;
import com.car.account.web.model.platform.PlatformStream;
import com.car.account.web.service.platform.PlatformService;
import com.car.common.enums.StsEnum;
import com.car.common.res.ResultRes;
import com.car.common.utils.UuidUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author zhangyp
 * @date 2021/1/16 21:28
 */
@Slf4j
@Service
public class PlatfromServiceImpl implements PlatformService {

    @Autowired
    PlatformStreamMapper platformStreamMapper;

    @Override
    public ResultRes<String> addPlatfrom(PlatformStreamReq platformStreamReq) {
        PlatformStream platformStream = new PlatformStream();
        BeanUtils.copyProperties(platformStreamReq,platformStream);
        platformStream.setUuid(UuidUtils.getUuid());
        platformStream.setCreatedTime(new Date());
        platformStream.setSts(StsEnum.ACTIVE.getValue());
        platformStreamMapper.insert(platformStream);
        return ResultRes.success(platformStream.getUuid());
    }

    @Override
    public ResultRes<PlatformStreamRes> selectPlatfrom(SelectPlatformReq selectPlatformReq) {
        PlatformStream platformStream = new PlatformStream();
        BeanUtils.copyProperties(selectPlatformReq,platformStream);
        PlatformStream stream = platformStreamMapper.selectOne(platformStream);
        PlatformStreamRes platformStreamRes =null ;
        if(stream !=null) {
            platformStreamRes = new PlatformStreamRes();
            BeanUtils.copyProperties(stream, platformStreamRes);
        }
        return ResultRes.success(platformStreamRes);
    }
}
