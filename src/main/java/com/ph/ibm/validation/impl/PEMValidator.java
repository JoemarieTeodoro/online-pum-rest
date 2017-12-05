package com.ph.ibm.validation.impl;

import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.ph.ibm.model.PEM;
import com.ph.ibm.opum.exception.InvalidCSVException;
import com.ph.ibm.repository.PEMRepository;
import com.ph.ibm.util.OpumConstants;
import com.ph.ibm.util.ValidationUtils;
import com.ph.ibm.validation.Validator;

public class PEMValidator implements Validator<PEM> {

	private PEMRepository pemRepository;
	
    private static Logger logger = Logger.getLogger( PEMValidator.class );
	
	public PEMValidator( PEMRepository repository ) {
		this.pemRepository = repository;
	}
	
	@Override
	public boolean validate( PEM pem ) throws Exception {
		return !isFieldsNull(pem) &&
			   isPEMSerialValid(pem) && 
			   isEmployeeSerialValid(pem) &&
			   isDateRangeValid(pem);
	}

	private boolean isFieldsNull(PEM pem) throws InvalidCSVException 
	{
		if(ValidationUtils.isValueEmpty( pem.getEmployeeSerial() ) ||
		   ValidationUtils.isValueEmpty( pem.getPEMSerial() ) ||
		   ValidationUtils.isValueEmpty( pem.getStartDate() ) ||
		   ValidationUtils.isValueEmpty( pem.getEndDate() ))
		{
            throw new InvalidCSVException( pem, OpumConstants.EMPTY_CSV_ERROR );
		}
		return false;
	}

	private boolean isPEMSerialValid(PEM pem) throws InvalidCSVException, SQLException {
		PEM retreivedPEM = pemRepository.getPEM( pem.getPEMSerial() );
		if( retreivedPEM != null )
		{
            logger.info( ValidationUtils.CAUSE_OF_ERROR + OpumConstants.PEM_ID_EXISTS );
            throw new InvalidCSVException( pem, OpumConstants.PEM_ID_EXISTS );
		}
		return ValidationUtils.regexValidator( pem, pem.getPEMSerial(), ValidationUtils.VALID_SERIAL_REGEX, OpumConstants.INVALID_PEM_ID );
	}
	
	private boolean isEmployeeSerialValid(PEM pem) throws InvalidCSVException, SQLException {
		return ValidationUtils.regexValidator( pem, pem.getEmployeeSerial(), ValidationUtils.VALID_SERIAL_REGEX, OpumConstants.INVALID_EMPLOYEE_ID );
	}
	
	private boolean isDateRangeValid(PEM pem) throws InvalidCSVException {
		
		return ValidationUtils.isValidDate(pem,pem.getStartDate()) &&
			   ValidationUtils.isValidDate(pem,pem.getEndDate()) &&
			   ValidationUtils.isValidDateRange(pem, pem.getStartDate(), pem.getEndDate());
	}
}
