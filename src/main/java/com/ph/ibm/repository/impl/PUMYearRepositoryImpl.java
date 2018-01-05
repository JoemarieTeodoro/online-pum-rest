package com.ph.ibm.repository.impl;

import static com.ph.ibm.util.OpumConstants.COUNT_OF_WEEKS_PER_QUARTER;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.ph.ibm.model.Holiday;
import com.ph.ibm.model.PUMMonth;
import com.ph.ibm.model.PUMQuarter;
import com.ph.ibm.model.PUMYear;
import com.ph.ibm.model.Role;
import com.ph.ibm.opum.exception.OpumException;
import com.ph.ibm.repository.PUMYearRepository;
import com.ph.ibm.resources.ConnectionPool;
import com.ph.ibm.util.FormatUtils;
import com.ph.ibm.util.OpumConstants;
import com.ph.ibm.util.SqlQueries;

public class PUMYearRepositoryImpl implements PUMYearRepository {
    private static final int SQL_GET_EMPLOYEE_LIST_ROLE_ID = 1;

	private static final int SQL_POPULATE_UTILIZATION_YEAR_ID = 3;

	private static final int SQL_POPULATE_UTILIZATION_FORECAST = 2;

	private static final int SQL_POPULATE_UTILIZATION_SERIAL_NUMBER = 1;

	Logger logger = Logger.getLogger( PUMYearRepositoryImpl.class );

    private ConnectionPool connectionPool = ConnectionPool.getInstance();

    public static final String DATE_FORMAT = "yyyy-MM-dd";

    protected LinkedHashMap<String, String[]> weekMap = new LinkedHashMap<>();

    protected int yearID;

    @Override
    public void saveYear( PUMYear pumYear ) throws SQLException, ParseException, OpumException {
        Connection connection = connectionPool.getConnection();
        PreparedStatement preparedStatement = null;
        try{
            DateFormat df = new SimpleDateFormat( DATE_FORMAT );
            connection.setAutoCommit( false );
            preparedStatement = connection.prepareStatement( SqlQueries.SQL_SAVE_YEAR );
            preparedStatement.setDate( 1, new Date( df.parse( pumYear.getStart() ).getTime() ) );
            preparedStatement.setDate( 2, new Date( df.parse( pumYear.getEnd() ).getTime() ) );
            preparedStatement.setInt( 3, pumYear.getPumYear() );
            preparedStatement.setString( 4, Role.ADMIN.toString() );
            preparedStatement.executeUpdate();
            connection.commit();

        }
        catch( Exception e ){
            logger.error( e.getMessage() );
            throw new OpumException( e.getMessage() );
        }
        finally{
            connectionPool.closeConnection( connection, preparedStatement );
        }
    }

    @Override
    public void populateFiscalYear( PUMYear pumYear ) {
        Connection connection = connectionPool.getConnection();
        PreparedStatement preparedStatement = null;

        List<String> lstWeekend = Arrays.asList( "SATURDAY", "SUNDAY" );
        try{
            connection.setAutoCommit( false );
            DateTimeFormatter df = DateTimeFormatter.ofPattern( DATE_FORMAT );
            LocalDate fromDate = LocalDate.parse( pumYear.getStart(), df );
            LocalDate toDate = LocalDate.parse( pumYear.getEnd(), df );
            LocalDateTime counterDateTime = LocalDateTime.of( fromDate, LocalTime.from( LocalTime.MIN ) );
            LocalDateTime toDateTime = LocalDateTime.of( toDate, LocalTime.from( LocalTime.MIN ) ).plusDays( 1 );
            yearID = retrieveYearDate( pumYear.getPumYear() ).getYearId();
            preparedStatement = connection.prepareStatement( SqlQueries.SQL_POPULATE_FY );

            while( !counterDateTime.equals( toDateTime ) ){
                preparedStatement.setInt( 1, yearID );
                preparedStatement.setDate( 2, Date.valueOf( counterDateTime.toLocalDate() ) );
                preparedStatement.setString( 3,
                    lstWeekend.contains( counterDateTime.getDayOfWeek().name() ) ? "" : OpumConstants.EIGHT );
                preparedStatement.setInt( 4, 0 ); // isHoliday
                preparedStatement.setString( 5, "" ); // EventName
                preparedStatement.addBatch();
                counterDateTime = counterDateTime.plusDays( 1 );
            }

            preparedStatement.executeBatch();
            connection.commit();
        }
        catch( SQLException e ){
            logger.error( e );
        }
        finally{
            connectionPool.closeConnection( connection, preparedStatement );
        }
    }

