package com.qubaopen.backend.repository.cash;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.cash.DoctorTakeCash;

public interface DoctorTakeCashRepository extends MyRepository<DoctorTakeCash, Long> {

	@Query("from DoctorTakeCash dtc where dtc.status=:status order by dtc.createdDate desc")
	List<DoctorTakeCash> findDoctorTakeCash(Pageable pageable,@Param("status")DoctorTakeCash.Status status);

}
