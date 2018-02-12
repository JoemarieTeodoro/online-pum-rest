package com.ph.ibm.report.pum.renderer;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFSheet;

import com.ph.ibm.model.Holiday;
import com.ph.ibm.report.pum.PUMUtilizationConstants;
import com.ph.ibm.report.pum.data.PUMQuarterlyUtilizationData;

public class PUMSecondQuarterMonthsDataRenderer extends PUMMonthlyDataRenderer{
	
	public PUMSecondQuarterMonthsDataRenderer(XSSFSheet dataSheet, List<PUMQuarterlyUtilizationData> quarterUtilizations, List<Holiday> holidays, int columnStartIndex) {
		super(dataSheet,quarterUtilizations,holidays,columnStartIndex);
	}
	
	@Override
	public String getQuarter() {
		return PUMUtilizationConstants.Q2_HEADER;
	}

	@Override
	public List<String> getIncludedMonths() {
		List<String> list = new ArrayList<>();
		list.add(PUMUtilizationConstants.APRIL);
		list.add(PUMUtilizationConstants.MAY);
		list.add(PUMUtilizationConstants.JUNE);
		return list;
	}

}
