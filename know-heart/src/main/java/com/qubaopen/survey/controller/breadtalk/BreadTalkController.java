package com.qubaopen.survey.controller.breadtalk;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.envers.Audited;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.qubaopen.core.controller.AbstractBaseController;
import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.bread.BreadTalk;
import com.qubaopen.survey.repository.breadtalk.BreadTalkRepository;

@RestController
@RequestMapping("breakTalk")
@Audited
public class BreadTalkController extends AbstractBaseController<BreadTalk, Long> {

	private static Logger logger = LoggerFactory.getLogger(BreadTalkController.class);
	
	@Autowired
	private BreadTalkRepository breadTalkRepository;
	
	@Override
	protected MyRepository<BreadTalk, Long> getRepository() {
		return breadTalkRepository;
	}
	
	/**
	 * @return
	 * 获取面包卷
	 */
	@RequestMapping(value = "retrieveBreadTalkCode", method = RequestMethod.POST)
	private String retrieveBreadTalkCode(@RequestParam int idx) {
		
		logger.trace("-- 获取面包卷 --");
		
//		List<BreadTalk> breadTalks = breadTalkRepository.findByUsedOrderByIdAsc(true, pageable);
		
		Map<String, Object> filters = new HashMap<String, Object>();
		filters.put("status_equal", BreadTalk.Status.Unused);
		BreadTalk breadTalk = null;
		for (int i = idx; i >= 0; i--) {
			filters.put("level_equal", BreadTalk.Level.values()[i]);
			breadTalk = breadTalkRepository.findOneByFilters(filters);
			if (breadTalk != null) {
				break;
			}
		}
		
		if (breadTalk == null) {
			return "{\"success\" : \"0\", \"message\" : \"亲，兑换卷已经被抢光啦～请期待其他活动～\"}";
		}
		
		if (breadTalk != null) {
			breadTalk.setReceiveTime(new Date());
			breadTalk.setStatus(BreadTalk.Status.Using);
			breadTalkRepository.save(breadTalk);
			
		}
		return "{\"success\" : \"1\", \"code\" : \""  + breadTalk.getCode() + "\", \"money\" : \"" + breadTalk.getMoney() + "\"}";
	}
	
	/**
	 * @param code
	 * @return
	 * 兑换面包卷
	 */
	@RequestMapping(value = "exchangeBreadTalk", method = RequestMethod.POST)
	private String exchangeBreadTalk(@RequestParam String code) {
		
		logger.trace("-- 兑换面包卷 --");
		
		BreadTalk breadTalk = breadTalkRepository.findByCode(code);
		
		if (breadTalk == null) {
			return "{\"success\" : \"0\", \"message\" : \"该面包卷不存在～\"}";
		}
		if (breadTalk.getStatus() == BreadTalk.Status.Unused) {
			return "{\"success\" : \"0\", \"message\" : \"该面包卷不存在～\"}";
		}
		if (breadTalk.getStatus() == BreadTalk.Status.Used) {
			return "{\"success\" : \"0\", \"message\" : \"该面包卷已被兑换\"}";
		}
		
		breadTalk.setStatus(BreadTalk.Status.Used);
		breadTalk.setExchangeTime(new Date());
		
		breadTalkRepository.save(breadTalk);
		
		return "{\"success\" : \"1\"}";
	}

}
