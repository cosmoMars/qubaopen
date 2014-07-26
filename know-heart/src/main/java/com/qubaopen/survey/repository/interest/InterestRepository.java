package com.qubaopen.survey.repository.interest;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.interest.Interest;
import com.qubaopen.survey.entity.user.User;

public interface InterestRepository extends MyRepository<Interest, Long> {

	@Query("from Interest i where i not in (select iuq.interest from InterestUserQuestionnaire iuq where iuq.user = :user)")
	List<Interest> findUnfinishInterest(@Param("user") User user);
}
