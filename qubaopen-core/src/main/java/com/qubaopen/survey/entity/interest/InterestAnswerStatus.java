package com.qubaopen.survey.entity.interest;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * @author mars 兴趣问卷用户答卷状态码表
 */
@Entity
@Table(name = "interest_answer_status")
@Audited
public class InterestAnswerStatus extends AbstractPersistable<Long> {

	private static final long serialVersionUID = -7887564106764682817L;

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
