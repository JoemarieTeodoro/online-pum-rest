package com.ph.ibm.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Employee extends BaseAuditBean {

    private String employeeSerial;

    private String employeeId;

    private String managerSerial;

    private String intranetId;

    private String fullName;

    private String rollInDate;

    private String rollOffDate;

    private Long teamId;

    private List<Role> assignedRoles;

    private List<Project> assignedProjects;

    private String primaryProject;

    private String password;

    private Boolean isActive;

    private Boolean isAdmin;

    private Utilization utilization;
    
    private Boolean isTeamLead;

    public Boolean getIsTeamLead() {
		return isTeamLead;
	}

	public void setIsTeamLead(Boolean isTeamLead) {
		this.isTeamLead = isTeamLead;
	}

	public Employee() {
        super();
    }

    public Employee( String employeeSerial, String managerSerial, String intranetId, String fullName, Long teamId,
                     List<Role> assignedRoles, List<Project> assignedProjects, String password, Boolean isActive,
                     Utilization utilization ) {
        super();
        this.employeeSerial = employeeSerial;
        this.managerSerial = managerSerial;
        this.intranetId = intranetId;
        this.fullName = fullName;
        this.teamId = teamId;
        this.assignedRoles = assignedRoles;
        this.assignedProjects = assignedProjects;
        this.password = password;
        this.isActive = isActive;
        this.utilization = utilization;
    }

    public Employee( String employeeSerial, String intranetId, String primaryProject, String password ) {
        this.employeeSerial = employeeSerial;
        this.intranetId = intranetId;
        this.primaryProject = primaryProject;
        this.password = password;
    }

    public String getEmployeeSerial() {
        return employeeSerial;
    }

    public void setEmployeeSerial( String employeeSerial ) {
        this.employeeSerial = employeeSerial;
    }

    public String getManagerSerial() {
        return managerSerial;
    }

    public void setManagerSerial( String managerSerial ) {
        this.managerSerial = managerSerial;
    }

    public String getIntranetId() {
        return intranetId;
    }

    public void setIntranetId( String intranetId ) {
        this.intranetId = intranetId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName( String fullName ) {
        this.fullName = fullName;
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId( Long teamId ) {
        this.teamId = teamId;
    }

    public List<Role> getAssignedRoles() {
        if( assignedRoles == null ){
            return new ArrayList<Role>();
        }
        return assignedRoles;
    }

    public void setAssignedRoles( List<Role> assignedRoles ) {
        this.assignedRoles = assignedRoles;
    }

    public List<Project> getAssignedProjects() {
        return assignedProjects;
    }

    public void setAssignedProjects( List<Project> assignedProjects ) {
        this.assignedProjects = assignedProjects;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword( String password ) {
        this.password = password;
    }

    public Utilization getUtilization() {
        return utilization;
    }

    public void setUtilization( Utilization utilization ) {
        this.utilization = utilization;
    }

    public String getPrimaryProject() {
        return primaryProject;
    }

    public void setPrimaryProject( String primaryProject ) {
        this.primaryProject = primaryProject;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive( Boolean isActive ) {
        this.isActive = isActive;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId( String employeeId ) {
        this.employeeId = employeeId;
    }

    /**
     * @return the rollInDate
     */
    public String getRollInDate() {
        return rollInDate;
    }

    /**
     * @param rollInDate the rollInDate to set
     */
    public void setRollInDate( String rollInDate ) {
        this.rollInDate = rollInDate;
    }

    /**
     * @return the rollOffDate
     */
    public String getRollOffDate() {
        return rollOffDate;
    }

    /**
     * @param rollOffDate the rollOffDate to set
     */
    public void setRollOffDate( String rollOffDate ) {
        this.rollOffDate = rollOffDate;
    }
    
    @Override
    public String toString(){
    	return String.format( "Employee!\n\nSerial No: %s \nFull Name: %s \nIntranet Id: %s \nRoll In Date: %s \nRoll Off date: %s",
    			this.getEmployeeSerial(), this.getFullName(), this.getIntranetId(), this.getRollInDate(), this.getRollOffDate());
    }

}