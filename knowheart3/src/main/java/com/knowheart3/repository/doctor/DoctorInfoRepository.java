package com.knowheart3.repository.doctor;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.knowheart3.repository.doctor.custom.DoctorInfoRepositoryCustom;
import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.doctor.DoctorInfo;

public interface DoctorInfoRepository extends MyRepository<DoctorInfo, Long>, DoctorInfoRepositoryCustom {

	@Query("from DoctorInfo di join fetch di.genres g where g.id = :genre")
	List<DoctorInfo> findDoctorByGenre(@Param("genre") long genre, Pageable pageable);
	
	@Query("from DoctorInfo di join fetch di.targetUsers tu where tu.id = :target")
	List<DoctorInfo> findDoctorByTargetUser(@Param("target") long target, Pageable pageable);
	
	@Query("from DoctorInfo di join fetch di.genres g join fetch di.targetUsers tu where g.id = :genre and tu.id = :target")
	List<DoctorInfo> findDoctorByGenreAndTargetUser(@Param("genre") long genre, @Param("target") long target, Pageable pageable);
	
	// 去除ids
	
	@Query("from DoctorInfo di where di.id not in (:ids)")
	List<DoctorInfo> findWithoutExistDoctor(@Param("ids") List<Long> ids, Pageable pageable);
	
	@Query("from DoctorInfo di join fetch di.genres g where g.id = :genre and di.id not in (:ids)")
	List<DoctorInfo> findDoctorByGenre(@Param("ids") List<Long> ids, @Param("genre") long genre, Pageable pageable);
	
	@Query("from DoctorInfo di join fetch di.targetUsers tu where tu.id = :target and di.id not in (:ids)")
	List<DoctorInfo> findDoctorByTargetUser(@Param("ids") List<Long> ids, @Param("target") long target, Pageable pageable);
	
	@Query("from DoctorInfo di join fetch di.genres g join fetch di.targetUsers tu where g.id = :genre and tu.id = :target and di.id not in (:ids)")
	List<DoctorInfo> findDoctorByGenreAndTargetUser(@Param("ids") List<Long> ids, @Param("genre") long genre, @Param("target") long target, Pageable pageable);
	
}
