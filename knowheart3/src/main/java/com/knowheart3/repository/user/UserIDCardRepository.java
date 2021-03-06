package com.knowheart3.repository.user;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.user.UserIDCard;

public interface UserIDCardRepository extends MyRepository<UserIDCard, Long> {

	
	@Query("from UserIDCard u where u.IDCard = :idCard")
	UserIDCard findByIDCard(@Param("idCard") String idCard);
}
