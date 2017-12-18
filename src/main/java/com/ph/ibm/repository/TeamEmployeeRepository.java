package com.ph.ibm.repository;

import java.sql.SQLException;
import java.util.List;

import com.ph.ibm.model.TeamEmployee;

public interface TeamEmployeeRepository {
	
	public boolean addTeamEmployee(List<TeamEmployee> teamEmpList) throws SQLException;
    public int teamExists( String teamName );
	public boolean empExists(String empId);
	public boolean updateTeamEmployee(List<TeamEmployee> teamEmpList);

    /**
     * @param serialNumber
     * @return
     * @throws SQLException
     */
    boolean updateEmployeeTeamMapping( String serialNumber ) throws SQLException;

    /**
     * @param serialNumber
     * @return
     * @throws SQLException
     */
    public String retrieveActiveTeamAssignment( String serialNumber ) throws SQLException;
}
