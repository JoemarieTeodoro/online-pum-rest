package com.ph.ibm.report.pum.renderer;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import com.ph.ibm.model.Holiday;
import com.ph.ibm.report.CellStyleUtils;
import com.ph.ibm.report.pum.PUMUtilizationConstants;
import com.ph.ibm.report.pum.data.PUMMonthlyUtilizationData;
import com.ph.ibm.report.pum.data.PUMQuarterlyUtilizationData;

public abstract class PUMMonthlyDataRenderer extends PUMColumnRenderer implements PUMRenderer {

	protected int START_ROW = 0;

	private List<PUMQuarterlyUtilizationData> quarterUtilizations;

	private List<Holiday> holidays;

	private static final int NUMBER_OF_ROWS = 6;

	public PUMMonthlyDataRenderer(XSSFSheet dataSheet, List<PUMQuarterlyUtilizationData> quarterUtilizations,
			List<Holiday> holidays, int columnStartIndex) {
		super(dataSheet, columnStartIndex);
		this.quarterUtilizations = quarterUtilizations;
		this.holidays = holidays;
	}

	public abstract String getQuarter();

	public abstract List<String> getIncludedMonths();

	@Override
	public void render() {
		int realStart = columnStartIndex;
		for (String month : getIncludedMonths()) {
			int monthstart = realStart;
			List<PUMMonthlyUtilizationData> monthData = getMonthlyData(month);
			if (monthData == null || monthData.isEmpty() || isAllNulls(monthData)) {
				continue;
			}
			generateWeeklyData(realStart, monthData);
			realStart = getColumnEndIndex();

			generateHeader(monthstart, month);
			generateSecondHeader(realStart);
			generateBlankRow(realStart, CellStyleUtils.PUM_BLUE_ROW_DATA_STYLE);
			generateBlankRow(realStart, CellStyleUtils.PUM_BLUE_ROW_DATA_STYLE);
			generateThirdHeader(realStart);
			generateData(realStart, month, monthData);
			generateLastRow(realStart, monthData);

			dataSheet.addMergedRegion(new CellRangeAddress(0, 0, monthstart, getColumnEndIndex() - 1));
			realStart = getColumnEndIndex();
			START_ROW = 0;
		}
	}

	private void generateLastRow(int start, List<PUMMonthlyUtilizationData> monthData) {
		Row row = getRow(dataSheet, START_ROW++);

		setCell(row, start++, getActualHoursTotal(monthData),
				myStyles.get(CellStyleUtils.PUM_DECIMAL_TOTAL_DATA_STYLE));
		setCell(row, start++, getLeaveTotal(monthData, PUMUtilizationConstants.VL_HEADER),
				myStyles.get(CellStyleUtils.PUM_NORMAL_TOTAL_DATA_STYLE));
		setCell(row, start++, getLeaveTotal(monthData, PUMUtilizationConstants.SL_HEADER),
				myStyles.get(CellStyleUtils.PUM_NORMAL_TOTAL_DATA_STYLE));
		setCell(row, start++, getLeaveTotal(monthData, PUMUtilizationConstants.OL_HEADER),
				myStyles.get(CellStyleUtils.PUM_NORMAL_TOTAL_DATA_STYLE));
		setCell(row, start++, getAvailableHoursTotal(monthData),
				myStyles.get(CellStyleUtils.PUM_DECIMAL_TOTAL_DATA_STYLE));
		setCell(row, start++, getActualVsAvailableTotal(monthData),
				myStyles.get(CellStyleUtils.PUM_DECIMAL_TOTAL_DATA_STYLE));

	}

	private List<PUMMonthlyUtilizationData> getMonthlyData(String month) {
		return quarterUtilizations.stream().map(quarterData -> getMonthlyData(quarterData)).map(data -> data.get(month))
				.collect(Collectors.toList());
	}

	private void generateWeeklyData(int realStart, List<PUMMonthlyUtilizationData> monthData) {
		PUMWeeklyDataRenderer renderer = new PUMWeeklyDataRenderer(dataSheet, monthData, holidays, realStart);
		renderer.render();
		columnUsed(renderer.getColumnsUsed());
	}

	private Map<String, PUMMonthlyUtilizationData> getMonthlyData(PUMQuarterlyUtilizationData data) {
		if (data == null || data.getMonthDatas() == null) {
			return new LinkedHashMap<String, PUMMonthlyUtilizationData>();
		}
		return data.getMonthDatas();
	}

	private void generateData(int realStart, String month, List<PUMMonthlyUtilizationData> monthData) {

		if (monthData == null || monthData.isEmpty()) {
			generateBlankRow(realStart, CellStyleUtils.PUM_BLUE_ROW_DATA_STYLE);
		} else {
			for (PUMMonthlyUtilizationData data : monthData) {
				generateMonthData(realStart, data);
			}
		}

	}

