package cn.qs.service.impl.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.qs.bean.common.Message;
import cn.qs.mapper.BaseMapper;
import cn.qs.mapper.common.MessageMapper;
import cn.qs.service.common.MessageService;
import cn.qs.service.impl.AbastractBaseSequenceServiceImpl;

@Service
@Transactional
public class MessageServiceImpl extends AbastractBaseSequenceServiceImpl<Message> implements MessageService {

	@Autowired
	private MessageMapper messageMapper;

	@Override
	public BaseMapper<Message, Integer> getBaseMapper() {
		return messageMapper;
	}

}
