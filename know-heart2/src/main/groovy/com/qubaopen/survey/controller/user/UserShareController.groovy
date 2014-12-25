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
import com.qubaopen.survey.entity.system.SystemVersion;
import com.qubaopen.survey.entity.user.User;
import com.qubaopen.survey.entity.user.UserShare
import com.qubaopen.survey.entity.user.UserShare.ShareOrigin;
import com.qubaopen.survey.entity.user.UserShare.ShareTarget
import com.qubaopen.survey.repository.user.UserShareRepository;
import com.qubaopen.survey.service.user.UserShareService;

@RestController
@RequestMapping('userShare')
@SessionAttributes('currentUser')
public class UserShareController extends AbstractBaseController<UserShare, Long>{
	@Autowired
	UserShareRepository userShareRepository;

	@Autowired
	UserShareService userShareService;

	@Override
	protected MyRepository<UserShare, Long> getRepository() {
		userShareRepository
	}

	@RequestMapping(value = 'share', method = RequestMethod.POST)
	userShare(@RequestParam(required = false) int target,
			@RequestParam(required = false) int origin,
			@RequestParam(required = false) String remark,
			@ModelAttribute('currentUser') User user) {
		if(target<0 || target>=ShareTarget.values().length){
			return '{"success": "0", "message": "err500"}' // target参数错误
		}
		if(origin<0 || origin>=ShareOrigin.values().length){
			return '{"success": "0", "message": "err501"}' // origin参数错误
		}
		
		if ( origin!=0 && (remark ==null || remark.equals(""))) {
			return '{"success": "0", "message": "err502"}' // remark为空
		}


		userShareService.saveUserShare(user,ShareTarget.values()[target], ShareOrigin.values()[origin], remark);

	}
}
