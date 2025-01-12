package com.car.system.web.mapper;

import com.car.system.web.model.SysDict;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

/**
 * @author xlj
 */
@Repository
public interface SysDictMapper extends Mapper<SysDict> {

    /**
     * 根据名称Like查询字典信息
     * @param descName
     * @return
     */
    SysDict selectLikeDescName(@Param("descName") String descName);
}
