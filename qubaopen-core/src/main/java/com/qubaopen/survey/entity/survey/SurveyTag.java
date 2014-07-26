package com.qubaopen.survey.entity.survey;

import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

import com.qubaopen.core.entity.AbstractBaseEntity;
import com.qubaopen.survey.entity.QuestionnaireTagType;

/**
 * 调研问卷 问卷标签 Created by duel on 2014/6/25.
 */

// @Entity
// @Table(name = "SURVEY_TAG")
public class SurveyTag extends AbstractBaseEntity<Long> {

	private static final long serialVersionUID = 4760114301673404621L;

	/**
	 * 所从属的调研问卷
	 */
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	private Survey survey;

	/**
	 * 问卷标签
	 */
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	private QuestionnaireTagType questionnaireTagType;

	public Survey getSurvey() {
		return survey;
	}

	public void setSurvey(Survey survey) {
		this.survey = survey;
	}

	public QuestionnaireTagType getQuestionnaireTagType() {
		return questionnaireTagType;
	}

	public void setQuestionnaireTagType(QuestionnaireTagType questionnaireTagType) {
		this.questionnaireTagType = questionnaireTagType;
	}

}
