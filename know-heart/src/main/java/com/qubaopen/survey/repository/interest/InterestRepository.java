package com.qubaopen.survey.repository.interest;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.interest.Interest;
import com.qubaopen.survey.entity.interest.InterestType;
import com.qubaopen.survey.entity.user.User;

public interface InterestRepository extends MyRepository<Interest, Long> {

	@Query("from Interest i where i not in (select iuq.interest from InterestUserQuestionnaire iuq where iuq.user = :user) order by i.recommendedValue desc")
	List<Interest> findUnfinishInterest(@Param("user") User user);
	
	
//	@Query("from Interest i where i not in (select iuq.interest from InterestUserQuestionnaire iuq where iuq.user = :user) and i.type = :type order by i.recommendedValue desc")
//	List<Interest> findByInterestType(@Param("type") InterestType interestType);
	
//	@Query("from Interest i left join (select iuq.interest.id id, count(iuq.interest) countNum from InterestUserQuestionnaire iuq group by iuq.interest.id) a"
//			+ "")
//	List<Interest> findByPopularity();
	
//	List<Interest> findByTime();
	
//	select * from interest i left join (select iuq.interest_id id,count(iuq.interest_id) countNum from interest_user_questionnaire iuq group by iuq.interest_id) a 
//	on i.id = a.id 
//	order by a.countNum desc,i.recommended_value desc


}
