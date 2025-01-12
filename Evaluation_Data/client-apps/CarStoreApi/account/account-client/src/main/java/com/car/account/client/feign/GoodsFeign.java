package com.car.account.client.feign;

import com.car.account.client.response.goods.GoodsRes;
import com.car.common.res.ResultRes;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Created by Intellij IDEA.
 *
 * @author:  cjw
 * Date:  2021/3/13
 */
@FeignClient(value = "account")
public interface GoodsFeign {
    /**
     * 查询商品详情
     * @param uuid
     * @return
     */
    @GetMapping(value = "/goods/queryGoodsDetail/{uuid}")
    public ResultRes<GoodsRes> queryGoodsDetail(@PathVariable(name = "uuid") String uuid);
}
