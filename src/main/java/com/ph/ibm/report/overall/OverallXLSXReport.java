package com.ph.ibm.report.overall;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import com.ph.ibm.report.XLSXReport;

public class OverallXLSXReport extends XLSXReport{

	@Override
	protected XSSFWorkbook populateWorkbook() {
		
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = createSheet(workbook);
		Row row = sheet.createRow(1);
		
		Cell cell = row.createCell(1);
	    cell.setCellValue("LOL");
		
		
		return workbook;
	}

	@Override
	protected String getFileName() {
		return "OverallReport.xlsx";
	}
	
	protected XSSFSheet createSheet(XSSFWorkbook wb) {
		XSSFSheet sheet = wb.createSheet("REPORT");
		return sheet;
	}
	
	@Test
	public void generate() throws FileNotFoundException, IOException {
		XLSXReport report = new OverallXLSXReport();
		report.generateReport2();
	}
}
