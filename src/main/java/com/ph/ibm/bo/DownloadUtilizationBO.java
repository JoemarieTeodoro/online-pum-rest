package com.ph.ibm.bo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.ph.ibm.OnlinePUMResource;
import com.ph.ibm.model.BillableHours;
import com.ph.ibm.model.ForecastedHours;
import com.ph.ibm.model.OutlookHours;
import com.ph.ibm.model.OverallUtilizationReport;
import com.ph.ibm.model.YtdUtilization;
import com.ph.ibm.report.DownloadUtilizationReportTable;
import com.ph.ibm.repository.DownloadUtilizationRepository;
import com.ph.ibm.repository.UtilizationRepository;
import com.ph.ibm.repository.impl.DownloadUtilizationRepositoryImpl;
import com.ph.ibm.repository.impl.UtilizationRepositoryImpl;
import com.ph.ibm.util.OpumConstants;

/**
 * @author <a HREF="dacanam@ph.ibm.com">Marjay Dacanay</a>
 * @author <a HREF="teodorj@ph.ibm.com">Joemarie Teodoro</a>
 */
public class DownloadUtilizationBO {

	private DownloadUtilizationRepository downloadUtilizationRepository;

	private OverallUtilizationReport overallUtilizationReport;

	private UtilizationRepository utilizationRepository;

	private Logger logger = Logger.getLogger(OnlinePUMResource.class);

	public DownloadUtilizationBO() {
		downloadUtilizationRepository = new DownloadUtilizationRepositoryImpl();
		utilizationRepository = new UtilizationRepositoryImpl();
		overallUtilizationReport = new OverallUtilizationReport();
	}

