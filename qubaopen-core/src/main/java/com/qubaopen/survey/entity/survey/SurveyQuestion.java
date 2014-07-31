package com.qubaopen.survey.entity.survey;

import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.AbstractPersistable;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 调研问卷 问题 Created by duel on 2014/6/25.
 */
@Entity
@Table(name = "SURVEY_QUESTION")
@Audited
public class SurveyQuestion extends AbstractPersistable<Long> {

	private static final long serialVersionUID = 4209633666066983696L;

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
	 * 编号
	 */
	private String serialNumber;

	/**
	 * 选项数量
	 */
	private Integer optionCount;

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
	private Integer selectionLimit;

	/**
	 * 该题目所从属的矩阵题
	 */
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "survey_question_group_id")
	private SurveyQuestionGroup surveyQuestionGroup;

	/**
	 * 问卷答案
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "surveyQuestion", cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
	private Set<SurveyQuestionOption> surveyQuestionOptions;

	@Lob
	@Basic(fetch = FetchType.LAZY)
	@JsonIgnore
	private byte[] pic;

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

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public Integer getOptionCount() {
		return optionCount;
	}

	public void setOptionCount(Integer optionCount) {
		this.optionCount = optionCount;
	}
//
//	public SurveyQuestionType getSurveyQuestionType() {
//		return surveyQuestionType;
//	}
//
//	public void setSurveyQuestionType(SurveyQuestionType surveyQuestionType) {
//		this.surveyQuestionType = surveyQuestionType;
//	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Integer getSelectionLimit() {
		return selectionLimit;
	}

	public void setSelectionLimit(Integer selectionLimit) {
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

	public byte[] getPic() {
		return pic;
	}

	public void setPic(byte[] pic) {
		this.pic = pic;
	}

	public Integer getDurationLimit() {
		return durationLimit;
	}

	public void setDurationLimit(Integer durationLimit) {
		this.durationLimit = durationLimit;
	}

	public boolean isCharactered() {
		return charactered;
	}

	public void setCharactered(boolean charactered) {
		this.charactered = charactered;
	}

}
