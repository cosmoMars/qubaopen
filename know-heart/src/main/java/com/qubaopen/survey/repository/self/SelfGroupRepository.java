package com.qubaopen.survey.repository.self;

import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.self.SelfGroup;
import com.qubaopen.survey.repository.self.custom.SelfGroupRepositoryCustom;

public interface SelfGroupRepository extends MyRepository<SelfGroup, Long>, SelfGroupRepositoryCustom {

}
