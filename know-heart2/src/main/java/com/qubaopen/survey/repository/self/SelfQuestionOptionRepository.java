package com.qubaopen.survey.repository.self;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.self.SelfQuestion;
import com.qubaopen.survey.entity.self.SelfQuestionOption;

public interface SelfQuestionOptionRepository extends MyRepository<SelfQuestionOption, Long> {


	@Query("from SelfQuestionOption sqo where sqo.selfQuestion = :selfQuestion order by sqo.optionNum asc")
	List<SelfQuestionOption> findBySelfQuestionSort(@Param("selfQuestion") SelfQuestion selfQuestion);

}
