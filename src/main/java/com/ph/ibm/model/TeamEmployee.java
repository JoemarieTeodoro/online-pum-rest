package com.ph.ibm.model;

import java.io.Serializable;

/**
 * Class mapper for team_employee table in opum database
 * 
 * @author P100YF
 *
 */
public class TeamEmployee implements Serializable {

	private static final long serialVersionUID = 1L;
	private int teamId;
	private String employeeId;
    private String teamName;
    private String rollInDate;
    private String rollOffDate;

	public int getTeamId() {
		return teamId;
	}

	public void setTeamId(int teamId) {
		this.teamId = teamId;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

    public String getRollInDate() {
        return rollInDate;
    }

    public void setRollInDate( String rollInDate ) {
        this.rollInDate = rollInDate;
    }

    public String getRollOffDate() {
        return rollOffDate;
    }

    public void setRollOffDate( String rollOffDate ) {
        this.rollOffDate = rollOffDate;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName( String teamName ) {
        this.teamName = teamName;
    }

}
