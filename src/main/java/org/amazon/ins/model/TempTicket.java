package org.amazon.ins.model;

/**
 * 
 * @author Raasi 
 *
 */

public class TempTicket {
	
	String userName;
	Long priority;
	String issueCategory;
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public Long getPriority() {
		return priority;
	}
	public void setPriority(Long priority) {
		this.priority = priority;
	}
	public String getIssueCategory() {
		return issueCategory;
	}
	public void setIssueCategory(String issueCategory) {
		this.issueCategory = issueCategory;
	}
	

}
