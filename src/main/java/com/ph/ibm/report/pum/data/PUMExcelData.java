package com.ph.ibm.report.pum.data;

import java.util.List;

import com.ph.ibm.model.Holiday;

public class PUMExcelData {

	public String fiscalYear;

	public List<Holiday> holidays;

	public List<PUMEmployeeData> employeeData;

	public PUMExcelData(String fiscalYear, List<Holiday> holidays,
			List<PUMEmployeeData> employeeData) {
		this.fiscalYear = fiscalYear;
		this.holidays = holidays;
		this.employeeData = employeeData;
	}

	public String getFiscalYear() {
		return fiscalYear;
	}

	public void setFiscalYear(String fiscalYear) {
		this.fiscalYear = fiscalYear;
	}

	public List<Holiday> getHolidays() {
		return holidays;
	}

	public void setHolidays(List<Holiday> holidays) {
		this.holidays = holidays;
	}

	public List<PUMEmployeeData> getEmployeeData() {
		return employeeData;
	}

	public void setEmployeeData(List<PUMEmployeeData> employeeData) {
		this.employeeData = employeeData;
	}
}
