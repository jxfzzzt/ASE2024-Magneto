package com.car.order.web.mapper.content;

import com.car.order.web.model.content.ContentResources;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author zhouz
 * @date 2020/12/28
 */
@Repository
public interface ContentResourcesMapper extends Mapper<ContentResources> {

    /**
     * 批量新增内容相关资源
     * @param contentResourcesList
     */
    void batchInsertContentResources(@Param("insertList") List<ContentResources> contentResourcesList);

    /**
     * 逻辑删除内容相关资源
     * @param contentUuid
     */
    void deleteResourcesByContentUuid(String contentUuid);
}
