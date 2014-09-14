package com.qubaopen.backend.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qubaopen.backend.service.InterestService;

@RestController
@RequestMapping("interest")
public class InterestController {
	
	private static final Logger logger = LoggerFactory.getLogger(InterestController.class);
	
	@Autowired
	private InterestService interestService;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@RequestMapping(value = "saveInterest", method = RequestMethod.POST)
	public void saveInterest(String json) {
		logger.debug(" =======================  json = {}", json);
		interestService.saveInterest(json);
	}

}
