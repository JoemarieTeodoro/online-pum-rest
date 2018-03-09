package com.ph.ibm.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.ph.ibm.util.DownloadUtilizationUtils;

/**
 * @author <a HREF="dacanam@ph.ibm.com">Marjay Dacanay</a>
 * @author <a HREF="teodorj@ph.ibm.com">Joemarie Teodoro</a>
 */
public class OverallUtilizationReport {

    private BillableHours billableHours;

    private ForecastedHours forecastedHours;

    private AvailableHours availableHours;

    private OutlookHours outlookHours;

    private UtePercentage utePercentage;

    public OverallUtilizationReport() {
        billableHours = new BillableHours();
        forecastedHours = new ForecastedHours();
        outlookHours = new OutlookHours();
        availableHours = new AvailableHours();
        utePercentage = new UtePercentage();
    }

    /**
     */
    public TreeMap<String, TreeMap<LocalDateTime, Integer>> getRollInRollOffHours( TreeMap<String, TreeMap<String, String>> employeeList ) {
        return availableHours.getTotal( employeeList );
    }

    /**
     */
    public TreeMap<String, Integer> getAvailableHoursOffshore( TreeMap<String, TreeMap<String, String>> employeeList,
                                                               Map<String, String> weekMap ) {
        return availableHours.retrieveAvailableWeeksOffshore( getRollInRollOffHours( employeeList ), weekMap );
    }

    /**
     */
    public TreeMap<String, Integer> getAvailableHoursOnshore( TreeMap<String, TreeMap<String, String>> employeeList,
                                                              Map<String, String> weekMap ) {
        return availableHours.retrieveAvailableWeeksOnshore( getRollInRollOffHours( employeeList ), weekMap );
    }

    public TreeMap<String, Integer> calculateWeeklyTotalAvailableHours( TreeMap<String, Integer> endingWeek ) {
        return availableHours.calculateWeeklyTotalAvailableHours( endingWeek );
    }

    public TreeMap<String, String> calculateWeeklyUtePercentage( List<Map<String, Double>> listUtePercentage,
                                                                 TreeMap<String, Integer> availableHoursMap ) {
        return utePercentage.getUTEPercentage( listUtePercentage, availableHoursMap );
    }

    /**
     * @return the billableHours
     */
    public BillableHours getBillableHours() {
        return billableHours;
    }

    /**
     * @return the forecastedHours
     */
    public ForecastedHours getForecastedHours() {
        return forecastedHours;
    }

    /**
     * @return the outlookHours
     */
    public OutlookHours getOutlookHours() {
        return outlookHours;
    }

    /**
     * @return the endingWeek
     */
    public TreeMap<String, Integer> getEndingWeek() {
        return availableHours.getEndingWeek();
    }

    /**
     * @return the outlookHours
     */
    public UtePercentage getUtePercentage() {
        return utePercentage;
    }

    /**
     * @param listOffshoreActualHours
     * @param listOnshoreActualHours
     */
    public void setActualHours( List<Map<String, Double>> listOffshoreActualHours,
                                List<Map<String, Double>> listOnshoreActualHours ) {
        billableHours.setOffshoreHours( listOffshoreActualHours );
        billableHours.setOnshoreHours( listOnshoreActualHours );

    }

    public void setBillableOffshoreTotalHours( Map<String, Double> totalOffshoreHours,
                                               Map<String, Double> totalOnshoreHours ) {
        billableHours.setBillableOffshoreTotalHours( totalOffshoreHours );
        billableHours.setBillableOnshoreTotalHours( totalOnshoreHours );
    }
    
    /**
     */
    public void setGrandTotalBillableHours( Double grandTotalHours ) {
        billableHours.setSumOfTotalBillableHours( grandTotalHours );
    }

    /**
     */
    public Double getGrandTotalBillableHours() {
        return billableHours.getSumOfTotalBillableHours();
    }

    /**
     */
    public void setGrandTotalForecastedHours( Double grandTotalHours ) {
        forecastedHours.setSumOfTotalForecastedHours( grandTotalHours );
    }

    /**
     */
    public Double getGrandTotalForecastedHours() {
        return forecastedHours.getSumOfTotalForecastedHours();
    }

