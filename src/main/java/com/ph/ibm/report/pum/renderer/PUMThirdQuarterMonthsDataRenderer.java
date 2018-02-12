package com.ph.ibm.report.pum.renderer;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFSheet;

import com.ph.ibm.model.Holiday;
import com.ph.ibm.report.pum.PUMUtilizationConstants;
import com.ph.ibm.report.pum.data.PUMQuarterlyUtilizationData;

public class PUMThirdQuarterMonthsDataRenderer extends PUMMonthlyDataRenderer{
	public PUMThirdQuarterMonthsDataRenderer(XSSFSheet dataSheet, List<PUMQuarterlyUtilizationData> quarterUtilizations, List<Holiday> holidays, int columnStartIndex) {
		super(dataSheet,quarterUtilizations,holidays,columnStartIndex);
	}
	
	@Override
	public String getQuarter() {
		return PUMUtilizationConstants.Q3_HEADER;
	}

	@Override
	public List<String> getIncludedMonths() {
		List<String> list = new ArrayList<>();
		list.add(PUMUtilizationConstants.JULY);
		list.add(PUMUtilizationConstants.AUGUST);
		list.add(PUMUtilizationConstants.SEPTEMBER);
		return list;
	}
}
