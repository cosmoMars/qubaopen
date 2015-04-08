package com.knowheart3.repository.user;

import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.user.UserMoodPic;

/**
 * Created by mars on 15/4/8.
 */
public interface UserMoodPicRepository extends MyRepository<UserMoodPic, Long> {

    UserMoodPic findByType(UserMoodPic.Type type);
}
