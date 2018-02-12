package com.ph.ibm.report.pum;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.ph.ibm.report.XLSXReport;
import com.ph.ibm.report.pum.data.PUMExcelData;
import com.ph.ibm.report.pum.renderer.PUMHolidaySheetRenderer;
import com.ph.ibm.report.pum.renderer.PUMRenderer;
import com.ph.ibm.report.pum.renderer.PUMYearlyRenderer;

public class PUMYearlyUtilizationXLSXReport extends XLSXReport{

	private PUMExcelData myData;
	
	public PUMYearlyUtilizationXLSXReport(PUMExcelData data) {
		myData = data;
	}

	@Override
	protected XSSFWorkbook populateWorkbook() {
		XSSFWorkbook workbook = new XSSFWorkbook();
		
		XSSFSheet dataSheet = createSheet(workbook,getPUMSheetName());
		XSSFSheet holidaySheet = createSheet(workbook, getHolidaySheetName());
		
		generateHolidaySheet(holidaySheet);
		generateYearlySheet(dataSheet);

		return workbook;
	}
	
	@Override
	protected String getFileName() {
		return myData.getFiscalYear() + PUMUtilizationConstants.EXCEL_FILE_NAME;
	}
	
	private XSSFSheet createSheet(XSSFWorkbook wb,String sheetName) {
		XSSFSheet sheet = wb.createSheet(sheetName);
		sheet.setDisplayGridlines(false);
		return sheet;
	}
	
	private void generateYearlySheet(XSSFSheet dataSheet) {
		PUMRenderer renderer = new PUMYearlyRenderer(0,dataSheet,myData);
		renderer.render();
	}

	private void generateHolidaySheet(XSSFSheet holidaySheet) {
		PUMHolidaySheetRenderer renderer = new PUMHolidaySheetRenderer(1,holidaySheet, myData.getHolidays(), myData.getFiscalYear());
		renderer.render();
	}
	
	private String getPUMSheetName() {
		return PUMUtilizationConstants.PUM + PUMUtilizationConstants.SPACE_STRING + myData.getFiscalYear();
	}
	
	private String getHolidaySheetName() {
		return myData.getFiscalYear()  + PUMUtilizationConstants.SPACE_STRING + PUMUtilizationConstants.HOLIDAYS;
	}
}
