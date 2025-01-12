package com.car.account.web.mapper.technician;

import com.car.account.client.response.technician.TechnicianBrandRes;
import com.car.account.web.model.technician.TechnicianBrand;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;


/**
 * @author xlj
 */
@Repository
public interface TechnicianBrandMapper extends Mapper<TechnicianBrand> {

    /**
     * 根据技师ID查询维修品牌信息
     * @param technicianUuid
     * @return
     */
    List<TechnicianBrandRes> selectBrandByTechnicianUuid(@Param("technicianUuid") String technicianUuid);

    /**
     * 根据技师UUID查询技师被预约次数
     * @param uuid
     * @return
     */
    int queryShareNum(String uuid);
}
