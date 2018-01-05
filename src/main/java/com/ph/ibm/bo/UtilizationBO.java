
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

public class UtilizationBO {

    private static final int FIFTH_COLUMN = 4;
    private static final int FOURTH_COLUMN = 3;
    private static final int THIRD_COLUMN = 2;
    private static final int SECOND_COLUMN = 1;
    private static final int FIRST_COLUMN = 0;

    UtilizationRepository utilizationRepository = new UtilizationRepositoryImpl();

    private Logger logger = Logger.getLogger( UtilizationBO.class );

    public Utilization getEmployeeUtilization( String serial, String year ) {
        List<Double> lstQuarterlyUtilization = new ArrayList<Double>();
        List<String> lstForecastedUtilization = new ArrayList<String>();
        Utilization forecastedUtilization = new Utilization();
        try{
            lstQuarterlyUtilization = utilizationRepository.getQuarterlyUtilizationHours( serial, year );
            lstForecastedUtilization = computeForecastedUtilization( lstQuarterlyUtilization );
            forecastedUtilization = setUtilization( lstForecastedUtilization );
        }
        catch( Exception e ){
            e.printStackTrace();
            logger.error( e );
        }
        return forecastedUtilization;

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
     * @param employeeUtilization
     * @return utilization
     */

    public Utilization setUtilization( List<String> employeeUtilization ) {

        Utilization utilization = new Utilization();

        utilization.setQuarter1( employeeUtilization.get( FIRST_COLUMN ) );
        utilization.setQuarter2( employeeUtilization.get( SECOND_COLUMN ) );
        utilization.setQuarter3( employeeUtilization.get( THIRD_COLUMN ) );
        utilization.setQuarter4( employeeUtilization.get( FOURTH_COLUMN ) );
        utilization.setYtd( employeeUtilization.get( FIFTH_COLUMN ) );
        return utilization;

    }
}
