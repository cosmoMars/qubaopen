package com.qubaopen.doctor.repository.map;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.self.Self;
import com.qubaopen.survey.entity.self.SelfResultOption;

public interface SelfResultOptionRepository extends MyRepository<SelfResultOption, Long> {

	@Query("select sum(sqo.score) from SelfQuestionOption sqo where sqo.id in (:ids)")
	long sumSelfOptions(@Param("ids") long... ids);

	@Query("from SelfResultOption sro where sro.name like :s1 and sro.name like :s2 and sro.name like :s3 and sro.selfResult.self = :self")
	List<SelfResultOption> findByTypeAlphabet(@Param("s1") String s1, @Param("s2") String s2, @Param("s3") String s3, @Param("self") Self self);
	
	@Query("from SelfResultOption sro where sro.name like :s1 and sro.name like :s2 and sro.name like :s3 and sro.name like :s4 and sro.selfResult.self = :self")
	List<SelfResultOption> findByTypeAlphabet(@Param("s1") String s1, @Param("s2") String s2, @Param("s3") String s3, @Param("s4") String s4, @Param("self") Self self);

	@Query("from SelfResultOption sro where sro.name like :s1 and sro.name like :s2 and sro.selfResult.self = :self")
	List<SelfResultOption> findByTypeAlphabet(@Param("s1") String s1, @Param("s2") String s2, @Param("self") Self self);
	
	SelfResultOption findByName(String name);
}
