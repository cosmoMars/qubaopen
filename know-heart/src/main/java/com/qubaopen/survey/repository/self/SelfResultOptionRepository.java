package com.qubaopen.survey.repository.self;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.self.SelfResultOption;

public interface SelfResultOptionRepository extends MyRepository<SelfResultOption, Long> {

	@Query("select sum(sqo.score) from SelfQuestionOption sqo where sqo.id in (:ids)")
	long sumSelfOptions(@Param("ids") long... ids);

	@Query("from SelfResultOption sro where sro.name like :s1 and sro.name like :s2 and sro.name like :s3")
	List<SelfResultOption> findByTypeAlphabet(@Param("s1") String s1, @Param("s2") String s2, @Param("s3") String s3);

	@Query("from SelfResultOption sro where sro.name like :s1 and sro.name like :s2")
	List<SelfResultOption> findByTypeAlphabet(@Param("s1") String s1, @Param("s2") String s2);
	
	SelfResultOption findByName(String name);
}
