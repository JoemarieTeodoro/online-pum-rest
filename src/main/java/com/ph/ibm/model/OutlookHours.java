package com.ph.ibm.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.ph.ibm.util.FormatUtils;

/**
 * @author <a HREF="dacanam@ph.ibm.com">Marjay Dacanay</a>
 * @author <a HREF="teodorj@ph.ibm.com">Joemarie Teodoro</a>
 */
public class OutlookHours {

    private static final int IDX_ONE = 1;

    private static final int IDX_ZERO = 0;

    private Map<String, Double> outlookHours;

    private Double sumOfTotalAvailableHours;


    private static final String ACTUALS_PLUS_REMAINING_HEADER = "Sum of QTD (Actuals + Remaining) ";

    private static final String QTD_AVAILABLE_HEADER = "Sum of QTD Available";

    public OutlookHours() {
        outlookHours = new TreeMap<>();
    }

    /**
     * @return the offshoreHours
     */
    public Map<String, Double> getOutlookHours() {
        return outlookHours;
    }

    /**
     * @param offshoreHours the offshoreHours to set
     */
    public void setOutlookHours( Map<String, Double> outlookHours ) {
        this.outlookHours = outlookHours;
    }

    public void setSumOfTotalAvailableHours( Double sumOfTotalAvailableHours ) {
        this.sumOfTotalAvailableHours = sumOfTotalAvailableHours;
    }

    public Double getSumOfTotalAvailableHours() {
        return sumOfTotalAvailableHours;
    }

    public TreeMap<String, String> calculateSumOfTotalOutlookHours( Map<String, Double> outlookHours ) {
        double total = 0.0;
        List<Double> hours = new ArrayList<>();
        TreeMap<String, String> sumOfTotalHours = new TreeMap<>();
        for( Entry<String, Double> outlookElement : outlookHours.entrySet() ){
            hours.add( outlookElement.getValue() );
        }

        double average = hours.get( IDX_ZERO ) / hours.get( IDX_ONE );
        sumOfTotalHours.put( "SUM OF QTD%", FormatUtils.toPercentage( average ) );
        return sumOfTotalHours;
    }

    public Map<String, Double> calculateTotalHours( List<Map<String, List<Double>>> actualHours ) {
        Map<String, Double> totalHours = computeTotalHours( actualHours );
        return totalHours;
    }

    private Map<String, Double> computeTotalHours( List<Map<String, List<Double>>> actualHours ) {
        Map<String, Double> totalHours = new TreeMap<>();
        Double total = 0.0;
        for( Map<String, List<Double>> map : actualHours ){
            for( Map.Entry<String, List<Double>> hourList : map.entrySet() ){
                total = computeTotalHours( total, hourList );
            }
        }
        totalHours.put( ACTUALS_PLUS_REMAINING_HEADER, total );
        totalHours.put( QTD_AVAILABLE_HEADER, getSumOfTotalAvailableHours() );
        return totalHours;
    }

    /**
     * @param total
     * @param hourList
     * @return
     */
    private Double computeTotalHours( Double total, Map.Entry<String, List<Double>> hourList ) {
        for( Double hours : hourList.getValue() ){
            total += hours;
        }
        return total;
    }


}
