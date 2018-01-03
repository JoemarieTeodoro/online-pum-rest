package com.ph.ibm.bo;

import java.sql.BatchUpdateException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.ph.ibm.model.Employee;
import com.ph.ibm.model.EmployeeEvent;
import com.ph.ibm.model.EmployeeLeave;
import com.ph.ibm.model.EmployeeUpdate;
import com.ph.ibm.model.PUMYear;
import com.ph.ibm.opum.exception.OpumException;
import com.ph.ibm.repository.EmployeeRepository;
import com.ph.ibm.repository.PUMYearRepository;
import com.ph.ibm.repository.impl.EmployeeRepositoryImpl;
import com.ph.ibm.repository.impl.PUMYearRepositoryImpl;
import com.ph.ibm.util.MD5HashEncrypter;

public class EmployeeBO {

    /**
     * EmployeeRepository is a Data Access Object which contain methods to add,
     * register, login, view, validate field/s stored in employee table - opum
     * database
     */
    private EmployeeRepository employeeRepository = new EmployeeRepositoryImpl();

    private PUMYearRepository pumYearRepository = new PUMYearRepositoryImpl();

    private static Logger logger = Logger.getLogger(EmployeeBO.class);

//    /**
//     * This method is used to register user
//     *
//     * @param companyIDNumber
//     * @param projectName
//     * @param email
//     * @param password
//     * @return String
//     * @throws Exception
//     */
//    public String registerEmployee(String employeeIdNumber, String projectName, String email, String password)
//            throws Exception {
//        Employee validateEmployee = new Employee(employeeIdNumber, email, projectName, password);
//        validator.validate(validateEmployee);
//
//        String hashed = MD5HashEncrypter.computeMD5Digest(password);
//        EmployeeProject employeeProject = new EmployeeProject(employeeIdNumber, email, hashed, projectName);
//        List<Project> projects = new ArrayList<Project>();
//        projects = projectRepository.retrieveData();
//        ProjectEngagement projectEngagement = new ProjectEngagement();
//
//        // check if USAA Project exists
//        for (Project project : projects) {
//            if (!project.getProjectName().equals(employeeProject.getProjectName())) {
//                continue;
//            }
//            projectEngagement.setProjectId(project.getProjectId());
//        }
//        Employee employee = new Employee();
//        employee.setEmployeeSerial(employeeProject.getEmployeeIdNumber());
//        employee.setIntranetId(employeeProject.getEmail());
//        employee.setPassword(employeeProject.getPassword());
//        // Employee employee = new Employee(employeeProject.getEmployeeIdNumber(),
//        // employeeProject.getEmail(), employeeProject.getPassword());
//        if (!employeeRepository.registerEmployee(employee)) {
//            logger.info(format("Unsuccessful Registration for employee %s %s", employeeProject.getEmployeeIdNumber(),
//                    employeeProject.getEmail()));
//            throw new InvalidEmployeeException(OpumConstants.INVALID_COMPANY_ID);
//        }
//        projectEngagement.setEmployeeId(employeeRepository.viewEmployee(employeeProject.getEmployeeIdNumber()));
//        projectEngagementRepository.addProjectEngagement(projectEngagement);
//
//        logger.info(OpumConstants.SUCCESSFULLY_REGISTERED);
//        return "Employee " + employeeProject.getEmployeeIdNumber() + " - " + employeeProject.getEmail()
//                + " was successfully registered";
//    }

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
    	List<EmployeeLeave> employeeLeaveList = new ArrayList<EmployeeLeave>();
    	
    	try {
    		PUMYear pumYear = pumYearRepository.retrieveCurrentFY();
    		String currFY = "";
    		String currFYStartDate = "";
    		String currFYEndDate = "";
    		if(pumYear!=null) {
    			DateTimeFormatter df = DateTimeFormatter.ofPattern(PUMYearRepositoryImpl.DATE_FORMAT);
    			LocalDate toDate = LocalDate.parse(pumYear.getEnd(), df);
    			LocalDateTime toDateTime = LocalDateTime.of(toDate, LocalTime.from(LocalTime.MIN)).plusDays(1);
    			currFY = String.valueOf(pumYear.getYearId());
    			currFYStartDate = pumYear.getStart();
    			currFYEndDate = toDateTime.toString();
    			
    			empEvent.setCurrFYStartDate(currFYStartDate);
    			empEvent.setCurrFYEndDate(currFYEndDate);
    		}
    		
    		employeeLeaveList = employeeRepository.getEmployeeLeaves(employeeId, currFY);
    		empEvent.setEmpLeaveList(employeeLeaveList);
    	} catch (Exception e) {
			logger.error(e.getMessage());
		}
    	return empEvent;
    }
    
    public List<EmployeeEvent> generatePUMCalendar() {
    	List<EmployeeEvent> cal = new ArrayList<EmployeeEvent>();
    	
    	return cal;
    }
    
	public boolean saveEmployeeLeave(List<EmployeeLeave> empLeave, boolean draft, String empID, String fyID) {
		try {
 			return employeeRepository.saveEmployeeLeave(empLeave, draft, empID, fyID);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return false;
	}
}
