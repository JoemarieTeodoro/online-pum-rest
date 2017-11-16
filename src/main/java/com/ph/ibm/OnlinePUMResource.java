package com.ph.ibm;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.apache.log4j.Logger;

import com.ph.ibm.bo.EmployeeBO;
import com.ph.ibm.bo.HolidayBO;
import com.ph.ibm.bo.ProjectBO;
import com.ph.ibm.bo.UtilityBO;
import com.ph.ibm.bo.YearBO;
import com.ph.ibm.model.Employee;
import com.ph.ibm.model.EmployeeUpdate;
import com.ph.ibm.model.EmployeeUtil;
import com.ph.ibm.model.Holiday;
import com.ph.ibm.model.HolidayList;
import com.ph.ibm.model.PUMMonth;
import com.ph.ibm.model.PUMQuarter;
import com.ph.ibm.model.PUMYear;
import com.ph.ibm.model.PUMYearList;
import com.ph.ibm.model.Project;
import com.ph.ibm.model.Utilization;
import com.ph.ibm.model.Year;
import com.ph.ibm.opum.exception.OpumException;
import com.ph.ibm.upload.Uploader;
import com.ph.ibm.upload.upload.impl.AdminListUploader;
import com.ph.ibm.upload.upload.impl.PEMListUploader;
import com.ph.ibm.util.Authenticate;
import com.ph.ibm.util.OpumConstants;

/**
 * Root resource (exposed at "opum" path) This class is an end point called by the client. The methods are exposed as a
 * Web Service.
 *
 * @author Claude
 * @author Christian
 * @author Nino
 * @author Jose
 * @author Aldaina
 * @author Raissa
 * @author Kristel
 * @version 5.0
 */
@Path( "opum" )
public class OnlinePUMResource {

    /**
     * EmployeeBO is a business object layer which handle validations and invoke method/s from the DAO
     */
    private EmployeeBO employeeBO;

    /**
     * ProjectBO is a business object layer which handle validations and invoke method/s from the DAO
     */
    private ProjectBO projectBO;

    /**
     * Service used for uploading project information
     */
    private Uploader uploader;

    /**
     * UtilityBO is a business object layer which handle validations and invoke method/s from the DAO
     */
    private UtilityBO utilityBO;

    /**
     * YearBO is a business object layer which handle validations and invoke method/s from the DAO
     */
    private YearBO yearBO;

    /**
     * HolidayBO is a business object layer which handle validations and invoke method/s from the DAO
     */
    private HolidayBO holidayBO;

    /**
     * Logger is used to document the execution of the system and logs the corresponding log level such as INFO, WARN,
     * ERROR
     */
    private Logger logger = Logger.getLogger( OnlinePUMResource.class );

    /**
     * <b>Class Constructor to initialize <u>Property Configurator</U> for logging</b>
     */
    public OnlinePUMResource() {
        super();
        ResourceBundle resource = ResourceBundle.getBundle( "log4j" );
        // PropertyConfigurator.configure(
        // getClass().getProtectionDomain().getCodeSource().getLocation().getPath() +
        // "log4j.properties");
    }

    /**
     * This method is used to authenticate user email and password before accessing a service
     *
     * @param email - this is user email address
     * @param password - this is user password
     * @return <b>boolean</b> - return true if email and password exists in employee table ======= //
     *         PropertyConfigurator.configure( //
     *         getClass().getProtectionDomain().getCodeSource().getLocation().getPath() + "log4j.properties"); } /**
     *         This method is used to authenticate user email and password before accessing a service
     * @throws Exception
     */
    public boolean authenticateUser( String email, String password ) throws Exception {
        return new Authenticate().check( email, password );
    }

    /**
     * This service is invoked when a user register <br>
     * Exposed at "opum/registration" path
     *
     * @param employeeIdNumber - this is the user employee serial number
     * @param projectName - this is the project name of the user
     * @param email - the is the email address of the user
     * @param password - this is the password of the user
     * @return <b>String</b> - holds a message if a user registration succeed or not
     * @throws Exception
     */
    @Path( "/registration" )
    @POST
    @Consumes( MediaType.APPLICATION_FORM_URLENCODED )
    @Produces( MediaType.TEXT_PLAIN )
    public String registerEmployee( @FormParam( "employeeIdNumber" ) String employeeIdNumber,
                                    @FormParam( "projectName" ) String projectName, @FormParam( "email" ) String email,
                                    @FormParam( "password" ) String password )
        throws Exception {
        logger.info( "START registerEmployee" );
        String message = null;
        try{
            employeeBO = new EmployeeBO();
            message = employeeBO.registerEmployee( employeeIdNumber, projectName, email, password );
        }
        catch( Exception e ){
            logger.error( e );
            throw new OpumException( e.getMessage(), e );
        }
        logger.info( "END registerEmployee" );
        return message;
    }

