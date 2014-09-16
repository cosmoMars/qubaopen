package com.qubaopen.backend.repository.interest;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.qubaopen.survey.entity.interest.Interest;
import com.qubaopen.survey.entity.interest.InterestQuestionOrder;

public interface InterestQuestionOrderRepository extends JpaRepository<InterestQuestionOrder, Long> {

	List<InterestQuestionOrder> findAllByInterest(Interest interest);
}
