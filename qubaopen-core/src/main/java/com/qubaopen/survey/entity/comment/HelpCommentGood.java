package com.qubaopen.survey.entity.comment;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import com.qubaopen.core.entity.AbstractBaseEntity;
import com.qubaopen.survey.entity.user.User;

@Entity
@Table(name = "help_comment_good")
@Audited
public class HelpCommentGood extends AbstractBaseEntity<Long> {

	private static final long serialVersionUID = 5477962123243153417L;

	@ManyToOne(fetch = FetchType.LAZY)
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	private HelpComment helpComment;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public HelpComment getHelpComment() {
		return helpComment;
	}

	public void setHelpComment(HelpComment helpComment) {
		this.helpComment = helpComment;
	}

}
