package com.ph.ibm.bo;

import java.sql.BatchUpdateException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.apache.log4j.Logger;

import com.ph.ibm.model.Employee;
import com.ph.ibm.model.EmployeeUtil;
import com.ph.ibm.model.Holiday;
import com.ph.ibm.model.Month;
import com.ph.ibm.model.PUMMonth;
import com.ph.ibm.model.PUMQuarter;
import com.ph.ibm.model.PUMYear;
import com.ph.ibm.model.Project;
import com.ph.ibm.model.ProjectEngagement;
import com.ph.ibm.model.Quarter;
import com.ph.ibm.model.Utilization;
import com.ph.ibm.model.UtilizationJson;
import com.ph.ibm.model.UtilizationYear;
import com.ph.ibm.model.Week;
import com.ph.ibm.model.Year;
import com.ph.ibm.opum.exception.InvalidCSVException;
import com.ph.ibm.opum.exception.InvalidEmployeeException;
import com.ph.ibm.repository.EmployeeRepository;
import com.ph.ibm.repository.HolidayEngagementRepository;
import com.ph.ibm.repository.PUMYearRepository;
import com.ph.ibm.repository.ProjectEngagementRepository;
import com.ph.ibm.repository.ProjectRepository;
import com.ph.ibm.repository.UtilizationEngagementRepository;
import com.ph.ibm.repository.impl.EmployeeRepositoryImpl;
import com.ph.ibm.repository.impl.HolidayRepositoryImpl;
import com.ph.ibm.repository.impl.PUMYearRepositoryImpl;
import com.ph.ibm.repository.impl.ProjectEngagementRepositoryImpl;
import com.ph.ibm.repository.impl.ProjectRepositoryImpl;
import com.ph.ibm.repository.impl.UtilizationEngagementRepositoryImpl;
import com.ph.ibm.util.CalendarUtils;
import com.ph.ibm.util.ObjectMapperAdapter;
import com.ph.ibm.util.OpumConstants;
import com.ph.ibm.validation.Validator;
import com.ph.ibm.validation.impl.EmployeeValidator;

public class ProjectBO {

    /**
     * EmployeeRepository is a Data Access Object which contain methods to add, register, login, view, validate field/s
     * stored in employee table - opum database
     */
    private EmployeeRepository employeeRepository = new EmployeeRepositoryImpl();

    /**
     * ProjectRepository is a Data Access Object which contain method to retrieve fields stored in project table - opum
     * database
     */
    private ProjectRepository projectRepository = new ProjectRepositoryImpl();

    /**
     * ProjectEngagementRepository is a Data Access Object which contain method to add, save, get, check field/s stored
     * in project_engagement table - opum database
     */
    private ProjectEngagementRepository projectEngagementRepository = new ProjectEngagementRepositoryImpl();

    /**
     * UtilizationEngagementRepository is a Data Access Object which contains method to retrieve data from
     * Utilization_JSON
     */

    private UtilizationEngagementRepository utilizationEngagementRepository = new UtilizationEngagementRepositoryImpl();

    /**
     * Validation contain methods to validate field such as employee name, employee id, project name, email address
     */
    private Validator<Employee> validator = new EmployeeValidator();

    /**
     * Logger is used to document the execution of the system and logs the corresponding log level such as INFO, WARN,
     * ERROR
     */
    private Logger logger = Logger.getLogger( ProjectBO.class );

    private PUMYearRepository pumYearRepository = new PUMYearRepositoryImpl();

    private HolidayEngagementRepository holidayEngagementRepository = new HolidayRepositoryImpl();

