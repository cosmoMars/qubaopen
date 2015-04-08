package com.knowheart3.repository.base;

import com.knowheart3.repository.base.custom.AreaCodeRepostioryCustom;
import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.base.AreaCode;

public interface AreaCodeRepository extends MyRepository<AreaCode, Long>, AreaCodeRepostioryCustom {
	AreaCode findByCode(String code);
}
