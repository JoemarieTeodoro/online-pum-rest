package com.ph.ibm.report.pum.data;

public class PUMHoursData {
	
	public double actualHours;
	
	public double availableHours;

	public PUMHoursData(double actualHours, double availableHours) {
		this.actualHours = actualHours;
		this.availableHours = availableHours;
	}

	public double getActualHours() {
		return actualHours;
	}

	public void setActualHours(double actualHours) {
		this.actualHours = actualHours;
	}

	public double getAvailableHours() {
		return availableHours;
	}

	public void setAvailableHours(double availableHours) {
		this.availableHours = availableHours;
	}
}
