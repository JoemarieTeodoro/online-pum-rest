package com.ph.ibm.upload.upload.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.apache.log4j.Logger;

import com.ph.ibm.model.EmployeeRole;
import com.ph.ibm.model.Role;
import com.ph.ibm.opum.exception.EmptyEmployeeRoleException;
import com.ph.ibm.opum.exception.InvalidCSVException;
import com.ph.ibm.repository.EmployeeRoleRepository;
import com.ph.ibm.repository.impl.EmployeeRoleRepositoryImpl;
import com.ph.ibm.upload.Uploader;
import com.ph.ibm.util.OpumConstants;
import com.ph.ibm.util.UploaderUtils;
import com.ph.ibm.validation.Validator;
import com.ph.ibm.validation.impl.EmployeeRoleValidator;

public class EmployeeRoleUploader implements Uploader {

    private EmployeeRoleRepository employeeRoleRepository = EmployeeRoleRepositoryImpl.getInstance();
    private Validator<EmployeeRole> employeeRoleValidator = new EmployeeRoleValidator(employeeRoleRepository);
    private Logger logger = Logger.getLogger( EmployeeRoleUploader.class );

	@Override
	public Response upload( String rawData, UriInfo uriInfo ) throws Exception {
		Map<String, List<String>> rows = UploaderUtils.populateList( rawData );
		List<EmployeeRole> validatedEmployeeRoles = new ArrayList<EmployeeRole>();
        EmployeeRole validateEmployeeRole = new EmployeeRole();
        try {
            for ( List<String> row : rows.values() ) {
            	validateEmployeeRole = validateEmployeeRoles( uriInfo, row );
            	validatedEmployeeRoles.add( validateEmployeeRole );
            }
            for ( EmployeeRole employeeRole : validatedEmployeeRoles ) {
            	employeeRole = setRoleEnumsForEmployeeRole( employeeRole );
            	boolean isEmployeeRoleExist = employeeRoleRepository.isEmployeeRoleExists( employeeRole );
            	if (!isEmployeeRoleExist) {
            		employeeRoleRepository.saveEmployeeRole( employeeRole );
            	}
            }
            logger.info( OpumConstants.SUCCESSFULLY_UPLOADED_FILE );
        } catch ( InvalidCSVException e ) {
            logger.error( e.getError() );
            return UploaderUtils.invalidCsvResponseBuilder( uriInfo, e.getObject(), e.getError() );
        } catch ( SQLException e ) {
            logger.error( "SQL Exception due to " + e.getMessage() );
            e.printStackTrace();
            return Response.status( 206 ).header( "Location", uriInfo.getBaseUri() + "employeerole/" ).entity(
                OpumConstants.SQL_ERROR ).build();
        }
        return Response.status( Status.OK ).header( "Location", uriInfo.getBaseUri() + "employeerole/" ).entity(
            "uploaded successfully" ).build();
	}

    private EmployeeRole validateEmployeeRoles( UriInfo uriInfo, List<String> row ) throws Exception {
        if ( row == null || row.isEmpty() ) {
            throw new EmptyEmployeeRoleException( OpumConstants.EMPTY_CSV_ENTRIES_EMPLOYEE_ROLE );
        }
        EmployeeRole validateEmployeeRole = new EmployeeRole();
        validateEmployeeRole.setEmployeeSerial( row.get( 0 ) );
        validateEmployeeRole.setEmployeeRoleString( row.get( 1 ) );
        employeeRoleValidator.validate( validateEmployeeRole );
        return validateEmployeeRole;
    }

    private EmployeeRole setRoleEnumsForEmployeeRole( EmployeeRole employeeRole ) {
    	employeeRole.setEmployeeRoleEnum( changeRoleStringToEnum(employeeRole.getEmployeeRoleString() ) );
    	return employeeRole;
    }

    private Role changeRoleStringToEnum(String employeeRoleString) {
    	Role roleValue = null;
    	switch (employeeRoleString) {
            case "SYSTEM ADMINISTRATOR":
    			roleValue = Role.SYS_ADMIN;
    			break;
            case "ADMINISTRATOR":
    			roleValue = Role.ADMIN;
    			break;
            case "USER":
    			roleValue = Role.USER;
    			break;
            case "PEOPLE MANAGER":
    			roleValue = Role.PEM;
    			break;
            case "TEAM LEAD":
    			roleValue = Role.TEAM_LEAD;
    			break;
    	}
    	return roleValue;
    }
}