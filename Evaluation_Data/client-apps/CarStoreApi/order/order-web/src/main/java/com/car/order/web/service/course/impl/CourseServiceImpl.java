package com.car.order.web.service.course.impl;

import com.alibaba.fastjson.JSON;
import com.car.common.enums.ResEnum;
import com.car.common.enums.StsEnum;
import com.car.common.exception.BusinessException;
import com.car.common.res.PageRes;
import com.car.common.res.ResultRes;
import com.car.common.utils.TokenHelper;
import com.car.common.utils.UuidUtils;
import com.car.order.client.enums.course.PurchaseTypeEnum;
import com.car.order.client.request.course.AddCourseReq;
import com.car.order.client.request.course.QueryCourseListReq;
import com.car.order.client.response.course.QueryCourseInfoRes;
import com.car.order.client.response.course.QueryCourseListRes;
import com.car.order.web.mapper.course.CourseContentMapper;
import com.car.order.web.mapper.course.CourseMapper;
import com.car.order.web.mapper.course.CourseOrderMapper;
import com.car.order.web.model.course.Course;
import com.car.order.web.model.course.CourseContent;
import com.car.order.web.service.course.CourseService;
import com.codingapi.txlcn.tc.annotation.TxcTransaction;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * Created by Intellij IDEA.
 *
 * @author:  cjw
 * Date:  2021/2/21
 */
