package com.ph.ibm.model;

public class Utilization extends BaseAuditBean{
	
	private Long utilizationId;
	private String employeeSerial;
	private String year;
	private String utilizationJson;
    private String forecastedQuarter1;
    private String forecastedQuarter2;
    private String forecastedQuarter3;
    private String forecastedQuarter4;
    private String forecastedYtd;
    private String actualQuarter1;
    private String actualQuarter2;
    private String actualQuarter3;
	private String actualQuarter4;
	private String actualYtd;

    //combined actual + forecast util
    private String combinedQuarter1;

    private String combinedQuarter2;

    private String combinedQuarter3;

    private String combinedQuarter4;

    private String combinedYtd;

	public Utilization() {
		super();
	}

    public Utilization( String employeeSerial, String year, String utilizationJson ) {
        super();
        this.employeeSerial = employeeSerial;
        this.year = year;
        this.utilizationJson = utilizationJson;
    }

    public Utilization( Long utilizationId, String employeeSerial, String year, String utilizationJson ) {
        super();
        this.utilizationId = utilizationId;
        this.employeeSerial = employeeSerial;
        this.year = year;
        this.utilizationJson = utilizationJson;
    }

    /**
     * @return the forecastedQuarter1
     */
    public String getForecastedQuarter1() {
        return forecastedQuarter1;
    }

    /**
     * @param forecastedQuarter1 the forecastedQuarter1 to set
     */
    public void setForecastedQuarter1( String forecastedQuarter1 ) {
        this.forecastedQuarter1 = forecastedQuarter1;
    }

    /**
     * @return the forecastedQuarter2
     */
    public String getForecastedQuarter2() {
        return forecastedQuarter2;
    }

    /**
     * @param forecastedQuarter2 the forecastedQuarter2 to set
     */
    public void setForecastedQuarter2( String forecastedQuarter2 ) {
        this.forecastedQuarter2 = forecastedQuarter2;
    }

    /**
     * @return the forecastedQuarter3
     */
    public String getForecastedQuarter3() {
        return forecastedQuarter3;
    }

    /**
     * @param forecastedQuarter3 the forecastedQuarter3 to set
     */
    public void setForecastedQuarter3( String forecastedQuarter3 ) {
        this.forecastedQuarter3 = forecastedQuarter3;
    }

    /**
     * @return the forecastedQuarter4
     */
    public String getForecastedQuarter4() {
        return forecastedQuarter4;
    }

    /**
     * @param forecastedQuarter4 the forecastedQuarter4 to set
     */
    public void setForecastedQuarter4( String forecastedQuarter4 ) {
        this.forecastedQuarter4 = forecastedQuarter4;
    }

    /**
     * @return the forecastedYtd
     */
    public String getForecastedYtd() {
        return forecastedYtd;
    }

    /**
     * @param forecastedYtd the forecastedYtd to set
     */
    public void setForecastedYtd( String forecastedYtd ) {
        this.forecastedYtd = forecastedYtd;
    }

    /**
     * @return the actualQuarter1
     */
    public String getActualQuarter1() {
        return actualQuarter1;
    }

    /**
     * @param actualQuarter1 the actualQuarter1 to set
     */
    public void setActualQuarter1( String actualQuarter1 ) {
        this.actualQuarter1 = actualQuarter1;
    }

    /**
     * @return the actualQuarter2
     */
    public String getActualQuarter2() {
        return actualQuarter2;
    }

    /**
     * @param actualQuarter2 the actualQuarter2 to set
     */
    public void setActualQuarter2( String actualQuarter2 ) {
        this.actualQuarter2 = actualQuarter2;
    }

    /**
     * @return the actualQuarter3
     */
    public String getActualQuarter3() {
        return actualQuarter3;
    }

    /**
     * @param actualQuarter3 the actualQuarter3 to set
     */
    public void setActualQuarter3( String actualQuarter3 ) {
        this.actualQuarter3 = actualQuarter3;
    }

    /**
     * @return the actualQuarter4
     */
    public String getActualQuarter4() {
        return actualQuarter4;
    }

    /**
     * @param actualQuarter4 the actualQuarter4 to set
     */
    public void setActualQuarter4( String actualQuarter4 ) {
        this.actualQuarter4 = actualQuarter4;
    }

    /**
     * @return the actualYtd
     */
    public String getActualYtd() {
        return actualYtd;
    }

    /**
     * @param actualYtd the actualYtd to set
     */
    public void setActualYtd( String actualYtd ) {
        this.actualYtd = actualYtd;
    }
	

	public Long getUtilizationId() {
		return utilizationId;
	}

	public void setUtilizationId(Long utilizationId) {
		this.utilizationId = utilizationId;
	}

	public String getEmployeeSerial() {
		return employeeSerial;
	}

	public void setEmployeeSerial(String employeeSerial) {
		this.employeeSerial = employeeSerial;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getUtilizationJson() {
		return utilizationJson;
	}

	public void setUtilizationJson(String utilizationJson) {
		this.utilizationJson = utilizationJson;
	}

    public String getCombinedQuarter1() {
        return combinedQuarter1;
    }

    public void setCombinedQuarter1( String combinedQuarter1 ) {
        this.combinedQuarter1 = combinedQuarter1;
    }

    public String getCombinedQuarter2() {
        return combinedQuarter2;
    }

    public void setCombinedQuarter2( String combinedQuarter2 ) {
        this.combinedQuarter2 = combinedQuarter2;
    }

    public String getCombinedQuarter3() {
        return combinedQuarter3;
    }

    public void setCombinedQuarter3( String combinedQuarter3 ) {
        this.combinedQuarter3 = combinedQuarter3;
    }

    public String getCombinedQuarter4() {
        return combinedQuarter4;
    }

    public void setCombinedQuarter4( String combinedQuarter4 ) {
        this.combinedQuarter4 = combinedQuarter4;
    }

    public String getCombinedYtd() {
        return combinedYtd;
    }

    public void setCombinedYtd( String combinedYtd ) {
        this.combinedYtd = combinedYtd;
    }

}
 