package com.qubaopen.survey.repository.user;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.user.User;

public interface UserRepository extends MyRepository<User, Long> {

	User findByPhone(String phone);

	@Query("from User u where u.phone = :phone and u.password = :password and u.isActivated = true")
	User login(@Param("phone") String phone, @Param("password") String password);

}

