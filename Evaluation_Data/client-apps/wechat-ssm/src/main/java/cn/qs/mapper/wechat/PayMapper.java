package cn.qs.mapper.wechat;

import cn.qs.bean.wechat.Pay;
import cn.qs.mapper.BaseSequenceMapper;

public interface PayMapper extends BaseSequenceMapper<Pay> {

	Pay findByOrderId(String orderId);
}
