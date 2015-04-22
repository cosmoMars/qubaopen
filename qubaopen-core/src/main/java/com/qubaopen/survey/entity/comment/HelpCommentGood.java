package com.qubaopen.survey.entity.comment;

import com.qubaopen.core.entity.AbstractBaseEntity;
import com.qubaopen.survey.entity.user.User;
import org.hibernate.envers.Audited;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author mars
 * 求助点赞
 */
@Entity
@Table(name = "help_comment_good")
@Audited
public class HelpCommentGood extends AbstractBaseEntity<Long> {

	private static final long serialVersionUID = 5477962123243153417L;

	@ManyToOne(fetch = FetchType.LAZY)
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	private HelpComment helpComment;

	/**
	 * 是否查看
	 */
	private boolean view;

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

	public boolean isView() {
		return view;
	}

	public void setView(boolean view) {
		this.view = view;
	}
}
