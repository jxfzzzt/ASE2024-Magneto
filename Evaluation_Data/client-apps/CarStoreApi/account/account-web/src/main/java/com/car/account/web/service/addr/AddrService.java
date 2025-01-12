package com.car.account.web.service.addr;

import com.car.account.client.request.addr.ModifyAddrReq;
import com.car.account.client.request.addr.ReceiveAddrReq;
import com.car.account.client.response.addr.ReceiveAddrRes;
import com.car.account.web.model.addr.ReceiveAddr;

import java.util.List;

/**
 * @author zhangyp
 * @date 2021/1/14 22:57
 */
public interface AddrService {

    /**
     * 新增地址
     * @param params
     * @return
     */
    ReceiveAddrRes addAddr(ReceiveAddrReq params);

    /**
     * 修改地址
     * @param params
     * @return
     */
    ReceiveAddrRes updateAddr(ModifyAddrReq params);

    /**
     * 逻辑删除地址
     * @param uuid
     */
    void disableAddr(String uuid);

    /**
     * 查询用户地址列表
     * @return
     */
    List<ReceiveAddrRes> listAddr();

    /**
     * 查询地址详情
     * @param uuid
     * @return
     */
    ReceiveAddrRes queryAddrDetail(String uuid);
}