	public Response downloadOverallUtilization(YtdUtilization ytdUtilization) throws SQLException {
		// START DATE & END DATE// can be used for headers "BILLABLE FROM start - end"
		System.out.println(ytdUtilization.getStartDate());
		System.out.println(ytdUtilization.getEndDate());

		TreeMap<String, TreeMap<Integer, String>> fyWeekMapping = new TreeMap<>();
		Map<String, String> weekMap = downloadUtilizationRepository.retrieveWeeks(ytdUtilization);
		fyWeekMapping = downloadUtilizationRepository.retrieveFiscalYear(ytdUtilization);
		List<Map<String, Double>> listOffshoreActualHours = new ArrayList<>();
		List<Map<String, Double>> listOnshoreActualHours = new ArrayList<>();
		List<Map<String, Double>> listOffshoreForecastHours = new ArrayList<>();
		List<Map<String, Double>> listOnshoreForecastHours = new ArrayList<>();

		TreeMap<String, TreeMap<String, String>> employeeOffshoreList = downloadUtilizationRepository
				.getRollInRollOffDate(OpumConstants.OFFSHORE);
		TreeMap<String, TreeMap<String, String>> employeeOnshoreList = downloadUtilizationRepository
				.getRollInRollOffDate(OpumConstants.ONSHORE);

		TreeMap<String, Integer> offshoreAvailableHoursMap = overallUtilizationReport
				.calculateWeeklyTotalAvailableHours(
						overallUtilizationReport.getAvailableHoursOffshore(employeeOffshoreList, weekMap));
		TreeMap<String, Integer> onshoreAvailableHoursMap = overallUtilizationReport.calculateWeeklyTotalAvailableHours(
				overallUtilizationReport.getAvailableHoursOnshore(employeeOnshoreList, weekMap));

		retrieveOverallUtilization(fyWeekMapping, listOffshoreActualHours, listOnshoreActualHours,
				listOffshoreForecastHours, listOnshoreForecastHours);

		Double availableOffshoreTotalHours = overallUtilizationReport
				.calculateSumOfTotalAvailableHours(offshoreAvailableHoursMap);
		Double availableOnshoreTotalHours = overallUtilizationReport
				.calculateSumOfTotalAvailableHours(onshoreAvailableHoursMap);
		Double availableHoursGrandTotal = overallUtilizationReport
				.calculateSumOfGrandTotalAvailableHours(availableOffshoreTotalHours, availableOnshoreTotalHours);
		overallUtilizationReport.setGrandTotalAvailableHours(availableHoursGrandTotal);

		Map<String, Double> outlookHoursMap = getTotalOutlookHours(fyWeekMapping);
		TreeMap<String, String> sumOfOutlookHours = overallUtilizationReport
				.calculateSumOfTotalOutlookHours(outlookHoursMap);

		// start of billable hours
		System.out.println("BILLABLE HOURS----------------------------------------------------");
		System.out.println("Offshore Billable Hours: " + listOffshoreActualHours);
		System.out.println("Onshore Billable Hours: " + listOnshoreActualHours);
		TreeMap<String, String> totalBillableHoursMap = overallUtilizationReport
				.getReportGrandTotalMap(listOffshoreActualHours, listOnshoreActualHours);
		System.out.println("Total Billable Hours: " + totalBillableHoursMap);
		Double billableOffshoreTotalHours = overallUtilizationReport
				.calculateSumOfTotalBillableHours(listOffshoreActualHours);
		Double billableOnshoreTotalHours = overallUtilizationReport
				.calculateSumOfTotalBillableHours(listOnshoreActualHours);
		Double billableHoursGrandTotal = overallUtilizationReport
				.calculateSumOfGrandTotalBillableHours(billableOffshoreTotalHours, billableOnshoreTotalHours);
		overallUtilizationReport.setGrandTotalBillableHours(billableHoursGrandTotal);
		System.out.println("Grand Total Billable Hours: " + overallUtilizationReport.getGrandTotalBillableHours());
		// end of billable hours

		// start of available hours
		System.out.println("AVAILABLE HOURS----------------------------------------------------");
		System.out.println("Offshore Available Hours: " + offshoreAvailableHoursMap);
		System.out.println("Onshore Available Hours: " + onshoreAvailableHoursMap);
		TreeMap<String, String> totalAvailableHoursMap = overallUtilizationReport
				.getReportGrandTotalMap(offshoreAvailableHoursMap, onshoreAvailableHoursMap);
		System.out.println("Total Available Hours: " + totalAvailableHoursMap);
		System.out.println("Grand Total Available Hours: " + overallUtilizationReport.getGrandTotalAvailableHours());
		// end of available hours

		// start of ute percentage
		System.out.println("UTE ACTUALS----------------------------------------------------");
		TreeMap<String, String> offShoreUtePercentage = overallUtilizationReport
				.calculateWeeklyUtePercentage(listOffshoreActualHours, offshoreAvailableHoursMap);
		TreeMap<String, String> onshoreUtePercentage = overallUtilizationReport
				.calculateWeeklyUtePercentage(listOnshoreActualHours, onshoreAvailableHoursMap);
		System.out.println("Offshore UTE Percentage: " + offShoreUtePercentage);
		System.out.println("Onshore UTE Percentage: " + onshoreUtePercentage);
		System.out.println("Total UTE Percentage: "
				+ overallUtilizationReport.getReportGrandTotalMap(totalBillableHoursMap, totalAvailableHoursMap));
		System.out.println(overallUtilizationReport.calculateAverageUtePercentage(offShoreUtePercentage));
		System.out.println(overallUtilizationReport.calculateAverageUtePercentage(onshoreUtePercentage));
		String offShoreUtePercentageString = overallUtilizationReport
				.calculateAverageUtePercentage(offShoreUtePercentage);
		String onShoreUtePercentageString = overallUtilizationReport
				.calculateAverageUtePercentage(onshoreUtePercentage);
		TreeMap<String, String> uteReportGrandTotalMap = overallUtilizationReport
				.getReportGrandTotalMap(totalBillableHoursMap, totalAvailableHoursMap);
		String utePercentageGrandTotal = overallUtilizationReport
				.calculateSumOfGrandTotalUtePercentage(billableHoursGrandTotal, availableHoursGrandTotal);
		System.out.println("Grand Total of UTE Percentage: " + utePercentageGrandTotal);

		// end of ute percentage
		System.out.println("FORECAST HOURS----------------------------------------------------");
		System.out.println("Offshore Forecast Hours: " + listOffshoreForecastHours);
		System.out.println("Onshore Forecast Hours: " + listOnshoreForecastHours);
		System.out.println("Total Forecast Hours: "
				+ overallUtilizationReport.getReportGrandTotalMap(listOffshoreForecastHours, listOnshoreForecastHours));
		Double forecastedOffshoreTotalHours = overallUtilizationReport
				.calculateSumOfTotalForecastedHours(listOffshoreForecastHours);
		Double forecastedOnshoreTotalHours = overallUtilizationReport
				.calculateSumOfTotalForecastedHours(listOnshoreForecastHours);
		Double forecastedHoursGrandTotal = overallUtilizationReport
				.calculateSumOfGrandTotalForecastedHours(forecastedOffshoreTotalHours, forecastedOnshoreTotalHours);
		overallUtilizationReport.setGrandTotalForecastedHours(forecastedHoursGrandTotal);
		System.out.println("Grand Total Billable Hours: " + overallUtilizationReport.getGrandTotalForecastedHours());

		System.out.println("OUTLOOK HOURS----------------------------------------------------");
		System.out.println(outlookHoursMap);
		System.out.println(sumOfOutlookHours);

		try {
			return generateExcelReport(fyWeekMapping, listOffshoreActualHours, listOnshoreActualHours,
					listOffshoreForecastHours, listOnshoreForecastHours, outlookHoursMap, offshoreAvailableHoursMap,
					onshoreAvailableHoursMap, totalBillableHoursMap, billableOffshoreTotalHours,
					billableOnshoreTotalHours, billableHoursGrandTotal, offShoreUtePercentage, onshoreUtePercentage,
					offShoreUtePercentageString, onShoreUtePercentageString, uteReportGrandTotalMap,
					utePercentageGrandTotal);
		} catch (IOException e) {
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).build();
		}
	}

	private Response generateExcelReport(TreeMap<String, TreeMap<Integer, String>> fyWeekMapping,
			List<Map<String, Double>> lstOffshoreActualHours, List<Map<String, Double>> lstOnshoreActualHours,
			List<Map<String, Double>> lstOffshoreForecastHours, List<Map<String, Double>> lstOnshoreForecastHours,
			Map<String, Double> lstOutlookHours, TreeMap<String, Integer> offshoreAvailableHoursMap,
			TreeMap<String, Integer> onshoreAvailableHoursMap, TreeMap<String, String> totalBillableHoursMap,
			Double billableOffshoreTotalHours, Double billableOnshoreTotalHours, Double billableHoursGrandTotal,
			TreeMap<String, String> offShoreUtePercentage, TreeMap<String, String> onshoreUtePercentage,
			String offShoreUtePercentageString, String onShoreUtePercentageString,
			TreeMap<String, String> uteReportGrandTotalMap, String utePercentageGrandTotal) throws IOException {
		XSSFWorkbook workbook = new XSSFWorkbook();
		try {
			String filePath = "overallUtil.xlsx";
			File file = new File(filePath);
			OutputStream fileOutputStream = new FileOutputStream(file);
			Sheet sheet = workbook.createSheet();

			createActualsSection(sheet, lstOffshoreActualHours, lstOnshoreActualHours, offshoreAvailableHoursMap,
					onshoreAvailableHoursMap, totalBillableHoursMap, billableOffshoreTotalHours,
					billableOnshoreTotalHours, billableHoursGrandTotal, offShoreUtePercentage, onshoreUtePercentage,
					offShoreUtePercentageString, onShoreUtePercentageString, uteReportGrandTotalMap,
					utePercentageGrandTotal);
			createForecastSection(sheet, lstOffshoreForecastHours, lstOnshoreForecastHours);
			createQTESection(sheet, lstOutlookHours);

			workbook.write(fileOutputStream);

			ResponseBuilder response = Response.ok(file);
			response.header("Content-Disposition", "attachement; filename=" + file.getName());
			return response.build();
		} catch (IOException e) {
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).build();
		} finally {
			workbook.close();
		}
	}

	private void createActualsSection(Sheet sheet, List<Map<String, Double>> lstOffshoreActualHours,
			List<Map<String, Double>> lstOnshoreActualHours, TreeMap<String, Integer> offshoreAvailableHoursMap,
			TreeMap<String, Integer> onshoreAvailableHoursMap, TreeMap<String, String> totalBillableHoursMap,
			Double billableOffshoreTotalHours, Double billableOnshoreTotalHours, Double billableHoursGrandTotal,
			TreeMap<String, String> offShoreUtePercentage, TreeMap<String, String> onshoreUtePercentage,
			String offShoreUtePercentageString, String onShoreUtePercentageString,
			TreeMap<String, String> uteReportGrandTotalMap, String utePercentageGrandTotal) {

		sheet.getWorkbook().setSheetName(0, "Online PUM");
		sheet.setColumnWidth(0, 600);

		Row row = sheet.createRow(0);
		row = sheet.getRow(0);
		Cell cell = CellUtil.createCell(row, 0, "ACTUALS");
		sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 17));
		CellStyle styleActuals = sheet.getWorkbook().createCellStyle();
		styleActuals.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
		styleActuals.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		Font fontActuals = sheet.getWorkbook().createFont();
		fontActuals.setColor(IndexedColors.WHITE.getIndex());
		fontActuals.setBold(true);
		styleActuals.setFont(fontActuals);
		cell.setCellStyle(styleActuals);
		CellUtil.setAlignment(cell, HorizontalAlignment.CENTER);
		CellUtil.setVerticalAlignment(cell, VerticalAlignment.CENTER);

		Row row2 = sheet.createRow(2);
		row2 = sheet.getRow(2);
		Cell cell2 = CellUtil.createCell(row2, 1, "BILLABLE from ");
		sheet.addMergedRegion(new CellRangeAddress(2, 2, 1, 3));
		CellStyle style2 = sheet.getWorkbook().createCellStyle();
		Font font = sheet.getWorkbook().createFont();
		font.setColor(IndexedColors.WHITE.getIndex());
		font.setBold(true);
		style2.setFont(font);
		style2.setFillForegroundColor(IndexedColors.BLACK.getIndex());
		style2.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		cell2.setCellStyle(style2);
		CellUtil.setAlignment(cell2, HorizontalAlignment.CENTER);
		CellUtil.setVerticalAlignment(cell2, VerticalAlignment.CENTER);

		Row row9 = sheet.createRow(9);
		row9 = sheet.getRow(9);
		Cell cell9 = CellUtil.createCell(row9, 1, "AVAILABLE HOURS ");
		sheet.addMergedRegion(new CellRangeAddress(9, 9, 1, 3));
		cell9.setCellStyle(style2);
		CellUtil.setAlignment(cell9, HorizontalAlignment.CENTER);
		CellUtil.setVerticalAlignment(cell9, VerticalAlignment.CENTER);

		Row row10 = sheet.createRow(10);
		row10 = sheet.getRow(10);
		Cell cell10 = CellUtil.createCell(row10, 1, "AVAILABLE HOURS from ");
		sheet.addMergedRegion(new CellRangeAddress(10, 10, 1, 3));
		cell10.setCellStyle(style2);
		CellUtil.setAlignment(cell10, HorizontalAlignment.CENTER);
		CellUtil.setVerticalAlignment(cell10, VerticalAlignment.CENTER);

		Row row17 = sheet.createRow(17);
		row17 = sheet.getRow(17);
		Cell cell17 = CellUtil.createCell(row17, 1, "UTE ACTUALS / AVAIL ");
		sheet.addMergedRegion(new CellRangeAddress(17, 17, 1, 3));
		cell17.setCellStyle(style2);
		CellUtil.setAlignment(cell17, HorizontalAlignment.CENTER);
		CellUtil.setVerticalAlignment(cell17, VerticalAlignment.CENTER);

		Row row18 = sheet.createRow(18);
		row18 = sheet.getRow(18);
		Cell cell18 = CellUtil.createCell(row18, 1, "ACTUALS / AVAIL ");
		sheet.addMergedRegion(new CellRangeAddress(18, 18, 1, 3));
		cell18.setCellStyle(style2);
		CellUtil.setAlignment(cell18, HorizontalAlignment.CENTER);
		CellUtil.setVerticalAlignment(cell18, VerticalAlignment.CENTER);

		int[] arrayFirstTable = { 4, 5, 6, 7 };
		DownloadUtilizationReportTable tableOne = new DownloadUtilizationReportTable("Row Labels", "Grand Total",
				"Sum of Total", "Sum of ", "MM/dd/YYYY", arrayFirstTable);
		int totalColumnCount = createExcelTable(sheet, lstOffshoreActualHours, lstOnshoreActualHours, tableOne);
		createExcelTableTotals(sheet, tableOne, totalBillableHoursMap, billableOffshoreTotalHours,
				billableOnshoreTotalHours, billableHoursGrandTotal, totalColumnCount);

		int[] arraySecondTable = { 12, 13, 14, 15 };
		DownloadUtilizationReportTable tableTwo = new DownloadUtilizationReportTable("Row Labels", "Grand Total",
				"Sum of Hrs", "Sum of ", "MM/dd/YYYY", arraySecondTable);
		int totalColumnCountAvailable = createExcelTableAvailable(sheet, tableTwo, offshoreAvailableHoursMap,
				onshoreAvailableHoursMap);
		createExcelTableTotalsAvailable(sheet, tableTwo, offshoreAvailableHoursMap, onshoreAvailableHoursMap,
				overallUtilizationReport.getReportGrandTotalMap(offshoreAvailableHoursMap, onshoreAvailableHoursMap),
				totalColumnCountAvailable,
				overallUtilizationReport.calculateSumOfTotalAvailableHours(offshoreAvailableHoursMap),
				overallUtilizationReport.calculateSumOfTotalAvailableHours(onshoreAvailableHoursMap),
				overallUtilizationReport.calculateSumOfGrandTotalAvailableHours(
						overallUtilizationReport.calculateSumOfTotalAvailableHours(offshoreAvailableHoursMap),
						overallUtilizationReport.calculateSumOfTotalAvailableHours(onshoreAvailableHoursMap)));

		int[] arrayThirdTable = { 20, 21, 22, 23 };
		DownloadUtilizationReportTable tableThree = new DownloadUtilizationReportTable("Project Name", "Grand Total",
				"UTE %", "UTE % ", "MM/dd/YYYY", arrayThirdTable);
		int totalColumnCountUTE = createExcelTableUTE(sheet, tableThree, offShoreUtePercentage, onshoreUtePercentage,
				offShoreUtePercentageString, onShoreUtePercentageString, uteReportGrandTotalMap);
		createExcelTableTotalsUTE(sheet, tableThree, totalColumnCountUTE, offShoreUtePercentage, onshoreUtePercentage,
				offShoreUtePercentageString, onShoreUtePercentageString, uteReportGrandTotalMap,
				utePercentageGrandTotal);

		int[] arrayFourthTable = { 28, 29, 30, 31 };
		DownloadUtilizationReportTable tableFour = new DownloadUtilizationReportTable("Row Labels", "Grand Total",
				"Sum of Hrs", "Sum of ", "MM/dd/YYYY", arrayFourthTable);
		createExcelTable(sheet, lstOffshoreActualHours, lstOnshoreActualHours, tableFour);

		int[] arrayFifthTable = { 45, 46, 47, 48 };
		DownloadUtilizationReportTable tableFive = new DownloadUtilizationReportTable("Project Name", "Grand Total",
				"TOTAL Fore/Avail %", "Fore/Avail  % ", "MM/dd/YYYY", arrayFifthTable);
		createExcelTable(sheet, lstOffshoreActualHours, lstOnshoreActualHours, tableFive);

		int[] arraySixthTable = { 51, 52, 53, 54 };
		DownloadUtilizationReportTable tableSix = new DownloadUtilizationReportTable("Project Name", "TOTAL", "",
				"DELTA Hrs ", "MM/dd/YYYY", arraySixthTable);
		createExcelTable(sheet, lstOffshoreActualHours, lstOnshoreActualHours, tableSix);
	}

	private int createExcelTable(Sheet sheet, List<Map<String, Double>> lstOffshore,
			List<Map<String, Double>> lstOnshore, DownloadUtilizationReportTable table) {
		Row firstRow = sheet.createRow(table.getExcelRows()[0]);
		Row secondRow = sheet.createRow(table.getExcelRows()[1]);
		Row thirdRow = sheet.createRow(table.getExcelRows()[2]);
		Row fourthRow = sheet.createRow(table.getExcelRows()[3]);
		int count = 1;
		int innerCountReturn = 0;

		CellUtil.createCell(firstRow, count, table.getFirstColumnLabel());
		CellUtil.createCell(secondRow, count, "Offshore");
		CellUtil.createCell(thirdRow, count, "Onshore");
		CellUtil.createCell(fourthRow, count, table.getTotalLabel());
		for (Map<String, Double> lstOffshoreActualHoursMap : lstOffshore) {
			for (Map.Entry<String, Double> entry : lstOffshoreActualHoursMap.entrySet()) {
				innerCountReturn = ++count;
				CellUtil.createCell(firstRow, innerCountReturn, entry.getKey());
				CellUtil.createCell(secondRow, innerCountReturn, entry.getValue().toString());
			}
		}
		CellUtil.createCell(firstRow, ++count, table.getSumLabel());
		count = 1;
		for (Map<String, Double> lstOnshoreActualHoursMap : lstOnshore) {
			for (Map.Entry<String, Double> entry : lstOnshoreActualHoursMap.entrySet()) {
				int innerCount = ++count;
				CellUtil.createCell(thirdRow, innerCount, entry.getValue().toString());
			}
		}
		return innerCountReturn;
	}

	private void createExcelTableTotals(Sheet sheet, DownloadUtilizationReportTable table,
			TreeMap<String, String> totalBillableHoursMap, Double billableOffshoreTotalHours,
			Double billableOnshoreTotalHours, Double billableHoursGrandTotal, int totalColumnCount) {

		Row secondRow = sheet.getRow(table.getExcelRows()[1]);
		Row thirdRow = sheet.getRow(table.getExcelRows()[2]);
		Row fourthRow = sheet.getRow(table.getExcelRows()[3]);
		int count = 1;

		int totalCountPlusOne = ++totalColumnCount;
		CellUtil.createCell(secondRow, totalCountPlusOne, billableOffshoreTotalHours.toString());
		CellUtil.createCell(thirdRow, totalCountPlusOne, billableOnshoreTotalHours.toString());

		Double grandTotalofSum = billableOnshoreTotalHours + billableOffshoreTotalHours;
		CellUtil.createCell(fourthRow, totalCountPlusOne, grandTotalofSum.toString());

		for (Map.Entry<String, String> entry : totalBillableHoursMap.entrySet()) {
			int innerCount = ++count;
			CellUtil.createCell(fourthRow, innerCount, entry.getValue().toString());
		}
	}

	private int createExcelTableAvailable(Sheet sheet, DownloadUtilizationReportTable table,
			TreeMap<String, Integer> offshoreAvailableHoursMap, TreeMap<String, Integer> onshoreAvailableHoursMap) {
		Row firstRow = sheet.createRow(table.getExcelRows()[0]);
		Row secondRow = sheet.createRow(table.getExcelRows()[1]);
		Row thirdRow = sheet.createRow(table.getExcelRows()[2]);
		Row fourthRow = sheet.createRow(table.getExcelRows()[3]);
		int count = 1;
		int innerCountReturn = 0;

		CellUtil.createCell(firstRow, count, table.getFirstColumnLabel());
		CellUtil.createCell(secondRow, count, "Offshore");
		CellUtil.createCell(thirdRow, count, "Onshore");
		CellUtil.createCell(fourthRow, count, table.getTotalLabel());
		for (Entry<String, Integer> entry : offshoreAvailableHoursMap.entrySet()) {
			innerCountReturn = ++count;
			CellUtil.createCell(firstRow, innerCountReturn, entry.getKey());
			CellUtil.createCell(secondRow, innerCountReturn, entry.getValue().toString());
		}

		count = 1;
		innerCountReturn = 0;
		for (Entry<String, Integer> entry : onshoreAvailableHoursMap.entrySet()) {
			innerCountReturn = ++count;
			CellUtil.createCell(firstRow, innerCountReturn, entry.getKey());
			CellUtil.createCell(thirdRow, innerCountReturn, entry.getValue().toString());
		}
		CellUtil.createCell(firstRow, ++count, table.getSumLabel());
		return innerCountReturn;
	}

	private void createExcelTableTotalsAvailable(Sheet sheet, DownloadUtilizationReportTable table,
			TreeMap<String, Integer> offshoreAvailableHoursMap, TreeMap<String, Integer> onshoreAvailableHoursMap,
			TreeMap<String, String> totalAvailableHoursMap, int totalColumnCount, Double availableOffshoreTotalHours,
			Double availableOnshoreTotalHours, Double availableHoursGrandTotal) {

		Row secondRow = sheet.getRow(table.getExcelRows()[1]);
		Row thirdRow = sheet.getRow(table.getExcelRows()[2]);
		Row fourthRow = sheet.getRow(table.getExcelRows()[3]);
		int count = 1;

		int totalCountPlusOne = ++totalColumnCount;
		CellUtil.createCell(secondRow, totalCountPlusOne, availableOffshoreTotalHours.toString());
		CellUtil.createCell(thirdRow, totalCountPlusOne, availableOnshoreTotalHours.toString());

		CellUtil.createCell(fourthRow, totalCountPlusOne, availableHoursGrandTotal.toString());

		for (Map.Entry<String, String> entry : totalAvailableHoursMap.entrySet()) {
			int innerCount = ++count;
			CellUtil.createCell(fourthRow, innerCount, entry.getValue().toString());
		}
	}

	private int createExcelTableUTE(Sheet sheet, DownloadUtilizationReportTable table,
			TreeMap<String, String> offShoreUtePercentage, TreeMap<String, String> onshoreUtePercentage,
			String offShoreUtePercentageString, String onShoreUtePercentageString,
			TreeMap<String, String> uteReportGrandTotalMap) {
		Row firstRow = sheet.createRow(table.getExcelRows()[0]);
		Row secondRow = sheet.createRow(table.getExcelRows()[1]);
		Row thirdRow = sheet.createRow(table.getExcelRows()[2]);
		Row fourthRow = sheet.createRow(table.getExcelRows()[3]);
		int count = 1;
		int innerCountReturn = 0;

		CellUtil.createCell(firstRow, count, table.getFirstColumnLabel());
		CellUtil.createCell(secondRow, count, "Offshore");
		CellUtil.createCell(thirdRow, count, "Onshore");
		CellUtil.createCell(fourthRow, count, table.getTotalLabel());
		for (Map.Entry<String, String> entry : offShoreUtePercentage.entrySet()) {
			innerCountReturn = ++count;
			CellUtil.createCell(firstRow, innerCountReturn, entry.getKey());
			CellUtil.createCell(secondRow, innerCountReturn, entry.getValue().toString());
		}
		CellUtil.createCell(firstRow, ++count, table.getSumLabel());
		count = 1;
		for (Map.Entry<String, String> entry : onshoreUtePercentage.entrySet()) {
			int innerCount = ++count;
			CellUtil.createCell(thirdRow, innerCount, entry.getValue().toString());
		}
		return innerCountReturn;
	}

	private void createExcelTableTotalsUTE(Sheet sheet, DownloadUtilizationReportTable table, int totalColumnCountUTE,
			TreeMap<String, String> offShoreUtePercentage, TreeMap<String, String> onshoreUtePercentage,
			String offShoreUtePercentageString, String onShoreUtePercentageString,
			TreeMap<String, String> uteReportGrandTotalMap, String utePercentageGrandTotal) {

		Row secondRow = sheet.getRow(table.getExcelRows()[1]);
		Row thirdRow = sheet.getRow(table.getExcelRows()[2]);
		Row fourthRow = sheet.getRow(table.getExcelRows()[3]);
		int count = 1;

		int totalCountPlusOne = ++totalColumnCountUTE;
		CellUtil.createCell(secondRow, totalCountPlusOne, offShoreUtePercentageString.toString());
		CellUtil.createCell(thirdRow, totalCountPlusOne, onShoreUtePercentageString.toString());
		CellUtil.createCell(fourthRow, totalCountPlusOne, utePercentageGrandTotal);
		for (Map.Entry<String, String> entry : uteReportGrandTotalMap.entrySet()) {
			int innerCount = ++count;
			CellUtil.createCell(fourthRow, innerCount, entry.getValue().toString());
		}
	}

	private void createForecastSection(Sheet sheet, List<Map<String, Double>> lstOffshoreForecastHours,
			List<Map<String, Double>> lstOnshoreForecastHours) {
		int count = 2;
		Row row = sheet.createRow(25);
		row = sheet.getRow(25);
		Cell cell = CellUtil.createCell(row, 0, "FORECASTED");
		sheet.addMergedRegion(new CellRangeAddress(25, 26, 0, 17));

		CellStyle styleForecasted = sheet.getWorkbook().createCellStyle();
		styleForecasted.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
		styleForecasted.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		Font fontForecasted = sheet.getWorkbook().createFont();
		fontForecasted.setColor(IndexedColors.WHITE.getIndex());
		fontForecasted.setBold(true);
		styleForecasted.setFont(fontForecasted);
		cell.setCellStyle(styleForecasted);
		CellUtil.setAlignment(cell, HorizontalAlignment.CENTER);
		CellUtil.setVerticalAlignment(cell, VerticalAlignment.CENTER);

		Row firstRow = sheet.createRow(57);
		Row secondRow = sheet.createRow(58);
		Row thirdRow = sheet.createRow(59);

		CellUtil.createCell(firstRow, 1, "Row Labels");
		CellUtil.createCell(firstRow, 2, "Sum of QTD (Actuals + Remaining) ");
		CellUtil.createCell(firstRow, 3, "Sum of QTD Available");
		CellUtil.createCell(firstRow, 4, "Sum of QTD %");

		CellUtil.createCell(secondRow, 1, "USAA");
		for (Map<String, Double> lstOffshoreActualHoursMap : lstOffshoreForecastHours) {
			for (Map.Entry<String, Double> entry : lstOffshoreActualHoursMap.entrySet()) {
				int innerCount = count++;
				CellUtil.createCell(secondRow, innerCount, entry.getValue().toString());
			}
		}

		count = 2;
		CellUtil.createCell(thirdRow, 1, "Grand Total");
		for (Map<String, Double> lstOffshoreActualHoursMap : lstOffshoreForecastHours) {
			for (Map.Entry<String, Double> entry : lstOffshoreActualHoursMap.entrySet()) {
				int innerCount = count++;
				CellUtil.createCell(thirdRow, innerCount, entry.getValue().toString());
			}
		}
	}

	private void createQTESection(Sheet sheet, Map<String, Double> lstOutlookHours) {
		int count = 2;
		Row row = sheet.createRow(62);
		row = sheet.getRow(62);
		Cell cell = CellUtil.createCell(row, 0, "QTE");
		sheet.addMergedRegion(new CellRangeAddress(62, 62, 0, 17));

		CellStyle style = sheet.getWorkbook().createCellStyle();
		style.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		cell.setCellStyle(style);
		CellUtil.setAlignment(cell, HorizontalAlignment.CENTER);
		CellUtil.setVerticalAlignment(cell, VerticalAlignment.CENTER);

		Row firstRow = sheet.createRow(64);
		Row secondRow = sheet.createRow(65);
		Row thirdRow = sheet.createRow(66);

		CellUtil.createCell(firstRow, 1, "Row Labels");
		CellUtil.createCell(firstRow, 2, "Commit Hours");
		CellUtil.createCell(firstRow, 3, "Available Hours");
		CellUtil.createCell(firstRow, 4, "QTR Commit %");
		CellUtil.createCell(firstRow, 5, "QTR Outlook %");
		CellUtil.createCell(firstRow, 6, "QTR Delta %");
		CellUtil.createCell(firstRow, 7, "QTR Delta (Hrs)");
		CellUtil.createCell(firstRow, 8, "Comments");

		CellUtil.createCell(secondRow, 1, "USAA");
		for (Map.Entry<String, Double> entry : lstOutlookHours.entrySet()) {
			int innerCount = count++;
			CellUtil.createCell(secondRow, innerCount, entry.getValue().toString());
		}

		count = 2;
		CellUtil.createCell(thirdRow, 1, "Grand Total");
		for (Map.Entry<String, Double> entry : lstOutlookHours.entrySet()) {
			int innerCount = count++;
			CellUtil.createCell(thirdRow, innerCount, entry.getValue().toString());
		}
	}

	/**
	 * @param fyWeekMapping
	 * @param listOffshoreActualHours
	 * @param listOnshoreActualHours
	 * @param listOffshoreForecastHours
	 * @param listOnshoreForecastHours
	 * @param listOutlookHours
	 * @throws SQLException
	 */
	private void retrieveOverallUtilization(TreeMap<String, TreeMap<Integer, String>> fyWeekMapping,
			List<Map<String, Double>> listOffshoreActualHours, List<Map<String, Double>> listOnshoreActualHours,
			List<Map<String, Double>> listOffshoreForecastHours, List<Map<String, Double>> listOnshoreForecastHours)
			throws SQLException {
		for (Entry<String, TreeMap<Integer, String>> fyWeekMap : fyWeekMapping.entrySet()) {
			String weekName = fyWeekMap.getKey();
			for (Entry<Integer, String> map : fyWeekMap.getValue().entrySet()) {
				int yearId = map.getKey();
				String weekEnding = map.getValue();
				listOffshoreActualHours.add(getTotalOffshoreActualHours(weekName, yearId, weekEnding));
				listOnshoreActualHours.add(getTotalOnshoreActualHours(weekName, yearId, weekEnding));
				listOffshoreForecastHours.add(getTotalOffshoreForecastHours(weekName, yearId, weekEnding));
				listOnshoreForecastHours.add(getTotalOnshoreForecastHours(weekName, yearId, weekEnding));
			}
		}
	}

	/**
	 * @param weekName
	 * @param yearId
	 * @param weekEnding
	 * @return
	 */
	private Map<String, Double> getTotalOffshoreActualHours(String weekName, int yearId, String weekEnding) {
		BillableHours billableHours = calculateTotalOffshoreHours(weekName, yearId, weekEnding);
		return billableHours.getOffshoreTotalHours();
	}

	/**
	 * @param weekName
	 * @param yearId
	 * @param weekEnding
	 * @return
	 */
	private Map<String, Double> getTotalOnshoreActualHours(String weekName, int yearId, String weekEnding) {
		BillableHours billableHours = calculateTotalOnshoreHours(weekName, yearId, weekEnding);
		return billableHours.getOnshoreTotalHours();
	}

	private BillableHours calculateTotalOnshoreHours(String weekName, int yearId, String weekEnding) {
		BillableHours billableHours = overallUtilizationReport.getBillableHours();
		List<Map<String, List<Double>>> actualHours = downloadUtilizationRepository.retrieveActualHours(weekName,
				yearId, weekEnding, OpumConstants.ONSHORE);
		Map<String, Double> totalOnshoreHours = billableHours.calculateTotalOnshoreHours(actualHours);
		billableHours.setBillableOnshoreTotalHours(totalOnshoreHours);
		return billableHours;
	}

	private BillableHours calculateTotalOffshoreHours(String weekName, int yearId, String weekEnding) {
		BillableHours billableHours = overallUtilizationReport.getBillableHours();
		List<Map<String, List<Double>>> actualHours = downloadUtilizationRepository.retrieveActualHours(weekName,
				yearId, weekEnding, OpumConstants.OFFSHORE);
		Map<String, Double> totalOffshoreHours = billableHours.calculateTotalOffshoreHours(actualHours);
		billableHours.setBillableOffshoreTotalHours(totalOffshoreHours);
		return billableHours;
	}

	/**
	 * @param weekName
	 * @param yearId
	 * @param weekEnding
	 * @return
	 */
	private Map<String, Double> getTotalOffshoreForecastHours(String weekName, int yearId, String weekEnding) {
		ForecastedHours forecastedHours = overallUtilizationReport.getForecastedHours();
		List<Map<String, List<Double>>> offshore_forecastedHours = downloadUtilizationRepository
				.retrieveForecastedHours(weekName, yearId, weekEnding, OpumConstants.OFFSHORE);
		return forecastedHours.calculateOffshoreTotalHours(offshore_forecastedHours);
	}

	/**
	 * @param weekName
	 * @param yearId
	 * @param weekEnding
	 * @return
	 */
	private Map<String, Double> getTotalOnshoreForecastHours(String weekName, int yearId, String weekEnding) {
		ForecastedHours forecastedHours = overallUtilizationReport.getForecastedHours();
		List<Map<String, List<Double>>> onshore_forecastedHours = downloadUtilizationRepository
				.retrieveForecastedHours(weekName, yearId, weekEnding, OpumConstants.ONSHORE);
		return forecastedHours.calculateOnshoreTotalHours(onshore_forecastedHours);
	}

	/**
	 * @param fyWeekMapping
	 * @return
	 * @throws SQLException
	 */
	private Map<String, Double> getTotalOutlookHours(TreeMap<String, TreeMap<Integer, String>> fyWeekMapping)
			throws SQLException {
		OutlookHours outlook = overallUtilizationReport.getOutlookHours();
		List<Map<String, List<Double>>> outlookHours = downloadUtilizationRepository.getOutlookHours(fyWeekMapping);
		return outlook.calculateTotalHours(outlookHours);
	}

}
