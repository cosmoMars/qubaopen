package com.qubaopen.backend.repository.self;

import org.springframework.data.jpa.repository.JpaRepository;

import com.qubaopen.survey.entity.self.SelfQuestion;

public interface SelfQuestionRepository extends JpaRepository<SelfQuestion, Long> {

}
