package com.car.account.web.service.platform;

import com.car.account.client.request.platform.PlatformStreamReq;
import com.car.account.client.request.platform.SelectPlatformReq;
import com.car.account.client.response.platform.PlatformStreamRes;
import com.car.common.res.ResultRes;
import org.springframework.web.bind.annotation.RequestBody;


public interface PlatformService {


    ResultRes<String> addPlatfrom( PlatformStreamReq platformStreamReq);

    ResultRes<PlatformStreamRes> selectPlatfrom(@RequestBody SelectPlatformReq selectPlatformReq);
}