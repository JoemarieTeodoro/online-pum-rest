
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

    private static final int SQL_GET_QUARTERLY_UTILIZATION_HOURS_SERIAL_NUMBER = 1;
	private static final int SQL_GET_QUARTERLY_UTILIZATION_HOURS_YEAR_ID = 2;
	private static final int SQL_RETRIEVE_YEAR_DATE_YEAR_ID = 1;
	private static final int SQL_GET_WEEKLY_UTILIZATION_HOURS_YEAR_ID = 2;
	private static final int SQL_GET_WEEKLY_UTILIZATION_HOURS_SERIAL_NUMBER = 1;
	private static final int SQL_UPDATE_UTILIZATION_HOURS_SERIAL_NUMBER = 54;
	private static final int SQL_UPDATE_UTILIZATION_HOURS_YEAR_ID = 53;
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
            preparedStatement.setString( SQL_GET_QUARTERLY_UTILIZATION_HOURS_SERIAL_NUMBER, serial );
            preparedStatement.setString( SQL_GET_QUARTERLY_UTILIZATION_HOURS_YEAR_ID, year_id );
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
			preparedStatement.setString(SQL_RETRIEVE_YEAR_DATE_YEAR_ID, year);
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
            preparedStatement.setString( SQL_RETRIEVE_YEAR_DATE_YEAR_ID, year );
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

    @Override
    public boolean updateUtilizationHours( String serial, String yearId, List<Double> lstWeeklyHours ) throws SQLException {
        Connection connection = connectionPool.getConnection();
        PreparedStatement preparedStatement = null;
        try{
            
            preparedStatement = connection.prepareStatement( SqlQueries.SQL_UPDATE_UTILIZATION_HOURS );
            for( int weekNumber = 1; weekNumber <= lstWeeklyHours.size(); weekNumber++ ){
                preparedStatement.setDouble( weekNumber, lstWeeklyHours.get( weekNumber - 1 ) );
            }
            preparedStatement.setString( SQL_UPDATE_UTILIZATION_HOURS_YEAR_ID, yearId );
            preparedStatement.setString( SQL_UPDATE_UTILIZATION_HOURS_SERIAL_NUMBER, serial );
            preparedStatement.executeUpdate();
            connection.commit();
        }
        catch( SQLException e ){
            return false;
        }
        finally{
            connectionPool.closeConnection( connection, preparedStatement );
        }
        return true;
    }

    @Override
    public List<Double> getEmployeeWeeklyHours( String serial, String year ) throws SQLException {
        Connection connection = connectionPool.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Double> lstWeeklyHours = new ArrayList<Double>();
        try{
            preparedStatement = connection.prepareStatement( SqlQueries.SQL_GET_WEEKLY_UTILIZATION_HOURS );
            preparedStatement.setString( SQL_GET_WEEKLY_UTILIZATION_HOURS_SERIAL_NUMBER, serial );
            preparedStatement.setString( SQL_GET_WEEKLY_UTILIZATION_HOURS_YEAR_ID, year );
            resultSet = preparedStatement.executeQuery();
            while( resultSet.next() ){
                lstWeeklyHours.add( resultSet.getDouble( "HOURS" ) );
            }
        }
        catch( SQLException e ){
            e.printStackTrace();
        }
        finally{
            connectionPool.closeConnection( connection, preparedStatement, resultSet );
        }
        return lstWeeklyHours;
    }

}
