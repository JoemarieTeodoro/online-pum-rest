package com.ph.ibm.upload.upload.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.apache.log4j.Logger;

import com.ph.ibm.model.EmployeeRole;
import com.ph.ibm.model.Role;
import com.ph.ibm.opum.exception.InvalidCSVException;
import com.ph.ibm.repository.EmployeeRoleRepository;
import com.ph.ibm.repository.impl.EmployeeRoleRepositoryImpl;
import com.ph.ibm.upload.CsvUploaderBase;
import com.ph.ibm.util.OpumConstants;
import com.ph.ibm.util.UploaderUtils;
import com.ph.ibm.validation.Validator;
import com.ph.ibm.validation.impl.EmployeeRoleValidator;

public class EmployeeRoleUploader extends CsvUploaderBase {

    private static final String ROLE_COLUMN_HEADER = "Role";

    private static final String EMPLOYEE_SERIAL_COLUMN_HEADER = "Employee Serial";

    private EmployeeRoleRepository employeeRoleRepository = EmployeeRoleRepositoryImpl.getInstance();

    private Validator<EmployeeRole> employeeRoleValidator = new EmployeeRoleValidator( employeeRoleRepository );

    private Logger logger = Logger.getLogger( EmployeeRoleUploader.class );

    /** Size of header column */
    private static final int ROW_HEADER_COLUMN_SIZE = 5;

    @Override
    public Response upload( String rawData, UriInfo uriInfo ) throws Exception {
        List<EmployeeRole> validatedEmployeeRoles = new ArrayList<EmployeeRole>();
        EmployeeRole validateEmployeeRole = new EmployeeRole();
        try{
            for( List<String> row : parseCSV( rawData ).values() ){
                validateEmployeeRole = validateEmployeeRoles( row );
                validatedEmployeeRoles.add( validateEmployeeRole );
            }
            for( EmployeeRole employeeRole : validatedEmployeeRoles ){
                employeeRole = setRoleEnumsForEmployeeRole( employeeRole );
                boolean isEmployeeRoleExist = employeeRoleRepository.isEmployeeRoleExists( employeeRole );
                if( !isEmployeeRoleExist ){
                    employeeRoleRepository.saveEmployeeRole( employeeRole );
                }
            }
            logger.info( OpumConstants.SUCCESSFULLY_UPLOADED_FILE );
        }
        catch( InvalidCSVException e ){
            logger.error( e.getError() );
            return UploaderUtils.invalidCsvResponseBuilder( uriInfo, e.getObject(), e.getError() );
        }
        catch( SQLException e ){
            logger.error( "SQL Exception due to " + e.getMessage() );
            e.printStackTrace();
            return Response.status( 206 ).header( "Location", uriInfo.getBaseUri() + "employeerole/" ).entity(
                OpumConstants.SQL_ERROR ).build();
        }
        return Response.status( Status.OK ).header( "Location", uriInfo.getBaseUri() + "employeerole/" ).entity(
            OpumConstants.SUCCESS_UPLOAD ).build();
    }

    private EmployeeRole validateEmployeeRoles( List<String> row ) throws Exception {
        EmployeeRole validateEmployeeRole = new EmployeeRole();
        validateEmployeeRole.setEmployeeSerial( row.get( 0 ) );
        validateEmployeeRole.setEmployeeRoleString( row.get( 1 ) );
        employeeRoleValidator.validate( validateEmployeeRole );
        return validateEmployeeRole;
    }

    private EmployeeRole setRoleEnumsForEmployeeRole( EmployeeRole employeeRole ) {
        employeeRole.setEmployeeRoleEnum( changeRoleStringToEnum( employeeRole.getEmployeeRoleString() ) );
        return employeeRole;
    }

    private Role changeRoleStringToEnum( String employeeRoleString ) {
        Role roleValue = null;
        String employeeRoleStringInLowerCase = employeeRoleString.toLowerCase();
        switch( employeeRoleStringInLowerCase ){
            case OpumConstants.SYS_ADMIN:
                roleValue = Role.SYS_ADMIN;
                break;
            case OpumConstants.ADMIN_FULL_FORM:
                roleValue = Role.ADMIN;
                break;
            case OpumConstants.USER:
                roleValue = Role.USER;
                break;
            case OpumConstants.PEM:
                roleValue = Role.PEM;
                break;
            case OpumConstants.TEAM_LEAD:
                roleValue = Role.TEAM_LEAD;
                break;
        }
        return roleValue;
    }

    /**
     * Checks basic row validation i.e row item must not be empty.
     * 
     * @param row row in CSV file
     * @throws InvalidCSVException when row value is not valid
     */
    private void checkRowIntegrity( List<String> row ) throws InvalidCSVException {
        if( row == null || row.isEmpty() || row.size() != 2 || row.get( 0 ).isEmpty() || row.get( 1 ).isEmpty() ){
            throw new InvalidCSVException( null, OpumConstants.EMPTY_CSV_ERROR );
        }
    }

    /**
     * @param row 1st line in CSV file
     * @return true if file contains header otherwise return false
     */
    @Override
    protected boolean doesContainsHeader( List<String> row ) {
        return ( row.get( 0 ).equalsIgnoreCase( EMPLOYEE_SERIAL_COLUMN_HEADER ) &&
            row.get( 1 ).equalsIgnoreCase( ROLE_COLUMN_HEADER ) ) && row.size() == ROW_HEADER_COLUMN_SIZE;
    }

    /**
     * @return valid header
     * @see com.ph.ibm.upload.CsvUploaderBase#getHeaders()
     */
    @Override
    protected String getHeaders() {
        String header = String.format( "INVALID HEADER FOUND!\nShould match:\n%s | %s", EMPLOYEE_SERIAL_COLUMN_HEADER,
            ROLE_COLUMN_HEADER );
        return header;
    }
}