package com.qubaopen.survey.repository.user;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.user.UserIDCard;

public interface UserIDCardRepository extends MyRepository<UserIDCard, Long> {

	
	@Query("from UserIDCard u where u.IDCard = :idCard and u.name = :name")
	UserIDCard findByIDCardAndName(@Param("idCard") String idCard,@Param("name") String name);
}
