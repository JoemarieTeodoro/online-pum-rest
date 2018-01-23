
package com.ph.ibm.repository;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.ph.ibm.opum.exception.OpumException;

/**
 * 
 * @author <a HREF="mailto:dacanam@ph.ibm.com">Marjay Dacanay</a>
 * @author <a HREF="mailto:balocaj@ph.ibm.com">Jerven Balocating</a>
 */
public interface UtilizationRepository {
    
    public List<Double> getQuarterlyUtilizationHours( String serial, String year ) throws SQLException;

    public List<Double> getEmployeeUtilization( String serial, String year, String utilizationType ) throws SQLException;

    /**
     * @param year
     * @return
     * @throws SQLException
     * @throws OpumException
     */
    public String retrieveYearID( String year ) throws SQLException;

    /**
     * @param year
     * @return
     * @throws SQLException
     */
    public boolean doesYearExists( String year ) throws SQLException;

    /**
     * @param serial
     * @param year
     * @return
     * @throws SQLException
     */
    public List<Double> getEmployeeWeeklyHours( String serial, String year ) throws SQLException;

    /**
     * @param serial
     * @param yearId
     * @param lstWeeklyHours
     * @return
     * @throws SQLException
     */
	public boolean updateUtilizationHours(String serial, String yearId, List<Double> lstWeeklyHours, String utilizationType) throws SQLException;
    
	public void populateActualUtilization(int yearId, Map<String, Map<String,Double>> employeeHoursMap) throws SQLException;
	
	public boolean isEmployeeInUtilization(String serial, int yearId, String utilizationType) throws SQLException;

    /**
     * @param serial
     * @param year
     * @return
     * @throws SQLException
     */
    public List<Double> getCombinedUtilization( String serial, String year ) throws SQLException;

}
