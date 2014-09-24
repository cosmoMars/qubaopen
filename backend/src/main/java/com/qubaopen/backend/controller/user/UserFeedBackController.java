package com.qubaopen.backend.controller.user;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qubaopen.backend.repository.user.UserFeedBackRepository;
import com.qubaopen.survey.entity.user.UserFeedBack;

@RestController
@RequestMapping("userFeedBack")
public class UserFeedBackController {

	private static final Logger logger = LoggerFactory.getLogger(UserFeedBackController.class);

	@Autowired
	private UserFeedBackRepository userFeedBackRepository;

	@SuppressWarnings("unused")
	private String findAllUserFeedBack(HttpServletRequest request, HttpServletResponse response) {
		logger.debug(" =======================  request = {}");
		Sort sort = new Sort(Direction.DESC, "createDate");
		List<UserFeedBack> userFeedBacks =  userFeedBackRepository.findAll(sort);
		
		request.setAttribute("userFeedBacks", userFeedBacks);
		return "success";
	}

} 