	private void generateMonthData(int start, PUMMonthlyUtilizationData data) {
		if (data == null) {
			generateBlankRow(start, CellStyleUtils.PUM_BLUE_ROW_DATA_STYLE);
		} else {
			Row row = getRow(dataSheet, START_ROW++);

			setCell(row, start++, data.getActualHours(), myStyles.get(CellStyleUtils.PUM_BLUE_ROW_DATA_STYLE));
			setCell(row, start++, data.getTotalLeaves().get(PUMUtilizationConstants.VL_HEADER),
					myStyles.get(CellStyleUtils.PUM_BLUE_LEAVE_ROW_DATA_STYLE));
			setCell(row, start++, data.getTotalLeaves().get(PUMUtilizationConstants.SL_HEADER),
					myStyles.get(CellStyleUtils.PUM_BLUE_LEAVE_ROW_DATA_STYLE));
			setCell(row, start++, data.getTotalLeaves().get(PUMUtilizationConstants.OL_HEADER),
					myStyles.get(CellStyleUtils.PUM_BLUE_LEAVE_ROW_DATA_STYLE));
			setCell(row, start++, data.getAvailableHours(), myStyles.get(CellStyleUtils.PUM_BLUE_ROW_DATA_STYLE));
			setCell(row, start++, getActualVsAvailable(data), myStyles.get(CellStyleUtils.PUM_BLUE_ROW_DATA_STYLE));
		}

	}

	private void generateThirdHeader(int realStart) {
		Row row = getRow(dataSheet, START_ROW++);

		setCell(row, realStart++, PUMUtilizationConstants.MTOT_HEADER,
				myStyles.get(CellStyleUtils.PUM_BLUE_HEADER_STYLE));
		setCell(row, realStart++, PUMUtilizationConstants.EMPTY_STRING,
				myStyles.get(CellStyleUtils.PUM_BLUE_HEADER_STYLE));
		setCell(row, realStart++, PUMUtilizationConstants.EMPTY_STRING,
				myStyles.get(CellStyleUtils.PUM_BLUE_HEADER_STYLE));
		setCell(row, realStart++, PUMUtilizationConstants.EMPTY_STRING,
				myStyles.get(CellStyleUtils.PUM_BLUE_HEADER_STYLE));
		setCell(row, realStart++, PUMUtilizationConstants.EMPTY_STRING,
				myStyles.get(CellStyleUtils.PUM_BLUE_HEADER_STYLE));
		setCell(row, realStart++, PUMUtilizationConstants.EMPTY_STRING,
				myStyles.get(CellStyleUtils.PUM_BLUE_HEADER_STYLE));

	}

	private void generateSecondHeader(int realstart) {
		Row row = getRow(dataSheet, START_ROW++);

		setCell(row, realstart++, PUMUtilizationConstants.ACTUAL_HEADER,
				myStyles.get(CellStyleUtils.PUM_BLUE_HEADER_STYLE));
		setCell(row, realstart++, PUMUtilizationConstants.VL_HEADER,
				myStyles.get(CellStyleUtils.PUM_BLUE_HEADER_STYLE));
		setCell(row, realstart++, PUMUtilizationConstants.SL_HEADER,
				myStyles.get(CellStyleUtils.PUM_BLUE_HEADER_STYLE));
		setCell(row, realstart++, PUMUtilizationConstants.OL_HEADER,
				myStyles.get(CellStyleUtils.PUM_BLUE_HEADER_STYLE));
		setCell(row, realstart++, PUMUtilizationConstants.AVAIL_HEADER,
				myStyles.get(CellStyleUtils.PUM_BLUE_HEADER_STYLE));
		setCell(row, realstart++, PUMUtilizationConstants.MTD_PERCENT_HEADER,
				myStyles.get(CellStyleUtils.PUM_BLUE_HEADER_STYLE));

		columnUsed(NUMBER_OF_ROWS);
	}

	private void generateHeader(int start, String month) {
		Row row = getRow(dataSheet, START_ROW++);

		setCell(row, start, month, myStyles.get(CellStyleUtils.PUM_MONTH_HEADER_STYLE));

	}

	private void generateBlankRow(int start, int style) {
		Row row = getRow(dataSheet, START_ROW++);

		for (int x = start; x < start + NUMBER_OF_ROWS; x++) {
			setCell(row, x, PUMUtilizationConstants.EMPTY_STRING, myStyles.get(style));
		}
	}

	private String getActualVsAvailableTotal(List<PUMMonthlyUtilizationData> monthData) {
		double total = monthData.stream().map(data -> (data.getActualHours() / data.getAvailableHours()) * 100)
				.mapToDouble(i -> i.doubleValue()).sum();

		return getPercentage(total / monthData.size());
	}

	private double getActualHoursTotal(List<PUMMonthlyUtilizationData> monthData) {
		return monthData.stream().map(data -> data.getActualHours()).mapToDouble(i -> i.doubleValue()).sum();
	}

	private double getAvailableHoursTotal(List<PUMMonthlyUtilizationData> monthData) {
		return monthData.stream().map(data -> data.getAvailableHours()).mapToDouble(i -> i.doubleValue()).sum();
	}

	private double getLeaveTotal(List<PUMMonthlyUtilizationData> monthData, String leave) {
		return monthData.stream().map(data -> data.getTotalLeaves().get(leave)).mapToDouble(i -> i.doubleValue()).sum();
	}

	private static boolean isAllNulls(Iterable<?> array) {
		return StreamSupport.stream(array.spliterator(), true).allMatch(o -> o == null);
	}

}
