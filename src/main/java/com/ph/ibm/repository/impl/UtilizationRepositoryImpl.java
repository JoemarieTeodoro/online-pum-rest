
package com.ph.ibm.repository.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.ph.ibm.repository.UtilizationRepository;
import com.ph.ibm.resources.ConnectionPool;
import com.ph.ibm.util.OpumConstants;
import com.ph.ibm.util.SqlQueries;
import com.ph.ibm.util.ValidationUtils;

/**
 * @author <a HREF="mailto:dacanam@ph.ibm.com">Marjay Dacanay</a>
 * @author <a HREF="mailto:balocaj@ph.ibm.com">Jerven Balocating</a>
 */
public class UtilizationRepositoryImpl implements UtilizationRepository {

    /**
     * 
     */
    private static final double ZERO_HOURS = 0.0;

    private static final int SQL_UPDATE_UTILIZATION_HOURS_UTILIZATION_TYPE = 55;

    private static final int SQL_GET_EMPLOYEE_UTILIZATION_TYPE = 3;

    private static final int SQL_WHERE_UTILIZATION_TYPE = 3;

    private static final int SQL_GET_QUARTERLY_UTILIZATION_HOURS_SERIAL_NUMBER = 1;

    private static final int SQL_GET_QUARTERLY_UTILIZATION_HOURS_YEAR_ID = 2;

    private static final int SQL_RETRIEVE_YEAR_DATE_YEAR_ID = 1;

    private static final int SQL_GET_WEEKLY_UTILIZATION_HOURS_YEAR_ID = 2;

    private static final int SQL_GET_WEEKLY_UTILIZATION_HOURS_SERIAL_NUMBER = 1;

    private static final int SQL_UPDATE_UTILIZATION_HOURS_SERIAL_NUMBER = 54;

    private static final int SQL_UPDATE_UTILIZATION_HOURS_YEAR_ID = 53;

    private static final int SQL_WHERE_ACTUAL_UTILIZATION = 55;

    private static final int SQL_INSERT_ACTUAL_UTILIZATION_YEAR_ID = 3;

    private static final int SQL_INSERT_ACTUAL_UTILIZATION_UTILIZATION_TYPE = 2;

    private static final int SQL_UPDATE_ACTUAL_UTILIZATION_YEAR_ID = 53;

    private static final int SQL_UPDATE_ACTUAL_UTILIZATION_SERIAL = 54;

    private static final int SQL_UPDATE_ACTUAL_UTILIZATION = 55;

    private int idx_max_col_utilization_table = 55;

    private int idx_col_week_utilization_table = 4;

