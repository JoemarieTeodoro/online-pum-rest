package com.ph.ibm.repository;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.ph.ibm.model.YtdUtilization;

/**
 * @author <a HREF="dacanam@ph.ibm.com">Marjay Dacanay</a>
 * @author <a HREF="teodorj@ph.ibm.com">Joemarie Teodoro</a>
 */
public interface DownloadUtilizationRepository {

    /**
     * @param weekName
     * @param yearId
     * @param weekEnding
     * @param designation
     * @return
     */
    public List<Map<String, List<Double>>> retrieveActualHours( String weekName, int yearId, String weekEnding,
                                                                String designation );

    /**
     * @param weekName
     * @param yearId
     * @param weekEnding
     * @param designation
     * @return
     */
    public List<Map<String, List<Double>>> retrieveForecastedHours( String weekName, int yearId, String weekEnding,
                                                                    String designation );

    /**
     * @param ytdUtilization
     * @return
     */
    public TreeMap<String, String> retrieveWeeks( YtdUtilization ytdUtilization );

    /**
     * @param designation
     * @return
     */
    public List<String> retrieveEmployeeList();

    /**
     * @param weekMap
     * @return
     */
    public TreeMap<String, TreeMap<Integer, String>> retrieveFiscalYear( YtdUtilization ytdUtilization );

    /**
     * @param fyWeekMapping
     * @return
     * @throws SQLException
     */
    public List<Map<String, List<Double>>> getOutlookHours( Map<String, TreeMap<Integer, String>> fyWeekMapping )
        throws SQLException;

    /**
     * @return
     */
    public TreeMap<String, TreeMap<String, String>> getRollInRollOffDate( String designation );

    /**
     * @param employeeList
     * @return
     */
    public TreeMap<LocalDateTime, Integer> retrieveAvailableData( TreeMap<String, TreeMap<String, String>> employeeList );

}