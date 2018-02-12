package com.ph.ibm.report;

import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Workbook;

public class CellStyleUtils {
	
	public static final int HOLIDAY_FIRST_HEADER = 1;
	
	public static final int HOLIDAY_SECOND_HEADER = 2;
	
	public static final int HOLIDAY_DATA_STYLE = 3;
	
	public static final int PUM_HEADER_STYLE = 4;
	
	public static final int PUM_NORMAL_DATA_STYLE = 5;
	
	public static final int PUM_NORMAL_RIGHT_ALIGN_DATA_STYLE = 6;
	
	public static final int PUM_NORMAL_CENTERED_DATA_STYLE = 7;
	
	public static final int PUM_NORMAL_TOTAL_DATA_STYLE = 8;
	
	public static final int PUM_YTD_HEADER_STYLE = 9;
	
	public static final int PUM_BLUE_ROW_DATA_STYLE = 10;
	
	public static final int PUM_BLUE_HEADER_STYLE = 11;
	
	public static final int PUM_QUARTER_DATA_STYLE = 12;
	
	public static final int PUM_MONTH_HEADER_STYLE = 13;
	
	public static final int PUM_DAY_NUMBER_HEADER_STYLE = 14;
	
	public static final int PUM_GREY_LEFT_DATA_STYLE = 15;
	
	public static final int PUM_HOLIDAY_DATA_STYLE = 16;
	
	public static final int PUM_BLUE_LEAVE_ROW_DATA_STYLE = 17;
	
	public static final int PUM_DECIMAL_TOTAL_DATA_STYLE = 18;
	
	public static final String TREBUCHET_MS = "Trebuchet MS";
	
	public static Map<Integer,CellStyle> generateStyles(Workbook wb){
		Map<Integer,CellStyle> stylemap = new HashMap<Integer,CellStyle>();
		stylemap.put(HOLIDAY_FIRST_HEADER, getWhiteBoldFontSeaGreenBackgroundBorderedStyle(wb));
		stylemap.put(HOLIDAY_SECOND_HEADER, getBlackBoldFontPaleBlueBackgroundBorderedStyle(wb));
		stylemap.put(HOLIDAY_DATA_STYLE, getBlackFontWhiteBackgroundBorderedStyle(wb));
		stylemap.put(PUM_HEADER_STYLE, getPUMHeader(wb));
		stylemap.put(PUM_NORMAL_DATA_STYLE, getPUMNormalDataStyle(wb));
		stylemap.put(PUM_NORMAL_RIGHT_ALIGN_DATA_STYLE, getPUMNormalDataRight(wb));
		stylemap.put(PUM_NORMAL_CENTERED_DATA_STYLE, getPUMNormalCenteredDataStyle(wb));
		stylemap.put(PUM_NORMAL_TOTAL_DATA_STYLE, getPUMNormalTotalDataStyle(wb));
		stylemap.put(PUM_YTD_HEADER_STYLE, getPUMYRDHeaderStyle(wb));
		stylemap.put(PUM_BLUE_ROW_DATA_STYLE, getPUMBlueRowDataStyle(wb));
		stylemap.put(PUM_BLUE_HEADER_STYLE, getPUMBlueHeaderStyle(wb));
		stylemap.put(PUM_QUARTER_DATA_STYLE, getQuarterHeaderStyle(wb));
		stylemap.put(PUM_MONTH_HEADER_STYLE, getMonthHeaderStyle(wb));
		stylemap.put(PUM_DAY_NUMBER_HEADER_STYLE, getDayNumberHeader(wb));
		stylemap.put(PUM_GREY_LEFT_DATA_STYLE, getGreyLeftDataStyle(wb));
		stylemap.put(PUM_HOLIDAY_DATA_STYLE, getHolidayStyle(wb));
		stylemap.put(PUM_BLUE_LEAVE_ROW_DATA_STYLE, getPUMBlueLeaveRowDataStyle(wb));
		stylemap.put(PUM_DECIMAL_TOTAL_DATA_STYLE, getPUMNormalTotalDataDecimalStyle(wb));
		return stylemap;
	}
	
