package cn.qs.mapper.wechat;

import cn.qs.bean.wechat.Kindergarten;
import cn.qs.mapper.BaseSequenceMapper;

public interface KindergartenMapper extends BaseSequenceMapper<Kindergarten> {

	Kindergarten findByName(String name);

}
