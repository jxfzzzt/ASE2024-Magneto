package com.car.account.web.mapper.technician;

import com.car.account.web.model.technician.TechnicianCertificateImages;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author zhouz
 * @date 2021/2/7
 */
@Repository
public interface TechnicianCertificateImagesMapper extends Mapper<TechnicianCertificateImages> {

    /**
     * 批量新增技师证书图片
     * @param insertList
     */
    void batchInsertCertificateImages(@Param("insertList") List<TechnicianCertificateImages> insertList);

}
