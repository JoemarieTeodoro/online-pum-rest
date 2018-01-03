package com.ph.ibm.validation.impl;

import static org.hamcrest.CoreMatchers.nullValue;

import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.ph.ibm.model.TeamEmployee;
import com.ph.ibm.opum.exception.InvalidCSVException;
import com.ph.ibm.repository.EmployeeRepository;
import com.ph.ibm.repository.TeamRepository;
import com.ph.ibm.repository.impl.EmployeeRepositoryImpl;
import com.ph.ibm.repository.impl.TeamRepositoryImpl;
import com.ph.ibm.util.OpumConstants;
import com.ph.ibm.util.ValidationUtils;
import com.ph.ibm.validation.Validator;

/**
 * Class that validates information based on employee team values
 *
 * @author <a HREF="teodorj@ph.ibm.com">Joemarie Teodoro</a>
 */
public class TeamEmployeeValidator implements Validator<TeamEmployee> {


    private static Logger logger = Logger.getLogger( TeamEmployeeValidator.class );

    /**
     * TeamRepository is a Data Access Object which contain methods to add, register, login, view, validate field/s
     * stored in team table - opum database
     */
    private TeamRepository teamRepository;

    /**
     * Object which contain methods to add, register, login, view, validate field/employee stored in employeeRepository
     */
    private EmployeeRepository employeeRepository;


    public TeamEmployeeValidator( ) {
        this.teamRepository = new TeamRepositoryImpl();
        this.employeeRepository = new EmployeeRepositoryImpl();
    }

    /**
     * @param entity
     * @return
     * @throws Exception
     * @see com.ph.ibm.validation.Validator#validate(java.lang.Object)
     */
    @Override
    public boolean validate( TeamEmployee teamEmployee )
        throws InvalidCSVException, SQLException {
        boolean isValid = isValidSerial( teamEmployee ) &&
                        isValidTeamName( teamEmployee ) &&
                        isValidDate( teamEmployee.getRollInDate() ) &&
                        isValidDate( teamEmployee.getRollOffDate() ) &&
                        isEmployeeExisting( teamEmployee ) &&
                        isTeamExists( teamEmployee ) &&
                        isValidDateRange( teamEmployee ) &&
                        isWithinDateRange( teamEmployee );
        return isValid;
    }

    private boolean isValidSerial( TeamEmployee teamEmployee ) throws InvalidCSVException {
        return ValidationUtils.regexValidator( teamEmployee, teamEmployee.getEmployeeId(),
            ValidationUtils.VALID_SERIAL_REGEX, OpumConstants.INVALID_EMPLOYEE_ID );
    }

    private boolean isEmployeeExisting( TeamEmployee teamEmployee ) throws SQLException, InvalidCSVException {
        if( employeeRepository.viewEmployee( teamEmployee.getEmployeeId() ) == null ){
            throw new InvalidCSVException( null,
                "No existing employee with Employee Serial: " + teamEmployee.getEmployeeId() );
        }
        return true;
    }

    private boolean isValidTeamName( TeamEmployee teamEmployee ) throws InvalidCSVException {
        return ValidationUtils.regexValidator( teamEmployee, teamEmployee.getTeamName(),
            ValidationUtils.VALID_TEAM_NAME_REGEX,
            OpumConstants.INVALID_TEAM_NAME );
    }

    private boolean isTeamExists( TeamEmployee teamEmployee ) throws InvalidCSVException {
        if( !teamRepository.teamExists( teamEmployee.getTeamName() ) ){
            throw new InvalidCSVException( null, "No existing team with Team Name: " + teamEmployee.getTeamName() );
        }
        return true;
    }

    private boolean isValidDate( String rollDate ) throws InvalidCSVException {
        return ValidationUtils.isValidDate( null, rollDate );
    }

    private boolean isValidDateRange( TeamEmployee teamEmployee ) throws InvalidCSVException {
        if( !ValidationUtils.isValidDateRange( nullValue(), teamEmployee.getRollInDate(),
            teamEmployee.getRollOffDate() ) ){
            throw new InvalidCSVException( null, "Invalid date range for roll in & roll off" );
        }
        return true;
    }

    private boolean isWithinDateRange( TeamEmployee teamEmployee ) throws InvalidCSVException, SQLException {
        if( !ValidationUtils.isWithinDateRange( employeeRepository, teamEmployee ) ){
            throw new InvalidCSVException( null,
                "Roll in and Roll off dates must within USAA Roll in and  Roll off Dates" );
        }
        return true;
    }


}
