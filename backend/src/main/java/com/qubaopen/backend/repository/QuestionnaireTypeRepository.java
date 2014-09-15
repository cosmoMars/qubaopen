package com.qubaopen.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.qubaopen.survey.entity.QuestionnaireType;

public interface QuestionnaireTypeRepository extends JpaRepository<QuestionnaireType, Long> {

}
