package com.qubaopen.survey.repository.self;

import java.util.List;

import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.self.Self;
import com.qubaopen.survey.entity.self.SelfQuestion;

public interface SelfQuestionRepository extends MyRepository<SelfQuestion, Long> {

	List<SelfQuestion> findAllBySelf(Self self);
}
