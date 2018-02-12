package com.ph.ibm.report.pum.data;

import java.util.List;
import java.util.Map;

public class PUMMonthlyUtilizationData extends PUMHoursData{
	
	public String monthName;

	public List<PUMWeeklyUtilizationData> weeklyDatas;
	
	public Map<String,Integer> totalLeaves;

	public PUMMonthlyUtilizationData(String monthName,
			List<PUMWeeklyUtilizationData> weeklyDatas, Map<String, Integer> leaves, 
			int actualHours, int availableHours) {
		super(actualHours, availableHours);
		this.monthName = monthName;
		this.weeklyDatas = weeklyDatas;
		this.totalLeaves = leaves;
	}

	public String getMonthName() {
		return monthName;
	}

	public void setMonthName(String monthName) {
		this.monthName = monthName;
	}

	public List<PUMWeeklyUtilizationData> getWeeklyDatas() {
		return weeklyDatas;
	}

	public void setWeeklyDatas(List<PUMWeeklyUtilizationData> weeklyDatas) {
		this.weeklyDatas = weeklyDatas;
	}

	public Map<String, Integer> getTotalLeaves() {
		return totalLeaves;
	}

	public void setTotalLeaves(Map<String, Integer> leaves) {
		this.totalLeaves = leaves;
	}
}
