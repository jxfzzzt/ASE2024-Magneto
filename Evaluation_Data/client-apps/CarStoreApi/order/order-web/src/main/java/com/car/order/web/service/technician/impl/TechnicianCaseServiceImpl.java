package com.car.order.web.service.technician.impl;

import com.alibaba.fastjson.JSON;
import com.car.account.client.feign.StoreFegin;
import com.car.account.client.feign.TechnicianFegin;
import com.car.account.client.response.store.StoreDetailRes;
import com.car.common.enums.ResEnum;
import com.car.common.enums.StsEnum;
import com.car.common.enums.UserTypeEnum;
import com.car.common.exception.BusinessException;
import com.car.common.res.PageRes;
import com.car.common.res.ResultRes;
import com.car.common.utils.TokenHelper;
import com.car.common.utils.UuidUtils;
import com.car.common.utils.token.LoginToken;
import com.car.order.client.enums.consult.OrderTypeEnum;
import com.car.order.client.request.content.AddContentReq;
import com.car.order.client.request.technician.AddTechnicianCaseReq;
import com.car.order.client.request.technician.CaseForTechnicianListRep;
import com.car.order.client.request.technician.CaseForVehicleListRep;
import com.car.order.client.request.technician.UpdateTechnicianCaseReq;
import com.car.order.client.response.technician.CaseForTechnicianItemRes;
import com.car.order.client.response.technician.CaseForVehicleItemRes;
import com.car.order.client.response.technician.TechnicianCaseRes;
import com.car.order.web.common.constants.Constants;
import com.car.order.web.common.constants.TechnicianConstants;
import com.car.order.web.mapper.instance.OrderCaseMapper;
import com.car.order.web.mapper.technician.TechnicianCaseImgMapper;
import com.car.order.web.mapper.technician.TechnicianCaseMapper;
import com.car.order.web.mapper.technician.TechnicianProfitMapper;
import com.car.order.web.model.instance.OrderCase;
import com.car.order.web.model.technician.cases.TechnicianCase;
import com.car.order.web.model.technician.cases.TechnicianCaseImg;
import com.car.order.web.model.technician.cases.TechnicianProfit;
import com.car.order.web.service.content.ContentService;
import com.car.order.web.service.technician.TechnicianCaseService;
import com.car.system.client.feign.SystemFeign;
import com.codingapi.txlcn.tc.annotation.TxcTransaction;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author zhouz
 * @date 2021/1/21
 */
@Slf4j
@Service
public class TechnicianCaseServiceImpl implements TechnicianCaseService {

	@Autowired
	TechnicianCaseMapper technicianCaseMapper;
	@Autowired
	TechnicianCaseImgMapper technicianCaseImgMapper;
	@Autowired
	TechnicianProfitMapper technicianProfitMapper;

	@Autowired
	ContentService contentService;
	@Resource
	OrderCaseMapper orderCaseMapper;

	@Autowired
	private StoreFegin storeFegin;
	@Autowired
	private SystemFeign systemFeign;

	@Override
	@Transactional(rollbackFor = Exception.class)
	@TxcTransaction
	public ResultRes<String> addTechnicianCase(AddTechnicianCaseReq addTechnicianCaseReq) {
		log.debug("新增案例");

		String uuid;
		String mobile = TokenHelper.getLoginToken().getUserMobile();
		if (UserTypeEnum.store.getType().equals(TokenHelper.getUserType())) {
			// 如果新增案例人员为店铺联系人,查询店铺信息
			StoreDetailRes storeDetailRes = queryStoreDetailByUser();
			uuid = storeDetailRes.getUuid();
		} else {
			uuid = TokenHelper.getUserUuid();
		}

//        ResultRes<TechnicianRes> technicianResResultRes = technicianFegin.queryTechnicianDetail(technicianUuid);
//        if(!technicianResResultRes.isSuccess()){
//            log.error("未定位到技师信息>>>technicianResResultRes:{}", JSON.toJSONString(technicianResResultRes));
//            throw new BusinessException(ResEnum.TECHNICIAN_NOT_EXIST);
//        }
//        TechnicianRes data = technicianResResultRes.getData();
//        String mobile = data.getMobile();

		// 验证车辆品牌与车辆型号是否存在 TODO 优化项

		TechnicianCase technicianCase = new TechnicianCase();
		technicianCase.setUuid(UuidUtils.getUuid());
		technicianCase.setCreatedBy(TokenHelper.getUserName());
		technicianCase.setCreatedTime(new Date());
		technicianCase.setSts(StsEnum.ACTIVE.getValue());
		technicianCase.setTechnicianUuid(uuid);
		BeanUtils.copyProperties(addTechnicianCaseReq, technicianCase);
		technicianCaseMapper.insert(technicianCase);

		List<String> addCaseImgList = addTechnicianCaseReq.getCaseImgList();
		if (!CollectionUtils.isEmpty(addCaseImgList)) {
			insertCaseImgList(technicianCase.getUuid(), addCaseImgList);
		}

		/*String content = String.format(Constants.TECHNICIAN_CASE_CONTENT_CHECK_KEY,
				new Object[] { addTechnicianCaseReq.getFaultDesc(), addTechnicianCaseReq.getIdeaProcess(),
						addTechnicianCaseReq.getSummary() });*/
		String content = addTechnicianCaseReq.getSummary() ;
		addContentCheck(uuid, mobile, OrderTypeEnum.EXAMPLE.getValue(), addTechnicianCaseReq.getTitle(), content,
				technicianCase.getUuid(), addCaseImgList);
		return ResultRes.success(technicianCase.getUuid());
	}

