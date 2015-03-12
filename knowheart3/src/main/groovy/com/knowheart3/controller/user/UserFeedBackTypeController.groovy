package com.knowheart3.controller.user;

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.SessionAttributes

import com.knowheart3.repository.user.UserFeedBackTypeRepository
import com.qubaopen.core.controller.AbstractBaseController
import com.qubaopen.core.repository.MyRepository
import com.qubaopen.survey.entity.user.User
import com.qubaopen.survey.entity.user.UserFeedBackType

@RestController
@RequestMapping('feedBackType')
@SessionAttributes('currentUser')
public class UserFeedBackTypeController extends AbstractBaseController<UserFeedBackType, Long> {
	
	private static Logger logger = LoggerFactory.getLogger(UserFeedBackController.class)

	@Autowired
	UserFeedBackTypeRepository userFeedBackTypeRepository
	
	@Override
	MyRepository<UserFeedBackType, Long> getRepository() {
		userFeedBackTypeRepository
	}
	
	/**
	 * @param user
	 * @return
	 * 获取反馈种类
	 */
	@RequestMapping(value = 'retrieveBackType', method = RequestMethod.GET)
	retrieveBackType(@ModelAttribute('currentUser') User user) {
		
		logger.trace('-- 获取反馈种类 --')
		
		def data = [],
			backTypes = userFeedBackTypeRepository.findAll()

		backTypes.each {
			data << [
				'id' : it.id,
				'name' : it.name
			]
		}
		
		[
			'success' : '1',
			'data' : data
		]

	}

}
