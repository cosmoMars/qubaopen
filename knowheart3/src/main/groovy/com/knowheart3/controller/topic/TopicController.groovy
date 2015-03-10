package com.knowheart3.controller.topic

import com.knowheart3.repository.topic.DailyDiscoveryRepository
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.SessionAttributes

import com.knowheart3.repository.favorite.UserFavoriteRepository
import com.knowheart3.repository.topic.TopicRepository
import com.qubaopen.core.controller.AbstractBaseController
import com.qubaopen.core.repository.MyRepository
import com.qubaopen.survey.entity.topic.Topic
import com.qubaopen.survey.entity.user.User

@RestController
@RequestMapping('topic')
@SessionAttributes('currentUser')
public class TopicController extends AbstractBaseController<Topic, Long> {

	@Autowired
	TopicRepository topicRepository
	
	@Autowired
	UserFavoriteRepository userFavoriteRepository

    @Autowired
    DailyDiscoveryRepository  dailyDiscoveryRepository
	
	@Override
	MyRepository<Topic, Long> getRepository() {
		topicRepository
	}
	
	
	/**
	 * @param id
	 * @param time
	 * @param user
	 * @return
	 * 获取专栏内容
	 */
	@RequestMapping(value = 'retrieveTopicContent', method = RequestMethod.POST)
	retrieveTopicContent(@RequestParam(required = false) Long id,
        @RequestParam(required = false) String time,
		@ModelAttribute('currentUser') User user) {

        if (null == user.id) {
            return '{"success" : "0", "message" : "err000"}'
        }
		
		def favorite, topic

        if (id == null) {
            def dailyDiscovery = dailyDiscoveryRepository.findByTime(DateUtils.parseDate(time, 'yyyy-MM-dd'))
            topic = dailyDiscovery.topic
        } else {
            topic = topicRepository.findOne(id)
        }
		if (null != user.id) {
			favorite = userFavoriteRepository.findOneByFilters([
				user_equal : user,
				topic_equal : topic
			])
		}
		
		def isFavorite = false
		if (favorite != null) {
			isFavorite = true
		}
		
		[
			'success' : '1',
			'id' : topic.id,
			'name' : topic.name,
			'content' : topic.content,
			'isFavorite' : isFavorite	
		]
	}

}
