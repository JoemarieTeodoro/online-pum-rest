package com.ph.ibm.report.pum.renderer;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import com.ph.ibm.report.CellStyleUtils;
import com.ph.ibm.report.pum.PUMUtilizationConstants;
import com.ph.ibm.report.pum.data.PUMEmployeeData;
import com.ph.ibm.util.FormatUtils;

public class PUMEmployeeDataRenderer extends PUMColumnRenderer implements PUMRenderer{

	private int START_ROW = 4;
	
	private List<PUMEmployeeData> employeeData;
	
	public PUMEmployeeDataRenderer( int columnStartIndex , XSSFSheet dataSheet, List<PUMEmployeeData> employeeData) {
		super(dataSheet,columnStartIndex);
		this.employeeData = employeeData;
	}
	
	@Override
	public void render() {
		generateHeader();
		generateData();
		generateBlankRow();
	}
	
	private void generateBlankRow() {
		int columnStart = columnStartIndex;
		Row row = getRow(dataSheet, START_ROW++);
		
		setCell(row, columnStart++, PUMUtilizationConstants.EMPTY_STRING, myStyles.get(CellStyleUtils.PUM_NORMAL_TOTAL_DATA_STYLE));
	    setCell(row, columnStart++, PUMUtilizationConstants.EMPTY_STRING, myStyles.get(CellStyleUtils.PUM_NORMAL_TOTAL_DATA_STYLE));
	    setCell(row, columnStart++, PUMUtilizationConstants.EMPTY_STRING, myStyles.get(CellStyleUtils.PUM_NORMAL_TOTAL_DATA_STYLE));
	    setCell(row, columnStart++, PUMUtilizationConstants.EMPTY_STRING, myStyles.get(CellStyleUtils.PUM_NORMAL_TOTAL_DATA_STYLE));
	    setCell(row, columnStart++, PUMUtilizationConstants.EMPTY_STRING, myStyles.get(CellStyleUtils.PUM_NORMAL_TOTAL_DATA_STYLE));
	}

	private void generateHeader() {
		int columnStart = columnStartIndex;
		Row row = getRow(dataSheet, START_ROW++);
		
		setCell(row, columnStart++, PUMUtilizationConstants.PROJECT_HEADER, myStyles.get(CellStyleUtils.PUM_HEADER_STYLE));
	    setCell(row, columnStart++, PUMUtilizationConstants.SERIAL_HEADER, myStyles.get(CellStyleUtils.PUM_HEADER_STYLE));
	    setCell(row, columnStart++, PUMUtilizationConstants.NAME_HEADER, myStyles.get(CellStyleUtils.PUM_HEADER_STYLE));
	    setCell(row, columnStart++, PUMUtilizationConstants.ROLL_IN_HEADER, myStyles.get(CellStyleUtils.PUM_HEADER_STYLE));
	    setCell(row, columnStart++, PUMUtilizationConstants.ROLL_OFF_HEADER, myStyles.get(CellStyleUtils.PUM_HEADER_STYLE));
	    
	    columnUsed(columnStart);
	}
	
	private void generateData() {
		
		for( PUMEmployeeData data : employeeData)
		{
			Row row = getRow(dataSheet, START_ROW++);
			int columnStart = columnStartIndex;
			setCell(row, columnStart++, data.getProjectName(), myStyles.get(CellStyleUtils.PUM_NORMAL_DATA_STYLE));
		    setCell(row, columnStart++, data.getSerialNumber(), myStyles.get(CellStyleUtils.PUM_NORMAL_CENTERED_DATA_STYLE));
		    setCell(row, columnStart++, data.getResourceName(), myStyles.get(CellStyleUtils.PUM_NORMAL_DATA_STYLE));
		    setCell(row, columnStart++, getRowDateData(data.getRollInData()), myStyles.get(CellStyleUtils.PUM_NORMAL_DATA_STYLE));
		    setCell(row, columnStart++, getRowDateData(data.getRollOffData()),myStyles.get(CellStyleUtils.PUM_NORMAL_DATA_STYLE));
		}
	}
	
	private String getRowDateData(String date) {
		if(date == null) {
			return PUMUtilizationConstants.EMPTY_STRING;
		}
		LocalDate convertedDate = FormatUtils.toDBDateFormat(date);
		return convertedDate.format(DateTimeFormatter.ofPattern("MMM d yyyy")).toString();
	}
}