	@Override
	public ResultRes<String> updateTechnicianCase(UpdateTechnicianCaseReq updateTechnicianCaseReq) {
		log.debug("修改案例");
		TechnicianCase technicianCase = checkCaseIsActive(updateTechnicianCaseReq.getUuid());
		technicianCase.setLastUpdatedBy(TokenHelper.getUserName());
		technicianCase.setLastUpdatedTime(new Date());
		BeanUtils.copyProperties(updateTechnicianCaseReq, technicianCase);
		technicianCaseMapper.updateByPrimaryKeySelective(technicianCase);

		List<String> addCaseImgList = updateTechnicianCaseReq.getCaseImgList();
		if (!CollectionUtils.isEmpty(addCaseImgList)) {
			TechnicianCaseImg technicianCaseImg = new TechnicianCaseImg();
			technicianCaseImg.setCaseUuid(technicianCase.getUuid());
			technicianCaseImgMapper.delete(technicianCaseImg);

			insertCaseImgList(technicianCase.getUuid(), addCaseImgList);
		}
		return ResultRes.success(updateTechnicianCaseReq.getUuid());
	}

	@Override
	public ResultRes<TechnicianCaseRes> queryTechnicianCaseDetail(String uuid) {
		TechnicianCaseRes technicianCaseRes = technicianCaseMapper.queryTechnicianCaseDetail(uuid);
		if (!StringUtils.isEmpty(technicianCaseRes)) {

			// 查询收益
			Integer userType = TokenHelper.getUserType();
			//if (UserTypeEnum.technician.getType().equals(userType)) {
				TechnicianProfit technicianProfit = new TechnicianProfit();
				technicianProfit.setSts(StsEnum.ACTIVE.getValue());
				technicianProfit.setCaseUuid(technicianCaseRes.getUuid());
				technicianProfit = technicianProfitMapper.selectOne(technicianProfit);
				if (!StringUtils.isEmpty(technicianProfit)) {
					technicianCaseRes.setEarnings(technicianProfit.getAmt());
					technicianCaseRes.setSalesVolume(technicianProfit.getNum());
				}
			//} else if (UserTypeEnum.vehicle.getType().equals(userType)) {

				String userUuid = TokenHelper.getUserUuid();
				OrderCase oc = new OrderCase();
				oc.setSts(StsEnum.ACTIVE.getValue());
				oc.setCarOwnerUuid(userUuid);
				oc.setCaseUuid(uuid);

				OrderCase orderCase = orderCaseMapper.selectOne(oc);
				if (null != orderCase) {

					Integer orderSts = orderCase.getOrderSts();
					technicianCaseRes.setOrderSts(orderSts);
					technicianCaseRes.setOrderUuid(orderCase.getUuid());
				}
			//}
		}
		return ResultRes.success(technicianCaseRes);
	}

	/**
	 * 查询案例列表 技师查询
	 * @param pageReq
	 * @return
	 */
	@Override
	public PageRes<List<CaseForTechnicianItemRes>> queryCaseForTechnicianList(CaseForTechnicianListRep pageReq) {
		log.debug("查询案例列表 技师查询");

		if (UserTypeEnum.store.getType().equals(TokenHelper.getUserType())) {
			StoreDetailRes storeDetailRes = queryStoreDetailByUser();
			pageReq.setTechnicianUuid(storeDetailRes.getUuid());
		} else {
			if (StringUtils.isEmpty(pageReq.getTechnicianUuid())) {
				pageReq.setTechnicianUuid(TokenHelper.getUserUuid());
			}
		}

		PageHelper.startPage(pageReq.getPageNum(), pageReq.getPageSize());
		List<CaseForTechnicianItemRes> caseForTechnicianList = technicianCaseMapper.queryCaseForTechnicianList(pageReq);
		PageInfo<CaseForTechnicianItemRes> pageInfo = new PageInfo<>(caseForTechnicianList);
		return PageRes.success(caseForTechnicianList, pageInfo.getPageSize(), (int) pageInfo.getTotal(),
				pageInfo.getPages());
	}

