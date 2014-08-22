package com.qubaopen.survey.entity.survey;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * 调研问卷 问题 选项 Created by duel on 2014/6/25.
 */
@Entity
@Table(name = "SURVEY_QUESTION_OPTION")
@Audited
public class SurveyQuestionOption extends AbstractPersistable<Long> {

	private static final long serialVersionUID = 6579787765729925547L;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "survey_question_id")
	private SurveyQuestion surveyQuestion;

	/**
	 * 选项内容
	 */
	private String content;

	/**
	 * 选项编号 A B C /1 2 3
	 */
	private String serialNum;

	private int sorce;


	private String picPath;

	public SurveyQuestion getSurveyQuestion() {
		return surveyQuestion;
	}

	public void setSurveyQuestion(SurveyQuestion surveyQuestion) {
		this.surveyQuestion = surveyQuestion;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getSerialNum() {
		return serialNum;
	}

	public void setSerialNum(String serialNum) {
		this.serialNum = serialNum;
	}

	public String getPicPath() {
		return picPath;
	}

	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}

	public int getSorce() {
		return sorce;
	}

	public void setSorce(int sorce) {
		this.sorce = sorce;
	}

}
