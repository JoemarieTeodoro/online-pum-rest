package com.ph.ibm.upload.upload.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.apache.log4j.Logger;

import com.ph.ibm.model.Employee;
import com.ph.ibm.model.Role;
import com.ph.ibm.opum.exception.InvalidCSVException;
import com.ph.ibm.repository.EmployeeRepository;
import com.ph.ibm.repository.impl.EmployeeRepositoryImpl;
import com.ph.ibm.upload.CsvUploaderBase;
import com.ph.ibm.util.OpumConstants;
import com.ph.ibm.util.UploaderUtils;
import com.ph.ibm.validation.Validator;
import com.ph.ibm.validation.impl.EmployeeValidator;

/**
 * Class implementation for uploading list of user administrator
 *
 * @author <a HREF="teodorj@ph.ibm.com">Joemarie Teodoro</a>
 * @author <a HREF="dacanam@ph.ibm.com">Marjay Dacanay</a>
 */
public class AdminListUploader extends CsvUploaderBase {

    /**
     * EmployeeRepository is a Data Access Object which contain methods to add, register, login, view, validate field/s
     * stored in employee table - opum database
     */
    private EmployeeRepository employeeRepository;

    /**
     * Validation contain methods to validate field such as employee name, employee id, project name, email address
     */
    private Validator<Employee> employeeValidator;

    /**
     * Logger is used to document the execution of the system and logs the corresponding log level such as INFO, WARN,
     * ERROR
     */
    private Logger logger;

    /** Size of header column */
    private static final int ROW_HEADER_COLUMN_SIZE = 5;

    /** Constructor */
    public AdminListUploader() {
        employeeRepository = new EmployeeRepositoryImpl();
        employeeValidator = new EmployeeValidator( employeeRepository );
        logger = Logger.getLogger( AdminListUploader.class );
    }

    /**
     * Uploads list of user administrator
     * 
     * @param rawData rawData Data from the CSV file
     * @param uriInfo URI information
     * @return Status.OK if successful otherwise return invalid response
     * @throws Exception exception
     * @see com.ph.ibm.upload.Uploader#upload(java.lang.String, javax.ws.rs.core.UriInfo)
     */
    @Override
    public Response upload( String rawData, UriInfo uriInfo ) throws Exception {
        List<Employee> validatedEmployees = new ArrayList<Employee>();
        List<String> recipientList = new ArrayList<String>();
        List<String> errorList = new ArrayList<String>();

        try{
            for( Map.Entry<String, List<String>> row : parseCSV( rawData ).entrySet() ){
                try{
                    Employee employee = new Employee();
                    employee = validateEmployee( row.getValue() );
                    validatedEmployees.add( employee );
                    recipientList.add( employee.getIntranetId() );
                }
                catch( InvalidCSVException e ){
                    errorList.add( "Line " + row.getKey() + " - Error: " + e.getError() );
                    continue;
                }
            }
            if( !errorList.isEmpty() ){
                return InvalidCsvErrors( uriInfo, errorList );
            }
            else{
                employeeRepository.saveOrUpdate( validatedEmployees, Role.SYS_ADMIN );
                logger.info( OpumConstants.SUCCESSFULLY_UPLOADED_FILE );
            }
        }
        catch( InvalidCSVException e ){
            logger.error( e.getError() );
            return UploaderUtils.invalidCsvResponseBuilder( uriInfo, e.getObject(), e.getError() );
        }
        catch( SQLException e ){
            logger.error( "SQL Exception due to " + e.getMessage() );
            logger.error( e.getStackTrace() );
            return Response.status( 406 ).entity( OpumConstants.SQL_ERROR ).build();
        }

        logger.info( OpumConstants.SUCCESSFULLY_UPLOADED_FILE );
        sendEmailsToListOfRecepientsToChangePasswords( recipientList );
        logger.info( OpumConstants.SUCCESSFULLY_EMAILED_LIST_OF_EMAIL_ADDRESS + recipientList.toString() );

        return Response.status( Status.OK ).entity( OpumConstants.SUCCESS_UPLOAD ).build();
    }

    /**
     * This method is used to validate uploaded list of Users/Employees
     *
     * @param row represents row in CSV file
     * @return Employee employee object
     * @throws InvalidCSVException custom exception for invalid CSV file values
     * @throws SQLException SQL related exception
     * @throws Exception exception
     */
    private Employee validateEmployee( List<String> row ) throws InvalidCSVException, SQLException, Exception {
        checkRowIntegrity( row );
        Employee employee = null;
        employee = new Employee();
        employee.setEmployeeSerial( row.get( 0 ) );
        employee.setFullName( row.get( 1 ) );
        employee.setIntranetId( row.get( 2 ) );
        employee.setRollInDate( row.get( 3 ) );
        employee.setRollOffDate( row.get( 4 ) );
        employeeValidator.validate( employee );
        return employee;
    }

    /**
     * Checks basic row validation i.e row item must not be empty.
     * 
     * @param row row in CSV file
     * @throws InvalidCSVException when row value is not valid
     */
    private void checkRowIntegrity( List<String> row ) throws InvalidCSVException {
        if( row == null || row.isEmpty() || row.size() != 5 || row.get( 0 ).isEmpty() || row.get( 1 ).isEmpty() ||
            row.get( 2 ).isEmpty() || row.get( 3 ).isEmpty() || row.get( 4 ).isEmpty() ){
            throw new InvalidCSVException( null, OpumConstants.EMPTY_CSV_ERROR );
        }
    }

    /**
     * @param row 1st line in CSV file
     * @return true if file contains header otherwise return false
     */
    @Override
    protected boolean doesContainsHeader( List<String> row ) {
        return ( row.get( 0 ).toLowerCase().contains( "serial" ) && row.get( 1 ).toLowerCase().contains( "employee" ) &&
            row.get( 2 ).toLowerCase().contains( "email" ) && row.get( 3 ).toLowerCase().contains( "roll-in date" ) &&
            row.get( 4 ).toLowerCase().contains( "roll-off date" ) ) || row.size() == ROW_HEADER_COLUMN_SIZE;
    }

}
