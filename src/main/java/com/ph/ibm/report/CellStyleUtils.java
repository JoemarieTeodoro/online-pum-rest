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
	
	public static final int YELLOW_BORDERED_HEADER = 1; 
	
	public static final int GREEN_BOLD_CENTERED_TOTAL = 2; 
	
	public static final int GREEN_BOLD_TOTAL = 3; 
	
	public static final int CENTERED = 4;
	
	public static final int RED_FONT_CENTERED = 5;
	
	public static final int GREEN_FONT_CENTERED = 6;
	
	public static final int VIOLET_FONTCENTERED = 7;
	
	public static final int DARK_BACK_WHITE_CENTERED_FONT = 8;
	
	public static final int DARK_BACK_WHITE_CENTERED_BOLD_FONT = 9;
	
	public static final int DARK_BACK_RED_CENTERED_BOLD_FONT = 10;
	
	public static final int DARK_BACK_GREEN_CENTERED_BOLD_FONT = 11;
	
	public static final int DARK_BACK_VIOLET_CENTERED_BOLD_FONT = 12;
	
	
	public static Map<Integer,CellStyle> generateStyles(Workbook book){
		Map<Integer,CellStyle> stylemap = new HashMap<Integer,CellStyle>();
		stylemap.put(YELLOW_BORDERED_HEADER, getYellowBorderedHeaderStyle(book));
		stylemap.put(GREEN_BOLD_TOTAL, getGreenStyle(book));
		stylemap.put(GREEN_BOLD_CENTERED_TOTAL, getGreenCenteredStyle(book));
		stylemap.put(CENTERED, getCenteredStyle(book));
		stylemap.put(DARK_BACK_WHITE_CENTERED_FONT, getDarkBackWhiteCenteredFontStyle(book));
		stylemap.put(DARK_BACK_WHITE_CENTERED_BOLD_FONT, getDarkBackWhiteCenteredBoldFontStyle(book));
		stylemap.put(RED_FONT_CENTERED, getCenteredRedFontStyle(book));
		stylemap.put(GREEN_FONT_CENTERED, getCenteredGreenFontStyle(book));
		stylemap.put(VIOLET_FONTCENTERED, getCenteredVioletFontStyle(book));
		stylemap.put(DARK_BACK_RED_CENTERED_BOLD_FONT, getDarkBackRedCenteredBoldFontStyle(book));
		stylemap.put(DARK_BACK_VIOLET_CENTERED_BOLD_FONT, getDarkBackVioletCenteredBoldFontStyle(book));
		stylemap.put(DARK_BACK_GREEN_CENTERED_BOLD_FONT, getDarkBackGreenCenteredBoldFontStyle(book));
		
		return stylemap;
	}
	
	private static CellStyle getDarkBackGreenCenteredBoldFontStyle(Workbook wb) {
	    Font redFont = wb.createFont();
	    redFont.setColor(IndexedColors.GREEN.getIndex());
	    redFont.setBold(true);
	    
	    return getDarkBackgroundCenterStyle(wb,redFont);
	}

	private static CellStyle getDarkBackVioletCenteredBoldFontStyle(Workbook wb) {
	    Font violetFont = wb.createFont();
	    violetFont.setColor(IndexedColors.VIOLET.getIndex());
	    violetFont.setBold(true);
	    
	    return getDarkBackgroundCenterStyle(wb,violetFont);
	}

	private static CellStyle getDarkBackRedCenteredBoldFontStyle(Workbook wb) {
	    Font redFont = wb.createFont();
	    redFont.setColor(IndexedColors.RED.getIndex());
	    redFont.setBold(true);
	    
	    return getDarkBackgroundCenterStyle(wb,redFont);
	}

	private static CellStyle getDarkBackWhiteCenteredBoldFontStyle(Workbook wb) {
	    Font whiteFont = wb.createFont();
	    whiteFont.setColor(IndexedColors.WHITE.getIndex());
	    whiteFont.setBold(true);
	    
	    return getDarkBackgroundCenterStyle(wb,whiteFont);
	}

	private static CellStyle getDarkBackWhiteCenteredFontStyle(Workbook wb){
	    Font whiteFont = wb.createFont();
	    whiteFont.setColor(IndexedColors.WHITE.getIndex());
	    
	    return getDarkBackgroundCenterStyle(wb,whiteFont);
	}
	
	private static CellStyle getDarkBackgroundCenterStyle(Workbook wb, Font font) {
	    CellStyle style = wb.createCellStyle();
	    style.setFillForegroundColor( IndexedColors.BLACK.getIndex());
	    style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
	    style.setWrapText(false);
	    style.setAlignment(HorizontalAlignment.CENTER);
	    style.setFont(font);
	    return style;
	}
	
	private static CellStyle getCenteredStyle(Workbook wb){
	    CellStyle style = wb.createCellStyle();
	    style.setAlignment(HorizontalAlignment.CENTER);
	    return style;
	}
	
	private static CellStyle getCenteredStyle(Workbook wb , Font font){
	    CellStyle style = wb.createCellStyle();
	    style.setAlignment(HorizontalAlignment.CENTER);
	    style.setFont(font);
	    return style;
	}
	
	private static CellStyle getCenteredRedFontStyle(Workbook wb){
		Font red = wb.createFont();
		red.setColor(IndexedColors.RED.getIndex());
				
	    return getCenteredStyle(wb , red);
	}
	
	private static CellStyle getCenteredGreenFontStyle(Workbook wb){
		Font red = wb.createFont();
		red.setColor(IndexedColors.GREEN.getIndex());
				
	    return getCenteredStyle(wb , red);
	}
	
	private static CellStyle getCenteredVioletFontStyle(Workbook wb){
		Font red = wb.createFont();
		red.setColor(IndexedColors.VIOLET.getIndex());
				
	    return getCenteredStyle(wb , red);
	}
	
	private static CellStyle getGreenStyle(Workbook wb){
	    Font boldFont = wb.createFont();
	    boldFont.setBold(true);
		
	    CellStyle style = wb.createCellStyle();
	    style.setFillForegroundColor( IndexedColors.LIGHT_GREEN.getIndex());
	    style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
	    style.setWrapText(false);
	    style.setFont(boldFont);
	    return style;
	}
	
	private static CellStyle getGreenCenteredStyle(Workbook wb){
	    CellStyle style = getGreenStyle(wb);
	    style.setAlignment(HorizontalAlignment.CENTER);
	    return style;
	}
	
	private static CellStyle getYellowBorderedHeaderStyle(Workbook wb){
	    CellStyle style = wb.createCellStyle();
	    style.setAlignment(HorizontalAlignment.CENTER);
	    style.setFillForegroundColor( IndexedColors.LIGHT_YELLOW.getIndex());
	    style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
	    style.setWrapText(false);
	    
	    style.setBorderBottom(BorderStyle.MEDIUM);
	    style.setBottomBorderColor(IndexedColors.GREY_80_PERCENT.getIndex());
	    
	    style.setBorderLeft(BorderStyle.MEDIUM);
	    style.setLeftBorderColor(IndexedColors.GREY_80_PERCENT.getIndex());
	    
	    style.setBorderRight(BorderStyle.MEDIUM);
	    style.setRightBorderColor(IndexedColors.GREY_80_PERCENT.getIndex());
	    
	    style.setBorderTop(BorderStyle.MEDIUM);
	    style.setTopBorderColor(IndexedColors.GREY_80_PERCENT.getIndex());
	    
	    return style;
	}
}
