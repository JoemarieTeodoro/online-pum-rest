package com.ph.ibm.report.pum.renderer;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import com.ph.ibm.model.Holiday;
import com.ph.ibm.report.CellStyleUtils;
import com.ph.ibm.report.pum.PUMUtilizationConstants;
import com.ph.ibm.util.FormatUtils;

public class PUMHolidaySheetRenderer extends PUMColumnRenderer implements PUMRenderer {

	private static final String DATE_FORMAT = "EEEE, MMMM d ,yyyy";

	private List<Holiday> myHolidays;

	private Map<Integer,CellStyle> myStyles;

	private String fiscalYear;

	private static final int START_ROW_INDEX = 1;
	
	private static final int NUMBER_OF_ROWS = 3;

	public PUMHolidaySheetRenderer(int columnStartIndex, XSSFSheet dataSheet, List<Holiday> holidays, String year) {
		super(dataSheet,columnStartIndex);
		myHolidays = holidays;
		fiscalYear = year;
		myStyles = CellStyleUtils.generateStyles(dataSheet.getWorkbook());
	}
	
	private void adjustColumnSize() {
		for( int x = 0; x < getColumnsUsed() ; x++ ){
			dataSheet.autoSizeColumn(x);
	    }
	}

	private void generateFirstHeader(int startRow) {
		int columnStart = columnStartIndex;
		Row row = dataSheet.createRow(startRow);

	    setCell(row, columnStart++, getHolidayHeader(), myStyles.get(CellStyleUtils.HOLIDAY_FIRST_HEADER));
	    setCell(row, columnStart++, PUMUtilizationConstants.EMPTY_STRING, myStyles.get(CellStyleUtils.HOLIDAY_FIRST_HEADER));
	    setCell(row, columnStart++, PUMUtilizationConstants.EMPTY_STRING, myStyles.get(CellStyleUtils.HOLIDAY_FIRST_HEADER));
	    
	    columnUsed(NUMBER_OF_ROWS);
	}

	private void generateSecondHeader(int startRow) {
		int columnStart = columnStartIndex;
		Row row = dataSheet.createRow(startRow);

	    setCell(row, columnStart++, PUMUtilizationConstants.HOLIDAY ,myStyles.get(CellStyleUtils.HOLIDAY_SECOND_HEADER));
	    setCell(row, columnStart++, PUMUtilizationConstants.DATE_HEADER ,myStyles.get(CellStyleUtils.HOLIDAY_SECOND_HEADER));
	    setCell(row, columnStart++, PUMUtilizationConstants.REMARKS_HEADER ,myStyles.get(CellStyleUtils.HOLIDAY_SECOND_HEADER));
	}

	private String getHolidayHeader() {
		return fiscalYear + PUMUtilizationConstants.SPACE_STRING + PUMUtilizationConstants.NATIONAL_HOLIDAY;
	}

	private void generateHolidayData(int startRow) {
		int rowStart = startRow;

		for (Holiday holiday : myHolidays) {
			Row row = dataSheet.createRow(rowStart++);
			int columnStart = columnStartIndex;
		    setCell(row, columnStart++, holiday.getName(),myStyles.get(CellStyleUtils.HOLIDAY_DATA_STYLE));
		    setCell(row, columnStart++, getRowDateData(holiday.getDate()),myStyles.get(CellStyleUtils.HOLIDAY_DATA_STYLE));
		    setCell(row, columnStart++, PUMUtilizationConstants.EMPTY_STRING,myStyles.get(CellStyleUtils.HOLIDAY_DATA_STYLE));
		}
	}

	private String getRowDateData(String date) {
		LocalDate convertedDate = FormatUtils.toDBDateFormat(date);
		return convertedDate.format(DateTimeFormatter.ofPattern(DATE_FORMAT)).toString();
	}
	
	@Override
	public void render() {
		int startRow = START_ROW_INDEX;

		generateFirstHeader(startRow++);
		generateSecondHeader(startRow++);
		generateHolidayData(startRow++);

		adjustColumnSize();
	}
}
