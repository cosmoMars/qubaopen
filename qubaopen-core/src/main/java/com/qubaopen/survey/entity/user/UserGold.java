package com.qubaopen.survey.entity.user;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import com.qubaopen.core.entity.AbstractBaseEntity2;

/**
 * @author mars 用户金币表
 */
@Entity
@Table(name = "user_gold")
@Audited
public class UserGold extends AbstractBaseEntity2<Long> {

	private static final long serialVersionUID = -8605208860664034157L;

	/**
	 * 用户
	 */
	@OneToOne(fetch = FetchType.LAZY, optional = false)
	@PrimaryKeyJoinColumn
	private User user;

	/**
	 * 历史总金币
	 */
	private int historyGold;

	/**
	 * 当前金币
	 */
	private int currentGold;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public int getHistoryGold() {
		return historyGold;
	}

	public void setHistoryGold(int historyGold) {
		this.historyGold = historyGold;
	}

	public int getCurrentGold() {
		return currentGold;
	}

	public void setCurrentGold(int currentGold) {
		this.currentGold = currentGold;
	}

}
