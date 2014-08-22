package com.qubaopen.survey.interceptor;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.qubaopen.survey.entity.user.User;

public class LoginInterceptor implements HandlerInterceptor {

	private static final Logger logger = LoggerFactory.getLogger(LoginInterceptor.class);

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

		logger.info(" =================== request preHandle =========================");

		// 服务器配置
		if (StringUtils.equals(request.getRequestURI().split("/")[2], "users") || StringUtils.equals(request.getRequestURI().split("/")[2], "pic")) {
			return true;
		}

		// 本地配置
//		if (StringUtils.equals(request.getRequestURI().split("/")[1], "users") || StringUtils.equals(request.getRequestURI().split("/")[1], "pic")) {
//			return true;
//		}

		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("currentUser");
		if (null == user) {
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			PrintWriter writer = response.getWriter();
			writer.println("{\"success\": \"0\", \"message\": \"err000\"}");
			writer.close();
			return false;
		}

		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

	}

}
