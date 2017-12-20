
package com.ph.ibm.repository;

import java.sql.BatchUpdateException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

import com.ph.ibm.model.Employee;
import com.ph.ibm.model.EmployeeLeave;
import com.ph.ibm.model.EmployeeUpdate;
import com.ph.ibm.model.ResetPassword;
import com.ph.ibm.model.Role;
import com.ph.ibm.opum.exception.OpumException;

/**
 * Data Access Object to employee table
 */
public interface EmployeeRepository {

    /**
     * This method is used to insert fields into employee table
     *
     * @param employee
     * @return boolean
     * @throws BatchUpdateException
     * @throws SQLException
     */
    public boolean addData( Employee employee ) throws BatchUpdateException, SQLException;

    /**
     * This method is used to update fields from employee table
     *
     * @param employee
     * @return boolean
     * @throws SQLException
     */
    public boolean registerEmployee( Employee employee ) throws SQLException;

    /**
     * This method is used to select field from employee table
     *
     * @param username
     * @param password
     * @return boolean
     * @throws SQLException
     */
    public Employee loginAdmin( String username, String hashed ) throws SQLException;

    /**
     * This method is used to select field from employee table
     *
     * @param username
     * @param password
     * @return boolean
     * @throws SQLException
     */
    public Employee loginUser( String username, String hashed ) throws SQLException;

    /**
     * This method is used to select field from employee table
     *
     * @param companyId
     * @return String
     * @throws SQLException
     */
    public String viewEmployee( String employeeIdNumber ) throws SQLException;

    /**
     * This method is used to check employee Id from employee table
     *
     * @param employeeIdNumber employee id number
     * @return
     * @throws SQLException sqlException
     */
    public boolean doesEmployeeIdExist( String employeeIdNumber ) throws SQLException;

    /**
     * This method is used to email from from employee table
     *
     * @param email employee email
     * @return
     * @throws SQLException sqlException
     */
    public boolean doesEmailExist( String email ) throws SQLException;

    /**
     * This method is used to select fields from employee table
     *
     * @param employeeIdNumber
     * @return EmployeeUpdate Object
     * @throws SQLException
     */
    public EmployeeUpdate searchEmployee( String employeeIdNumber ) throws SQLException;

    /**
     * This method is used to update fields from employee table
     *
     * @param employeeUpdate
     * @return boolean
     * @throws SQLException
     * @throws BatchUpdateException
     */
    public boolean updateEmployee( EmployeeUpdate employeeUpdate ) throws SQLException, BatchUpdateException;

    /**
     * This method is used save validated employee data to employee table
     *
     * @param validateEmployee
     * @param role
     * @return boolean
     * @throws Exception
     */
    public boolean saveOrUpdate( List<Employee> validateEmployee, Role role ) throws Exception;

    /**
     * This method is used save employee roles to employee_role table
     *
     * @param employeeUpdate
     * @return boolean
     * @throws SQLException
     * @throws BatchUpdateException
     * @throws ParseException
     */
    public Employee saveEmployeeRole( String employeeSerial, Role role ) throws BatchUpdateException, SQLException;

    /**
     * This method is used to retrieve the salt from employee table to be used for the reset password link token
     *
     * @param employeeUpdate
     * @return boolean
     * @throws SQLException
     * @throws OpumException
     */
    public String retrieveSalt( String email ) throws SQLException, OpumException;

    /**
     * This method is used to update the password
     *
     * @param resetPassword
     * @return boolean
     * @throws SQLException
     * @throws OpumException
     */
    boolean updatePassword( ResetPassword resetPassword ) throws SQLException;

    /**
     * This method is used to check role Id from role table
     *
     * @param roleId
     * @return boolea
     * @throws SQLException
     */
    boolean doesEmployeeRoleIdExist( int roleId) throws SQLException;

    List<EmployeeLeave> getEmployeeLeaves(String empId, String currFY);
    
    /**
     * This method is used to update employee status in employee table
     *
     * @param roleId
     * @return boolean
     * @throws SQLException
     */
    public boolean updateEmployeeStatus( String serialNumber ) throws SQLException;

    /**
     * This method is used to retrieve employee details count
     *
     * @param serialNumber Employee Serial Number
     * @return boolean
     * @throws SQLException
     */
    public int getEmployeeCount( String serialNumber ) throws SQLException;

    /**
     * This method is used to retrieve the recent password of employee
     * 
     * @param serialNumber
     * @return
     * @throws SQLException
     */
    public String retrieveRecentPassword( String serialNumber ) throws SQLException;

    boolean saveEmployeeLeave(List<EmployeeLeave> employeeLeaveList) throws SQLException;
	boolean updateEmployeeLeave(EmployeeLeave employeeLeave) throws SQLException;

    /**
     * This method is used to get the list of admin emails
     *
     * @return list
     * @throws SQLException
     */
    public List<String> getAdminEmailList() throws SQLException;
}
