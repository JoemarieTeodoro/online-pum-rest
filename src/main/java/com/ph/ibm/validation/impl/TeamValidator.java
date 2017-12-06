
package com.ph.ibm.validation.impl;

import static com.ph.ibm.util.ValidationUtils.isValueEmpty;

import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.ph.ibm.model.Team;
import com.ph.ibm.opum.exception.InvalidCSVException;
import com.ph.ibm.repository.TeamRepository;
import com.ph.ibm.util.OpumConstants;
import com.ph.ibm.util.ValidationUtils;
import com.ph.ibm.validation.Validator;

/**
 * Class implementation for team validation
 * 
 * @author <a HREF="teodorj@ph.ibm.com">Joemarie Teodoro</a>
 * @author <a HREF="dacanam@ph.ibm.com">Marjay Dacanay</a>
 */
public class TeamValidator implements Validator<Team> {

    private TeamRepository teamRepository;

    private static Logger logger = Logger.getLogger( TeamValidator.class );

    public TeamValidator( TeamRepository teamRepository ) {
        this.teamRepository = teamRepository;
    }

    /**
     * @param entity
     * @return
     * @throws Exception
     * @see com.ph.ibm.validation.Validator#validate(java.lang.Object)
     */
    @Override
    public boolean validate( Team team ) throws Exception {
        boolean isValid = !isTeamValueEmpty( team )
                        && isValidTeamName( team )
                        && !isTeamExisting( team )                    
                        && isValidRecoverable( team )
                        && isValidLeadSerial( team );
        return isValid;
    }

    /**
     * Checks for empty values in Serial, Name, Email, Rollin/off Date
     *
     * @param team object
     * @return
     * @throws InvalidCSVException
     */
    protected boolean isTeamValueEmpty( Team team ) throws InvalidCSVException {
        if( isValueEmpty( team.getTeamName() ) || isValueEmpty( team.getIsRecoverable() ) ||
            isValueEmpty( team.getTeamLeadSerial() ) ){
            logger.info( ValidationUtils.CAUSE_OF_ERROR + " " + OpumConstants.EMPTY_CSV_ERROR );
            throw new InvalidCSVException( team, OpumConstants.EMPTY_CSV_ERROR );
        }
        return false;
    }

    /**
     * This method validate the format of team lead serial id
     *
     * @param employeeId
     * @return true if employee id matches the regular expression pattern
     * @throws InvalidCSVException
     */
    protected boolean isValidLeadSerial( Team team ) throws InvalidCSVException {
        return ValidationUtils.regexValidator( team, team.getTeamLeadSerial(), ValidationUtils.VALID_SERIAL_REGEX,
            OpumConstants.INVALID_TEAM_LEAD_SERIAL );
    }

    /**
     * This method validate the format of Recoverable Status
     *
     * @param employeeId
     * @return true if employee id matches the regular expression pattern
     * @throws InvalidCSVException
     */
    protected boolean isValidRecoverable( Team team ) throws InvalidCSVException {
        return ValidationUtils.regexValidator( team, team.getIsRecoverable().toUpperCase(),
            ValidationUtils.VALID_RECOVERABLE_REGEX, OpumConstants.INVALID_RECOVERABLE );
    }

    /**
     * This method validate the format of Team name
     *
     * @param employeeId
     * @return true if employee id matches the regular expression pattern
     * @throws InvalidCSVException
     */
    protected boolean isValidTeamName( Team team ) throws InvalidCSVException {
        return ValidationUtils.regexValidator( team, team.getTeamName(), ValidationUtils.VALID_TEAM_NAME_REGEX,
            OpumConstants.INVALID_TEAM_NAME );
    }

    /**
     * This method validate if team is already existing in DB
     *
     * @param employeeId
     * @return true if employee id matches the regular expression pattern
     * @throws InvalidCSVException
     * @throws SQLException
     */
    protected boolean isTeamExisting( Team team ) throws InvalidCSVException, SQLException {
        boolean isExisting = teamRepository.getTeam( team );
        if( isExisting ){
            logger.info( ValidationUtils.CAUSE_OF_ERROR + OpumConstants.TEAM_NAME_EXISTS );
            throw new InvalidCSVException( team, OpumConstants.TEAM_NAME_EXISTS );
        }
        return isExisting;

    }

}
