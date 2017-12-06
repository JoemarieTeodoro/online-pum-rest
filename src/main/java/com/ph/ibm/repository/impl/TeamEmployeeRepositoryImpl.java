package com.ph.ibm.repository.impl;

import static com.ph.ibm.util.ValidationUtils.dateFormat;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;

import com.ph.ibm.model.TeamEmployee;
import com.ph.ibm.repository.TeamEmployeeRepository;
import com.ph.ibm.repository.TeamRepository;
import com.ph.ibm.resources.ConnectionPool;

public class TeamEmployeeRepositoryImpl implements TeamEmployeeRepository {
	private ConnectionPool connectionPool = ConnectionPool.getInstance();

    private Logger logger = Logger.getLogger( TeamRepositoryImpl.class );

    TeamRepository teamRepository = new TeamRepositoryImpl();

	@Override
	public boolean addTeamEmployee(List<TeamEmployee> teamEmpList) throws SQLException {
		Connection connection = connectionPool.getConnection();
		PreparedStatement preparedStatement = null;
        int teamId;
		connection.setAutoCommit(false);
        String query =
            "INSERT INTO EMPLOYEE_TEAM (" + "EMPLOYEE_ID,TEAM_ID,ROLL_IN_DATE,ROLL_OFF_DATE) " + "VALUES (?,?,?,?); ";
		preparedStatement = connection.prepareStatement(query);

		for (TeamEmployee teamEmp : teamEmpList) {
            teamId = teamExists( teamEmp.getTeamName() );
			preparedStatement.setString(1, teamEmp.getEmployeeId());
            preparedStatement.setInt( 2, teamId );
            preparedStatement.setString( 3, dateFormat( teamEmp.getRollInDate() ) );
            preparedStatement.setString( 4, dateFormat( teamEmp.getRollOffDate() ) );
			preparedStatement.addBatch();
		}
		preparedStatement.executeBatch();
		connection.commit();
		preparedStatement.close();

		return false;
	}

	@Override
    public int teamExists( String teamName ) {
        Connection connection = connectionPool.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int teamID = 0;
        try{
            String query = "SELECT TEAM_ID FROM TEAM WHERE NAME = ?";
            preparedStatement = connection.prepareStatement( query );
            preparedStatement.setString( 1, teamName );
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                return teamID = resultSet.getInt( 1 );
            }
            resultSet.close();
            preparedStatement.close();
        }
        catch( SQLException e ){
            logger.error( e.getStackTrace() );
        }
        return teamID;
	}

	@Override
	public boolean empExists(String empId) {
		return false;
	}

	@Override
	public boolean updateTeamEmployee(List<TeamEmployee> teamEmpList) {
		return false;
	}
}
