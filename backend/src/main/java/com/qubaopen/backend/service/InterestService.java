package com.qubaopen.backend.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qubaopen.backend.repository.InterestRepository;

@Service
@Transactional(readOnly = true)
public class InterestService {
	
	private static final Logger logger = LoggerFactory.getLogger(InterestService.class);
	
	@Autowired
	private InterestRepository interestRepository;
	
	@Transactional
	public void saveInterest(String json) {
		long count = interestRepository.count();
		logger.debug(" ================================ count = {}", count);
	}

}
