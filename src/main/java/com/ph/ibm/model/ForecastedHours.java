package com.ph.ibm.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

/**
 * @author <a HREF="dacanam@ph.ibm.com">Marjay Dacanay</a>
 * @author <a HREF="teodorj@ph.ibm.com">Joemarie Teodoro</a>
 */
public class ForecastedHours {

    private List<Map<String, Double>> offshoreHours;

    private List<Map<String, Double>> onshoreHours;

    private Map<String, Double> offshoreTotalHours;

    private Map<String, Double> onshoreTotalHours;

    private Double sumOfTotalForecastedHours;

    public ForecastedHours() {
        offshoreHours = new ArrayList<>();
        onshoreHours = new ArrayList<>();
        offshoreTotalHours = new TreeMap<>();
        onshoreTotalHours = new TreeMap<>();
    }

    /**
     * @return the sumOfTotalForecastedHours
     */
    public Double getSumOfTotalForecastedHours() {
        return sumOfTotalForecastedHours;
    }

    /**
     * @param sumOfTotalForecastedHours the sumOfTotalForecastedHours to set
     */
    public void setSumOfTotalForecastedHours( Double sumOfTotalForecastedHours ) {
        this.sumOfTotalForecastedHours = sumOfTotalForecastedHours;
    }

    /**
     * @return the offshoreHours
     */
    public List<Map<String, Double>> getOffshoreHours() {
        return offshoreHours;
    }

    /**
     * @param listOffshoreForecastHours the offshoreHours to set
     */
    public void setOffshoreHours( List<Map<String, Double>> listOffshoreForecastHours ) {
        this.offshoreHours = listOffshoreForecastHours;
    }

    /**
     * @return the offshoreTotalHours
     */
    public Map<String, Double> getOffshoreTotalHours() {
        return offshoreTotalHours;
    }

    /**
     * @param offshoreTotalHours the offshoreTotalHours to set
     */
    public void setOffshoreTotalHours( Map<String, Double> offshoreTotalHours ) {
        this.offshoreTotalHours = offshoreTotalHours;
    }

    /**
     * @return the onshoreHours
     */
    public List<Map<String, Double>> getOnshoreHours() {
        return onshoreHours;
    }

    /**
     * @param listOnshoreForecastHours the onshoreHours to set
     */
    public void setOnshoreHours( List<Map<String, Double>> listOnshoreForecastHours ) {
        this.onshoreHours = listOnshoreForecastHours;
    }

    /**
     * @return the onshoreTotalHours
     */
    public Map<String, Double> getOnshoreTotalHours() {
        return onshoreTotalHours;
    }

    /**
     * @param onshoreTotalHours the onshoreTotalHours to set
     */
    public void setOnshoreTotalHours( Map<String, Double> onshoreTotalHours ) {
        this.onshoreTotalHours = onshoreTotalHours;
    }

    public Map<String, Double> calculateOffshoreTotalHours( List<Map<String, List<Double>>> actualHours ) {
        Map<String, Double> offshore_totalHours = computeHours( actualHours );
        offshoreTotalHours = offshore_totalHours;
        return offshore_totalHours;
    }

    public Map<String, Double> calculateOnshoreTotalHours( List<Map<String, List<Double>>> actualHours ) {
        Map<String, Double> onshore_totalHours = computeHours( actualHours );
        onshoreTotalHours = onshore_totalHours;
        return onshore_totalHours;
    }

    private Map<String, Double> computeHours( List<Map<String, List<Double>>> actualHours ) {
        Map<String, Double> totalHours = new TreeMap<>();
        Double total = 0.0;
        for( Map<String, List<Double>> map : actualHours ){
            for( Map.Entry<String, List<Double>> hourList : map.entrySet() ){
                totalHours.put( hourList.getKey(), getTotalHours( total, hourList ) );
            }
        }
        return totalHours;
    }

    /**
     * @param total
     * @param hourList
     * @return
     */
    private Double getTotalHours( Double total, Map.Entry<String, List<Double>> hourList ) {
        for( Double hours : hourList.getValue() ){
            total += hours;
        }
        return total;
    }

    public Double calculateSumOfTotalForecastedHours( List<Map<String, Double>> forecastedHoursMap ) {
        double totalHours = 0.0;

        for( Map<String, Double> map : forecastedHoursMap ){
            for( Entry<String, Double> forecast : map.entrySet() ){
                totalHours += forecast.getValue();
            }
        }
        return totalHours;
    }

    public Double calculateSumOfGrandTotalForecastedHours( Double offshoreHours, Double onshoreHours ) {

        return offshoreHours + onshoreHours;
    }
}
