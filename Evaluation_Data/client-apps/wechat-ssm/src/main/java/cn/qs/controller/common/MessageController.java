package cn.qs.controller.common;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.qs.bean.common.Message;
import cn.qs.controller.AbstractSequenceController;
import cn.qs.service.BaseService;
import cn.qs.service.common.MessageService;

@Controller
@RequestMapping("message")
public class MessageController extends AbstractSequenceController<Message> {

	@Autowired
	private MessageService messageService;

	@Override
	public String getViewBasePath() {
		return "message";
	}

	@Override
	public BaseService<Message, Integer> getBaseService() {
		return messageService;
	}

	@RequestMapping("/detail/{messageId}")
	public String getMessagedetail(ModelMap map, @PathVariable() Integer messageId, HttpServletRequest request) {
		Message message = messageService.findById(messageId);
		map.put("message", message);

		return getViewPath("detail");
	}

}
