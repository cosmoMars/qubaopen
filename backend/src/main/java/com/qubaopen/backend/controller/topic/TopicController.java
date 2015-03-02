package com.qubaopen.backend.controller.topic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.qubaopen.backend.repository.topic.DailyDiscoveryRepository;
import com.qubaopen.backend.repository.topic.TopicRepository;
import com.qubaopen.core.controller.AbstractBaseController;
import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.topic.Topic;

@RestController
@RequestMapping("topic")
public class TopicController extends AbstractBaseController<Topic, Long> {
	
	@Autowired
	private TopicRepository topicRepository;
	
	@Autowired
	private DailyDiscoveryRepository dailyTaskRepository;
	
	@Override
	protected MyRepository<Topic, Long> getRepository() {
		return topicRepository;
	}

	
	/**
	 * @param id
	 * @param name
	 * @param content
	 * @return
	 * 生成专题
	 */
	@RequestMapping(value = "generateTopic", method = RequestMethod.POST)
	private Object generateTopic(@RequestParam(required = false) Long id,
			@RequestParam(required = false) String name, @RequestParam(required = false) String content) {

		Topic topic = null;
		if (id != null) {
			topic = topicRepository.findOne(id);
		} else {
			topic = new Topic();
		}
		if (name != null)
			topic.setName(name);
		if (content != null)
			topic.setContent(content);

		topicRepository.save(topic);

		return "{\"success\" : \"1\"}";
	}

}
