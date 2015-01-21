package com.qubaopen.survey.repository.breadtalk;

import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.bread.BreadTalk;

public interface BreadTalkRepository extends MyRepository<BreadTalk, Long> {

	BreadTalk findByCode(String code);
}
