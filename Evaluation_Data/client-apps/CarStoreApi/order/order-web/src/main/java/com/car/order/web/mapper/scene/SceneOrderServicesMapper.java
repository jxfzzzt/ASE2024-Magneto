package com.car.order.web.mapper.scene;

import com.car.order.client.request.scene.QuerySceneOrderListReq;
import com.car.order.client.response.scene.QuerySceneOrderListRes;
import com.car.order.web.dto.LaAndLoDto;
import com.car.order.web.dto.scene.SceneOrderDto;
import com.car.order.web.model.scene.SceneOrder;
import com.car.order.web.model.scene.SceneOrderServices;
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
public interface SceneOrderServicesMapper extends Mapper<SceneOrderServices> {



}
