package com.knowheart3.repository.user;

import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.user.User;
import com.qubaopen.survey.entity.user.UserHelpData;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by mars on 15/5/28.
 */
public interface UserHelpDataRepository extends MyRepository<UserHelpData, Long> {


    @Query("from UserHelpData u where u.helpComment.id in (:ids) and u.user = :user and u.refresh = true")
    List<UserHelpData> findByHelpCommentsAndUser(@Param("ids") List<Long> ids, @Param("user") User user);
}
