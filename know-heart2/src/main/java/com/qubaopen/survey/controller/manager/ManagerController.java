package com.qubaopen.survey.controller.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qubaopen.core.controller.AbstractBaseController;
import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.manager.Manager;
import com.qubaopen.survey.repository.manager.ManagerRepository;

@RestController
@RequestMapping("managers")
public class ManagerController extends AbstractBaseController<Manager, Long> {

	@Autowired
	private ManagerRepository managerRepository;
	
	@Override
	protected MyRepository<Manager, Long> getRepository() {
		return managerRepository;
	}

}
