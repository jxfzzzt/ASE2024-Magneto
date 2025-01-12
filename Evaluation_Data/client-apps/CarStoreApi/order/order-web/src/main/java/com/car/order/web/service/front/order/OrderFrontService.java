package com.car.order.web.service.front.order;

import com.alibaba.fastjson.JSONObject;
import com.car.common.res.ResultRes;

/**
 * Created by Intellij IDEA.
 *
 * @author:  cjw
 * Date:  2021/1/31
 */
public interface OrderFrontService {

    /**
     * 查询全部订单详情
     * @param uuid
     * @return
     */
    ResultRes<JSONObject> queryOrderInfo(String uuid);

    /**
     * 查询我的提问数量
     * @param userUuid
     * @return
     */
    ResultRes<Integer> queryQuizCount(String userUuid);

    /**
     * 查询我的案例数量
     * @param userUuid
     * @return
     */
    ResultRes<Integer> queryCaseCount(String userUuid);
}
