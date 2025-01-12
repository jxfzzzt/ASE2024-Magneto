package com.car.order.web.mapper.technician;


import com.car.order.web.model.technician.cases.TechnicianCaseImg;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface TechnicianCaseImgMapper extends Mapper<TechnicianCaseImg> {
    /**
     * 批量新增案例相关图片
     * @param caseImgList
     */
    void batchInsertTechnicianCaseImages(@Param("insertList") List<TechnicianCaseImg> caseImgList);


    /**
     * 根据技师案例uuid查询相关文件/图片url
     * @param caseUuid
     * @return
     */
    List<String> queryCaseImages(@Param("caseUuid") String caseUuid);
}