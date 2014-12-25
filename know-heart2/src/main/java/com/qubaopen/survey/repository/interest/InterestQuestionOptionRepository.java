package com.qubaopen.survey.repository.interest;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.interest.InterestQuestionOption;

public interface InterestQuestionOptionRepository extends MyRepository<InterestQuestionOption, Long> {

	@Query("select sum(iqo.score) from InterestQuestionOption iqo where id in (:ids)")
	long sumQuestionOptions(@Param("ids") long... ids);

}
