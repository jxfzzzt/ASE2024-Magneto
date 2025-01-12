package com.car.order.web.service.consult.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.car.common.enums.ConsultOrderTypeEnum;
import com.car.common.enums.ResEnum;
import com.car.common.enums.StsEnum;
import com.car.common.enums.UserTypeEnum;
import com.car.common.exception.BusinessException;
import com.car.common.req.PageReq;
import com.car.common.res.PageRes;
import com.car.common.res.ResultRes;
import com.car.common.utils.DateUtil;
import com.car.common.utils.TokenHelper;
import com.car.order.client.enums.goods.OrderStsEnum;
import com.car.order.client.response.order.consult.ConsultBaseRes;
import com.car.order.client.response.order.consult.ConsultRes;
import com.car.order.web.common.constants.ConfigConsts;
import com.car.order.web.common.constants.Constants;
import com.car.order.web.dto.consult.ConsultDto;
import com.car.order.web.mapper.consult.ConsultImagesMapper;
import com.car.order.web.mapper.consult.ConsultMapper;
import com.car.order.web.mapper.consult.ConsultOrderMapper;
import com.car.order.web.model.consult.ConsultImages;
import com.car.order.web.model.consult.ConsultOrder;
import com.car.order.web.service.consult.ConsultService;
import com.car.system.client.feign.SystemFeign;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import lombok.extern.slf4j.Slf4j;

/**
 * @author zhangyp
 * @date 2021/1/28 0:21
 */
@Slf4j
@Service
public class ConsultServiceImpl implements ConsultService {

	private static final int SUB_STR_LENGTH = 46;
	@Resource
	private ConsultMapper consultMapper;
	@Resource
	private ConsultImagesMapper consultImagesMapper;
	@Resource
	private ConsultOrderMapper consultOrderMapper;

	@Autowired
	ConfigConsts configConsts;
	@Autowired
	private SystemFeign systemFeign;

	@Override
	public PageRes<List<ConsultBaseRes>> queryConsultList(PageReq pageReq) {

		Integer pageNum = pageReq.getPageNum();
		Integer pageSize = pageReq.getPageSize();
		PageHelper.startPage(pageNum, pageSize);

		List<ConsultDto> list = consultMapper.queryShowConsultList();
		PageInfo pageInfo = new PageInfo<>(list);

		List<ConsultBaseRes> rstList = new ArrayList<>();
		if (!CollectionUtils.isEmpty(list)) {

			boolean isApple = "1".equals(systemFeign.queryByUuid(com.car.common.utils.Constants.PRICE_ON_OFF_UUID)
					.getData().getLableValue());

			for (ConsultDto c : list) {

				ConsultBaseRes res = new ConsultBaseRes();
				res.setUuid(c.getUuid());
				res.setTechnicianImgUrl(c.getTechnicianImgUrl());
				res.setTechnicianName(c.getTechnicianName() == null ? Constants.TECHNICIAN_NICK_NAME
						: c.getTechnicianName().substring(0, 1) + Constants.TECHNICIAN_NICK_NAME);
				res.setCarOwnerName(c.getCarOwnerName() == null ? Constants.VEHICLE_NICK_NAME : c.getCarOwnerName());
				res.setOrderUuid(c.getOrderUuid());
				String consultDesc = c.getConsultDesc();
				String answerDesc = c.getAnswerDesc();
				if (StringUtils.isNotBlank(consultDesc) && consultDesc.length() > SUB_STR_LENGTH) {
					res.setConsultDesc(consultDesc.substring(0, SUB_STR_LENGTH) + "...");
				} else {
					res.setConsultDesc(consultDesc);
				}
				if (StringUtils.isNotBlank(answerDesc) && answerDesc.length() > SUB_STR_LENGTH) {
					res.setAnswerDesc(answerDesc.substring(0, SUB_STR_LENGTH) + "...");
				} else {
					res.setAnswerDesc(answerDesc);
				}
				res.setTitle(c.getTitle());
				res.setCreatedTime(DateUtil.dateToStr(c.getCreatedTime(), DateUtil.YYYY_MM_DD));

				res.setConsultAmt(BigDecimal.valueOf(configConsts.getAuditorOrderMoney()));

				if (isApple) {
					res.setConsultAmt(BigDecimal.ZERO);
				}
				rstList.add(res);
			}
		}
		return PageRes.success(rstList, pageInfo.getPageSize(), (int) pageInfo.getTotal(), pageInfo.getPages());
	}

