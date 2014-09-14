package com.qubaopen.backend.controller;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
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
		
		try {
			JsonNode jsonNode = objectMapper.readTree(json);
			ArrayNode questions = (ArrayNode) jsonNode.path("questions");
			for (JsonNode question : questions) {
				System.out.println(question.get("questionNo"));
				System.out.println(question.get("questionContent"));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		interestService.saveInterest(json);
	}

}
