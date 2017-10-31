package com.ph.ibm.util;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.junit.Before;
import org.junit.Test;

import com.ph.ibm.model.Project;
import com.ph.ibm.model.ProjectEngagement;
import com.ph.ibm.model.Utilization;
import com.ph.ibm.opum.exception.OpumException;
import com.ph.ibm.repository.ProjectEngagementRepository;
import com.ph.ibm.repository.ProjectRepository;
import com.ph.ibm.repository.UtilizationEngagementRepository;

public class SampleExcelExportTest {
	
	private static final String UTIL_YEAR = "2017";
	
	private static final short SMALL_FONT_SIZE = (short) 9;
	private static final short MEDIUM_FONT_SIZE = (short) (12);
	
	HSSFCellStyle dataStyle;
	HSSFCellStyle dataHeaderStyle;
	HSSFCellStyle monthHeaderStyle;
	
	HSSFWorkbook workbook;
	
	
	// Mocks
	private UtilizationEngagementRepository mockUtilImpl = mock(UtilizationEngagementRepository.class);
	private ProjectEngagementRepository mockProjEngagementImpl = mock(ProjectEngagementRepository.class);
	private ProjectRepository mockProjImpl = mock(ProjectRepository.class);
	private SampleExcelExport excelExport;
	
	
	private List<ProjectEngagement> mockProjectEngagements;
	private List<Project> mockProjects;
	
	@Before
	public void setup() throws SQLException {
		workbook = new HSSFWorkbook();
		
		excelExport = spy(new SampleExcelExport());
		excelExport.setProjEngagementImpl(mockProjEngagementImpl);
		excelExport.setUtilEngagementImpl(mockUtilImpl);
		excelExport.setProjImpl(mockProjImpl);
		
		excelExport.setWorkbook(workbook);
		excelExport.setSheet(workbook.createSheet());
		
		Font dataFont = createFont("Calibri", SMALL_FONT_SIZE, IndexedColors.BLACK, false);
		HSSFCellStyle dataStyle = createCellStyle(dataFont);

		Font dataHeaderFont = createFont("Verdana", SMALL_FONT_SIZE, IndexedColors.DARK_TEAL, true);
		HSSFCellStyle dataHeaderStyle = createCellStyle(dataHeaderFont, IndexedColors.LIGHT_YELLOW, FillPatternType.SOLID_FOREGROUND);

		Font monthHeaderFont = createFont("Trebuchet MS", MEDIUM_FONT_SIZE, IndexedColors.WHITE, true);
		HSSFCellStyle monthHeaderStyle = createCellStyle(monthHeaderFont, IndexedColors.GREEN, FillPatternType.SOLID_FOREGROUND);
		
		excelExport.setDataFont(dataFont);
		excelExport.setDataStyle(dataStyle);
		
		excelExport.setDataHeaderFont(dataHeaderFont);
		excelExport.setDataHeaderStyle(dataHeaderStyle);
		
		excelExport.setMonthHeaderFont(monthHeaderFont);
		excelExport.setMonthHeaderStyle(monthHeaderStyle);
		
		mockProjectEngagements = new ArrayList<>();
		mockProjects = new ArrayList<>();
		
		Project stubProject = new Project();
		stubProject.setAccountId(1L);
		stubProject.setProjectId(1L);
		stubProject.setProjectName("USAA");

		ProjectEngagement stubEngagement = new ProjectEngagement();
		stubEngagement.setEmployeeId("147866PH1");
		stubEngagement.setProjectEngagementId(1L);
		stubEngagement.setProjectId(1L);
		stubEngagement.setStartDate(Date.valueOf(LocalDate.of(2017, 1, 17)));
		stubEngagement.setEndDate(Date.valueOf(LocalDate.of(2018, 1, 17)));

		mockProjectEngagements.add(stubEngagement);
		mockProjects.add(stubProject);

		when(mockProjImpl.retrieveData()).thenReturn(mockProjects);
		when(mockProjEngagementImpl.getAllProjectEngagement()).thenReturn(mockProjectEngagements);
	}

