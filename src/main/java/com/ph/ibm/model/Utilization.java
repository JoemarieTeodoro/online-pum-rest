package com.ph.ibm.model;

public class Utilization extends BaseAuditBean{
	
	private Long utilizationId;
	private String employeeSerial;
	private String year;
	private String utilizationJson;
    private String quarter1;
    private String quarter2;
    private String quarter3;
    private String quarter4;
    private String ytd;
	
	public Utilization() {
		super();
	}
	
	public Utilization(String employeeSerial, String year, String utilizationJson) {
		super();
		this.employeeSerial = employeeSerial;
		this.year = year;
		this.utilizationJson = utilizationJson;
	}

	public Utilization(Long utilizationId, String employeeSerial, String year, String utilizationJson) {
		super();
		this.utilizationId = utilizationId;
		this.employeeSerial = employeeSerial;
		this.year = year;
		this.utilizationJson = utilizationJson;
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

    public String getQuarter1() {
        return quarter1;
    }

    public void setQuarter1( String quarter1 ) {
        this.quarter1 = quarter1;
    }

    public String getQuarter2() {
        return quarter2;
    }

    public void setQuarter2( String quarter2 ) {
        this.quarter2 = quarter2;
    }

    public String getQuarter3() {
        return quarter3;
    }

    public void setQuarter3( String quarter3 ) {
        this.quarter3 = quarter3;
    }

    public String getQuarter4() {
        return quarter4;
    }

    public void setQuarter4( String quarter4 ) {
        this.quarter4 = quarter4;
    }

    public String getYtd() {
        return ytd;
    }

    public void setYtd( String ytd ) {
        this.ytd = ytd;
    }
}
 