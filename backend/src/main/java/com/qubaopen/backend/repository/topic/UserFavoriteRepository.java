package com.qubaopen.backend.repository.topic;

import com.qubaopen.backend.repository.topic.custom.UserFavoriteRepositoryCustom;
import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.user.UserFavorite;

public interface UserFavoriteRepository extends MyRepository<UserFavorite, Long>, UserFavoriteRepositoryCustom {

}
