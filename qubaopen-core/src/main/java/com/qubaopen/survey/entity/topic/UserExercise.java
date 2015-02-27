package com.qubaopen.survey.entity.topic;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import com.qubaopen.core.entity.AbstractBaseEntity;
import com.qubaopen.survey.entity.user.User;

@Entity
@Table(name = "user_exercise")
@Audited
public class UserExercise extends AbstractBaseEntity<Long> {

	private static final long serialVersionUID = 603280928908933588L;

	/**
	 * 用户
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	private User user;

	/**
	 * 练习
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	private Exercise exercise;

	/**
	 * 完成题号
	 */
	private String number;

	/**
	 * 是否已完成
	 */
	private boolean complete;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Exercise getExercise() {
		return exercise;
	}

	public void setExercise(Exercise exercise) {
		this.exercise = exercise;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public boolean isComplete() {
		return complete;
	}

	public void setComplete(boolean complete) {
		this.complete = complete;
	}

}
