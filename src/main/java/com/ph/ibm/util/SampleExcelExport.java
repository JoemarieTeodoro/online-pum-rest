package com.ph.ibm.util;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

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
import com.ph.ibm.repository.impl.ProjectEngagementRepositoryImpl;
import com.ph.ibm.repository.impl.ProjectRepositoryImpl;
import com.ph.ibm.repository.impl.UtilizationEngagementRepositoryImpl;

public class SampleExcelExport {

	
	
	public void workbook() throws IOException {
		UtilizationEngagementRepositoryImpl util = new UtilizationEngagementRepositoryImpl();
		ProjectEngagementRepositoryImpl projectEngagementImplementation = new ProjectEngagementRepositoryImpl();
		ProjectRepositoryImpl projectImplementation = new ProjectRepositoryImpl();
		JSONToJava jsontojava = new JSONToJava();
		LocalDateTime now = LocalDateTime.now();
		
		try {
			Utilization utilization = null;
			List<ProjectEngagement> projectEngagementList = projectEngagementImplementation.getAllProjectEngagement();
			List<Project> projectList	= projectImplementation.retrieveData();
			HSSFWorkbook workbook = new HSSFWorkbook();
			HSSFSheet sheet = workbook.createSheet("PUM 2017");
			sheet.addMergedRegion(new CellRangeAddress(0,3,0,4));
			
			
			HSSFCreationHelper createHelper = workbook.getCreationHelper();
			HSSFCellStyle dateStyle = workbook.createCellStyle();
			dateStyle.setDataFormat(createHelper.createDataFormat().getFormat("MM-dd-yyyy"));
			Font dateFont = workbook.createFont();
			dateFont.setFontHeightInPoints((short)(9));
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
			
			HSSFCellStyle hStyle = workbook.createCellStyle();
			Font font = workbook.createFont();
			font.setFontHeightInPoints((short)(9));
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
			
			
			HSSFCellStyle monthStyle = workbook.createCellStyle();			
			Font monthFont = workbook.createFont();
			monthFont.setFontHeightInPoints((short)(12));
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
			
			HSSFCellStyle dataStyle = workbook.createCellStyle();
			Font dataFont = workbook.createFont();
			dataFont.setFontHeightInPoints((short)(9));
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
		    
		    
		  
			for (int i = 0; i<projectEngagementList.size(); i++) {
				
				Utilization excelRow = util.downloadUtilization("2017", projectEngagementList.get(i).getEmployeeId());
				HSSFRow row = sheet.createRow(rowNum);

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
				for(UtilizationJson utilJson: utilizationYear.getUtilizationJSON())
				{       
					HSSFCell cellHours = row.createCell(utilColumn++);
					cellHours.setCellValue(utilJson.getUtilizationHours());
					cellHours.setCellStyle(dataStyle);
					
					HSSFCell dayOfMonth = daysHeader.createCell(daysUtil++);
					dayOfMonth.setCellValue(utilJson.getDayOfMonth());
					dayOfMonth.setCellStyle(hStyle);
					
					HSSFCell dayHeader = header.createCell(colHeader++);
					if(utilJson.getDay() == 1){
						dayHeader.setCellValue("S");
						dayHeader.setCellStyle(hStyle);
					}else if(utilJson.getDay() == 2){
						dayHeader.setCellValue("M");
						dayHeader.setCellStyle(hStyle);
					}else if(utilJson.getDay() == 3){
						dayHeader.setCellValue("T");
						dayHeader.setCellStyle(hStyle);
					}else if(utilJson.getDay() == 4){
						dayHeader.setCellValue("W");
						dayHeader.setCellStyle(hStyle);
					}else if(utilJson.getDay() == 5){
						dayHeader.setCellValue("T");
						dayHeader.setCellStyle(hStyle);
					}else if(utilJson.getDay() == 6){
						dayHeader.setCellValue("F");
						dayHeader.setCellStyle(hStyle);
					}else if(utilJson.getDay() == 7){
						dayHeader.setCellValue("S");
						dayHeader.setCellStyle(hStyle);
					}else{
					dayHeader.setCellValue(utilJson.getDay());
					}
					
					HSSFCell month = monthHeader.createCell(monthHeaderCtr++);
					if(utilJson.getMonth()==1){
						month.setCellValue("JAN");
						month.setCellStyle(monthStyle);
					}else if(utilJson.getMonth()==2){
						month.setCellValue("FEB");
						month.setCellStyle(monthStyle);
					}else if(utilJson.getMonth()==3){
						month.setCellValue("MAR");
						month.setCellStyle(monthStyle);
					}else if(utilJson.getMonth()==4){
						month.setCellValue("APR");
						month.setCellStyle(monthStyle);
					}else if(utilJson.getMonth()==5){
						month.setCellValue("MAY");
						month.setCellStyle(monthStyle);
					}else if(utilJson.getMonth()==6){
						month.setCellValue("JUN");
						month.setCellStyle(monthStyle);
					}else if(utilJson.getMonth()==7){
						month.setCellValue("JUL");
						month.setCellStyle(monthStyle);
					}else if(utilJson.getMonth()==8){
						month.setCellValue("AUG");
						month.setCellStyle(monthStyle);
					}else if(utilJson.getMonth()==9){
						month.setCellValue("SEP");
						month.setCellStyle(monthStyle);
					}else if(utilJson.getMonth()==10){
						month.setCellValue("OCT");
						month.setCellStyle(monthStyle);
					}else if(utilJson.getMonth()==11){
						month.setCellValue("NOV");
						month.setCellStyle(monthStyle);
					}else if(utilJson.getMonth()==12){
						month.setCellValue("DEC");
						month.setCellStyle(monthStyle);
					}else{
					month.setCellValue(utilJson.getMonth());
					}
					
					if(ctr < utilizationYear.getUtilizationJSON().size() && i==0){
					if(utilJson.getMonth() != utilizationYear.getUtilizationJSON().get(ctr).getMonth()){
						
						sheet.addMergedRegion(new CellRangeAddress(0,0,monthStartCtr,monthEndCtr));
						monthStartCtr = monthEndCtr + 1;
						}
					}else if(ctr == utilizationYear.getUtilizationJSON().size() && i==0){
						sheet.addMergedRegion(new CellRangeAddress(0,0,monthStartCtr,monthEndCtr));
					}
					monthEndCtr++;
					ctr++;
					}
				
				rowNum++;
			}
			
			System.out.println("Test 2");
			try  {
				File file = new File("C:\\Users\\IBM_ADMIN\\Desktop\\"+"USAA_"+ "PUM" + "_as of "+ now.now().toLocalDate() +".xls");
				workbook.write(file);
				workbook.close();
				
				ResponseBuilder response = Response.ok((workbook));
				
			} finally {
				
			}
			System.out.println("Test 3");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws IOException {
		new SampleExcelExport().workbook();
	}

}
