package com.qubaopen.survey.entity.survey;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * 调研问卷 问题 顺序 （逻辑） Created by duel on 2014/6/26.
 */

@Entity
@Table(name = "SURVEY_LOGIC")
@Audited
public class SurveyLogic extends AbstractPersistable<Long> {

	private static final long serialVersionUID = 2483392548204300813L;

	/**
	 * 从属的组
	 */
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "survey_logic_group_id")
	private SurveyLogicGroup surveyLogicGroup;

	/**
	 * 问卷题目 本题id
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "survey_question_id")
	private SurveyQuestion surveyQuestion;

	/**
	 * 题目选择的选项
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "survey_question_option_id")
	private SurveyQuestionOption surveyQuestionOption;

	public SurveyLogicGroup getSurveyLogicGroup() {
		return surveyLogicGroup;
	}

	public void setSurveyLogicGroup(SurveyLogicGroup surveyLogicGroup) {
		this.surveyLogicGroup = surveyLogicGroup;
	}

	public SurveyQuestion getSurveyQuestion() {
		return surveyQuestion;
	}

	public void setSurveyQuestion(SurveyQuestion surveyQuestion) {
		this.surveyQuestion = surveyQuestion;
	}

	public SurveyQuestionOption getSurveyQuestionOption() {
		return surveyQuestionOption;
	}

	public void setSurveyQuestionOption(SurveyQuestionOption surveyQuestionOption) {
		this.surveyQuestionOption = surveyQuestionOption;
	}

}