@Slf4j
@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private CourseContentMapper courseContentMapper;

    @Autowired
    private CourseOrderMapper courseOrderMapper;

    /**
     * 根据id查询详情
     * @param uuid
     * @return
     */
    @Override
    public ResultRes<QueryCourseInfoRes> getById(String uuid) {
        if (StringUtils.isEmpty(uuid)) {
            log.error("查询课程分类详情uuid不能为空");
            throw new BusinessException(ResEnum.LACK_PARAMETER);
        }

        QueryCourseInfoRes res = courseMapper.getById(uuid);
        if (null != res) {
            int orderCount = courseOrderMapper.queryIsPurchase(res.getUuid(), TokenHelper.getUserUuid());
            if (orderCount <= 0) {
                //是否购买该课程
                res.setIsPurchase(PurchaseTypeEnum.NOT_PURCHASE.getValue());
            } else {
                res.setIsPurchase(PurchaseTypeEnum.PURCHASE.getValue());
            }
        }
        return ResultRes.success(res);
    }

    /**
     * 新增课程信息
     * @param req
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @TxcTransaction
    public ResultRes<String> add(AddCourseReq req) {
        //当前用户人用户名
        String userName = TokenHelper.getUserName();
        //新增课程信息
        String courseUuid = insertCourse(userName, req);
        //新增课程详情信息
        insertCourseContent(courseUuid, userName, req);
        return ResultRes.success(courseUuid);
    }

    /**
     * 根据id修改课程信息
     * @param req
     * @param uuid
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @TxcTransaction
    public ResultRes<String> updateById(AddCourseReq req, String uuid) {
        if (StringUtils.isEmpty(uuid)) {
            log.error("修改课程uuid不能为空");
            throw new BusinessException(ResEnum.LACK_PARAMETER);
        }
        //当前用户人用户名
        String userName = TokenHelper.getUserName();
        //修改课程信息
        updateCourse(userName, req, uuid);
        //新增课程详情信息
        updateCourseContent(uuid, userName, req);
        return ResultRes.success(uuid);
    }

    /**
     * 删除课程信息
     * @param uuid
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @TxcTransaction
    public ResultRes<String> deleteById(String uuid) {
        if (StringUtils.isEmpty(uuid)) {
            log.error("删除课程信息uuid不能为空");
            throw new BusinessException(ResEnum.LACK_PARAMETER);
        }
        //当前用户人用户名
        String userName = TokenHelper.getUserName();
        //删除课程信息
        deleteCourse(uuid, userName);
        //删除课程详情信息
        deleteCourseContent(uuid, userName);
        Course course = new Course();
        course.setUuid(uuid);
        course.setSts(StsEnum.INVALID.getValue());
        course.setLastUpdatedBy(userName);
        course.setLastUpdatedTime(new Date());
        int updateNum = courseMapper.updateByPrimaryKeySelective(course);
        if (updateNum <= 0) {
            log.error("删除课程信息失败，请求参数为：{}", JSON.toJSONString(course));
            throw new BusinessException(ResEnum.DELETE_DB_ERROR);
        }
        return ResultRes.success(uuid);
    }

    /**
     * 分页列表
     * @param req
     * @return
     */
    @Override
    public PageRes<List<QueryCourseListRes>> list(QueryCourseListReq req) {
        PageHelper.startPage(req.getPageNum(), req.getPageSize());
        List<QueryCourseListRes> resList = courseMapper.list(req);
        PageInfo<QueryCourseListRes> pageInfo = new PageInfo<>(resList);
        return PageRes.success(resList, pageInfo.getPageSize(), (int) pageInfo.getTotal(), pageInfo.getPages());
    }

    /**
     * 新增课程信息
     * @param userName
     * @param req
     * @return
     */
    private String insertCourse (String userName, AddCourseReq req) {
        Course course = new Course();
        course.setUuid(UuidUtils.getUuid());
        course.setCourseParentUuid(req.getCourseParentUuid());
        course.setCourseTitle(req.getCourseTitle());
        course.setCourseCover(req.getCourseCover());
        course.setCourseType(req.getCourseType());
        course.setCourseAmount(req.getCourseAmount());
        course.setCourseLecturer(req.getCourseLecturer());
        course.setLatestCourse(req.getLatestCourse());
        course.setSts(StsEnum.ACTIVE.getValue());
        course.setCreatedBy(userName);
        course.setCreatedTime(new Date());
        int insertNum = courseMapper.insert(course);
        if (insertNum <= 0 ) {
            log.error("新增课程信息失败，请求参数为：{}", JSON.toJSONString(course));
            throw new BusinessException(ResEnum.INSERT_DB_ERROR);
        }
        return course.getUuid();
    }

    /**
     * 新增课程信息
     * @param userName
     * @param req
     * @return
     */
    private void insertCourseContent (String courseUuid, String userName, AddCourseReq req) {
        CourseContent courseContent = new CourseContent();
        courseContent.setUuid(UuidUtils.getUuid());
        courseContent.setCourseUuid(courseUuid);
        courseContent.setCourseIntro(req.getCourseIntro());
        courseContent.setCourseContent(req.getCourseContent());
        courseContent.setCourseTime(req.getCourseTime());
        courseContent.setCourseUrl(req.getCourseUrl());
        courseContent.setSts(StsEnum.ACTIVE.getValue());
        courseContent.setCreatedBy(userName);
        courseContent.setCreatedTime(new Date());
        int insertNum = courseContentMapper.insert(courseContent);
        if (insertNum <= 0 ) {
            log.error("新增课程详情信息失败，请求参数为：{}", JSON.toJSONString(courseContent));
            throw new BusinessException(ResEnum.INSERT_DB_ERROR);
        }
    }

    /**
     * 修改课程信息
     * @param userName
     * @param req
     * @return
     */
    private void updateCourse (String userName, AddCourseReq req, String uuid) {
        Course course = new Course();
        course.setUuid(uuid);
        course.setCourseParentUuid(req.getCourseParentUuid());
        course.setCourseTitle(req.getCourseTitle());
        course.setCourseCover(req.getCourseCover());
        course.setCourseType(req.getCourseType());
        course.setCourseAmount(req.getCourseAmount());
        course.setLatestCourse(req.getLatestCourse());
        course.setCourseLecturer(req.getCourseLecturer());
        course.setLastUpdatedBy(userName);
        course.setLastUpdatedTime(new Date());
        int insertNum = courseMapper.updateByPrimaryKeySelective(course);
        if (insertNum <= 0 ) {
            log.error("修改课程信息失败，请求参数为：{}", JSON.toJSONString(course));
            throw new BusinessException(ResEnum.INSERT_DB_ERROR);
        }
    }

    /**
     * 修改课程信息
     * @param userName
     * @param req
     * @return
     */
    private void updateCourseContent (String courseUuid, String userName, AddCourseReq req) {
        CourseContent courseContent = new CourseContent();
        courseContent.setCourseUuid(courseUuid);
        courseContent.setCourseIntro(req.getCourseIntro());
        courseContent.setCourseContent(req.getCourseContent());
        courseContent.setCourseTime(req.getCourseTime());
        courseContent.setCourseUrl(req.getCourseUrl());
        courseContent.setLastUpdatedBy(userName);
        courseContent.setLastUpdatedTime(new Date());
        int insertNum = courseContentMapper.updateCourseContent(courseContent);
        if (insertNum <= 0 ) {
            log.error("修改课程详情信息失败，请求参数为：{}", JSON.toJSONString(courseContent));
            throw new BusinessException(ResEnum.INSERT_DB_ERROR);
        }
    }

    /**
     * 删除课程信息
     * @param userName
     * @param uuid
     * @return
     */
    private void deleteCourse (String uuid, String userName) {
        Course course = new Course();
        course.setUuid(uuid);
        course.setSts(StsEnum.INVALID.getValue());
        course.setLastUpdatedBy(userName);
        course.setLastUpdatedTime(new Date());
        int insertNum = courseMapper.updateByPrimaryKeySelective(course);
        if (insertNum <= 0 ) {
            log.error("删除课程信息失败，请求参数为：{}", JSON.toJSONString(course));
            throw new BusinessException(ResEnum.INSERT_DB_ERROR);
        }
    }

    /**
     * 新增课程信息
     * @param userName
     * @param req
     * @return
     */
    private void deleteCourseContent (String courseUuid, String userName) {
        int insertNum = courseContentMapper.deleteCourseContent(courseUuid, userName);
        if (insertNum <= 0 ) {
            log.error("修改课程详情信息失败，请求参数，课程id：{}", courseUuid);
            throw new BusinessException(ResEnum.INSERT_DB_ERROR);
        }
    }
}
