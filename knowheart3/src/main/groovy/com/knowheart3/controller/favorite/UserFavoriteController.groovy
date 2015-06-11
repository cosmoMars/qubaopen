package com.knowheart3.controller.favorite;

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort.Direction
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.SessionAttributes

import com.knowheart3.repository.favorite.UserFavoriteRepository
import com.qubaopen.core.controller.AbstractBaseController
import com.qubaopen.core.repository.MyRepository
import com.qubaopen.survey.entity.self.Self
import com.qubaopen.survey.entity.topic.Topic
import com.qubaopen.survey.entity.user.User
import com.qubaopen.survey.entity.user.UserFavorite

@RestController
@RequestMapping('userFavorite')
@SessionAttributes('currentUser')
public class UserFavoriteController extends AbstractBaseController<UserFavorite, Long> {

	@Autowired
	UserFavoriteRepository userFavoriteRepository
	
	@Override
	MyRepository<UserFavorite, Long> getRepository() {
		userFavoriteRepository
	}
	
	
	/**
	 * @param user
	 * @return
	 * 获取收藏列表
	 */
	@RequestMapping(value = 'retrieveFavoriteList', method = RequestMethod.POST)
	retrieveFavoriteList(@PageableDefault(page = 0, size = 20, sort = 'createdDate', direction = Direction.DESC)
		Pageable pageable,
        @RequestParam(required = false) String type,
		@ModelAttribute('currentUser') User user) {
		
		if (null == user.id) {
			return '{"success" : "0", "message" : "err000"}'
		}

        def favorites = userFavoriteRepository.findByTypeAndUser(type, user, pageable)

		def list = [], more = true
		if (favorites.size() < pageable.pageSize) {
			more = false
		}
		favorites.each {
			if (it.self) {
				list << [
					'favoriteId' : it?.id,
					'selfId' : it?.self?.id,
					'selfName' : it?.self?.title,
					'selfContent' : it.self.remark,
					'selfUrl' : it?.self?.picPath
				]
			} else if (it.topic) {
				list << [
					'favoriteId' : it.id,
					'topicId' : it?.topic?.id,
					'topicName' : it?.topic?.name,
					'topicContent' : it.topic.content,
					'topicUrl' : it?.topic?.picUrl
				]
			}
		}
		[
			'success' : '1',
			'list' : list,
			'more' :  more
		]
	}
	
	/**
	 * @param selfId
	 * @param topicId
	 * @param user
	 * @return
	 * 收藏关注取消
	 */
	@RequestMapping(value = 'comfirmFavorite', method = RequestMethod.POST)
	comfirmFavorite(@RequestParam(required = false) Long selfId,
		@RequestParam(required = false) Long topicId,
		@ModelAttribute('currentUser') User user) {
		
		if (user.id == null) {
			return '{"success" : "0", "message" : "err000"}'
		}
		
		def favorite
		if (selfId != null) {
			favorite = userFavoriteRepository.findOneByFilters([
				user_equal : user,
				self_equal : new Self(id : selfId)
			])
		} else if (topicId != null) {
			favorite = userFavoriteRepository.findOneByFilters([
				user_equal : user,
				topic_equal : new Topic(id : topicId)
			])
		}
		
		if (favorite) {
			userFavoriteRepository.delete(favorite)
			return '{"success" : "1"}'
		} else {
			favorite = new UserFavorite(
				user : user
			)
			if (selfId != null) {
				favorite.self = new Self(id : selfId)
			}
			if (topicId != null) {
				favorite.topic = new Topic(id : topicId)
			}
			if (favorite.self != null || favorite.topic != null) {
				userFavoriteRepository.save(favorite)
				return '{"success" : "1", "favoriteId" : ' + favorite.id + '}'
			}
		}
		
	}
}
