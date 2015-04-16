package com.qubaopen.backend.controller.topic;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.qubaopen.backend.repository.topic.DailyDiscoveryRepository;
import com.qubaopen.backend.repository.topic.TopicRepository;
import com.qubaopen.backend.utils.UploadUtils;
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
	

    @Value("${topic_url}")
    private String topic_url;
	
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
			@RequestParam(required = false) String name, 
			@RequestParam(required = false) String content,
			@RequestParam(required = false) MultipartFile multipartFile) {
		Map<String, Object> result = new HashMap<String, Object>();
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

		if (null != multipartFile) {
			String uname;
			if (null == topic.getId()) {
				uname = topic_url;
			} else {
				uname = topic_url + topic.getId();
			}

			String picUrl;
			try {
				picUrl = UploadUtils.uploadTo7niu(1, uname, multipartFile.getInputStream());
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			topic.setPicUrl(picUrl);
		}
		
		topicRepository.save(topic);

		result.put("success", "1");
		return result;
	}

}
