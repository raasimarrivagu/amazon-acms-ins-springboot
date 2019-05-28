package org.amazon.ins.model;

import javax.persistence.Id;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 
 * @authors Sravanthi, Raasi
 *
 */

@Document(collection="helpdocument")
public class HelpDocument {
	
	@Id
	String issueId;
	String issueCategory,helpText;
	
	
	public String getIssueId() {
		return issueId;
	}
	public void setIssueId(String issueId) {
		this.issueId = issueId;
	}
	public String getIssueCategory() {
		return issueCategory;
	}
	public void setIssueCategory(String issueCategory) {
		this.issueCategory = issueCategory;
	}
	public String getHelpText() {
		return helpText;
	}
	public void setHelpText(String helpText) {
		this.helpText = helpText;
	}
}
