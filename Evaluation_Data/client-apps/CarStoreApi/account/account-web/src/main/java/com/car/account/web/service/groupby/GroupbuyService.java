package com.car.account.web.service.groupby;

import java.util.List;

import com.car.account.client.request.groupbuy.AddGroupbuyReq;
import com.car.account.client.request.groupbuy.QueryGroupbuyListReq;
import com.car.account.client.request.groupbuy.UpdateGroupbuyReq;
import com.car.account.client.response.groupbuy.GroupbuyRes;
import com.car.common.res.PageRes;
import com.car.common.res.ResultRes;

public interface GroupbuyService {

	ResultRes<String> add(AddGroupbuyReq addGroupbuyReq);

	ResultRes<String> update(UpdateGroupbuyReq updateGroupbuyReq);

	ResultRes<String> delete(String uuid);

	ResultRes<GroupbuyRes> queryByUuid(String uuid);

	PageRes<List<GroupbuyRes>> queryGroupbuyList(QueryGroupbuyListReq queryGroupbuyListReq);
}
