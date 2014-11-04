package com.qubaopen.survey.entity.user;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * 用户称号
 * 
 * @author mars
 *
 */
@Entity
@Table(name = "user_self_title")
@Audited
public class UserSelfTitle extends AbstractPersistable<Long> {

	private static final long serialVersionUID = -5008381929936459798L;

	/**
	 * 称号
	 */
	private String name;

	/**
	 * 分数下限
	 */
	private double minScore;

	/**
	 * 分数上线
	 */
	private double maxScore;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getMinScore() {
		return minScore;
	}

	public void setMinScore(double minScore) {
		this.minScore = minScore;
	}

	public double getMaxScore() {
		return maxScore;
	}

	public void setMaxScore(double maxScore) {
		this.maxScore = maxScore;
	}

}
