package com.car.system.web.mapper;

import com.car.system.client.request.area.AreaReq;
import com.car.system.client.response.area.AreaRes;
import com.car.system.web.model.SysArea;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;


/**
 * @author xlj
 */
@Repository
public interface SysAreaMapper extends Mapper<SysArea> {

    /**
     * g根据父节点查询区域信息
     * @param search
     * @return
     */
    List<SysArea> queryListByParent(SysArea search);

    /**
     * 根据名称，type模糊查询
     * @param name
     * @param areaType
     * @return
     */
    List<SysArea> queryAreaNameAreaType(@Param("name") String name, @Param("areaType") Integer areaType);

    /**
     * 查询地区列表
     * @param params
     * @return
     */
    List<AreaRes> queryAreaList(AreaReq params);
}
