package com.qubaopen.survey.entity.topic;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import com.qubaopen.core.entity.AbstractBaseEntity;
import com.qubaopen.survey.entity.user.User;

@Entity
@Table(name = "user_exercise_info_log")
@Audited
public class UserExerciseInfoLog extends AbstractBaseEntity<Long> {

	private static final long serialVersionUID = 3206492307053461561L;

	@ManyToOne(fetch = FetchType.LAZY)
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	private ExerciseInfo exerciseInfo;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public ExerciseInfo getExerciseInfo() {
		return exerciseInfo;
	}

	public void setExerciseInfo(ExerciseInfo exerciseInfo) {
		this.exerciseInfo = exerciseInfo;
	}

}
