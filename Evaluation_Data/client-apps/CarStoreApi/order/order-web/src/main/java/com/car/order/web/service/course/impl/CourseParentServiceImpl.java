package com.car.order.web.service.course.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.car.common.enums.ResEnum;
import com.car.common.enums.StsEnum;
import com.car.common.exception.BusinessException;
import com.car.common.res.PageRes;
import com.car.common.res.ResultRes;
import com.car.common.utils.TokenHelper;
import com.car.common.utils.UuidUtils;
import com.car.order.client.request.course.AddCourseParentReq;
import com.car.order.client.request.course.QueryCourseParentListReq;
import com.car.order.client.response.course.QueryCourseParentInfoRes;
import com.car.order.client.response.course.QueryCourseParentListRes;
import com.car.order.web.common.constants.ConfigConsts;
import com.car.order.web.mapper.course.CourseParentMapper;
import com.car.order.web.model.course.CourseParent;
import com.car.order.web.service.course.CourseParentService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by Intellij IDEA.
 *
 * @author:  cjw
 * Date:  2021/2/21
 */
@Slf4j
@Service
public class CourseParentServiceImpl implements CourseParentService {

	@Autowired
	private CourseParentMapper courseParentMapper;

	@Autowired
	private ConfigConsts configConsts;

	/**
	 * 根据id查询详情
	 * @param uuid
	 * @return
	 */
	@Override
	public ResultRes<QueryCourseParentInfoRes> getById(String uuid) {
		if (StringUtils.isEmpty(uuid)) {
			log.error("查询课程分类详情uuid不能为空");
			throw new BusinessException(ResEnum.LACK_PARAMETER);
		}
		QueryCourseParentInfoRes res = courseParentMapper.getById(uuid);
		return ResultRes.success(res);
	}

	/**
	 * 新增教程分类信息
	 * @param req
	 * @return
	 */
	@Override
	public ResultRes<String> add(AddCourseParentReq req) {
		// 当前用户人用户名
		String userName = TokenHelper.getUserName();
		CourseParent courseParent = new CourseParent();
		courseParent.setUuid(UuidUtils.getUuid());
		courseParent.setCourseTitle(req.getCourseTitle());
		courseParent.setCourseCover(req.getCourseCover());
		courseParent.setCourseDesc(req.getCourseDesc());
		courseParent.setSts(StsEnum.ACTIVE.getValue());
		courseParent.setCreatedBy(userName);
		courseParent.setCreatedTime(new Date());
		int insertNum = courseParentMapper.insert(courseParent);
		if (insertNum <= 0) {
			log.error("新增课程分类失败，请求参数为：{}", JSON.toJSONString(courseParent));
			throw new BusinessException(ResEnum.INSERT_DB_ERROR);
		}
		return ResultRes.success(courseParent.getUuid());
	}

	/**
	 * 根据id修改课程分类信息
	 * @param req
	 * @param uuid
	 * @return
	 */
	@Override
	public ResultRes<String> updateById(AddCourseParentReq req, String uuid) {
		if (StringUtils.isEmpty(uuid)) {
			log.error("修改课程分类信息uuid不能为空");
			throw new BusinessException(ResEnum.LACK_PARAMETER);
		}
		// 当前用户人用户名
		String userName = TokenHelper.getUserName();
		CourseParent courseParent = new CourseParent();
		courseParent.setUuid(uuid);
		courseParent.setCourseTitle(req.getCourseTitle());
		courseParent.setCourseCover(req.getCourseCover());
		courseParent.setCourseDesc(req.getCourseDesc());
		courseParent.setLastUpdatedBy(userName);
		courseParent.setLastUpdatedTime(new Date());
		int updateNum = courseParentMapper.updateByPrimaryKeySelective(courseParent);
		if (updateNum <= 0) {
			log.error("修改课程分类失败，请求参数为：{}", JSON.toJSONString(courseParent));
			throw new BusinessException(ResEnum.UPDATE_DB_ERROR);
		}
		return ResultRes.success(courseParent.getUuid());
	}

	/**
	 * 删除课程分类信息
	 * @param uuid
	 * @return
	 */
	@Override
	public ResultRes<String> deleteById(String uuid) {
		if (StringUtils.isEmpty(uuid)) {
			log.error("查询课程分类详情uuid不能为空");
			throw new BusinessException(ResEnum.LACK_PARAMETER);
		}
		// 当前用户人用户名
		String userName = TokenHelper.getUserName();
		CourseParent courseParent = new CourseParent();
		courseParent.setUuid(uuid);
		courseParent.setSts(StsEnum.INVALID.getValue());
		courseParent.setLastUpdatedBy(userName);
		courseParent.setLastUpdatedTime(new Date());
		int updateNum = courseParentMapper.updateByPrimaryKeySelective(courseParent);
		if (updateNum <= 0) {
			log.error("删除课程分类失败，请求参数为：{}", JSON.toJSONString(courseParent));
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
	public PageRes<List<QueryCourseParentListRes>> list(QueryCourseParentListReq req) {
		PageHelper.startPage(req.getPageNum(), req.getPageSize());
		List<QueryCourseParentListRes> resList = courseParentMapper.list(req);
		// 获取最新的N条数据（N有配置文件控制）
		List<QueryCourseParentListRes> limitNewResList = courseParentMapper
				.queryCourseParentLimit(configConsts.getNewCourseParentNum());
		// 标记最新的三条
		if (!CollectionUtils.isEmpty(resList) && !CollectionUtils.isEmpty(limitNewResList)) {
			for (QueryCourseParentListRes res : resList) {
				boolean flag = false;
				for (QueryCourseParentListRes limitNewRes : limitNewResList) {
					if (res.getUuid().equals(limitNewRes.getUuid())) {
						flag = true;
					}
				}
				res.setNewest(flag);
			}
		}
		PageInfo<QueryCourseParentListRes> pageInfo = new PageInfo<>(resList);
		return PageRes.success(resList, pageInfo.getPageSize(), (int) pageInfo.getTotal(), pageInfo.getPages());
	}

	/**
	 * 分页列表
	 * @param req
	 * @return
	 */
	@Override
	public PageRes<List<QueryCourseParentListRes>> listNewest(QueryCourseParentListReq req) {
		PageHelper.startPage(req.getPageNum(), req.getPageSize());
		List<QueryCourseParentListRes> resList = courseParentMapper.listNewest(req);
		PageInfo<QueryCourseParentListRes> pageInfo = new PageInfo<>(resList);
		return PageRes.success(resList, pageInfo.getPageSize(), (int) pageInfo.getTotal(), pageInfo.getPages());
	}

	/**
	 * 分页列表
	 * @param req
	 * @return
	 */
	@Override
	public PageRes<List<QueryCourseParentListRes>> listGeneral(QueryCourseParentListReq req) {
		PageHelper.startPage(req.getPageNum(), req.getPageSize());
		List<QueryCourseParentListRes> resList = courseParentMapper.listGeneral(req);
		PageInfo<QueryCourseParentListRes> pageInfo = new PageInfo<>(resList);
		return PageRes.success(resList, pageInfo.getPageSize(), (int) pageInfo.getTotal(), pageInfo.getPages());
	}
}
