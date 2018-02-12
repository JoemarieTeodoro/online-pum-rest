package com.ph.ibm.model;
import java.text.DateFormatSymbols;
public class PUMDownloadReportMonth {
	private int monthId;
	private String monthName;
	private String weekEnd;
	private int yearId;

	public PUMDownloadReportMonth() {}

	public PUMDownloadReportMonth(String monthName, String weekEnd) {
		this.monthName = monthName;
		this.weekEnd = weekEnd;
	}

	public int getMonthId() {
		return monthId;
	}

	public void setMonthId(int monthId) {
		this.monthId = monthId;
	}

	public String getMonthName() {
		return monthName;
	}
	public void setMonthName(String monthName) {
		this.monthName = monthName;
	}
	public String getWeekEnd() {
		return weekEnd;
	}
	public void setWeekEnd(String weekEnd) {
		this.weekEnd = weekEnd;
	}
	public int getYearId() {
		return yearId;
	}
	public void setYearId(int yearId) {
		this.yearId = yearId;
	}
	
	public String getMonthId(int month) {
	    return new DateFormatSymbols().getMonths()[month-1];
	}

}
