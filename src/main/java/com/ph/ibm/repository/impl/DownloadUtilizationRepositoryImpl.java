package com.ph.ibm.repository.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import com.ph.ibm.model.YtdUtilization;
import com.ph.ibm.repository.DownloadUtilizationRepository;
import com.ph.ibm.repository.UtilizationRepository;
import com.ph.ibm.resources.ConnectionPool;
import com.ph.ibm.util.FormatUtils;
import com.ph.ibm.util.OpumConstants;
import com.ph.ibm.util.SqlQueries;

/**
 * @author <a HREF="dacanam@ph.ibm.com">Marjay Dacanay</a>
 * @author <a HREF="teodorj@ph.ibm.com">Joemarie Teodoro</a>
 */
public class DownloadUtilizationRepositoryImpl implements DownloadUtilizationRepository {

    private ConnectionPool connectionPool = ConnectionPool.getInstance();

    private Logger logger = Logger.getLogger( DownloadUtilizationRepositoryImpl.class );

    /**
     * @param ytdUtilization
     * @return
     * @see com.ph.ibm.repository.DownloadUtilizationRepository#retrieveOffshoreActualHours(com.ph.ibm.model.YtdUtilization)
     */
    @Override
    public TreeMap<String, String> retrieveWeeks( YtdUtilization ytdUtilization ) {
        Connection connection = connectionPool.getConnection();
        TreeMap<String, String> week = new TreeMap<>();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try{
            String query = "SELECT NAME, END FROM WEEK WHERE END BETWEEN ? AND ? ";
            preparedStatement = connection.prepareStatement( query );
            preparedStatement.setString( 1, ytdUtilization.getStartDate() );
            preparedStatement.setString( 2, ytdUtilization.getEndDate() );
            resultSet = preparedStatement.executeQuery();
            while( resultSet.next() ){
                week.put( resultSet.getString( "NAME" ), resultSet.getString( "END" ) );
            }
            resultSet.close();
            preparedStatement.close();
        }
        catch( SQLException e ){
            e.printStackTrace();
            logger.error( e.getStackTrace() );
        }
        return week;
    }

    /**
     * @param weekMap
     * @return
     * @see com.ph.ibm.repository.DownloadUtilizationRepository#retrieveOffshoreActualHours(java.util.TreeMap)
     */
    @Override
    public List<Map<String, List<Double>>> retrieveActualHours( String weekName, int yearId, String weekEnding,
                                                                String designation ) {
        Connection connection = connectionPool.getConnection();
        List<Map<String, List<Double>>> lstWeeklyHours = new ArrayList<>();
        Map<String, List<Double>> weeklyHoursMap = new TreeMap<>();
        List<Double> listWeeklyHours = new ArrayList<Double>();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try{
            String query = "SELECT * FROM UTILIZATION U, EMPLOYEE E WHERE E.EMPLOYEE_ID = U.EMPLOYEE_SERIAL" +
                " AND E.DESIGNATION = ? AND E.EMP_STATUS = 'A' AND U.TYPE = 'ACTUAL' AND U.YEAR_ID = ?";
            preparedStatement = connection.prepareStatement( query );
            preparedStatement.setString( 1, designation );
            preparedStatement.setInt( 2, yearId );
            resultSet = preparedStatement.executeQuery();

            if( resultSet != null ){
                while( resultSet.next() ){
                    listWeeklyHours.add( resultSet.getDouble( weekName ) );
                }
                weeklyHoursMap.put( weekEnding, listWeeklyHours );
                listWeeklyHours = new ArrayList<Double>();
                resultSet.beforeFirst();
                lstWeeklyHours.add( weeklyHoursMap );
            }
            resultSet.close();
            preparedStatement.close();
        }
        catch( SQLException e ){
            e.printStackTrace();
            logger.error( e.getStackTrace() );
        }
        return lstWeeklyHours;
    }

