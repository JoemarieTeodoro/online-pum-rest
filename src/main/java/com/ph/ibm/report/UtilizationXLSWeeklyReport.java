package com.ph.ibm.report;

import static com.ph.ibm.report.UtilizationXLSConstants.DATA_ROW_NUMBER;
import static com.ph.ibm.report.UtilizationXLSConstants.EMPLOYEE_NAME_COLUMN_NUMBER;
import static com.ph.ibm.report.UtilizationXLSConstants.EMPLOYEE_NAME_HEADER;
import static com.ph.ibm.report.UtilizationXLSConstants.EMPLOYEE_SERIAL_COLUMN_NUMBER;
import static com.ph.ibm.report.UtilizationXLSConstants.EMPLOYEE_SERIAL_HEADER;
import static com.ph.ibm.report.UtilizationXLSConstants.EMPTY_STRING;
import static com.ph.ibm.report.UtilizationXLSConstants.GRAND_TOTAL_HEADER;
import static com.ph.ibm.report.UtilizationXLSConstants.HEADER_ROW_NUMBER;
import static com.ph.ibm.report.UtilizationXLSConstants.NUMBER_OF_DATA_ROWS;
import static com.ph.ibm.report.UtilizationXLSConstants.YTD_HEADER;
import static com.ph.ibm.util.OpumConstants.TOTAL_NUMBER_OF_WEEKS;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import com.ph.ibm.model.UtilizationRowData;
import com.ph.ibm.model.UtilizationXLSData;

public class UtilizationXLSWeeklyReport extends UtilizationXLSReport {
	
	private static final int DOUBLE_DIGIT_VALUE = 10;

	public static final int WEEK_START_INDEX = 1;

	public static final int WEEK_START_COLUMN_NUMBER = 2;
	
	public static final int NUMBER_OF_HOURS_PER_WEEK = 40;
	
	public static final String WEEK_HEADER = "WK";
	
	public Map<Integer, String> weekHeaders;
	
	public int numberOfWeeks;

	public UtilizationXLSWeeklyReport(UtilizationXLSData data) {
		super(data);
		this.numberOfWeeks = data.endIndex;
		weekHeaders = generateWeekHeaders(data.getFiscalYearStartDate());
	}

	protected void generateHeaderRow(Sheet sheet) {
		Row row = sheet.createRow(HEADER_ROW_NUMBER);

	    setCell(row, EMPLOYEE_SERIAL_COLUMN_NUMBER, EMPLOYEE_SERIAL_HEADER);
	    setCell(row, EMPLOYEE_NAME_COLUMN_NUMBER, EMPLOYEE_NAME_HEADER);

	    int columnNumber = WEEK_START_COLUMN_NUMBER;
	    for( int weekNumber = WEEK_START_INDEX ; columnNumber < numberOfWeeks + WEEK_START_COLUMN_NUMBER ; columnNumber++ , weekNumber++) {
		    setCell(row, columnNumber, weekHeaders.get(weekNumber),cellStyles.get(CellStyleUtils.YELLOW_BORDERED_HEADER));
	    }

	    setCell(row, columnNumber, GRAND_TOTAL_HEADER,cellStyles.get(CellStyleUtils.YELLOW_BORDERED_HEADER));
	    setCell(row, ++columnNumber, YTD_HEADER,cellStyles.get(CellStyleUtils.DARK_BACK_WHITE_CENTERED_FONT));
	}

	protected void generateDataRows(Sheet sheet) {
		Map<String, Number> grandTotal = new HashMap<String, Number>();
	    addToMap( grandTotal, NUMBER_OF_DATA_ROWS , dataRows.size() );
		int rowNumber = DATA_ROW_NUMBER;

		for( UtilizationRowData data : dataRows ) {
			Row row = sheet.createRow(rowNumber);
		    double rowGrandTotal = 0;
		    int columnNumber = WEEK_START_COLUMN_NUMBER;

		    setCell(row, EMPLOYEE_SERIAL_COLUMN_NUMBER, data.getEmpSerial());
		    setCell(row, EMPLOYEE_NAME_COLUMN_NUMBER, data.getEmpName());

		    for( int weekNumber = WEEK_START_INDEX ; columnNumber < numberOfWeeks + WEEK_START_COLUMN_NUMBER ; columnNumber++ , weekNumber++) {
			    setCell(row, columnNumber, data.getWeekHour(weekNumber), cellStyles.get(CellStyleUtils.CENTERED));
			    rowGrandTotal += data.getWeekHour(weekNumber);
			    addToMap( grandTotal, String.valueOf(weekNumber) , data.getWeekHour(weekNumber));
		    }

		    setCell(row, columnNumber, rowGrandTotal, cellStyles.get(CellStyleUtils.CENTERED));
		    addToMap( grandTotal, GRAND_TOTAL_HEADER , rowGrandTotal );

			double YTD =  ( ( ( double ) rowGrandTotal/( numberOfWeeks * NUMBER_OF_HOURS_PER_WEEK ) ) * 100);
		    setCell(row, ++columnNumber, getPercentage( YTD ), getYTDCellStyle(YTD));
		    addToMap( grandTotal, YTD_HEADER , YTD );
		    rowNumber++;
		}

		generateGrandTotal( sheet , rowNumber ,grandTotal );
	}

