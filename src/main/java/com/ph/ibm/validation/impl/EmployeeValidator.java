package com.ph.ibm.validation.impl;

import static com.ph.ibm.util.ValidationUtils.isValueEmpty;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.apache.log4j.Logger;

import com.ph.ibm.model.Employee;
import com.ph.ibm.opum.exception.InvalidCSVException;
import com.ph.ibm.repository.EmployeeRepository;
import com.ph.ibm.util.OpumConstants;
import com.ph.ibm.util.ValidationUtils;
import com.ph.ibm.validation.Validator;

/**
 * This class contains regular expression patterns that validates format of name, email address, employee id, project
 * name
 */
public class EmployeeValidator implements Validator<Employee> {

    private static final String DATE_FORMAT = "M/d/yyyy";

	private static final String VALID_SERIAL_REGEX = "^([A-Za-z0-9]{6,})*$";

	private static final String VALID_EMAIL_REGEX = "^[a-zA-Z0-9]+@{1}+[a-zA-Z]{0,2}([\\\\.]+)+ibm+([\\\\.]+)com$";

	private static final String VALID_DATE_REGEX = "^(0[1-9]|1[0-2]|[1-9])\\/(0[1-9]|[1-9]|[12][0-9]|3[01])\\/(19|20)\\d{2}$";

	private static final String VALID_NAME_REGEX = "^([A-Za-z.]+[ ]{0,1})*([A-Za-z.]+[ ]{0,1})*$";

	private EmployeeRepository employeeRepository;

    private static Logger logger = Logger.getLogger( EmployeeValidator.class );

    public EmployeeValidator(EmployeeRepository employeeRepository) {
    	this.employeeRepository = employeeRepository;
	}

    @Override
    public boolean validate( Employee employee ) throws InvalidCSVException, SQLException {
        boolean isValid = !isEmployeeValueEmpty( employee )
				        	&& isValidEmailAddress( employee )
				            && isValidEmployeeSerial( employee )
				            && !isEmployeeExisting( employee )
				            && isValidEmployeeName( employee )
				            && isValidRollDate( employee, employee.getRollInDate() )
				            && isValidRollDate( employee, employee.getRollOffDate() )
				            && isValidDateRange( employee, employee.getRollInDate(), employee.getRollOffDate() );
        return isValid;
    }

    /**
     * Checks for empty values in Serial, Name, Email, Rollin/off Date
     *
     * @param employee
     * @return
     * @throws InvalidCSVException
     */
    protected boolean isEmployeeValueEmpty( Employee employee ) throws InvalidCSVException {
        if( isValueEmpty( employee.getEmployeeSerial() ) || isValueEmpty( employee.getFullName() ) ||
            isValueEmpty( employee.getIntranetId() ) || isValueEmpty( employee.getRollInDate() ) ||
            isValueEmpty( employee.getRollOffDate() )){
            logger.info( ValidationUtils.CAUSE_OF_ERROR + OpumConstants.EMPTY_CSV_VALUE );
            throw new InvalidCSVException( employee , OpumConstants.EMPTY_CSV_VALUE);
        }
        return false;
    }

    /**
     * This method validate the format of employee email address
     *
     * @param employeeEmail
     * @return true if email address matches the regular expression pattern
     * @throws InvalidCSVException
     */
    protected boolean isValidEmailAddress( Employee employee ) throws InvalidCSVException {
        return ValidationUtils.regexValidator( employee, employee.getIntranetId() , VALID_EMAIL_REGEX, OpumConstants.INVALID_EMAIL_ADDRESS );
    }

    /**
     * This method validate the format of employee id
     *
     * @param employeeId
     * @return true if employee id matches the regular expression pattern
     * @throws InvalidCSVException
     */
    protected boolean isValidEmployeeSerial( Employee employee ) throws InvalidCSVException {
        return ValidationUtils.regexValidator( employee, employee.getEmployeeSerial(), VALID_SERIAL_REGEX, OpumConstants.INVALID_EMPLOYEE_ID );
    }

    /**
     * Validate if employee id number and email exist in database
     *
     * @param employee
     * @return
     * @throws SQLException
     * @throws InvalidCSVException
     */
    protected boolean isEmployeeExisting( Employee employee )
        throws SQLException, InvalidCSVException {
        boolean isExisting =
            employeeRepository.doesEmployeeExist( employee.getEmployeeSerial(), employee.getIntranetId() );
        if( isExisting ){
            logger.info( ValidationUtils.CAUSE_OF_ERROR + OpumConstants.EMPLOYEE_ID_EMAIL_EXISTS );
            throw new InvalidCSVException( employee, OpumConstants.EMPLOYEE_ID_EMAIL_EXISTS );
        }
        return isExisting;
    }

	/**
     * This method validate the format of employee name
     *
     * @param employeeName
     * @return true if name matches the regular expression pattern
	 * @throws InvalidCSVException
     */
    protected boolean isValidEmployeeName( Employee employee ) throws InvalidCSVException {
        return ValidationUtils.regexValidator( employee, employee.getFullName(), VALID_NAME_REGEX, OpumConstants.INVALID_NAME );
    }

    /**
     * Checks validity of the day in the date and date format.
     *
     * @param rollDate - date to be verified
     * @return true or Exception
     * @throws InvalidCSVException
     */
    protected boolean isValidRollDate(Employee employee, String rollDate) throws InvalidCSVException {
        try {
            DateFormat format = new SimpleDateFormat(DATE_FORMAT);
            format.setLenient(false);
            format.parse(rollDate);

            return ValidationUtils.regexValidator( employee, rollDate, VALID_DATE_REGEX, OpumConstants.INVALID_NAME );
        } catch (ParseException e) {
            logger.info( ValidationUtils.CAUSE_OF_ERROR + OpumConstants.INVALID_DATE );
            throw new InvalidCSVException(employee, OpumConstants.INVALID_DATE);
        }
	}


    /**
     * Checks if rollOff is after rollIn
     *
     * @param rollIn
     * @param rollOff
     * @return
     * @throws InvalidCSVException
     */
    protected boolean isValidDateRange(Employee employee, String rollIn, String rollOff) throws InvalidCSVException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
        LocalDate rollInDate = LocalDate.parse(rollIn, formatter);
        LocalDate rollOffDate = LocalDate.parse(rollOff, formatter);

        if( rollInDate.isAfter(rollOffDate) ){
            logger.info( ValidationUtils.CAUSE_OF_ERROR + OpumConstants.INVALID_DATE_RANGE );
        	throw new InvalidCSVException( employee,OpumConstants.INVALID_DATE_RANGE );
        }

		return true;
	}
}
