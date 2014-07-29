package com.qubaopen.survey.entity.self;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * @author mars 自测问题类型码表
 */
@Entity
@Table(name = "self_question_type")
@Audited
public class SelfQuestionType extends AbstractPersistable<Long> {

	private static final long serialVersionUID = -4628955204696291427L;

	/**
	 * 名称
	 */
	private String name;

	/**
	 * 别名
	 */
	private String nickName;

	/**
	 * 备注
	 */
	private String remark;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
