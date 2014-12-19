package com.qubaopen.doctor.controller.cash;

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.qubaopen.core.controller.AbstractBaseController
import com.qubaopen.core.repository.MyRepository
import com.qubaopen.doctor.repository.cash.BankRepository
import com.qubaopen.survey.entity.cash.Bank
import com.qubaopen.survey.entity.doctor.Doctor


@RestController
@RequestMapping('bank')
@SessionAttributes('currentDoctor')
public class BankController extends AbstractBaseController<Bank, Long> {

	@Autowired
	BankRepository	bankRepository
	
	@Override
	MyRepository<Bank, Long> getRepository() {
		bankRepository
	}
	
	/**
	 * @param doctor
	 * @return
	 * 获取银行
	 */
	@RequestMapping(value = 'retrieveBank', method = RequestMethod.GET)
	retrieveBank(@ModelAttribute('currentDoctor') Doctor doctor) {
		
		def banks = bankRepository.findAll(),
			result = []
		
		banks.each {
			result << [
				'id' : it?.id,
				'name' : it?.name
			]
		}
		
		result
	}
}
