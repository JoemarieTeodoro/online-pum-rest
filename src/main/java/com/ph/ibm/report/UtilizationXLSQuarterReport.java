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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import com.ph.ibm.model.UtilizationRowData;
import com.ph.ibm.model.UtilizationXLSData;

public class UtilizationXLSQuarterReport extends UtilizationXLSReport{
	
	public static final int QUARTER_START_INDEX = 1;

	public static final int QUARTER_START_COLUMN_NUMBER = 2;
	
	public List<String> quarterHeaders;
	
	public int numberOfQuarters;

	public UtilizationXLSQuarterReport(UtilizationXLSData data) {
		super(data);
		this.numberOfQuarters = data.getEndIndex();
		quarterHeaders = createQuarterHeaders();
	}

	private List<String> createQuarterHeaders(){
		List<String> quarterHeader = new ArrayList<String>();
		quarterHeader.add("Q1");
		quarterHeader.add("Q2");
		quarterHeader.add("Q3");
		quarterHeader.add("Q4");
		return quarterHeader;
	}
	
	@Override
	protected void generateHeaderRow(Sheet sheet) {
		Row row = sheet.createRow(HEADER_ROW_NUMBER);

	    setCell(row, EMPLOYEE_SERIAL_COLUMN_NUMBER, EMPLOYEE_SERIAL_HEADER);
	    setCell(row, EMPLOYEE_NAME_COLUMN_NUMBER, EMPLOYEE_NAME_HEADER);
	    
	    int columnNumber = QUARTER_START_COLUMN_NUMBER;
	    
	    for(  int quarterNumber = 1 ; quarterNumber < numberOfQuarters + 1 ; columnNumber++ , quarterNumber++) {
	    	setCell(row, columnNumber, quarterHeaders.get( quarterNumber - 1),cellStyles.get(CellStyleUtils.YELLOW_BORDERED_HEADER));
	    }

	    setCell(row, columnNumber, GRAND_TOTAL_HEADER,cellStyles.get(CellStyleUtils.YELLOW_BORDERED_HEADER));
	    setCell(row, ++columnNumber, YTD_HEADER,cellStyles.get(CellStyleUtils.DARK_BACK_WHITE_CENTERED_FONT));
	}

	@Override
	protected void generateDataRows(Sheet sheet) {
		Map<String, Integer> grandTotal = new HashMap<String, Integer>();
	    addToMap( grandTotal, NUMBER_OF_DATA_ROWS , dataRows.size() );
		int rowNumber = DATA_ROW_NUMBER;
		for( UtilizationRowData data : dataRows ) {
			Row row = sheet.createRow(rowNumber);
		    
		    setCell(row, EMPLOYEE_SERIAL_COLUMN_NUMBER, data.getEmpSerial());
		    setCell(row, EMPLOYEE_NAME_COLUMN_NUMBER, data.getEmpName());
		    
		    int rowGrandTotal = 0;
		    int columnNumber = QUARTER_START_COLUMN_NUMBER;
		    for( int quarterNumber = 1 ; quarterNumber < numberOfQuarters + 1 ; columnNumber++ , quarterNumber++) {
			    setCell(row, columnNumber, data.getQuarterHour(quarterNumber), cellStyles.get(CellStyleUtils.CENTERED));
			    rowGrandTotal += data.getQuarterHour(quarterNumber);
			    addToMap( grandTotal, String.valueOf(quarterNumber) , data.getQuarterHour(quarterNumber));
		    }
		    
		    setCell(row, columnNumber, rowGrandTotal, cellStyles.get(CellStyleUtils.CENTERED));
		    addToMap( grandTotal, GRAND_TOTAL_HEADER , rowGrandTotal );

			int YTD =  (int)( ( ( double ) rowGrandTotal/ ( 520 * numberOfQuarters ) ) * 100);
		    setCell(row, ++columnNumber, getPercentage( YTD ), getYTDCellStyle(YTD));
		    addToMap( grandTotal, YTD_HEADER , YTD );
		    rowNumber++;
		}
		
		generateGrandTotal( sheet , rowNumber ,grandTotal );
	}

	@Override
	protected void generateGrandTotal(Sheet sheet, int rowNumber, Map<String, Integer> grandTotal) {
		Row row = sheet.createRow(rowNumber);
		int YTD = grandTotal.get(YTD_HEADER)/grandTotal.get(NUMBER_OF_DATA_ROWS);

	    setCell(row, EMPLOYEE_SERIAL_COLUMN_NUMBER, GRAND_TOTAL_HEADER, cellStyles.get(CellStyleUtils.GREEN_BOLD_TOTAL));
	    setCell(row, EMPLOYEE_NAME_COLUMN_NUMBER, EMPTY_STRING, cellStyles.get(CellStyleUtils.GREEN_BOLD_TOTAL));

	    
	    int columnNumber = QUARTER_START_COLUMN_NUMBER;
	    for( int quarterNumber = 1 ; quarterNumber < numberOfQuarters + 1 ; columnNumber++ , quarterNumber++) {
		    setCell(row, columnNumber,grandTotal.get( String.valueOf( quarterNumber ) ), cellStyles.get(CellStyleUtils.GREEN_BOLD_CENTERED_TOTAL));
	    }

	    setCell(row, columnNumber, grandTotal.get(GRAND_TOTAL_HEADER), cellStyles.get(CellStyleUtils.GREEN_BOLD_CENTERED_TOTAL));
	    setCell(row, ++columnNumber, getPercentage( YTD ), getYTDTotalCellStyle(YTD) );
	}

	@Override
	protected void adjustColumnSize(HSSFSheet sheet) {
		for( int x = 0; x < numberOfQuarters + 4 ; x++ ){
		    sheet.autoSizeColumn(x);
	    }
	}}