	protected void generateGrandTotal(Sheet sheet, int rowNumber, Map<String, Number> grandTotal) {
		Row row = sheet.createRow(rowNumber);
		double YTD = grandTotal.get(YTD_HEADER).doubleValue()/grandTotal.get(NUMBER_OF_DATA_ROWS).doubleValue();

	    setCell(row, EMPLOYEE_SERIAL_COLUMN_NUMBER, GRAND_TOTAL_HEADER, cellStyles.get(CellStyleUtils.GREEN_BOLD_TOTAL));
	    setCell(row, EMPLOYEE_NAME_COLUMN_NUMBER, EMPTY_STRING, cellStyles.get(CellStyleUtils.GREEN_BOLD_TOTAL));

	    int columnNumber = WEEK_START_COLUMN_NUMBER;
	    for( int weekNumber = WEEK_START_INDEX ; columnNumber < numberOfWeeks + WEEK_START_COLUMN_NUMBER ; columnNumber++ , weekNumber++) {
	    	setCell(row, columnNumber,grandTotal.get( String.valueOf( weekNumber ) ).doubleValue(), cellStyles.get(CellStyleUtils.GREEN_BOLD_CENTERED_TOTAL));
	    }

	    setCell(row, columnNumber, grandTotal.get(GRAND_TOTAL_HEADER).doubleValue(), cellStyles.get(CellStyleUtils.GREEN_BOLD_CENTERED_TOTAL));
	    setCell(row, ++columnNumber, getPercentage( YTD ), getYTDTotalCellStyle(YTD) );
	}
	
	protected Map<Integer,String> generateWeekHeaders(Date startDate){
		if(startDate == null){
			return new HashMap<Integer,String>();
		}
		Map<Integer,String> headers = new HashMap<Integer,String>();
		LocalDate currentDate = convertToLocalDate(startDate);
		for(int weekNumber = WEEK_START_INDEX; weekNumber <= TOTAL_NUMBER_OF_WEEKS ; weekNumber++){
			if( weekNumber == WEEK_START_INDEX && isFriday(currentDate)) {
				headers.put( weekNumber, createWeekHeader(currentDate));
			}
			else {
				currentDate = getNextFriday(currentDate);
				headers.put( weekNumber, createWeekHeader(currentDate));
			}
		}

		return headers;
	}
	
	protected void adjustColumnSize(HSSFSheet sheet) {
		for( int x = 0; x < numberOfWeeks + 4 ; x++ ){
		    sheet.autoSizeColumn(x);
	    }
	}

	protected String createWeekHeader(LocalDate date) {
		int monthValue = date.getMonth().getValue();
		int dayValue = date.getDayOfMonth();

		return WEEK_HEADER + getStringValue(monthValue) + getStringValue(dayValue);
	}

	protected String getStringValue(int value) {
		if(value < DOUBLE_DIGIT_VALUE) {
			return "0" + value;
		}
		return String.valueOf(value);
	}

	protected LocalDate convertToLocalDate(Date date) {
		return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}

	protected boolean isFriday(LocalDate date) {
		return date.getDayOfWeek() == DayOfWeek.FRIDAY;
	}

	protected LocalDate getNextFriday(LocalDate date) {
		if(isFriday(date)){
			return date.plusDays(7);
		}

		return date = getFirstFridayInWeek(date);
	}

	protected LocalDate getFirstFridayInWeek(LocalDate date) {
		while( !isFriday(date) ){
			date = date.plusDays(1);
		}
		return date;
	}
}
