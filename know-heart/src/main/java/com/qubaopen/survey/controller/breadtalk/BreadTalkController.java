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
	@RequestMapping(value = "retrieveBreadTalkCode", method = RequestMethod.GET)
	private String retrieveBreadTalkCode() {
		
		logger.trace("-- 获取面包卷 --");
		
//		List<BreadTalk> breadTalks = breadTalkRepository.findByUsedOrderByIdAsc(true, pageable);
		
		Map<String, Object> filters = new HashMap<String, Object>();
		filters.put("used_equal", true);
		
		BreadTalk breadTalk = breadTalkRepository.findOneByFilters(filters);
		
		String code = breadTalk != null ? (breadTalk.getCode() != null ? breadTalk.getCode() : "亲，活动已经结束咯～") : "亲，活动已经结束咯～";
		
		if (breadTalk != null) {
			breadTalk.setReceiveTime(new Date());
			breadTalkRepository.save(breadTalk);
		}
		return "{\"success\" : \"1\", \"code\" : \""  + code + "\"}";
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
		if (breadTalk.isUsed() == true) {
			return "{\"success\" : \"0\", \"message\" : \"该面包卷已被兑换\"}";
		}
		
		breadTalk.setUsed(true);
		breadTalk.setExchangeTime(new Date());
		
		breadTalkRepository.save(breadTalk);
		
		return "{\"success\" : \"1\"}";
	}

}
