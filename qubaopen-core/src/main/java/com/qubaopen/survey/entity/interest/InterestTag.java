package com.qubaopen.survey.entity.interest;

import javax.persistence.ManyToOne;

import com.qubaopen.core.entity.AbstractBaseEntity;
import com.qubaopen.survey.entity.QuestionnaireTagType;

/**
 * @author mars 问卷标签表
 */
// @Entity
// @Table(name = "interest_tag")
public class InterestTag extends AbstractBaseEntity<Long> {

	private static final long serialVersionUID = 7681067867127150577L;

	@ManyToOne
	private Interest interest;

	/**
	 * 问卷标签
	 */
	@ManyToOne
	private QuestionnaireTagType questionnaireTagType;

	public Interest getInterest() {
		return interest;
	}

	public void setInterest(Interest interest) {
		this.interest = interest;
	}

	public QuestionnaireTagType getQuestionnaireTagType() {
		return questionnaireTagType;
	}

	public void setQuestionnaireTagType(QuestionnaireTagType questionnaireTagType) {
		this.questionnaireTagType = questionnaireTagType;
	}

}
