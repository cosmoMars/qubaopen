package com.knowheart3.repository.self;

import com.knowheart3.repository.self.custom.SelfGroupRepositoryCustom;
import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.self.SelfGroup;

public interface SelfGroupRepository extends MyRepository<SelfGroup, Long>, SelfGroupRepositoryCustom {

}
