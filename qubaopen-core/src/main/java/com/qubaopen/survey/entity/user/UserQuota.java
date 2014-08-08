package com.qubaopen.survey.entity.user;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;

import com.qubaopen.survey.entity.base.AreaCode;

/**
 * @author mars 用户配额表
 */
@Entity
@Table(name = "user_quota")
@Audited
public class UserQuota implements Serializable {

	private static final long serialVersionUID = 1215357259864269176L;

	@Id
	private long id;

	/**
	 * 用户id
	 */
	@OneToOne(fetch = FetchType.LAZY, optional = false)
	@PrimaryKeyJoinColumn
	private User user;

	/**
	 * 性别
	 */
	@Enumerated
	private Sex sex;

	private enum Sex {
		MALE, FEMALE, OTHER
	}

	/**
	 * 出生时间
	 */
	@Temporal(TemporalType.DATE)
	private Date birthday;
	
	/**
	 * 地区代码
	 */
//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "region_code_id")
//	private RegionCode regionCode;
	
	/**
	 * 地区代码
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "area_code_id")
	private AreaCode areaCode;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Sex getSex() {
		return sex;
	}

	public void setSex(Sex sex) {
		this.sex = sex;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public AreaCode getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(AreaCode areaCode) {
		this.areaCode = areaCode;
	}

}
