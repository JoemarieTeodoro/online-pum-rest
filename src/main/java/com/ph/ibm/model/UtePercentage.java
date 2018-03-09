package com.ph.ibm.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.ph.ibm.util.DownloadUtilizationUtils;
import com.ph.ibm.util.FormatUtils;

/**
 * @author <a HREF="dacanam@ph.ibm.com">Marjay Dacanay</a>
 * @author <a HREF="teodorj@ph.ibm.com">Joemarie Teodoro</a>
 */
public class UtePercentage {

    private List<Map<String, Double>> listUteActualsOffshore;

    private List<Map<String, Double>> listUteActualsOnshore;

    public UtePercentage() {
        listUteActualsOffshore = new ArrayList<>();
        listUteActualsOnshore = new ArrayList<>();
    }

    /**
     * @return the listUteActualsOffshore
     */
    public List<Map<String, Double>> getListUteActualsOffshore() {
        return listUteActualsOffshore;
    }

    /**
     * @param listUteActualsOffshore the listUteActualsOffshore to set
     */
    public void setListUteActualsOffshore( List<Map<String, Double>> listUteActualsOffshore ) {
        this.listUteActualsOffshore = listUteActualsOffshore;
    }

    /**
     * @return the listUteActualsOnshore
     */
    public List<Map<String, Double>> getListUteActualsOnshore() {
        return listUteActualsOnshore;
    }

    /**
     * @param listUteActualsOnshore the listUteActualsOnshore to set
     */
    public void setListUteActualsOnshore( List<Map<String, Double>> listUteActualsOnshore ) {
        this.listUteActualsOnshore = listUteActualsOnshore;
    }

    public TreeMap<String, String> getUTEPercentage( List<Map<String, Double>> listActuals,
                                                     TreeMap<String, Integer> availableMap ) {
        TreeMap<String, String> utePercentage = new TreeMap<>();
        for( Entry<String, Integer> available : availableMap.entrySet() ){
            for( Map<String, Double> actual : listActuals ){
                populateUteMap( utePercentage, available, actual );
            }
        }
        return utePercentage;

    }

    public String calculateAverageUtePercentage( TreeMap<String, String> utePercentage ) {
        
        double total = 0.0;
        for( Entry<String, String> percentage : utePercentage.entrySet() ){
            total += DownloadUtilizationUtils.convertPercentageToDouble( percentage.getValue() );
        }
        double average = total / utePercentage.size();
        return DownloadUtilizationUtils.toPercent( average );
    }

    /**
     * @param utePercentage
     * @param available
     * @param actual
     */
    private void populateUteMap( TreeMap<String, String> utePercentage, Entry<String, Integer> available,
                                 Map<String, Double> actual ) {
        for( Entry<String, Double> actualHours : actual.entrySet() ){
            if( actualHours.getKey().equalsIgnoreCase( available.getKey() ) ){
                Double percentage = computeUTEPercentage( available, actualHours );
                utePercentage.put( actualHours.getKey(), FormatUtils.toPercentage( percentage ) );
            }
        }
    }

    private Double computeUTEPercentage( Entry<String, Integer> available, Entry<String, Double> actualHours ) {
        return actualHours.getValue() / available.getValue();
    }

}
