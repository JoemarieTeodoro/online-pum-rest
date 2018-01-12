package com.ph.ibm.bo;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFCreationHelper;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellRangeAddress;

import com.ph.ibm.model.Project;
import com.ph.ibm.model.ProjectEngagement;
import com.ph.ibm.model.Utilization;
import com.ph.ibm.model.UtilizationJson;
import com.ph.ibm.model.UtilizationYear;
import com.ph.ibm.model.Year;
import com.ph.ibm.repository.UtilizationEngagementRepository;
import com.ph.ibm.repository.impl.ProjectEngagementRepositoryImpl;
import com.ph.ibm.repository.impl.ProjectRepositoryImpl;
import com.ph.ibm.repository.impl.UtilizationEngagementRepositoryImpl;
import com.ph.ibm.util.JSONToJava;
import com.ph.ibm.util.ObjectMapperAdapter;

public class UtilityBO {

	/**
	 * 
	 */
	private UtilizationEngagementRepository utilizationEngagementRepository = new UtilizationEngagementRepositoryImpl();

	
	/**
	 * @throws IOException
	 */
	public Response downloadUtilization(String year) throws IOException {
		UtilizationEngagementRepositoryImpl util = new UtilizationEngagementRepositoryImpl();
		ProjectEngagementRepositoryImpl projectEngagementImplementation = new ProjectEngagementRepositoryImpl();
		ProjectRepositoryImpl projectImplementation = new ProjectRepositoryImpl();
		JSONToJava jsontojava = new JSONToJava();
		LocalDateTime now = LocalDateTime.now();
		ProjectBO projectBO = new ProjectBO();
		UtilityBO utilityBO = new UtilityBO();
		HSSFWorkbook workbook = new HSSFWorkbook();
		System.out.println("Year: " + year);
		try {
			Utilization utilization = null;
			List<ProjectEngagement> projectEngagementList = projectEngagementImplementation.getAllProjectEngagement();
			List<Project> projectList = projectImplementation.retrieveData();

			HSSFSheet sheet = workbook.createSheet("PUM 2017");
			sheet.addMergedRegion(new CellRangeAddress(0, 3, 0, 4));

			HSSFCellStyle dateStyle = createStylingForRollinRolloffDate(workbook);

			HSSFCellStyle hStyle = createHeaderStyling(workbook);

			HSSFCellStyle compStyle = createYTDStyle(workbook);

			HSSFCellStyle monthStyle = createMonthHeaderStyling(workbook);

			HSSFCellStyle dataStyle = createDataStyling(workbook);

			int rowNum = 5;
			HSSFRow daysHeader = sheet.createRow(3);
			HSSFRow monthHeader = sheet.createRow(0);
			HSSFRow header = sheet.createRow(4);

			HSSFCell header1 = header.createCell(0);
			HSSFCell header2 = header.createCell(1);
			HSSFCell header3 = header.createCell(2);
			HSSFCell header4 = header.createCell(3);
			HSSFCell header5 = header.createCell(4);

			header1.setCellValue("Project");
			header1.setCellStyle(hStyle);
			sheet.autoSizeColumn(0);
			header2.setCellValue("Employee Serial No.");
			header2.setCellStyle(hStyle);
			sheet.autoSizeColumn(1);

			header3.setCellValue("Year");
			header3.setCellStyle(hStyle);
			sheet.autoSizeColumn(2);
			header4.setCellValue("Roll In Date");
			header4.setCellStyle(hStyle);
			sheet.autoSizeColumn(3);
			header5.setCellValue("Roll Off Date");
			header5.setCellStyle(hStyle);
			sheet.autoSizeColumn(4);

			for (int i = 0; i < projectEngagementList.size(); i++) {

				Utilization excelRow = util.downloadUtilization("2017", projectEngagementList.get(i).getEmployeeId());
				HSSFRow row = sheet.createRow(rowNum);
				// Year yearComputation =
				// projectBO.getComputation(projectEngagementList.get(i).getEmployeeId(),
				// Integer.parseInt(excelRow.getYear()));
				Year yearComputation = utilityBO.getYTDComputation(projectEngagementList.get(i).getEmployeeId(),
						Integer.parseInt(excelRow.getYear()));
				HSSFCell cell1 = row.createCell(0);
				HSSFCell cell2 = row.createCell(1);
				HSSFCell cell3 = row.createCell(2);
				HSSFCell cell4 = row.createCell(3);
				HSSFCell cell5 = row.createCell(4);

				cell1.setCellValue("USAA");
				cell1.setCellStyle(dataStyle);
				cell2.setCellValue(excelRow.getEmployeeSerial());
				cell2.setCellStyle(dataStyle);
				cell3.setCellValue(excelRow.getYear());
				cell3.setCellStyle(dataStyle);

				cell4.setCellValue(projectEngagementList.get(i).getStartDate());
				cell4.setCellStyle(dateStyle);
				sheet.autoSizeColumn(3);
				cell5.setCellValue(projectEngagementList.get(i).getEndDate());
				cell5.setCellStyle(dateStyle);
				sheet.autoSizeColumn(4);

				UtilizationYear utilizationYear = jsontojava.jsonToJava(excelRow.getUtilizationJson());

				int utilColumn = 5;
				int colHeader = 5;
				int monthHeaderCtr = 5;
				int daysUtil = 5;
				int ctr = 1;
				int monthStartCtr = 5;
				int monthEndCtr = 5;
				for (UtilizationJson utilJson : utilizationYear.getUtilizationJSON()) {
					HSSFCell cellHours = row.createCell(utilColumn++);
					cellHours.setCellValue(utilJson.getUtilizationHours());
					cellHours.setCellStyle(dataStyle);

					HSSFCell dayOfMonth = daysHeader.createCell(daysUtil++);
					dayOfMonth.setCellValue(utilJson.getDayOfMonth());
					dayOfMonth.setCellStyle(hStyle);

					HSSFCell dayHeader = header.createCell(colHeader++);
					if (utilJson.getDay() == 1) {
						dayHeader.setCellValue("S"); // Change to Sunday
						dayHeader.setCellStyle(hStyle);
					} else if (utilJson.getDay() == 2) {
						dayHeader.setCellValue("M");
						dayHeader.setCellStyle(hStyle);
					} else if (utilJson.getDay() == 3) {
						dayHeader.setCellValue("T");
						dayHeader.setCellStyle(hStyle);
					} else if (utilJson.getDay() == 4) {
						dayHeader.setCellValue("W");
						dayHeader.setCellStyle(hStyle);
					} else if (utilJson.getDay() == 5) {
						dayHeader.setCellValue("T");// Change to Thursday
						dayHeader.setCellStyle(hStyle);
					} else if (utilJson.getDay() == 6) {
						dayHeader.setCellValue("F");
						dayHeader.setCellStyle(hStyle);
					} else if (utilJson.getDay() == 7) {
						dayHeader.setCellValue("S"); // Change to Saturday
						dayHeader.setCellStyle(hStyle);
					} else {
						dayHeader.setCellValue(utilJson.getDay());
					}

					HSSFCell month = monthHeader.createCell(monthHeaderCtr++);
					if (utilJson.getMonth() == 1) {
						month.setCellValue("JAN");
						month.setCellStyle(monthStyle);
					} else if (utilJson.getMonth() == 2) {
						month.setCellValue("FEB");
						month.setCellStyle(monthStyle);
					} else if (utilJson.getMonth() == 3) {
						month.setCellValue("MAR");
						month.setCellStyle(monthStyle);
					} else if (utilJson.getMonth() == 4) {
						month.setCellValue("APR");
						month.setCellStyle(monthStyle);
					} else if (utilJson.getMonth() == 5) {
						month.setCellValue("MAY");
						month.setCellStyle(monthStyle);
					} else if (utilJson.getMonth() == 6) {
						month.setCellValue("JUN");
						month.setCellStyle(monthStyle);
					} else if (utilJson.getMonth() == 7) {
						month.setCellValue("JUL");
						month.setCellStyle(monthStyle);
					} else if (utilJson.getMonth() == 8) {
						month.setCellValue("AUG");
						month.setCellStyle(monthStyle);
					} else if (utilJson.getMonth() == 9) {
						month.setCellValue("SEP");
						month.setCellStyle(monthStyle);
					} else if (utilJson.getMonth() == 10) {
						month.setCellValue("OCT");
						month.setCellStyle(monthStyle);
					} else if (utilJson.getMonth() == 11) {
						month.setCellValue("NOV");
						month.setCellStyle(monthStyle);
					} else if (utilJson.getMonth() == 12) {
						month.setCellValue("DEC");
						month.setCellStyle(monthStyle);
					} else {
						month.setCellValue(utilJson.getMonth());
					}

					if (ctr < utilizationYear.getUtilizationJSON().size() && i == 0) {
						if (utilJson.getMonth() != utilizationYear.getUtilizationJSON().get(ctr).getMonth()) {

							sheet.addMergedRegion(new CellRangeAddress(0, 0, monthStartCtr, monthEndCtr));
							monthStartCtr = monthEndCtr + 1;
						}
					} else if (ctr == utilizationYear.getUtilizationJSON().size() && i == 0) {
						sheet.addMergedRegion(new CellRangeAddress(0, 0, monthStartCtr, monthEndCtr));
					}
					monthEndCtr++;
					ctr++;
				}

				sheet.autoSizeColumn(colHeader);
				HSSFCell cellNumOfAvailHrsHdr = header.createCell(colHeader++);
				cellNumOfAvailHrsHdr.setCellValue("Available Hours");
				cellNumOfAvailHrsHdr.setCellStyle(compStyle);

				sheet.autoSizeColumn(colHeader);
				HSSFCell cellNumOfCDOHdr = header.createCell(colHeader++);
				cellNumOfCDOHdr.setCellValue(TimeAwayTokens.CDO.getS());
				cellNumOfCDOHdr.setCellStyle(compStyle);

				sheet.autoSizeColumn(colHeader);
				HSSFCell cellNumOfELHdr = header.createCell(colHeader++);
				cellNumOfELHdr.setCellValue("EL");
				cellNumOfELHdr.setCellStyle(compStyle);

				sheet.autoSizeColumn(colHeader);
				HSSFCell cellNumOfHOHdr = header.createCell(colHeader++);
				cellNumOfHOHdr.setCellValue("HO");
				cellNumOfHOHdr.setCellStyle(compStyle);

				sheet.autoSizeColumn(colHeader);
				HSSFCell cellNumOfOLHdr = header.createCell(colHeader++);
				cellNumOfOLHdr.setCellValue("OL");
				cellNumOfOLHdr.setCellStyle(compStyle);

				sheet.autoSizeColumn(colHeader);
				HSSFCell cellNumOfSLHdr = header.createCell(colHeader++);
				cellNumOfSLHdr.setCellValue("SL");
				cellNumOfSLHdr.setCellStyle(compStyle);

				sheet.autoSizeColumn(colHeader);
				HSSFCell cellNumOfTRHdr = header.createCell(colHeader++);
				cellNumOfTRHdr.setCellValue("TR");
				cellNumOfTRHdr.setCellStyle(compStyle);

				sheet.autoSizeColumn(colHeader);
				HSSFCell cellNumOfVLHdr = header.createCell(colHeader++);
				cellNumOfVLHdr.setCellValue("VL");
				cellNumOfVLHdr.setCellStyle(compStyle);

				sheet.autoSizeColumn(colHeader);
				HSSFCell cellTotalHrsHdr = header.createCell(colHeader++);
				cellTotalHrsHdr.setCellValue("Total Hours");
				cellTotalHrsHdr.setCellStyle(compStyle);

				sheet.autoSizeColumn(colHeader);
				HSSFCell cellYrToDateUtilHdr = header.createCell(colHeader++);
				cellYrToDateUtilHdr.setCellValue("YTD%");
				cellYrToDateUtilHdr.setCellStyle(compStyle);

				HSSFCell cellNumOfAvailHrs = row.createCell(daysUtil++);
				HSSFCell cellNumOfCDO = row.createCell(daysUtil++);
				HSSFCell cellNumOfEL = row.createCell(daysUtil++);
				HSSFCell cellNumOfHO = row.createCell(daysUtil++);
				HSSFCell cellNumOfOL = row.createCell(daysUtil++);
				HSSFCell cellNumOfSL = row.createCell(daysUtil++);
				HSSFCell cellNumOfTR = row.createCell(daysUtil++);
				HSSFCell cellNumOfVL = row.createCell(daysUtil++);
				HSSFCell cellTotalHrs = row.createCell(daysUtil++);
				HSSFCell cellYrToDateUtil = row.createCell(daysUtil++);

				cellNumOfAvailHrs.setCellValue(yearComputation.getNumberOfAvailableHours());
				cellNumOfAvailHrs.setCellStyle(dataStyle);

				cellNumOfCDO.setCellValue(yearComputation.getNumberOfCDO());
				cellNumOfCDO.setCellStyle(dataStyle);

				cellNumOfEL.setCellValue(yearComputation.getNumberOfEL());
				cellNumOfEL.setCellStyle(dataStyle);

				cellNumOfHO.setCellValue(yearComputation.getNumberOfHO());
				cellNumOfHO.setCellStyle(dataStyle);

				cellNumOfOL.setCellValue(yearComputation.getNumberOfOL());
				cellNumOfOL.setCellStyle(dataStyle);

				cellNumOfSL.setCellValue(yearComputation.getNumberOfSL());
				cellNumOfSL.setCellStyle(dataStyle);

				cellNumOfTR.setCellValue(yearComputation.getNumberOfTR());
				cellNumOfTR.setCellStyle(dataStyle);

				cellNumOfVL.setCellValue(yearComputation.getNumberOfVL());
				cellNumOfVL.setCellStyle(dataStyle);

				cellTotalHrs.setCellValue(yearComputation.getTotalHours());
				cellTotalHrs.setCellStyle(dataStyle);

				cellYrToDateUtil.setCellValue(yearComputation.getYearToDateUtilization());
				cellYrToDateUtil.setCellStyle(dataStyle);

				rowNum++;
			}
			try {
				File file = new File(
						"C:\\Users\\IBM_ADMIN\\Desktop\\USAA_PUM_as_of" + now.now().toLocalDate() + ".xls");// CHANGE
																											// PATH TO
																											// DYNAMIC
																											// BASED ON
																											// ENVIRONEMTN
				workbook.write(file);
				workbook.close();
				ResponseBuilder response = Response.ok(file);
				response.header("Content-Disposition",
						"attachement; filename=USAA_PUM_as_of" + now.now().toLocalDate() + ".xls");
				return response.build();
			} catch (IOException io) {
				io.printStackTrace();
				return Response.status(Status.BAD_REQUEST).entity(workbook).type(MediaType.APPLICATION_OCTET_STREAM)
						.build();
			} finally {
				workbook.close();
			}
		} catch (Exception e) { // Change to specific exception
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).entity(workbook).type(MediaType.APPLICATION_OCTET_STREAM)
					.build();
		}
	}
	

	public Response downloadUtilizationReport(String periodKey, int periodValue, String filePath) throws Exception {
		
		UtilizationEngagementRepositoryImpl util = new UtilizationEngagementRepositoryImpl();
		return util.downloadUtilizationReport(periodKey, periodValue, filePath);

	}
	
	private HSSFCellStyle createDataStyling(HSSFWorkbook workbook) {
		HSSFCellStyle dataStyle = workbook.createCellStyle();
		Font dataFont = workbook.createFont();
		dataFont.setFontHeightInPoints((short) (9));
		dataFont.setFontName("Calibri");
		dataFont.setColor(IndexedColors.BLACK.getIndex());
		dataFont.setBold(false);
		dataFont.setItalic(false);
		dataStyle.setAlignment(HorizontalAlignment.CENTER);
		dataStyle.setBorderLeft(BorderStyle.THIN);
		dataStyle.setBorderBottom(BorderStyle.THIN);
		dataStyle.setBorderRight(BorderStyle.THIN);
		dataStyle.setBorderTop(BorderStyle.THIN);
		dataStyle.setFont(dataFont);
		return dataStyle;
	}

	private HSSFCellStyle createMonthHeaderStyling(HSSFWorkbook workbook) {
		HSSFCellStyle monthStyle = workbook.createCellStyle();
		Font monthFont = workbook.createFont();
		monthFont.setFontHeightInPoints((short) (12));
		monthFont.setFontName("Trebuchet MS");
		monthFont.setColor(IndexedColors.WHITE.getIndex());
		monthFont.setBold(true);
		monthFont.setItalic(false);
		monthStyle.setAlignment(HorizontalAlignment.CENTER);
		monthStyle.setFillForegroundColor(IndexedColors.GREEN.getIndex());
		monthStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		monthStyle.setBorderLeft(BorderStyle.THIN);
		monthStyle.setBorderBottom(BorderStyle.THIN);
		monthStyle.setBorderRight(BorderStyle.THIN);
		monthStyle.setBorderTop(BorderStyle.THIN);
		monthStyle.setFont(monthFont);
		return monthStyle;
	}

	private HSSFCellStyle createYTDStyle(HSSFWorkbook workbook) {
		HSSFCellStyle compStyle = workbook.createCellStyle();
		Font comFont = workbook.createFont();
		comFont.setFontHeightInPoints((short) (9));
		comFont.setFontName("Verdana");
		comFont.setColor(IndexedColors.DARK_TEAL.getIndex());
		comFont.setBold(true);
		comFont.setItalic(false);
		compStyle.setAlignment(HorizontalAlignment.CENTER);
		compStyle.setFillForegroundColor(IndexedColors.LAVENDER.getIndex());
		compStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		compStyle.setBorderLeft(BorderStyle.THIN);
		compStyle.setBorderBottom(BorderStyle.THIN);
		compStyle.setBorderRight(BorderStyle.THIN);
		compStyle.setBorderTop(BorderStyle.THIN);
		compStyle.setFont(comFont);
		return compStyle;
	}

	private HSSFCellStyle createHeaderStyling(HSSFWorkbook workbook) {
		HSSFCellStyle hStyle = workbook.createCellStyle();
		Font font = workbook.createFont();
		font.setFontHeightInPoints((short) (9));
		font.setFontName("Verdana");
		font.setColor(IndexedColors.DARK_TEAL.getIndex());
		font.setBold(true);
		font.setItalic(false);
		hStyle.setAlignment(HorizontalAlignment.CENTER);
		hStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
		hStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		hStyle.setBorderLeft(BorderStyle.THIN);
		hStyle.setBorderBottom(BorderStyle.THIN);
		hStyle.setBorderRight(BorderStyle.THIN);
		hStyle.setBorderTop(BorderStyle.THIN);
		hStyle.setFont(font);
		return hStyle;
	}

	private HSSFCellStyle createStylingForRollinRolloffDate(HSSFWorkbook workbook) {
		HSSFCreationHelper createHelper = workbook.getCreationHelper();
		HSSFCellStyle dateStyle = workbook.createCellStyle();
		dateStyle.setDataFormat(createHelper.createDataFormat().getFormat("MM-dd-yyyy"));
		Font dateFont = workbook.createFont();
		dateFont.setFontHeightInPoints((short) (9));
		dateFont.setFontName("Calibri");
		dateFont.setColor(IndexedColors.BLACK.getIndex());
		dateFont.setBold(false);
		dateFont.setItalic(false);
		dateStyle.setAlignment(HorizontalAlignment.CENTER);
		dateStyle.setBorderLeft(BorderStyle.THIN);
		dateStyle.setBorderBottom(BorderStyle.THIN);
		dateStyle.setBorderRight(BorderStyle.THIN);
		dateStyle.setBorderTop(BorderStyle.THIN);
		dateStyle.setFont(dateFont);
		return dateStyle;
	}

	/**
	 * This method is used to save utilization
	 * 
	 * @param utilization
	 * @return boolean
	 * @throws SQLException
	 */
	public boolean saveUtilization(Utilization utilization) throws SQLException {
		return utilizationEngagementRepository.saveUtilization(utilization);
	}

	/**
	 * This method is used to get utilization from utilization table
	 * 
	 * @param employeeIdNumber
	 * @param year
	 * @return String
	 * @throws SQLException
	 */
	public String fetchUtilizations(String employeeIdNumber, String year) throws SQLException {
		List<Utilization> utilizations = new ArrayList<Utilization>();
		utilizations = utilizationEngagementRepository.retrieveUtilizations(employeeIdNumber, year);
		if (utilizations.size() > 0) {
			UtilizationYear utilizationYear = ObjectMapperAdapter.unmarshal(utilizations.get(0).getUtilizationJson(),
					UtilizationYear.class);

			LocalDateTime now = LocalDateTime.now();
			int currentYear = now.getYear();
			int currentMonth = now.getMonthValue();
			int currentDay = now.getDayOfMonth();
			int counter = 0;
			for (UtilizationJson json : utilizationYear.getUtilizationJSON()) {
				if (utilizationYear.getYear() < currentYear) {
					json.setEditable("D");
					counter++;
				} else if (utilizationYear.getYear() == currentYear) {
					// Reduce indentation
					if (json.getMonth() < currentMonth) {
						json.setEditable("D");
						counter++;
					} else if (json.getMonth() == currentMonth) {
						if (json.getDayOfMonth() < currentDay) {
							json.setEditable("D");
							counter++;
						} else {
							json.setEditable("E");
							counter++;
						}
					} else {
						json.setEditable("E");
						counter++;
					}
				} else {
					json.setEditable("E");
					counter++;
				}
			}
			String finalJson = ObjectMapperAdapter.marshal(utilizationYear);
			System.out.println(finalJson);
			return finalJson;
		} else {
			return null;
		}
	}

	/**
	 * 
	 * 
	 * @param
	 * @param year
	 * @return Response
	 * @throws SQLException
	 * @throws ParseException
	 */
	public Year getYTDComputation(String employeeSerial, int year) throws SQLException, ParseException {
		Utilization utilization = utilizationEngagementRepository.getComputation(employeeSerial, year);
		UtilizationYear utilization_Year = ObjectMapperAdapter.unmarshal(utilization.getUtilizationJson(),
				UtilizationYear.class);
		Year ytdComputation = new Year();
		double hours = 0; // TODO: change to big decimal
		double totalActualHours = 0;
		double availableHours = 0;
		double VLcount = 0;
		double SLcount = 0;
		double ELcount = 0;
		double OLcount = 0;
		double HOcount = 0;
		double TRcount = 0;
		double CDOcount = 0;
		double availableHoursCounter = 0;
		double YTD = 0;
		DecimalFormat formatter = new DecimalFormat("#0.00");
		// String ytdResult = formatter.format(YTD);

		// ytdBD.

		for (UtilizationJson json : utilization_Year.getUtilizationJSON()) {
			if (json.getUtilizationHours().equalsIgnoreCase("VL") || json.getUtilizationHours().equals("SL")
					|| json.getUtilizationHours().equals("OL") || json.getUtilizationHours().equals("EL")
					|| json.getUtilizationHours().equals("HO") || json.getUtilizationHours().equals("TR")
					|| json.getUtilizationHours().equals("CDO") || json.getUtilizationHours().equals("")) {

				hours = 0;
			} else if (json.getUtilizationHours() != null) {
				hours = Integer.parseInt(json.getUtilizationHours());

			}
			if (json.getUtilizationHours().equals("VL")) {
				VLcount++;
			}
			if (json.getUtilizationHours().equals("SL")) {
				SLcount++;
			}
			if (json.getUtilizationHours().equals("OL")) {
				OLcount++;
			}
			if (json.getUtilizationHours().equals("EL")) {
				ELcount++;
			}
			if (json.getUtilizationHours().equals("CDO")) {
				CDOcount++;
			}
			if (json.getUtilizationHours().equals("TR")) {
				TRcount++;
			}
			if (json.getUtilizationHours().equals("HO")) {
				HOcount++;
			}
			if (json.getDay() != 6 && json.getDay() != 1) {
				availableHoursCounter++;
			}

			if (json.getMonth() <= 12) {
				totalActualHours = totalActualHours + hours;
			}
			availableHours = availableHoursCounter * 8;
			YTD = (totalActualHours / availableHours) * 100;

		}

		ytdComputation.setTotalHours(totalActualHours);
		ytdComputation.setNumberOfVL(VLcount);
		ytdComputation.setNumberOfSL(SLcount);
		ytdComputation.setNumberOfEL(ELcount);
		ytdComputation.setNumberOfOL(OLcount);
		ytdComputation.setNumberOfTR(TRcount);
		ytdComputation.setNumberOfHO(HOcount);
		ytdComputation.setNumberOfCDO(CDOcount);
		ytdComputation.setNumberOfAvailableHours(availableHours);
		ytdComputation.setYearToDateUtilization(Double.parseDouble(formatter.format(YTD)));

		return ytdComputation;
	}

}
