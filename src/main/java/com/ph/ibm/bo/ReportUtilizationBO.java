package com.ph.ibm.bo;

import java.sql.SQLException;
import java.text.DateFormatSymbols;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.ph.ibm.model.EmployeeReportDetails;
import com.ph.ibm.model.PUMYear;
import com.ph.ibm.model.TotalLeaves;
import com.ph.ibm.report.pum.data.PUMEmployeeData;
import com.ph.ibm.report.pum.data.PUMExcelData;
import com.ph.ibm.report.pum.data.PUMMonthlyUtilizationData;
import com.ph.ibm.report.pum.data.PUMOverallUtilizationData;
import com.ph.ibm.report.pum.data.PUMQuarterlyUtilizationData;
import com.ph.ibm.report.pum.data.PUMWeeklyUtilizationData;
import com.ph.ibm.repository.EmployeeRepository;
import com.ph.ibm.repository.MonthRepository;
import com.ph.ibm.repository.UtilizationRepository;
import com.ph.ibm.repository.impl.EmployeeRepositoryImpl;
import com.ph.ibm.repository.impl.MonthRepositoryImpl;
import com.ph.ibm.repository.impl.UtilizationRepositoryImpl;
import com.ph.ibm.util.OpumConstants;
import com.ph.ibm.report.pum.PUMUtilizationConstants;

public class ReportUtilizationBO {

	public PUMExcelData getUtilizationPerEmployee(PUMExcelData pumExcel) throws SQLException {
		EmployeeRepository employeeRepository = new EmployeeRepositoryImpl();
		UtilizationRepository utilizationRepository = new UtilizationRepositoryImpl();

		List<EmployeeReportDetails> employeeList = employeeRepository.getEmployeeListReportDetails();
		List<PUMEmployeeData> pumEmployeeDataList = new ArrayList<PUMEmployeeData>();

		String selectedYearID = getSelectedYearID(pumExcel.getFiscalYear());

		for (EmployeeReportDetails empDet : employeeList) {
			List<PUMWeeklyUtilizationData> pumWeeklyUtilizationDataList = utilizationRepository
					.getEmployeeUtilizationHours(empDet.getSerialNumber(), selectedYearID);

			List<PUMMonthlyUtilizationData> pumMonthlyUtilizationDataList = getUtilizationPerMonth(
					pumWeeklyUtilizationDataList, selectedYearID);
			Map<String, PUMQuarterlyUtilizationData> pumQuarterlyUtilizationDataList = getQuarterlyUtilization(
					pumMonthlyUtilizationDataList);
			PUMOverallUtilizationData pumOverallUtilizationData = getOverallUtilization(
					pumQuarterlyUtilizationDataList);

			PUMEmployeeData pumEmployeeData = new PUMEmployeeData(empDet.getProjectName(), empDet.getSerialNumber(),
					empDet.getResourceName(), empDet.getRollInDate(), empDet.getRollOffDate(),
					pumOverallUtilizationData);
			pumEmployeeDataList.add(pumEmployeeData);
		}
		pumExcel.setEmployeeData(pumEmployeeDataList);

		return pumExcel;
	}

	private String getSelectedYearID(String selectedYear) throws SQLException {
		PUMYear pumYear = new PUMYear();
		YearBO yearBO = new YearBO();
		pumYear = yearBO.retrieveYearDate(Integer.parseInt(selectedYear));
		String selectedYearID = Integer.toString(pumYear.getYearId());

		return selectedYearID;
	}

	private List<PUMMonthlyUtilizationData> getUtilizationPerMonth(
			List<PUMWeeklyUtilizationData> pumWeeklyUtilizationData, String selectedYearID) throws SQLException {
		List<PUMMonthlyUtilizationData> pumMonthlyUtilizationDataList = new ArrayList<PUMMonthlyUtilizationData>();
		int monthCounter = 1;
		MonthRepository monthRepository = new MonthRepositoryImpl();
		List<PUMWeeklyUtilizationData> weeklyUtilizationDataList = new ArrayList<PUMWeeklyUtilizationData>();

		for (PUMWeeklyUtilizationData data : pumWeeklyUtilizationData) {
			if (monthCounter > OpumConstants.COUNT_OF_MONTHS_PER_YEAR) {
				break;
			}
			String monthEndDate = monthRepository.getMonthEndDate(selectedYearID, monthCounter);
			weeklyUtilizationDataList.add(data);

			LocalDate dateEndMonth = stringToLocalDate(monthEndDate);
			LocalDate dateEndWeek = stringToLocalDate(data.getWeekDate());

			if (dateEndMonth.equals(dateEndWeek) || dateEndMonth.isBefore(dateEndWeek)) {
				PUMMonthlyUtilizationData pumMonthlyUtilizationData = new PUMMonthlyUtilizationData(
						getMonthForInt(monthCounter - 1), weeklyUtilizationDataList,
						getMonthlyTotals(weeklyUtilizationDataList), getMonthlyActual(weeklyUtilizationDataList),
						getMonthlyAvailable(weeklyUtilizationDataList));
				pumMonthlyUtilizationDataList.add(pumMonthlyUtilizationData);

				weeklyUtilizationDataList = new ArrayList<PUMWeeklyUtilizationData>();
				monthCounter++;
			}
		}

		return pumMonthlyUtilizationDataList;
	}

