package com.qubaopen.survey.service.mindmap

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import com.qubaopen.survey.entity.user.User
import com.qubaopen.survey.repository.self.SelfUserAnswerRepository
import com.qubaopen.survey.repository.self.SelfUserQuestionnaireRepository

@Service
public class MindMapService {

	@Autowired
	SelfUserAnswerRepository selfUserAnswerRepository

	@Autowired
	SelfUserQuestionnaireRepository selfUserQuestionnaireRepository


	@Transactional
	calculateJop(long userId) {

		def user = new User(id : userId)

		def selfUserQuestionnaire = selfUserQuestionnaireRepository.find()
	}


}
