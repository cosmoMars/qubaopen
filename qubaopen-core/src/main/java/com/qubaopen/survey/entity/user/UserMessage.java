package com.qubaopen.survey.entity.user;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;

import com.qubaopen.core.entity.AbstractBaseEntity;

/**
 * 用户站内信 Created by duel on 2014/6/30.
 */
@Entity
@Table(name = "user_message")
@Audited
public class UserMessage extends AbstractBaseEntity<Long> {

	private static final long serialVersionUID = 7714281517994739016L;

	/**
	 * 用户
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	/**
	 * 站内信标题
	 */
	private String title;

	/**
	 * 站内信内容
	 */
	private String content;

	/**
	 * 是否推送
	 */
	private boolean push;

	/**
	 * 是否已读
	 */
	private boolean readed;

	/**
	 * 站内信类型
	 *
	 * 0 系统信息 1 中奖信息 2 私信 3 好友请求 4 调研问卷跳转 5奖品跳转 (4,5不存在站内信)
	 */
	private MessageType messageType;

	private enum MessageType {
		SYSTEM, REWARD, PRIVATE, FRIEND, SURVEYJUMP, REWARDJUMP
	}

	/**
	 * 发送标志 0未发送 1发送中 2已发送
	 */
	@Enumerated
	private Transmit transmit;

	/**
	 * NOTRANSMIT 0 未发送, TRANSMITTING 1 发送中,TRANSMITTED 2 已发送
	 */
	private enum Transmit {
		NOTRANSMIT, TRANSMITTING, TRANSMITTED
	}

	/**
	 * 发送来源
	 */
	private String sendSource;

	/**
	 * 推送时间
	 */
	@Temporal(TemporalType.TIME)
	private Date pushTime;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Boolean getPush() {
		return push;
	}

	public void setPush(Boolean push) {
		this.push = push;
	}

	public Boolean getReaded() {
		return readed;
	}

	public void setReaded(Boolean readed) {
		this.readed = readed;
	}

	public MessageType getMessageType() {
		return messageType;
	}

	public void setMessageType(MessageType messageType) {
		this.messageType = messageType;
	}

	public Transmit getTransmit() {
		return transmit;
	}

	public void setTransmit(Transmit transmit) {
		this.transmit = transmit;
	}

	public String getSendSource() {
		return sendSource;
	}

	public void setSendSource(String sendSource) {
		this.sendSource = sendSource;
	}

	public Date getPushTime() {
		return pushTime;
	}

	public void setPushTime(Date pushTime) {
		this.pushTime = pushTime;
	}

}
