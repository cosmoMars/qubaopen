package com.qubaopen.survey.entity;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.AbstractPersistable;

import com.qubaopen.survey.entity.interest.Interest;
import com.qubaopen.survey.entity.survey.Survey;

/**
 * 问卷标签 码表 问卷列表中显示的标签 （例如：“热门” “最新” “推荐”等） Created by duel on 2014/6/25.
 */

@Entity
@Table(name = "QUESTIONNAIRE_TAG_TYPE")
@Audited
public class QuestionnaireTagType extends AbstractPersistable<Long> {

	private static final long serialVersionUID = -2335552412742913115L;

	/**
	 * 名称
	 */
	private String name;

	/**
	 * 兴趣问卷集合
	 */
	@ManyToMany
	@JoinTable(name = "interest_questionnaire_relation", joinColumns = @JoinColumn(name = "type_id"), inverseJoinColumns = @JoinColumn(name = "interest_id"))
	private Set<Interest> interests;

	/**
	 * 调研问卷集合
	 */
	@ManyToMany
	@JoinTable(name = "survey_questionnaire_relation", joinColumns = @JoinColumn(name = "type_id"), inverseJoinColumns = @JoinColumn(name = "survey_id"))
	private Set<Survey> surveys;


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<Interest> getInterests() {
		return interests;
	}

	public void setInterests(Set<Interest> interests) {
		this.interests = interests;
	}

	public Set<Survey> getSurveys() {
		return surveys;
	}

	public void setSurveys(Set<Survey> surveys) {
		this.surveys = surveys;
	}

}