	/**
	 * 查询案例列表 车主查询
	 * @param caseForVehicleListRep
	 * @return
	 */
	@Override
	public PageRes<List<CaseForVehicleItemRes>> queryCaseForVehicleList(CaseForVehicleListRep caseForVehicleListRep) {
		log.debug("查询案例列表 车主查询");
		PageHelper.startPage(caseForVehicleListRep.getPageNum(), caseForVehicleListRep.getPageSize());
		List<CaseForVehicleItemRes> caseForVehicleList = technicianCaseMapper
				.queryCaseForVehicleList(caseForVehicleListRep);

		boolean isApple = "1".equals(
				systemFeign.queryByUuid(com.car.common.utils.Constants.PRICE_ON_OFF_UUID).getData().getLableValue());

		if (isApple) {
			caseForVehicleList.forEach(o -> o.setAmt(BigDecimal.ZERO));
		}
		for (CaseForVehicleItemRes caseForVehicleItemRes : caseForVehicleList) {
			String userName = caseForVehicleItemRes.getTechnicianName();
			if (StringUtils.isEmpty(userName)) {
				userName = TechnicianConstants.TECHNICIAN_JOINT_STRING;
			} else {
				userName = userName.substring(0, 1) + TechnicianConstants.TECHNICIAN_JOINT_STRING;
			}
			caseForVehicleItemRes.setTechnicianName(userName);
		}



		PageInfo<CaseForVehicleItemRes> pageInfo = new PageInfo<>(caseForVehicleList);
		return PageRes.success(caseForVehicleList, pageInfo.getPageSize(), (int) pageInfo.getTotal(),
				pageInfo.getPages());
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	@TxcTransaction
	public void updateTechnicianCaseCheckSts(String caseUuid, Integer checkSts, String checkor) {
		log.debug("修改案例审核状态");
		TechnicianCase technicianCase = checkCaseIsActive(caseUuid);
		technicianCase.setCheckStatus(checkSts);
		technicianCase.setCheckor(checkor);
		technicianCaseMapper.updateByPrimaryKeySelective(technicianCase);
	}

	/**
	 * 增加内容审核
	 * @param technicianUuid
	 * @param technicianMobile
	 * @param orderType
	 * @param orderName
	 * @param checkContentDesc
	 * @param caseUuid
	 * @param resourcesList
	 */
	private void addContentCheck(String technicianUuid, String technicianMobile, Integer orderType, String orderName,
			String checkContentDesc, String caseUuid, List<String> resourcesList) {
		AddContentReq addContentReq = new AddContentReq();
		addContentReq.setUserUuid(technicianUuid);
		addContentReq.setMobile(technicianMobile);
		addContentReq.setOrderType(orderType);
		addContentReq.setOrderName(orderName);
		addContentReq.setContentDetail(checkContentDesc);
		addContentReq.setOrderUuid(caseUuid);
		addContentReq.setResourcesList(resourcesList);
		contentService.addContent(addContentReq);
	}

	/**
	 * 验证案例是否有效
	 * @param caseUuid
	 */
	private TechnicianCase checkCaseIsActive(String caseUuid) {
		TechnicianCase technicianCase = new TechnicianCase();
		technicianCase.setUuid(caseUuid);
		technicianCase.setSts(StsEnum.ACTIVE.getValue());
		technicianCase = technicianCaseMapper.selectOne(technicianCase);
		if (StringUtils.isEmpty(technicianCase)) {
			log.error("案例不存在 caseUuid:{}", caseUuid);
			throw new BusinessException(ResEnum.NON_EXISTENT);
		}

		return technicianCase;
	}

	/**
	 * 添加案例资源
	 * @param caseUuid
	 * @param addCaseImgList
	 */
	private void insertCaseImgList(String caseUuid, List<String> addCaseImgList) {
		TechnicianCaseImg technicianCaseImg;
		List<TechnicianCaseImg> caseImgList = new ArrayList<>(addCaseImgList.size());
		for (String caseImgUrl : addCaseImgList) {
			technicianCaseImg = new TechnicianCaseImg();
			technicianCaseImg.setUuid(UuidUtils.getUuid());
			technicianCaseImg.setCaseUuid(caseUuid);
			technicianCaseImg.setCreatedBy(TokenHelper.getUserName());
			technicianCaseImg.setCreatedTime(new Date());
			technicianCaseImg.setSts(StsEnum.ACTIVE.getValue());
			technicianCaseImg.setUrl(caseImgUrl);
			caseImgList.add(technicianCaseImg);
		}
		technicianCaseImgMapper.batchInsertTechnicianCaseImages(caseImgList);
	}

	/**
	 * 根据店铺登录用户查询店铺信息
	 * @return
	 */
	private StoreDetailRes queryStoreDetailByUser() {
		ResultRes<StoreDetailRes> storeDetailResResultRes = storeFegin.queryStoreDetailByUser();
		if (!storeDetailResResultRes.isSuccess()) {
			log.error("根据店铺联系人未定位到店铺信息 >>>storeDetailResResultRes:{}", JSON.toJSONString(storeDetailResResultRes));
			throw new BusinessException(ResEnum.STORE_INFO_NOT_BY_USER);
		}
		StoreDetailRes storeDetailRes = storeDetailResResultRes.getData();
		return storeDetailRes;
	}
}
