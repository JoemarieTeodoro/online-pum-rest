package com.ph.ibm.upload.upload.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.apache.log4j.Logger;

import com.ph.ibm.model.TeamEmployee;
import com.ph.ibm.opum.exception.InvalidCSVException;
import com.ph.ibm.repository.EmployeeRepository;
import com.ph.ibm.repository.TeamEmployeeRepository;
import com.ph.ibm.repository.TeamRepository;
import com.ph.ibm.repository.impl.EmployeeRepositoryImpl;
import com.ph.ibm.repository.impl.TeamEmployeeRepositoryImpl;
import com.ph.ibm.repository.impl.TeamRepositoryImpl;
import com.ph.ibm.upload.CsvUploaderBase;
import com.ph.ibm.util.OpumConstants;
import com.ph.ibm.validation.Validator;
import com.ph.ibm.validation.impl.TeamEmployeeValidator;

public class TeamEmployeeUploader extends CsvUploaderBase {


    private static final String ROLL_OFF_DATE_COLUMN_HEADER = "Roll-off Date";

    private static final String ROLL_IN_DATE_COLUMN_HEADER = "Roll-in Date";

    private static final String TEAM_COLUMN_HEADER = "Team";

    private static final String EMPLOYEE_SERIAL_COLUMN_HEADER = "Employee Serial";

    /**
     * EmployeeRepository is a Data Access Object which contain methods to add, register, login, view, validate field/s
     * stored in employee table - opum database
     */
    private EmployeeRepository employeeRepository = new EmployeeRepositoryImpl();

    /**
     * TeamRepository is a Data Access Object which contain methods to add, register, login, view, validate field/s
     * stored in team table - opum database
     */
    private TeamRepository teamRepository = new TeamRepositoryImpl();

    /**
     * TeamEmployeeRepository is a Data Access Object in accessing employee_team table - opum database
     */
    private TeamEmployeeRepository teamEmployeeRepository = new TeamEmployeeRepositoryImpl();

    /**
     * Contain methods to validate team employee field
     */
    private Validator<TeamEmployee> teamEmployeeValidator = new TeamEmployeeValidator();

    /**
     * Logger is used to document the execution of the system and logs the corresponding log level such as INFO, WARN,
     * ERROR
     */
    private Logger logger = Logger.getLogger( TeamEmployeeUploader.class );

    /** Size of header column */
    private static final int ROW_HEADER_COLUMN_SIZE = 4;

    @Override
    public Response upload( String rawData, UriInfo uriInfo ) throws Exception {
        List<TeamEmployee> validatedEmployee = new ArrayList<TeamEmployee>();
        String currentEmployeeID = null;
        List<String> errorList = new ArrayList<String>();
        try{
            for( Map.Entry<String, List<String>> row : parseCSV( rawData ).entrySet() ){
                try{
                    TeamEmployee validateEmployee = new TeamEmployee();
                    validateEmployee = validateTeamEmployee( uriInfo, row.getValue() );
                    currentEmployeeID = validateEmployee.getEmployeeId();
                    validatedEmployee.add( validateEmployee );
                }
                catch( InvalidCSVException e ){
                    errorList.add( "Line " + row.getKey() + " - Error: " + e.getError() );
                    continue;
                }
            }
            if( !errorList.isEmpty() ){
                return InvalidCsvErrors( uriInfo, errorList );
            }
            else{
                teamEmployeeRepository.addTeamEmployee( validatedEmployee );
                logger.info( OpumConstants.SUCCESSFULLY_UPLOADED_FILE );
            }
        }
        catch( InvalidCSVException e ){
            logger.error( e.getError() );
            return Response.status( 406 ).entity( e.getError() ).build();
        }
        catch( SQLException e ){
            logger.error( "SQL Exception due to " + e.getMessage() );
            String msg = OpumConstants.DUPLICATE_ENTRY + ": " + currentEmployeeID;
            return Response.status( 406 ).entity( msg ).build();
        }

        return Response.status( Status.OK ).header( "Location", uriInfo.getBaseUri() + "employee/" ).entity(
            OpumConstants.SUCCESS_UPLOAD ).build();
    }

    /**
     * This method is used to validate uploaded list of Users/Employees
     * 
     * @param uriInfo
     * @param row
     * @return TeamEmployee team employee instance
     * @throws Exception
     */
    private TeamEmployee validateTeamEmployee( UriInfo uriInfo, List<String> row )
        throws Exception {
        checkRowIntegrity( row );
        TeamEmployee teamEmployee = new TeamEmployee();
        teamEmployee.setEmployeeId( row.get( 0 ) );
        teamEmployee.setTeamName( row.get( 1 ) );
        teamEmployee.setRollInDate( row.get( 2 ) );
        teamEmployee.setRollOffDate( row.get( 3 ) );
        
        teamEmployeeValidator.validate( teamEmployee );

        return teamEmployee;

    }

    /**
     * Checks basic row validation like row item must not be empty and row second item should be int.
     * 
     * @param row
     * @param teamEmployee
     * @return boolean
     * @throws InvalidCSVException when row value is not valid
     */
    private void checkRowIntegrity( List<String> row ) throws InvalidCSVException {
        if( row == null || row.isEmpty() || row.size() != 4 || row.get( 0 ).isEmpty() || row.get( 1 ).isEmpty() ||
            row.get( 2 ).isEmpty() || row.get( 3 ).isEmpty() ){
            throw new InvalidCSVException( null, OpumConstants.EMPTY_CSV_ERROR );
        }
    }

    /**
     * @param row 1st line in CSV file
     * @return true if file contains header otherwise return false
     */
    @Override
    protected boolean doesContainsHeader( List<String> row ) {
        return ( row.get( 0 ).trim().equalsIgnoreCase( EMPLOYEE_SERIAL_COLUMN_HEADER ) &&
            row.get( 1 ).trim().equalsIgnoreCase( TEAM_COLUMN_HEADER ) &&
            row.get( 2 ).trim().equalsIgnoreCase( ROLL_IN_DATE_COLUMN_HEADER ) &&
            row.get( 3 ).trim().equalsIgnoreCase( ROLL_OFF_DATE_COLUMN_HEADER ) &&
            row.size() == ROW_HEADER_COLUMN_SIZE );
    }

    /**
     * @return valid header
     * @see com.ph.ibm.upload.CsvUploaderBase#getHeaders()
     */
    @Override
    protected String getHeaders() {
        String header =
            String.format( "INVALID HEADER FOUND!\nShould match:\n%s | %s | %s | %s", EMPLOYEE_SERIAL_COLUMN_HEADER,
                TEAM_COLUMN_HEADER, ROLL_IN_DATE_COLUMN_HEADER, ROLL_OFF_DATE_COLUMN_HEADER );
        return header;
    }
}