    private int idx_update_week = 1;

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
            preparedStatement.setString( SQL_RETRIEVE_YEAR_DATE_YEAR_ID, year );
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
    public boolean updateUtilizationHours( String serial, String yearId, List<Double> lstWeeklyHours,
                                           String utilizationType )
        throws SQLException {
        Connection connection = connectionPool.getConnection();
        PreparedStatement preparedStatement = null;
        try{

            preparedStatement = connection.prepareStatement( SqlQueries.SQL_UPDATE_UTILIZATION_HOURS );
            for( int weekNumber = 1; weekNumber <= lstWeeklyHours.size(); weekNumber++ ){
                preparedStatement.setDouble( weekNumber, lstWeeklyHours.get( weekNumber - 1 ) );
            }
            preparedStatement.setString( SQL_UPDATE_UTILIZATION_HOURS_YEAR_ID, yearId );
            preparedStatement.setString( SQL_UPDATE_UTILIZATION_HOURS_SERIAL_NUMBER, serial );
            preparedStatement.setString( SQL_UPDATE_UTILIZATION_HOURS_UTILIZATION_TYPE, utilizationType );
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

    public void populateActualUtilization( int yearId, Map<String, Map<String, Double>> employeeHoursMap )
        throws SQLException {
        for( Map.Entry<String, Map<String, Double>> employeeSerial : employeeHoursMap.entrySet() ){
            if( ValidationUtils.isValueEmpty( employeeSerial.getKey() ) ){
                break;
            }

            List<Double> lstWeeklyHours = getEmployeeWeeklyHours( employeeSerial.getValue() );

            if( isEmployeeInUtilization( employeeSerial.getKey(), yearId, OpumConstants.ACTUAL_UTILIZATION ) ){
                updateEmployeeActualUtilizationHours( employeeSerial.getKey(), yearId, lstWeeklyHours );
            }
            else{
                insertEmployeeActualUtilization( employeeSerial.getKey(), yearId, lstWeeklyHours );
            }
        }
    }

    @Override
    public boolean isEmployeeInUtilization( String serial, int yearId, String utilizationType ) throws SQLException {
        Connection connection = connectionPool.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try{
            preparedStatement = connection.prepareStatement( SqlQueries.SQL_GET_EMPLOYEE_UTILIZATION );
            preparedStatement.setString( SQL_GET_WEEKLY_UTILIZATION_HOURS_SERIAL_NUMBER, serial );
            preparedStatement.setInt( SQL_GET_WEEKLY_UTILIZATION_HOURS_YEAR_ID, yearId );
            preparedStatement.setString( SQL_WHERE_UTILIZATION_TYPE, utilizationType );
            resultSet = preparedStatement.executeQuery();
            while( resultSet.next() ){
                return true;
            }
        }
        catch( SQLException e ){
            e.printStackTrace();
        }
        finally{
            connectionPool.closeConnection( connection, preparedStatement, resultSet );
        }

        return false;
    }

    /**
     * @param serial
     * @param year
     * @return
     * @throws SQLException
     * @see com.ph.ibm.repository.UtilizationRepository#getActualUtilization(java.lang.String, java.lang.String)
     */
    @Override
    public List<Double> getEmployeeUtilization( String serial, String year, String utilizationType )
        throws SQLException {
        Connection connection = connectionPool.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String year_id = retrieveYearID( year );
        int idxWeekCol = 1;
        List<Double> utilizationHours = new ArrayList<Double>();
        try{
            preparedStatement = connection.prepareStatement( SqlQueries.SQL_GET_EMPLOYEE_UTILIZATION );
            preparedStatement.setString( SQL_GET_QUARTERLY_UTILIZATION_HOURS_SERIAL_NUMBER, serial );
            preparedStatement.setString( SQL_GET_QUARTERLY_UTILIZATION_HOURS_YEAR_ID, year_id );
            preparedStatement.setString( SQL_GET_EMPLOYEE_UTILIZATION_TYPE, utilizationType );
            resultSet = preparedStatement.executeQuery();
            if( resultSet.next() ){
                    for( idxWeekCol = 1; idxWeekCol <= OpumConstants.TOTAL_NUMBER_OF_WEEKS; idxWeekCol++ ){
                        utilizationHours.add( resultSet.getDouble( "week" + idxWeekCol ) );
                    }
            }
            else{
                    for( idxWeekCol = 1; idxWeekCol <= OpumConstants.TOTAL_NUMBER_OF_WEEKS; idxWeekCol++ ){
                        utilizationHours.add( ZERO_HOURS );
                }
            }
        }
        catch( SQLException e ){
            e.printStackTrace();
        }
        finally{
            connectionPool.closeConnection( connection, preparedStatement, resultSet );
        }
        return utilizationHours;
    }

    private void updateEmployeeActualUtilizationHours( String serial, int yearId, List<Double> lstWeeklyHours ) {
        Connection connection = connectionPool.getConnection();
        PreparedStatement preparedStatement = null;
        try{
            connection.setAutoCommit( false );
            preparedStatement = connection.prepareStatement( SqlQueries.SQL_UPDATE_ACTUAL_UTILIZATION_HOURS );
            preparedStatement = setWeeklyHours( idx_update_week, lstWeeklyHours, preparedStatement );
            preparedStatement.setString( SQL_UPDATE_ACTUAL_UTILIZATION_SERIAL, serial );
            preparedStatement.setInt( SQL_UPDATE_ACTUAL_UTILIZATION_YEAR_ID, yearId );
            preparedStatement.setString( SQL_UPDATE_ACTUAL_UTILIZATION, OpumConstants.ACTUAL_UTILIZATION );

            preparedStatement.executeUpdate();
            connection.commit();
        }
        catch( SQLException e ){
            e.printStackTrace();
        }
        finally{
            connectionPool.closeConnection( connection, preparedStatement );
        }
    }

    private void insertEmployeeActualUtilization( String serial, int yearId, List<Double> lstWeeklyHours )
        throws SQLException {
        Connection connection = connectionPool.getConnection();
        PreparedStatement preparedStatement = null;
        try{
            connection.setAutoCommit( false );
            preparedStatement = connection.prepareStatement( SqlQueries.SQL_INSERT_ACTUAL_UTILIZATION_HOURS );
            preparedStatement = setWeeklyHours( idx_col_week_utilization_table, lstWeeklyHours, preparedStatement );
            preparedStatement.setString( SQL_GET_WEEKLY_UTILIZATION_HOURS_SERIAL_NUMBER, serial );
            preparedStatement.setString( SQL_INSERT_ACTUAL_UTILIZATION_UTILIZATION_TYPE,
                OpumConstants.ACTUAL_UTILIZATION );
            preparedStatement.setInt( SQL_INSERT_ACTUAL_UTILIZATION_YEAR_ID, yearId );
            preparedStatement.executeUpdate();
            connection.commit();
        }
        catch( SQLException e ){
            e.printStackTrace();
        }
        finally{
            connectionPool.closeConnection( connection, preparedStatement );
        }
    }

    private PreparedStatement setWeeklyHours( int index, List<Double> lstWeeklyHours, PreparedStatement ps ) {
        PreparedStatement preparedStatement = ps;
        try{
            // Fill hours based on hours from ILC Extract Excel File
            for( Double hours : lstWeeklyHours ){
                preparedStatement.setDouble( index, hours );
                index++;
            }

            // Fill remaining hours to default 0.0 value
            while( index <= idx_max_col_utilization_table ){
                preparedStatement.setDouble( index, 0 );
                index++;
            }

        }
        catch( SQLException e ){
            e.printStackTrace();
        }

        return preparedStatement;

    }

    private List<Double> getEmployeeWeeklyHours( Map<String, Double> weekMap ) {
        List<Double> empWeekHoursMap = new ArrayList<>();

        for( Entry<String, Double> weekHours : weekMap.entrySet() ){
            empWeekHoursMap.add( weekHours.getValue() );
        }

        return empWeekHoursMap;
    }
}
