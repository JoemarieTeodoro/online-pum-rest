package com.ph.ibm.report.pum.renderer;

import java.util.LinkedList;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFSheet;

import com.ph.ibm.model.Holiday;
import com.ph.ibm.report.pum.PUMUtilizationConstants;
import com.ph.ibm.report.pum.data.PUMQuarterlyUtilizationData;

public class PUMFourthQuarterMonthsDataRenderer extends PUMMonthlyDataRenderer{

	
	public PUMFourthQuarterMonthsDataRenderer(XSSFSheet dataSheet, List<PUMQuarterlyUtilizationData> quarterUtilizations, List<Holiday> holidays, int columnStartIndex) {
		super(dataSheet,quarterUtilizations,holidays,columnStartIndex);
	}

	@Override
	public String getQuarter() {
		return PUMUtilizationConstants.Q4_HEADER;
	}

	@Override
	public List<String> getIncludedMonths() {
		List<String> list = new LinkedList<>();
		list.add(PUMUtilizationConstants.OCTOBER);
		list.add(PUMUtilizationConstants.NOVEMBER);
		list.add(PUMUtilizationConstants.DECEMBER);
		return list;
	}

}
