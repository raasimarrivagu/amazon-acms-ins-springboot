package org.amazon.ins.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * 
 * @authors Manasa, Raasi, Sanjana
 *
 */
@Entity
@Table(name="issue")
public class Issue {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	Long id;
	
	@NotNull
	String issueCategory;
	
	@NotNull
	@Lob
	String description;
	
	@NotNull
	String impact;
	
	@NotNull
	Long timeToResolve;
	
	
	public Long getId() {
		return id;
	}
	
	public String getDescription() {
		return description;
	}
	
	public String getIssueCategory() {
		return issueCategory;
	}
	
	public String getImpact() {
		return impact;
	}
	
	public Long getTimeToResolve() {
		return timeToResolve;
	}
}
