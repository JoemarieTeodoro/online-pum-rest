package com.ph.ibm.upload;

import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public abstract class ExcelUploaderBase implements ExcelUploader {
	
	protected abstract Map<String, Map<String, Double>> parseExcelFile(Workbook workbook);
	
	/**
	 * Returns the column number of the specified header value
	 * @param sheet The spreadsheet POI object
	 * @param headerValue The header value you want to find the column number to
	 */
	protected int getHeaderValueColumnNum(Sheet sheet, String headerValue) {
		int maxColNum = sheet.getRow(0).getLastCellNum();
		
		String cellVal = "";
		for(int i = 0; i < maxColNum; i++) {
			cellVal = retrieveHeader(sheet, i);
			if(cellVal.toUpperCase().equals(headerValue.toUpperCase())) {
				return i;
			}
		}

		return 0;
	}
	
	/**
	 * Retrieves the spreadsheet's header in the specified column number
	 * @param sheet The spreadsheet object
	 * @param colNum The column number
	 * @return The string value of the retrieved header
	 */
	protected String retrieveHeader(Sheet sheet, int colNum) {
		//the sheet object is 0-indexed, so the header is at row 0
		return retrieveValueFromCell(sheet, 0, colNum);
	}
	
	/**
	 * Retrieves a cell's equivalent String value from the spreadsheet
	 * @param sheet The spreadsheet object
	 * @param rowNum The cell's row number
	 * @param colNum The cell's column number
	 * @return The string value of the specified cell
	*/
	protected String retrieveValueFromCell(Sheet sheet, int rowNum, int colNum) {
		Cell cell = sheet.getRow(rowNum).getCell(colNum);
		return getStringValueFromCell(cell);
	}
	
	/**
	 * Retrieves a cell's value from the specified column number in the specified Row object
	 * @param row
	 * @param colNum
	 * @return The string value of the specified cell
	 */
	protected String retrieveValueFromCell(Row row, int colNum) {
		Cell cell = row.getCell(colNum);
		return getStringValueFromCell(cell);

	}
	
	private String getStringValueFromCell(Cell cell) {
		DataFormatter formatter = new DataFormatter();
		return formatter.formatCellValue(cell);
	}
}
