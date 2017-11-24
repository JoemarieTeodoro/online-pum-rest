package com.ph.ibm.model;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Team extends BaseAuditBean{
	
	private Long teamId;
	private Long projectId;
	private String teamName;

    private String isRecoverable;
	private String teamLeadSerial;
	private List<Employee> members;
	
	public Team() {
		super();
	}

	public Team(Long teamId, Long projectId, String teamName, String teamLeadSerial, List<Employee> members) {
		super();
		this.teamId = teamId;
		this.projectId = projectId;
		this.teamName = teamName;
		this.teamLeadSerial = teamLeadSerial;
		this.members = members;
	}

	public Long getTeamId() {
		return teamId;
	}

	public void setTeamId(Long teamId) {
		this.teamId = teamId;
	}

	public Long getProjectId() {
		return projectId;
	}

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public String getTeamLeadSerial() {
		return teamLeadSerial;
	}

	public void setTeamLeadSerial(String teamLeadSerial) {
		this.teamLeadSerial = teamLeadSerial;
	}

	public List<Employee> getMembers() {
		return members;
	}

	public void setMembers(List<Employee> members) {
		this.members = members;
	}

    public String getIsRecoverable() {
        return isRecoverable;
    }

    public void setIsRecoverable( String isRecoverable ) {
        this.isRecoverable = isRecoverable;
    }

}
