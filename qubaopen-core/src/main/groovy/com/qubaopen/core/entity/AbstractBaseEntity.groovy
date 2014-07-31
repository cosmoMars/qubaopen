package com.qubaopen.core.entity

import static javax.persistence.TemporalType.TIMESTAMP

import javax.persistence.Column
import javax.persistence.MappedSuperclass
import javax.persistence.Temporal

import org.joda.time.DateTime
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.AbstractPersistable

import com.fasterxml.jackson.annotation.JsonIgnore

@MappedSuperclass
abstract class AbstractBaseEntity<ID extends Serializable> extends AbstractPersistable<ID> {

	static final long serialVersionUID = 2834452590374861385L

	@JsonIgnore
	Long createdBy

	@CreatedDate
	@Temporal(TIMESTAMP)
	@Column(updatable = false)
	@JsonIgnore
	Date createdDate

	@JsonIgnore
	Long lastModifiedBy

	@LastModifiedDate
	@Temporal(TIMESTAMP)
	@JsonIgnore
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
