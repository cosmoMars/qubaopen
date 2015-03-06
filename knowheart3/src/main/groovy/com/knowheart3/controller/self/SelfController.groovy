package com.knowheart3.controller.self;

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.SessionAttributes

import com.knowheart3.repository.favorite.UserFavoriteRepository;
import com.knowheart3.repository.self.SelfRepository
import com.qubaopen.core.controller.AbstractBaseController
import com.qubaopen.core.repository.MyRepository
import com.qubaopen.survey.entity.self.Self
import com.qubaopen.survey.entity.user.User


@RestController
@RequestMapping('self')
@SessionAttributes('currentUser')
public class SelfController extends AbstractBaseController<Self, Long> {

	@Autowired
	SelfRepository selfRepository
	
	@Autowired
	UserFavoriteRepository userFavoriteRepository
	
	@Override
	MyRepository<Self, Long> getRepository() {
		selfRepository
	}
	
	
	/**
	 * @param id
	 * @param user
	 * @return
	 * 获取自测信息
	 */
	@RequestMapping(value = 'retrieveSelfContent', method = RequestMethod.POST)
	retrieveSelfContent(@RequestParam(required = false) Long id,
		@ModelAttribute('currentUser') User user) {
		
		def favorite
		def self = selfRepository.findOne(id)
		if (null != user.id) {
			favorite = userFavoriteRepository.findOneByFilters([
				user_equal : user,
				self_equal : self
			])
		}
		
		def isFavorite = false
		if (favorite != null) {
			isFavorite = true
		}
		
		[
			'success' : '1',
			'id' : self.id,
			'name' : self?.title,
			'guidanceSentence' : self?.guidanceSentence,
			'tips' : self?.tips,
			'content' : self?.remark,
			'isFavorite' : isFavorite
		]
	}
}
