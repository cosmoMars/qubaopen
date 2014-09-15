package com.qubaopen.backend.repository.self;

import org.springframework.data.jpa.repository.JpaRepository;

import com.qubaopen.survey.entity.self.SelfQuestionOption;

public interface SelfQuestionOptionRepository extends JpaRepository<SelfQuestionOption, Long> {

}
