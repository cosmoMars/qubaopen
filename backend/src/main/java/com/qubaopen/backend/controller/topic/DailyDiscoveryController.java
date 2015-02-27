package com.qubaopen.backend.controller.topic;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.qubaopen.backend.repository.self.SelfRepository;
import com.qubaopen.backend.repository.topic.DailyDiscoveryRepository;
import com.qubaopen.backend.repository.topic.TopicRepository;
import com.qubaopen.survey.entity.self.Self;
import com.qubaopen.survey.entity.topic.DailyDiscovery;
import com.qubaopen.survey.entity.topic.Topic;


@RestController
@RequestMapping("dailyDiscovery")
public class DailyDiscoveryController {
	
	@Autowired
	private TopicRepository topicRepository;

	@Autowired
	private DailyDiscoveryRepository dailyDiscoveryRepository;
	
	@Autowired
	private SelfRepository selfRepository;
	
	@RequestMapping(value = "retrieveTopic", method = RequestMethod.GET)
	private Object retrieveTopic(@RequestParam(required = false) Boolean join) {
		
		Map<String, Object> result = new HashMap<String, Object>();
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		if (join != null && true == join) {
			List<DailyDiscovery> dailyDiscoveries = dailyDiscoveryRepository.findDailyDiscoveryOrderByTimeAsc();
			
			for (DailyDiscovery dailyDiscovery : dailyDiscoveries) {
				
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("id", dailyDiscovery.getTopic().getId());
				map.put("name", dailyDiscovery.getTopic().getName());
				map.put("content", dailyDiscovery.getTopic().getContent());
				map.put("dailyTime", dailyDiscovery.getTime());
				map.put("createdDate", dailyDiscovery.getTopic().getCreatedDate() != null ? dailyDiscovery.getTopic().getCreatedDate(): "");
				list.add(map);
				
			}
		} else {
			List<Topic> topics = topicRepository.findTopicOrderBycreatedDateDesc();
			for (Topic topic : topics) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("id", topic.getId());
				map.put("name", topic.getName());
				map.put("content", topic.getContent());
				map.put("createdDate", topic.getCreatedDate() != null ? topic.getCreatedDate(): "");
				list.add(map);
			}
		}
		
		result.put("success", "1");
		result.put("list", list);
		return result;
	}
	
	@RequestMapping(value = "retrieveSelf", method = RequestMethod.GET)
	private Object retrieveSelf(@RequestParam(required = false) Boolean join) {
		
		Map<String, Object> result = new HashMap<String, Object>();
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		if (join != null && true == join) {
			List<DailyDiscovery> dailyDiscoveries = dailyDiscoveryRepository.findDailyDiscoveryOrderByTimeAsc();
			for (DailyDiscovery dailyDiscovery : dailyDiscoveries) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("id", dailyDiscovery.getSelf().getId());
				map.put("name", dailyDiscovery.getSelf().getTitle());
				map.put("content", dailyDiscovery.getSelf().getRemark());
				map.put("dailyTime", dailyDiscovery.getTime());
				map.put("createdDate", dailyDiscovery.getSelf().getCreatedDate() != null ? dailyDiscovery.getSelf().getCreatedDate(): "");
				list.add(map);
			}
		} else {
			List<Self> selfs = selfRepository.findSelfOrderByCreatedDateDesc();
			
			for (Self self : selfs) {
				Map<String, Object> map =  new HashMap<String, Object>();
				map.put("id", self.getId());
				map.put("name", self.getTitle());
				map.put("content", self.getRemark());
				map.put("createdDate", self.getCreatedDate() != null ? self.getCreatedDate(): "");
				list.add(map);
			}
		}
		
		result.put("success", "1");
		result.put("list", list);
		return result;
	}
	
	
	@RequestMapping(value = "generateDailyDiscovery", method = RequestMethod.POST)
	private Object generateDailyDiscovery(@RequestParam(required = false) Long id,
			@RequestParam(required = false) String time,
			@RequestParam(required = false) Long selfId,
			@RequestParam(required = false) Long topicId) {
		
		DailyDiscovery dd = null;
		
		if (id != null) {
			dd = dailyDiscoveryRepository.findOne(id);
		} else {
			dd = new DailyDiscovery();
		}

		if (selfId != null) {
			Self s = selfRepository.findOne(selfId);
			dd.setSelf(s);
		}
		if (topicId != null) {
			Topic t = topicRepository.findOne(topicId);
			dd.setTopic(t);
		}
		
		if (dd.getSelf() != null || dd.getTopic() != null) { //存在一个保存
			
			if (time == null) {
				DailyDiscovery exist = dailyDiscoveryRepository.findByMaxTime();
				
				Date date = new Date();
				// 多一天
				if (DateUtils.isSameDay(exist.getTime(), date)) {
					Calendar c = Calendar.getInstance();
					c.setTime(date);
					c.add(Calendar.DATE, 1);
					
					dd.setTime(c.getTime());
				}
				// 设置当天
				if (!DateUtils.isSameDay(exist.getTime(), date) && exist.getTime().getTime() < date.getTime()) {
					dd.setTime(date);
				}
			} else {
				try {
					dd.setTime(DateUtils.parseDate(time, "yyyy-MM-dd"));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			dailyDiscoveryRepository.save(dd);
		}
		
		return "{\"success\" : \"1\"}";
	}
	
	@RequestMapping(value = "retrieveDailyDiscoverys", method = RequestMethod.GET)
	private Object retrieveDailyDiscoverys() {
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		
		List<DailyDiscovery> dailyDiscoveries = dailyDiscoveryRepository.findDailyDiscoveryOrderByTimeDesc();
		
		for (DailyDiscovery dailyDiscovery : dailyDiscoveries) {
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", dailyDiscovery.getId());
			map.put("time", dailyDiscovery.getTime());
			map.put("selfId", dailyDiscovery.getSelf().getId());
			map.put("selfName", dailyDiscovery.getSelf().getTitle());
			map.put("topicId", dailyDiscovery.getTopic().getId());
			map.put("topicName", dailyDiscovery.getTopic().getName());
			list.add(map);
			
			
		}
		result.put("success", "1");
		result.put("list", list);
		return result;
	}
}
