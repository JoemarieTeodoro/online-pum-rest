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
public class BillableHours {

    private List<Map<String, Double>> offshoreHours;

    private List<Map<String, Double>> onshoreHours;

    private Map<String, Double> offshoreTotalHours;

    private Map<String, Double> onshoreTotalHours;

    private Double sumOfTotalBillableHours;

    public BillableHours() {
        offshoreHours = new ArrayList<>();
        onshoreHours = new ArrayList<>();
        offshoreTotalHours = new TreeMap<>();
        onshoreTotalHours = new TreeMap<>();
    }

    /**
     * @return the sumOfTotalBillableHours
     */
    public Double getSumOfTotalBillableHours() {
        return sumOfTotalBillableHours;
    }

    /**
     * @param sumOfTotalBillableHours the sumOfTotalBillableHours to set
     */
    public void setSumOfTotalBillableHours( Double sumOfTotalBillableHours ) {
        this.sumOfTotalBillableHours = sumOfTotalBillableHours;
    }

    /**
     * @return the offshoreHours
     */
    public List<Map<String, Double>> getOffshoreHours() {
        return offshoreHours;
    }

    /**
     * @param listOffshoreActualHours the offshoreHours to set
     */
    public void setOffshoreHours( List<Map<String, Double>> listOffshoreActualHours ) {
        this.offshoreHours = listOffshoreActualHours;
    }

    /**
     * @return the onshoreHours
     */
    public List<Map<String, Double>> getOnshoreHours() {
        return onshoreHours;
    }

    /**
     * @param listOnshoreActualHours the onshoreHours to set
     */
    public void setOnshoreHours( List<Map<String, Double>> listOnshoreActualHours ) {
        this.onshoreHours = listOnshoreActualHours;
    }

    public Map<String, Double> getOnshoreTotalHours() {
        return onshoreTotalHours;
    }

    public Map<String, Double> getOffshoreTotalHours() {
        return offshoreTotalHours;
    }

    public void setBillableOffshoreTotalHours( Map<String, Double> totalOffshoreHours ) {
        this.offshoreTotalHours = totalOffshoreHours;
    }

    public void setBillableOnshoreTotalHours( Map<String, Double> totalOnshoreHours ) {
        this.onshoreTotalHours = totalOnshoreHours;
    }

    public Map<String, Double> calculateTotalOffshoreHours( List<Map<String, List<Double>>> actualHours ) {
        Map<String, Double> offshore_TotalHours = computeHours( actualHours );
        offshoreTotalHours = offshore_TotalHours;
        return offshore_TotalHours;
    }

    public Map<String, Double> calculateTotalOnshoreHours( List<Map<String, List<Double>>> actualHours ) {
        Map<String, Double> onshore_TotalHours = computeHours( actualHours );
        onshoreTotalHours = onshore_TotalHours;
        return onshore_TotalHours;
    }

    private Map<String, Double> computeHours( List<Map<String, List<Double>>> actualHours ) {
        Map<String, Double> totalHours = new TreeMap<>();
        for( Map<String, List<Double>> map : actualHours ){
            for( Map.Entry<String, List<Double>> hourList : map.entrySet() ){
                totalHours.put( hourList.getKey(), computeTotalHours( hourList ) );
            }
        }
        return totalHours;
    }

    /**
     * @param total
     * @param hourList
     * @return total
     */
    private Double computeTotalHours( Map.Entry<String, List<Double>> hourList ) {
        double total = 0.0;
        for( Double hours : hourList.getValue() ){
            total += hours;
        }
        return total;
    }

    public Double calculateSumOfTotalBillableHours( List<Map<String, Double>> billableHoursMap ) {
        double totalHours = 0.0;

        for( Map<String, Double> map : billableHoursMap ){
            for( Entry<String, Double> billable : map.entrySet() ){
                totalHours += billable.getValue();
            }
        }
        return totalHours;
    }

    public Double calculateSumOfGrandTotalBillableHours( Double offshoreHours, Double onshoreHours ) {

        return offshoreHours + onshoreHours;
    }

}
