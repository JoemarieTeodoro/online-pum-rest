package com.ph.ibm.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.ph.ibm.util.FormatUtils;

/**
 * @author <a HREF="dacanam@ph.ibm.com">Marjay Dacanay</a>
 * @author <a HREF="teodorj@ph.ibm.com">Joemarie Teodoro</a>
 */
public class AvailableHours {

    private TreeMap<String, TreeMap<LocalDateTime, Integer>> availableHours;

    private TreeMap<String, Integer> endingWeekOffshore;

    private TreeMap<String, Integer> endingWeekOnshore;

    private double sumOfTotalAvailableHours;

    private static final int WORKING_HOURS = 8;

    private static final String FRIDAY = "FRIDAY";

    private static final String SATURDAY = "SATURDAY";

    private static final String SUNDAY = "SUNDAY";

    private static final char UNDERSCORE = '_';

    public AvailableHours() {
        endingWeekOffshore = new TreeMap<>();
        endingWeekOnshore = new TreeMap<>();
    }

    /**
     * @return the sumOfTotalAvailableHours
     */
    public double getSumOfTotalAvailableHours() {
        return sumOfTotalAvailableHours;
    }

    /**
     * @param sumOfTotalAvailableHours the sumOfTotalAvailableHours to set
     */
    public void setSumOfTotalAvailableHours( double sumOfTotalAvailableHours ) {
        this.sumOfTotalAvailableHours = sumOfTotalAvailableHours;
    }

    /**
     * @return the total
     */
    public TreeMap<String, TreeMap<LocalDateTime, Integer>> getTotal( TreeMap<String, TreeMap<String, String>> employeeList ) {
        TreeMap<String, TreeMap<LocalDateTime, Integer>> employeeWeekMap = new TreeMap<>();
        TreeMap<LocalDateTime, Integer> weekMapping = new TreeMap<>();
        for( Entry<String, TreeMap<String, String>> employeeWeeks : employeeList.entrySet() ){
            for( Entry<String, String> employeeRollInRollOffDate : employeeWeeks.getValue().entrySet() ){
                weekMapping = calculateTotalAvailableHours( employeeRollInRollOffDate );
            }
            employeeWeekMap.put( employeeWeeks.getKey(), weekMapping );
            weekMapping = new TreeMap<>();
        }
        return employeeWeekMap;
    }

    /**
     * Retrieve the week endings within the specified start date and end date
     * 
     * @return the endingWeek
     */
    public TreeMap<String, Integer> retrieveAvailableWeeksOffshore( TreeMap<String, TreeMap<LocalDateTime, Integer>> hoursMap,
                                                                    Map<String, String> weekMap ) {
        endingWeekOffshore = populateWeekMap( hoursMap, weekMap );

        return endingWeekOffshore;
    }

    /**
     * Retrieve the week endings within the specified start date and end date
     * 
     * @return the endingWeek
     */
    public TreeMap<String, Integer> retrieveAvailableWeeksOnshore( TreeMap<String, TreeMap<LocalDateTime, Integer>> hoursMap,
                                                                   Map<String, String> weekMap ) {
        endingWeekOnshore = populateWeekMap( hoursMap, weekMap );

        return endingWeekOnshore;
    }

    public TreeMap<String, Integer> calculateWeeklyTotalAvailableHours( TreeMap<String, Integer> endingWeek ) {
        return populateWeeklyTotalHours( endingWeek );

    }

    /**
     * @return the endingWeek
     */
    public TreeMap<String, Integer> getEndingWeek() {
        return endingWeekOffshore;
    }

    public Double calculateSumOfTotalAvailableHours( TreeMap<String, Integer> availableHoursMap ) {
        double totalHours = 0.0;

        for( Entry<String, Integer> availableHours : availableHoursMap.entrySet() ){
            totalHours += availableHours.getValue();
        }
        return totalHours;
    }

    public Double calculateSumOfGrandTotal( Double offshoreHours, Double onshoreHours ) {

        return offshoreHours + onshoreHours;
    }

