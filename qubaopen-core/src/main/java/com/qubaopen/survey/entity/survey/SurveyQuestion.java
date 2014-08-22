package com.qubaopen.survey.entity.survey;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * 调研问卷 问题 Created by duel on 2014/6/25.
 */
@Entity
@Table(name = "SURVEY_QUESTION")
@Audited
public class SurveyQuestion extends AbstractPersistable<Long> {

	private static final long serialVersionUID = -7993683241226191949L;

	/**
	 * 所从属的调研问卷
	 */
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "survey_id")
	private Survey survey;

	/**
	 * 内容
	 */
	private String content;

	/**
	 * 题号
	 */
	private String questionNum;

	/**
	 * 选项数量
	 */
	private int optionCount;

//	/**
//	 * 题目类型 选择 问答 排序 打分
//	 */
//	@ManyToOne(fetch = FetchType.LAZY, optional = false)
//	@JoinColumn(name = "survey_question_type_id")
//	private SurveyQuestionType surveyQuestionType;

	/**
	 * 问卷类型  单选 SINGLE, 多选 MULTIPLE, 问答 QA, 排序 SORT, 打分 SCORE
	 */
	@Enumerated
	private Type type;

	private enum Type {
		SINGLE, MULTIPLE, QA, SORT, SCORE
	}

	/**
	 * 是否为性格题
	 */
	private boolean charactered;

	/**
	 * 答题最短时间限制
	 */
	private Integer durationLimit;

	/**
	 * 最多选择数量限制
	 */
	private int selectionLimit;

	/**
	 * 该题目所从属的矩阵题
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "survey_question_group_id")
	private SurveyQuestionGroup surveyQuestionGroup;

	/**
	 * 问卷答案
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "surveyQuestion", cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
	private Set<SurveyQuestionOption> surveyQuestionOptions;

	private String picPath;

	public Survey getSurvey() {
		return survey;
	}

	public void setSurvey(Survey survey) {
		this.survey = survey;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getQuestionNum() {
		return questionNum;
	}

	public void setQuestionNum(String questionNum) {
		this.questionNum = questionNum;
	}

	public int getOptionCount() {
		return optionCount;
	}

	public void setOptionCount(int optionCount) {
		this.optionCount = optionCount;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public boolean isCharactered() {
		return charactered;
	}

	public void setCharactered(boolean charactered) {
		this.charactered = charactered;
	}

	public Integer getDurationLimit() {
		return durationLimit;
	}

	public void setDurationLimit(Integer durationLimit) {
		this.durationLimit = durationLimit;
	}

	public int getSelectionLimit() {
		return selectionLimit;
	}

	public void setSelectionLimit(int selectionLimit) {
		this.selectionLimit = selectionLimit;
	}

	public SurveyQuestionGroup getSurveyQuestionGroup() {
		return surveyQuestionGroup;
	}

	public void setSurveyQuestionGroup(SurveyQuestionGroup surveyQuestionGroup) {
		this.surveyQuestionGroup = surveyQuestionGroup;
	}

	public Set<SurveyQuestionOption> getSurveyQuestionOptions() {
		return surveyQuestionOptions;
	}

	public void setSurveyQuestionOptions(Set<SurveyQuestionOption> surveyQuestionOptions) {
		this.surveyQuestionOptions = surveyQuestionOptions;
	}

	public String getPicPath() {
		return picPath;
	}

	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}

}