    @Override
    public void addUpdateHolidayInFiscalYearTemplate( List<Holiday> lstHoliday, PUMYear pumYear ) throws OpumException {
        Connection connection = connectionPool.getConnection();
        PreparedStatement preparedStatement = null;

        try{
            if( pumYear != null ){
                for( Holiday holiday : lstHoliday ){
                    connection.setAutoCommit( false );
                    preparedStatement = connection.prepareStatement( SqlQueries.SQL_ADD_OR_UPDATE_HOLIDAY );
                    preparedStatement.setString( 1, holiday.getName() );
                    preparedStatement.setDate( 2, Date.valueOf( holiday.getDate() ) );
                    preparedStatement.setInt( 3, pumYear.getYearId() );
                    preparedStatement.addBatch();
                }
            }
            else{
                throw new OpumException( "Fiscal year not found!" );
            }
            preparedStatement.executeBatch();
            connection.commit();
        }
        catch( SQLException e ){
            logger.error( e );
        }
        catch( OpumException e ){
            throw new OpumException( e.getMessage() );
        }
        finally{
            connectionPool.closeConnection( connection, preparedStatement );
        }
    }

    @Override
    public boolean checkIfPUMCycleExisting( PUMYear pumYear ) throws ParseException, OpumException {
        ConnectionPool connectionPool = ConnectionPool.getInstance();
        Connection connection = connectionPool.getConnection();
        ResultSet rs = null;
        PreparedStatement preparedStatement = null;

        try{
            preparedStatement = connection.prepareStatement( SqlQueries.SQL_CHECK_PUM_CYLCE );
            preparedStatement.setInt( 1, pumYear.getPumYear() );
            rs = preparedStatement.executeQuery();
            if( rs.next() ){
                if( Integer.valueOf( rs.getString( 1 ) ) > 0 ){
                    return true;
                }
            }
        }
        catch( SQLException e ){
            logger.error( e.getMessage() );
        }
        finally{
            connectionPool.closeConnection( connection, preparedStatement, rs );
        }

        return false;
    }

    @Override
    public boolean saveQuarter( PUMQuarter pumQuarter ) throws SQLException, ParseException {
        Connection connection = connectionPool.getConnection();
        PreparedStatement preparedStatement = null;

        try{
            DateFormat df = new SimpleDateFormat( "yyyy-MM-dd" );
            connection.setAutoCommit( false );
            preparedStatement = connection.prepareStatement( SqlQueries.SQL_SAVE_QUARTER );
            preparedStatement.setDate( 1, new java.sql.Date( df.parse( pumQuarter.getStart() ).getTime() ) );
            preparedStatement.setDate( 2, new java.sql.Date( df.parse( pumQuarter.getEnd() ).getTime() ) );
            preparedStatement.setInt( 3, pumQuarter.getPumQuarter() );

            preparedStatement.executeUpdate();
            connection.commit();

            System.out.println( OpumConstants.UPDATED_SUCCESS );
            return true;
        }
        catch( SQLException e ){
            e.printStackTrace();
        }
        finally{
            connectionPool.closeConnection( connection, preparedStatement );
        }
        return false;
    }

