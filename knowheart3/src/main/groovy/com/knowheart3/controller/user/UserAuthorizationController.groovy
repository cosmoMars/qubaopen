package com.knowheart3.controller.user

import com.knowheart3.repository.user.UserAuthorizationRepository
import com.qubaopen.core.controller.AbstractBaseController
import com.qubaopen.core.repository.MyRepository
import com.qubaopen.survey.entity.doctor.Doctor
import com.qubaopen.survey.entity.user.User
import com.qubaopen.survey.entity.user.UserAuthorization
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

/**
 * Created by mars on 15/5/21.
 */
@RestController
@RequestMapping('userAuthorization')
@SessionAttributes('currentUser')
class UserAuthorizationController extends AbstractBaseController<UserAuthorization, Long> {

    @Autowired
    UserAuthorizationRepository userAuthorizationRepository

    @Override
    protected MyRepository<UserAuthorization, Long> getRepository() {
        userAuthorizationRepository
    }

    @RequestMapping(value = 'modifyAuthorization')
    modifyAuthorization(@RequestParam(required = false) Boolean authorise,
                        @RequestParam long doctorId,
                        @ModelAttribute('currentUser') User user) {

        def ua = userAuthorizationRepository.findByUserAndDoctor(user, new Doctor(id: doctorId))

        // 存在授权，取消授权
        if (ua && authorise == false) {
            userAuthorizationRepository.delete(ua)
            // 不存在存在授权，创建授权
        } else if (!ua && (authorise == null || authorise == true)) {
            ua = new UserAuthorization(
                    user: user,
                    doctor: new Doctor(id: doctorId)
            )
            userAuthorizationRepository.save(ua)
        }

        '{"success" : "1"}'

    }
}