	@Test
	public void workbookIsSuccessfullyCreated() throws IOException, SQLException, OpumException {
		String utilizationJsonString = "" 
				+ "{" 
				+ " \"year\": 2017," 
				+ " \"utilizationJSON\": " 
				+ "		["
				+ "		  {\"month\": 8,\"day\": 1,\"dayOfMonth\": 1, \"utilizationHours\": \"8 \",\"editable\": \"E\"}," 
				+ "		  {\"month\": 8,\"day\": 2,\"dayOfMonth\": 2, \"utilizationHours\": \"8 \",\"editable\": \"E\"}," 
				+ "		  {\"month\": 9,\"day\": 3,\"dayOfMonth\": 25,\"utilizationHours\": \"9:00\",\"editable\": \"E\"},"
				+ "		  {\"month\": 9,\"day\": 4,\"dayOfMonth\": 26,\"utilizationHours\": \"9:00\",\"editable\": \"E\"},"
				+ "		  {\"month\": 9,\"day\": 5,\"dayOfMonth\": 27,\"utilizationHours\": \"9:00\",\"editable\": \"E\"},"
				+ "		  {\"month\": 9,\"day\": 6,\"dayOfMonth\": 28,\"utilizationHours\": \"9:00\",\"editable\": \"E\"},"
				+ "		  {\"month\": 9,\"day\": 7,\"dayOfMonth\": 29,\"utilizationHours\": \"9:00\",\"editable\": \"E\"}"
				+ "		]" 
				+ "}";

		Utilization util = utilizationBuilder(utilizationJsonString);
		when(mockUtilImpl.downloadUtilization(anyString(), anyString())).thenReturn(util);
		assertNotNull(excelExport.exportToExcel());
	}
	
	@Test(expected = OpumException.class)
	public void throwsExceptionWhenJsonHasInvalidData() throws IOException, SQLException, OpumException {
		String invalidUtilizationJsonString = "" 
				+ "{" 
				+ " \"year\": 2017," 
				+ " \"utilizationJSON\": " 
				+ "		["
				+ "		  {\"month\": 8,\"day\": 1,\"dayOfMonth\": 1, \"utilizationHours\": \"8 \",\"editable\": \"E\"}," 
				+ "		  {\"month\": 8,\"day\": 2,\"dayOfMonth\": 2, \"utilizationHours\": \"8 \",\"editable\": \"E\"}," 
				+ "		  {\"month\": 9,\"day\": 3,\"dayOfMonth\": 25,\"utilizationHours\": \"9:00\",\"editable\": \"E\"},"
				+ "		  {\"month\": 9,\"day\": 4,\"dayOfMonth\": 26,\"utilizationHours\": \"9:00\",\"editable\": \"E\"},"
				+ "		  {\"month\": 9,\"day\": 5,\"dayOfMonth\": 27,\"utilizationHours\": \"9:00\",\"editable\": \"E\"},"
				+ "		  {\"month\": 9,\"day\": 6,\"dayOfMonth\": 28,\"utilizationHours\": \"9:00\",\"editable\": \"E\"},"
				+ "		  {\"month\": 9,\"day\": 7,\"dayOfMonth\": 29,\"utilizationHours\": \"9:00\",\"editable\": \"E\"},"
				+ "		  {\"month\": 9,\"day\": 8,\"dayOfMonth\": 30,\"utilizationHours\": \"9:00\",\"editable\": \"E\"}"
				+ "		]" 
				+ "}";
		Utilization util = utilizationBuilder(invalidUtilizationJsonString);
		when(mockUtilImpl.downloadUtilization(anyString(), anyString())).thenReturn(util);
		excelExport.exportToExcel();
	}

	private Utilization utilizationBuilder(String invalidUtilizationJsonString) {
		Utilization util = new Utilization();
		util.setUtilizationJson(invalidUtilizationJsonString);
		util.setEmployeeSerial("147866PH1");
		util.setUtilizationId(1L);
		util.setYear(UTIL_YEAR);
		return util;
	}

	HSSFCellStyle createCellStyle(Font font) {
		HSSFCellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setAlignment(HorizontalAlignment.CENTER);
		cellStyle.setBorderLeft(BorderStyle.THIN);
		cellStyle.setBorderBottom(BorderStyle.THIN);
		cellStyle.setBorderRight(BorderStyle.THIN);
		cellStyle.setBorderTop(BorderStyle.THIN);
		cellStyle.setFont(font);
		return cellStyle;
	}

	HSSFCellStyle createCellStyle(Font font, IndexedColors foregroundColor, FillPatternType fillPatternType) {
		HSSFCellStyle cellStyle = createCellStyle(font);
		cellStyle.setFillForegroundColor(foregroundColor.getIndex());
		cellStyle.setFillPattern(fillPatternType);
		return cellStyle;
	}
	
	Font createFont(String name, short height, IndexedColors color, boolean isBold) {
		Font font = workbook.createFont();
		font.setFontHeightInPoints(height);
		font.setFontName(name);
		font.setColor(color.getIndex());
		font.setBold(isBold);
		font.setItalic(false);
		return font;
	}
	
}