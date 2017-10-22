package com.ph.ibm.model;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Project extends BaseAuditBean{
	
	private Long projectId;
	private String projectName;
	private Long accountId;
	private List<Team> teams;
	
	public Project() {
		super();
	}

	public Project(Long projectId, String projectName, String createDate, String createdBy) {
		super();
		this.projectId = projectId;
		this.projectName = projectName;
		this.setCreateDate(createDate);
		this.setCreatedBy(createdBy);
	}
	
	public Project(Long projectId, String projectName, Long accountId, List<Team> teams) {
		super();
		this.projectId = projectId;
		this.projectName = projectName;
		this.accountId = accountId;
		this.teams = teams;
	}

	public Long getProjectId() {
		return projectId;
	}

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public List<Team> getTeams() {
		return teams;
	}

	public void setTeams(List<Team> teams) {
		this.teams = teams;
	}

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}
}
