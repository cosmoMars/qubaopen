package com.qubaopen.survey.repository.user;

import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.user.UserMood;
import com.qubaopen.survey.repository.user.custom.UserMoodRepositoryCustom;

public interface UserMoodRepository extends MyRepository<UserMood, Long>, UserMoodRepositoryCustom {

}
