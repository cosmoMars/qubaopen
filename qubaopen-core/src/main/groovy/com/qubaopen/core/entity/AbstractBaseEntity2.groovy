package com.qubaopen.core.entity

import static javax.persistence.TemporalType.TIMESTAMP

import javax.persistence.Column
import javax.persistence.Id
import javax.persistence.MappedSuperclass
import javax.persistence.Temporal

import org.joda.time.DateTime
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate

@MappedSuperclass
abstract class AbstractBaseEntity2<ID extends Serializable> implements Serializable {

	static final long serialVersionUID = 4051847990560824873L

	@Id
	Long id

	Long createdBy

	@CreatedDate
	@Temporal(TIMESTAMP)
	@Column(updatable = false)
	Date createdDate

	Long lastModifiedBy

	@LastModifiedDate
	@Temporal(TIMESTAMP)
	Date lastModifiedDate

	DateTime getCreatedDate() {
		null == createdDate ? null : new DateTime(createdDate)
	}

	void setCreatedDate(DateTime createdDate) {
		this.createdDate = null == createdDate ? null : createdDate.toDate()
	}

	DateTime getLastModifiedDate() {
		null == lastModifiedDate ? null : new DateTime(lastModifiedDate)
	}

	void setLastModifiedDate(DateTime lastModifiedDate) {
		this.lastModifiedDate = null == lastModifiedDate ? null : lastModifiedDate.toDate()
	}

}
