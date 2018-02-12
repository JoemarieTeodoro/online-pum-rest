package com.ph.ibm.report.pum.renderer;

import java.util.LinkedList;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFSheet;

import com.ph.ibm.model.Holiday;
import com.ph.ibm.report.pum.PUMUtilizationConstants;
import com.ph.ibm.report.pum.data.PUMQuarterlyUtilizationData;

public class PUMFirstQuarterMonthsDataRenderer extends PUMMonthlyDataRenderer {
	
	public PUMFirstQuarterMonthsDataRenderer(XSSFSheet dataSheet, List<PUMQuarterlyUtilizationData> quarterUtilizations, List<Holiday> holidays, int columnStartIndex) {
		super(dataSheet,quarterUtilizations,holidays,columnStartIndex);
	}

	@Override
	public String getQuarter() {
		return PUMUtilizationConstants.Q1_HEADER;
	}

	@Override
	public List<String> getIncludedMonths() {
		List<String> list = new LinkedList<>();
		list.add(PUMUtilizationConstants.JANUARY);
		list.add(PUMUtilizationConstants.FEBUARY);
		list.add(PUMUtilizationConstants.MARCH);
		return list;
	}
}
