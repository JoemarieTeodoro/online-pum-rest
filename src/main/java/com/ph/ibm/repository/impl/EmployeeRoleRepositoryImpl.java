package com.ph.ibm.repository.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;

import com.ph.ibm.model.EmployeeRole;
import com.ph.ibm.repository.EmployeeRoleRepository;
import com.ph.ibm.resources.ConnectionPool;
import com.ph.ibm.util.OpumConstants;

public class EmployeeRoleRepositoryImpl implements EmployeeRoleRepository {

	private Logger logger = Logger.getLogger( EmployeeRoleRepositoryImpl.class );
	private static EmployeeRoleRepositoryImpl employeeRoleRepositoryImpl;
	private ConnectionPool connectionPool = ConnectionPool.getInstance();
	private EmployeeRoleRepositoryImpl() { }

	public static EmployeeRoleRepositoryImpl getInstance() {
		if (employeeRoleRepositoryImpl == null) {
			employeeRoleRepositoryImpl = new EmployeeRoleRepositoryImpl();
		}
		return employeeRoleRepositoryImpl;
	}

	@Override
	public boolean isEmployeeRoleExists(EmployeeRole employeeRole) {
		Connection connection = connectionPool.getConnection();
		PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        boolean isEmployeeRoleExists = false;
        try {
            String query = "SELECT * FROM EMPLOYEE_ROLE WHERE EMPLOYEE_ID = ? AND ROLE_ID = ?";
            preparedStatement = connection.prepareStatement( query );
            preparedStatement.setString( 1, employeeRole.getEmployeeSerial() );
            preparedStatement.setInt( 2,  employeeRole.getEmployeeRoleEnum().getRoleId() );
            resultSet = preparedStatement.executeQuery();
            isEmployeeRoleExists =  resultSet.next();
            resultSet.close();
            preparedStatement.close();
        }
        catch ( SQLException e ) {
            e.printStackTrace();
        }
        finally {
            connectionPool.closeConnection( connection, preparedStatement, resultSet );
        }
		return isEmployeeRoleExists;
	}

	@Override
	public boolean saveEmployeeRoles(List<EmployeeRole> employeeRoles) throws Exception {
		Connection connection = connectionPool.getConnection();
        PreparedStatement preparedStatement = null;
        connection.setAutoCommit( false );
        String query = "INSERT INTO EMPLOYEE_ROLE (EMPLOYEE_ID, ROLE_ID) VALUES (?, ?)";
        preparedStatement = connection.prepareStatement( query );

        for( EmployeeRole employeeRole : employeeRoles ){
            preparedStatement.setString( 1, employeeRole.getEmployeeSerial() );
            preparedStatement.setInt( 2, employeeRole.getEmployeeRoleEnum().getRoleId() );
            preparedStatement.addBatch();
        }

        preparedStatement.executeBatch();
        connection.commit();
        preparedStatement.close();
        logger.info( OpumConstants.SUCCESSFULLY_SAVED_DATA );

		return true;
	}
}