    /**
     * @param pumYear
     * @return String
     * @throws SQLException
     * @throws ParseException
     */
    public String saveYear( PUMYear pumYear ) throws SQLException, ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat( "YYYY-MM-DD" );
        boolean value = false;
        if( pumYear.getStart() != null ){
            if( pumYear.getEnd() != null ){
                value = pumYearRepository.saveYear( pumYear );
                if( value ){
                    logger.info( "END saveYear" );
                    return OpumConstants.SUCCESSFULLY_SAVED;
                }
                else{
                    logger.info( "END saveYear" );
                    return OpumConstants.ERROR_WHEN_SAVING;
                }
            }
            else{
                return OpumConstants.ERROR_END_DATE;
            }
        }
        else{
            return OpumConstants.YEAR_START_NOTFOUND;
        }
    }

    /**
     * @param pumYear
     * @return String
     * @throws SQLException
     * @throws ParseException
     */
    public String editYear( PUMYear pumYear ) throws SQLException, ParseException {
        boolean value = false;
        if( pumYear.getStart() != null ){
            if( pumYear.getEnd() != null ){
                value = pumYearRepository.editYear( pumYear );
                if( value ){
                    logger.info( "END editYear" );
                    return OpumConstants.UPDATED_SUCCESS;
                }
                else{
                    logger.info( "END editYear" );
                    return OpumConstants.ERROR_WHEN_SAVING;
                }
            }
            else{
                return OpumConstants.ERROR_END_DATE;
            }
        }
        else{
            return OpumConstants.YEAR_START_NOTFOUND;
        }

    }

    public String updateHoliday( Holiday holiday ) throws SQLException, ParseException {
        boolean value = false;
        if( holiday.getName() != null ){
            if( holiday.getDate() != null ){
                value = holidayEngagementRepository.updateHolidayEngagement( holiday );
                if( value ){
                    logger.info( "END updateHoliday" );
                    return OpumConstants.UPDATED_SUCCESS;
                }
                else{
                    logger.info( "END updateHoliday" );
                    return OpumConstants.ERROR_WHEN_SAVING;
                }
            }
            else{
                return OpumConstants.ERROR_END_DATE;
            }
        }
        else{
            return OpumConstants.YEAR_START_NOTFOUND;
        }
    }

    /**
     * This method is used to save employee start and end date
     * 
     * @param employeeUtil
     * @return String
     * @throws SQLException
     * @throws ParseException
     */
    public String saveDate( EmployeeUtil employeeUtil ) throws SQLException, ParseException {
        String employeeId = employeeRepository.viewEmployee( employeeUtil.getEmployeeIdNumber() );
        Long projectEngagementId = -1L;
        List<Project> projectList = new ArrayList<Project>();
        projectList = projectRepository.retrieveData();
        ProjectEngagement projectEngagement = new ProjectEngagement();
        boolean valid = false;
        for( Project project : projectList ){
            if( project.getProjectName().equals( employeeUtil.getProjectName() ) ){
                valid = true;
                projectEngagement.setProjectId( project.getProjectId() );
            }
        }
        projectEngagement.setEmployeeId( employeeId );

        DateFormat df = new SimpleDateFormat( "yyyy-MM-dd" );
        projectEngagement.setStartDate( new java.sql.Date( df.parse( employeeUtil.getStartDate() ).getTime() ) );
        projectEngagement.setEndDate( new java.sql.Date( df.parse( employeeUtil.getEndDate() ).getTime() ) );
        projectEngagementId = projectEngagementRepository.getProjectEngagementId( projectEngagement.getProjectId(),
            projectEngagement.getEmployeeId() );

        if( projectEngagementId == -1 ){
            return OpumConstants.PROJECT_ENGAGEMENT_NOT_FOUND;
        }
        else{
            projectEngagement.setProjectEngagementId( projectEngagementId );
            logger.info( "END saveDate" );
            return projectEngagementRepository.saveDate( projectEngagement ) ? OpumConstants.SUCCESSFULLY_SAVED
                            : OpumConstants.ERROR_WHEN_SAVING;
        }
    }

    /**
     * @param rawData
     * @param uriInfo
     * @return Response
     * @throws Exception
     */
    public Response uploadEmployeeList( String rawData, @Context UriInfo uriInfo ) throws Exception {

        ProjectEngagement projectEngagement = new ProjectEngagement();
        List<List<String>> rows = populateEmployeeProjectEngagements( rawData );
        List<Employee> validatedEmployee = new ArrayList<Employee>();
        try{
            for( List<String> row : rows ){
                Employee validateEmployee = validateEmployee( uriInfo, row );
                validatedEmployee.add( validateEmployee );
                System.out.println( "Row Data: " + row );
            }

            for( Employee employee : validatedEmployee ){
                Employee savedEmployee = employeeRepository.saveOrUpdate( employee );
                if( savedEmployee != null ){
                    List<Project> projectdata = projectRepository.retrieveData();
                    for( Project project : projectdata ){
                        // if (!project.getProjectName().equals(validatedEmployee.get(3))) {
                        // TODO return invalidCsvResponseBuilder(uriInfo, employeeProjectEngagement,
                        // OpumConstants.INVALID_PROJECT_NAME);
                        // }

                        projectEngagement.setProjectId( project.getProjectId() );
                    }

                    projectEngagement.setEmployeeId( employeeRepository.viewEmployee( employee.getEmployeeSerial() ) );
                    projectEngagementRepository.addProjectEngagement( projectEngagement );
                }

            }
        }
        catch( BatchUpdateException e ){
            logger.error( "BatchUpdateException due to " + e.getMessage() );
            System.out.println( e.getErrorCode() );
            // if (e.getErrorCode() == OpumConstants.MYSQL_DUPLICATE_PK_ERROR_CODE) {
            return Response.status( 206 ).header( "Location", uriInfo.getBaseUri() + "employee/" ).entity(
                OpumConstants.DUPLICATE_ENTRY ).build();
            // return invalidCsvResponseBuilder(uriInfo, employeeProjectEngagement,
            // OpumConstants.DUPLICATE_ENTRY);
            // }
        }
        catch( InvalidEmployeeException e ){
            logger.error( OpumConstants.INVALID_CSV );
            return Response.status( 206 ).header( "Location", uriInfo.getBaseUri() + "employee/" ).entity(
                OpumConstants.INVALID_CSV ).build();
            // invalidCsvResponseBuilder(uriInfo, employeeProjectEngagement,
            // e.getMessage());
        }
        catch( SQLException e ){
            logger.error( "SQL Exception due to " + e.getMessage() );
            e.printStackTrace();
            return Response.status( 206 ).header( "Location", uriInfo.getBaseUri() + "employee/" ).entity(
                OpumConstants.SQL_ERROR ).build();
            // OpumConstants.SQL_ERROR
        }

        logger.info( OpumConstants.SUCCESSFULLY_UPLOADED_FILE );
        return Response.status( Status.OK ).header( "Location", uriInfo.getBaseUri() + "employee/" ).entity(
            "uploaded successfully" ).build();

    }

    /**
     * @param rawData
     * @param uriInfo
     * @return Response
     * @throws Exception
     */
    public Response uploadAdminEmployeeList( String rawData, @Context UriInfo uriInfo ) throws Exception {

        ProjectEngagement projectEngagement = new ProjectEngagement();
        List<List<String>> rows = populateEmployeeProjectEngagements( rawData );
        List<Employee> validatedEmployee = new ArrayList<Employee>();
        try{
            for( List<String> row : rows ){
                Employee validateEmployee = validateEmployee( uriInfo, row );
                validatedEmployee.add( validateEmployee );
                System.out.println( "Row Data: " + row );
            }
            for( Employee employee : validatedEmployee ){
                Employee savedEmployee = employeeRepository.saveOrUpdate( employee );
                if( savedEmployee != null ){
                    List<Project> projectdata = projectRepository.retrieveData();
                    for( Project project : projectdata ){
                        // if (!project.getProjectName().equals(validatedEmployee.get(3))) {
                        // TODO return invalidCsvResponseBuilder(uriInfo, employeeProjectEngagement,
                        // OpumConstants.INVALID_PROJECT_NAME);
                        // }

                        projectEngagement.setProjectId( project.getProjectId() );
                    }

                    projectEngagement.setEmployeeId( employeeRepository.viewEmployee( employee.getEmployeeSerial() ) );
                    projectEngagementRepository.addProjectEngagement( projectEngagement );
                }
            }
        }
        catch( BatchUpdateException e ){
            logger.error( "BatchUpdateException due to " + e.getMessage() );
            System.out.println( e.getErrorCode() );
            // if (e.getErrorCode() == OpumConstants.MYSQL_DUPLICATE_PK_ERROR_CODE) {
            return Response.status( 206 ).header( "Location", uriInfo.getBaseUri() + "employee/" ).entity(
                OpumConstants.DUPLICATE_ENTRY ).build();
            // return invalidCsvResponseBuilder(uriInfo, employeeProjectEngagement,
            // OpumConstants.DUPLICATE_ENTRY);
            // }
        }
        catch( InvalidEmployeeException e ){
            logger.error( OpumConstants.INVALID_CSV );
            return Response.status( 206 ).header( "Location", uriInfo.getBaseUri() + "employee/" ).entity(
                OpumConstants.INVALID_CSV ).build();
            // invalidCsvResponseBuilder(uriInfo, employeeProjectEngagement,
            // e.getMessage());
        }
        catch( InvalidCSVException e ){
            logger.error( OpumConstants.INVALID_CSV );
            // return Response.status(206).header("Location", uriInfo.getBaseUri() +
            // "employee/")
            // .entity(OpumConstants.INVALID_CSV).build();
            return invalidCsvResponseBuilder( uriInfo, e.getEmployee(), OpumConstants.INVALID_CSV );
        }
        catch( SQLException e ){
            logger.error( "SQL Exception due to " + e.getMessage() );
            e.printStackTrace();
            return Response.status( 206 ).header( "Location", uriInfo.getBaseUri() + "employee/" ).entity(
                OpumConstants.SQL_ERROR ).build();
            // OpumConstants.SQL_ERROR
        }

        logger.info( OpumConstants.SUCCESSFULLY_UPLOADED_FILE );
        return Response.status( Status.OK ).header( "Location", uriInfo.getBaseUri() + "employee/" ).entity(
            "uploaded successfully" ).build();
    }

    /**
     * @param rawData
     * @param uriInfo
     * @return Response
     * @throws Exception
     */

    private Employee validateEmployee( UriInfo uriInfo, List<String> row ) throws Exception {

        if( row == null || row.isEmpty() ){
            throw new InvalidEmployeeException( OpumConstants.INVALID_CSV );
        }
        Employee validateEmployee = new Employee();
        validateEmployee.setEmployeeSerial( row.get( 1 ) );
        validateEmployee.setFullName( row.get( 8 ) );
        validateEmployee.setIntranetId( row.get( 2 ) );
        validator.validate( validateEmployee );
        return validateEmployee;
    }

    private List<List<String>> populateEmployeeProjectEngagements( String rawData ) {
        List<List<String>> employeeProjectEngagements = new ArrayList<List<String>>();
        List<String> row;
        String delimiter = ",";
        Scanner sc = new Scanner( rawData );
        ignoreFirstRow( sc );
        while( sc.hasNextLine() ){
            String line = sc.nextLine();
            if( !isRowEmpty( line ) && !line.startsWith( "----" ) ){
                row = Arrays.asList( line.split( delimiter ) );
                employeeProjectEngagements.add( row );
            }
        }
        sc.close();
        return employeeProjectEngagements;
    }

    private void ignoreFirstRow( Scanner sc ) {
        while( sc.hasNextLine() ){
            String line = sc.nextLine();
            if( isRowEmpty( line ) ){
                sc.nextLine();
                break;
            }
        }
    }

    private boolean isRowEmpty( String line ) {
        return line == null || line.equals( "\\n" ) || line.equals( "" );
    }

    private Response invalidCsvResponseBuilder( UriInfo uriInfo, Employee employee, String errorMessage ) {
        String invalidCsv;
        invalidCsv = String.format( "Invalid CSV for employee: %s, %s, %s \n", employee.getEmployeeSerial(),
            employee.getIntranetId(),
            employee.getFullName(), uriInfo );
        return Response.status( 206 ).header( "Location", uriInfo.getBaseUri() + "employee/" ).entity(
            invalidCsv ).build();
    }

    public Year getComputation( String employeeId, int year ) throws SQLException, ParseException {
        Utilization utilization = utilizationEngagementRepository.getComputation( employeeId, year );
        UtilizationYear utilization_Year =
            ObjectMapperAdapter.unmarshal( utilization.getUtilizationJson(), UtilizationYear.class );
        DecimalFormat formatter = new DecimalFormat( "#0.00" );
        double hours = 0;
        double weekHours = 0;
        double monthHours = 0;
        double quarterHours = 0;
        double monthVLCount = 0;
        double monthSLCount = 0;
        double monthOLCount = 0;
        int weekCounter = 0;
        int monthCounter = 0;
        int quarterCounter = 0;

        Year yearCalculation = new Year();
        yearCalculation.setQuarters( new ArrayList<Quarter>() );
        yearCalculation.getQuarters().add( new Quarter() );

        final Quarter quarterOfYear = yearCalculation.getQuarters().get( quarterCounter );
        quarterOfYear.setMonths( new ArrayList<Month>() );
        quarterOfYear.getMonths().add( new Month() );

        Month monthOfQuarter = quarterOfYear.getMonths().get( monthCounter );
        monthOfQuarter.setWeeks( new ArrayList<Week>() );

        for( UtilizationJson json : utilization_Year.getUtilizationJSON() ){
            //
            final String utilizationCellValue = json.getUtilizationHours();
            if( utilizationCellValue.equals( "VL" ) || utilizationCellValue.equals( "SL" ) ||
                utilizationCellValue.equals( "OL" ) || utilizationCellValue.equals( "EL" ) ||
                utilizationCellValue.equals( "HO" ) || utilizationCellValue.equals( "TR" ) ||
                utilizationCellValue.equals( "CDO" ) || utilizationCellValue.equals( "" ) ){

                hours = 0;
            }
            else if( utilizationCellValue != null ){
                hours = Integer.parseInt( utilizationCellValue );
            }

            if( utilizationCellValue.equals( "VL" ) ){
                monthVLCount++;
            }
            else if( utilizationCellValue.equals( "SL" ) ){
                monthSLCount++;
            }
            else if( utilizationCellValue.equals( "OL" ) ){
                monthOLCount++;
            }

            // Compute hours per week
            final List<Week> weeksOfMonth = monthOfQuarter.getWeeks();
            if( json.getDay() != 7 ){
                weekHours = weekHours + hours;
                System.out.println( "Total hours per day: " + hours );
            }
            else{
                System.out.println( "Total hours per day: " + hours );
                System.out.println( "  TOTAL HOURS PER WEEK: " + weekHours );
                weeksOfMonth.add( new Week() );
                weeksOfMonth.get( weekCounter ).setTotalHours( weekHours );
                weeksOfMonth.get( weekCounter ).setWeekEndingDate( json.getMonth() + "/" + json.getDayOfMonth() );
                monthHours = monthHours + weekHours;
                weekHours = 0;
                weekCounter++;
            }

            // Compute all data per month
            final boolean isQ1 = json.getMonth() >= 4 && json.getDayOfMonth() == 1;
            final boolean isQ2 = json.getMonth() >= 7 && json.getDayOfMonth() == 1;
            final boolean isQ3 = json.getMonth() >= 10 && json.getDayOfMonth() == 1;
            final boolean isQ4 = json.getMonth() == 12 && json.getDayOfMonth() == 31;
            boolean isValidCalendarQuarter = isQ1 || isQ2 || isQ3 || isQ4;
            if( weekCounter == 5 && isValidCalendarQuarter ){
                // Process a five-week month
                final int FIVE_WEEK_TOTAL_HOURS = 200;
                double MTD5 = ( ( monthHours / FIVE_WEEK_TOTAL_HOURS ) * 100 );
                monthOfQuarter.setTotalHours( monthHours );
                monthOfQuarter.setNumberOfVL( monthVLCount );
                monthOfQuarter.setNumberOfSL( monthSLCount );
                monthOfQuarter.setNumberOfOL( monthOLCount );
                monthOfQuarter.setNumberOfAvailableHours( FIVE_WEEK_TOTAL_HOURS );
                monthOfQuarter.setMonthToDateUtilization( Double.parseDouble( formatter.format( MTD5 ) ) );

                // Months
                monthOfQuarter.setName(CalendarUtils.getMonthString(json.getMonth(), TextStyle.FULL));

                getComputationSysouts( monthHours, monthVLCount, monthSLCount, monthOLCount, MTD5 );

                quarterHours = quarterHours + monthHours;
                monthHours = 0;
                monthVLCount = 0;
                monthSLCount = 0;
                monthOLCount = 0;
                quarterOfYear.getMonths().add( new Month() );
                monthCounter++;
                monthOfQuarter.setWeeks( new ArrayList<Week>() );
                weeksOfMonth.add( new Week() );
                weekCounter = 0;

            }
            else if( weekCounter == 4 ){
                // Process a four-week month
                final int FOUR_WEEK_TOTAL_HOURS = 160;
                double MTD4 = ( ( monthHours / FOUR_WEEK_TOTAL_HOURS ) * 100 );
                monthOfQuarter.setTotalHours( monthHours );
                monthOfQuarter.setNumberOfVL( monthVLCount );
                monthOfQuarter.setNumberOfSL( monthSLCount );
                monthOfQuarter.setNumberOfOL( monthOLCount );
                monthOfQuarter.setNumberOfAvailableHours( FOUR_WEEK_TOTAL_HOURS );
                monthOfQuarter.setMonthToDateUtilization( Double.parseDouble( formatter.format( MTD4 ) ) );

                monthOfQuarter.setName(CalendarUtils.getMonthString(json.getMonth(), TextStyle.FULL));

                getComputationSysouts( monthHours, monthVLCount, monthSLCount, monthOLCount, MTD4 );

                quarterHours = quarterHours + monthHours;
                monthHours = 0;
                monthVLCount = 0;
                monthSLCount = 0;
                monthOLCount = 0;
                quarterOfYear.getMonths().add( new Month() );
                monthCounter++;
                monthOfQuarter.setWeeks( new ArrayList<Week>() );
                weeksOfMonth.add( new Week() );
                weekCounter = 0;
            }

            // Compute all data per quarter
            // TODO: validation
            // ?
            if( isValidCalendarQuarter ){
                double QTD = ( ( quarterHours / 560 ) * 100 );
                quarterOfYear.setTotalHours( quarterHours );
                quarterOfYear.setNumberOfAvailableHours( 560 );
                quarterOfYear.setQuarterToDateUtilization( Double.parseDouble( formatter.format( QTD ) ) );

                if( isQ1 ){
                    quarterOfYear.setName( "1st Quarter" );
                }
                else if( isQ2 ){
                    quarterOfYear.setName( "2nd Quarter" );
                }
                else if( isQ3 ){
                    quarterOfYear.setName( "3rd Quarter" );
                }
                else if( isQ4 ){
                    quarterOfYear.setName( "4th Quarter" );
                }

                System.out.println( "Total hours per quarter:" + quarterHours );
                System.out.println( "QTD: " + QTD + "%" );
                quarterHours = 0;
                yearCalculation.getQuarters().add( new Quarter() );
                quarterCounter++;
                quarterOfYear.setMonths( new ArrayList<Month>() );
                quarterOfYear.getMonths().add( new Month() );
                monthCounter = 0;
                monthOfQuarter.setWeeks( new ArrayList<Week>() );
                weeksOfMonth.add( new Week() );
                weekCounter = 0;
            }
        }
        return yearCalculation;
    }

    private void getComputationSysouts( double monthHours, double monthVLCount, double monthSLCount,
                                        double monthOLCount, double MTD5 ) {
        System.out.println( "     TOTAL HOURS PER MONTH: " + monthHours );
        System.out.println( "Total VLs per month: " + monthVLCount );
        System.out.println( "Total SLs per month: " + monthSLCount );
        System.out.println( "Total OLs per month: " + monthOLCount );
        System.out.println( "MTD: " + MTD5 + "%" );
    }

    /**
     * @return List
     * @throws SQLException
     */
    public List<Project> retrieveData() throws SQLException {
        return projectRepository.retrieveData();
    }

    /**
     * @param employeeSerial
     * @param year
     * @return Response
     * @throws SQLException
     * @throws ParseException
     */
    public Response getYTDComputation( String employeeSerial, int year ) throws SQLException, ParseException {
        Utilization utilization = utilizationEngagementRepository.getComputation( employeeSerial, year );
        UtilizationYear utilization_Year =
            ObjectMapperAdapter.unmarshal( utilization.getUtilizationJson(), UtilizationYear.class );

        double hours = 0;
        double VLcount = 0;
        double SLcount = 0;
        double ELcount = 0;
        double OLcount = 0;
        double HOcount = 0;
        double TRcount = 0;
        double CDOcount = 0;
        for( UtilizationJson json : utilization_Year.getUtilizationJSON() ){
            if( json.getUtilizationHours().equals( "VL" ) || json.getUtilizationHours().equals( "SL" ) ||
                json.getUtilizationHours().equals( "OL" ) || json.getUtilizationHours().equals( "EL" ) ||
                json.getUtilizationHours().equals( "HO" ) || json.getUtilizationHours().equals( "TR" ) ||
                json.getUtilizationHours().equals( "CDO" ) || json.getUtilizationHours().equals( "" ) ){

                hours = 0;
            }
            else if( json.getUtilizationHours() != null ){
                hours = Integer.parseInt( json.getUtilizationHours() );

            }
            if( json.getUtilizationHours().equals( "VL" ) ){
                VLcount++;
            }
            if( json.getUtilizationHours().equals( "SL" ) ){
                SLcount++;
            }
            if( json.getUtilizationHours().equals( "OL" ) ){
                OLcount++;
            }
            if( json.getUtilizationHours().equals( "EL" ) ){
                ELcount++;
            }
            if( json.getUtilizationHours().equals( "CDO" ) ){
                CDOcount++;
            }
            if( json.getUtilizationHours().equals( "TR" ) ){
                TRcount++;
            }
            if( json.getUtilizationHours().equals( "HO" ) ){
                HOcount++;
            }

        }
        return null;
    }

    public String saveQuarter( PUMQuarter pumQuarter ) throws SQLException, ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat( "YYYY-MM-DD" );
        boolean value = false;
        if( pumQuarter.getStart() != null ){
            if( pumQuarter.getEnd() != null ){
                value = pumYearRepository.saveQuarter( pumQuarter );
                if( value ){
                    logger.info( "END saveQuarter" );
                    return OpumConstants.SUCCESSFULLY_SAVED;
                }
                else{
                    logger.info( "END saveQuarter" );
                    return OpumConstants.ERROR_WHEN_SAVING;
                }
            }
            else{
                return OpumConstants.ERROR_END_DATE;
            }
        }
        else{
            return OpumConstants.ERROR;
        }
    }

    public String saveMonth( PUMMonth pumMonth ) throws SQLException, ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat( "YYYY-MM-DD" );
        boolean value = false;
        if( pumMonth.getStart() != null ){
            if( pumMonth.getEnd() != null ){
                value = pumYearRepository.saveMonth( pumMonth );
                if( value ){
                    logger.info( "END saveMonth" );
                    return OpumConstants.SUCCESSFULLY_SAVED;
                }
                else{
                    logger.info( "END saveMonth" );
                    return OpumConstants.ERROR_WHEN_SAVING;
                }
            }
            else{
                return OpumConstants.ERROR_END_DATE;
            }
        }
        else{
            return OpumConstants.YEAR_START_NOTFOUND;
        }
    }
}
