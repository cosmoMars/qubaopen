package com.qubaopen.survey.controller.interest

import java.lang.reflect.Method;

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController

import com.qubaopen.core.controller.AbstractBaseController
import com.qubaopen.core.repository.MyRepository
import com.qubaopen.survey.entity.interest.InterestType
import com.qubaopen.survey.repository.interest.InterestTypeRepository

@RestController
@RequestMapping('interestTypes')
public class InterestTypeController extends AbstractBaseController<InterestType, Long> {

	@Autowired
	InterestTypeRepository interestTypeRepository

	@Override
	protected MyRepository<InterestType, Long> getRepository() {
		interestTypeRepository
	}
	

	@RequestMapping(value = 'retrieveInterestType', method = RequestMethod.GET)
	retrieveInterestType() {
		
		def datas = [],
			types = interestTypeRepository.findAll() 
		
		types.each {
			datas << [
				'typeId' : it.id,
				'name' : it.name
			]
		}
		
		[
			'success' : '1',
			'message' : '成功',
			'data' : datas
		]
	}

}