    /**
     * This service is invoked when administrator upload file <br>
     * <br>
     * Exposed at "opum/dataLoading" path ======= <br>
     * <br>
     * 
     * @throws OpumException
     */
    @Path( "/dataLoading" )
    @POST
    @Consumes( MediaType.MULTIPART_FORM_DATA )
    @Produces( MediaType.TEXT_PLAIN )
    public Response uploadEmployeeList( String rawData, @Context UriInfo uriInfo ) throws SQLException, OpumException {
        logger.info( "START uploadEmployeeList" );
        Response response;
        try{
            uploader = new AdminListUploader();
            response = uploader.upload( rawData, uriInfo );
        }
        catch( Exception e ){
            logger.error( e );
            throw new OpumException( e.getMessage(), e );
        }
        logger.info( "END uploadEmployeeList" );
        return response;
    }
    
    /**
     * This service is invoked when administrator upload file <br>
     * <br>
     * Exposed at "opum/dataLoading" path ======= <br>
     * <br>
     * 
     * @throws OpumException
     */
    @Path( "/pem" )
    @POST
    @Consumes( MediaType.MULTIPART_FORM_DATA )
    @Produces( MediaType.TEXT_PLAIN )
    public Response uploadPEMList( String rawData, @Context UriInfo uriInfo ) throws SQLException, OpumException {
        logger.info( "START uploadPEMList" );
        Response response;
        try{
            uploader = new PEMListUploader();
            response = uploader.upload( rawData, uriInfo );
        }
        catch( Exception e ){
            logger.error( e );
            throw new OpumException( e.getMessage(), e );
        }
        logger.info( "END uploadPEMList" );
        return response;
    }

    /**
     * This service is invoked when super administrator upload csv file of admin <br>
     * <br>
     * Exposed at "opum/adminDataLoading" path
     * 
     * @param rawData - JSON file e.g. (.xlsx, .xls, .csv)
     * @param uriInfo - used to obtain information about URI in Response
     * @return <b>Response</b> - object that contains the HTTP Response
     * @throws OpumException
     */
    @Path( "/adminDataLoading" )
    @POST
    @Consumes( MediaType.MULTIPART_FORM_DATA )
    @Produces( MediaType.TEXT_PLAIN )
    public Response uploadAdminEmployeeList( String rawData, @Context UriInfo uriInfo )
        throws SQLException, OpumException {
        logger.info( "START uploadAdminEmployeeList" );
        Response response;
        try{
            projectBO = new ProjectBO();
            response = uploader.upload( rawData, uriInfo );
        }
        catch( Exception e ){
            logger.error( e );
            throw new OpumException( e.getMessage(), e );
        }
        logger.info( "END uploadAdminEmployeeList" );
        return response;
    }

    /**
     * This service is invoked when user log-in <br>
     * <br>
     * Exposed at "opum/userLogin" path
     * 
     * @param username - this is the email address of the user
     * @param password - this is the password of the user
     * @return <b>Response</b> - object that contains the http response
     * @throws SQLException
     * @throws OpumException
     */
    @Path( "/userLogin/{username}" )
    @POST
    @Produces( MediaType.APPLICATION_JSON )
    public Response loginEmployee( @PathParam( "username" ) String username, String password )
        throws SQLException, OpumException {
        logger.info( "START loginEmployee" );
        Employee employee = null;
        try{
            employeeBO = new EmployeeBO();
            employee = employeeBO.loginEmployee( username, password );
            logger.info( "END loginEmployee" );
            return Response.status( 201 ).entity( employee ).build();
        }
        catch( Exception e ){
            logger.error( e );
            throw new OpumException( e.getMessage(), e );
        }
    }

