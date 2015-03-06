package com.qubaopen.backend.controller.topic;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.time.DateUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.qubaopen.backend.repository.self.SelfRepository;
import com.qubaopen.backend.repository.topic.DailyDiscoveryRepository;
import com.qubaopen.backend.repository.topic.UserFavoriteRepository;
import com.qubaopen.backend.repository.topic.TopicRepository;
import com.qubaopen.backend.vo.FavoriteVo;
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
	
	@Autowired
	private UserFavoriteRepository favoriteRepository;
	
	@RequestMapping(value = "retrieveTopic", method = RequestMethod.GET)
	private Object retrieveTopic(@PageableDefault(page = 0, size = 20)
			Pageable pageable,
			@RequestParam(required = false) Boolean join) {
		
		Map<String, Object> result = new HashMap<String, Object>();
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		if (join != null && true == join) {
//			List<DailyDiscovery> dailyDiscoveries = dailyDiscoveryRepository.findDailyDiscoveryOrderByTimeAsc(pageable);
			
			List<Topic> topics = topicRepository.findTopicOrderBycreatedDateDesc(pageable);
			
			for (Topic topic : topics) {
				
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("id", topic.getId());
				map.put("name", topic.getName());
				map.put("content", topic.getContent());
				map.put("dailyTime", "");
				Date time = null;
				if (null != topic.getCreatedDate()) {
					DateTime sqlTime = topic.getCreatedDate();
					time = new Date(sqlTime.getMillis());
				}
				map.put("createdDate", time);
				list.add(map);
			}
		} else {
			List<FavoriteVo> topicVos = favoriteRepository.findTopicVos(pageable);
			for (FavoriteVo vo : topicVos) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("id", vo.getId());
				map.put("name", vo.getName());
				map.put("content", vo.getContent());
				map.put("dailyTime", vo.getFavoriteCreateDate());
				map.put("createdDate", vo.getCreateDate());
				list.add(map);
			}
		}
		
		result.put("success", "1");
		result.put("list", list);
		return result;
	}
	
	/**
	 * @param join
	 * @return
	 * 获取测评
	 */
	@RequestMapping(value = "retrieveSelf", method = RequestMethod.GET)
	private Object retrieveSelf(@PageableDefault(page = 0, size = 20)
			Pageable pageable,
			@RequestParam(required = false) Boolean join) {
		
		Map<String, Object> result = new HashMap<String, Object>();
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		if (join != null && true == join) {
//			List<DailyDiscovery> dailyDiscoveries = dailyDiscoveryRepository.findDailyDiscoveryOrderByTimeAsc(pageable);
			List<Self> selfs = selfRepository.findSelfOrderByCreatedDateDesc(pageable);
			
			for (Self self : selfs) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("id", self.getId());
				map.put("name", self.getTitle());
				map.put("content", self.getRemark());
				map.put("dailyTime", "");
				Date time = null;
				if (null != self.getCreatedDate()) {
					DateTime sqlTime = self.getCreatedDate();
					time = new Date(sqlTime.getMillis());
				}
				map.put("createdDate", time);
				list.add(map);
			}
		} else {
			List<FavoriteVo> favoriteVos = favoriteRepository.findSelfVos(pageable);
			
			for (FavoriteVo favoriteVo : favoriteVos) {
				Map<String, Object> map =  new HashMap<String, Object>();
				map.put("id", favoriteVo.getId());
				map.put("name", favoriteVo.getName());
				map.put("content", favoriteVo.getContent());
				map.put("dailyTime", favoriteVo.getFavoriteCreateDate());
				map.put("createdDate", favoriteVo.getCreateDate());
				list.add(map);
			}
		}
		
		result.put("success", "1");
		result.put("list", list);
		return result;
	}
	
	
	/**
	 * @param id
	 * @param time
	 * @param selfId
	 * @param topicId
	 * @return
	 * 新增修改发现
	 */
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
		
		if (dd.getSelf() != null || dd.getTopic() != null) { //存在一个就保存
			
			if (time == null) {
				DailyDiscovery exist = dailyDiscoveryRepository.findByMaxTime();
				Date date = new Date();
				// 多一天
				if (exist != null) {
					Calendar c = Calendar.getInstance();
					c.setTime(exist.getTime());
					c.add(Calendar.DATE, 1);
					
					dd.setTime(c.getTime());
				}
				// 设置当天
				if (exist == null) {
					dd.setTime(date);
				}
			} else {
				try {
					DailyDiscovery exist = dailyDiscoveryRepository.findByTime(DateUtils.parseDate(time, "yyyy-MM-dd"));
					if (exist != null) {
						return  "该日期已经被使用";
					}
					dd.setTime(DateUtils.parseDate(time, "yyyy-MM-dd"));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			dailyDiscoveryRepository.save(dd);
		}
		
		return "{\"success\" : \"1\",  \"discoveryId\" : " + dd.getId() + "}";
	}
	
	/**
	 * @return
	 */
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