    /**
     * @param weekMap
     * @param designation
     * @return
     * @see com.ph.ibm.repository.DownloadUtilizationRepository#retrieveForecastedHours(java.util.TreeMap,
     *      java.lang.String)
     */
    @Override
    public List<Map<String, List<Double>>> retrieveForecastedHours( String weekName, int yearId, String weekEnding,
                                                                    String designation ) {
        Connection connection = connectionPool.getConnection();
        List<Map<String, List<Double>>> lstWeeklyHours = new ArrayList<>();
        Map<String, List<Double>> weeklyHoursMap = new TreeMap<>();
        List<Double> listWeeklyHours = new ArrayList<Double>();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try{
            String query = "SELECT * FROM UTILIZATION U, EMPLOYEE E WHERE E.EMPLOYEE_ID = U.EMPLOYEE_SERIAL" +
                " AND E.DESIGNATION = ? AND E.EMP_STATUS = 'A' AND U.TYPE = 'FORECAST' AND U.YEAR_ID = ?";
            preparedStatement = connection.prepareStatement( query );
            preparedStatement.setString( 1, designation );
            preparedStatement.setInt( 2, yearId );
            resultSet = preparedStatement.executeQuery();

            if( resultSet != null ){
                while( resultSet.next() ){
                    listWeeklyHours.add( resultSet.getDouble( weekName ) );
                }
                weeklyHoursMap.put( weekEnding, listWeeklyHours );
                listWeeklyHours = new ArrayList<Double>();
                resultSet.beforeFirst();
                lstWeeklyHours.add( weeklyHoursMap );
            }
            resultSet.close();
            preparedStatement.close();
        }
        catch( SQLException e ){
            e.printStackTrace();
            logger.error( e.getStackTrace() );
        }
        return lstWeeklyHours;
    }

    /**
     * @param ytdUtilization
     * @return
     * @see com.ph.ibm.repository.DownloadUtilizationRepository#retrieveOffshoreActualHours(com.ph.ibm.model.YtdUtilization)
     */
    @Override
    public List<String> retrieveEmployeeList() {
        Connection connection = connectionPool.getConnection();
        List<String> employeeList = new ArrayList<>();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try{
            String query = "SELECT DISTINCT * FROM EMPLOYEE E, EMPLOYEE_ROLE ER WHERE\r\n" +
                " E.EMPLOYEE_ID = ER.EMPLOYEE_ID AND\r\n" + " E.EMP_STATUS = 'A' AND\r\n" + " ER.ROLE_ID = 5; ";
            preparedStatement = connection.prepareStatement( query );
            resultSet = preparedStatement.executeQuery();
            while( resultSet.next() ){
                employeeList.add( resultSet.getString( "EMPLOYEE_ID" ) );
            }
            resultSet.close();
            preparedStatement.close();
        }
        catch( SQLException e ){
            e.printStackTrace();
            logger.error( e.getStackTrace() );
        }
        return employeeList;
    }

    /**
     * @param ytdUtilization
     * @return
     * @see com.ph.ibm.repository.DownloadUtilizationRepository#retrieveOffshoreActualHours(com.ph.ibm.model.YtdUtilization)
     */
    @Override
    public TreeMap<String, TreeMap<Integer, String>> retrieveFiscalYear( YtdUtilization ytdUtilization ) {
        Connection connection = connectionPool.getConnection();
        TreeMap<String, TreeMap<Integer, String>> weekMapping = new TreeMap<>();
        TreeMap<Integer, String> fyWeekMap = new TreeMap<>();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try{
            String query = "SELECT NAME, END, YEAR_ID FROM WEEK WHERE END BETWEEN ? AND ? ";
            preparedStatement = connection.prepareStatement( query );
            preparedStatement.setString( 1, ytdUtilization.getStartDate() );
            preparedStatement.setString( 2, ytdUtilization.getEndDate() );
            resultSet = preparedStatement.executeQuery();
            while( resultSet.next() ){
                fyWeekMap.put( resultSet.getInt( "YEAR_ID" ), resultSet.getString( "END" ) );
                weekMapping.put( resultSet.getString( "NAME" ), fyWeekMap );
                fyWeekMap = new TreeMap<>();
            }
            resultSet.close();
            preparedStatement.close();
        }
        catch( SQLException e ){
            e.printStackTrace();
            logger.error( e.getStackTrace() );
        }
        return weekMapping;
    }