	private static CellStyle getGreyLeftDataStyle(Workbook wb) {
		CellStyle style = getPUMNormalDataStyle(wb);
		style.setAlignment(HorizontalAlignment.LEFT);
	    style.setFillForegroundColor( IndexedColors.GREY_25_PERCENT.getIndex());
		return style;
	}
	
	private static CellStyle getPUMNormalDataRight(Workbook wb) {
		CellStyle style = getPUMNormalDataStyle(wb);
		style.setAlignment(HorizontalAlignment.RIGHT);
		return style;
	}
	
	private static CellStyle getDayNumberHeader(Workbook wb) {
		CellStyle style = getPUMHeader(wb);
		style.setAlignment(HorizontalAlignment.RIGHT);
		return style;
	}
	
	private static CellStyle getPUMNormalTotalDataDecimalStyle(Workbook wb) {
		CellStyle style = getPUMNormalTotalDataStyle(wb);
	    style.setDataFormat(wb.createDataFormat().getFormat("0.0"));
		return style;
	}

	private static CellStyle getPUMNormalTotalDataStyle(Workbook wb) {
	    Font font = wb.createFont();
	    font.setColor(IndexedColors.WHITE.getIndex());
	    font.setFontHeightInPoints((short) 9); 
	    font.setBold(true);
	    font.setFontName(TREBUCHET_MS);
		
	    CellStyle style = wb.createCellStyle();
	    style.setFillForegroundColor( IndexedColors.SEA_GREEN.getIndex());
	    style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
	    style.setWrapText(false);
	    style.setAlignment(HorizontalAlignment.RIGHT);
	    style.setFont(font);
	    
	    style.setBorderBottom(BorderStyle.THIN);
	    style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
	    
	    style.setBorderLeft(BorderStyle.THIN);
	    style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
	    
	    style.setBorderRight(BorderStyle.THIN);
	    style.setRightBorderColor(IndexedColors.BLACK.getIndex());
	    
	    style.setBorderTop(BorderStyle.THIN);
	    style.setTopBorderColor(IndexedColors.BLACK.getIndex());
	    return style;
	}
	
	private static CellStyle getPUMYRDHeaderStyle(Workbook wb) {
	    Font font = wb.createFont();
	    font.setColor(IndexedColors.WHITE.getIndex());
	    font.setFontHeightInPoints((short) 11); 
	    font.setBold(true);
	    font.setFontName("Trebuchet MS");
		
	    CellStyle style = wb.createCellStyle();
	    style.setFillForegroundColor( IndexedColors.DARK_BLUE.getIndex());
	    style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
	    style.setWrapText(false);
	    style.setAlignment(HorizontalAlignment.CENTER);
	    style.setFont(font);
	    
	    style.setBorderRight(BorderStyle.THIN);
	    style.setRightBorderColor(IndexedColors.BLACK.getIndex());
	    return style;
	}
	
	private static CellStyle getQuarterHeaderStyle(Workbook wb) {
	    Font font = wb.createFont();
	    font.setColor(IndexedColors.WHITE.getIndex());
	    font.setFontHeightInPoints((short) 11); 
	    font.setBold(true);
	    font.setFontName("Trebuchet MS");
		
	    CellStyle style = wb.createCellStyle();
	    style.setFillForegroundColor( IndexedColors.AQUA.getIndex());
	    style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
	    style.setWrapText(false);
	    style.setAlignment(HorizontalAlignment.CENTER);
	    style.setFont(font);
	    
	    style.setBorderBottom(BorderStyle.THIN);
	    style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
	    
	    return style;
	}
	
