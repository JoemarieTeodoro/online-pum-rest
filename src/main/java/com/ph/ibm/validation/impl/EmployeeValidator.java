
package com.ph.ibm.validation.impl;

import java.sql.SQLException;

import com.ph.ibm.model.Employee;
import com.ph.ibm.opum.exception.InvalidCSVException;
import com.ph.ibm.repository.EmployeeRepository;
import com.ph.ibm.util.OpumConstants;
import com.ph.ibm.util.ValidationUtils;

/**
 * Class implementation for validation for specific employee
 *
 * @author <a HREF="teodorj@ph.ibm.com">Joemarie Teodoro</a>
 * @author <a HREF="dacanam@ph.ibm.com">Marjay Dacanay</a>
 */
public class EmployeeValidator extends AdminEmployeeValidator {

    /**
     * @param employeeRepository
     */
    public EmployeeValidator( EmployeeRepository employeeRepository ) {
        super( employeeRepository );
    }
    
    @Override
    public boolean validate( Employee employee ) throws InvalidCSVException, SQLException {

        boolean isValid = !isEmployeeValueEmpty( employee ) && 
                        isValidDesignation(employee) &&
                        isValidEmployeeSerial( employee ) &&
                        isEmployeeIdExisting( employee ) &&
                        isValidEmployeeName( employee ) &&
                        isValidEmailAddress( employee ) &&
                        isEmployeeEmailExisting( employee ) && 
                        ValidationUtils.isValidDate( employee, employee.getRollInDate() ) &&
                        ValidationUtils.isValidDate( employee, employee.getRollOffDate() ) &&
                        ValidationUtils.isValidDateRange( employee, employee.getRollInDate(), employee.getRollOffDate() );
        return isValid;
    }
    
    /**
     * This method validate the format of employee name
     *
     * @param employeeName
     * @return true if name matches the regular expression pattern
     * @throws InvalidCSVException
     */
    private boolean isValidDesignation( Employee employee ) throws InvalidCSVException {
        return ValidationUtils.regexValidator( employee, employee.getDesignation().trim(),
            ValidationUtils.VALID_DESIGNATION_REGEX, OpumConstants.INVALID_DESIGNATION );
    }

}
