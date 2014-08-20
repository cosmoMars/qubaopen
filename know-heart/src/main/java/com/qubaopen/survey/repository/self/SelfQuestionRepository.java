package com.qubaopen.survey.repository.self;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.self.Self;
import com.qubaopen.survey.entity.self.SelfQuestion;

public interface SelfQuestionRepository extends MyRepository<SelfQuestion, Long> {

	List<SelfQuestion> findAllBySelf(Self self);

	@Query("from SelfQuestion sq where sq.self = :self and sq.parent is null order by sq.questionNum asc, sq.special desc")
	List<SelfQuestion> findBySelf(@Param("self") Self self);

	@Query("from SelfQuestion sq where sq.parent.id = :parentId order by sq.questionNum asc")
	List<SelfQuestion> findByParent(@Param("parentId") long parentId);

}
