package com.car.account.client.feign;


import com.car.account.client.request.platform.PlatformStreamReq;
import com.car.account.client.request.platform.SelectPlatformReq;
import com.car.account.client.response.platform.PlatformStreamRes;
import com.car.common.res.ResultRes;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author
 * @date 2020/6/15 11:47
 */
@FeignClient(value = "account")
public interface PlatformStreamFeign {

    /**
     * 添加平台流水
     */
    @PostMapping(value = "/platform/addPlatfrom")
    public ResultRes<String> addPlatfrom(@RequestBody PlatformStreamReq platformStreamReq);

    @PostMapping(value = "/platform/selectPlatfrom")
    public ResultRes<PlatformStreamRes> selectPlatfrom(@RequestBody SelectPlatformReq selectPlatformReq);


}
