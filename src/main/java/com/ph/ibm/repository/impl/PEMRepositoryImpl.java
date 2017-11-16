package com.ph.ibm.repository.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.ph.ibm.model.PEM;
import com.ph.ibm.repository.PEMRepository;
import com.ph.ibm.resources.ConnectionPool;
import com.ph.ibm.util.OpumConstants;
import com.ph.ibm.util.ValidationUtils;

public class PEMRepositoryImpl implements PEMRepository {
	
    private ConnectionPool connectionPool = ConnectionPool.getInstance();
	
    @Override
    public boolean addPEM( PEM pem ) throws SQLException {
        Connection connection = connectionPool.getConnection();
        PreparedStatement preparedStatement = null;
        try{
            connection.setAutoCommit( false );
            String query =
                "INSERT INTO PEM (" + "PEM_SERIAL,EMPLOYEE_SERIAL,START_DATE,END_DATE) " + "VALUES (?,?,?,?); ";
            preparedStatement = connection.prepareStatement( query );
            preparedStatement.setString( 1, pem.getPEMSerial() );
            preparedStatement.setString( 2, pem.getEmployeeSerial() );
            preparedStatement.setString( 3, ValidationUtils.dateFormat(pem.getStartDate()) );
            preparedStatement.setString( 4, ValidationUtils.dateFormat(pem.getEndDate()) );
            preparedStatement.addBatch();
            preparedStatement.executeBatch();
            connection.commit();
            System.out.println( OpumConstants.INSERTED_SUCCESS );
            preparedStatement.close();
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
    public PEM getPEM( String pemSerialNumber ) throws SQLException {
        Connection connection = connectionPool.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        PEM pem = null;
        try{
            String query = "SELECT PEM_SERIAL,EMPLOYEE_SERIAL,START_DATE,END_DATE FROM PEM WHERE PEM_SERIAL = ?";
            preparedStatement = connection.prepareStatement( query );
            preparedStatement.setString( 1, pemSerialNumber );
            resultSet = preparedStatement.executeQuery();
            while( resultSet.next() ){
            	pem = new PEM();
            	pem.setPEMSerial( resultSet.getString("PEM_SERIAL") );
            	pem.setEmployeeSerial( resultSet.getString("EMPLOYEE_SERIAL") );
            	pem.setStartDate( resultSet.getString("START_DATE") );
            	pem.setEndDate( resultSet.getString("END_DATE") );
            }
            resultSet.close();
            preparedStatement.close();
        }
        catch( SQLException e ){
            e.printStackTrace();
        }
        finally{
            connectionPool.closeConnection( connection, preparedStatement, resultSet );
        }
        return pem;
    }
}
