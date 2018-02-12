package com.ph.ibm.report.pum.data;

import java.util.Map;

public class PUMWeeklyUtilizationData {

	/**
	 * must be the friday of that week.
	 * 
	 * sample: 1/19/2018
	 */
	public String weekDate;
	
	/**
	 * Sample Data
	 * 
	 * Saturday, ""
	 * Sunday, ""
	 * Monday , 8.0
	 * Tuesday, 8.0
	 * Wednesday, VL
	 * Thursday, EL
	 * Friday, SL
	 */
	public Map<String,String> dailyData;
	
	public int total;

	public PUMWeeklyUtilizationData(String weekDate, Map<String, String> dailyData, int total) {
		super();
		this.weekDate = weekDate;
		this.dailyData = dailyData;
		this.total = total;
	}

	public String getWeekDate() {
		return weekDate;
	}

	public void setWeekDate(String weekDate) {
		this.weekDate = weekDate;
	}

	public Map<String, String> getDailyData() {
		return dailyData;
	}

	public void setDailyData(Map<String, String> dailyData) {
		this.dailyData = dailyData;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}
}