    /**
     * This method is used to retrieve the combined actual and forecasted hours using a stored procedure
     * 
     * @param serial
     * @param year
     * @return
     * @throws SQLException
     */
    @Override
    public List<Map<String, List<Double>>> getOutlookHours( Map<String, TreeMap<Integer, String>> fyWeekMapping )
        throws SQLException {
        Connection connection = connectionPool.getConnection();
        UtilizationRepository utilizationRepository = new UtilizationRepositoryImpl();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<String> employeeList = retrieveEmployeeList();
        List<Double> lstCombinedUtilizationHours = new ArrayList<Double>();
        Map<String, List<Double>> lstOffShoreOutlookHours = new TreeMap<>();
        List<Map<String, List<Double>>> lstOffShoreWeeklyOutlookHours = new ArrayList<>();
        try{

            for( Entry<String, TreeMap<Integer, String>> weekMap : fyWeekMapping.entrySet() ){
                for( Entry<Integer, String> map : weekMap.getValue().entrySet() ){
                    for( String employee : employeeList ){
                        CallableStatement cStmt =
                            connection.prepareCall( SqlQueries.SQL_GET_EMPLOYEE_COMBINED_UTILIZATION );
                        cStmt.setString( 1, employee );
                        cStmt.setInt( 2, Integer.valueOf( map.getKey() ) );
                        resultSet = cStmt.executeQuery();
                        if( resultSet.next() ){
                            lstCombinedUtilizationHours.add( resultSet.getDouble( weekMap.getKey() ) );
                        }
                        else{
                            lstCombinedUtilizationHours = utilizationRepository.getEmployeeUtilization( employee,
                                String.valueOf( map.getKey() ), OpumConstants.FORECAST_UTILIZATION );
                        }
                    }
                    lstOffShoreOutlookHours.put( map.getValue(), lstCombinedUtilizationHours );
                    lstCombinedUtilizationHours = new ArrayList<>();
                }
            }

        }
        catch( SQLException e ){
            e.printStackTrace();
        }
        finally{
            connectionPool.closeConnection( connection, preparedStatement, resultSet );
        }
        lstOffShoreWeeklyOutlookHours.add( lstOffShoreOutlookHours );
        return lstOffShoreWeeklyOutlookHours;
    }

    /**
     * @param ytdUtilization
     * @return
     * @see com.ph.ibm.repository.DownloadUtilizationRepository#retrieveOffshoreActualHours(com.ph.ibm.model.YtdUtilization)
     */
    @Override
    public TreeMap<String, TreeMap<String, String>> getRollInRollOffDate( String designation ) {
        Connection connection = connectionPool.getConnection();
        TreeMap<String, TreeMap<String, String>> employeeList = new TreeMap<>();
        TreeMap<String, String> empRollInRollOffMap = new TreeMap<>();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try{
            String query =
                "SELECT DISTINCT E.EMPLOYEE_ID, E.ROLL_IN_DATE, E.ROLL_OFF_DATE FROM EMPLOYEE E, EMPLOYEE_ROLE ER WHERE\r\n" +
                    " E.EMPLOYEE_ID = ER.EMPLOYEE_ID AND\r\n" + " E.EMP_STATUS = 'A' AND\r\n" +
                    " ER.ROLE_ID = 5 AND E.DESIGNATION = ?";
            preparedStatement = connection.prepareStatement( query );
            preparedStatement.setString( 1, designation );
            resultSet = preparedStatement.executeQuery();
            while( resultSet.next() ){
                empRollInRollOffMap.put( resultSet.getString( "ROLL_IN_DATE" ),
                    resultSet.getString( "ROLL_OFF_DATE" ) );
                employeeList.put( resultSet.getString( "EMPLOYEE_ID" ), empRollInRollOffMap );
                empRollInRollOffMap = new TreeMap<>();
            }
            resultSet.close();
            preparedStatement.close();
        }
        catch( SQLException e ){
            e.printStackTrace();
            logger.error( e.getStackTrace() );
        }
        return employeeList;
    }

