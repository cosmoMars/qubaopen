package com.qubaopen.survey.entity.user;

import com.qubaopen.core.entity.AbstractBaseEntity;
import com.qubaopen.survey.entity.self.Self;
import com.qubaopen.survey.entity.topic.Topic;
import org.hibernate.envers.Audited;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "user_favorite")
@Audited
public class UserFavorite extends AbstractBaseEntity<Long> {

	private static final long serialVersionUID = -70499406453833782L;

	/**
	 * 用户
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	private User user;

	/**
	 * 专题
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	private Topic topic;

//	@ManyToMany
//	@JoinTable(name = "user_topic_relation", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "topic_id"))
//	private Set<Topic> topics;
//
//	@ManyToMany
//	@JoinTable(name = "user_self_relation", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "self_id"))
//	private Set<Self> selfs;

	/**
	 * 自测
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	private Self self;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Topic getTopic() {
		return topic;
	}

	public void setTopic(Topic topic) {
		this.topic = topic;
	}

	public Self getSelf() {
		return self;
	}

	public void setSelf(Self self) {
		this.self = self;
	}

}
