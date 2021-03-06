package com.knowheart3.controller.user
import com.knowheart3.repository.user.UserShareRepository
import com.knowheart3.service.user.UserShareService
import com.qubaopen.core.controller.AbstractBaseController
import com.qubaopen.core.repository.MyRepository
import com.qubaopen.survey.entity.user.User
import com.qubaopen.survey.entity.user.UserShare
import com.qubaopen.survey.entity.user.UserShare.ShareOrigin
import com.qubaopen.survey.entity.user.UserShare.ShareTarget
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

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

		if (user == null) {
			return '{"success": "0", "message": "err000"}'
		}
		if (target < 0 || target >= ShareTarget.values().length) {
			return '{"success": "0", "message": "err500"}' // target参数错误
		}
		if (origin < 0 || origin >= ShareOrigin.values().length) {
			return '{"success": "0", "message": "err501"}' // origin参数错误
		}

		if (origin != 0 && (remark == null || remark.equals(""))) {
			return '{"success": "0", "message": "err502"}' // remark为空
		}

		userShareService.saveUserShare(user, ShareTarget.values()[target], ShareOrigin.values()[origin], remark);

	}
}
