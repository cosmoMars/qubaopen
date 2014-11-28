package com.qubaopen.doctor.repository.user;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.user.UserIDCard;

public interface UserIdCardRepository extends MyRepository<UserIDCard, Long> {

	@Query("from UserIDCard u where u.IDCard = :idCard")
	UserIDCard findByIDCard(@Param("idCard") String idCard);
}
