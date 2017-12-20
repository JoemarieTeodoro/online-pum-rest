package com.ph.ibm.repository.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.ph.ibm.model.EmployeeLeave;
import com.ph.ibm.model.ForApproval;
import com.ph.ibm.repository.EmployeeLeaveRepository;
import com.ph.ibm.resources.ConnectionPool;
import com.ph.ibm.util.OpumConstants;

public class EmployeeLeaveRepistoryImpl implements EmployeeLeaveRepository{
	
    private Logger logger = Logger.getLogger( EmployeeLeaveRepistoryImpl.class );
    
    private ConnectionPool connectionPool = ConnectionPool.getInstance();
    
    @Override
    public EmployeeLeave getEmployeeLeave( ForApproval approval )
    {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Connection connection = connectionPool.getConnection();
        try{
            preparedStatement = createGetQuery(approval, connection);
            logger.debug("Get Employee Leave Statement: " + preparedStatement.toString());
            resultSet = preparedStatement.executeQuery();
    		return contructEmployeeLeave( resultSet );
        } catch (SQLException e) {
			e.printStackTrace();
		} finally {
			connectionPool.closeConnection( connection, preparedStatement, resultSet );
		}
        
		return null;
    }

	private PreparedStatement createGetQuery(ForApproval approval, Connection connection) throws SQLException {
		PreparedStatement preparedStatement;
		String query = "SELECT * FROM employee_leave "
							+ "WHERE Employee_Leave_ID = ? "
							+ "AND "
							+ "Employee_ID = ? AND "
							+ "Status = ? "
							+ ";";
		preparedStatement = connection.prepareStatement( query );
		preparedStatement.setString( 1, String.valueOf(approval.getEmployee_Leave_Id()) );
		preparedStatement.setString( 2, approval.getEmployee_Id() );
		preparedStatement.setString( 3, "Pending" );
		return preparedStatement;
	}
	
	private EmployeeLeave contructEmployeeLeave( ResultSet resultSet )
	{
		EmployeeLeave employeeLeave = null;
        try {
			if( resultSet.next() ){
				employeeLeave = new EmployeeLeave();
			    employeeLeave.setEmployeeLeaveID( resultSet.getString(1) );
			    employeeLeave.setEmployeeID( resultSet.getString(2) );
			    employeeLeave.setYearID( resultSet.getString(3) );
			    employeeLeave.setStatus( resultSet.getString(4) );
			    employeeLeave.setDate( resultSet.getString(5) );
			    employeeLeave.setLeaveName( resultSet.getString(6) );
			    employeeLeave.setCreateDate( resultSet.getString(7) );
			    employeeLeave.setUpdateDate( resultSet.getString(8) );
	            logger.info( "employee leave created: " +  employeeLeave.toString() );
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
        
        return employeeLeave;
	}

	@Override
	public boolean updateEmployeeLeaveStatus(EmployeeLeave leave) 
	{
        Connection connection = connectionPool.getConnection();
        PreparedStatement preparedStatement = null;
        String query = "UPDATE employee_leave SET Status = ? WHERE Employee_Leave_ID = ?;";
        try{
        	logger.debug("Updating EmployeeLeave to: " + leave.toString());
            connection.setAutoCommit( false );
            preparedStatement = connection.prepareStatement( query );
            preparedStatement.setString( 1, leave.getStatus() );
            preparedStatement.setString( 2, leave.getEmployeeLeaveID() );
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

}