    @Override
    public boolean saveMonth( PUMMonth pumMonth ) throws SQLException, ParseException {
        Connection connection = connectionPool.getConnection();
        PreparedStatement preparedStatement = null;

        try{
            DateFormat df = new SimpleDateFormat( "yyyy-MM-dd" );
            connection.setAutoCommit( false );
            preparedStatement = connection.prepareStatement( SqlQueries.SQL_SAVE_MONTH );
            preparedStatement.setDate( 1, new java.sql.Date( df.parse( pumMonth.getStart() ).getTime() ) );
            preparedStatement.setDate( 2, new java.sql.Date( df.parse( pumMonth.getEnd() ).getTime() ) );
            preparedStatement.setInt( 3, pumMonth.getPumMonth() );

            preparedStatement.executeUpdate();
            connection.commit();

            System.out.println( OpumConstants.UPDATED_SUCCESS );

            return true;
        }
        catch( SQLException e ){
            e.printStackTrace();
        }
        finally{
            connectionPool.closeConnection( connection, preparedStatement );
        }
        return false;
    }

    @Override
    public List<PUMYear> retrieveYear() throws SQLException {
        Connection connection = connectionPool.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<PUMYear> pumYearList = new ArrayList<PUMYear>();
        try{
            preparedStatement = connection.prepareStatement( SqlQueries.SQL_RETRIEVE_ALL_YEAR );
            resultSet = preparedStatement.executeQuery();
            while( resultSet.next() ){
                int yearId = resultSet.getInt( "YEAR_ID" );
                int pumYear = resultSet.getInt( "PUMYEAR" );
                String end = resultSet.getString( "END" );
                String start = resultSet.getString( "START" );
                String createDate = resultSet.getString( "CREATEDATE" );
                String createdBy = resultSet.getString( "CREATEDBY" );
                String updateDate = resultSet.getString( "UPDATEDATE" );
                String updatedBy = resultSet.getString( "UPDATEDBY" );
                PUMYear pumYear1 =
                    new PUMYear( yearId, pumYear, end, start, createDate, createdBy, updateDate, updatedBy );
                pumYearList.add( pumYear1 );
            }

        }
        catch( SQLException e ){
            e.printStackTrace();
        }
        finally{
            connectionPool.closeConnection( connection, preparedStatement, resultSet );
        }
        return pumYearList;
    }

    @Override
    public PUMYear retrieveYearDate( int year ) throws SQLException {
        Connection connection = connectionPool.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        PUMYear pumYear = null;
        try{
            preparedStatement = connection.prepareStatement( SqlQueries.SQL_RETRIEVE_YEAR_DATE );
            preparedStatement.setInt( 1, year );
            resultSet = preparedStatement.executeQuery();
            while( resultSet.next() ){
                int yearId = resultSet.getInt( "YEAR_ID" );
                int y = resultSet.getInt( "PUMYEAR" );
                String end = resultSet.getString( "END" );
                String start = resultSet.getString( "START" );
                String createDate = resultSet.getString( "CREATEDATE" );
                String createdBy = resultSet.getString( "CREATEDBY" );
                String updateDate = resultSet.getString( "UPDATEDATE" );
                String updatedBy = resultSet.getString( "UPDATEDBY" );
                pumYear = new PUMYear( yearId, y, end, start, createDate, createdBy, updateDate, updatedBy );
            }

        }
        catch( SQLException e ){
            e.printStackTrace();
        }
        finally{
            connectionPool.closeConnection( connection, preparedStatement, resultSet );
        }
        return pumYear;
    }

    @Override
    public boolean editYear( PUMYear pumYear ) throws SQLException, ParseException {
        Connection connection = connectionPool.getConnection();
        PreparedStatement preparedStatement = null;
        DateFormat df = new SimpleDateFormat( "yyyy-MM-dd" );

        try{
            preparedStatement = connection.prepareStatement( SqlQueries.SQL_EDIT_YEAR );
            preparedStatement.setDate( 1, new java.sql.Date( df.parse( pumYear.getStart() ).getTime() ) );
            preparedStatement.setDate( 2, new java.sql.Date( df.parse( pumYear.getEnd() ).getTime() ) );
            preparedStatement.setInt( 3, pumYear.getPumYear() );
            preparedStatement.executeUpdate();
            connection.commit();
            System.out.println( OpumConstants.UPDATED_SUCCESS );
            preparedStatement.close();
            return true;
        }
        catch( SQLException e ){
            e.printStackTrace();

        }
        finally{
            connectionPool.closeConnection( connection, preparedStatement );
        }

        return false;
    }

