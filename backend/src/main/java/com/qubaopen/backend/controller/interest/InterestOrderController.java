package com.qubaopen.backend.controller.interest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("backendInterestOrder")
public class InterestOrderController {

	@Autowired
	private ObjectMapper objectMapper;
	
	
	@Transactional
	@RequestMapping(value = "saveInterestOrder", method = RequestMethod.POST)
	private String saveInterestOrder(String json) {
		
		
		return null;
	}
}
