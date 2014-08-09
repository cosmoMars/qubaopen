package com.qubaopen.survey.repository.interest;

import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.interest.Interest;
import com.qubaopen.survey.entity.interest.InterestUserQuestionnaire;
import com.qubaopen.survey.entity.user.User;

public interface InterestUserQuestionnaireRepository extends MyRepository<InterestUserQuestionnaire, Long> {

	InterestUserQuestionnaire findByUserAndInterest(User user, Interest interest);
}