    @Override
    public PUMYear retrieveCurrentFY() {
        Connection connection = connectionPool.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try{
            PUMYear pumYear = null;
            preparedStatement = connection.prepareStatement( SqlQueries.SQL_RETRIEVE_CURRENT_FY );
            resultSet = preparedStatement.executeQuery();
            while( resultSet.next() ){
                pumYear = new PUMYear();
                pumYear.setCreateDate( resultSet.getString( "createDate" ) );
                pumYear.setCreatedBy( resultSet.getString( "createdBy" ) );
                pumYear.setEnd( resultSet.getString( "end" ) );
                pumYear.setPumYear( resultSet.getInt( "pumYear" ) );
                pumYear.setStart( resultSet.getString( "start" ) );
                pumYear.setUpdateDate( resultSet.getString( "updateDate" ) );
                pumYear.setYearId( resultSet.getInt( "year_id" ) );
            }
            return pumYear;
        }
        catch( Exception e ){
            logger.error( e.getMessage() );
        }
        return null;
    }

    @Override
    public void updateFiscalYear( PUMYear pumYear ) throws ParseException, OpumException {
        Connection connection = connectionPool.getConnection();
        PreparedStatement preparedStatement = null;

        try{
            connection.setAutoCommit( false );
            DateFormat df = new SimpleDateFormat( DATE_FORMAT );
            preparedStatement = connection.prepareStatement( SqlQueries.SQL_UPDATE_FISCAL_YEAR );
            preparedStatement.setDate( 1, new Date( df.parse( pumYear.getStart() ).getTime() ) );
            preparedStatement.setDate( 2, new Date( df.parse( pumYear.getEnd() ).getTime() ) );
            preparedStatement.setInt( 3, pumYear.getPumYear() );
            preparedStatement.executeUpdate();
            connection.commit();
        }
        catch( SQLException e ){
            logger.error( e.getMessage() );
        }
        finally{
            connectionPool.closeConnection( connection, preparedStatement );
        }

    }

    @Override
    public void deleteFiscalYearTemplate( PUMYear pumYear ) {
        Connection connection = connectionPool.getConnection();
        PreparedStatement preparedStatement = null;

        try{
            connection.setAutoCommit( false );
            preparedStatement = connection.prepareStatement( SqlQueries.SQL_DELETE_FY_TEMPLATE );
            preparedStatement.setInt( 1, pumYear.getYearId() );
            preparedStatement.executeUpdate();
            connection.commit();

        }
        catch( Exception e ){
            logger.error( e.getMessage() );
        }
        finally{
            connectionPool.closeConnection( connection, preparedStatement );
        }

    }

    @Override
    public void deleteFiscalQuarters( PUMYear pumYear ) {
        Connection connection = connectionPool.getConnection();
        PreparedStatement preparedStatement = null;

        try{
            connection.setAutoCommit( false );
            preparedStatement = connection.prepareStatement( SqlQueries.SQL_DELETE_FY_QUARTER );
            preparedStatement.setInt( 1, pumYear.getYearId() );
            preparedStatement.executeUpdate();
            connection.commit();

        }
        catch( Exception e ){
            logger.error( e.getMessage() );
        }
        finally{
            connectionPool.closeConnection( connection, preparedStatement );
        }

    }

    @Override
    public void deleteFiscalWeeks( PUMYear pumYear ) {
        Connection connection = connectionPool.getConnection();
        PreparedStatement preparedStatement = null;

        try{
            connection.setAutoCommit( false );
            preparedStatement = connection.prepareStatement( SqlQueries.SQL_DELETE_FY_WEEK );
            preparedStatement.setInt( 1, pumYear.getYearId() );
            preparedStatement.executeUpdate();
            connection.commit();

        }
        catch( Exception e ){
            logger.error( e.getMessage() );
        }
        finally{
            connectionPool.closeConnection( connection, preparedStatement );
        }

    }