	private static CellStyle getMonthHeaderStyle(Workbook wb) {
	    Font font = wb.createFont();
	    font.setColor(IndexedColors.WHITE.getIndex());
	    font.setFontHeightInPoints((short) 11); 
	    font.setBold(true);
	    font.setFontName("Trebuchet MS");
		
	    CellStyle style = wb.createCellStyle();
	    style.setFillForegroundColor( IndexedColors.SEA_GREEN.getIndex());
	    style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
	    style.setWrapText(false);
	    style.setAlignment(HorizontalAlignment.CENTER);
	    style.setFont(font);
	    
	    style.setBorderBottom(BorderStyle.THIN);
	    style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
	    
	    return style;
	}
	
	private static CellStyle getPUMBlueLeaveRowDataStyle(Workbook wb) {
	    Font font = wb.createFont();
	    font.setColor(IndexedColors.BLACK.getIndex());
	    font.setFontHeightInPoints((short) 8); 
	    font.setBold(false);
	    font.setFontName("Trebuchet MS");
		
	    CellStyle style = wb.createCellStyle();
	    style.setFillForegroundColor( IndexedColors.PALE_BLUE.getIndex());
	    style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
	    style.setWrapText(false);
	    style.setAlignment(HorizontalAlignment.RIGHT);
	    style.setFont(font);
	    
	    style.setBorderBottom(BorderStyle.THIN);
	    style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
	    
	    style.setBorderLeft(BorderStyle.THIN);
	    style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
	    
	    style.setBorderRight(BorderStyle.THIN);
	    style.setRightBorderColor(IndexedColors.BLACK.getIndex());
	    
	    style.setBorderTop(BorderStyle.THIN);
	    style.setTopBorderColor(IndexedColors.BLACK.getIndex());
	    return style;
	}
	
	private static CellStyle getPUMBlueRowDataStyle(Workbook wb) {
		CellStyle style = getPUMBlueLeaveRowDataStyle(wb);
	    style.setDataFormat(wb.createDataFormat().getFormat("0.0"));
		
		return style;
	}
	
	private static CellStyle getPUMBlueHeaderStyle(Workbook wb) {
		
	    CellStyle style = getPUMBlueRowDataStyle(wb);
	    style.setAlignment(HorizontalAlignment.CENTER);
	    return style;
	}


	private static CellStyle getPUMNormalCenteredDataStyle(Workbook wb) {
	    CellStyle style = getPUMNormalDataStyle(wb);
	    style.setAlignment(HorizontalAlignment.CENTER);
		return style;
	}
	
	private static CellStyle getHolidayStyle(Workbook wb) {
		CellStyle style = getPUMNormalDataStyle(wb);
		style.setFillForegroundColor(IndexedColors.ROSE.getIndex());
		
		return style;
	}

	private static CellStyle getPUMNormalDataStyle(Workbook wb) {
	    Font font = wb.createFont();
	    font.setColor(IndexedColors.BLACK.getIndex());
	    font.setFontHeightInPoints((short) 8); 
	    font.setBold(false);
	    font.setFontName("Trebuchet MS");
		
	    CellStyle style = wb.createCellStyle();
	    style.setFillForegroundColor( IndexedColors.WHITE.getIndex());
	    style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
	    style.setWrapText(false);
	    style.setAlignment(HorizontalAlignment.LEFT);
	    style.setFont(font);
	    
	    style.setBorderBottom(BorderStyle.THIN);
	    style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
	    
	    style.setBorderLeft(BorderStyle.THIN);
	    style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
	    
	    style.setBorderRight(BorderStyle.THIN);
	    style.setRightBorderColor(IndexedColors.BLACK.getIndex());
	    
	    style.setBorderTop(BorderStyle.THIN);
	    style.setTopBorderColor(IndexedColors.BLACK.getIndex());
	    return style;
	}

