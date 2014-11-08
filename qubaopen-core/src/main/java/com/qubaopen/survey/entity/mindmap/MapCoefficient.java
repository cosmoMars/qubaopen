package com.qubaopen.survey.entity.mindmap;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.qubaopen.core.entity.AbstractBaseEntity2;
import com.qubaopen.survey.entity.user.User;

/**
 * 心理地图系数表
 * @author mars
 *
 */
@Entity
@Table(name = "map_conefficient")
@Audited
public class MapCoefficient extends AbstractBaseEntity2<Long> {

	private static final long serialVersionUID = -1078133878171469128L;

	/**
	 * 用户
	 */
	@OneToOne(fetch = FetchType.LAZY, optional = false)
	@PrimaryKeyJoinColumn
	@JsonIgnore
	private User user;

	/**
	 * 纪录时间
	 */
	private Date time;
	
	/**
	 * 系数pa1
	 */
	private double pa1;

	/**
	 * 系数pa2
	 */
	private double pa2;

	/**
	 * 系数pa3
	 */
	private double pa3;

	/**
	 * 系数pa4
	 */
	private double pa4;
	
	private double na1;
	
	private double na2;
	
	private double na3;
	
	private double na4;
	
	private double mid1;
	
	private double mid2;
	
	private double mid3;
	
	private double mid4;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public double getPa1() {
		return pa1;
	}

	public void setPa1(double pa1) {
		this.pa1 = pa1;
	}

	public double getPa2() {
		return pa2;
	}

	public void setPa2(double pa2) {
		this.pa2 = pa2;
	}

	public double getPa3() {
		return pa3;
	}

	public void setPa3(double pa3) {
		this.pa3 = pa3;
	}

	public double getPa4() {
		return pa4;
	}

	public void setPa4(double pa4) {
		this.pa4 = pa4;
	}

	public double getNa1() {
		return na1;
	}

	public void setNa1(double na1) {
		this.na1 = na1;
	}

	public double getNa2() {
		return na2;
	}

	public void setNa2(double na2) {
		this.na2 = na2;
	}

	public double getNa3() {
		return na3;
	}

	public void setNa3(double na3) {
		this.na3 = na3;
	}

	public double getNa4() {
		return na4;
	}

	public void setNa4(double na4) {
		this.na4 = na4;
	}

	public double getMid1() {
		return mid1;
	}

	public void setMid1(double mid1) {
		this.mid1 = mid1;
	}

	public double getMid2() {
		return mid2;
	}

	public void setMid2(double mid2) {
		this.mid2 = mid2;
	}

	public double getMid3() {
		return mid3;
	}

	public void setMid3(double mid3) {
		this.mid3 = mid3;
	}

	public double getMid4() {
		return mid4;
	}

	public void setMid4(double mid4) {
		this.mid4 = mid4;
	}
	
}
