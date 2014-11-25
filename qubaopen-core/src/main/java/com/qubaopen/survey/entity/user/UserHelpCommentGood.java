package com.qubaopen.survey.entity.user;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import com.qubaopen.core.entity.AbstractBaseEntity;

@Entity
@Table(name = "user_help_comment_good")
@Audited
public class UserHelpCommentGood extends AbstractBaseEntity<Long> {

	private static final long serialVersionUID = 5477962123243153417L;

	@ManyToOne(fetch = FetchType.LAZY)
	private User user;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private UserHelpComment userHelpComment;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public UserHelpComment getUserHelpComment() {
		return userHelpComment;
	}

	public void setUserHelpComment(UserHelpComment userHelpComment) {
		this.userHelpComment = userHelpComment;
	}
	
}
