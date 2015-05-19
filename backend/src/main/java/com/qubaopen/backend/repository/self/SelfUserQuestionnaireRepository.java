package com.qubaopen.backend.repository.self;

import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.self.Self;
import com.qubaopen.survey.entity.self.SelfUserQuestionnaire;
import com.qubaopen.survey.entity.user.User;

/**
 * Created by mars on 15/5/19.
 */
public interface SelfUserQuestionnaireRepository extends MyRepository<SelfUserQuestionnaire,Long> {
    int countBySelfAndUser(Self self, User user);
}
