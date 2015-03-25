package com.qubaopen.backend.repository.doctor;

import com.qubaopen.backend.repository.doctor.custom.AssistantRepositoryCustom;
import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.doctor.Assistant;

/**
 * Created by mars on 15/3/25.
 */
public interface AssistantRepository extends MyRepository<Assistant, Long>, AssistantRepositoryCustom {
}
