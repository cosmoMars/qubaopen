package com.qubaopen.backend.controller.self;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.qubaopen.backend.repository.GraphicsTypeRepository;
import com.qubaopen.backend.repository.QuestionnaireTypeRepository;
import com.qubaopen.backend.repository.SelfManagementTypeRepository;
import com.qubaopen.backend.repository.self.SelfGroupRepository;
import com.qubaopen.backend.repository.self.SelfQuestionOptionRepository;
import com.qubaopen.backend.repository.self.SelfQuestionOrderRepository;
import com.qubaopen.backend.repository.self.SelfQuestionRepository;
import com.qubaopen.backend.repository.self.SelfRepository;
import com.qubaopen.backend.repository.self.SelfResultOptionRepository;
import com.qubaopen.backend.repository.self.SelfResultRepository;
import com.qubaopen.core.controller.AbstractBaseController;
import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.QuestionnaireType;
import com.qubaopen.survey.entity.self.GraphicsType;
import com.qubaopen.survey.entity.self.Self;
import com.qubaopen.survey.entity.self.SelfGroup;
import com.qubaopen.survey.entity.self.SelfManagementType;
import com.qubaopen.survey.entity.self.SelfQuestion;
import com.qubaopen.survey.entity.self.SelfQuestionOption;
import com.qubaopen.survey.entity.self.SelfResult;
import com.qubaopen.survey.entity.self.SelfResultOption;

@RestController
@RequestMapping("self")
public class SelfController extends AbstractBaseController<Self, Long> {

	private static final Logger logger = LoggerFactory.getLogger(SelfController.class);

	@Autowired
	private QuestionnaireTypeRepository questionnaireTypeRepository;

	@Autowired
	private SelfManagementTypeRepository selfManagementTypeRepository;

	@Autowired
	private GraphicsTypeRepository graphicsTypeRepository;

	@Autowired
	private SelfRepository selfRepository;

	@Autowired
	private SelfQuestionRepository selfQuestionRepository;

	@Autowired
	private SelfQuestionOptionRepository selfQuestionOptionRepository;

	@Autowired
	private SelfResultRepository selfResultRepository;

	@Autowired
	private SelfResultOptionRepository selfResultOptionRepository;

	@Autowired
	private SelfGroupRepository selfGroupRepository;
	
	@Autowired
	private SelfQuestionOrderRepository selfQuestionOrderRepository;

	@Autowired
	private ObjectMapper objectMapper;

	
	@Override
	protected MyRepository<Self, Long> getRepository() {
		return selfRepository;
	}


