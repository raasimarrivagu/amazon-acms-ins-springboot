package org.amazon.ins.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The entity ticket contains a ManyToOne mapping on User
 * 
 * @authors Manasa, Raasi, Sanjana
 *
 */

@Entity
@Table(name="ticket")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value={"createdOn","updateOn","status"},allowGetters=true)
public class Ticket implements Serializable{

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	Long id;
	
	@NotNull
	Long issueId;
	
	@NotNull
	Long priority;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(updatable=false,nullable=false)
	@CreatedDate
	Date createdOn;
	
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(updatable=true,nullable=false)
	@LastModifiedDate
	Date updatedOn;
	
	@Column(updatable=true,nullable=false)
	String status;
	
	@ManyToOne(fetch=FetchType.EAGER,optional=false)
	@JoinColumn(name="user_id",nullable=false)
	@OnDelete(action=OnDeleteAction.CASCADE)
	User user;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Long getIssueId() {
		return issueId;
	}
	public void setIssueId(Long issueId) {
		this.issueId = issueId;
	}
	public Long getPriority() {
		return priority;
	}
	public void setPriority(Long priority) {
		this.priority = priority;
	}
	public Date getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}
	public String getStatus() {
		return status;
	}
	public Date getUpdatedOn() {
		return updatedOn;
	}
	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	
}
