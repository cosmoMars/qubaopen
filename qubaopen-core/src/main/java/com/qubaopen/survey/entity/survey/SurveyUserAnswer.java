package com.qubaopen.survey.entity.survey;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.AbstractPersistable;

import com.qubaopen.survey.entity.user.User;

/**
 * 调研问卷 用户问卷 答题内容 Created by duel on 2014/6/26.
 */

@Entity
@Table(name = "survey_user_answer")
@Audited
public class SurveyUserAnswer extends AbstractPersistable<Long> {

	private static final long serialVersionUID = -4958815591444663072L;

	/**
	 * 用户
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	/**
	 * 调研用户问卷
	 */
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "survey_user_questionnaire_id")
	private SurveyUserQuestionnaire surveyUserQuestionnaire;

	/**
	 * 调研问题
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "survey_question_id")
	private SurveyQuestion surveyQuestion;

	/**
	 * 调研问题选项
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "survey_question_option_id")
	private SurveyQuestionOption surveyQuestionOption;

	/**
	 * 内容 问答题使用
	 */
	private String content;

	/**
	 * 顺序 排序题使用
	 */
	private String turn;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public SurveyUserQuestionnaire getSurveyUserQuestionnaire() {
		return surveyUserQuestionnaire;
	}

	public void setSurveyUserQuestionnaire(SurveyUserQuestionnaire surveyUserQuestionnaire) {
		this.surveyUserQuestionnaire = surveyUserQuestionnaire;
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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTurn() {
		return turn;
	}

	public void setTurn(String turn) {
		this.turn = turn;
	}

}
