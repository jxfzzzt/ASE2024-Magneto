package com.car.order.web.service.groupbuy.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.car.account.client.enums.goods.ImgTypeEnum;
import com.car.common.res.PageRes;
import com.car.common.res.ResultRes;
import com.car.order.client.request.order.groupbuy.CreateOrderGroupbuyReq;
import com.car.order.client.request.order.groupbuy.QueryOrderGroupbuyListReq;
import com.car.order.client.response.order.goods.GoodsRes;
import com.car.order.client.response.order.groupbuy.GroupbuyRes;
import com.car.order.client.response.order.groupbuy.OrderGroupbuyRes;
import com.car.order.web.mapper.goods.GoodsImagesMapper;
import com.car.order.web.mapper.goods.GoodsMapper;
import com.car.order.web.mapper.groupbuy.GroupbuyGoodsMapper;
import com.car.order.web.mapper.groupbuy.GroupbuyMapper;
import com.car.order.web.mapper.groupbuy.OrderGroupbuyMapper;
import com.car.order.web.model.goods.GoodsImages;
import com.car.order.web.model.groupbuy.Groupbuy;
import com.car.order.web.model.groupbuy.GroupbuyGoods;
import com.car.order.web.service.groupbuy.OrderGroupbuyService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OrderGroupbuyServiceImpl implements OrderGroupbuyService {

	@Autowired
	private OrderGroupbuyMapper orderGroupbuyMapper;
	@Autowired
	private GroupbuyMapper groupbuyMapper;
	@Autowired
	private GoodsMapper goodsMapper;
	@Autowired
	private GoodsImagesMapper goodsImagesMapper;
	@Autowired
	private GroupbuyGoodsMapper groupbuyGoodsMapper;

	@Override
	public String create(CreateOrderGroupbuyReq params) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResultRes<OrderGroupbuyRes> queryByUuid(String uuid) {

		QueryOrderGroupbuyListReq param = new QueryOrderGroupbuyListReq();
		param.setUuid(uuid);
		OrderGroupbuyRes res = orderGroupbuyMapper.queryOrderGroupbuyList(param).get(0);
		res.setGoodsRes(findGoodsByGroupId(res.getGroupbuyUuid()));

		return ResultRes.success(res);
	}

	@Override
	public PageRes<List<OrderGroupbuyRes>> queryOrderGroupbuyList(QueryOrderGroupbuyListReq param) {
		PageHelper.startPage(param.getPageNum(), param.getPageSize());
		List<OrderGroupbuyRes> goodsList = orderGroupbuyMapper.queryOrderGroupbuyList(param);

		if (!CollectionUtils.isEmpty(goodsList)) {

			Optional<List<GoodsRes>> gList = Optional.empty();
			Optional<GroupbuyRes> gRes = Optional.empty();
			;
			if (StringUtils.isNotBlank(param.getGroupbuyUuid())) {
				gList = Optional.ofNullable(findGoodsByGroupId(param.getGroupbuyUuid()));
				gRes = Optional.ofNullable(findGroupbuyRes(param.getGroupbuyUuid()));
			}

			for (OrderGroupbuyRes orderGroupbuyRes : goodsList) {
				orderGroupbuyRes.setGoodsRes(gList.orElse(findGoodsByGroupId(orderGroupbuyRes.getGroupbuyUuid())));
				orderGroupbuyRes.setGroupbuyRes(gRes.orElse(findGroupbuyRes(param.getGroupbuyUuid())));
			}
		}

		PageInfo<OrderGroupbuyRes> pageInfo = new PageInfo<>(goodsList);
		return PageRes.success(goodsList, pageInfo.getPageSize(), (int) pageInfo.getTotal(), pageInfo.getPages());
	}

	private GroupbuyRes findGroupbuyRes(String groupUuid) {
		Groupbuy buy = groupbuyMapper.selectByPrimaryKey(groupUuid);
		GroupbuyRes res = new GroupbuyRes();
		if (buy != null) {
			BeanUtils.copyProperties(buy, res);
		}
		return res;
	}

	private List<GoodsRes> findGoodsByGroupId(String groupUuid) {

		List<GoodsRes> retlist = new ArrayList<>();

		GroupbuyGoods query = new GroupbuyGoods();
		query.setSts(0);
		query.setGroupbuyUuid(groupUuid);
		List<GroupbuyGoods> gList = groupbuyGoodsMapper.select(query);
		for (GroupbuyGoods groupbuyGoods : gList) {
			GoodsRes res = new GoodsRes();
			BeanUtils.copyProperties(goodsMapper.selectByPrimaryKey(groupbuyGoods.getGoodsUuid()), res);

			// 查询商品对应图片
			List<GoodsImages> goodsImagesList = goodsImagesMapper.queryListByGoodsId(groupbuyGoods.getGoodsUuid());
			String imgUrl = null;
			if (!CollectionUtils.isEmpty(goodsImagesList)) {
				for (GoodsImages goodsImages : goodsImagesList) {
					if (ImgTypeEnum.MAIN_GRAPH.getValue().equals(goodsImages.getImgType())) {
						imgUrl = goodsImages.getImgPath();
						if (!StringUtils.isEmpty(imgUrl)) {
							break;
						}
					}
				}
			}

			res.setImgUrl(imgUrl);

			retlist.add(res);
		}

		return retlist;
	}

	@Override
	public ResultRes<String> updateGroupbuyEnd(String uuid) {

		orderGroupbuyMapper.updateGroupbuyEnd1To2(uuid);
		orderGroupbuyMapper.updateGroupbuyEnd0To4(uuid);

		return ResultRes.success();
	}
}
