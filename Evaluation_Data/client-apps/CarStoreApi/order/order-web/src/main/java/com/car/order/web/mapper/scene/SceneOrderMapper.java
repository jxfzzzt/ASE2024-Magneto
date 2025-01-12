package com.car.order.web.mapper.scene;

import com.car.order.client.request.scene.QuerySceneOrderListReq;
import com.car.order.client.response.scene.QuerySceneOrderInfoRes;
import com.car.order.client.response.scene.QuerySceneOrderListRes;
import com.car.order.web.dto.LaAndLoDto;
import com.car.order.web.dto.scene.SceneOrderDto;
import com.car.order.web.model.scene.SceneOrder;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;
import java.util.List;

/**
 * Created by Intellij IDEA.
 *
 * @author:  cjw
 * Date:  2021/1/30
 */
@Repository
public interface SceneOrderMapper extends Mapper<SceneOrder> {


    /**
     *查询现场订单列表
     * @param req 请求参数
     * @param userUuid 发布人uuid
     * @param grabOrderSts 抢单状态
     * @param buyerUuid  抢单者uuid
     * @param laAndLoDto
     * @param orderSts
     * @return
     */
    List<QuerySceneOrderListRes> querySceneOrderList(@Param("req") QuerySceneOrderListReq req, @Param("userUuid") String userUuid, @Param("grabOrderSts") Integer grabOrderSts, @Param("buyerUuid") String buyerUuid, @Param("laAndLoDto") LaAndLoDto laAndLoDto, @Param("orderSts") Integer orderSts);

    /**
     * 查询现场下单详情
     * @param sceneOrderUuid
     * @return
     */
    SceneOrderDto querySceneOrderInfo(@Param("sceneOrderUuid") String sceneOrderUuid);
}
