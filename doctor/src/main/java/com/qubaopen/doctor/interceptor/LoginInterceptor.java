package com.qubaopen.doctor.interceptor;

import com.qubaopen.survey.entity.doctor.Doctor;
import com.qubaopen.survey.entity.hospital.Hospital;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;

public class LoginInterceptor implements HandlerInterceptor {

	private static final Logger logger = LoggerFactory.getLogger(LoginInterceptor.class);

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

//		logger.info(" =================== request preHandle =========================");
		
//		System.out.println(request.getSession().getId() + "-------------------------------");

//		System.out.println(request.getRequestURI() + "====================");
//		System.out.println(request.getSession().getServletContext().getRealPath("/"));
//		String[] property = System.getProperty("user.dir").split("/");
//		System.out.println(org.apache.commons.lang3.StringUtils.join(property,"/"));
		if (request.getRequestURI().contains("uDoctor") || request.getRequestURI().contains("dweb") || request.getRequestURI().contains("uHospital")
				|| request.getRequestURI().contains("pic") || request.getRequestURI().contains("doctorDir") || request.getRequestURI().contains("recordDir") || request.getRequestURI().contains("systemVersions")) {
			return true;
		}

		HttpSession session = request.getSession();
		Doctor doctor = (Doctor) session.getAttribute("currentDoctor");
		Hospital hospital = (Hospital) session.getAttribute("currentHospital");
		if (null == doctor && null == hospital) {
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
