package com.ph.ibm.report.pum.renderer;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import com.ph.ibm.model.Holiday;
import com.ph.ibm.report.CellStyleUtils;
import com.ph.ibm.report.pum.PUMUtilizationConstants;
import com.ph.ibm.report.pum.data.PUMMonthlyUtilizationData;
import com.ph.ibm.report.pum.data.PUMWeeklyUtilizationData;
import com.ph.ibm.util.FormatUtils;

public class PUMWeeklyDataRenderer extends PUMColumnRenderer implements PUMRenderer {

	private static final String DATE_HEADER_FORMAT = "M/d/yyyy";

	protected int startRow = 1;

	private final int DOUBLE_DIGIT_VALUE = 10;

	private final long NUMBER_OF_DAYS_IN_WEEK = 7;

	private final int NUMBER_OF_ROWS = 8;

	private final int DEFAULT_START_OF_ROWS = 1;

	private List<PUMMonthlyUtilizationData> monthData;

	private List<Holiday> holidays;

	private String[] days_header = { "S", "S", "M", "T", "W", "T", "F" };

	public PUMWeeklyDataRenderer(XSSFSheet dataSheet, List<PUMMonthlyUtilizationData> monthData, List<Holiday> holidays,
			int columnStartIndex) {
		super(dataSheet, columnStartIndex);
		this.monthData = monthData;
		this.holidays = holidays;
	}

	@Override
	public void render() {
		Map<LocalDate, List<PUMWeeklyUtilizationData>> data = getMappedWeeklyData();

		int realStart = columnStartIndex;
		for (Map.Entry<LocalDate, List<PUMWeeklyUtilizationData>> entry : data.entrySet()) {

			List<LocalDate> weekDates = getWeekDate(entry.getKey());

			generateHeader(realStart, entry.getKey());
			generateSecondHeader(realStart, weekDates);
			generateThirdHeader(realStart, weekDates);
			generateFourthHeader(realStart, entry.getKey());
			generateData(realStart, weekDates, entry.getValue());
			generateLastRow(realStart, entry.getValue(), CellStyleUtils.PUM_DECIMAL_TOTAL_DATA_STYLE);

			dataSheet.addMergedRegion(new CellRangeAddress(1, 1, realStart, getColumnEndIndex() - 1));
			realStart = getColumnEndIndex();
			startRow = DEFAULT_START_OF_ROWS;
		}
	}

	private Map<LocalDate, List<PUMWeeklyUtilizationData>> getMappedWeeklyData() {
		Map<LocalDate, List<PUMWeeklyUtilizationData>> map = new LinkedHashMap<>();
		for (PUMMonthlyUtilizationData data : monthData) {
			if (data != null && data.getWeeklyDatas() != null) {
				for (PUMWeeklyUtilizationData week : data.getWeeklyDatas()) {
					LocalDate date = FormatUtils.toDBDateFormat(week.getWeekDate());
					if (map.containsKey(date)) {
						List<PUMWeeklyUtilizationData> weekData = map.get(date);
						weekData.add(week);
						map.put(date, weekData);
					} else {
						List<PUMWeeklyUtilizationData> weekData = new ArrayList<>();
						weekData.add(week);
						map.put(date, weekData);
					}
				}
			}
		}

		return map;
	}

	private void generateLastRow(int start, List<PUMWeeklyUtilizationData> datas, int style) {
		Row row = getRow(dataSheet, startRow++);

		for (int x = start; x < start + NUMBER_OF_DAYS_IN_WEEK; x++) {
			setCell(row, x, PUMUtilizationConstants.EMPTY_STRING, myStyles.get(style));
		}

		setCell(row, start + (int) NUMBER_OF_DAYS_IN_WEEK, getTotal(datas), myStyles.get(style));
	}

	private int getTotal(List<PUMWeeklyUtilizationData> datas) {
		int total = 0;
		for (PUMWeeklyUtilizationData data : datas) {
			total += data.getTotal();
		}
		return total;
	}

	private void generateData(int start, List<LocalDate> weekDates, List<PUMWeeklyUtilizationData> utilDatas) {
		int realStart = start;
		for (PUMWeeklyUtilizationData utildata : utilDatas) {
			Row row = getRow(dataSheet, startRow++);
			for (LocalDate weekDate : weekDates) {
				String displayData = getDisplayData(utildata, weekDate);
				setCell(row, realStart++, displayData, getCellStyle(displayData, weekDate));
			}
			setCell(row, realStart++, utildata.getTotal(), myStyles.get(CellStyleUtils.PUM_BLUE_ROW_DATA_STYLE));
			realStart = start;
		}
	}

	private void generateHeader(int realStart, LocalDate key) {
		Row row = getRow(dataSheet, startRow++);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_HEADER_FORMAT);

