package com.car.order.web.mapper.course;

import com.car.order.web.model.course.CourseContent;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

/**
 * Created by Intellij IDEA.
 *
 * @author:  cjw
 * Date:  2021/2/21
 */
@Repository
public interface CourseContentMapper extends Mapper<CourseContent> {


    /**
     * 修改课程详情
     * @param courseContent
     * @return
     */
   int updateCourseContent(@Param("courseContent") CourseContent courseContent);

    /**
     * 删除课程详情
     * @param courseUuid
     * @param userName
     * @return
     */
   int deleteCourseContent(@Param("courseUuid") String courseUuid, @Param("userName")String userName);
}
