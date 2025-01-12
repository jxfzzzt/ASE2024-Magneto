package com.car.order.web.service.sence;


import com.car.common.res.PageRes;
import com.car.common.res.ResultRes;
import com.car.order.client.request.scene.*;
import com.car.order.client.response.scene.QuerySceneOrderInfoRes;
import com.car.order.client.response.scene.QuerySceneOrderListRes;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * 现场订单信息
 *
 * @author cjw
 * @date 2021-02-26 22:08:48
 */
public interface SceneOrderService {


    /**
     * 新增现场订单
     * @param req
     * @return
     */
    ResultRes<String> addSceneOrder(AddSceneOrderReq req);



    /**
     * 技师提交说明
     * @param req
     * @return
     */
    ResultRes<String> sceneOrderDescribe(SceneOrderDescribeReq req);

    /**
     * 客户确认
     * @param req
     * @return
     */
    ResultRes<String> sceneOrderConfirm(SceneOrderConfirmReq req);

    /**
     * 提交方案
     * @param req
     * @return
     */
    ResultRes<String> sceneSubmitPlan(AddSceneOrderServiceReq req);


    /**
     * 查询现场订单列表
     * @param req
     * @return
     */
    PageRes<List<QuerySceneOrderListRes>> querySceneOrderList(QuerySceneOrderListReq req);

    /**
     * 查询现场订单详情
     * @param uuid
     * @return
     */
    ResultRes<QuerySceneOrderInfoRes> querySceneOrderInfo(@PathVariable(name = "uuid") String uuid);

    /**
     * 现场订单抢单
     * @param sceneOrderUuid
     * @return
     */
    ResultRes<String> grabbingOrders(String sceneOrderUuid);

    /**
     * dtc图片新增
     * @param dtcImageList
     * @param userName
     * @param relationUuid
     */
    void insertSceneOrderDtcImg(List<String> dtcImageList, String userName, String relationUuid,Integer type);

    /**
     * 完成现场订单
     * @param sceneOrderUuid
     * @return
     */
    ResultRes<String> completeOrder(String sceneOrderUuid);

    /**
     * 取消订单
     * @param sceneOrderUuid
     * @return
     */
    ResultRes<String> cancelOrderOrder(String sceneOrderUuid);


}

