package com.ph.ibm.repository;

import java.sql.BatchUpdateException;
import java.sql.SQLException;
import java.util.List;

import com.ph.ibm.model.Role;
import com.ph.ibm.model.Team;
/**
 * Interface for uploading list of teams
 * 
 * @author <a HREF="teodorj@ph.ibm.com">Joemarie Teodoro</a>
 * @author <a HREF="dacanam@ph.ibm.com">Marjay Dacanay</a>
 */

public interface TeamRepository {
    
    /** Checks in the db if a certain team exists based on its teamID**/
    boolean teamExists( String teamName );

    public boolean getTeam( Team team ) throws SQLException;
    
    public boolean addTeam( List<Team> team, Role role ) throws SQLException, BatchUpdateException;
    
    public String getRecoverableFlag(String teamId);
}

