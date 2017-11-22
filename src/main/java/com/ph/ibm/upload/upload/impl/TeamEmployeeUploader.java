package com.ph.ibm.upload.upload.impl;

import java.sql.BatchUpdateException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
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
import com.ph.ibm.repository.TeamRepositoryImpl;
import com.ph.ibm.repository.impl.EmployeeRepositoryImpl;
import com.ph.ibm.repository.impl.TeamEmployeeRepositoryImpl;
import com.ph.ibm.upload.Uploader;
import com.ph.ibm.util.OpumConstants;
import com.ph.ibm.util.UploaderUtils;

public class TeamEmployeeUploader implements Uploader {

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

	@Override
	public Response upload(String rawData, UriInfo uriInfo) {
		Map<String, List<String>> rows = UploaderUtils.populateList(rawData);
		
		List<TeamEmployee> validatedEmployee = new ArrayList<TeamEmployee>();
		String currentEmployeeID = null;
		
		try {
			for (List<String> row : rows.values()) {
				TeamEmployee validateEmployee = new TeamEmployee();
				validateEmployee = validateTeamEmployee(uriInfo, row);
				currentEmployeeID = validateEmployee.getEmployeeId();
				validatedEmployee.add(validateEmployee);
			}

			teamEmployeeRepository.addTeamEmployee(validatedEmployee);
			logger.info(OpumConstants.SUCCESSFULLY_UPLOADED_FILE);
		} catch (InvalidCSVException e) {
			logger.error(e.getError());
			return Response.status(406).entity(e.getError()).build();
		} catch (SQLException e) {
			logger.error("SQL Exception due to " + e.getMessage());
			String msg = OpumConstants.DUPLICATE_ENTRY + ": " + currentEmployeeID;
			return Response.status(406).entity(msg).build();
		}
		return Response.status(Status.OK).header("Location", uriInfo.getBaseUri() + "employee/")
				.entity("uploaded successfully").build();
	}

	/**
	 * This method is used to validate uploaded list of Users/Employees
	 * 
	 * @param uriInfo
	 * @param row
	 * @return Employee
	 * @throws InvalidCSVException
	 *             when row value is not valid
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
		Pattern p = Pattern.compile("[^0-9]");
		boolean isEmployeeIDValid = p.matcher(row.get(0)).matches();
		boolean isTeamIDValid = p.matcher(row.get(1)).matches();
		if (row.isEmpty() || row.size() != 2 || row.get(0).isEmpty() || row.get(1).isEmpty()) {
			throw new InvalidCSVException(null, "CSV contents should not be empty.");
		} else if (isEmployeeIDValid) {
			throw new InvalidCSVException(null, "Employee ID must be a number.");
		} else if (isTeamIDValid) {
			throw new InvalidCSVException(null, "Team ID must be a number.");
		}
	}
}
