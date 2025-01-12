package com.car.order.web.mapper.addr;


import com.car.order.web.model.addr.ReceiveAddr;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface ReceiveAddrMapper extends Mapper<ReceiveAddr> {

    List<ReceiveAddr> queryAddrList(ReceiveAddr params);

    /**
     * 查询地区
     * @param areaCode
     * @return
     */
    String selectAreaNameByAreaCode(String areaCode);
}