    private TreeMap<LocalDateTime, Integer> calculateTotalAvailableHours( Entry<String, String> employeeRollInRollOffDate ) {
        LocalDate fromDate = FormatUtils.toDBDateFormat( employeeRollInRollOffDate.getKey() );
        LocalDate toDate = FormatUtils.toDBDateFormat( employeeRollInRollOffDate.getValue() );
        LocalDateTime counterDateTime = LocalDateTime.of( fromDate, LocalTime.from( LocalTime.MIN ) );
        LocalDateTime toDateTime = LocalDateTime.of( toDate, LocalTime.from( LocalTime.MIN ) );
        TreeMap<LocalDateTime, Integer> weekMapping = new TreeMap<>();
        while( isBeforeEndDate( counterDateTime, toDateTime ) ){
            int totalHours = 0;
            while( isWeekDays( counterDateTime, toDateTime ) ){
                totalHours += WORKING_HOURS;
                if( isDayFriday( counterDateTime ) || counterDateTime.equals( toDateTime ) ){
                    break;
                }
                counterDateTime = counterDateTime.plusDays( 1 );
            }
            if( totalHours == 0 ){
                counterDateTime = counterDateTime.plusDays( 1 );
                continue;
            }
            while( !isDayFriday( counterDateTime ) ){
                counterDateTime = counterDateTime.plusDays( 1 );
            }
            weekMapping.put( counterDateTime, totalHours );
            counterDateTime = counterDateTime.plusDays( 1 );
            totalHours = 0;
        }
        return weekMapping;
    }

    /**
     * @param endingWeek
     * @param totalWeeklyHours
     */
    private TreeMap<String, Integer> populateWeeklyTotalHours( TreeMap<String, Integer> endingWeek ) {
        int hours;
        TreeMap<String, Integer> totalWeeklyHours = new TreeMap<>();
        for( Entry<String, Integer> end : endingWeek.entrySet() ){
            String key = removeSerialInKey( end );
            if( totalWeeklyHours.containsKey( key ) ){
                hours = totalWeeklyHours.get( key ) + end.getValue();
                totalWeeklyHours.put( key, hours );
            }
            else{
                totalWeeklyHours.put( key, end.getValue() );
            }
        }
        return totalWeeklyHours;
    }

    /**
     * @param hoursMap
     * @param weekMap
     */
    private TreeMap<String, Integer> populateWeekMap( TreeMap<String, TreeMap<LocalDateTime, Integer>> hoursMap,
                                                      Map<String, String> weekMap ) {
        TreeMap<String, Integer> endingWeek = new TreeMap<>();
        for( Entry<String, TreeMap<LocalDateTime, Integer>> map : hoursMap.entrySet() ){
            for( Entry<LocalDateTime, Integer> weekMapping : map.getValue().entrySet() ){
                LocalDate weekEndingDate = weekMapping.getKey().toLocalDate();
                String weekEnding = weekEndingDate.toString();
                if( weekMap.containsValue( weekEnding ) ){
                    endingWeek.put( concatWeekEndingAndSerialForKey( weekEnding, map.getKey() ),
                        weekMapping.getValue() );
                }
            }
        }
        return endingWeek;
    }

    private boolean isWeekDays( LocalDateTime counterDateTime, LocalDateTime toDateTime ) {
        return !isWeekEnd( counterDateTime ) && isBeforeEndDate( counterDateTime, toDateTime );
    }

    private boolean isDayFriday( LocalDateTime counterDateTime ) {
        return counterDateTime.getDayOfWeek().name().equalsIgnoreCase( FRIDAY );
    }

    private String concatWeekEndingAndSerialForKey( String weekEnding, String serial ) {
        return weekEnding + UNDERSCORE + serial;
    }

    private String removeSerialInKey( Entry<String, Integer> end ) {
        return end.getKey().substring( 0, end.getKey().indexOf( UNDERSCORE ) );
    }

    private boolean isBeforeEndDate( LocalDateTime counterDateTime, LocalDateTime toDateTime ) {
        return counterDateTime.isBefore( toDateTime ) || counterDateTime.isEqual( toDateTime );
    }

    private boolean isWeekEnd( LocalDateTime counterDateTime ) {
        return counterDateTime.getDayOfWeek().name().equalsIgnoreCase( SATURDAY ) ||
            counterDateTime.getDayOfWeek().name().equalsIgnoreCase( SUNDAY );
    }

}
