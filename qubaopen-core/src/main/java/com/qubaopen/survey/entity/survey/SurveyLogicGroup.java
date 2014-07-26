package com.qubaopen.survey.entity.survey;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * 调研问卷 逻辑组 逻辑关系 Created by duel on 2014/6/26.
 */
@Entity
@Table(name = "SURVEY_LOGIC_GROUP")
@Audited
public class SurveyLogicGroup extends AbstractPersistable<Long> {

	private static final long serialVersionUID = 2321404457632121755L;

	/**
	 * 所从属的调研问卷
	 */
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "survey_id")
	private Survey survey;

	/**
	 * 组成员之间的关系 0 或 1 和
	 */
	@Enumerated
	private Relation relation;

	/**
	 * 0 或 1 和
	 */
	private enum Relation {
		OR, AND
	}

	/**
	 * 可为空 复杂逻辑使用 组中组
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "survey_logic_group_id")
	private SurveyLogicGroup surveyLogicGroup;

	/**
	 * 跳转至的题目（结论题号）
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name ="survey_question_id")
	private SurveyQuestion nextQuestion;

	/**
	 * 对结论题号进行的操作（例如：跳转至、跳过等）
	 */
	private Long todo;

	public Survey getSurvey() {
		return survey;
	}

	public void setSurvey(Survey survey) {
		this.survey = survey;
	}

	public Relation getRelation() {
		return relation;
	}

	public void setRelation(Relation relation) {
		this.relation = relation;
	}

	public SurveyLogicGroup getSurveyLogicGroup() {
		return surveyLogicGroup;
	}

	public void setSurveyLogicGroup(SurveyLogicGroup surveyLogicGroup) {
		this.surveyLogicGroup = surveyLogicGroup;
	}

	public SurveyQuestion getNextQuestion() {
		return nextQuestion;
	}

	public void setNextQuestion(SurveyQuestion nextQuestion) {
		this.nextQuestion = nextQuestion;
	}

	public Long getTodo() {
		return todo;
	}

	public void setTodo(Long todo) {
		this.todo = todo;
	}

}