		setCell(row, realStart++, key.format(formatter).toString(), myStyles.get(CellStyleUtils.PUM_HEADER_STYLE));
		for (int x = 0; x < NUMBER_OF_ROWS; x++) {
			setCell(row, realStart++, PUMUtilizationConstants.EMPTY_STRING,
					myStyles.get(CellStyleUtils.PUM_HEADER_STYLE));
		}
	}

	private void generateSecondHeader(int start, List<LocalDate> weekDates) {
		Row row = getRow(dataSheet, startRow++);

		for (LocalDate date : weekDates) {
			if (checkHoliday(date)) {
				setCell(row, start++, PUMUtilizationConstants.HO_HEADER,
						myStyles.get(CellStyleUtils.PUM_HOLIDAY_DATA_STYLE));
			} else {
				setCell(row, start++, PUMUtilizationConstants.EMPTY_STRING,
						myStyles.get(CellStyleUtils.PUM_HEADER_STYLE));
			}
		}
		setCell(row, start++, PUMUtilizationConstants.EMPTY_STRING, myStyles.get(CellStyleUtils.PUM_HEADER_STYLE));
	}

	private void generateThirdHeader(int realStart, List<LocalDate> weekDates) {
		Row row = getRow(dataSheet, startRow++);

		for (LocalDate date : weekDates) {
			setCell(row, realStart++, getStringValue(date.getDayOfMonth()),
					myStyles.get(CellStyleUtils.PUM_DAY_NUMBER_HEADER_STYLE));
			columnUsed();
		}
		setCell(row, realStart++, PUMUtilizationConstants.EMPTY_STRING,
				myStyles.get(CellStyleUtils.PUM_DAY_NUMBER_HEADER_STYLE));
	}

	private void generateFourthHeader(int realStart, LocalDate localDate) {
		Row row = getRow(dataSheet, startRow++);

		for (String header : days_header) {
			setCell(row, realStart++, header, myStyles.get(CellStyleUtils.PUM_HEADER_STYLE));
		}

		setCell(row, realStart++, createWeekHeader(localDate), myStyles.get(CellStyleUtils.PUM_BLUE_ROW_DATA_STYLE));
		columnUsed();
	}

	private boolean checkHoliday(LocalDate date) {
		for (Holiday holiday : holidays) {
			LocalDate newDate = FormatUtils.toDBDateFormat(holiday.getDate());
			if (date.getYear() == newDate.getYear() && date.getMonthValue() == newDate.getMonthValue()
					&& date.getDayOfMonth() == newDate.getDayOfMonth()) {
				return true;
			}
		}
		return false;
	}

	private String createWeekHeader(LocalDate date) {
		int monthValue = date.getMonth().getValue();
		int dayValue = date.getDayOfMonth();

		return PUMUtilizationConstants.WEEK_HEADER + getStringValue(monthValue) + getStringValue(dayValue);
	}

	private String getStringValue(int value) {
		if (value < DOUBLE_DIGIT_VALUE) {
			return PUMUtilizationConstants.ZERO_STRING + value;
		}
		return String.valueOf(value);
	}

	private List<LocalDate> getWeekDate(LocalDate date) {
		List<LocalDate> dateList = new ArrayList<>();
		date = date.minusDays(NUMBER_OF_DAYS_IN_WEEK);
		for (int x = 0; x < NUMBER_OF_DAYS_IN_WEEK; x++) {
			date = date.plusDays(1);
			dateList.add(date);
		}
		return dateList;
	}

	private CellStyle getCellStyle(String displayData, LocalDate weekDate) {
		if (displayData.isEmpty() || displayData == null) {
			return getDayWeekCellStyle(weekDate);
		}
		return getDayLeaveCellStyle(displayData);
	}

	private CellStyle getDayLeaveCellStyle(String displayData) {
		if (displayData.equals(PUMUtilizationConstants.VL_HEADER)
				|| displayData.equals(PUMUtilizationConstants.SL_HEADER)
				|| displayData.equals(PUMUtilizationConstants.EL_HEADER)
				|| displayData.equals(PUMUtilizationConstants.OL_HEADER)) {
			return myStyles.get(CellStyleUtils.PUM_GREY_LEFT_DATA_STYLE);
		} else if (displayData.equals(PUMUtilizationConstants.HO_HEADER)) {
			return myStyles.get(CellStyleUtils.PUM_HOLIDAY_DATA_STYLE);
		}
		return myStyles.get(CellStyleUtils.PUM_NORMAL_RIGHT_ALIGN_DATA_STYLE);
	}

	private CellStyle getDayWeekCellStyle(LocalDate weekDate) {
		if (DayOfWeek.SATURDAY.equals(weekDate.getDayOfWeek()) || DayOfWeek.SUNDAY.equals(weekDate.getDayOfWeek())) {
			return myStyles.get(CellStyleUtils.PUM_GREY_LEFT_DATA_STYLE);
		}
		return myStyles.get(CellStyleUtils.PUM_NORMAL_RIGHT_ALIGN_DATA_STYLE);
	}

	private String getDisplayData(PUMWeeklyUtilizationData utildata, LocalDate weekDate) {
		if (utildata.getDailyData() == null) {
			return PUMUtilizationConstants.EMPTY_STRING;
		}
		String data = utildata.getDailyData().get(getDayString(weekDate));
		if (data == null || data.isEmpty()) {
			return PUMUtilizationConstants.EMPTY_STRING;
		}
		return data;
	}

	private String getDayString(LocalDate date) {
		String day = date.getDayOfWeek().toString();
		String dayResult;

		switch (day) {
		case PUMUtilizationConstants.MONDAY_UPPERCASE:
			dayResult = PUMUtilizationConstants.MONDAY;
			break;
		case PUMUtilizationConstants.TUESDAY_UPPERCASE:
			dayResult = PUMUtilizationConstants.TUESDAY;
			break;
		case PUMUtilizationConstants.WEDNESDAY_UPPERCASE:
			dayResult = PUMUtilizationConstants.WEDNESDAY;
			break;
		case PUMUtilizationConstants.THURSDAY_UPPERCASE:
			dayResult = PUMUtilizationConstants.THURSDAY;
			break;
		case PUMUtilizationConstants.FRIDAY_UPPERCASE:
			dayResult = PUMUtilizationConstants.FRIDAY;
			break;
		case PUMUtilizationConstants.SATURDAY_UPPERCASE:
			dayResult = PUMUtilizationConstants.SATURDAY;
			break;
		case PUMUtilizationConstants.SUNDAY_UPPERCASE:
			dayResult = PUMUtilizationConstants.SUNDAY;
			break;
		default:
			dayResult = PUMUtilizationConstants.EMPTY_STRING;
			break;

		}

		return dayResult;
	}
}