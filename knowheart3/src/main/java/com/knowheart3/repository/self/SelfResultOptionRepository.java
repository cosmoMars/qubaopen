package com.knowheart3.repository.self;

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

	@Query("from SelfResultOption sro where sro.id in (248,249,250) and sro.lowestScore <= :score and sro.highestScore >= :score)")
	SelfResultOption findByMaleAndHealthy(@Param("score") int score);

	@Query("from SelfResultOption sro where sro.id in (251,252,253) and sro.lowestScore <= :score and sro.highestScore >= :score)")
	SelfResultOption findByMaleAndCoordinate(@Param("score") int score);

	@Query("from SelfResultOption sro where sro.id in (271,272,273) and sro.lowestScore <= :score and sro.highestScore >= :score)")
	SelfResultOption findByMaleAndSports(@Param("score") int score);

	@Query("from SelfResultOption sro where sro.id in (274,275,276) and sro.lowestScore <= :score and sro.highestScore >= :score)")
	SelfResultOption findByMaleAndFat(@Param("score") int score);

	@Query("from SelfResultOption sro where sro.id in (277,278,279) and sro.lowestScore <= :score and sro.highestScore >= :score)")
	SelfResultOption findByMaleAndExercise(@Param("score") int score);

	@Query("from SelfResultOption sro where sro.id in (280,281,282) and sro.lowestScore <= :score and sro.highestScore >= :score)")
	SelfResultOption findByMaleAndBody(@Param("score") int score);

	@Query("from SelfResultOption sro where sro.id in (283,284,285) and sro.lowestScore <= :score and sro.highestScore >= :score)")
	SelfResultOption findByMaleAndSurface(@Param("score") int score);

	@Query("from SelfResultOption sro where sro.id in (286,287,288) and sro.lowestScore <= :score and sro.highestScore >= :score)")
	SelfResultOption findByMaleAndPower(@Param("score") int score);

	@Query("from SelfResultOption sro where sro.id in (289,290,291) and sro.lowestScore <= :score and sro.highestScore >= :score)")
	SelfResultOption findByMaleAndFlexible(@Param("score") int score);

	@Query("from SelfResultOption sro where sro.id in (292,293,294) and sro.lowestScore <= :score and sro.highestScore >= :score)")
	SelfResultOption findByMaleAndEndurance(@Param("score") int score);

	@Query("from SelfResultOption sro where sro.id in (295,296,297) and sro.lowestScore <= :score and sro.highestScore >= :score)")
	SelfResultOption findByMaleAndSelfEsteem(@Param("score") int score);



	@Query("from SelfResultOption sro where sro.id in (298,299,300) and sro.lowestScore <= :score and sro.highestScore >= :score)")
	SelfResultOption findByFemaleAndCoordinate(@Param("score") int score);

	@Query("from SelfResultOption sro where sro.id in (301,302,303) and sro.lowestScore <= :score and sro.highestScore >= :score)")
	SelfResultOption findByFemaleAndSports(@Param("score") int score);

	@Query("from SelfResultOption sro where sro.id in (304,305,306) and sro.lowestScore <= :score and sro.highestScore >= :score)")
	SelfResultOption findByFemaleAndFat(@Param("score") int score);

	@Query("from SelfResultOption sro where sro.id in (307,308,309) and sro.lowestScore <= :score and sro.highestScore >= :score)")
	SelfResultOption findByFemaleAndExercise(@Param("score") int score);

	@Query("from SelfResultOption sro where sro.id in (310,311,312) and sro.lowestScore <= :score and sro.highestScore >= :score)")
	SelfResultOption findByFemaleAndBody(@Param("score") int score);

	@Query("from SelfResultOption sro where sro.id in (313,314,315) and sro.lowestScore <= :score and sro.highestScore >= :score)")
	SelfResultOption findByFemaleAndSurface(@Param("score") int score);

	@Query("from SelfResultOption sro where sro.id in (316,317,318) and sro.lowestScore <= :score and sro.highestScore >= :score)")
	SelfResultOption findByFemaleAndFlexible(@Param("score") int score);

	@Query("from SelfResultOption sro where sro.id in (319,320,321) and sro.lowestScore <= :score and sro.highestScore >= :score)")
	SelfResultOption findByFemaleAndEndurance(@Param("score") int score);

	@Query("from SelfResultOption sro where sro.id in (322,323,324) and sro.lowestScore <= :score and sro.highestScore >= :score)")
	SelfResultOption findByFemaleAndSelfEsteem(@Param("score") int score);

}
