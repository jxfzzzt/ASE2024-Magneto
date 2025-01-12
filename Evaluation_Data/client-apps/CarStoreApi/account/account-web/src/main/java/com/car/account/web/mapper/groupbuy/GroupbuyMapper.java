package com.car.account.web.mapper.groupbuy;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.car.account.client.request.groupbuy.QueryGroupbuyListReq;
import com.car.account.client.response.groupbuy.GroupbuyRes;
import com.car.account.web.model.groupbuy.Groupbuy;

import tk.mybatis.mapper.common.Mapper;

@Repository
public interface GroupbuyMapper extends Mapper<Groupbuy> {

	List<GroupbuyRes> queryGroupbuyList(QueryGroupbuyListReq param);

	/**
	 * 将开始时间到了的团购开团
	 */
	public void updateStartGroup();
}
