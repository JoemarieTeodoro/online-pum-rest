package com.ph.ibm.report.pum.renderer;

import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import com.ph.ibm.report.CellStyleUtils;
import com.ph.ibm.report.pum.PUMUtilizationConstants;
import com.ph.ibm.report.pum.data.PUMEmployeeData;

public class PUMYTDRenderer extends PUMColumnRenderer implements PUMRenderer {

	private int start_row = 0;

	private List<PUMEmployeeData> employeeData;

	public PUMYTDRenderer(int columnStartIndex, XSSFSheet dataSheet, List<PUMEmployeeData> employeeData) {
		super(dataSheet, columnStartIndex);
		this.employeeData = employeeData;
	}

	@Override
	public void render() {
		int realStart = columnStartIndex;
		generateYTDHeader();
		generateOtherHeader();
		generateBlankRow(CellStyleUtils.PUM_BLUE_ROW_DATA_STYLE);
		generateBlankRow(CellStyleUtils.PUM_BLUE_ROW_DATA_STYLE);
		generateBlankRow(CellStyleUtils.PUM_BLUE_ROW_DATA_STYLE);
		generateData();
		generateLastRow();

		dataSheet.addMergedRegion(new CellRangeAddress(0, 0, realStart, realStart + 9));
	}

	private void generateLastRow() {
		int columnStart = columnStartIndex;
		Row row = getRow(dataSheet, start_row++);

		setCell(row, columnStart++, getLeaveTotal(PUMUtilizationConstants.VL_HEADER),
				myStyles.get(CellStyleUtils.PUM_NORMAL_TOTAL_DATA_STYLE));
		setCell(row, columnStart++, getLeaveTotal(PUMUtilizationConstants.SL_HEADER),
				myStyles.get(CellStyleUtils.PUM_NORMAL_TOTAL_DATA_STYLE));
		setCell(row, columnStart++, getLeaveTotal(PUMUtilizationConstants.OL_HEADER),
				myStyles.get(CellStyleUtils.PUM_NORMAL_TOTAL_DATA_STYLE));
		setCell(row, columnStart++, getLeaveTotal(PUMUtilizationConstants.EL_HEADER),
				myStyles.get(CellStyleUtils.PUM_NORMAL_TOTAL_DATA_STYLE));
		setCell(row, columnStart++, getLeaveTotal(PUMUtilizationConstants.TR_HEADER),
				myStyles.get(CellStyleUtils.PUM_NORMAL_TOTAL_DATA_STYLE));
		setCell(row, columnStart++, getLeaveTotal(PUMUtilizationConstants.HO_HEADER),
				myStyles.get(CellStyleUtils.PUM_NORMAL_TOTAL_DATA_STYLE));
		setCell(row, columnStart++, getLeaveTotal(PUMUtilizationConstants.CDO_HEADER),
				myStyles.get(CellStyleUtils.PUM_NORMAL_TOTAL_DATA_STYLE));
		setCell(row, columnStart++, getActualHoursTotal(), myStyles.get(CellStyleUtils.PUM_DECIMAL_TOTAL_DATA_STYLE));
		setCell(row, columnStart++, getAvailableHoursTotal(),
				myStyles.get(CellStyleUtils.PUM_DECIMAL_TOTAL_DATA_STYLE));
		setCell(row, columnStart++, getActualVsAvailableTotal(),
				myStyles.get(CellStyleUtils.PUM_DECIMAL_TOTAL_DATA_STYLE));

	}

	private String getActualVsAvailableTotal() {
		double total = employeeData.stream().map(edata -> edata.getUtilData())
				.map(data -> (data.getActualHours() / data.getAvailableHours()) * 100).mapToDouble(i -> i.doubleValue())
				.sum();

		return getPercentage(total / employeeData.size());
	}

	private double getActualHoursTotal() {
		return employeeData.stream().map(edata -> edata.getUtilData()).map(data -> data.getActualHours())
				.mapToDouble(i -> i.doubleValue()).sum();
	}

	private double getAvailableHoursTotal() {
		return employeeData.stream().map(edata -> edata.getUtilData()).map(data -> data.getAvailableHours())
				.mapToDouble(i -> i.doubleValue()).sum();
	}

	private double getLeaveTotal(String type) {
		return employeeData.stream().map(edata -> edata.getUtilData()).map(data -> data.getTotalLeaves().get(type))
				.mapToDouble(i -> i.doubleValue()).sum();
	}

