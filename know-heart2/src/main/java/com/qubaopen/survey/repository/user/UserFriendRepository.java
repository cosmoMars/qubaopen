package com.qubaopen.survey.repository.user;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.user.User;
import com.qubaopen.survey.entity.user.UserFriend;

public interface UserFriendRepository extends MyRepository<UserFriend, Long> {


	@Query("select count(*) from UserFriend uf where uf in (from UserFriend ufr where ufr.user = :user) and uf.friend = :user")
	long countUserFriend(@Param("user") User user);

	@Query("from UserFriend uf where uf in (from UserFriend ufr where ufr.user = :user) and uf.friend = :user")
	List<UserFriend> findByUser(@Param("user") User user);
}
