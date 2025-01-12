package com.car.common.datasource.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.Date;

/**
 * @author: xlj
 * @Date: 2019/5/10 9:43
 * @Description: 公共属性
 */
@Data
public class BaseModelInfo {

    /**
     * 主键ID
     */
    @Id
    private String uuid;

    /**
     * 是否有效 0:生效 1：无效
     */
    @Column(name = "sts")
    private Integer sts;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Column(name = "created_time")
    private Date createdTime;
    /**
     * 创建人
     */
    @Column(name = "created_by")
    private String createdBy;
    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Column(name = "last_updated_time")
    private Date lastUpdatedTime;
    /**
     * 更新人
     */
    @Column(name = "last_updated_by")
    private String lastUpdatedBy;
}
