package com.ph.ibm.bo;

import java.sql.BatchUpdateException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;

import com.ph.ibm.model.Employee;
import com.ph.ibm.model.EmployeeEvent;
import com.ph.ibm.model.EmployeeLeave;
import com.ph.ibm.model.EmployeeUpdate;
import com.ph.ibm.model.ForApproval;
import com.ph.ibm.model.PUMYear;
import com.ph.ibm.opum.exception.OpumException;
import com.ph.ibm.repository.EmployeeRepository;
import com.ph.ibm.repository.LeaveRepository;
import com.ph.ibm.repository.PUMYearRepository;
import com.ph.ibm.repository.TeamEmployeeRepository;
import com.ph.ibm.repository.TeamRepository;
import com.ph.ibm.repository.UtilizationRepository;
import com.ph.ibm.repository.impl.EmployeeRepositoryImpl;
import com.ph.ibm.repository.impl.LeaveRepositoryImpl;
import com.ph.ibm.repository.impl.PUMYearRepositoryImpl;
import com.ph.ibm.repository.impl.TeamEmployeeRepositoryImpl;
import com.ph.ibm.repository.impl.TeamRepositoryImpl;
import com.ph.ibm.repository.impl.UtilizationRepositoryImpl;
import com.ph.ibm.util.FormatUtils;
import com.ph.ibm.util.MD5HashEncrypter;
import com.ph.ibm.util.OpumConstants;
import com.ph.ibm.util.ValidationUtils;

public class EmployeeBO {

	private static final String RECOVERABLE_TEAM_VALUE = "Y";

	public static final int INVALID_TEAM_ID = -1;

	private LeaveRepository leaveRepository = new LeaveRepositoryImpl();
	
    /**
     * EmployeeRepository is a Data Access Object which contain methods to add,
     * register, login, view, validate field/s stored in employee table - opum
     * database
     */
    private EmployeeRepository employeeRepository = new EmployeeRepositoryImpl();

    private PUMYearRepository pumYearRepository = new PUMYearRepositoryImpl();
    
    private UtilizationRepository utilizationRepository = new UtilizationRepositoryImpl();

    private TeamEmployeeRepository teamEmpRepository = new TeamEmployeeRepositoryImpl();

    private static Logger logger = Logger.getLogger(EmployeeBO.class);

    /**
     * This method is used to login user
     *
     * @param username
     * @param password
     * @return Employee object
     * @throws Exception
     */
    public Employee loginEmployee(String username, String password) throws Exception {
        String hashed = MD5HashEncrypter.computeMD5Digest(password);
        Employee employee = null;
        try {
            employee = employeeRepository.loginAdmin(username, hashed);
        } catch (Exception e) {
            logger.error(e);
            throw new OpumException(e.getMessage(), e);
        }
        return employee;
    }

    /**
     * This method is used to search employee
     *
     * @param employeeIdNumber
     * @return EmployeeUpdate object
     * @throws SQLException
     */
    public EmployeeUpdate searchEmployee(String employeeIdNumber) throws SQLException {
        return employeeRepository.searchEmployee(employeeIdNumber);
    }

    /**
     * This method is used to update employee details
     *
     * @param employeeUpdate
     * @return boolean
     * @throws BatchUpdateException
     * @throws SQLException
     */
    public boolean updateEmployee(EmployeeUpdate employeeUpdate) throws BatchUpdateException, SQLException {
        return employeeRepository.updateEmployee(employeeUpdate);
    }
    
    public EmployeeEvent getEmployeeEvent(String employeeId) {
    	EmployeeEvent empEvent = new EmployeeEvent();
    	
    	try {
    		empEvent = populateEmployeeEvent(employeeId);
    	} catch (OpumException e) {
			logger.error(e.getMessage());
		}
    	return empEvent;
    }

	private EmployeeEvent populateEmployeeEvent(String employeeId) throws OpumException {
		EmployeeEvent empEvent = new EmployeeEvent();
		List<EmployeeLeave> employeeLeaveList = new ArrayList<EmployeeLeave>();
		PUMYear pumYear = pumYearRepository.retrieveCurrentFY();
		
		String currFY = "";
		if(pumYear != null) {
			String currFYStartDate = "";
			String currFYEndDate = "";
			LocalDate toDate = FormatUtils.toDBDateFormat(pumYear.getEnd());
			LocalDateTime toDateTime = LocalDateTime.of(toDate, LocalTime.from(LocalTime.MIN)).plusDays(1);
			currFY = String.valueOf(pumYear.getYearId());
			currFYStartDate = pumYear.getStart();
			currFYEndDate = toDateTime.toString();
			
			empEvent.setCurrFYStartDate(currFYStartDate);
			empEvent.setCurrFYEndDate(currFYEndDate);
			setRecoverable(employeeId, empEvent);
		}
		
		employeeLeaveList = employeeRepository.getEmployeeLeaves(employeeId, currFY);
		empEvent.setEmpLeaveList(employeeLeaveList);
		return empEvent;
	}

	private void setRecoverable(String employeeId, EmployeeEvent empEvent) throws OpumException {
		int teamID = teamEmpRepository.getTeamID(employeeId);
		if (teamID == TeamEmployeeRepositoryImpl.INVALID_VALUE) {
			throw new OpumException(OpumConstants.EMPLOYEE_HAS_NO_TEAM);
		}

		TeamRepository teamRepository = new TeamRepositoryImpl();
		String recoverableValue = teamRepository.getRecoverableFlag(String.valueOf(teamID));
		if (!ValidationUtils.isValueEmpty(recoverableValue)
				&& recoverableValue.toUpperCase().equals(RECOVERABLE_TEAM_VALUE)) {
			empEvent.setRecoverable(true);
		}
	}
    
	public boolean saveEmployeeLeave(List<EmployeeLeave> empLeave, boolean isDraft, String empID, String fyID) {
		try {
 			
		    String serial = empLeave.get( 0 ).getEmployeeID();
		    String yearID = empLeave.get( 0 ).getYearID();
            employeeRepository.saveEmployeeLeave( empLeave, isDraft, empID, fyID );
            utilizationRepository.updateUtilizationHours( serial, yearID,
                utilizationRepository.getEmployeeWeeklyHours( serial, yearID ), OpumConstants.FORECAST_UTILIZATION );
            return true;
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return false;
	}
    
	public List<ForApproval> getAllForApproval() throws SQLException {
		return leaveRepository.getAllForApproval();
	}

	public Response insertUserPastDate(EmployeeLeave empLeave) {
		try {
			PUMYear pumYear = pumYearRepository.retrieveCurrentFY();
			checkDateWithFY(empLeave, pumYear);
			employeeRepository.insertUserPastDate(empLeave, pumYear.getYearId());
		} catch (SQLException e) {
			logger.error(e.getMessage() + e.getStackTrace());
			return Response.status(Status.NOT_ACCEPTABLE).entity(e.getMessage()).build();
		}
		return Response.status(Status.OK).build();
	}

	private void checkDateWithFY(EmployeeLeave empLeave, PUMYear pumYear) throws SQLException {
		LocalDate currFYStartDate = FormatUtils.toDBDateFormat(pumYear.getStart());
		LocalDate currFYEndDate = FormatUtils.toDBDateFormat(pumYear.getEnd());
		LocalDate leaveDate = FormatUtils.toDBDateFormat(empLeave.getDate());
		if (currFYStartDate.isAfter(leaveDate) || currFYEndDate.isBefore(leaveDate)) {
			throw new SQLException("Date is out of range for current Fiscal Year!");
		}
	}
}
