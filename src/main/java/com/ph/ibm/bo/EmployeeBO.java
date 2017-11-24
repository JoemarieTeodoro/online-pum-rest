package com.ph.ibm.bo;

import java.sql.BatchUpdateException;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.ph.ibm.model.Employee;
import com.ph.ibm.model.EmployeeUpdate;
import com.ph.ibm.opum.exception.OpumException;
import com.ph.ibm.repository.EmployeeRepository;
import com.ph.ibm.repository.ProjectEngagementRepository;
import com.ph.ibm.repository.ProjectRepository;
import com.ph.ibm.repository.impl.EmployeeRepositoryImpl;
import com.ph.ibm.repository.impl.ProjectEngagementRepositoryImpl;
import com.ph.ibm.repository.impl.ProjectRepositoryImpl;
import com.ph.ibm.util.MD5HashEncrypter;
import com.ph.ibm.validation.Validator;
import com.ph.ibm.validation.impl.EmployeeValidator;

public class EmployeeBO {

    /**
     * EmployeeRepository is a Data Access Object which contain methods to add,
     * register, login, view, validate field/s stored in employee table - opum
     * database
     */
    private EmployeeRepository employeeRepository = new EmployeeRepositoryImpl();

    /**
     * ProjectRepository is a Data Access Object which contain method to retrieve
     * fields stored in project table - opum database
     */
    private ProjectRepository projectRepository = new ProjectRepositoryImpl();

    /**
     * ProjectEngagementRepository is a Data Access Object which contain method to
     * add, save, get, check field/s stored in project_engagement table - opum
     * database
     */
    private ProjectEngagementRepository projectEngagementRepository = new ProjectEngagementRepositoryImpl();

    /**
     * Validation contain methods to validate field such as employee name, employee
     * id, project name, email address
     */
    private Validator<Employee> validator = new EmployeeValidator(employeeRepository);

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
}
