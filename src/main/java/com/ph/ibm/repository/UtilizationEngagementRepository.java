package com.ph.ibm.repository;

import java.sql.SQLException;
import java.util.List;

import com.ph.ibm.model.Utilization;

/**
 * Data Access Object to utilization table
 */
public interface UtilizationEngagementRepository {

	/**
	 * This method is used to insert fields to utilization table
	 * 
	 * @param utilization
	 * @return boolean
	 * @throws SQLException
	 */
	public boolean saveUtilization(Utilization utilization) throws SQLException;

	/**
	 * This method is used to select fields from utilization table
	 * 
	 * @param employeeId
	 * @param year
	 * @return list of utilization
	 * @throws SQLException
	 */
	public List<Utilization> retrieveUtilizations(String employeeIdNumber, String year) throws SQLException;
	
	
	/**
	 * This method is used to select fields from utilization table to be exported to excel file
	 * 
	 * @param employeeId
	 * @param year
	 * @return list of utilization
	 * @throws SQLException
	 *
	 */
	public Utilization downloadUtilization(String year, int employeeId) throws SQLException;


	/**
	 * 
	 * 
	 * @param employeeSerial
	 * @param year
	 * @return Utilization object
	 * @throws SQLException
	 */
	public Utilization getComputation(String employeeSerial, int year) throws SQLException;


}
