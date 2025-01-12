package com.car.account.web.mapper.store;

import com.car.account.client.request.store.QueryStoreListReq;
import com.car.account.client.response.store.QueryShareStoreListRes;
import com.car.account.client.response.store.QueryStoreListRes;
import com.car.account.client.response.store.StoreDetailRes;
import com.car.account.web.model.store.Store;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author zhouz
 * @date 2020/12/19
 */
@Repository
public interface StoreMapper extends Mapper<Store> {

	/**
	 * 查询店铺列表
	 * @param param
	 * @param checkSts
	 * @return
	 */
	List<QueryStoreListRes> queryStoreList(@Param("param") QueryStoreListReq param,
			@Param("checkSts") Integer checkSts);

	/**
	 * 查询店铺详情
	 * @param uuid
	 * @return
	 */
	StoreDetailRes queryStoreDetail(String uuid);

	/**
	 * 根据用户uuid 查询店铺信息
	 * @param userUuid
	 * @return
	 */
	String queryStoreUuidByUserUuid(String userUuid);

	/**
	 * 查询当前阈值范围内的店铺信息
	 * @param distance
	 * @return
	 */
	List<QueryShareStoreListRes> queryShareStoreList(@Param("distance") Integer distance,
			@Param("longitude") Float longitude, @Param("latitude") Float latitude,
			@Param("brandUuid") String brandUuid, @Param("storeType") String storeType, @Param("shareStationTypeName") String shareStationTypeName);
}
