package com.qubaopen.survey.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qubaopen.core.controller.AbstractBaseController;
import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.AdvertiseConfirm;
import com.qubaopen.survey.repository.AdvertiseConfirmRepository;

@RestController
@RequestMapping("advertiseConfirms")
public class AdvertiseConfirmController extends AbstractBaseController<AdvertiseConfirm, Long> {

	@Autowired
	private AdvertiseConfirmRepository advertiseConfirmRepository;

	@Override
	protected MyRepository<AdvertiseConfirm, Long> getRepository() {
		return advertiseConfirmRepository;
	}

}