    @Override
    public void populateFiscalWeeks( PUMYear pumYear ) {
        Connection connection = connectionPool.getConnection();
        PreparedStatement preparedStatement = null;
        try{
            connection.setAutoCommit( false );
            preparedStatement = connection.prepareStatement( SqlQueries.SQL_POPULATE_FY_WEEKS );

            for( Map.Entry<String, String[]> week : weekMap.entrySet() ){
                preparedStatement.setString( 1, week.getValue()[0] );
                preparedStatement.setString( 2, week.getKey() );
                preparedStatement.setInt( 3, Integer.valueOf( week.getValue()[1] ) );
                preparedStatement.setDate( 4, Date.valueOf( week.getValue()[2] ) );
                preparedStatement.setDate( 5, Date.valueOf( week.getValue()[3] ) );
                preparedStatement.addBatch();
            }

            preparedStatement.executeBatch();
            connection.commit();
        }
        catch(

        SQLException e ){
            e.printStackTrace();
            logger.error( e );
        }
        finally{
            weekMap.clear();
            connectionPool.closeConnection( connection, preparedStatement );
        }
    }

    @Override
    public void populateFiscalQuarters( PUMYear pumYear ) {
        Connection connection = connectionPool.getConnection();
        PreparedStatement preparedStatement = null;

        try{
            connection.setAutoCommit( false );
            LocalDate fromDate = FormatUtils.toDBDateFormat( pumYear.getStart() );
            LocalDate toDate = FormatUtils.toDBDateFormat( pumYear.getEnd() );
            LocalDateTime counterDateTime = LocalDateTime.of( fromDate, LocalTime.from( LocalTime.MIN ) );
            LocalDateTime startWeekDate = null;
            LocalDateTime startQuarterDate = counterDateTime;
            LocalDateTime toDateTime = LocalDateTime.of( toDate, LocalTime.from( LocalTime.MIN ) );
            int weekID = 1;
            int quarterID = 1;
            int yearId = retrieveYearDate( pumYear.getPumYear() ).getYearId();
            preparedStatement = connection.prepareStatement( SqlQueries.SQL_POPULATE_FY_QUARTER );

            while( counterDateTime.isBefore( toDateTime ) ){
                //start date of the week
                startWeekDate = counterDateTime;
                while( !counterDateTime.getDayOfWeek().name().equalsIgnoreCase( "FRIDAY" ) &&
                    counterDateTime.isBefore( toDateTime ) ){
                    counterDateTime = counterDateTime.plusDays( 1 );
                }

                weekMap = populateWeekMap( weekMap, quarterID, yearId, startWeekDate, counterDateTime, weekID );

                // Insert Fiscal Quarter's Start and End Date
                if( weekID % COUNT_OF_WEEKS_PER_QUARTER == 0 ){
                    preparedStatement.setInt( 1, yearId );
                    preparedStatement.setString( 2, "Quarter " + quarterID );
                    preparedStatement.setDate( 3, Date.valueOf( startQuarterDate.toLocalDate() ) );
                    preparedStatement.setDate( 4, Date.valueOf( counterDateTime.toLocalDate() ) );
                    preparedStatement.addBatch();
                    quarterID++;
                    startQuarterDate = counterDateTime.plusDays( 1 );
                }

                weekID++; // increment week number
                counterDateTime = counterDateTime.plusDays( 1 ); // increment to first day of week
            }
            preparedStatement.executeBatch();
            connection.commit();
        }
        catch( SQLException e ){
            e.printStackTrace();
            logger.error( e );
        }
        finally{
            connectionPool.closeConnection( connection, preparedStatement );
        }
    }

