
package com.ph.ibm.repository.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ph.ibm.repository.UtilizationRepository;
import com.ph.ibm.resources.ConnectionPool;
import com.ph.ibm.util.SqlQueries;

/**
 * @author <a HREF="mailto:dacanam@ph.ibm.com">Marjay Dacanay</a>
 * @author <a HREF="mailto:balocaj@ph.ibm.com">Jerven Balocating</a>
 */
public class UtilizationRepositoryImpl implements UtilizationRepository {

    private ConnectionPool connectionPool = ConnectionPool.getInstance();

    /**
     * @param utilization
     * @return
     * @throws SQLException
     * @see com.ph.ibm.repository.UtilizationRepository#getUtilizationHours(com.ph.ibm.model.Utilization)
     */
    @Override
    public List<Double> getQuarterlyUtilizationHours( String serial, String year ) throws SQLException {
        Connection connection = connectionPool.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String year_id = retrieveYearID( year );
        List<Double> lstQuarterlyUtilization = new ArrayList<Double>();
        try{
            preparedStatement = connection.prepareStatement( SqlQueries.SQL_GET_QUARTERLY_UTILIZATION_HOURS );
            preparedStatement.setString( 1, serial );
            preparedStatement.setString( 2, year_id );
            resultSet = preparedStatement.executeQuery();
            while( resultSet.next() ){
                lstQuarterlyUtilization.add( resultSet.getDouble( "HOURS" ) );
            }
        }
        catch( SQLException e ){
            e.printStackTrace();
        }
        finally{
            connectionPool.closeConnection( connection, preparedStatement, resultSet );
        }
        return lstQuarterlyUtilization;
    }

    @Override
    public String retrieveYearID( String year ) throws SQLException {
        Connection connection = connectionPool.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String year_id = null;
        try{
            preparedStatement = connection.prepareStatement( SqlQueries.SQL_RETRIEVE_YEAR_DATE );
            preparedStatement.setString( 1, year );
            resultSet = preparedStatement.executeQuery();

            if( resultSet.next() ){
                year_id = resultSet.getString( "YEAR_ID" );
            }
        }
        catch( SQLException e ){
            return null;
        }
        finally{
            connectionPool.closeConnection( connection, preparedStatement, resultSet );
        }
        return year_id;
    }

    @Override
    public boolean doesYearExists( String year ) throws SQLException {
        Connection connection = connectionPool.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try{
            preparedStatement = connection.prepareStatement( SqlQueries.SQL_RETRIEVE_YEAR_DATE );
            preparedStatement.setString( 1, year );
            resultSet = preparedStatement.executeQuery();

            if( resultSet.next() ){
                return true;
            }
            else{
                return false;
            }
        }
        catch( SQLException e ){
            return false;
        }
        finally{
            connectionPool.closeConnection( connection, preparedStatement, resultSet );
        }
    }

}
