package com.ph.ibm.validation.impl;

import static com.ph.ibm.util.ValidationUtils.isValueEmpty;

import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.ph.ibm.model.Employee;
import com.ph.ibm.opum.exception.InvalidCSVException;
import com.ph.ibm.repository.EmployeeRepository;
import com.ph.ibm.repository.impl.EmployeeRepositoryImpl;
import com.ph.ibm.util.OpumConstants;
import com.ph.ibm.util.ValidationUtils;
import com.ph.ibm.validation.Validator;

/**
 * This class contains regular expression patterns that validates format of name, email address, employee id, project
 * name
 */
public class EmployeeValidator implements Validator<Employee> {

    private EmployeeRepository employeeRepository = new EmployeeRepositoryImpl();

    private static Logger logger = Logger.getLogger( EmployeeValidator.class );

    @Override
    public boolean validate( Employee employee ) throws InvalidCSVException, SQLException {
        boolean isValid = !isEmployeeValueEmpty( employee ) && isValidEmployeeName( employee ) &&
            isValidEmailAddress( employee ) && isValidEmployeeId( employee ) && isValidRollInDate( employee ) &&
            isValidRollInDate( employee ) && doesEmployeeExist( employee );
        //	&& isValidPrimaryProject(employee.getPrimaryProject());
        return isValid;
    }

    /**
     * This method validate the format of employee name
     * 
     * @param employeeName
     * @return true if name matches the regular expression pattern
     * @throws InvalidCSVException
     */
    private boolean isValidEmployeeName( Employee employee ) throws InvalidCSVException {
        String patternToMatch = ( "^([A-Za-z.]+[ ]{0,1})*([A-Za-z.]+[ ]{0,1})*$" );
        return ValidationUtils.regexValidator( employee, employee.getFullName(), patternToMatch,
            OpumConstants.INVALID_NAME );
    }

    /**
     * This method validate if Employee is not assigned any value or empty
     * 
     * @param employee
     * @return true if name matches the regular expression pattern
     * @throws InvalidCSVException
     */
    public boolean isEmployeeValueEmpty( Employee employee ) throws InvalidCSVException {

        if( isValueEmpty( employee.getEmployeeSerial() ) || isValueEmpty( employee.getFullName() ) ||
            isValueEmpty( employee.getIntranetId() ) || isValueEmpty( employee.getRollInDate() ) ||
            isValueEmpty( employee.getRollOffDate() ) ){
            logger.info( "CAUSE OF ERROR: " + OpumConstants.EMPTY_CSV_VALUE );
            throw new InvalidCSVException( employee, OpumConstants.EMPTY_CSV_VALUE );
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
    private boolean isValidEmailAddress( Employee employee ) throws InvalidCSVException {
        String patternToMatch = ( "^[a-zA-Z0-9]+@+[a-zA-Z]{0,2}([\\.]+)+[ibm]+([\\.]+)([com]{3,})$" );
        return ValidationUtils.regexValidator( employee, employee.getIntranetId(), patternToMatch,
            OpumConstants.INVALID_EMAIL_ADDRESS );
    }

    /**
     * This method validate the format of roll-in date of the employee
     * 
     * @param date
     * @return true if date matches the regular expression pattern
     * @throws InvalidCSVException
     */
    private boolean isValidRollInDate( Employee employee ) throws InvalidCSVException {
        String patternToMatch = ( "[0-9]{2}(/)[0-9]{2}(/)[0-9]{4}" );
        return ValidationUtils.regexValidator( employee, employee.getRollInDate(), patternToMatch,
            OpumConstants.INVALID_DATE );
    }

    /**
     * This method validate the format of roll-off date of the employee
     * 
     * @param date
     * @return true if date matches the regular expression pattern
     * @throws InvalidCSVException
     */
    private boolean isValidRollOffDate( Employee employee ) throws InvalidCSVException {
        String patternToMatch = ( "[0-9]{2}(/)[0-9]{2}(/)[0-9]{4}" );
        return ValidationUtils.regexValidator( employee, employee.getRollOffDate(), patternToMatch,
            OpumConstants.INVALID_DATE );
    }

    /**
     * This method validate the format of employee id
     * 
     * @param employeeId
     * @return true if employee id matches the regular expression pattern
     * @throws InvalidCSVException
     */
    private boolean isValidEmployeeId( Employee employee ) throws InvalidCSVException {
        String patternToMatch = ( "^([A-Za-z0-9]+[ ]{0,1})*([A-Za-z0-9]+[ ]{0,1})*$" );
        if( employee.getEmployeeSerial().isEmpty() ){
            logger.error( OpumConstants.INVALID_EMPLOYEE_ID );
            return false;
        }
        return ValidationUtils.regexValidator( employee, employee.getEmployeeSerial(), patternToMatch,
            OpumConstants.INVALID_EMPLOYEE_ID );
    }

    /**
     * This method validate the format of project name
     * 
     * @param projectName
     * @return true if project name matches the regular expression pattern
     * @throws InvalidCSVException
     */
    private boolean isValidPrimaryProject( Employee employee ) throws InvalidCSVException {
        String patternTomatch = "^[a-zA-Z0-9]{3,10}$";
        return ValidationUtils.regexValidator( employee, employee.getPrimaryProject(), patternTomatch,
            OpumConstants.INVALID_PROJECT_NAME );
    }

    /**
     * Validate if employee id number and email exist in database
     * 
     * @param employee
     * @param employeeSerial
     * @param intranetId
     * @return
     * @throws SQLException
     * @throws InvalidCSVException
     */
    private boolean doesEmployeeExist( Employee employee ) throws SQLException, InvalidCSVException {
        boolean isValid =
            employeeRepository.doesEmployeeExist( employee.getEmployeeSerial(), employee.getIntranetId() );
        if( isValid ){
            logger.info( "CAUSE OF ERROR: " + OpumConstants.EMPLOYEE_ID_EMAIL_EXISTS );
            throw new InvalidCSVException( employee, OpumConstants.EMPLOYEE_ID_EMAIL_EXISTS );
        }
        return isValid;
    }
}
