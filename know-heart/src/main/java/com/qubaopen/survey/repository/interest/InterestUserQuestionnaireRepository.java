package com.qubaopen.survey.repository.interest;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.interest.Interest;
import com.qubaopen.survey.entity.interest.InterestUserQuestionnaire;
import com.qubaopen.survey.entity.user.User;

public interface InterestUserQuestionnaireRepository extends MyRepository<InterestUserQuestionnaire, Long> {

	InterestUserQuestionnaire findByUserAndInterest(User user, Interest interest);

//	long countUserFriend(Interest interest, List<User>)

	@Query("select count(*) from InterestUserQuestionnaire iuq where iuq.user in (select fr.user from UserFriend fr where fr in (from UserFriend uf where uf.user = :user) and fr.friend = :user) and iuq.interest = :interest")
	long countUserFriend(@Param("user") User user, @Param("interest") Interest interest);
	
	@Query("from InterestUserQuestionnaire i where i.user = :user and i.interest.id not in (:ids)")
	List<InterestUserQuestionnaire> findQuestionnaireByFilter(@Param("user") User user, @Param("ids") List<Long> ids, Pageable pageable);
	
	@Query("from InterestUserQuestionnaire i where i.user = :user and i.interest.interestType.id = :typeId and i.interest.id not in (:ids)")
	List<InterestUserQuestionnaire> findQuestionnaireByFilter(@Param("user") User user, @Param("typeId") long typeId, @Param("ids") List<Long> ids, Pageable pageable);

}