    /**
     * @param ytdUtilization
     * @return
     * @see com.ph.ibm.repository.DownloadUtilizationRepository#retrieveOffshoreActualHours(com.ph.ibm.model.YtdUtilization)
     */
    @Override
    public TreeMap<LocalDateTime, Integer> retrieveAvailableData( TreeMap<String, TreeMap<String, String>> employeeList ) {
        Connection connection = connectionPool.getConnection();
        TreeMap<String, TreeMap<LocalDateTime, Integer>> employeeWeekMap = new TreeMap<>();
        TreeMap<LocalDateTime, Integer> weekMapping = new TreeMap<>();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try{
            for( Entry<String, TreeMap<String, String>> employeeWeeks : employeeList.entrySet() ){
                for( Entry<String, String> employeeRollInRollOffDate : employeeWeeks.getValue().entrySet() ){
                    String query =
                        " SELECT DATE, VALUE FROM FY_TEMPLATE FT\r\n" + " WHERE\r\n" + " DATE BETWEEN ? AND ?";
                    preparedStatement = connection.prepareStatement( query );
                    preparedStatement.setString( 1, employeeRollInRollOffDate.getKey() );
                    preparedStatement.setString( 2, employeeRollInRollOffDate.getValue() );
                    resultSet = preparedStatement.executeQuery();
                    LocalDate fromDate = FormatUtils.toDBDateFormat( employeeRollInRollOffDate.getKey() );
                    LocalDate toDate = FormatUtils.toDBDateFormat( employeeRollInRollOffDate.getValue() );
                    LocalDateTime counterDateTime = LocalDateTime.of( fromDate, LocalTime.from( LocalTime.MIN ) );
                    LocalDateTime toDateTime = LocalDateTime.of( toDate, LocalTime.from( LocalTime.MIN ) );
                    while( counterDateTime.isBefore( toDateTime ) || counterDateTime.isEqual( toDateTime ) ){
                        int totalHours = 0;
                        //start date of the week
                        while( !( counterDateTime.getDayOfWeek().name().equalsIgnoreCase( "SATURDAY" ) ||
                            counterDateTime.getDayOfWeek().name().equalsIgnoreCase( "SUNDAY" ) ) &&
                            ( counterDateTime.isBefore( toDateTime ) || counterDateTime.isEqual( toDateTime ) ) ){
                            totalHours += 8;
                            if( counterDateTime.getDayOfWeek().name().equals( "FRIDAY" ) ||
                                counterDateTime.equals( toDateTime ) ){
                                break;
                            }
                            counterDateTime = counterDateTime.plusDays( 1 );
                        }
                        if( totalHours == 0 ){
                            counterDateTime = counterDateTime.plusDays( 1 );
                            continue;
                        }

                        weekMapping.put( counterDateTime, totalHours );
                        counterDateTime = counterDateTime.plusDays( 1 );
                        totalHours = 0;
                    }
                }
                employeeWeekMap.put( employeeWeeks.getKey(), weekMapping );
                weekMapping = new TreeMap<>();
            }
            resultSet.close();
            preparedStatement.close();
        }
        catch( SQLException e ){
            e.printStackTrace();
            logger.error( e.getStackTrace() );
        }
        for( Entry<String, TreeMap<LocalDateTime, Integer>> map : employeeWeekMap.entrySet() ){
            System.out.println( map.getValue() );
        }
        return weekMapping;
    }
}
