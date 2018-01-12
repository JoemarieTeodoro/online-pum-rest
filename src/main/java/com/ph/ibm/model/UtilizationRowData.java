package com.ph.ibm.model;

import java.util.List;

public class UtilizationRowData {
	
	private static final int VALUE_PADDING = 1;

	private static final int ZERO_INTEGER = 0;

	public String empSerial;
	
	public String empName;
	
	public List<Integer> weekUtilization;
	
	public List<Integer> quarterUtilization;

	public UtilizationRowData(String empSerial, String empName, List<Integer> weekUtilization, List<Integer> quarterUtilization) {
		this.empSerial = empSerial;
		this.empName = empName;
		this.weekUtilization = weekUtilization;
		this.quarterUtilization = quarterUtilization;
	}

	public String getEmpSerial() {
		return empSerial;
	}

	public void setEmpSerial(String empSerial) {
		this.empSerial = empSerial;
	}

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	public List<Integer> getWeekUtilization() {
		return weekUtilization;
	}

	public void setWeekUtilization(List<Integer> weekUtilization) {
		this.weekUtilization = weekUtilization;
	}
	
	public List<Integer> getQuarterUtilization() {
		return quarterUtilization;
	}

	public void setQuarterUtilization(List<Integer> quarterUtilization) {
		this.quarterUtilization = quarterUtilization;
	}
	
	public int getWeekHour(int weekNumber) {
		if( weekUtilization == null || weekNumber == ZERO_INTEGER || weekUtilization.size() < weekNumber) {
			return ZERO_INTEGER;
		}
		return weekUtilization.get(weekNumber - VALUE_PADDING);
	}
	
	public int getQuarterHour(int quarterNumber) {
		if( quarterUtilization == null || quarterNumber == ZERO_INTEGER || quarterUtilization.size() < quarterNumber) {
			return ZERO_INTEGER;
		}
		return quarterUtilization.get(quarterNumber - VALUE_PADDING);
	}
	
}
