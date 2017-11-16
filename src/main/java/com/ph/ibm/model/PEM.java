package com.ph.ibm.model;

public class PEM {
	
	private String pemSerial;
	
	private String employeeSerial;
	
	private String startDate;
	
	private String endDate;
	
	public PEM(){
		
	}

	public PEM(String pemSerial, String employeeSerial, String startDate, String endDate) {
		this.pemSerial = pemSerial;
		this.employeeSerial = employeeSerial;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	public String getPEMSerial() {
		return pemSerial;
	}

	public void setPEMSerial(String pEMSerial) {
		pemSerial = pEMSerial;
	}

	public String getEmployeeSerial() {
		return employeeSerial;
	}

	public void setEmployeeSerial(String employeeSerial) {
		this.employeeSerial = employeeSerial;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

    @Override
    public String toString(){
    	return String.format( "PEM!\n\nPEM Serial No: %s \nEmployee Serial No: %s \nStart Date: %s \nEnd date: %s",
    			 this.pemSerial, this.employeeSerial, this.startDate, this.endDate);
    }
}
