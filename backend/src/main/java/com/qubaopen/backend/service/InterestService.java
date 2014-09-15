package com.qubaopen.backend.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qubaopen.backend.repository.interest.InterestQuestionRepository;
import com.qubaopen.backend.repository.interest.InterestRepository;
import com.qubaopen.backend.repository.interest.InterestResultOptionRepository;
import com.qubaopen.survey.entity.interest.InterestQuestion;
import com.qubaopen.survey.entity.interest.InterestResultOption;

@Service
@Transactional(readOnly = true)
public class InterestService {
	
	private static final Logger logger = LoggerFactory.getLogger(InterestService.class);
	
	@Autowired
	private InterestRepository interestRepository;
	
	@Autowired
	private InterestQuestionRepository interestQuestionRepository;
	
	@Autowired
	private InterestResultOptionRepository interestResultOptionRepository;
	
	@Transactional
	public void saveInterest(List<InterestQuestion> interestQuestions, List<InterestResultOption> interestResultOptions) {
//		long count = interestRepository.count();
//		logger.debug(" ================================ count = {}", count);
		interestQuestionRepository.save(interestQuestions);
		interestResultOptionRepository.save(interestResultOptions);
		
		
	}

}
