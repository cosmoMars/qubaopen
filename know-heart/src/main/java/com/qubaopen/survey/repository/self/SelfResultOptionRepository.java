package com.qubaopen.survey.repository.self;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.self.SelfQuestionType;
import com.qubaopen.survey.entity.self.SelfResultOption;

public interface SelfResultOptionRepository extends MyRepository<SelfResultOption, Long> {

//	@Query("from SelfReusultOption sro where sro.name in (:name)")
//	List<SelfResultOption> findAllByName(@Param("name") String name);

	@Query("select sum(sqo.score) from SelfQuestionOption sqo where sqo.id in (:ids)")
	long sumSelfOptions(@Param("ids") long... ids);

	@Query("from SelfQuestionType sqt where sqt.name like :s1 and sqt.name like :s2 and sqt.name like :s3")
	List<SelfQuestionType> findByTypeAlphabet(@Param("s1") String s1,@Param("s2") String s2,@Param("s3") String s3);
}
