package cn.qs.service.wechat;

import java.util.List;
import java.util.Map;

import cn.qs.bean.wechat.Kindergarten;
import cn.qs.service.BaseSequenceService;

public interface KindergartenService extends BaseSequenceService<Kindergarten> {

	List<Map<String, Object>> listNamesAndIds();

	Kindergarten findByName(String name);

}