    /**
     * @param counterDateTime
     * @param startWeekDate
     * @param weekID
     * @param quarterID
     * @param yearId
     * @return
     */
    private LinkedHashMap<String, String[]> populateWeekMap( LinkedHashMap<String, String[]> weekMap, int quarterID,
                                                             int yearId, LocalDateTime startWeekDate,
                                                             LocalDateTime counterDateTime, int weekID ) {
        weekMap.put( "Week " + weekID,
            new String[]{ String.valueOf( quarterID ), String.valueOf( yearId ), startWeekDate.toLocalDate().toString(),
                          counterDateTime.toLocalDate().toString(), } );
        return weekMap;

    }

    /**
     * @param employeeSerial
     * @return
     * @throws SQLException
     * @see com.ph.ibm.repository.PUMYearRepository#createFiscaWeeksTemplate(java.lang.String)
     */
    @Override
    public void populateUtilization( List<String> lstEmployees ) throws SQLException {
        Connection connection = connectionPool.getConnection();
        PreparedStatement preparedStatement = null;
        connection.setAutoCommit( false );
        try{
            int yearId = retrieveCurrentFY().getYearId();
            preparedStatement = connection.prepareStatement( SqlQueries.SQL_POPULATE_UTILIZATION );
            for( String employee : lstEmployees ){
                preparedStatement.setString( SQL_POPULATE_UTILIZATION_SERIAL_NUMBER, employee );
                preparedStatement.setString( SQL_POPULATE_UTILIZATION_FORECAST, OpumConstants.FORECAST_UTILIZATION );
                preparedStatement.setInt( SQL_POPULATE_UTILIZATION_YEAR_ID, yearId );
                preparedStatement.addBatch();
            }

            preparedStatement.executeBatch();
            connection.commit();
            preparedStatement.close();
        }
        catch( Exception e ){
            logger.error( e.getMessage() );
        }
    }

    /**
     * @param employeeSerial
     * @return
     * @throws SQLException
     * @see com.ph.ibm.repository.PUMYearRepository#createFiscaWeeksTemplate(java.lang.String)
     */
    @Override
    public void populateUtilization( String employee ) throws SQLException {
        Connection connection = connectionPool.getConnection();
        PreparedStatement preparedStatement = null;
        connection.setAutoCommit( false );
        try{
            int yearId;
            if( retrieveCurrentFY() != null ){
                yearId = retrieveCurrentFY().getYearId();
                preparedStatement = connection.prepareStatement( SqlQueries.SQL_POPULATE_UTILIZATION );
                preparedStatement.setString( SQL_POPULATE_UTILIZATION_SERIAL_NUMBER, employee );
                preparedStatement.setString( SQL_POPULATE_UTILIZATION_FORECAST, OpumConstants.FORECAST_UTILIZATION );
                preparedStatement.setInt( SQL_POPULATE_UTILIZATION_YEAR_ID, yearId );
                preparedStatement.executeUpdate();
                connection.commit();
                preparedStatement.close();
            }
        }
        catch( Exception e ){
            logger.error( e.getMessage() );
        }
    }

    /**
     * @param serialNumber
     * @return
     * @throws SQLException
     * @see com.ph.ibm.repository.EmployeeRepository#retrieveRecentPassword(java.lang.String)
     */
    @Override
    public List<String> getEmployeeList() throws SQLException {
        Connection connection = connectionPool.getConnection();
        PreparedStatement preparedStatement = null;
        List<String> lstEmployees = new ArrayList<String>();
        ResultSet resultSet = null;
        try{
            preparedStatement = connection.prepareStatement( SqlQueries.SQL_GET_EMPLOYEE_LIST );
            preparedStatement.setInt( SQL_GET_EMPLOYEE_LIST_ROLE_ID, Role.USER.getRoleId() );
            resultSet = preparedStatement.executeQuery();
            while( resultSet.next() ){
                lstEmployees.add( resultSet.getString( "EMPLOYEE_ID" ) );
            }
        }
        catch( SQLException e ){
            logger.error( e.getStackTrace() );
            return null;
        }
        finally{
            connectionPool.closeConnection( connection, preparedStatement, resultSet );
        }

        return lstEmployees;
    }

}
