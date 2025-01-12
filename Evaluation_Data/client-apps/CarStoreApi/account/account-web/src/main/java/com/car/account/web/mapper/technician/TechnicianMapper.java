package com.car.account.web.mapper.technician;

import com.car.account.client.request.technician.TechnicianListReq;
import com.car.account.client.response.technician.TechnicianAnswerRes;
import com.car.account.client.response.technician.TechnicianCountRes;
import com.car.account.client.response.technician.TechnicianListRes;
import com.car.account.web.model.technician.Technician;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author xlj
 */
@Repository
public interface TechnicianMapper extends Mapper<Technician> {

	/**
	 * 查询技师统计数据（注册/订单/案例/回答/支持）
	 * @param param
	 * @return
	 */
	TechnicianCountRes queryTechnicianCount(TechnicianListReq param);

	/**
	 * 查询技师列表
	 * @param param
	 * @return
	 */
	List<TechnicianListRes> queryTechnicianList(TechnicianListReq param);

	/**
	 * 查询问答技师列表
	 * @return
	 */
	List<Technician> queryTechnicianAnswerList();

	/**
	 * 查询布点技师信息列表
	 * @param latitude
	 * @param longitude
	 * @param zoomNum
	 * @return
	 */
	List<Technician> queryTechnicianLocationList(@Param("latitude") Float latitude, @Param("longitude") Float longitude,
			@Param("zoomNum") Integer zoomNum, @Param("brandUuid") String brandUuid,
			@Param("technologyType") String technologyType);
}
