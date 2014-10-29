package com.qubaopen.backend.controller.user;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.qubaopen.backend.repository.user.UserFeedBackRepository;
import com.qubaopen.backend.vo.UserFeedBackVo;
import com.qubaopen.survey.entity.user.UserFeedBack;

@RestController
@RequestMapping("userFeedBack")
public class UserFeedBackController {

	private static final Logger logger = LoggerFactory.getLogger(UserFeedBackController.class);

	@Autowired
	private UserFeedBackRepository userFeedBackRepository;

	@RequestMapping(value = "findAllUserFeedBack", method = RequestMethod.GET)
	public String findAllUserFeedBack(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		logger.debug(" =======================  request = {}");
		Sort sort = new Sort(Direction.DESC, "feedBackTime");
		List<UserFeedBack> userFeedBacks = userFeedBackRepository.findAll(sort);

		request.setAttribute("userFeedBacks", userFeedBacks);
		request.getRequestDispatcher("/userFeedBack.jsp").forward(request, response);
		return "success";
	}
	
	@RequestMapping(value = "getUserFeedBack", method = RequestMethod.GET)
	public Map<String, Object> getUserFeedBack() {
		logger.debug(" =======================  request = {}");
		Sort sort = new Sort(Direction.DESC, "feedBackTime");
		List<UserFeedBack> userFeedBacks = userFeedBackRepository.findAll(sort);
		
		List<UserFeedBackVo> userFeedBackVos = new ArrayList<UserFeedBackVo>();
		for (UserFeedBack u : userFeedBacks) {
			UserFeedBackVo vo = new UserFeedBackVo();
			vo.setContent(u.getContent());
			vo.setUserId(u.getUser().getId());
			if (u.getFeedBackType() != null) {
				if (0 == u.getFeedBackType().ordinal()) {
					vo.setFeedBackType("普通用户");
				} else {
					vo.setFeedBackType("企业用户");
				}
			}
			if (u.getContactMethod() != null)
				vo.setContactMethod(u.getContactMethod());
			if (u.getFeedBackTime() != null) {
				vo.setFeedBackTime(DateFormatUtils.format(u.getFeedBackTime(), "yyyy-MM-dd HH:mm:ss"));
			}
			if (u.getCreatedDate() != null) {
				vo.setCreateDate(u.getCreatedDate().toString("yyyy-MM-dd HH:mm:ss"));
			}
			userFeedBackVos.add(vo);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("success", "1");
		map.put("feedBacks", userFeedBackVos);
		return map;
	}
}
