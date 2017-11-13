
package com.ph.ibm.repository;

import java.sql.BatchUpdateException;
import java.sql.SQLException;
import java.text.ParseException;

import com.ph.ibm.model.Employee;
import com.ph.ibm.model.EmployeeUpdate;
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
     * This method is used to select field from employee table
     * 
     * @param companyIDNumber
     * @param email
     * @return boolean
     * @throws SQLException
     */
    public boolean doesEmployeeExist( String employeeIdNumber, String email ) throws SQLException;

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
     * @param employeeUpdate
     * @return boolean
     * @throws SQLException
     * @throws BatchUpdateException
     * @throws ParseException
     */
    public Employee saveOrUpdate( Employee validateEmployee ) throws BatchUpdateException, SQLException;

    /**
     * This method is used to retrieve the salt from employee table to be used for the reset password link token
     * 
     * @param employeeUpdate
     * @return boolean
     * @throws SQLException
     * @throws OpumException
     */
    public String retrieveSalt( String email ) throws SQLException, OpumException;

}
