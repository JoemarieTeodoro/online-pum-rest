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
import com.ph.ibm.util.BluePagesUtils;
import com.ph.ibm.util.OpumConstants;
import com.ph.ibm.util.UploaderUtils;
import com.ph.ibm.validation.impl.EmployeeValidator;

/**
 * Class implementation for uploading list of employees
 * 
 * @author <a HREF="teodorj@ph.ibm.com">Joemarie Teodoro</a>
 * @author <a HREF="dacanam@ph.ibm.com">Marjay Dacanay</a>
 */
public class EmployeeListUploader extends CsvUploaderBase {

    private static final String ROLL_OFF_DATE_COLUMN_HEADER = "Roll-off Date";

    private static final String ROLL_IN_DATE_COLUMN_HEADER = "Roll-in Date";

    private static final String EMAIL_COLUMN_HEADER = "Email";

    private static final String EMPLOYEE_COLUMN_HEADER = "Employee";

    private static final String SERIAL_COLUMN_HEADER = "Serial";

    private static final String DESIGNATION_COLUMN_HEADER = "Designation";

    /** Data Access Object to employee table */
    private EmployeeRepository employeeRepository = new EmployeeRepositoryImpl();

    /** Validator class for employee */
    private EmployeeValidator employeeValidator = new EmployeeValidator( employeeRepository );

    /** Logger instance */
    private Logger logger = Logger.getLogger( EmployeeListUploader.class );

    /** Size of header column */
    private static final int ROW_HEADER_COLUMN_SIZE = 6;

    /**
     * Used when Super Administrator uploads the list of Admin Users
     * 
     * @param rawData Data from the CSV file
     * @param uriInfo uri information
     * @return @throws Exception exception
     * @see com.ph.ibm.upload.Uploader#upload(java.lang.String, javax.ws.rs.core.UriInfo)
     */
    @Override
    public Response upload( String rawData, UriInfo uriInfo ) throws Exception {
        List<Employee> validatedEmployee = new ArrayList<Employee>();
        List<String> recipientList = new ArrayList<String>();
        List<String> errorList = new ArrayList<String>();

        try{
            for( Map.Entry<String, List<String>> row : parseCSV( rawData ).entrySet() ){
                try{
                    Employee employee = new Employee();
                    employee = validateEmployee( row.getValue() );
                    validatedEmployee.add( employee );
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
                employeeRepository.saveOrUpdate( validatedEmployee, Role.ADMIN );
                logger.info( OpumConstants.SUCCESSFULLY_UPLOADED_FILE );
                logger.info( OpumConstants.SUCCESSFULLY_EMAILED_LIST_OF_EMAIL_ADDRESS + recipientList.toString() );
            }
        }
        catch( InvalidCSVException e ){
            logger.error( e.getError() );
            return UploaderUtils.invalidCsvResponseBuilder( uriInfo, e.getObject(), e.getError() );
        }
        catch( SQLException e ){
            logger.error( "SQL Exception due to " + e.getMessage() );
            e.printStackTrace();
            return Response.status( 406 ).entity( OpumConstants.SQL_ERROR ).build();
        }

        return Response.status( Status.OK ).entity( OpumConstants.SUCCESS_UPLOAD ).build();
    }

    /**
     * Validates the uploaded list of Users/Employees
     * 
     * @param row represents row in CSV file
     * @return Employee employee object
     * @throws InvalidCSVException custom exception for invalid CSV file values
     * @throws SQLException SQL related exception
     */
    private Employee validateEmployee( List<String> row ) throws InvalidCSVException, SQLException {
        checkRowIntegrity( row );
        Employee employee = null;
        employee = new Employee();
        employee.setEmployeeSerial( row.get( 0 ) );
        employee.setFullName( row.get( 1 ) );
        employee.setIntranetId( row.get( 2 ) );
        employee.setRollInDate( row.get( 3 ) );
        employee.setRollOffDate( row.get( 4 ) );
        employee.setDesignation( row.get( 5 ) );
        employeeValidator.validate( employee );
        employee.setManagerSerial( BluePagesUtils.getManagerSerial( employee ) );
        return employee;
    }

    /**
     * Checks basic row validation like row item must not be empty.
     * 
     * @param rows list of row CSV values
     * @throws InvalidCSVException when row value is not valid
     */
    private void checkRowIntegrity( List<String> rows ) throws InvalidCSVException {
        if( rows == null || rows.isEmpty() || rows.size() != 6 || rows.get( 0 ).isEmpty() || rows.get( 1 ).isEmpty() ||
            rows.get( 2 ).isEmpty() || rows.get( 3 ).isEmpty() || rows.get( 4 ).isEmpty() || rows.get( 5 ).isEmpty() ){
            throw new InvalidCSVException( null, OpumConstants.EMPTY_CSV_ERROR );
        }
    }

    /**
     * @param row 1st line in CSV file
     * @return true if file contains header otherwise return false
     */
    @Override
    protected boolean doesContainsHeader( List<String> row ) {
        return ( row.get( 0 ).trim().equalsIgnoreCase( SERIAL_COLUMN_HEADER ) &&
            row.get( 1 ).trim().equalsIgnoreCase( EMPLOYEE_COLUMN_HEADER ) &&
            row.get( 2 ).trim().equalsIgnoreCase( EMAIL_COLUMN_HEADER ) &&
            row.get( 3 ).trim().equalsIgnoreCase( ROLL_IN_DATE_COLUMN_HEADER ) &&
            row.get( 4 ).trim().equalsIgnoreCase( ROLL_OFF_DATE_COLUMN_HEADER ) &&
            row.get( 5 ).trim().equalsIgnoreCase( DESIGNATION_COLUMN_HEADER ) && 
            row.size() == ROW_HEADER_COLUMN_SIZE );
    }

    /**
     * @return valid headers
     * @see com.ph.ibm.upload.CsvUploaderBase#getHeaders()
     */
    @Override
    protected String getHeaders() {
        String header =
            String.format( "INVALID HEADER FOUND!\nShould match:\n%s | %s | %s | %s | %s | %s", SERIAL_COLUMN_HEADER,
                EMPLOYEE_COLUMN_HEADER, EMAIL_COLUMN_HEADER, ROLL_IN_DATE_COLUMN_HEADER, ROLL_OFF_DATE_COLUMN_HEADER,
                DESIGNATION_COLUMN_HEADER );
        return header;
    }

}
