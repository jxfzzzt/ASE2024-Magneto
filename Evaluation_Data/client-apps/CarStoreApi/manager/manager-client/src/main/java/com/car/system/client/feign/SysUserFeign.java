package com.car.system.client.feign;

import com.car.common.constant.ServiceNameConstant;
import com.car.common.res.ResultRes;
import com.car.system.client.response.user.UserDetailRes;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Created by Intellij IDEA.
 *
 * @author:  cjw
 * Date:  2021/2/19
 */
@FeignClient(value = ServiceNameConstant.SYSTEM)
public interface SysUserFeign {


    /**
     * 查询用户详情
     * @param uuid
     * @return
     */
    @GetMapping(value = "/user/queryUserDetail/{uuid}")
    ResultRes<UserDetailRes> queryUserDetail(@PathVariable(value = "uuid")String uuid);
}
