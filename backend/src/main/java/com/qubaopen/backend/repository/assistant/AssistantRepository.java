package com.qubaopen.backend.repository.assistant;

import com.qubaopen.backend.repository.assistant.custom.AssistantRepositoryCustom;
import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.doctor.Assistant;

/**
 * Created by mars on 15/3/26.
 */
public interface AssistantRepository extends MyRepository<Assistant, Long>, AssistantRepositoryCustom {
}

