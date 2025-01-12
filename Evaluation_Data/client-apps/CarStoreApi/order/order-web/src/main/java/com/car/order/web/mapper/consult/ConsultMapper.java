package com.car.order.web.mapper.consult;

import com.car.order.web.dto.consult.ConsultDto;
import com.car.order.web.model.consult.Consult;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author zhouz
 * @date 2021/1/2
 */
@Repository
public interface ConsultMapper extends Mapper<Consult> {
    /**
     * 查询咨询列表
     * @return
     */
    List<ConsultDto> queryShowConsultList();

    /**
     * 查询咨询信息
     * @param uuid
     * @return
     */
    ConsultDto queryConsultByUuid(@Param("uuid") String uuid);

    /**
     * 技師待回復問題列表
     * @param userUuid
     * @return
     */
    List<ConsultDto> queryPreAnswerList(@Param("userUuid") String userUuid);
}
