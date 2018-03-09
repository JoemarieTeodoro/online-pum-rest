package com.ph.ibm.model;

/**
 * @author <a HREF="dacanam@ph.ibm.com">Marjay Dacanay</a>
 * @author <a HREF="teodorj@ph.ibm.com">Joemarie Teodoro</a>
 */
public class YtdUtilization {

    private String startDate;

    private String endDate;

    public YtdUtilization(String startDate, String endDate) {
    	this.startDate = startDate;
    	this.endDate = endDate;
    }
    
    /**
     * @return the startDate
     */
    public String getStartDate() {
        return startDate;
    }

    /**
     * @param startDate the startDate to set
     */
    public void setStartDate( String startDate ) {
        this.startDate = startDate;
    }

    /**
     * @return the endDate
     */
    public String getEndDate() {
        return endDate;
    }

    /**
     * @param endDate the endDate to set
     */
    public void setEndDate( String endDate ) {
        this.endDate = endDate;
    }

}
