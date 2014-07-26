package com.qubaopen.survey.entity.self;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
	 * 问卷类型
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "self_result_type_id")
	private SelfResultType selfResultType;

	/**
	 * 标题
	 */
	private String title;

	public Self getSelf() {
		return self;
	}

	public void setSelf(Self self) {
		this.self = self;
	}

	public SelfResultType getSelfResultType() {
		return selfResultType;
	}

	public void setSelfResultType(SelfResultType selfResultType) {
		this.selfResultType = selfResultType;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
