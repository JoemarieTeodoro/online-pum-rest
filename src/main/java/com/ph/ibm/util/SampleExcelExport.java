package com.ph.ibm.util;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.util.CellRangeAddress;

import com.ph.ibm.model.ProjectEngagement;
import com.ph.ibm.model.Utilization;
import com.ph.ibm.model.UtilizationJson;
import com.ph.ibm.model.UtilizationYear;
import com.ph.ibm.opum.exception.OpumException;
import com.ph.ibm.repository.ProjectEngagementRepository;
import com.ph.ibm.repository.ProjectRepository;
import com.ph.ibm.repository.UtilizationEngagementRepository;

public class SampleExcelExport {

	private static final String SHEET_NAME = "PUM 2017";
	private static final String UTIL_YEAR = "2017";
	private static final String ROLL_OFF_DATE_HEADER = "Roll Off Date";
	private static final String ROLL_IN_DATE_HEADER = "Roll In Date";
	private static final String YEAR_HEADER = "Year";
	private static final String EMP_SERIAL_HEADER = "Employee Serial No.";
	private static final String PROJECT_HEADER = "Project";

	private static final String FILENAME = "USAA_PUM_as of %s.xls";
	private static final String PATH = "src/test/resources";

	private String[] constantsArray = { PROJECT_HEADER, EMP_SERIAL_HEADER, YEAR_HEADER, ROLL_IN_DATE_HEADER, ROLL_OFF_DATE_HEADER };

	private int employeeDataRowIndex = 5;
	
	private HSSFWorkbook workbook;
	private HSSFSheet sheet;
	
	private HSSFRow monthRowHeader;
	private HSSFRow dayOfMonthRowHeader;
	private HSSFRow dataRowHeader;
	
	private Font dataFont;
	private Font dataHeaderFont;
	private Font monthHeaderFont;

	private HSSFCellStyle dataStyle;
	private HSSFCellStyle headerStyle;
	private HSSFCellStyle monthHeaderStyle;

	private UtilizationEngagementRepository utilEngagementImpl;
	private ProjectEngagementRepository projEngagementImpl;
	private ProjectRepository projImpl;

	private List<ProjectEngagement> projectEngagements;

	private Logger logger = Logger.getLogger(SampleExcelExport.class);
	
	public SampleExcelExport() {}
	
	public SampleExcelExport(UtilizationEngagementRepository utilEngagement, ProjectEngagementRepository projEngagement, ProjectRepository proj) {
		this.utilEngagementImpl = utilEngagement;
		this.projEngagementImpl = projEngagement;
		this.projImpl = proj;
	}

	public File exportToExcel() throws IOException, SQLException, OpumException {
		sheet = workbook.createSheet(SHEET_NAME);

		sheet.addMergedRegion(new CellRangeAddress(0, 3, 0, 4));

		monthRowHeader = sheet.createRow(0);
		dayOfMonthRowHeader = sheet.createRow(3);
		dataRowHeader = sheet.createRow(4);

		List<String> projectEngagementHeaderConstants = Arrays.asList(constantsArray);

		// Populate initial project engagement headers
		for (String dataHeaderValue : Arrays.asList(constantsArray)) {
			int index = projectEngagementHeaderConstants.indexOf(dataHeaderValue);
			populateCell(dataRowHeader.createCell(index), headerStyle, dataHeaderValue);
			sheet.autoSizeColumn(index);
		}

		// Loop through each assigned project of a resource
		projectEngagements = projEngagementImpl.getAllProjectEngagement();
		for (ProjectEngagement projEngagement : projectEngagements) {
			Utilization utilData = utilEngagementImpl.downloadUtilization(UTIL_YEAR, projEngagement.getEmployeeId());
			
			HSSFRow dataEntryRow = sheet.createRow(employeeDataRowIndex);

			// Could be refactored to a parameter object - values could possibly be derived
			String[] projEngagementData = { "USAA", utilData.getEmployeeSerial(), utilData.getYear(),
					new SimpleDateFormat("MM-dd-yyyy").format(projEngagement.getStartDate()),
					new SimpleDateFormat("MM-dd-yyyy").format(projEngagement.getEndDate()) };

			// Populate data mapped to project engagement headers
			for (int i = 0; i < projEngagementData.length; i++) {
				populateCell(dataEntryRow.createCell(i), dataStyle, projEngagementData[i]);
			}

			UtilizationYear utilizationYear = ObjectMapperAdapter.unmarshal(utilData.getUtilizationJson(), UtilizationYear.class);

			int utilizationDataColIndex = 5;
			int dataEntryRowCount = 1;
			int monthStartColIndex = 5;
			int monthEndColIndex = 5;

			// Iterate though each utilization of a resource for the given year
			for (UtilizationJson utilJson : utilizationYear.getUtilizationJSON()) {
				populateCell(dataEntryRow.createCell(utilizationDataColIndex), dataStyle, utilJson.getUtilizationHours());
				populateCell(dayOfMonthRowHeader.createCell(utilizationDataColIndex), headerStyle, String.valueOf(utilJson.getDayOfMonth()));
				
				try {
					populateCell(dataRowHeader.createCell(utilizationDataColIndex), headerStyle, setDayHeaders(utilJson));
					populateCell(monthRowHeader.createCell(utilizationDataColIndex), monthHeaderStyle, setMonthHeaders(utilJson));
				} catch (IllegalArgumentException e) {
					logger.error(e.getMessage());
					throw new OpumException(e.getMessage());
				}

				utilizationDataColIndex++;

				int assignedProjectCount = projectEngagements.indexOf(projEngagement);
				if (dataEntryRowCount < utilizationYear.getUtilizationJSON().size() && assignedProjectCount == 0) {
					if (utilJson.getMonth() != utilizationYear.getUtilizationJSON().get(dataEntryRowCount).getMonth()) {
						sheet.addMergedRegion(new CellRangeAddress(0, 0, monthStartColIndex, monthEndColIndex));
						monthStartColIndex = monthEndColIndex + 1;
					}
				} else if (dataEntryRowCount == utilizationYear.getUtilizationJSON().size() && assignedProjectCount == 0) {
					sheet.addMergedRegion(new CellRangeAddress(0, 0, monthStartColIndex, monthEndColIndex));
				}
				monthEndColIndex++;
				dataEntryRowCount++;
			}
			
			employeeDataRowIndex++;
		}
		
		return createExcelWorkbook(PATH, String.format(FILENAME, LocalDateTime.now().toLocalDate()));
	}
	
