
package com.ph.ibm.bo;

import static com.ph.ibm.util.FormatUtils.toPercentage;
import static com.ph.ibm.util.OpumConstants.COUNT_OF_WEEKS_PER_QUARTER;
import static com.ph.ibm.util.OpumConstants.TOTAL_NUMBER_OF_WEEKS;
import static com.ph.ibm.util.OpumConstants.TOTAL_WEEKLY_HOURS;

/**
 * 
 * @author <a HREF="mailto:dacanam@ph.ibm.com">Marjay Dacanay</a>
 * @author <a HREF="mailto:balocaj@ph.ibm.com">Jerven Balocating</a>
 */

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.ph.ibm.model.Utilization;
import com.ph.ibm.repository.UtilizationRepository;
import com.ph.ibm.repository.impl.UtilizationRepositoryImpl;
import com.ph.ibm.util.OpumConstants;

public class UtilizationBO {

    private static final int FIFTH_COLUMN = 4;

    private static final int FOURTH_COLUMN = 3;

    private static final int THIRD_COLUMN = 2;

    private static final int SECOND_COLUMN = 1;

    private static final int FIRST_COLUMN = 0;

    UtilizationRepository utilizationRepository = new UtilizationRepositoryImpl();

    private Logger logger = Logger.getLogger( UtilizationBO.class );

    public Utilization getEmployeeUtilization( String serial, String year ) {
        List<String> lstForecastedUtilization = new ArrayList<String>();
        List<String> lstActualUtilization = new ArrayList<String>();
        Utilization utilization = new Utilization();
        try{
            lstForecastedUtilization = computeEmployeeUtilization(utilizationRepository.getEmployeeUtilization( serial, year, OpumConstants.FORECAST_UTILIZATION ) );
            lstActualUtilization = computeEmployeeUtilization( utilizationRepository.getEmployeeUtilization( serial, year, OpumConstants.ACTUAL_UTILIZATION ) );
            utilization = setUtilization( lstForecastedUtilization, lstActualUtilization );
        }
        catch( Exception e ){
            e.printStackTrace();
            logger.error( e );
        }
        return utilization;

    }

    /**
     * Method add and compute utilization to a list of string
     * 
     * @param lstQuarterlyUtilization
     * @return String
     */
    public List<String> computeForecastedUtilization( List<Double> lstQuarterlyUtilization ) {
        List<String> lstForecastedUtilization = new ArrayList<String>();
        double totalHours = 0;
        for( Double quarterlyUtilization : lstQuarterlyUtilization ){
            totalHours += quarterlyUtilization;
            lstForecastedUtilization.add( calculateQuarterlyUtilizationInPercentage( quarterlyUtilization ) );
        }
        lstForecastedUtilization.add( calculateYtdUtilizationInPercentage( totalHours ) );
        return lstForecastedUtilization;
    }

    /**
     * Method add and compute utilization to a list of string
     * 
     * @param lstWeeklyUtilizationHours
     * @return String
     */
    public List<String> computeEmployeeUtilization( List<Double> lstWeeklyUtilizationHours ) {
        List<String> lstActualUtilization = new ArrayList<String>();
        double quarterlyHours = 0;
        double totalHours = 0;
        int idxWeekCounter = 1;
        for( Double weeklyHours : lstWeeklyUtilizationHours ){
            quarterlyHours += weeklyHours;
            if( idxWeekCounter % OpumConstants.COUNT_OF_WEEKS_PER_QUARTER == 0 ){
                lstActualUtilization.add( calculateQuarterlyUtilizationInPercentage( quarterlyHours ) );
                totalHours += quarterlyHours;
                quarterlyHours = 0;
            }
            idxWeekCounter++;
        }
        lstActualUtilization.add( calculateYtdUtilizationInPercentage( totalHours ) );
        return lstActualUtilization;
    }

    /**
     * Method to calculate the yearly utilization of an employee
     * 
     * @param totalHours
     * @return String
     */
    private String calculateYtdUtilizationInPercentage( double totalHours ) {
        return toPercentage( ( totalHours / ( TOTAL_NUMBER_OF_WEEKS * TOTAL_WEEKLY_HOURS ) ) );
    }

    /**
     * Method to calculate the quarterly utilization of an employee
     * 
     * @param quarterlyUtilization
     * @return String
     */
    private String calculateQuarterlyUtilizationInPercentage( Double quarterlyUtilization ) {
        return toPercentage( ( quarterlyUtilization / ( COUNT_OF_WEEKS_PER_QUARTER * TOTAL_WEEKLY_HOURS ) ) );
    }

    /**
     * Method to set the utilization from list to Utilization object
     * 
     * @param employeeForecastedUtilization
     * @return utilization
     */

    public Utilization setUtilization( List<String> employeeForecastedUtilization, List<String> employeeActualUtilization ) {

        Utilization utilization = new Utilization();
        //set forecasted utilization to object
        utilization.setForecastedQuarter1( employeeForecastedUtilization.get( FIRST_COLUMN ) );
        utilization.setForecastedQuarter2( employeeForecastedUtilization.get( SECOND_COLUMN ) );
        utilization.setForecastedQuarter3( employeeForecastedUtilization.get( THIRD_COLUMN ) );
        utilization.setForecastedQuarter4( employeeForecastedUtilization.get( FOURTH_COLUMN ) );
        utilization.setForecastedYtd( employeeForecastedUtilization.get( FIFTH_COLUMN ) );
        //set actual utilization to object
        utilization.setActualQuarter1( employeeActualUtilization.get( FIRST_COLUMN ) );
        utilization.setActualQuarter2( employeeActualUtilization.get( SECOND_COLUMN ) );
        utilization.setActualQuarter3( employeeActualUtilization.get( THIRD_COLUMN ) );
        utilization.setActualQuarter4( employeeActualUtilization.get( FOURTH_COLUMN ) );
        utilization.setActualYtd( employeeActualUtilization.get( FIFTH_COLUMN ) );

        return utilization;

    }
}
