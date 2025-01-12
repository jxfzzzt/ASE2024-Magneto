package com.car.order.web.mapper.scene;

import com.car.order.web.model.scene.SceneOrderTechnician;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

/**
 * Created by Intellij IDEA.
 *
 * @author:  cjw
 * Date:  2021/1/30
 */
@Repository
public interface SceneOrderTechnicianMapper extends Mapper<SceneOrderTechnician> {


    /**
     * 删除现场订单技师内容dtc图片
     * @param sceneOrderTechnicianUuid
     * @return
     */
    int deleteSceneOrderTechnician(@Param("sceneOrderTechnicianUuid") String sceneOrderTechnicianUuid);

}