    /**
     * This service is invoked when user view a utilization <br>
     * Exposed at "opum/utilization/{employeeId}/{year}" path
     *
     * @param employeeId - this is the user input employee id number
     * @param year - this is user input year - (YYYY)
     * @param header - this contains the HTTP request header - username and password
     * @return <b>Response</b> - object that contains the http response
     * @throws Exception
     */
    @GET
    @Path( "/utilization/{employeeIdNumber}/{year}" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response fetchUtilization( @PathParam( "employeeIdNumber" ) String employeeIdNumber,
                                      @PathParam( "year" ) String year, @Context HttpHeaders header )
        throws Exception {
        /*
         * MultivaluedMap<String, String> headerParams = header.getRequestHeaders();
         * String email = headerParams.getFirst("username"); String password =
         * headerParams.getFirst("password"); if (!(authenticateUser(email, password)))
         * { logger.error(OpumConstants.UNAUTHORIZED); return
         * Response.status(Status.UNAUTHORIZED).build(); }
         */
        logger.info( "START fetchUtilization" );
        String result;
        try{
            utilityBO = new UtilityBO();
            result = utilityBO.fetchUtilizations( employeeIdNumber, year );
        }
        catch( Exception e ){
            logger.error( e );
            throw new OpumException( e.getMessage(), e );
        }
        logger.info( "END fetchUtilization" );
        return Response.status( Response.Status.OK ).entity( result ).type( MediaType.APPLICATION_JSON ).build();
    }

    /**
     * This service is invoked when user save utilization <br>
     * <br>
     * Exposed at "opum/utilization/{employeeId}/{year}" path
     *
     * @param rawData - JSON object
     * @param employeeId - this is the user input employee id number
     * @param year - this is user input year - (YYYY)
     * @param header - this contains the HTTP request header - username and password
     * @return <b>String</b> - holds a message if a user save succeed or not ======= - this contains the HTTP request
     *         header - username and password
     * @throws Exception
     */
    @POST
    @Path( "/utilization/{employeeIdNumber}/{year}" )
    @Consumes( MediaType.APPLICATION_JSON )
    @Produces( MediaType.TEXT_PLAIN )
    public String saveUtilization( String rawData, @PathParam( "employeeIdNumber" ) String employeeIdNumber,
                                   @PathParam( "year" ) String year, @Context HttpHeaders header )
        throws Exception {
        /*
         * MultivaluedMap<String, String> headerParams = header.getRequestHeaders();
         * String email = headerParams.getFirst("username"); String password =
         * headerParams.getFirst("password"); if (!(authenticateUser(email, password)))
         * { logger.error(OpumConstants.UNAUTHORIZED); return
         * Response.Status.UNAUTHORIZED.toString(); }
         */
        logger.info( "START saveUtilization" );
        boolean status;
        try{
            Utilization utilization = new Utilization( employeeIdNumber, year, rawData );
            utilityBO = new UtilityBO();
            status = utilityBO.saveUtilization( utilization );
        }
        catch( Exception e ){
            throw new OpumException( e.getMessage(), e );
        }
        logger.info( "END saveUtilization" );
        return status ? Response.Status.ACCEPTED.toString() : Response.Status.INTERNAL_SERVER_ERROR.toString();
    }

    /**
     * This service is invoked when user retrieve data <br>
     * <br>
     * Exposed at "opum/projectList" path
     *
     * @param header - this contains the HTTP request header - username and password
     * @return <b>JSON</b> - List of Project
     * @throws Exception
     */
    @GET
    @Path( "/projectList" )
    @Produces( MediaType.APPLICATION_JSON )
    public List<Project> retrieveData( @Context HttpHeaders header ) throws Exception {
        /*
         * MultivaluedMap<String, String> headerParams = header.getRequestHeaders();
         * String email = headerParams.getFirst("username"); String password =
         * headerParams.getFirst("password"); if (!(authenticateUser(email, password)))
         * { logger.error(OpumConstants.UNAUTHORIZED); return null; }
         */
        List<Project> projectdata = new ArrayList<Project>();
        logger.info( "START retrieveData" );
        projectBO = new ProjectBO();
        try{
            projectdata = projectBO.retrieveData();
        }
        catch( Exception e ){
            logger.error( e );
            throw new OpumException( e.getMessage(), e );
        }
        logger.info( "END retrieveData" );
        return projectdata;
    }

    /**
     * This service is invoked when admin enter start and end date for the employee <br>
     * <br>
     * Exposed at "opum/saveProjectEngagement" path <br>
     * <br>
     * This method save the roll-in and roll-off dates for each employee in the table
     *
     * @param employeeUtil - Plain Old Java Object
     * @param header - this contains the HTTP request header - username and password
     * @return <b>String</b> - holds a message if a user save succeed or not
     * @throws Exception
     */
    @POST
    @Path( "/saveProjectEngagementDate" )
    @Consumes( MediaType.APPLICATION_JSON )
    @Produces( MediaType.TEXT_PLAIN )
    public String saveDate( EmployeeUtil employeeUtil, @Context HttpHeaders header ) throws Exception {
        /*
         * MultivaluedMap<String, String> headerParams = header.getRequestHeaders();
         * String email = headerParams.getFirst("username"); String password =
         * headerParams.getFirst("password"); if (!(authenticateUser(email, password)))
         * { logger.error(OpumConstants.UNAUTHORIZED); return
         * Response.Status.UNAUTHORIZED.toString(); }
         */
        logger.info( "START saveDate" );
        String message = null;
        try{
            projectBO = new ProjectBO();
            message = projectBO.saveDate( employeeUtil );
        }
        catch( Exception e ){
            logger.error( e );
            throw new OpumException( e.getMessage(), e );
        }
        logger.info( "END saveDate" );
        return message;
    }

    /**
     * This service is invoked when admin save PUM year date <br>
     * <br>
     * Exposed at "opum/savePUMYearDate" path
     *
     * @param pumYear - Plain Old Java Object
     * @param header - this contains the HTTP request header - username and password
     * @return <b>String</b> - holds a message if a user save succeed or not
     * @throws Exception
     */
    @POST
    @Path( "/savePUMYearDate" )
    @Consumes( MediaType.APPLICATION_JSON )
    public String saveYear( PUMYear pumYear, @Context HttpHeaders header ) throws Exception {
        /*
         * MultivaluedMap<String, String> headerParams = header.getRequestHeaders();
         * String email = headerParams.getFirst("username"); String password =
         * headerParams.getFirst("password"); if (!(authenticateUser(email, password)))
         * { logger.error(OpumConstants.UNAUTHORIZED); return
         * Response.Status.UNAUTHORIZED.toString(); }
         */
        logger.info( "START saveYear" );
        String message = null;
        try{
            projectBO = new ProjectBO();
            message = projectBO.saveYear( pumYear );
        }
        catch( Exception e ){
            logger.error( e );
            throw new OpumException( e.getMessage(), e );
        }
        logger.info( "END saveYear" );
        return message;
    }

    /**
     * This service is invoked when admin edit year <br>
     * <br>
     * Exposed at "opum/editPUMYearDate" path
     *
     * @param pumYear - Plain Old Java Object
     * @param header - this contains the HTTP request header - username and password
     * @return <b>String</b> - holds a message if a user update succeed or not
     * @throws Exception
     */
    @PUT
    @Path( "/editPUMYearDate" )
    @Consumes( MediaType.APPLICATION_JSON )
    @Produces( MediaType.TEXT_PLAIN )
    public String editYear( PUMYear pumYear, @Context HttpHeaders header ) throws Exception {
        /*
         * MultivaluedMap<String, String> headerParams = header.getRequestHeaders();
         * String email = headerParams.getFirst("username"); String password =
         * headerParams.getFirst("password"); if (!(authenticateUser(email, password)))
         * { logger.error(OpumConstants.UNAUTHORIZED); return
         * Response.Status.UNAUTHORIZED.toString(); }
         */
        logger.info( "START editYEAR" );
        String message = null;
        try{
            projectBO = new ProjectBO();
            message = projectBO.editYear( pumYear );
        }
        catch( Exception e ){
            logger.error( e );
            throw new OpumException( e.getMessage(), e );
        }
        return message;
    }

    /**
     * This service is invoked when user retrieve list of year <br>
     * <br>
     * Exposed at "opum/yearList" path
     * 
     * @param header - this contains the HTTP request header - username and password
     * @return <b>JSON</b> - PUMYearList object
     * @throws Exception
     */
    @GET
    @Path( "/yearList" )
    @Produces( MediaType.APPLICATION_JSON )
    public PUMYearList retrieveYear( @Context HttpHeaders header ) throws Exception {
        /*
         * MultivaluedMap<String, String> headerParams = header.getRequestHeaders();
         * String email = headerParams.getFirst("username"); String password =
         * headerParams.getFirst("password"); if (!(authenticateUser(email, password)))
         * { logger.error(OpumConstants.UNAUTHORIZED); return null; }
         */
        logger.info( "START retrieveYear" );
        yearBO = new YearBO();
        PUMYearList pumYearList = new PUMYearList();
        try{
            pumYearList = yearBO.retrieveYear( pumYearList );
        }
        catch( Exception e ){
            logger.error( e );
            throw new OpumException( e.getMessage(), e );
        }
        logger.info( "END retrieveYear" );
        return pumYearList;
    }

    /**
     * This service is invoke when user retrieve single row of year date <br>
     * <br>
     * Exposed at "opum/yeardate/{year}" path
     *
     * @param year - this is user input year (YYYY)
     * @param header - this contains the HTTP request header - username and password
     * @return <b>Response</b> - object that contains the http response
     * @throws Exception
     */
    @GET
    @Path( "/yeardate/{year}" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response retrieveYearDate( @PathParam( "year" ) int year, @Context HttpHeaders header ) throws Exception {
        /*
         * MultivaluedMap<String, String> headerParams = header.getRequestHeaders();
         * String email = headerParams.getFirst("username"); String password =
         * headerParams.getFirst("password"); if (!(authenticateUser(email, password)))
         * { logger.error(OpumConstants.UNAUTHORIZED); return null; }
         */
        logger.info( "START retrieveYear" );
        yearBO = new YearBO();
        PUMYear yeardate = new PUMYear();
        try{
            yeardate = yearBO.retrieveYearDate( year );
        }
        catch( Exception e ){
            logger.error( e );
            throw new OpumException( e.getMessage(), e );
        }
        logger.info( "END retrieveYear" );
        return Response.status( Status.OK ).entity( yeardate ).build();
    }

    /**
     * This method makes the admin search for an employee and display their information by entering their employee id
     * number in reference with the employee table <br>
     * <br>
     * Exposed at "opum/searchEmployee{companyIdNumber}" path ======= <br>
     * <br>
     * 
     * @param employeeIdNumber - this is the company Id number of an employee
     * @param header - this contains the HTTP request header - username and password
     * @return <b>String</b> - holds a message if the admin has successfully
     * @throws Exception
     */
    @GET
    @Path( "/searchEmployee/{employeeIdNumber}" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response searchEmployee( @PathParam( "employeeIdNumber" ) String employeeIdNumber,
                                    @Context HttpHeaders header )
        throws Exception {
        /*
         * MultivaluedMap<String, String> headerParams = header.getRequestHeaders();
         * String email = headerParams.getFirst("username"); String password =
         * headerParams.getFirst("password"); if (!(authenticateUser(email, password)))
         * { logger.error(OpumConstants.UNAUTHORIZED); return
         * Response.status(Status.UNAUTHORIZED).build(); }
         */
        logger.info( "START searchEmployee" );
        employeeBO = new EmployeeBO();
        EmployeeUpdate employee = null;
        try{
            employee = employeeBO.searchEmployee( employeeIdNumber );
            if( employee != null ){
                logger.info( "END searchEmployee" );
                return Response.status( Status.OK ).entity( employee ).build();
            }
        }
        catch( Exception e ){
            logger.error( e );
            throw new OpumException( e.getMessage() );
        }
        logger.info( "END searchEmployee" );
        return Response.status( Status.INTERNAL_SERVER_ERROR ).entity( OpumConstants.INVALID_EMPLOYEE_ID ).build();
    }

    /**
     * This service is invoked when admin edit or update employee information details <br>
     * <br>
     * 
     * @param employeeUpdate - Plain Old Java Object
     * @param header - this contains the HTTP request header - username and password
     * @return <b>String</b> - holds a message if the admin has successfully updated
     * @throws Exception
     */
    @PUT
    @Path( "/updateEmployeeDetails" )
    @Consumes( MediaType.APPLICATION_JSON )
    @Produces( MediaType.TEXT_PLAIN )
    public String updateDetails( EmployeeUpdate employeeUpdate, @Context HttpHeaders header ) throws Exception {
        /*
         * MultivaluedMap<String, String> headerParams = header.getRequestHeaders();
         * String email = headerParams.getFirst("username"); String password =
         * headerParams.getFirst("password"); if (!(authenticateUser(email, password)))
         * { logger.error(OpumConstants.UNAUTHORIZED); return
         * Response.Status.UNAUTHORIZED.toString(); }
         */
        logger.info( "START updateEmployeeDetails" );
        employeeBO = new EmployeeBO();
        try{
            logger.info( "END updateEmployeeDetails" );
            return employeeBO.updateEmployee( employeeUpdate ) ? Response.Status.OK.toString()
                            : Response.Status.INTERNAL_SERVER_ERROR.toString();
        }
        catch( Exception e ){
            logger.error( e );
            throw new OpumException( e.getMessage(), e );
        }
    }

    /**
     * This service is invoked when the user download excel file <br>
     * Exposed at "opum/downloadUtilization/{year}" path >>>>>>> 4e17786... Squash commit for PUM-003 and PUM-004
     *
     * @param year
     * @param header
     * @return Response
     * @throws Exception
     */
    @GET
    @Path( "/downloadUtilization" )
    @Produces( "application/vnd.ms-excel" )
    public Response downloadUtilization( @QueryParam( "year" ) String year, @Context HttpHeaders header )
        throws Exception {
        /*
         * MultivaluedMap<String, String> headerParams = header.getRequestHeaders();
         * String email = headerParams.getFirst("username"); String password =
         * headerParams.getFirst("password"); if (!(authenticateUser(email, password)))
         * { logger.error(OpumConstants.UNAUTHORIZED); return
         * Response.status(Status.UNAUTHORIZED).build(); }
         */
        Response response;
        try{
            utilityBO = new UtilityBO();
            response = utilityBO.downloadUtilization( year );
        }
        catch( Exception e ){
            logger.error( e );
            throw new OpumException( e.getMessage(), e );
        }
        return response;
    }

    /**
     * This service is invoked when admin insert holidays <br>
     * <br>
     * Exposed at "opum/saveHolidays" path
     *
     * @param holiday - Plain Old Java Object
     * @param header - this contains the HTTP request header - username and password
     * @return <b>String</b> - holds a message if successfully saved data or not
     * @throws Exception
     */
    @POST
    @Path( "/saveHolidays" )
    @Consumes( MediaType.APPLICATION_JSON )
    @Produces( MediaType.TEXT_PLAIN )
    public String addHolidayEngagement( Holiday holiday, @Context HttpHeaders header ) throws Exception {
        /*
         * MultivaluedMap<String, String> headerParams = header.getRequestHeaders();
         * String email = headerParams.getFirst("username"); String password =
         * headerParams.getFirst("password"); if (!(authenticateUser(email, password)))
         * { logger.error(OpumConstants.UNAUTHORIZED); return
         * Response.Status.UNAUTHORIZED.toString(); }
         */
        logger.info( "START addHoliday" );
        try{
            holidayBO = new HolidayBO();
            logger.info( "END addHolidayEngagement" );
            return holidayBO.addHolidayEngagement( holiday ) ? Response.Status.OK.toString()
                            : Response.Status.INTERNAL_SERVER_ERROR.toString();
        }
        catch( Exception e ){
            logger.error( e );
            throw new OpumException( e.getMessage(), e );
        }
    }

    /**
     * This service is invoked when admin update holiday <br>
     * Exposed at "opum/updateHoliday" path
     *
     * @param holiday - Plain Old Java Object
     * @param header - this contains the HTTP request header - username and password
     * @return <b>String</b> - holds a message if successfully updated data or not
     * @throws Exception
     */
    @POST
    @Path( "/updateHoliday" )
    @Consumes( MediaType.APPLICATION_JSON )
    public String updateHoliday( Holiday holiday, @Context HttpHeaders header ) throws Exception {
        /*
         * MultivaluedMap<String, String> headerParams = header.getRequestHeaders();
         * String email = headerParams.getFirst("username"); String password =
         * headerParams.getFirst("password"); if (!(authenticateUser(email, password)))
         * { logger.error(OpumConstants.UNAUTHORIZED); return
         * Response.Status.UNAUTHORIZED.toString(); }
         */
        logger.info( "START updateHoliday" );
        try{
            holidayBO = new HolidayBO();
            logger.info( "END updateEmployeeDetails" );
            return holidayBO.updateHoliday( holiday ) ? Response.Status.OK.toString()
                            : Response.Status.INTERNAL_SERVER_ERROR.toString();
        }
        catch( Exception e ){
            logger.error( e );
            throw new OpumException( e.getMessage(), e );
        }
    }

    /**
     * This service is invoked when admin delete holiday <br>
     * <br>
     * Exposed at "opum/deleteHoliday" path
     *
     * @param holiday - Plain Old Java Object
     * @param header - this contains the HTTP request header - username and password
     * @return <b>String</b> - holds a message if successfully deleted data or not
     * @throws Exception
     */
    @POST
    @Path( "/deleteHoliday" )
    @Consumes( MediaType.APPLICATION_JSON )
    @Produces( MediaType.TEXT_PLAIN )
    public String deleteHoliday( Holiday holiday, @Context HttpHeaders header ) throws Exception {
        /*
         * MultivaluedMap<String, String> headerParams = header.getRequestHeaders();
         * String email = headerParams.getFirst("username"); String password =
         * headerParams.getFirst("password"); if (!(authenticateUser(email, password)))
         * { logger.error(OpumConstants.UNAUTHORIZED); return
         * Response.Status.UNAUTHORIZED.toString(); }
         */
        logger.info( "START deleteHoliday" );
        try{
            holidayBO = new HolidayBO();
            logger.info( "END deleteHoliday" );
            return holidayBO.deleteHoliday( holiday ) ? Response.Status.OK.toString()
                            : Response.Status.INTERNAL_SERVER_ERROR.toString();
        }
        catch( Exception e ){
            logger.error( e );
            throw new OpumException( e.getMessage(), e );
        }
    }

    /**
     * This service is invoked when user view all holidays <br>
     * Exposed at "opum/holidayList" path
     *
     * @param header - this contains the HTTP request header - username and password
     * @return <b>holidayList</b> - this contain Lists of Holiday >>>>>>> 4e17786... Squash commit for PUM-003 and
     *         PUM-004
     * @throws Exception
     */
    @GET
    @Path( "/holidayList" )
    @Produces( MediaType.APPLICATION_JSON )
    public HolidayList getAllHoliday( @Context HttpHeaders header ) throws Exception {
        /*
         * MultivaluedMap<String, String> headerParams = header.getRequestHeaders();
         * String email = headerParams.getFirst("username"); String password =
         * headerParams.getFirst("password"); if (!(authenticateUser(email, password)))
         * { logger.error(OpumConstants.UNAUTHORIZED); return null; }
         */
        logger.info( "START getAllHoliday" );
        HolidayList holidayList = new HolidayList();
        try{
            holidayBO = new HolidayBO();
            holidayList.setHolidayList( holidayBO.getAllHoliday() );
        }
        catch( Exception e ){
            logger.error( e );
            throw new OpumException( e.getMessage(), e );
        }
        logger.info( "END getAllHoliday" );
        return holidayList;
    }

    /**
     * This service is invoked when user display a certain holiday <br>
     * Exposed at "opum/checkHoliday/{name}" path
     *
     * @param name - this is user input holiday name
     * @param header - this contains the HTTP request header - username and password
     * @return <b>Response</b> - object that contains the http response
     * @throws Exception
     */
    @GET
    @Path( "/checkHoliday/{name}" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response checkHolidays( @PathParam( "name" ) String name, @Context HttpHeaders header ) throws Exception {
        /*
         * MultivaluedMap<String, String> headerParams = header.getRequestHeaders();
         * String email = headerParams.getFirst("username"); String password =
         * headerParams.getFirst("password"); if (!(authenticateUser(email, password)))
         * { logger.error(OpumConstants.UNAUTHORIZED); return
         * Response.status(Status.UNAUTHORIZED).build(); }
         */
        logger.info( "START checkHolidays" );
        Holiday holiday = null;
        try{
            holidayBO = new HolidayBO();
            holiday = holidayBO.checkHoliday( name );
            if( holiday != null ){
                logger.info( "END checkHolidays" );
                return Response.status( Status.OK ).entity( holiday ).build();
            }
        }
        catch( Exception e ){
            logger.error( e );
            throw new OpumException( e.getMessage() );
        }
        logger.info( "END checkHoliday" );
        return Response.status( Status.INTERNAL_SERVER_ERROR ).entity( OpumConstants.INVALID_HOLIDAY ).build();
    }

    /**
     * This service is used to do computation <br>
     * <br>
     * Exposed at "opum/getComputation/{employeeId}/year" path
     *
     * @param employeeId - this is user input employee id
     * @param year - this is user input year - (YYYY)
     * @param header - this contains the HTTP request header - username and password
     * @throws Exception
     */
    @GET
    @Path( "/getComputation/{employeeId}/{year}" )
    @Produces( MediaType.APPLICATION_JSON )

    public Response getComputation( @PathParam( "employeeId" ) String employeeId, @PathParam( "year" ) int year,
                                    @Context HttpHeaders header )
        throws Exception {
        /*
         * MultivaluedMap<String, String> headerParams = header.getRequestHeaders();
         * String email = headerParams.getFirst("username"); String password =
         * headerParams.getFirst("password"); if (!(authenticateUser(email, password)))
         * { logger.error(OpumConstants.UNAUTHORIZED); return
         * Response.status(Status.UNAUTHORIZED).build(); }
         */
        logger.info( "START getComputation" );
        projectBO = new ProjectBO();
        Year utilizationComputation = null;
        try{
            utilizationComputation = projectBO.getComputation( employeeId, year );
            if( utilizationComputation != null ){
                logger.info( "END getComputation" );
                return Response.status( Status.OK ).entity( utilizationComputation ).build();
            }
        }
        catch( Exception e ){
            logger.error( e );
            throw new OpumException( e.getMessage() );
        }
        logger.info( "END getComputation" );
        return Response.status( Status.INTERNAL_SERVER_ERROR ).entity( OpumConstants.INVALID_EMPLOYEE_ID ).build();
    }

    /**
     * This service is used in YTD Computation <br>
     * <br>
     * Exposed at "opum/getYTDComputation/{employeeId}/{year}" path
     *
     * @param employeeId - this is user input employee id
     * @param year - this is user input year - (YYYY)
     * @param header - this contains the HTTP request header - username and password
     * @throws Exception
     */
    @GET
    @Path( "/getYTDComputation/{employeeId}/{year}" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response getYTDComputation( @PathParam( "employeeId" ) String employeeId, @PathParam( "year" ) int year,
                                       @Context HttpHeaders header )
        throws Exception {
        /*
         * MultivaluedMap<String, String> headerParams = header.getRequestHeaders();
         * String email = headerParams.getFirst("username"); String password =
         * headerParams.getFirst("password"); if (!(authenticateUser(email, password)))
         * { logger.error(OpumConstants.UNAUTHORIZED); return
         * Response.status(Status.UNAUTHORIZED).build(); }
         */
        logger.info( "START getYTDComputation" );
        utilityBO = new UtilityBO();
        Year ytdComputation = null;
        try{
            ytdComputation = utilityBO.getYTDComputation( employeeId, year );
            if( ytdComputation != null ){
                logger.info( "END getYTDComputation" );
                return Response.status( Status.OK ).entity( ytdComputation ).build();
            }
        }
        catch( Exception e ){
            logger.error( e );
            throw new OpumException( e.getMessage() );
        }
        logger.info( "END getYTDComputation" );
        return Response.status( Status.INTERNAL_SERVER_ERROR ).entity( OpumConstants.INVALID_EMPLOYEE_ID ).build();
    }

    @POST
    @Path( "/savePUMQuarter" )
    @Consumes( MediaType.APPLICATION_JSON )

    public String saveQuarter( PUMQuarter pumQuarter, @Context HttpHeaders header ) throws Exception {
        logger.info( "START saveQuarter" );
        String message = null;
        try{
            projectBO = new ProjectBO();
            message = projectBO.saveQuarter( pumQuarter );
        }
        catch( Exception e ){
            logger.error( e );
            throw new OpumException( e.getMessage(), e );
        }
        logger.info( "END saveQuarter" );
        return message;
    }

    @POST
    @Path( "/savePUMMonth" )
    @Consumes( MediaType.APPLICATION_JSON )

    public String saveMonth( PUMMonth pumMonth, @Context HttpHeaders header ) throws Exception {
        logger.info( "START saveMonth" );
        String message = null;

        try{
            projectBO = new ProjectBO();
            message = projectBO.saveMonth( pumMonth );
        }
        catch( Exception e ){
            logger.error( e );
            throw new OpumException( e.getMessage(), e );
        }
        logger.info( "END saveMonth" );
        return message;
    }
}
