package com.qubaopen.survey.entity.survey;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * 调研问卷 问题类型 码表 Created by duel on 2014/6/25.
 */

@Entity
@Table(name = "SURVEY_QUESTION_TYPE")
@Audited
public class SurveyQuestionType extends AbstractPersistable<Long> {

	private static final long serialVersionUID = 3407928511100142970L;

	/**
	 * 名称
	 */
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}