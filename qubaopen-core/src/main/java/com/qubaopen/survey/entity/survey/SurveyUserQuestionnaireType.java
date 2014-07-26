package com.qubaopen.survey.entity.survey;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * 调研问卷 用户问卷 状态 Created by duel on 2014/6/26.
 */

@Entity
@Table(name = "SURVEY_USER_QUESTIONNAIRE_TYPE")
@Audited
public class SurveyUserQuestionnaireType extends AbstractPersistable<Long> {

	private static final long serialVersionUID = -76651313268340341L;

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
