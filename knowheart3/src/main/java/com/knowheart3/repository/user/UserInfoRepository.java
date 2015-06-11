package com.knowheart3.repository.user;

import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.user.UserInfo;

public interface UserInfoRepository extends MyRepository<UserInfo, Long> {


    UserInfo findByNickName(String nickName);
}
