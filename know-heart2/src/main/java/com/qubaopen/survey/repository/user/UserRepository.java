package com.qubaopen.survey.repository.user;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.user.User;
import com.qubaopen.survey.entity.user.User.ThirdType;

public interface UserRepository extends MyRepository<User, Long> {

	User findByPhone(String phone);
	
	User findByPhoneAndActivated(String phone, boolean activated);

	@Query("from User u join fetch u.userInfo left join fetch u.userIdCardBind where phone = :phone and password = :password and activated = true")
	User login(@Param("phone") String phone, @Param("password") String password);
	
	@Query("from User")
	List<User> findAllUsers(Pageable pageable);
	
	User findByTokenAndThirdType(String token, ThirdType thirdType);
	
	@Query("from User u join fetch u.userInfo left join fetch u.userIdCardBind where u.token = :token and u.thirdType = :thirdType")
	User thirdLogin(@Param("token") String token, @Param("thirdType") ThirdType thirdType);

	@Query("from User u join fetch u.userInfo left join fetch u.userIdCardBind where u.id = :userId")
	User thirdLogin(@Param("userId") long userId);
}

