package com.car.account.client.feign;

import com.car.account.client.request.technician.UpdateTechnicianAccountReq;
import com.car.account.client.response.technician.TechnicianRes;
import com.car.common.res.ResultRes;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author zhangyp
 * @date 2021/1/21 21:39
 */
@FeignClient(value = "account")
public interface TechnicianFegin {

    @GetMapping(value = "/technician/queryTechnicianDetail/{uuid}")
    ResultRes<TechnicianRes> queryTechnicianDetail(@PathVariable("uuid") String uuid);

    /**
     * 修改技师账户信息
     * @param req
     * @return
     */
    @PostMapping(value = "/technicianAccount/updateTechnicianAccount")
    ResultRes<String> updateTechnicianAccount (@RequestBody @Valid UpdateTechnicianAccountReq req);

    /**
     * 修改技师问答数量
     * @param uuid
     * @return
     */
    @RequestMapping(value = "/technician/updateQaCount/{uuid}", method = RequestMethod.PUT)
    public ResultRes<String> updateQaCount(@PathVariable("uuid") String uuid);
}
