package com.car.account.web.mapper.profit;

import com.car.account.web.dto.profit.ClassifyProfitDto;
import com.car.account.web.model.profit.ProfitStream;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface ProfitStreamMapper extends Mapper<ProfitStream> {


    /**
     * 统计收益
     * @param userUuid
     * @param userType
     * @param streamType
     * @return
     */
    List<ClassifyProfitDto> staticsClassifyProfitAmt(@Param("userUuid") String userUuid,@Param("userType") Integer userType,@Param("streamType") Integer streamType);

}