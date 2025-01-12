package com.car.order.web.service.dtc;

import com.car.common.res.PageRes;
import com.car.common.res.ResultRes;
import com.car.order.client.request.dtc.AddDtcReq;
import com.car.order.client.request.dtc.CheckDtcReq;
import com.car.order.client.request.dtc.QueryDtcListReq;
import com.car.order.client.response.dtc.QueryDtcInfoRes;
import com.car.order.client.response.dtc.QueryDtcListRes;
import com.car.order.web.model.dtc.Dtc;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Created by Intellij IDEA.
 *
 * @author:  cjw
 * Date:  2021/2/17
 */
public interface DtcService {

    /**
     * 根据id查询详情
     * @param uuid
     * @return
     */
    ResultRes<QueryDtcInfoRes> getById(String uuid);


    /**
     * 新增DTC信息
     * @param req
     * @return
     */
    ResultRes<String> add(AddDtcReq req);


    /**
     * 根据id修改dtc信息
     * @param dtc
     * @param uuid
     * @return
     */
    ResultRes<String> updateById(AddDtcReq dtc, String uuid);


    /**
     * 删除dtc信息
     * @param uuid
     * @return
     */
    ResultRes<String> deleteById(String uuid);

    /**
     * 分页列表
     * @param req
     * @return
     */
    PageRes<List<QueryDtcListRes>> list(@RequestBody QueryDtcListReq req);

    /**
     * dtc审核
     * @param req
     * @return
     */
    ResultRes<String> checkDtc(CheckDtcReq req);
    /**
     * 批量导入
     * @param file
     * @return
     */
    ResultRes batchImport(MultipartFile file,String brandUuid,String dtcTypeUuid);
}
