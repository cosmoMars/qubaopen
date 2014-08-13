package com.qubaopen.survey.repository.interest;

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

}