	HSSFCell populateCell(HSSFCell cell, HSSFCellStyle cellStyle, String cellValue){
		cell.setCellValue(cellValue);
		cell.setCellStyle(cellStyle);
		return cell;
	}
	
	void createDirectoryPath(String directoryPath) {
		File directory = new File(directoryPath);
		if (!directory.exists()) {
			directory.mkdirs();
		}
	}

	File createExcelWorkbook(String directoryPath, String fileName) throws IOException {
		createDirectoryPath(directoryPath);
		File file = new File(directoryPath, fileName);
		workbook.write(file);
		workbook.close();
		return file;
	}

	String setMonthHeaders(UtilizationJson utilJson) throws IllegalArgumentException {
		int monthIndex = utilJson.getMonth();
		if (!isValidMonthIndex(monthIndex)) {
			throw new IllegalArgumentException("Invalid month value from JSON");
		}
		Month month = Month.of(monthIndex);
		return month.getDisplayName(TextStyle.SHORT, Locale.getDefault()).toUpperCase();
	}

	/**
	 * Previous code referenced indexes were Sun - Sat, Java 8 enum starts from
	 * Mon-Sun
	 * 
	 * @param cellStyle
	 * @param utilJson
	 * @param dayHeader
	 * @throws OpumException
	 */
	String setDayHeaders(UtilizationJson utilJson) throws IllegalArgumentException {
		int dayIndex = utilJson.getDay();
		if (!isValidDayIndex(dayIndex)) {
			throw new IllegalArgumentException("Invalid day value from JSON");
		}
		DayOfWeek day = DayOfWeek.of(dayIndex);
		return day.getDisplayName(TextStyle.NARROW, Locale.getDefault());
	}

	private boolean isValidDayIndex(int dayIndex) {
		return 1 <= dayIndex && dayIndex <= 7;
	}

	private boolean isValidMonthIndex(int monthIndex) {
		return 1 <= monthIndex && monthIndex <= 12;
	}

	public UtilizationEngagementRepository getUtilEngagementImpl() {
		return utilEngagementImpl;
	}

	public void setUtilEngagementImpl(UtilizationEngagementRepository utilEngagementImpl) {
		this.utilEngagementImpl = utilEngagementImpl;
	}

	public ProjectEngagementRepository getProjEngagementImpl() {
		return projEngagementImpl;
	}

	public void setProjEngagementImpl(ProjectEngagementRepository projEngagementImpl) {
		this.projEngagementImpl = projEngagementImpl;
	}

	public ProjectRepository getProjImpl() {
		return projImpl;
	}

	public void setProjImpl(ProjectRepository projImpl) {
		this.projImpl = projImpl;
	}

	public Font getDataFont() {
		return dataFont;
	}

	public void setDataFont(Font dataFont) {
		this.dataFont = dataFont;
	}

	public Font getDataHeaderFont() {
		return dataHeaderFont;
	}

	public void setDataHeaderFont(Font dataHeaderFont) {
		this.dataHeaderFont = dataHeaderFont;
	}

	public Font getMonthHeaderFont() {
		return monthHeaderFont;
	}

	public void setMonthHeaderFont(Font monthHeaderFont) {
		this.monthHeaderFont = monthHeaderFont;
	}

	public HSSFCellStyle getDataStyle() {
		return dataStyle;
	}

	public void setDataStyle(HSSFCellStyle dataStyle) {
		this.dataStyle = dataStyle;
	}

	public HSSFWorkbook getWorkbook() {
		return workbook;
	}

	public void setWorkbook(HSSFWorkbook workbook) {
		this.workbook = workbook;
	}

	public HSSFCellStyle getDataHeaderStyle() {
		return headerStyle;
	}

	public void setDataHeaderStyle(HSSFCellStyle dataHeaderStyle) {
		this.headerStyle = dataHeaderStyle;
	}

	public HSSFCellStyle getMonthHeaderStyle() {
		return monthHeaderStyle;
	}

	public void setMonthHeaderStyle(HSSFCellStyle monthHeaderStyle) {
		this.monthHeaderStyle = monthHeaderStyle;
	}
	
	public HSSFSheet getSheet() {
		return sheet;
	}

	public void setSheet(HSSFSheet sheet) {
		this.sheet = sheet;
	}
	
}
