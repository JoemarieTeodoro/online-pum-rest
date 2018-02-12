package com.ph.ibm.report.pum.data;

import java.util.Map;

public class PUMOverallUtilizationData extends PUMHoursData{
	
	public Map<String,PUMQuarterlyUtilizationData> quarterlyDatas;
	
	public Map<String,Integer> totalLeaves;

	public PUMOverallUtilizationData(Map<String,PUMQuarterlyUtilizationData> QuarterlyDatas, 
			Map<String,Integer> leaves, int actualHours, int availableHours) {
		super(actualHours, availableHours);
		this.quarterlyDatas = QuarterlyDatas;
		this.totalLeaves = leaves;
	}

	public Map<String,PUMQuarterlyUtilizationData> getQuarterlyDatas() {
		return quarterlyDatas;
	}

	public void setQuarterlyDatas(Map<String,PUMQuarterlyUtilizationData> quarterlyDatas) {
		this.quarterlyDatas = quarterlyDatas;
	}

	public Map<String, Integer> getTotalLeaves() {
		return totalLeaves;
	}

	public void setTotalLeaves(Map<String, Integer> leaves) {
		this.totalLeaves = leaves;
	}
}
