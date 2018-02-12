package com.ph.ibm.report.pum.data;

import java.util.Map;

public class PUMQuarterlyUtilizationData extends PUMHoursData{

	/**
	 * "Q1" "Q2" "Q3" "Q4"
	 */
	public String quarterName;
	
	/**
	 * Sample: (January, new PUMQuarterlyUtilizationData(....))
	 */
	public Map<String,PUMMonthlyUtilizationData> monthDatas;

	public PUMQuarterlyUtilizationData( Map<String,PUMMonthlyUtilizationData> monthDatas, int actualHours, 
			int availableHours) {
		super(actualHours, availableHours);
		this.monthDatas = monthDatas;
	}
	public String getQuarterName() {
		return quarterName;
	}

	public void setQuarterName(String quarterName) {
		this.quarterName = quarterName;
	}

	public Map<String,PUMMonthlyUtilizationData> getMonthDatas() {
		return monthDatas;
	}

	public void setMonthDatas(Map<String,PUMMonthlyUtilizationData> monthDatas) {
		this.monthDatas = monthDatas;
	}
	
}