	private LocalDate stringToLocalDate(String date) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-M-d");
		return LocalDate.parse(date, formatter);
	}

	private int getMonthlyActual(List<PUMWeeklyUtilizationData> weeklyUtilizationDataList) {
		return weeklyUtilizationDataList.stream().mapToInt(weekdata -> weekdata.getTotal()).sum();
	}

	private int getMonthlyAvailable(List<PUMWeeklyUtilizationData> weeklyUtilizationDataList) {
		return weeklyUtilizationDataList.size() * OpumConstants.TOTAL_WEEKLY_HOURS;
	}

	private Map<String, Integer> getMonthlyTotals(List<PUMWeeklyUtilizationData> pumWeeklyUtilizationData) {
		TotalLeaves totalLeaves = new TotalLeaves();
		for (PUMWeeklyUtilizationData data : pumWeeklyUtilizationData) {
			for (Map.Entry<String, String> entry : data.getDailyData().entrySet()) {
				totalLeaves = addToTotalLeaves(totalLeaves, 1, entry.getValue());
			}
		}
		Map<String, Integer> totalLeavesPerMonth = converToTotalLeavesList(totalLeaves);
		return totalLeavesPerMonth;
	}

	private TotalLeaves addToTotalLeaves(TotalLeaves totalLeaves, int leaveCount, String leaveType) {

		if (PUMUtilizationConstants.VL_HEADER.equals(leaveType)) {
			totalLeaves.setTotalVL(totalLeaves.getTotalVL() + leaveCount);
		} else if (PUMUtilizationConstants.SL_HEADER.equals(leaveType)) {
			totalLeaves.setTotalSL(totalLeaves.getTotalSL() + leaveCount);
		} else if (PUMUtilizationConstants.OL_HEADER.equals(leaveType)) {
			totalLeaves.setTotalOL(totalLeaves.getTotalOL() + leaveCount);
		} else if (PUMUtilizationConstants.EL_HEADER.equals(leaveType)) {
			totalLeaves.setTotalEL(totalLeaves.getTotalEL() + leaveCount);
		} else if (PUMUtilizationConstants.TR_HEADER.equals(leaveType)) {
			totalLeaves.setTotalTR(totalLeaves.getTotalTR() + leaveCount);
		} else if (PUMUtilizationConstants.HO_HEADER.equals(leaveType)) {
			totalLeaves.setTotalHO(totalLeaves.getTotalHO() + leaveCount);
		} else if (PUMUtilizationConstants.CDO_HEADER.equals(leaveType)) {
			totalLeaves.setTotalCDO(totalLeaves.getTotalCDO() + leaveCount);
		}

		return totalLeaves;
	}

	private Map<String, Integer> converToTotalLeavesList(TotalLeaves totalLeaves) {
		Map<String, Integer> totalLeavesList = new HashMap<String, Integer>();

		totalLeavesList.put(PUMUtilizationConstants.VL_HEADER, totalLeaves.getTotalVL());
		totalLeavesList.put(PUMUtilizationConstants.SL_HEADER, totalLeaves.getTotalSL());
		totalLeavesList.put(PUMUtilizationConstants.OL_HEADER, totalLeaves.getTotalOL());
		totalLeavesList.put(PUMUtilizationConstants.EL_HEADER, totalLeaves.getTotalEL());
		totalLeavesList.put(PUMUtilizationConstants.TR_HEADER, totalLeaves.getTotalTR());
		totalLeavesList.put(PUMUtilizationConstants.HO_HEADER, totalLeaves.getTotalHO());
		totalLeavesList.put(PUMUtilizationConstants.CDO_HEADER, totalLeaves.getTotalCDO());

		return totalLeavesList;
	}

	private Map<String, PUMQuarterlyUtilizationData> getQuarterlyUtilization(
			List<PUMMonthlyUtilizationData> pumMonthlyUtilizationDataList) throws SQLException {

		Map<String, PUMMonthlyUtilizationData> pumMonthlyDataOneQuarter = new LinkedHashMap<String, PUMMonthlyUtilizationData>();
		Map<String, PUMQuarterlyUtilizationData> pumQuarterUtilizationDataMap = new LinkedHashMap<String, PUMQuarterlyUtilizationData>();

		int monthCount = 1;
		int quarterCount = 1;

		for (PUMMonthlyUtilizationData data : pumMonthlyUtilizationDataList) {

			if (monthCount == OpumConstants.COUNT_OF_MONTHS_PER_QUARTER) {
				pumMonthlyDataOneQuarter.put(data.getMonthName(), data);
				PUMQuarterlyUtilizationData pumQuarlyUtilizationData = new PUMQuarterlyUtilizationData(
						pumMonthlyDataOneQuarter, getQuarteryActualHours(pumMonthlyDataOneQuarter),
						getQuarteryAvailableHours(pumMonthlyDataOneQuarter));
				pumQuarlyUtilizationData.setQuarterName("Q" + Integer.toString(quarterCount));
				pumQuarterUtilizationDataMap.put(pumQuarlyUtilizationData.getQuarterName(), pumQuarlyUtilizationData);
				pumMonthlyDataOneQuarter = new LinkedHashMap<>();
				monthCount = 1;
				quarterCount++;
			} else {
				pumMonthlyDataOneQuarter.put(data.getMonthName(), data);
				monthCount++;
			}
		}
		return pumQuarterUtilizationDataMap;
	}

	private int getQuarteryActualHours(Map<String, PUMMonthlyUtilizationData> monthDatas) {
		return monthDatas.values().stream().mapToInt(month -> ((Double) month.getActualHours()).intValue()).sum();
	}

	private int getQuarteryAvailableHours(Map<String, PUMMonthlyUtilizationData> monthDatas) {
		return monthDatas.values().stream().mapToInt(month -> ((Double) month.getAvailableHours()).intValue()).sum();
	}

	private PUMOverallUtilizationData getOverallUtilization(
			Map<String, PUMQuarterlyUtilizationData> pumQuarterlyUtilizationDataMap) throws SQLException {

		PUMOverallUtilizationData pumOverallUtilizationData = new PUMOverallUtilizationData(
				pumQuarterlyUtilizationDataMap, getOverallLeaves(pumQuarterlyUtilizationDataMap),
				getOverAllActualHours(pumQuarterlyUtilizationDataMap),
				getOverAllAvailableHours(pumQuarterlyUtilizationDataMap));

		return pumOverallUtilizationData;
	}

	private Map<String, Integer> getOverallLeaves(
			Map<String, PUMQuarterlyUtilizationData> pumQuarterlyUtilizationDataMap) {
		TotalLeaves totalLeaves = new TotalLeaves();
		for (PUMQuarterlyUtilizationData quarterData : pumQuarterlyUtilizationDataMap.values()) {
			for (PUMMonthlyUtilizationData monthlyData : quarterData.getMonthDatas().values()) {
				for (String i : monthlyData.getTotalLeaves().keySet()) {
					totalLeaves = addToTotalLeaves(totalLeaves, monthlyData.getTotalLeaves().get(i), i);
				}
			}
		}
		Map<String, Integer> overallLeaves = converToTotalLeavesList(totalLeaves);
		return overallLeaves;
	}

	private int getOverAllActualHours(Map<String, PUMQuarterlyUtilizationData> pumQuarterlyUtilizationDataMap) {
		int totalActualHours = 0;

		for (PUMQuarterlyUtilizationData quarterData : pumQuarterlyUtilizationDataMap.values()) {
			for (PUMMonthlyUtilizationData monthlyData : quarterData.getMonthDatas().values()) {
				totalActualHours += (int) monthlyData.getActualHours();
			}
		}

		return totalActualHours;
	}

	private int getOverAllAvailableHours(Map<String, PUMQuarterlyUtilizationData> pumQuarterlyUtilizationDataMap) {
		int totalAvailableHours = 0;

		for (PUMQuarterlyUtilizationData quarterData : pumQuarterlyUtilizationDataMap.values()) {
			for (PUMMonthlyUtilizationData monthlyData : quarterData.getMonthDatas().values()) {
				totalAvailableHours += (int) monthlyData.getAvailableHours();
			}
		}

		return totalAvailableHours;
	}

	private String getMonthForInt(int m) {
		String month = "invalid";
		DateFormatSymbols dfs = new DateFormatSymbols();
		String[] months = dfs.getMonths();
		if (m >= 0 && m < OpumConstants.COUNT_OF_MONTHS_PER_YEAR) {
			month = months[m];
		}
		return month;
	}

}
