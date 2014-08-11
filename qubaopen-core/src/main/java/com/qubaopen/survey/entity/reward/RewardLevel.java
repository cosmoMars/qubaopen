package com.qubaopen.survey.entity.reward;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * 奖品类型 Created by duel on 2014/6/27.
 */
@Entity
@Table(name = "reward_level")
@Audited
public class RewardLevel extends AbstractPersistable<Long> {

	private static final long serialVersionUID = -145447906458238247L;

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
