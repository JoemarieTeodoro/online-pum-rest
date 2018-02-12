package com.ph.ibm.report.pum.renderer;

import java.util.List;

import org.apache.poi.ss.usermodel.IgnoredErrorType;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import com.ph.ibm.model.Holiday;
import com.ph.ibm.report.pum.data.PUMEmployeeData;
import com.ph.ibm.report.pum.data.PUMExcelData;

public class PUMYearlyRenderer extends PUMColumnRenderer implements PUMRenderer{ 
	
	private List<PUMEmployeeData> employeeData;
	
	private List<Holiday> holidays ;
	
	public PUMYearlyRenderer( int columnStartIndex, XSSFSheet dataSheet ,PUMExcelData data) {
		super(dataSheet,columnStartIndex);
		this.employeeData = data.getEmployeeData();
		this.holidays = data.getHolidays();
	}

	public void render() {
		renderEmployeeData();
		renderQuarterData();
		renderYTD();

		dataSheet.createFreezePane(3, 5);
		dataSheet.addIgnoredErrors(new CellRangeAddress(0, employeeData.size() + 6, getColumnStartIndex(), getColumnEndIndex()), IgnoredErrorType.NUMBER_STORED_AS_TEXT);
		adjustColumnSize();
	}

	
	private void adjustColumnSize() {
		for( int x = columnStartIndex; x < columnEndIndex ; x++ ){
			dataSheet.autoSizeColumn(x);
	    }
	}
	
	private void renderQuarterData() {
		PUMRenderer renderer = new PUMQuarterDataRenderer(getColumnEndIndex(), dataSheet, employeeData, holidays);
		renderer.render();
		columnUsed(renderer.getColumnsUsed());
	}
	
	private void renderEmployeeData() {
		PUMRenderer renderer = new PUMEmployeeDataRenderer(columnStartIndex, dataSheet, employeeData);
		renderer.render();
		columnUsed(renderer.getColumnsUsed());
	}
	
	private void renderYTD() {
		PUMRenderer renderer = new PUMYTDRenderer(getColumnEndIndex(), dataSheet, employeeData);
		renderer.render();
		columnUsed(renderer.getColumnsUsed());
	}
}
