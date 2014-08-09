package com.qubaopen.survey.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * 问卷标签 码表 问卷列表中显示的标签 （例如：“热门” “最新” “推荐”等） Created by duel on 2014/6/25.
 */

@Entity
@Table(name = "QUESTIONNAIRE_TAG_TYPE")
@Audited
public class QuestionnaireTagType extends AbstractPersistable<Long> {

	private static final long serialVersionUID = -2335552412742913115L;

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
