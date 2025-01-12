package com.car.account.web.service.groupby.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.car.account.client.request.groupbuy.AddGroupbuyReq;
import com.car.account.client.request.groupbuy.QueryGroupbuyListReq;
import com.car.account.client.request.groupbuy.UpdateGroupbuyReq;
import com.car.account.client.response.goods.GoodsRes;
import com.car.account.client.response.groupbuy.GroupbuyRes;
import com.car.account.web.mapper.groupbuy.GroupbuyGoodsMapper;
import com.car.account.web.mapper.groupbuy.GroupbuyMapper;
import com.car.account.web.model.groupbuy.Groupbuy;
import com.car.account.web.model.groupbuy.GroupbuyGoods;
import com.car.account.web.service.goods.GoodsService;
import com.car.account.web.service.groupby.GroupbuyService;
import com.car.common.enums.GroupbuyEnum;
import com.car.common.enums.ResEnum;
import com.car.common.enums.StsEnum;
import com.car.common.exception.BusinessException;
import com.car.common.res.PageRes;
import com.car.common.res.ResultRes;
import com.car.common.utils.StringUtil;
import com.car.common.utils.TokenHelper;
import com.car.common.utils.UuidUtils;
import com.car.order.client.feign.OrderGroupbuyFeign;
import com.codingapi.txlcn.tc.annotation.TxcTransaction;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@EnableScheduling
public class GroupbuyServiceImpl implements GroupbuyService {

	@Autowired
	private GroupbuyMapper groupbuyMapper;
	@Autowired
	private GroupbuyGoodsMapper groupbuyGoodsMapper;
	@Autowired
	private GoodsService goodsService;
	@Autowired
	private OrderGroupbuyFeign orderGroupbuyFeign;

	/**
	 * 功能：定时任务：团购开始时间到了，自动开团
	 */
	@Scheduled(fixedDelay = 60000)
	public void updateStartGroup() {
		groupbuyMapper.updateStartGroup();
	}

