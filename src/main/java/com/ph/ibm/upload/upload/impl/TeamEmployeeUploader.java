package com.ph.ibm.upload.upload.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

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

public class TeamEmployeeUploader extends CsvUploaderBase {

	/**
	 * EmployeeRepository is a Data Access Object which contain methods to add,
	 * register, login, view, validate field/s stored in employee table - opum
	 * database
	 */
	private EmployeeRepository employeeRepository = new EmployeeRepositoryImpl();

	/**
	 * TeamRepository is a Data Access Object which contain methods to add,
	 * register, login, view, validate field/s stored in team table - opum database
	 */
	private TeamRepository teamRepository = new TeamRepositoryImpl();

	/**
	 * TeamEmployeeRepository is a Data Access Object in accessing employee_team
	 * table - opum database
	 */
	private TeamEmployeeRepository teamEmployeeRepository = new TeamEmployeeRepositoryImpl();

	/**
	 * Logger is used to document the execution of the system and logs the
	 * corresponding log level such as INFO, WARN, ERROR
	 */
	private Logger logger = Logger.getLogger(TeamEmployeeUploader.class);

    /** Size of header column */
    private static final int ROW_HEADER_COLUMN_SIZE = 3;

	@Override
	public Response upload(String rawData, UriInfo uriInfo) {
		List<TeamEmployee> validatedEmployee = new ArrayList<TeamEmployee>();
		String currentEmployeeID = null;
        List<String> errorList = new ArrayList<String>();
		try {
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
		} catch (InvalidCSVException e) {
			logger.error(e.getError());
			return Response.status(406).entity(e.getError()).build();
		} catch (SQLException e) {
			logger.error("SQL Exception due to " + e.getMessage());
			String msg = OpumConstants.DUPLICATE_ENTRY + ": " + currentEmployeeID;
			return Response.status(406).entity(msg).build();
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
     * @throws InvalidCSVException when row value is not valid
     * @throws SQLException
     * @throws NumberFormatException
     * @throws Exception
     */
	private TeamEmployee validateTeamEmployee(UriInfo uriInfo, List<String> row)
			throws InvalidCSVException, NumberFormatException, SQLException {
		TeamEmployee teamEmployee = null;
		checkRowIntegrity(row, teamEmployee);
		if (employeeRepository.viewEmployee(row.get(0)) == null) {
			throw new InvalidCSVException(null, "No existing employee with Employee Serial: " + row.get(0));
		} else if (!teamRepository.teamExists(Integer.parseInt(row.get(1)))) {
			throw new InvalidCSVException(null, "No existing team with Team ID: " + row.get(1));
		}

		teamEmployee = new TeamEmployee();
		teamEmployee.setEmployeeId(row.get(0));
		teamEmployee.setTeamId(Integer.parseInt(row.get(1)));
		return teamEmployee;
	}

	/**
	 * Checks basic row validation like row item must not be empty and row second
	 * item should be int.
	 * 
	 * @param row
	 * @param teamEmployee
	 * @return boolean
	 * @throws InvalidCSVException
	 *             when row value is not valid
	 */
	private void checkRowIntegrity(List<String> row, TeamEmployee teamEmployee) throws InvalidCSVException {
		if (row.get(0).isEmpty()) {
			throw new InvalidCSVException(null, "Employee ID must be exist.");
		}

		if (row.size() < 2) {
			throw new InvalidCSVException(null, "Team ID must not be empty");
		}
		Pattern teamIDPattern = Pattern.compile("\\d");
		boolean isTeamIDValid = teamIDPattern.matcher(row.get(1)).matches();
		if (!isTeamIDValid) {
			throw new InvalidCSVException(null, "Team ID must be a number.");
		}
	}

    /**
     * @param row 1st line in CSV file
     * @return true if file contains header otherwise return false
     */
    @Override
    protected boolean doesContainsHeader( List<String> row ) {
        return (row.get( 0 ).toLowerCase().contains( "employee serial" ) &&
            row.get( 1 ).toLowerCase().contains( "team" ) ) || row.size() == ROW_HEADER_COLUMN_SIZE;
    }
}