    /**
     * @param listOffshoreForecastHours
     * @param listOnshoreForecastHours
     */
    public void setForecastedHours( List<Map<String, Double>> listOffshoreForecastHours,
                                    List<Map<String, Double>> listOnshoreForecastHours ) {
        forecastedHours.setOffshoreHours( listOffshoreForecastHours );
        forecastedHours.setOnshoreHours( listOnshoreForecastHours );

    }

    /**
     * @param listOffshoreForecastHours
     * @param listOnshoreForecastHours
     */
    public void setOutlookHours( Map<String, Double> listOutlookHours ) {
        outlookHours.setOutlookHours( listOutlookHours );
    }

    /**
     */
    public void setGrandTotalAvailableHours( Double grandTotalHours ) {
        availableHours.setSumOfTotalAvailableHours( grandTotalHours );
    }

    /**
     */
    public Double getGrandTotalAvailableHours() {
        return availableHours.getSumOfTotalAvailableHours();
    }

    /**
     * @param listOnshoreForecastHours
     */
    public void setUTEActualTotalHours( List<Map<String, Double>> listUteActualsOffshore,
                                        List<Map<String, Double>> listUteActualsOnshore ) {
        utePercentage.setListUteActualsOffshore( listUteActualsOffshore );
        utePercentage.setListUteActualsOnshore( listUteActualsOnshore );
    }

    public Double calculateSumOfTotalAvailableHours( TreeMap<String, Integer> availableHoursMap ) {
        return availableHours.calculateSumOfTotalAvailableHours( availableHoursMap );
    }

    public Double calculateSumOfGrandTotalAvailableHours( Double offshoreHours, Double onshoreHours ) {
        Double grandTotalAvailableHours = availableHours.calculateSumOfGrandTotal( offshoreHours, onshoreHours );
        outlookHours.setSumOfTotalAvailableHours( grandTotalAvailableHours );
        return grandTotalAvailableHours;
    }

    public Double calculateSumOfTotalBillableHours( List<Map<String, Double>> billableHoursMap ) {
        return billableHours.calculateSumOfTotalBillableHours( billableHoursMap );
    }

    public Double calculateSumOfGrandTotalBillableHours( Double offshoreHours, Double onshoreHours ) {
        return billableHours.calculateSumOfGrandTotalBillableHours( offshoreHours, onshoreHours );
    }
    
    public Double calculateSumOfTotalForecastedHours( List<Map<String, Double>> forecastedHoursMap ) {
        return forecastedHours.calculateSumOfTotalForecastedHours( forecastedHoursMap );
    }

    public Double calculateSumOfGrandTotalForecastedHours( Double offshoreHours, Double onshoreHours ) {
        return forecastedHours.calculateSumOfGrandTotalForecastedHours( offshoreHours, onshoreHours );
    }

    public String calculateAverageUtePercentage( TreeMap<String, String> ute ) {
        return utePercentage.calculateAverageUtePercentage( ute );
    }

    public Double calculateSumOfGrandTotalBillableHours1( Double offshoreHours, Double onshoreHours ) {
        return billableHours.calculateSumOfGrandTotalBillableHours( offshoreHours, onshoreHours );
    }
    
    public TreeMap<String, String> getReportGrandTotalMap (List<Map<String, Double>> listOffshoreHours, List<Map<String, Double>> listOnshoreHours) {
        TreeMap<String, String> totalHoursMap =
            DownloadUtilizationUtils.populateTotalHoursList( listOffshoreHours, listOnshoreHours );
        return totalHoursMap; 
    }

    public TreeMap<String, String> calculateSumOfTotalOutlookHours( Map<String, Double> outlookHoursMap ) {
        return outlookHours.calculateSumOfTotalOutlookHours( outlookHoursMap );
    }

    public TreeMap<String, String> getReportGrandTotalMap( Map<String, Integer> listOffshoreHours,
                                                           Map<String, Integer> listOnshoreHours ) {
        TreeMap<String, String> totalHoursMap =
            DownloadUtilizationUtils.populateTotalHoursList( listOffshoreHours, listOnshoreHours );
        return totalHoursMap;
    }

    public TreeMap<String, String> getReportGrandTotalMap( TreeMap<String, String> totalBillableHours,
                                                           TreeMap<String, String> totalAvailableHours ) {
        TreeMap<String, String> totalHoursMap =
            DownloadUtilizationUtils.populateTotalHoursList( totalBillableHours, totalAvailableHours );
        return totalHoursMap;
    }


}
