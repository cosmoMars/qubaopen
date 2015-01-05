package com.qubaopen.survey.repository.doctor;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.doctor.DoctorInfo;
import com.qubaopen.survey.entity.doctor.Genre;

public interface DoctorInfoRepository extends MyRepository<DoctorInfo, Long> {

	@Query("from DoctorInfo d where d.id not in (:ids) and :genre in (d.genres) ")
	List<DoctorInfo> findDoctorWithoutGenreAndIds(@Param("genre") Genre genre, @Param("ids") List<Long> ids, Pageable pageable);
	
	@Query("from DoctorInfo d where d.genres = :genre) ")
	List<DoctorInfo> findDoctorWithoutGenre(@Param("genre") Genre genre, Pageable pageable);
}
