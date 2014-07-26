package com.qubaopen.survey.entity.user;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.AbstractPersistable;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author mars 用户身份证资料库，将验证完的合法身份证存放在此处
 */
@Entity
@Table(name = "user_id_card")
@Audited
public class UserIDCard extends AbstractPersistable<Long> {

	private static final long serialVersionUID = -3372595247528105990L;

	/**
	 * 身份证
	 */
	private String IDCard;

	/**
	 * 姓名
	 */
	private String name;

	/**
	 * 照片
	 */
	@Lob
	@Basic(fetch = FetchType.LAZY)
	@JsonIgnore
	private byte[] photo;

	public String getIDCard() {
		return IDCard;
	}

	public void setIDCard(String iDCard) {
		IDCard = iDCard;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public byte[] getPhoto() {
		return photo;
	}

	public void setPhoto(byte[] photo) {
		this.photo = photo;
	}

}