	/**
	 * 功能：定时任务：团购结束时间到了，自动成团
	 */
	@Scheduled(fixedDelay = 60000)
	public void updateEndGroup() {

		QueryGroupbuyListReq param = new QueryGroupbuyListReq();
		param.setGroupSts(Arrays.asList(1));
		List<GroupbuyRes> groupbuyResList = groupbuyMapper.queryGroupbuyList(param);

		for (GroupbuyRes groupbuyRes : groupbuyResList) {
			if (groupbuyRes.getEndTime().before(new Date())) {
				Groupbuy gb = new Groupbuy();
				gb.setUuid(groupbuyRes.getUuid());
				gb.setGroupSts(2);
				groupbuyMapper.updateByPrimaryKeySelective(gb);

				orderGroupbuyFeign.updateGroupbuyEnd(groupbuyRes.getUuid());
			}
		}

	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	@TxcTransaction
	public ResultRes<String> add(AddGroupbuyReq addGroupbuyReq) {

		String uuid = UuidUtils.getUuid();
		String userName = TokenHelper.getUserName();
		Date currDate = new Date();

		if (addGroupbuyReq.getStartTime().after(addGroupbuyReq.getEndTime())) {
			return ResultRes.error("结束日期不能在开始日期之前");
		}

		if (addGroupbuyReq.getUserNum() < 2) {
			return ResultRes.error("参团人数必须大于1");
		}

		if (currDate.after(addGroupbuyReq.getEndTime())) {
			return ResultRes.error("结束时间错误");
		}

		Groupbuy buy = new Groupbuy();
		BeanUtils.copyProperties(addGroupbuyReq, buy);
		buy.setSts(StsEnum.ACTIVE.getValue());
		buy.setCreatedBy(userName);
		buy.setCreatedTime(currDate);

		buy.setParticipateNum(0);
		buy.setUuid(uuid);
		buy.setUserUuid(TokenHelper.getUserUuid());
		setGroupSts(buy);
		Integer receiveMethod = null;

		List<String> goodsList = StringUtil.splitDefDistinctNotBlank(addGroupbuyReq.getGoodsUuids());
		for (String goodsUuid : goodsList) {

			GoodsRes res = goodsService.queryGoods(goodsUuid).getData();
			if (receiveMethod != null && !receiveMethod.equals(res.getReceiveMethod())) {
				log.error("添加团购失败>>>params:{};商品的配送方式不一致，不能加入一个团中", JSON.toJSONString(addGroupbuyReq));
				throw new BusinessException(ResEnum.INCONSISTENT_DELIVERY_METHODS);
			}
			receiveMethod = res.getReceiveMethod();

			GroupbuyGoods goods = new GroupbuyGoods();
			goods.setUuid(UuidUtils.getUuid());
			goods.setSts(StsEnum.ACTIVE.getValue());
			goods.setCreatedBy(userName);
			goods.setCreatedTime(currDate);
			goods.setGoodsUuid(goodsUuid);
			goods.setGroupbuyUuid(uuid);

			groupbuyGoodsMapper.insert(goods);
		}

		buy.setReceiveMethod(receiveMethod);
		groupbuyMapper.insert(buy);

		return ResultRes.success(uuid);
	}

	public void setGroupSts(Groupbuy buy) {
		buy.setGroupSts(GroupbuyEnum.WAIT.getValue());
		Date currDate = new Date();
		if (currDate.after(buy.getEndTime())) {
			buy.setGroupSts(GroupbuyEnum.OVER.getValue());
			return;
		}

		if (currDate.after(buy.getStartTime())) {
			buy.setGroupSts(GroupbuyEnum.PROCESSING.getValue());
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	@TxcTransaction
	public ResultRes<String> update(UpdateGroupbuyReq updateGroupbuyReq) {

		String userName = TokenHelper.getUserName();
		Date currDate = new Date();

		if (updateGroupbuyReq.getStartTime().after(updateGroupbuyReq.getEndTime())) {
			return ResultRes.error("结束日期不能在开始日期之前");
		}

		if (updateGroupbuyReq.getUserNum() < 2) {
			return ResultRes.error("参团人数必须大于1");
		}

		if (currDate.after(updateGroupbuyReq.getEndTime())) {
			return ResultRes.error("结束时间错误");
		}

		Groupbuy buy = new Groupbuy();
		BeanUtils.copyProperties(updateGroupbuyReq, buy);
		buy.setLastUpdatedTime(currDate);
		buy.setLastUpdatedBy(userName);
		setGroupSts(buy);

		List<String> goodsList = StringUtil.splitDefDistinctNotBlank(updateGroupbuyReq.getGoodsUuids());

		GroupbuyGoods query = new GroupbuyGoods();
		query.setSts(StsEnum.ACTIVE.getValue());
		query.setGroupbuyUuid(updateGroupbuyReq.getUuid());
		List<GroupbuyGoods> groupbuyGoodsList = groupbuyGoodsMapper.select(query);

		Integer receiveMethod = null;
		for (GroupbuyGoods groupbuyGoods : groupbuyGoodsList) {

			if (goodsList.indexOf(groupbuyGoods.getGoodsUuid()) == -1) {

				groupbuyGoods.setSts(StsEnum.INVALID.getValue());
				groupbuyGoods.setLastUpdatedBy(userName);
				groupbuyGoods.setLastUpdatedTime(currDate);
				groupbuyGoodsMapper.updateByPrimaryKey(groupbuyGoods);

			} else {
				goodsList.remove(groupbuyGoods.getGoodsUuid());

				if (receiveMethod == null) {
					GoodsRes res = goodsService.queryGoods(groupbuyGoods.getGoodsUuid()).getData();
					receiveMethod = res.getReceiveMethod();
				}
			}
		}

		for (String goodsUuid : goodsList) {

			GoodsRes res = goodsService.queryGoods(goodsUuid).getData();
			if (receiveMethod != null && !receiveMethod.equals(res.getReceiveMethod())) {
				log.error("修改团购失败>>>params:{};商品的配送方式不一致，不能加入一个团中", JSON.toJSONString(updateGroupbuyReq));
				throw new BusinessException(ResEnum.INCONSISTENT_DELIVERY_METHODS);
			}
			receiveMethod = res.getReceiveMethod();

			GroupbuyGoods goods = new GroupbuyGoods();
			goods.setUuid(UuidUtils.getUuid());
			goods.setSts(StsEnum.ACTIVE.getValue());
			goods.setCreatedBy(userName);
			goods.setCreatedTime(currDate);
			goods.setGoodsUuid(goodsUuid);
			goods.setGroupbuyUuid(buy.getUuid());

			groupbuyGoodsMapper.insert(goods);
		}

		buy.setReceiveMethod(receiveMethod);
		groupbuyMapper.updateByPrimaryKeySelective(buy);
		return ResultRes.success();
	}

	@Override
	public ResultRes<String> delete(String uuid) {

		Groupbuy buy = new Groupbuy();
		buy.setLastUpdatedBy(TokenHelper.getUserName());
		buy.setLastUpdatedTime(new Date());
		buy.setUuid(uuid);
		buy.setSts(StsEnum.INVALID.getValue());
		groupbuyMapper.updateByPrimaryKeySelective(buy);

		return ResultRes.success();
	}

	@Override
	public ResultRes<GroupbuyRes> queryByUuid(String uuid) {

		GroupbuyRes buyRes = new GroupbuyRes();

		Groupbuy buy = groupbuyMapper.selectByPrimaryKey(uuid);
		BeanUtils.copyProperties(buy, buyRes);

		List<GoodsRes> goodsRes = new ArrayList<>();

		GroupbuyGoods query = new GroupbuyGoods();
		query.setSts(StsEnum.ACTIVE.getValue());
		query.setGroupbuyUuid(uuid);
		List<GroupbuyGoods> groupbuyGoodsList = groupbuyGoodsMapper.select(query);

		for (GroupbuyGoods groupbuyGoods : groupbuyGoodsList) {
			ResultRes<GoodsRes> res = goodsService.queryGoods(groupbuyGoods.getGoodsUuid());
			goodsRes.add(res.getData());
		}

		buyRes.setGoodsRes(goodsRes);

		return ResultRes.success(buyRes);
	}

	@Override
	public PageRes<List<GroupbuyRes>> queryGroupbuyList(QueryGroupbuyListReq param) {

		PageHelper.startPage(param.getPageNum(), param.getPageSize());
		List<GroupbuyRes> goodsList = groupbuyMapper.queryGroupbuyList(param);

		if (!CollectionUtils.isEmpty(goodsList)) {
			Map<String, GoodsRes> uidGoodsMap = new HashMap<>();
			goodsList.forEach(g -> {
				List<GoodsRes> goodsRes = new ArrayList<>();

				GroupbuyGoods query = new GroupbuyGoods();
				query.setSts(StsEnum.ACTIVE.getValue());
				query.setGroupbuyUuid(g.getUuid());
				List<GroupbuyGoods> groupbuyGoodsList = groupbuyGoodsMapper.select(query);

				for (GroupbuyGoods by : groupbuyGoodsList) {
					GoodsRes grs = uidGoodsMap.get(by.getGoodsUuid());
					if (grs == null) {
						grs = goodsService.queryGoods(by.getGoodsUuid()).getData();
						uidGoodsMap.put(by.getGoodsUuid(), grs);
					}
					goodsRes.add(grs);
				}

				g.setGoodsRes(goodsRes);
			});
		}

		PageInfo<GroupbuyRes> pageInfo = new PageInfo<>(goodsList);
		return PageRes.success(goodsList, pageInfo.getPageSize(), (int) pageInfo.getTotal(), pageInfo.getPages());
	}

}
