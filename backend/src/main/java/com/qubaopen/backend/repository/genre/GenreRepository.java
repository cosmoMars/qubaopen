package com.qubaopen.backend.repository.genre;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.doctor.Genre;

public interface GenreRepository extends MyRepository<Genre, Long> {
	@Query("from Genre g where g.id in (:ids)")
	List<Genre> findByIds(@Param("ids")List<Long> ids);
}
