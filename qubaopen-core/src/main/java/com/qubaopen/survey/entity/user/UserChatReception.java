package com.qubaopen.survey.entity.user;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;

import com.qubaopen.core.entity.AbstractBaseEntity;

/**
 * @author mars 用户聊天接收表
 */
@Entity
@Table(name = "user_chat_reception")
@Audited
public class UserChatReception extends AbstractBaseEntity<Long> {

	private static final long serialVersionUID = 912167512208766053L;

	/**
	 * 发送方
	 */
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sender_id", nullable = false, unique = true)
	private User sender;

	/**
	 * 接受方
	 */
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "recipient_id", nullable = false, unique = true)
	private User recipient;

	/**
	 * 接收时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	private Date receptionTime;

	/**
	 * 服务器获取内容的时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	private Date getContentTime;

	/**
	 * 内容
	 */
	private String content;

	public User getSender() {
		return sender;
	}

	public void setSender(User sender) {
		this.sender = sender;
	}

	public User getRecipient() {
		return recipient;
	}

	public void setRecipient(User recipient) {
		this.recipient = recipient;
	}

	public Date getReceptionTime() {
		return receptionTime;
	}

	public void setReceptionTime(Date receptionTime) {
		this.receptionTime = receptionTime;
	}

	public Date getGetContentTime() {
		return getContentTime;
	}

	public void setGetContentTime(Date getContentTime) {
		this.getContentTime = getContentTime;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
