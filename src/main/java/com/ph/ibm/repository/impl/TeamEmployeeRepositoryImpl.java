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
import com.ph.ibm.util.OpumConstants;

public class TeamEmployeeRepositoryImpl implements TeamEmployeeRepository {
    public static final int INVALID_VALUE = -1;

	public static final String GET_TEAM_USING_EMPID = "SELECT TEAM_ID FROM EMPLOYEE_TEAM WHERE EMPLOYEE_ID = ?";

	private ConnectionPool connectionPool = ConnectionPool.getInstance();

    private Logger logger = Logger.getLogger( TeamRepositoryImpl.class );

    TeamRepository teamRepository = new TeamRepositoryImpl();

    @Override
    public boolean addTeamEmployee( List<TeamEmployee> teamEmpList ) throws SQLException {
        Connection connection = connectionPool.getConnection();
        PreparedStatement preparedStatement = null;
        int teamId;
        String isActive = "A";
        connection.setAutoCommit( false );
        String query = "INSERT INTO EMPLOYEE_TEAM (" +
            "EMPLOYEE_ID,TEAM_ID,ROLL_IN_DATE,ROLL_OFF_DATE,EMP_TEAM_STATUS) " + "VALUES (?,?,?,?,?); ";
        preparedStatement = connection.prepareStatement( query );

        for( TeamEmployee teamEmp : teamEmpList ){
            teamId = teamExists( teamEmp.getTeamName() );
            preparedStatement.setString( 1, teamEmp.getEmployeeId() );
            preparedStatement.setInt( 2, teamId );
            preparedStatement.setString( 3, dateFormat( teamEmp.getRollInDate() ) );
            preparedStatement.setString( 4, dateFormat( teamEmp.getRollOffDate() ) );
            preparedStatement.setString( 5, isActive );
            preparedStatement.addBatch();
            updateEmployeeTeamMapping( teamEmp.getEmployeeId() );
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
            while( resultSet.next() ){
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
    public boolean empExists( String empId ) {
    	Connection connection = connectionPool.getConnection();
        PreparedStatement preparedStatement = null;
        String teamName = null;
        ResultSet resultSet;
        try{
            connection.setAutoCommit( false );
            String query = "SELECT TEAM_ID FROM EMPLOYEE_TEAM WHERE EMPLOYEE_ID = ?";
            preparedStatement = connection.prepareStatement( query );
            preparedStatement.setString( 1, empId );
            resultSet = preparedStatement.executeQuery();
            while( resultSet.next() ){
                teamName = resultSet.getString( 1 );
            }
                return teamName != null;            
        }
        catch( SQLException e ){
            logger.error( e.getStackTrace() );
        }

        finally{
            connectionPool.closeConnection( connection, preparedStatement );
        }
        return false;
    }

    @Override
    public boolean updateTeamEmployee( List<TeamEmployee> teamEmpList ) {
        return false;
    }

    @Override
    public boolean updateEmployeeTeamMapping( String serialNumber ) throws SQLException {
        Connection connection = connectionPool.getConnection();
        PreparedStatement preparedStatement = null;
        try{
            connection.setAutoCommit( false );
            String query = "UPDATE EMPLOYEE_TEAM SET EMP_TEAM_STATUS = 'I' WHERE EMPLOYEE_ID = ?;";
            preparedStatement = connection.prepareStatement( query );
            preparedStatement.setString( 1, serialNumber );
            preparedStatement.executeUpdate();
            connection.commit();
            logger.info( OpumConstants.UPDATED_SUCCESS );
            return true;
        }
        catch( SQLException e ){
            logger.error( e.getStackTrace() );
        }
        finally{
            connectionPool.closeConnection( connection, preparedStatement );
        }
        return false;
    }

    @Override
    public String retrieveActiveTeamAssignment( String serialNumber ) throws SQLException {
        Connection connection = connectionPool.getConnection();
        PreparedStatement preparedStatement = null;
        String teamName = null;
        ResultSet resultSet;
        try{
            connection.setAutoCommit( false );
            String query = "SELECT NAME FROM EMPLOYEE_TEAM_V WHERE EMPLOYEE_ID = ?";
            preparedStatement = connection.prepareStatement( query );
            preparedStatement.setString( 1, serialNumber );
            resultSet = preparedStatement.executeQuery();
            while( resultSet.next() ){
                teamName = resultSet.getString( 1 );
            }
        }
        catch( SQLException e ){
            logger.error( e.getStackTrace() );
        }

        finally{
            connectionPool.closeConnection( connection, preparedStatement );
        }
        return teamName;
    }

	@Override
	public int getTeamID(String empId) {
		Connection connection = connectionPool.getConnection();
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			String query = GET_TEAM_USING_EMPID;
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, empId);
			resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				return resultSet.getInt(1);
			}
		} catch (SQLException e) {
			logger.error(e.getStackTrace());
		} finally {
			connectionPool.closeConnection(connection, preparedStatement, resultSet);
		}
		return INVALID_VALUE;
	}
}
