package com.car.order.web.model.content;

import com.car.common.datasource.model.BaseModelInfo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * @author zhouz
 * @date 2020/12/28
 */
@Data
@Table(name = "content_resources")
public class ContentResources extends BaseModelInfo {
    /**
     * 内容uuid
     */
    @Column(name = "content_uuid")
    private String contentUuid;

    /**
     * 资源url
     */
    @Column(name = "resources_url")
    private String resourcesUrl;

}
