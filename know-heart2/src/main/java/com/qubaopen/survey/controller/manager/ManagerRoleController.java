package com.qubaopen.survey.controller.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qubaopen.core.controller.AbstractBaseController;
import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.manager.ManagerRole;
import com.qubaopen.survey.repository.manager.ManagerRoleRepository;

@RestController
@RequestMapping("managerRoles")
public class ManagerRoleController extends AbstractBaseController<ManagerRole, Long> {

	@Autowired
	private ManagerRoleRepository managerRoleRepository;
	
	@Override
	protected MyRepository<ManagerRole, Long> getRepository() {
		return managerRoleRepository;
	}

}