	@Override
	public PageRes<List<ConsultRes>> queryPreConsultList(PageReq pageReq) {

		String userUuid = TokenHelper.getUserUuid();
		Integer userType = TokenHelper.getUserType();

		if (Arrays.asList(UserTypeEnum.vehicle.getType(), UserTypeEnum.store.getType()).contains(userType)) {
			log.info("店铺或车主没有调用技师待问答列表权限");
			return PageRes.success(Collections.emptyList(), 0, 0, 0);
		}

		Integer pageNum = pageReq.getPageNum();
		Integer pageSize = pageReq.getPageSize();
		PageHelper.startPage(pageNum, pageSize);

		List<ConsultDto> list = consultMapper.queryPreAnswerList(userUuid);
		PageInfo pageInfo = new PageInfo<>(list);
		List<ConsultRes> rstList = new ArrayList<>();
		if (!CollectionUtils.isEmpty(list)) {
			list.stream().forEach(c -> {
				ConsultRes res = new ConsultRes();
				BeanUtils.copyProperties(c, res);
				res.setCreatedTime(DateUtil.dateToStr(c.getCreatedTime(), DateUtil.YYYY_MM_DD));
				String uuid = c.getUuid();
				ConsultImages i = new ConsultImages();
				i.setSts(StsEnum.ACTIVE.getValue());
				i.setConsultUuid(uuid);
				List<ConsultImages> imgList = consultImagesMapper.select(i);

				if (!CollectionUtils.isEmpty(imgList)) {
					List<String> ask = new ArrayList<>();
					List<String> ans = new ArrayList<>();
					imgList.stream().forEach(img -> {
						String imgUrl = img.getImgUrl();
						// 0 咨询 1 回答
						Integer imgType = img.getImgType();
						if (Integer.valueOf(0).equals(imgType)) {
							ask.add(imgUrl);
						} else if (Integer.valueOf(1).equals(imgType)) {
							ans.add(imgUrl);
						}
					});
					res.setAnswerImgList(ans);
					res.setConsultImgList(ask);
				}
				rstList.add(res);
			});
		}
		return PageRes.success(rstList, pageInfo.getPageSize(), (int) pageInfo.getTotal(), pageInfo.getPages());
	}

	@Override
	public ResultRes<ConsultRes> queryDetail(String uuid) {

		ConsultDto consult = consultMapper.queryConsultByUuid(uuid);
		if (null == consult) {

			log.error("未定位到咨询数据>>>uuid:{}", uuid);
			throw new BusinessException(ResEnum.NON_EXISTENT);
		}

		ConsultRes res = new ConsultRes();
		BeanUtils.copyProperties(consult, res);
		res.setCreatedTime(DateUtil.dateToStr(consult.getCreatedTime(), DateUtil.YYYY_MM_DD));

		String userUuid = TokenHelper.getUserUuid();
		ConsultOrder consultOrderParams = new ConsultOrder();
		consultOrderParams.setSts(StsEnum.ACTIVE.getValue());
		consultOrderParams.setConsultUuid(uuid);
		consultOrderParams.setCarOwnerUuid(userUuid);
		List<ConsultOrder> lls = consultOrderMapper.select(consultOrderParams);
		if (!CollectionUtils.isEmpty(lls)) {
			ConsultOrder consultOrder = lls.get(0);
			res.setOrderUuid(consultOrder.getUuid());
			res.setOrderSts(consultOrder.getOrderSts());

			Integer orderType = (null != consultOrder.getOrderType()) ? consultOrder.getOrderType() : Integer.MAX_VALUE;
			// 旁听或未支付 问答截取
			if (null != consultOrder && ConsultOrderTypeEnum.Audit.getType().equals(orderType)
					&& !OrderStsEnum.HAVE_PAID.getValue().equals(orderType)) {
				// 未支付 截取部分
				String consultDesc = consult.getConsultDesc();
				String answerDesc = consult.getAnswerDesc();
				if (StringUtils.isNotBlank(consultDesc) && consultDesc.length() > SUB_STR_LENGTH) {
					res.setConsultDesc(consultDesc.substring(0, SUB_STR_LENGTH) + "...");
				} else {
					res.setConsultDesc(consultDesc);
				}
				if (StringUtils.isNotBlank(answerDesc) && answerDesc.length() > SUB_STR_LENGTH) {
					res.setAnswerDesc(answerDesc.substring(0, SUB_STR_LENGTH) + "...");
				} else {
					res.setAnswerDesc(answerDesc);
				}
			}
		}

		ConsultImages i = new ConsultImages();
		i.setSts(StsEnum.ACTIVE.getValue());
		i.setConsultUuid(uuid);
		List<ConsultImages> imgList = consultImagesMapper.select(i);

		if (!CollectionUtils.isEmpty(imgList)) {
			List<String> ask = new ArrayList<>();
			List<String> ans = new ArrayList<>();
			imgList.stream().forEach(img -> {

				String imgUrl = img.getImgUrl();
				// 0 咨询 1 回答
				Integer imgType = img.getImgType();
				if (0 == imgType) {
					ask.add(imgUrl);
				} else if (1 == imgType) {
					ans.add(imgUrl);
				}
			});
			res.setAnswerImgList(ans);
			res.setConsultImgList(ask);
		}

		return ResultRes.success(res);
	}
}
