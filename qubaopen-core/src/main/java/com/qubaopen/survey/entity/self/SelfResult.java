package com.qubaopen.survey.entity.self;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * @author mars 自测问卷结果表
 */
@Entity
@Table(name = "self_result")
@Audited
public class SelfResult extends AbstractPersistable<Long> {

	private static final long serialVersionUID = 5423415333691493148L;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "self_id")
	private Self self;

	/**
	 * ORDER 顺序得出结果, SCORE 积分得出结果
	 */
	private Type type;

	private enum Type {
		ORDER, SCORE
	}

	/**
	 * 标题
	 */
	private String title;

	/**
	 * 名称
	 */
	private String name;

	/**
	 * 备注
	 */
	@Column(columnDefinition = "TEXT")
	private String remark;

	public Self getSelf() {
		return self;
	}

	public void setSelf(Self self) {
		this.self = self;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
