package com.qubaopen.backend.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.aspectj.weaver.ast.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.qubaopen.backend.repository.QuestionnaireTagTypeRepository;
import com.qubaopen.backend.repository.QuestionnaireTypeRepository;
import com.qubaopen.backend.repository.interest.InterestQuestionOptionRepository;
import com.qubaopen.backend.repository.interest.InterestQuestionRepository;
import com.qubaopen.backend.repository.interest.InterestRepository;
import com.qubaopen.backend.repository.interest.InterestResultOptionRepository;
import com.qubaopen.backend.repository.interest.InterestResultRepository;
import com.qubaopen.backend.repository.interest.InterestTypeRepository;
import com.qubaopen.backend.service.InterestService;
import com.qubaopen.survey.entity.QuestionnaireTagType;
import com.qubaopen.survey.entity.QuestionnaireType;
import com.qubaopen.survey.entity.interest.Interest;
import com.qubaopen.survey.entity.interest.InterestQuestion;
import com.qubaopen.survey.entity.interest.InterestQuestionOption;
import com.qubaopen.survey.entity.interest.InterestResult;
import com.qubaopen.survey.entity.interest.InterestResultOption;
import com.qubaopen.survey.entity.interest.InterestType;

@RestController
@RequestMapping("interest")
public class InterestController {

	private static final Logger logger = LoggerFactory.getLogger(InterestController.class);

	@Autowired
	private InterestService interestService;

	@Autowired
	private InterestTypeRepository interestTypeRepository;

	@Autowired
	private QuestionnaireTagTypeRepository questionnaireTagTypeRepository;

	@Autowired
	private InterestRepository interestRepository;

	@Autowired
	private InterestResultRepository interestResultRepository;

	@Autowired
	private InterestQuestionRepository interestQuestionRepository;

	@Autowired
	private InterestResultOptionRepository interestResultOptionRepository;

	@Autowired
	private InterestQuestionOptionRepository interestQuestionOptionRepository;
	
	@Autowired
	private QuestionnaireTypeRepository questionnaireTypeRepository;

	@Autowired
	private ObjectMapper objectMapper;
	
	@RequestMapping(value = "test", method = RequestMethod.POST)
	public String test(){
		return "{\"success\" : \"1\"}";
	}

	@Transactional
	@RequestMapping(value = "saveInterest", method = RequestMethod.POST)
	public String saveInterest(String json) {
		System.out.println(json);
		logger.debug(" =======================  json = {}", json);

		try {

			JsonNode jsonNode = objectMapper.readTree(json);
			// ArrayNode interestTitlle = (ArrayNode) jsonNode.path("title");

			ArrayNode tags = (ArrayNode) jsonNode.path("bq");
			List<Long> tagIds = new ArrayList<Long>();
			for (JsonNode tagsId : tags) {
				tagIds.add(tagsId.asLong());
			}
			List<QuestionnaireTagType> questionnaireTagTypes = questionnaireTagTypeRepository.findAll(tagIds);
			System.out.println(tagIds.toString());
			
			QuestionnaireType qType = questionnaireTypeRepository.findOne(jsonNode.get("calType").asLong());
			// 问题
			long type = jsonNode.get("questionnaireContentType").asLong();
			InterestType interestType = interestTypeRepository.findOne(type);
			String interestTitle = jsonNode.get("title").asText();
			Interest interest = new Interest();
			interest.setTitle(interestTitle);
			interest.setInterestType(interestType);
			interest.setQuestionnaireTagTypes(new HashSet<QuestionnaireTagType>(questionnaireTagTypes));
			interest.setAbbreviation(jsonNode.get("abbreviation").asText());
			interest.setGuidanceSentence(jsonNode.get("guidanceSentence").asText());
			interest.setRemark(jsonNode.get("remark").asText());
			interest.setRecommendedValue(jsonNode.get("recommendedValue").asInt());
			interest.setQuestionnaireType(qType);
			
			switch (jsonNode.get("status").asInt()) {
			case 0 : 
				interest.setStatus(Interest.Status.INITIAL);
				break;
			case 1 : 
				interest.setStatus(Interest.Status.ONLINE);
				break;
			case 2 : 
				interest.setStatus(Interest.Status.CLOSED);
				break;
			}
			
			ArrayNode questions = (ArrayNode) jsonNode.path("questions");
			interest = interestRepository.save(interest);

			for (JsonNode question : questions) {
				InterestQuestion iq = new InterestQuestion();
				iq.setQuestionNum(question.get("questionNo").asInt());
				iq.setContent(question.get("questionContent").asText());
				iq.setAnswerTimeLimit(question.get("limitTime").asInt());
				switch (question.get("questionLx").asInt()) {
				case 1:
					iq.setType(InterestQuestion.Type.SINGLE);
					break;
				case 2:
					iq.setType(InterestQuestion.Type.MULTIPLE);
					break;
				case 3:
					iq.setType(InterestQuestion.Type.QA);
					break;
				case 4:
					iq.setType(InterestQuestion.Type.SORT);
					break;
				case 5:
					iq.setType(InterestQuestion.Type.SCORE);
					break;
				case 6:
					iq.setType(InterestQuestion.Type.MULQA);
					break;
				}

				iq.setInterest(interest);
				iq = interestQuestionRepository.save(iq);
				ArrayNode questionOptions = (ArrayNode) question.path("choices");
				for (JsonNode questionOption : questionOptions) {
					InterestQuestionOption option = new InterestQuestionOption();
					option.setContent(questionOption.get("choiceDescription").asText());
					option.setScore(questionOption.get("choicePoint").asInt());
					option.setInterestQuestion(iq);
					interestQuestionOptionRepository.save(option);
				}
				// interestQuestions.add(iq);
			}
			// interestQuestionRepository.save(interestQuestions);
			// 结果
			List<InterestResultOption> interestResultOptions = new ArrayList<InterestResultOption>();

			String answerTitle = jsonNode.get("answerTitle").asText();
			InterestResult result = new InterestResult();
			result.setTitle(answerTitle);
			result.setInterest(interest);

			result = interestResultRepository.save(result);

			ArrayNode answers = (ArrayNode) jsonNode.path("answers");
			for (JsonNode an : answers) {
				InterestResultOption option = new InterestResultOption();
				option.setContent(an.get("description").asText());
				option.setResultNum(an.get("conclusionNo").asText());
				option.setLowestScore(an.get("lowPoint").asInt());
				option.setHighestScore(an.get("maxPoint").asInt());
				option.setInterestResult(result);
				interestResultOptions.add(option);
				// interestResultOptionRepository.save(option)
			}

			interestResultOptionRepository.save(interestResultOptions);

			// interestService.saveInterest(interestQuestions,
			// interestResultOptions);
			return "{\"success\" : \"1\"}";
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "{\"success\" : \"0\"}";

	}
}
