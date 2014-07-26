package com.qubaopen.survey.entity.user;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import com.qubaopen.core.entity.AbstractBaseEntity;

/**
 * @author mars 用户好友
 */
@Entity
@Table(name = "user_friends")
@Audited
public class UserFriend extends AbstractBaseEntity<Long> {

	private static final long serialVersionUID = -4345699594852842100L;

	/**
	 * 好友对应的用户
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	/**
	 * 好友
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "friend_id")
	private User friend;


	public void setUser(User user) {
		this.user = user;
	}

	public User getFriend() {
		return friend;
	}

	public void setFriend(User friend) {
		this.friend = friend;
	}

}
