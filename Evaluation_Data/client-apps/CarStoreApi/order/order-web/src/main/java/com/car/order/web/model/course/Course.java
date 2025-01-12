package com.car.order.web.model.course;

import com.car.common.datasource.model.BaseModelInfo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * @author zhouz
 * @date 2021/2/18
 */
@Data
@Table(name = "course")
public class Course extends BaseModelInfo {

    /**
     * 对应教程uuid
     */
    @Column(name = "course_parent_uuid")
    private String courseParentUuid;

    /**
     * 课程名称
     */
    @Column(name = "course_title")
    private String courseTitle;

    /**
     * 课程封面
     */
    @Column(name = "course_cover")
    private String courseCover;

    /**
     * 课程类型 0:直播 1:图文
     */
    @Column(name = "course_type")
    private Integer courseType;

    /**
     * 课程金额
     */
    @Column(name = "course_amount")
    private BigDecimal courseAmount;

    /**
     * 课程销量
     */
    @Column(name = "course_sales_volume")
    private Integer courseSalesVolume;

    /**
     * 课程讲师
     */
    @Column(name = "course_lecturer")
    private String courseLecturer;

    /**
     * 课程讲师简介
     */
    @Column(name = "course_lecturer_referral")
    private String courseLecturerReferral;

    /**
     * 评分
     */
    @Column(name = "score")
    private BigDecimal score;

    /**
     * 最新课程 0不是，1是
     */
    @Column(name = "latest_course")
    private Integer latestCourse;
}
