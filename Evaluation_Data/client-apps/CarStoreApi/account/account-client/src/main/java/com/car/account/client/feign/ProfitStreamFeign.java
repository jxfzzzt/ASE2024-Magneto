package com.car.account.client.feign;


import com.car.account.client.request.profit.AddProfitReq;
import com.car.common.res.ResultRes;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author
 * @date 2020/6/15 11:47
 */
@FeignClient(value = "account")
public interface ProfitStreamFeign {

    /**
     * {@code true}添加账户流水
     */
    @PostMapping(value = "/profitStream/addProfit")
    public ResultRes<String> addProfit(@RequestBody AddProfitReq addProfitReq);



}
