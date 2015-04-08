package com.knowheart3.repository.favorite;

import com.knowheart3.repository.favorite.custom.UserFavoriteRepositoryCustom;
import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.self.Self;
import com.qubaopen.survey.entity.user.User;
import com.qubaopen.survey.entity.user.UserFavorite;

public interface UserFavoriteRepository extends MyRepository<UserFavorite, Long>, UserFavoriteRepositoryCustom {

    UserFavorite findBySelfAndUser(Self self, User user);
}