	private void generateData() {
		for (PUMEmployeeData data : employeeData) {
			Row row = getRow(dataSheet, start_row++);
			int columnStart = columnStartIndex;
			setCell(row, columnStart++, getLeave(data, PUMUtilizationConstants.VL_HEADER),
					myStyles.get(CellStyleUtils.PUM_BLUE_LEAVE_ROW_DATA_STYLE));
			setCell(row, columnStart++, getLeave(data, PUMUtilizationConstants.SL_HEADER),
					myStyles.get(CellStyleUtils.PUM_BLUE_LEAVE_ROW_DATA_STYLE));
			setCell(row, columnStart++, getLeave(data, PUMUtilizationConstants.OL_HEADER),
					myStyles.get(CellStyleUtils.PUM_BLUE_LEAVE_ROW_DATA_STYLE));
			setCell(row, columnStart++, getLeave(data, PUMUtilizationConstants.EL_HEADER),
					myStyles.get(CellStyleUtils.PUM_BLUE_LEAVE_ROW_DATA_STYLE));
			setCell(row, columnStart++, getLeave(data, PUMUtilizationConstants.TR_HEADER),
					myStyles.get(CellStyleUtils.PUM_BLUE_LEAVE_ROW_DATA_STYLE));
			setCell(row, columnStart++, getLeave(data, PUMUtilizationConstants.HO_HEADER),
					myStyles.get(CellStyleUtils.PUM_BLUE_LEAVE_ROW_DATA_STYLE));
			setCell(row, columnStart++, getLeave(data, PUMUtilizationConstants.CDO_HEADER),
					myStyles.get(CellStyleUtils.PUM_BLUE_LEAVE_ROW_DATA_STYLE));
			setCell(row, columnStart++, data.getUtilData().getActualHours(),
					myStyles.get(CellStyleUtils.PUM_BLUE_ROW_DATA_STYLE));
			setCell(row, columnStart++, data.getUtilData().getAvailableHours(),
					myStyles.get(CellStyleUtils.PUM_BLUE_ROW_DATA_STYLE));
			setCell(row, columnStart++, getActualVsAvailable(data.getUtilData()),
					myStyles.get(CellStyleUtils.PUM_BLUE_ROW_DATA_STYLE));
		}
	}

	private int getLeave(PUMEmployeeData data, String type) {
		return data.getUtilData().getTotalLeaves().get(type);
	}

	private void generateBlankRow(int style) {
		int columnStart = columnStartIndex;
		Row row = getRow(dataSheet, start_row++);

		for (int x = columnStart; x < columnStart + 10; x++) {
			setCell(row, x, PUMUtilizationConstants.EMPTY_STRING, myStyles.get(style));
		}
	}

	private void generateOtherHeader() {
		int columnStart = columnStartIndex;
		Row row = getRow(dataSheet, start_row++);

		setCell(row, columnStart++, PUMUtilizationConstants.VL_HEADER,
				myStyles.get(CellStyleUtils.PUM_BLUE_ROW_DATA_STYLE));
		setCell(row, columnStart++, PUMUtilizationConstants.SL_HEADER,
				myStyles.get(CellStyleUtils.PUM_BLUE_ROW_DATA_STYLE));
		setCell(row, columnStart++, PUMUtilizationConstants.OL_HEADER,
				myStyles.get(CellStyleUtils.PUM_BLUE_ROW_DATA_STYLE));
		setCell(row, columnStart++, PUMUtilizationConstants.EL_HEADER,
				myStyles.get(CellStyleUtils.PUM_BLUE_ROW_DATA_STYLE));
		setCell(row, columnStart++, PUMUtilizationConstants.TR_HEADER,
				myStyles.get(CellStyleUtils.PUM_BLUE_ROW_DATA_STYLE));
		setCell(row, columnStart++, PUMUtilizationConstants.HO_HEADER,
				myStyles.get(CellStyleUtils.PUM_BLUE_ROW_DATA_STYLE));
		setCell(row, columnStart++, PUMUtilizationConstants.CDO_HEADER,
				myStyles.get(CellStyleUtils.PUM_BLUE_ROW_DATA_STYLE));
		setCell(row, columnStart++, PUMUtilizationConstants.ACTUAL_HEADER,
				myStyles.get(CellStyleUtils.PUM_BLUE_ROW_DATA_STYLE));
		setCell(row, columnStart++, PUMUtilizationConstants.AVAIL_HEADER,
				myStyles.get(CellStyleUtils.PUM_BLUE_ROW_DATA_STYLE));
		setCell(row, columnStart++, PUMUtilizationConstants.YTD_PERCENT_HEADER,
				myStyles.get(CellStyleUtils.PUM_BLUE_ROW_DATA_STYLE));

		columnUsed(columnStart);
	}

	private void generateYTDHeader() {
		int columnStart = columnStartIndex;
		Row row = getRow(dataSheet, start_row++);

		setCell(row, columnStart, PUMUtilizationConstants.YTD_HEADER,
				myStyles.get(CellStyleUtils.PUM_YTD_HEADER_STYLE));
	}
}
