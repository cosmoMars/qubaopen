package com.qubaopen.survey.controller.user
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.SessionAttributes
import org.springframework.web.bind.annotation.RestController;

import com.qubaopen.core.controller.AbstractBaseController;
import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.user.User;
import com.qubaopen.survey.entity.user.UserMood
import com.qubaopen.survey.entity.user.UserMood.MoodType
import com.qubaopen.survey.repository.user.UserMoodRepository;
import com.qubaopen.survey.service.user.UserMoodService;

@RestController
@RequestMapping('userMood')
@SessionAttributes('currentUser')
public class UserMoodController extends AbstractBaseController<UserMood, Long>{
	@Autowired
	UserMoodRepository userMoodRepository;

	@Autowired
	UserMoodService userMoodService;

	@Override
	protected MyRepository<UserMood, Long> getRepository() {
		userMoodRepository
	}

	/**
	 * 用户提交心情
	 * @param type
	 * @param user
	 * @return
	 */
	@RequestMapping(value = 'setMood', method = RequestMethod.POST)
	userMood(@RequestParam int type,
			@RequestParam(required = false) String message,
			@ModelAttribute('currentUser') User user) {

		if(type<0 || type>=MoodType.values().length){
			return '{"success": "0", "message": "err800"}' // type参数错误
		}

		userMoodService.saveUserMood(user, MoodType.values()[type], message);
	}

	/**
	 * 获取用户心情 停用并 已并入登录接口
	 * @param user
	 * @return
	 */
	@RequestMapping(value = 'getMood', method = RequestMethod.GET)
	getMood(@ModelAttribute('currentUser') User user) {

		userMoodService.getUserMood(user);
	}
	
	/**
	 * 获取用户指定月份的心情数据 每日的最后一次
	 * @param user
	 * @return
	 */
	@RequestMapping(value = 'getMoodList', method = RequestMethod.POST)
	getMoodList(@RequestParam int month,
		@ModelAttribute('currentUser') User user) {
		userMoodService.getUserMood(user,month);
	}
}
