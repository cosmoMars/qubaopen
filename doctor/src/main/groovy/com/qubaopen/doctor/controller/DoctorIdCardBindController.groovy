package com.qubaopen.doctor.controller;

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.SessionAttributes

import com.qubaopen.core.controller.AbstractBaseController
import com.qubaopen.core.repository.MyRepository
import com.qubaopen.doctor.repository.DoctorIdCardBindRepository
import com.qubaopen.doctor.service.DoctorIdCardBindService
import com.qubaopen.survey.entity.doctor.Doctor
import com.qubaopen.survey.entity.doctor.DoctorIdCardBind
import com.qubaopen.survey.entity.user.User;


@RestController
@RequestMapping('doctorIdCardBind')
@SessionAttributes('currentDoctor')
public class DoctorIdCardBindController extends AbstractBaseController<DoctorIdCardBind, Long> {

	private static Logger logger = LoggerFactory.getLogger(DoctorIdCardBindController.class)
	
	@Autowired
	DoctorIdCardBindRepository doctorIdCardBindRepository
	
	@Autowired
	DoctorIdCardBindService doctorIdCardBindService
	
	@Override
	protected MyRepository<DoctorIdCardBind, Long> getRepository() {
		return doctorIdCardBindRepository;
	}
	
	/**
	 * @param idCard
	 * @param name
	 * @param doctor
	 * @return
	 * 绑定身份证
	 */
	@RequestMapping(value = 'submitIdCard', method = RequestMethod.POST)
	submitIdCard(@RequestParam(required = false) String idCard, @RequestParam(required = false) String name, @ModelAttribute('currentDoctor') Doctor doctor) {
	 
		logger.trace('-- 身份验证 --')
		
		doctorIdCardBindService.submitUserIdCard(idCard, name, doctor)
		
	}
	
	/**
	 * 获取用户身份证信息
	 * @param user
	 * @return
	 */
	@RequestMapping(value = 'retrieveIdCard', method = RequestMethod.GET)
	retrieveIdCard(@ModelAttribute('currentDoctor') User user) {
		
		logger.trace('-- 获取用户身份证信息 --')
		
		def idCardBind = doctorIdCardBindRepository.findOne(user.id)
		
		if (idCardBind) {
			return [
				'success' : '1',
				'message' : '成功',
				'exist' : true,
				'name' : idCardBind.userIDCard.name,
				'idCard' : idCardBind.userIDCard.IDCard
			]
		} else {
			return [
				'success' : '1',
				'message' : '身份证未认证',
				'exist' : false,
				'name' : '',
				'idCard' : ''
			]
		}
	}

}
