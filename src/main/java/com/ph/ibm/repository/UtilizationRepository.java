
package com.ph.ibm.repository;

import java.sql.SQLException;
import java.util.List;

import com.ph.ibm.opum.exception.OpumException;

/**
 * 
 * @author <a HREF="mailto:dacanam@ph.ibm.com">Marjay Dacanay</a>
 * @author <a HREF="mailto:balocaj@ph.ibm.com">Jerven Balocating</a>
 */
public interface UtilizationRepository {
    
    public List<Double> getQuarterlyUtilizationHours( String serial, String year ) throws SQLException;

    /**
     * @param year
     * @return
     * @throws SQLException
     * @throws OpumException
     */
    String retrieveYearID( String year ) throws SQLException;

    /**
     * @param year
     * @return
     * @throws SQLException
     */
    boolean doesYearExists( String year ) throws SQLException;

}
