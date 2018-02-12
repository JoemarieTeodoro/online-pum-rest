package com.ph.ibm.report.pum.renderer;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import com.ph.ibm.report.CellStyleUtils;
import com.ph.ibm.report.pum.PUMUtilizationConstants;
import com.ph.ibm.report.pum.data.PUMHoursData;

public class PUMColumnRenderer {

	private static final int DECIMAL_PLACES = 1;

	public int columnStartIndex;

	public int columnEndIndex;

	public Map<Integer, CellStyle> myStyles;

	public XSSFSheet dataSheet;

	public PUMColumnRenderer(XSSFSheet dataSheet, int columnStartIndex) {
		this.columnStartIndex = columnStartIndex;
		this.columnEndIndex = columnStartIndex;
		this.dataSheet = dataSheet;
		myStyles = CellStyleUtils.generateStyles(dataSheet.getWorkbook());

	}

	protected Row getRow(XSSFSheet dataSheet, int rowNumber) {
		Row row = dataSheet.getRow(rowNumber);
		return (row != null) ? row : dataSheet.createRow(rowNumber);
	}

	protected void setCell(Row row, int columnNumber, String cellValue) {
		Cell cell = row.createCell(columnNumber);
		cell.setCellValue(cellValue);
	}

	protected void setCell(Row row, int columnNumber, String cellValue, CellStyle style) {
		Cell cell = row.createCell(columnNumber);
		cell.setCellValue(cellValue);
		cell.setCellStyle(style);
		cell.setCellType(CellType.STRING);
	}

	protected void setCell(Row row, int columnNumber, int cellValue, CellStyle style) {
		Cell cell = row.createCell(columnNumber);
		cell.setCellValue(cellValue);
		cell.setCellStyle(style);
		cell.setCellType(CellType.NUMERIC);
	}

	protected void setCell(Row row, int columnNumber, double cellValue, CellStyle style) {
		Cell cell = row.createCell(columnNumber);
		cell.setCellValue(cellValue);
		cell.setCellStyle(style);
		cell.setCellType(CellType.NUMERIC);
	}

	protected String getActualVsAvailable(PUMHoursData data) {
		double total = data.getActualHours() / data.getAvailableHours();

		return getPercentage(total * 100);
	}

	protected String getPercentage(Number value) {
		return round(value, DECIMAL_PLACES) + PUMUtilizationConstants.PERCENT_SYMBOL;
	}

	private double round(Number value, int places) {
		if (places < 0)
			throw new IllegalArgumentException();

		BigDecimal bd = new BigDecimal(value.doubleValue());
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}

	public int getColumnStartIndex() {
		return columnStartIndex;
	}

	public void setColumnStartIndex(int columnStartIndex) {
		this.columnStartIndex = columnStartIndex;
	}

	public void columnUsed(int value) {
		columnEndIndex += value;
	}

	public void columnUsed() {
		columnEndIndex++;
	}

	public int getColumnEndIndex() {
		return columnEndIndex;
	}

	public int getColumnsUsed() {
		return columnEndIndex - columnStartIndex;
	}
}
