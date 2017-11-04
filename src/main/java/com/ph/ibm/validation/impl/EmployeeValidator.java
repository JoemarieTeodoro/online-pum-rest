package com.ph.ibm.validation.impl;

import static com.ph.ibm.util.ValidationUtils.isValueEmpty;

import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.ph.ibm.model.Employee;
import com.ph.ibm.opum.exception.InvalidCSVException;
import com.ph.ibm.opum.exception.InvalidEmployeeException;
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
    public boolean validate( Employee employee ) throws InvalidEmployeeException, InvalidCSVException, SQLException {
        boolean isValid = !isEmployeeValueEmpty( employee ) && isValidEmployeeName( employee.getFullName() ) &&
            isValidEmailAddress( employee.getIntranetId() ) && isValidEmployeeId( employee.getEmployeeSerial() ) &&
            doesEmployeeExist( employee );
        //	&& isValidPrimaryProject(employee.getPrimaryProject());
        return isValid;
    }

    /**
     * This method validate the format of employee name
     * 
     * @param employeeName
     * @return true if name matches the regular expression pattern
     * @throws InvalidEmployeeException
     */
    private boolean isValidEmployeeName( String name ) throws InvalidEmployeeException {
        String patternToMatch = ( "^([A-Za-z.]+[ ]{0,1})*([A-Za-z.]+[ ]{0,1})*$" );
        return ValidationUtils.regexValidator( name, patternToMatch, OpumConstants.INVALID_NAME );
    }

    public boolean isEmployeeValueEmpty( Employee employee ) throws InvalidEmployeeException, InvalidCSVException {

        if( isValueEmpty( employee.getEmployeeSerial() ) || isValueEmpty( employee.getFullName() ) ||
            isValueEmpty( employee.getIntranetId() ) ){
            logger.info( "CAUSE OF ERROR: " + OpumConstants.EMPTY_CSV_VALUE );
            throw new InvalidCSVException( employee );
        }
        return true;
    }

    /**
     * This method validate the format of employee email address
     * 
     * @param employeeEmail
     * @return true if email address matches the regular expression pattern
     * @throws InvalidEmployeeException
     */
    private boolean isValidEmailAddress( String intranetId ) throws InvalidEmployeeException {
        String patternToMatch = ( "^[a-zA-Z0-9]+@+[a-zA-Z]{0,2}([\\.]+)+[ibm]+([\\.]+)([com]{3,})$" );
        return ValidationUtils.regexValidator( intranetId, patternToMatch, OpumConstants.INVALID_EMAIL_ID );
    }

    /**
     * This method validate the format of employee id
     * 
     * @param employeeId
     * @return true if employee id matches the regular expression pattern
     * @throws InvalidEmployeeException
     */
    private boolean isValidEmployeeId( String employeeSerial ) throws InvalidEmployeeException {
        String patternToMatch = ( "^([A-Za-z0-9]+[ ]{0,1})*([A-Za-z0-9]+[ ]{0,1})*$" );
        if( employeeSerial.isEmpty() || employeeSerial.isEmpty() ){
            logger.error( OpumConstants.INVALID_EMPLOYEE_ID );
            return false;
        }
        return ValidationUtils.regexValidator( employeeSerial, patternToMatch, OpumConstants.INVALID_EMPLOYEE_ID );
    }

    /**
     * This method validate the format of project name
     * 
     * @param projectName
     * @return true if project name matches the regular expression pattern
     * @throws InvalidEmployeeException
     */
    private boolean isValidPrimaryProject( String projectName ) throws InvalidEmployeeException {
        String patternTomatch = "^[a-zA-Z0-9]{3,10}$";
        return ValidationUtils.regexValidator( projectName, patternTomatch, OpumConstants.INVALID_PROJECT_NAME );
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
    private boolean doesEmployeeExist( Employee employee )
        throws SQLException, InvalidCSVException {
        boolean isValid =
            employeeRepository.doesEmployeeExist( employee.getEmployeeSerial(), employee.getIntranetId() );
        if( isValid ){
            logger.info( "CAUSE OF ERROR: " + OpumConstants.EMPLOYEE_ID_EMAIL_EXISTS );
            throw new InvalidCSVException( employee );
        }
        return isValid;
    }
}
