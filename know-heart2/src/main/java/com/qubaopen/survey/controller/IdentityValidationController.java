package com.qubaopen.survey.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.qubaopen.survey.service.IdentityValidationService;

@Controller
@RequestMapping("identityValidation")
public class IdentityValidationController {
	
	@Autowired
	private IdentityValidationService identityValidationService;
	
	@RequestMapping(method = RequestMethod.GET)
	public void identityValidationController() throws Exception {
		identityValidationService.identityValidation(null, null);
	}

}
