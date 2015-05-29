package com.qubaopen.backend.repository.self;

import org.springframework.data.jpa.repository.JpaRepository;

import com.qubaopen.survey.entity.self.SelfQuestionOrder;

public interface SelfQuestionOrderRepository extends JpaRepository<SelfQuestionOrder, Long> {

}
