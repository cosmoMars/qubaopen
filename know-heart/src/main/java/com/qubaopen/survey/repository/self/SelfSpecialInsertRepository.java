package com.qubaopen.survey.repository.self;

import java.util.List;

import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.self.Self;
import com.qubaopen.survey.entity.self.SelfSpecialInsert;

public interface SelfSpecialInsertRepository extends MyRepository<SelfSpecialInsert, Long> {

	List<SelfSpecialInsert> findAllBySelf(Self self);
}