	private static CellStyle getPUMHeader(Workbook wb) {
	    Font font = wb.createFont();
	    font.setColor(IndexedColors.BLACK.getIndex());
	    font.setFontHeightInPoints((short) 8); 
	    font.setBold(false);
	    font.setFontName("Trebuchet MS");
		
	    CellStyle style = wb.createCellStyle();
	    style.setFillForegroundColor( IndexedColors.LIGHT_YELLOW.getIndex());
	    style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
	    style.setWrapText(false);
	    style.setAlignment(HorizontalAlignment.CENTER);
	    style.setFont(font);
	    
	    style.setBorderBottom(BorderStyle.THIN);
	    style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
	    
	    style.setBorderLeft(BorderStyle.THIN);
	    style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
	    
	    style.setBorderRight(BorderStyle.THIN);
	    style.setRightBorderColor(IndexedColors.BLACK.getIndex());
	    
	    style.setBorderTop(BorderStyle.THIN);
	    style.setTopBorderColor(IndexedColors.BLACK.getIndex());
	    return style;
	}

	private static CellStyle getWhiteBoldFontSeaGreenBackgroundBorderedStyle(Workbook wb) {
	    Font font = wb.createFont();
	    font.setColor(IndexedColors.WHITE.getIndex());
	    font.setFontHeightInPoints((short) 9); 
	    font.setBold(true);
	    font.setFontName("Helvetica");
		
	    CellStyle style = wb.createCellStyle();
	    style.setFillForegroundColor( IndexedColors.SEA_GREEN.getIndex());
	    style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
	    style.setWrapText(false);
	    style.setAlignment(HorizontalAlignment.CENTER);
	    style.setFont(font);
	    
	    style.setBorderBottom(BorderStyle.THIN);
	    style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
	    
	    style.setBorderLeft(BorderStyle.THIN);
	    style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
	    
	    style.setBorderRight(BorderStyle.THIN);
	    style.setRightBorderColor(IndexedColors.BLACK.getIndex());
	    
	    style.setBorderTop(BorderStyle.THIN);
	    style.setTopBorderColor(IndexedColors.BLACK.getIndex());
	    return style;
	}
	
	private static CellStyle getBlackBoldFontPaleBlueBackgroundBorderedStyle(Workbook wb) {
	    Font font = wb.createFont();
	    font.setColor(IndexedColors.BLACK.getIndex());
	    font.setFontHeightInPoints((short) 9); 
	    font.setBold(true);
	    font.setFontName("Helvetica");
		
	    CellStyle style = wb.createCellStyle();
	    style.setFillForegroundColor( IndexedColors.PALE_BLUE.getIndex());
	    style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
	    style.setWrapText(false);
	    style.setAlignment(HorizontalAlignment.CENTER);
	    style.setFont(font);
	    
	    style.setBorderBottom(BorderStyle.THIN);
	    style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
	    
	    style.setBorderLeft(BorderStyle.THIN);
	    style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
	    
	    style.setBorderRight(BorderStyle.THIN);
	    style.setRightBorderColor(IndexedColors.BLACK.getIndex());
	    
	    style.setBorderTop(BorderStyle.THIN);
	    style.setTopBorderColor(IndexedColors.BLACK.getIndex());
	    return style;
	}
	
	private static CellStyle getBlackFontWhiteBackgroundBorderedStyle(Workbook wb) {
	    Font font = wb.createFont();
	    font.setColor(IndexedColors.BLACK.getIndex());
	    font.setFontHeightInPoints((short) 8); 
	    font.setBold(false);
	    font.setFontName("Helvetica");
		
	    CellStyle style = wb.createCellStyle();
	    style.setFillForegroundColor( IndexedColors.WHITE.getIndex());
	    style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
	    style.setWrapText(false);
	    style.setAlignment(HorizontalAlignment.LEFT);
	    style.setFont(font);
	    
	    style.setBorderBottom(BorderStyle.THIN);
	    style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
	    
	    style.setBorderLeft(BorderStyle.THIN);
	    style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
	    
	    style.setBorderRight(BorderStyle.THIN);
	    style.setRightBorderColor(IndexedColors.BLACK.getIndex());
	    
	    style.setBorderTop(BorderStyle.THIN);
	    style.setTopBorderColor(IndexedColors.BLACK.getIndex());
	    return style;
	}
}
