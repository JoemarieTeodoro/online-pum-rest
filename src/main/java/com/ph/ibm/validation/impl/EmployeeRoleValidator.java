package com.ph.ibm.validation.impl;

import static com.ph.ibm.util.ValidationUtils.isValueEmpty;

import org.apache.log4j.Logger;

import com.ph.ibm.model.EmployeeRole;
import com.ph.ibm.model.Role;
import com.ph.ibm.opum.exception.InvalidCSVException;
import com.ph.ibm.repository.EmployeeRoleRepository;
import com.ph.ibm.util.OpumConstants;
import com.ph.ibm.util.ValidationUtils;
import com.ph.ibm.validation.Validator;

public class EmployeeRoleValidator implements Validator<EmployeeRole> {

	private EmployeeRoleRepository employeeRoleRepository;

	public EmployeeRoleValidator(EmployeeRoleRepository employeeRoleRepository) {
		this.employeeRoleRepository = employeeRoleRepository;
	}

	private static Logger logger = Logger.getLogger( EmployeeValidator.class );

	@Override
	public boolean validate(EmployeeRole employeeRole) throws InvalidCSVException {
		boolean isValid = !isEmployeeRoleValueEmpty( employeeRole )
		                && isValidEmployeeRoleSerialName( employeeRole )
		                && isValidEmployeeRoleIdString( employeeRole );
		return isValid;
	}

	protected boolean isEmployeeRoleValueEmpty(EmployeeRole employeeRole) throws InvalidCSVException {
		if ( isValueEmpty( employeeRole.getEmployeeSerial() ) ||
				isValueEmpty( employeeRole.getEmployeeRoleString() ) ) {
            logger.info( "CAUSE OF ERROR: " + OpumConstants.EMPTY_CSV_ERROR );
            throw new InvalidCSVException( employeeRole, OpumConstants.EMPTY_CSV_ERROR );
		}
		return false;
	}

	protected boolean isValidEmployeeRoleSerialName(EmployeeRole employeeRole) throws InvalidCSVException {
        String patternToMatch = ( "^([A-Za-z0-9]+[ ]{0,1})*([A-Za-z0-9]+[ ]{0,1})*$" );
        if ( employeeRole.getEmployeeSerial().isEmpty() ) {
            logger.error( OpumConstants.INVALID_EMPLOYEE_ID );
            return false;
        }
        return ValidationUtils.regexValidator( employeeRole, employeeRole.getEmployeeSerial(),
        	   patternToMatch, OpumConstants.INVALID_EMPLOYEE_ID );
	}

	protected boolean isValidEmployeeRoleIdString(EmployeeRole employeeRole) throws InvalidCSVException {
		boolean isValidEmployeeRoleIdString = false;
		for ( Role employeeRoleEnum: Role.values() ) {
			if ( employeeRoleEnum.getRoleValue().equalsIgnoreCase( employeeRole.getEmployeeRoleString() ) ) {
				isValidEmployeeRoleIdString = true;
			}
		}
		if ( !isValidEmployeeRoleIdString ) {
			logger.info( "CAUSE OF ERROR: " + OpumConstants.INVALID_EMPLOYEE_ROLE );
			throw new InvalidCSVException( employeeRole, OpumConstants.INVALID_EMPLOYEE_ROLE );
		}
		return isValidEmployeeRoleIdString;
	}
}
