package com.ph.ibm.model;

public class Utilization extends BaseAuditBean{
	
	private Long utilizationId;
	private String employeeSerial;
	private String year;
	private String utilizationJson;
	
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
}
 