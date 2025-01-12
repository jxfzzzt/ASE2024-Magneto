package com.car.order.web.model.course;

import com.car.common.datasource.model.BaseModelInfo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author zhouz
 * @date 2021/2/18
 */
@Data
@Table(name = "course_order_detail")
public class CourseOrderDetail extends BaseModelInfo {

    /**
     * 课程订单uuid
     */
    @Column(name = "order_uuid")
    private String orderUuid;

    /**
     * 课程简介
     */
    @Column(name = "course_intro")
    private String courseIntro;

    /**
     * 课程内容:直播课程时为空  图文课程保存具体图文及视频信息
     */
    @Column(name = "course_content")
    private String courseContent;

    /**
     * 课程时间:直播课程时才有
     */
    @Column(name = "course_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date courseTime;

    /**
     * 课程链接:直播课程时才有
     */
    @Column(name = "course_url")
    private String courseUrl;
}
