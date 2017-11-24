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
import com.ph.ibm.upload.Uploader;
import com.ph.ibm.util.OpumConstants;
import com.ph.ibm.util.UploaderUtils;
import com.ph.ibm.validation.impl.EmployeeValidator;

/**
 * Class implementation for uploading list of employees
 * 
 * @author <a HREF="teodorj@ph.ibm.com">Joemarie Teodoro</a>
 * @author <a HREF="dacanam@ph.ibm.com">Marjay Dacanay</a>
 */
public class EmployeeListUploader implements Uploader {

    /** Data Access Object to employee table */
    private EmployeeRepository employeeRepository = new EmployeeRepositoryImpl();

    /** Validator class for employee */
    private EmployeeValidator employeeValidator = new EmployeeValidator( employeeRepository );

    /** Logger instance */
    private Logger logger = Logger.getLogger( EmployeeListUploader.class );

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

        Map<String, List<String>> rows = UploaderUtils.populateList( rawData );
        if( rows.isEmpty() ){
            return UploaderUtils.invalidCsvResponseBuilder( uriInfo, null, OpumConstants.EMPTY_CSV );
        }
        List<Employee> validatedEmployee = new ArrayList<Employee>();
        String currentEmployeeID = null;

        try{
            for( List<String> row : rows.values() ){
                Employee validateEmployee = new Employee();
                validateEmployee = validateEmployee( row );
                currentEmployeeID = validateEmployee.getEmployeeId();
                validatedEmployee.add( validateEmployee );
            }

            employeeRepository.saveOrUpdate( validatedEmployee, Role.ADMIN );
            logger.info( OpumConstants.SUCCESSFULLY_UPLOADED_FILE );
        }
        catch( InvalidCSVException e ){
            logger.error( e.getError() );
            return UploaderUtils.invalidCsvResponseBuilder( uriInfo, e.getObject(), e.getError() );
        }
        catch(

        SQLException e ){
            logger.error( "SQL Exception due to " + e.getMessage() );
            e.printStackTrace();
            return Response.status( 406 ).entity( OpumConstants.SQL_ERROR ).build();
        }

        logger.info( OpumConstants.SUCCESSFULLY_UPLOADED_FILE );
        return Response.status( Status.OK ).entity( "CSV Uploaded Successfully!" ).build();
    }

    /**
     * Validates the uploaded list of Users/Employees
     * 
     * @param row represents row in csv file
     * @return Employee employee object
     * @throws Exception exception
     */
    private Employee validateEmployee( List<String> row ) throws InvalidCSVException, SQLException {

        if( row == null || row.isEmpty() ){
            throw new InvalidCSVException( null, OpumConstants.INVALID_CSV );
        }
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
     * Checks basic row validation like row item must not be empty.
     * 
     * @param row
     * @param employee
     * @return boolean
     * @throws InvalidCSVException when row value is not valid
     */
    private void checkRowIntegrity( List<String> row ) throws InvalidCSVException {
        if( row.isEmpty() || row.size() != 5 || row.get( 0 ).isEmpty() || row.get( 1 ).isEmpty() ||
            row.get( 2 ).isEmpty() || row.get( 3 ).isEmpty() || row.get( 4 ).isEmpty() ){
            throw new InvalidCSVException( null, "CSV contents should not be empty." );
        }
    }

}
