package com.qubaopen.survey.entity.system;

import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.*;
import java.util.Date;

/**
 * sms189 中国短信网回调 Created by duel on 2014/6/30.
 */
@Entity
@Table(name = "sms_call_back")
@Audited
public class SmsCallBack extends AbstractPersistable<Long> {

	private static final long serialVersionUID = -4276646803175758354L;

	/**
	 * 短信模版id
	 */
	private String templeteId;

	/**
	 * 短信回调值
	 */
	private String resCode;

    /**
     * 手机
     */
    private String phone;

	/**
	 * 时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	private Date time;

    public String getTempleteId() {
        return templeteId;
    }

    public void setTempleteId(String templeteId) {
        this.templeteId = templeteId;
    }

    public String getResCode() {
        return resCode;
    }

    public void setResCode(String resCode) {
        this.resCode = resCode;
    }

    public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