	@Transactional
	@RequestMapping(value = "saveSelf", method = RequestMethod.POST)
	public String saveSelf(String json) {

		System.out.println(json);
		logger.debug(" =======================  json = {}", json);

		try {

			JsonNode jsonNode = objectMapper.readTree(json);

			QuestionnaireType qType = questionnaireTypeRepository.findOne(jsonNode.get("calType").asLong());

			// 问题
			SelfManagementType managementType = selfManagementTypeRepository.findOne(jsonNode.get("selfManagementType").asLong());

			GraphicsType graphicsType = graphicsTypeRepository.findOne(jsonNode.get("graphicsType").asLong());

			Self self = new Self();
			self.setTitle(jsonNode.get("title").asText());
			self.setMapMax(jsonNode.get("mapMax").asInt());
			self.setIntervalTime(jsonNode.get("intervalTime").asInt());
			self.setGraphicsType(graphicsType);
			self.setAbbreviation(jsonNode.get("abbreviation").asText());
			self.setGuidanceSentence(jsonNode.get("guidanceSentence").asText());
			self.setRemark(jsonNode.get("remark").asText());
			self.setRecommendedValue(jsonNode.get("recommendedValue").asInt());
			self.setRecommendedValue(jsonNode.get("tips").asInt());
			self.setSelfManagementType(managementType);
			self.setQuestionnaireType(qType);

			switch (jsonNode.get("status").asInt()) {
			case 0:
				self.setStatus(Self.Status.INITIAL);
				break;
			case 1:
				self.setStatus(Self.Status.ONLINE);
				break;
			case 2:
				self.setStatus(Self.Status.CLOSED);
				break;
			}

			self = selfRepository.save(self);

			ArrayNode questions = (ArrayNode) jsonNode.path("questions");
			for (JsonNode question : questions) {
				SelfQuestion sq = new SelfQuestion();
				sq.setQuestionNum(question.get("questionNo").asInt());
				sq.setContent(question.get("questionContent").asText());
				sq.setAnswerTimeLimit(question.get("limitTime").asInt());
				switch (question.get("questionLx").asInt()) {
				case 1:
					sq.setType(SelfQuestion.Type.SINGLE);
					break;
				case 2:
					sq.setType(SelfQuestion.Type.MULTIPLE);
					break;
				case 3:
					sq.setType(SelfQuestion.Type.QA);
					break;
				case 4:
					sq.setType(SelfQuestion.Type.SORT);
					break;
				case 5:
					sq.setType(SelfQuestion.Type.SCORE);
					break;
				case 6:
					sq.setType(SelfQuestion.Type.MULQA);
					break;
				}

				sq.setSelf(self);
				ArrayNode questionOptions = (ArrayNode) question.path("choices");
				sq.setOptionCount(questionOptions.size());
				sq = selfQuestionRepository.save(sq);
				for (JsonNode questionOption : questionOptions) {
					SelfQuestionOption option = new SelfQuestionOption();
					option.setContent(questionOption.get("choiceDescription").asText());
					option.setScore(questionOption.get("choicePoint").asInt());
					option.setSelfQuestion(sq);
					selfQuestionOptionRepository.save(option);
				}
				// interestQuestions.add(iq);
			}
			// interestQuestionRepository.save(interestQuestions);
			// 结果
			List<SelfResultOption> selfResultOptions = new ArrayList<SelfResultOption>();

			SelfResult result = new SelfResult();
			result.setTitle(jsonNode.get("answerTitle").asText());
			result.setSelf(self);

			result = selfResultRepository.save(result);

			ArrayNode answers = (ArrayNode) jsonNode.path("answers");
			for (JsonNode an : answers) {
				SelfResultOption option = new SelfResultOption();
				option.setContent(an.get("description").asText());
				option.setResultNum(an.get("conclusionNo").asText());
				option.setLowestScore(an.get("lowPoint").asInt());
				option.setHighestScore(an.get("maxPoint").asInt());
				option.setTitle(an.get("title").asText());
				option.setName(an.get("name").asText());
				option.setSelfResult(result);
				selfResultOptions.add(option);
				// interestResultOptionRepository.save(option)
			}

			selfResultOptionRepository.save(selfResultOptions);

			// interestService.saveInterest(interestQuestions,
			// interestResultOptions);
			return "{\"success\" : \"1\"}";
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "{\"success\" : \"0\"}";

	}
	
	

	@Transactional
	@RequestMapping(value = "generateSelf", method = RequestMethod.POST)
	public String generateSelf(String json) {

		System.out.println(json);
		logger.debug(" =======================  json = {}", json);

		try {

			JsonNode jsonNode = objectMapper.readTree(json);

			QuestionnaireType qType = questionnaireTypeRepository.findOne(jsonNode.get("calType").asLong());

			// 问题
			SelfManagementType managementType = selfManagementTypeRepository.findOne(jsonNode.get("selfManagementType").asLong());
			SelfManagementType selfManagementType = selfManagementTypeRepository.findOne(jsonNode.get("group_self_management_type_id").asLong());

			GraphicsType graphicsType = graphicsTypeRepository.findOne(jsonNode.get("graphicsType").asLong());
			
			SelfGroup selfGroup=new SelfGroup();
			selfGroup.setName(jsonNode.get("group_name").asText());
			selfGroup.setTitle(jsonNode.get("group_title").asText());
			selfGroup.setContent(jsonNode.get("group_content").asText());
			selfGroup.setMapMax(jsonNode.get("group_map_max").asInt());
			selfGroup.setRecommendedValue(jsonNode.get("group_recommended_value").asInt());
			selfGroup.setSelfManagementType(selfManagementType);
			selfGroup.setGraphicsType(graphicsType);
			selfGroup.setStatus(false);

			selfGroupRepository.save(selfGroup);

			Self self = new Self();
			self.setTitle(jsonNode.get("title").asText());
			self.setMapMax(jsonNode.get("mapMax").asInt());
			self.setIntervalTime(jsonNode.get("intervalTime").asInt());
			self.setGraphicsType(graphicsType);
			self.setAbbreviation(jsonNode.get("abbreviation").asText());
			self.setGuidanceSentence(jsonNode.get("guidanceSentence").asText());
			self.setRemark(jsonNode.get("remark").asText());
			self.setRecommendedValue(jsonNode.get("recommendedValue").asInt());
			self.setTips(jsonNode.get("tips").asText());
			self.setSelfManagementType(managementType);
			self.setQuestionnaireType(qType);
			self.setStatus(Self.Status.INITIAL);
			self.setSelfGroup(selfGroup);


			self = selfRepository.save(self);

//			ArrayNode questions = (ArrayNode) jsonNode.path("questions");
//			for (JsonNode question : questions) {
//				SelfQuestion sq = new SelfQuestion();
//				sq.setQuestionNum(question.get("questionNo").asInt());
//				sq.setContent(question.get("questionContent").asText());
//				sq.setAnswerTimeLimit(question.get("limitTime").asInt());
//				switch (question.get("questionLx").asInt()) {
//				case 1:
//					sq.setType(SelfQuestion.Type.SINGLE);
//					break;
//				case 2:
//					sq.setType(SelfQuestion.Type.MULTIPLE);
//					break;
//				case 3:
//					sq.setType(SelfQuestion.Type.QA);
//					break;
//				case 4:
//					sq.setType(SelfQuestion.Type.SORT);
//					break;
//				case 5:
//					sq.setType(SelfQuestion.Type.SCORE);
//					break;
//				case 6:
//					sq.setType(SelfQuestion.Type.MULQA);
//					break;
//				}
//
//				sq.setSelf(self);
//				ArrayNode questionOptions = (ArrayNode) question.path("choices");
//				sq.setOptionCount(questionOptions.size());
//				sq = selfQuestionRepository.save(sq);
//				for (JsonNode questionOption : questionOptions) {
//					SelfQuestionOption option = new SelfQuestionOption();
//					option.setContent(questionOption.get("choiceDescription").asText());
//					option.setScore(questionOption.get("choicePoint").asInt());
//					option.setSelfQuestion(sq);
//					selfQuestionOptionRepository.save(option);
//				}
//				// interestQuestions.add(iq);
//			}
//			// interestQuestionRepository.save(interestQuestions);
//			// 结果
//			List<SelfResultOption> selfResultOptions = new ArrayList<SelfResultOption>();
//
//			SelfResult result = new SelfResult();
//			result.setTitle(jsonNode.get("answerTitle").asText());
//			result.setSelf(self);
//
//			result = selfResultRepository.save(result);
//
//			ArrayNode answers = (ArrayNode) jsonNode.path("answers");
//			for (JsonNode an : answers) {
//				SelfResultOption option = new SelfResultOption();
//				option.setContent(an.get("description").asText());
//				option.setResultNum(an.get("conclusionNo").asText());
//				option.setLowestScore(an.get("lowPoint").asInt());
//				option.setHighestScore(an.get("maxPoint").asInt());
//				option.setTitle(an.get("title").asText());
//				option.setName(an.get("name").asText());
//				option.setSelfResult(result);
//				selfResultOptions.add(option);
//				// interestResultOptionRepository.save(option)
//			}
//
//			selfResultOptionRepository.save(selfResultOptions);

			// interestService.saveInterest(interestQuestions,
			// interestResultOptions);
			return "{\"success\" : \"1\"}";
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "{\"success\" : \"0\"}";

	}
	
	@RequestMapping(value = "test", method = RequestMethod.POST)
	private Object test(@RequestParam(required = false) String phone) {

		return "{\"success\" : \"1\"}";

	}

}
