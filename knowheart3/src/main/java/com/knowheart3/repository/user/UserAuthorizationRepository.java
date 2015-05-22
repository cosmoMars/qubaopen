package com.knowheart3.repository.user;

import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.doctor.Doctor;
import com.qubaopen.survey.entity.user.User;
import com.qubaopen.survey.entity.user.UserAuthorization;

/**
 * Created by mars on 15/5/21.
 */
public interface UserAuthorizationRepository extends MyRepository<UserAuthorization, Long> {

    UserAuthorization findByUserAndDoctor(User user, Doctor doctor);

}
