package com.qubaopen.doctor.repository.map;

import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.doctor.repository.map.custom.SelfGroupRepositoryCustom;
import com.qubaopen.survey.entity.self.SelfGroup;

public interface SelfGroupRepository extends MyRepository<SelfGroup, Long>, SelfGroupRepositoryCustom {

}
