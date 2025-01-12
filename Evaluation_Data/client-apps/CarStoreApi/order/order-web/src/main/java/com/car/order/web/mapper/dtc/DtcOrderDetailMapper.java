package com.car.order.web.mapper.dtc;

import com.car.order.web.model.dtc.DtcContent;
import com.car.order.web.model.dtc.DtcOrderDetail;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

/**
 * Created by Intellij IDEA.
 *
 * @author:  cjw
 * Date:  2021/2/19
 */
@Repository
public interface DtcOrderDetailMapper extends Mapper<DtcOrderDetail> {
}
