package com.ph.ibm.report.pum.renderer;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import com.ph.ibm.model.Holiday;
import com.ph.ibm.report.CellStyleUtils;
import com.ph.ibm.report.pum.PUMUtilizationConstants;
import com.ph.ibm.report.pum.data.PUMEmployeeData;
import com.ph.ibm.report.pum.data.PUMQuarterlyUtilizationData;

public class PUMQuarterDataRenderer extends PUMColumnRenderer implements PUMRenderer {

	private List<Holiday> holidays;

	private List<PUMEmployeeData> employeeData;

	private static final int NUMBER_OF_ROWS = 3;

	private int START_ROW = 0;

	private String QUARTERS[] = { PUMUtilizationConstants.Q1_HEADER, PUMUtilizationConstants.Q2_HEADER,
			PUMUtilizationConstants.Q3_HEADER, PUMUtilizationConstants.Q4_HEADER };

	public PUMQuarterDataRenderer(int columnStartIndex, XSSFSheet dataSheet, List<PUMEmployeeData> employeeData,
			List<Holiday> holidays) {
		super(dataSheet, columnStartIndex);
		this.employeeData = employeeData;
		this.holidays = holidays;
	}

	@Override
	public void render() {
		int realStart = columnStartIndex;
		for (String quarter : QUARTERS) {
			List<PUMQuarterlyUtilizationData> quarterData = getQuarterData(quarter);
			if (quarterData == null || quarterData.isEmpty() || isAllNulls(quarterData)) {
				continue;
			}

			PUMRenderer renderer = getMonthlyRenderer(quarter, quarterData, realStart);
			renderer.render();
			columnUsed(renderer.getColumnsUsed());
			realStart = renderer.getColumnEndIndex();

			generateQuarterHeader(realStart, quarter);
			generateOtherHeader(realStart);
			generateBlankRow(realStart, CellStyleUtils.PUM_BLUE_ROW_DATA_STYLE);
			generateBlankRow(realStart, CellStyleUtils.PUM_BLUE_ROW_DATA_STYLE);
			generateBlankRow(realStart, CellStyleUtils.PUM_BLUE_ROW_DATA_STYLE);
			generateData(realStart, quarterData);
			generateLastRow(realStart, quarterData, CellStyleUtils.PUM_DECIMAL_TOTAL_DATA_STYLE);

			dataSheet.addMergedRegion(new CellRangeAddress(0, 0, realStart, realStart + 2));
			realStart += NUMBER_OF_ROWS;
			START_ROW = 0;
		}
	}

	private void generateLastRow(int start, List<PUMQuarterlyUtilizationData> quarterData, int style) {
		Row row = getRow(dataSheet, START_ROW++);

		setCell(row, start++, getActualHoursTotal(quarterData), myStyles.get(style));
		setCell(row, start++, getAvailableHoursTotal(quarterData), myStyles.get(style));
		setCell(row, start++, getActualVsAvailableTotal(quarterData), myStyles.get(style));

	}

	private String getActualVsAvailableTotal(List<PUMQuarterlyUtilizationData> quarterData) {
		double total = quarterData.stream().map(data -> (data.getActualHours() / data.getAvailableHours()) * 100)
				.mapToDouble(i -> i.doubleValue()).sum();

		return getPercentage(total / quarterData.size());
	}

	private double getActualHoursTotal(List<PUMQuarterlyUtilizationData> quarterData) {
		return quarterData.stream().map(data -> data.getActualHours()).mapToDouble(i -> i.doubleValue()).sum();
	}

	private double getAvailableHoursTotal(List<PUMQuarterlyUtilizationData> quarterData) {
		return quarterData.stream().map(data -> data.getAvailableHours()).mapToDouble(i -> i.doubleValue()).sum();
	}

	private List<PUMQuarterlyUtilizationData> getQuarterData(String quarter) {
		return employeeData.stream().map(empData -> empData.getUtilData())
				.map(utilData -> utilData.getQuarterlyDatas().get(quarter)).collect(Collectors.toList());
	}

	private static boolean isAllNulls(Iterable<?> array) {
		return StreamSupport.stream(array.spliterator(), true).allMatch(o -> o == null);
	}

	private PUMRenderer getMonthlyRenderer(String quarter, List<PUMQuarterlyUtilizationData> data, int start) {
		switch (quarter) {
		case PUMUtilizationConstants.Q1_HEADER:
			return new PUMFirstQuarterMonthsDataRenderer(dataSheet, data, holidays, start);
		case PUMUtilizationConstants.Q2_HEADER:
			return new PUMSecondQuarterMonthsDataRenderer(dataSheet, data, holidays, start);
		case PUMUtilizationConstants.Q3_HEADER:
			return new PUMThirdQuarterMonthsDataRenderer(dataSheet, data, holidays, start);
		case PUMUtilizationConstants.Q4_HEADER:
			return new PUMFourthQuarterMonthsDataRenderer(dataSheet, data, holidays, start);
		default:
			return null;
		}
	}

	private void generateData(int realStart, List<PUMQuarterlyUtilizationData> quarterData) {

		if (quarterData == null || quarterData.isEmpty()) {
			generateBlankRow(realStart, CellStyleUtils.PUM_BLUE_ROW_DATA_STYLE);
		} else {
			for (PUMQuarterlyUtilizationData data : quarterData) {
				generateQuarterData(realStart, data);
			}
		}
	}

	private void generateQuarterData(int start, PUMQuarterlyUtilizationData data) {
		if (data == null) {
			generateBlankRow(start, CellStyleUtils.PUM_BLUE_ROW_DATA_STYLE);
		} else {
			Row row = getRow(dataSheet, START_ROW++);

			setCell(row, start++, data.getActualHours(), myStyles.get(CellStyleUtils.PUM_BLUE_ROW_DATA_STYLE));
			setCell(row, start++, data.getAvailableHours(), myStyles.get(CellStyleUtils.PUM_BLUE_ROW_DATA_STYLE));
			setCell(row, start++, getActualVsAvailable(data), myStyles.get(CellStyleUtils.PUM_BLUE_ROW_DATA_STYLE));
		}
	}

	private void generateBlankRow(int start, int style) {
		Row row = getRow(dataSheet, START_ROW++);

		for (int x = start; x < start + NUMBER_OF_ROWS; x++) {
			setCell(row, x, PUMUtilizationConstants.EMPTY_STRING, myStyles.get(style));
		}
	}

	private void generateOtherHeader(int start) {
		Row row = getRow(dataSheet, START_ROW++);

		setCell(row, start++, PUMUtilizationConstants.ACTUAL_HEADER,
				myStyles.get(CellStyleUtils.PUM_BLUE_HEADER_STYLE));
		setCell(row, start++, PUMUtilizationConstants.AVAIL_HEADER, myStyles.get(CellStyleUtils.PUM_BLUE_HEADER_STYLE));
		setCell(row, start++, PUMUtilizationConstants.QTD_PERCENT_HEADER,
				myStyles.get(CellStyleUtils.PUM_BLUE_HEADER_STYLE));

		columnUsed(NUMBER_OF_ROWS);
	}

	private void generateQuarterHeader(int start, String quarterName) {
		Row row = getRow(dataSheet, START_ROW++);

		setCell(row, start++, quarterName, myStyles.get(CellStyleUtils.PUM_QUARTER_DATA_STYLE));
		setCell(row, start++, quarterName, myStyles.get(CellStyleUtils.PUM_QUARTER_DATA_STYLE));
		setCell(row, start++, quarterName, myStyles.get(CellStyleUtils.PUM_QUARTER_DATA_STYLE));
	}

}
