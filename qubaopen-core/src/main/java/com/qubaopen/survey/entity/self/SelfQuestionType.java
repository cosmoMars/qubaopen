package com.qubaopen.survey.entity.self;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * @author mars 自测问卷问题类型码表
 */
@Entity
@Table(name = "self_question_type")
@Audited
public class SelfQuestionType extends AbstractPersistable<Long> {

	private static final long serialVersionUID = -327879623653079584L;
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
