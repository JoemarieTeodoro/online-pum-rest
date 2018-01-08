
package com.ph.ibm.repository.impl;

import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;

import com.ph.ibm.model.Role;
import com.ph.ibm.model.Team;
import com.ph.ibm.repository.TeamRepository;
import com.ph.ibm.resources.ConnectionPool;
import com.ph.ibm.util.OpumConstants;
/**
 * Class implementation for uploading list of teams
 * 
 * @author <a HREF="teodorj@ph.ibm.com">Joemarie Teodoro</a>
 * @author <a HREF="dacanam@ph.ibm.com">Marjay Dacanay</a>
 */

public class TeamRepositoryImpl implements TeamRepository {

    private static final String GET_RECOVERABLE_FLAG_BASE_ON_TEAM_ID = "SELECT ISRECOVERABLE FROM TEAM WHERE TEAM_ID = ?";
	public static final String INVALID_RECOVERABLE_VALUE = "";
	private Logger logger = Logger.getLogger( TeamRepositoryImpl.class );
    private ConnectionPool connectionPool = ConnectionPool.getInstance();

    /** 
     * @param employeeIdNumber
     * @return
     * @throws SQLException
     * @see com.ph.ibm.repository.TeamRepository#getTeam(java.lang.String)
     */
    @Override
    public boolean getTeam( Team team ) throws SQLException {
        Connection connection = connectionPool.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String teamName = null;
        try{
            String query = "SELECT NAME FROM TEAM WHERE NAME = ?";
            preparedStatement = connection.prepareStatement( query );
            preparedStatement.setString( 1, team.getTeamName() );
            resultSet = preparedStatement.executeQuery();
            if( resultSet.next() ){
                return true;
            }
            else{
                return false;
            }

        }
        catch( SQLException e ){
            e.printStackTrace();
            return false;
        }
        finally{
            preparedStatement.close();
            connectionPool.closeConnection( connection, preparedStatement, resultSet );
        }

    }

    /** 
     * @param team
     * @return
     * @throws SQLException
     * @throws BatchUpdateException
     * @see com.ph.ibm.repository.TeamRepository#addTeam(com.ph.ibm.model.Team)
     */
    @Override
    public boolean addTeam( List<Team> teamList, Role role ) throws SQLException, BatchUpdateException {
        Connection connection = connectionPool.getConnection();
        PreparedStatement preparedStatement = null;
        try{
            connection.setAutoCommit( false );
            String query =
                "INSERT INTO TEAM (" +
                    "PROJECT_ID, TEAM_LEAD_EMPLOYEE_ID, NAME, ISRECOVERABLE, CREATEDATE, CREATEDBY, UPDATEDATE, UPDATEDBY) " +
                    "VALUES (?,?,?,?,?,?,?,?); ";
            preparedStatement = connection.prepareStatement( query );

            for( Team team : teamList ){
                preparedStatement.setInt( 1, 1 );
                preparedStatement.setString( 2, team.getTeamLeadSerial() );
                preparedStatement.setString( 3, team.getTeamName() );
                preparedStatement.setString( 4, team.getIsRecoverable() );
                preparedStatement.setString( 5, team.getCreateDate() );
                preparedStatement.setString( 6, role.getRoleValue() );
                preparedStatement.setString( 7, team.getUpdateDate() );
                preparedStatement.setString( 8, team.getUpdatedBy() );
                preparedStatement.addBatch();
            }

            preparedStatement.executeBatch();
            connection.commit();
            preparedStatement.close();
            logger.info( OpumConstants.INSERTED_SUCCESS );
        }
        catch( SQLException e ){
            e.printStackTrace();
        }
        finally{
            try{
                if( preparedStatement != null )
                    preparedStatement.close();
            }
            catch( Exception e ){
            }
            try{
                if( connection != null )
                    connection.close();
            }
            catch( Exception e ){
            }
        }
        return true;
    }

    @Override
    public boolean teamExists( String teamName ) {
        boolean exists = false;

        try{
            Connection connection = connectionPool.getConnection();
            PreparedStatement preparedStatement = null;
            ResultSet resultSet = null;
            String query = "SELECT TEAM_ID FROM TEAM WHERE NAME = ?";
            preparedStatement = connection.prepareStatement( query );
            preparedStatement.setString( 1, teamName );
            resultSet = preparedStatement.executeQuery();
            exists = resultSet.next();
            resultSet.close();
            preparedStatement.close();
        }
        catch( SQLException e ){
            logger.error( e.getStackTrace() );
        }
        return exists;
    }

	@Override
	public String getRecoverableFlag(String teamId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try{
            connection = connectionPool.getConnection();
            String query = GET_RECOVERABLE_FLAG_BASE_ON_TEAM_ID;
            preparedStatement = connection.prepareStatement( query );
            preparedStatement.setString( 1, teamId );
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
            	return resultSet.getString(1);
            }
            resultSet.close();
            preparedStatement.close();
        }
        catch( SQLException e ){
            logger.error( e.getStackTrace() );
        } finally {
        	connectionPool.closeConnection( connection, preparedStatement, resultSet );
        }
        return INVALID_RECOVERABLE_VALUE;
	}
}
