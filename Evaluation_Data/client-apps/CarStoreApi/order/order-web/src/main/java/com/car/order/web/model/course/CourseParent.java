package com.car.order.web.model.course;

import com.car.common.datasource.model.BaseModelInfo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * @author zhouz
 * @date 2021/2/18
 */
@Data
@Table(name = "course_parent")
public class CourseParent extends BaseModelInfo {

    /**
     * 教程名称
     */
    @Column(name = "course_title")
    private String courseTitle;

    /**
     * 教程封面图片url
     */
    @Column(name = "course_cover")
    private String courseCover;

    /**
     * 教程描述图文
     */
    @Column(name = "course_desc")
    private String courseDesc;


}
