package com.qubaopen.survey.controller.breadtalk;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.RandomStringUtils;
import org.hibernate.envers.Audited;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
	private String retrieveBreadTalkCode(@RequestParam int idx,
			@PageableDefault(size = 1) Pageable pageable) {
		
		logger.trace("-- 获取面包卷 --");
		
		Map<String, Object> filters = new HashMap<String, Object>();
		filters.put("status_equal", BreadTalk.Status.Unused);
		Page<BreadTalk> page = null;
		BreadTalk bt = null;
		for (int i = idx; i >= 0; i--) {
			filters.put("level_equal", BreadTalk.Level.values()[i]);
			page = breadTalkRepository.findAll(filters, pageable);
			if (page.getContent() != null && page.getContent().size() > 0) {
				bt = page.getContent().get(0);
			}
			if (bt != null) {
				break;
			}
		}
		
		if (bt == null) {
			return "{\"success\" : \"0\", \"message\" : \"亲，兑换卷已经被抢光啦～请期待其他活动～\"}";
		}
		
		if (bt != null) {
			bt.setReceiveTime(new Date());
			bt.setStatus(BreadTalk.Status.Using);
			breadTalkRepository.save(bt);
			
		}
		return "{\"success\" : \"1\", \"code\" : \""  + bt.getCode() + "\", \"money\" : \"" + bt.getMoney() + "\"}";
	}
	
	/**
	 * @param code
	 * @return
	 * 兑换面包卷
	 */
	@RequestMapping(value = "exchangeBreadTalk", method = RequestMethod.POST)
	private String exchangeBreadTalk(@RequestParam String code) {
		
		logger.trace("-- 兑换面包卷 --");
		
		code = code.toUpperCase();
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
		
		return "{\"success\" : \"1\", \"money\" : " + breadTalk.getMoney() + "}";
	}

	
	@RequestMapping(value = "generateCode", method = RequestMethod.GET)
	private String generateCode() {

		Set<BreadTalk> breadTalks = new HashSet<BreadTalk>();
		
		// 生成兑换码
		for (int i = 0; i < 8000; i++) {
			String code = "BT" + RandomStringUtils.random(8, "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ");
			BreadTalk bt = new BreadTalk();
			bt.setCode(code);
			bt.setLevel(BreadTalk.Level.Ten);
			bt.setStatus(BreadTalk.Status.Unused);
			bt.setMoney(10);
			breadTalks.add(bt);
			if (breadTalks.size() < (i + 1)) {
				i--;
			}
			if (breadTalks.size() == 8000) {
				break;
			}
		}
		for (int i = 0; i < 3000; i++) {
			String code = "BT" + RandomStringUtils.random(8, "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ");
			BreadTalk bt = new BreadTalk();
			bt.setCode(code);
			bt.setLevel(BreadTalk.Level.Twenty);
			bt.setStatus(BreadTalk.Status.Unused);
			bt.setMoney(20);
			breadTalks.add(bt);
			if (breadTalks.size() < (i + 1)) {
				i--;
			}
			if (breadTalks.size() == 3000) {
				break;
			}
		}
		for (int i = 0; i < 300; i++) {
			String code = "BT" + RandomStringUtils.random(8, "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ");
			BreadTalk bt = new BreadTalk();
			bt.setCode(code);
			bt.setLevel(BreadTalk.Level.Fifty);
			bt.setStatus(BreadTalk.Status.Unused);
			bt.setMoney(50);
			breadTalks.add(bt);
			if (breadTalks.size() < (i + 1)) {
				i--;
			}
			if (breadTalks.size() == 300) {
				break;
			}
		}
		for (int i = 0; i < 20; i++) {
			String code = "BT" + RandomStringUtils.random(8, "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ");
			BreadTalk bt = new BreadTalk();
			bt.setCode(code);
			bt.setLevel(BreadTalk.Level.Hundred);
			bt.setStatus(BreadTalk.Status.Unused);
			bt.setMoney(100);
			breadTalks.add(bt);
			if (breadTalks.size() < (i + 1)) {
				i--;
			}
			if (breadTalks.size() == 20) {
				break;
			}
		}
		breadTalkRepository.save(breadTalks);
		return "success";
	}
}
