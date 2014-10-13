package com.qubaopen.backend.controller.user;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.qubaopen.backend.repository.user.UserFeedBackRepository;
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
//		response.setContentType("text/html; charset=UTF-8");
//		response.sendRedirect("/userFeedBack.jsp");
		return "success";
	}
}
