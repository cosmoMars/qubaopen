package com.knowheart3.repository.favorite.custom;

import com.qubaopen.survey.entity.user.User;
import com.qubaopen.survey.entity.user.UserFavorite;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by mars on 15/3/30.
 */
public interface UserFavoriteRepositoryCustom {

    List<UserFavorite> findByTypeAndUser(String type, User user,Pageable pageable);

}
