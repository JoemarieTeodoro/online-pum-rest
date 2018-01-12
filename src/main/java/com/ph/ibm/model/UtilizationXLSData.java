package com.ph.ibm.model;

import java.util.Date;
import java.util.List;

public class UtilizationXLSData {
	
	public Date fiscalYearStartDate;

	public String filePath;
	
	public int endIndex;
	
	public List<UtilizationRowData> dataRows;
	
	public UtilizationXLSData() {
	}

	public UtilizationXLSData(String filePath, int endIndex, List<UtilizationRowData> dataRows, Date fiscalYearStartDate) {
		this.filePath = filePath;
		this.endIndex = endIndex;
		this.dataRows = dataRows;
		this.fiscalYearStartDate = fiscalYearStartDate;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public int getEndIndex() {
		return endIndex;
	}

	public void setEndIndex(int endIndex) {
		this.endIndex = endIndex;
	}

	public List<UtilizationRowData> getDataRows() {
		return dataRows;
	}

	public void setDataRows(List<UtilizationRowData> dataRows) {
		this.dataRows = dataRows;
	}
	
	public Date getFiscalYearStartDate() {
		return fiscalYearStartDate;
	}

	public void setFiscalYearStartDate(Date fiscalYearStartDate) {
		this.fiscalYearStartDate = fiscalYearStartDate;
	}
}
