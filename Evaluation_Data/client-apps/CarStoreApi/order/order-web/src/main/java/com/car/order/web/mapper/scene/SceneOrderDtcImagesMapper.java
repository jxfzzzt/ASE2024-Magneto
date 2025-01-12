package com.car.order.web.mapper.scene;

import com.car.order.web.model.scene.SceneOrderDtcImages;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * Created by Intellij IDEA.
 *
 * @author:  cjw
 * Date:  2021/3/5
 */
@Repository
public interface SceneOrderDtcImagesMapper extends Mapper<SceneOrderDtcImages> {

    /**
     * 根据第三方uuid查询相关现场dtc相关图片
     * @param relationUuid
     * @return
     */
    List<String> queryList(@Param("relationUuid") String relationUuid);
}
