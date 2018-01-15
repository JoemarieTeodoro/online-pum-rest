package com.ph.ibm.report;

import static com.ph.ibm.report.UtilizationXLSConstants.PERCENT_SYMBOL;
import static com.ph.ibm.report.UtilizationXLSConstants.SHEET_NAME;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import com.ph.ibm.model.UtilizationRowData;
import com.ph.ibm.model.UtilizationXLSData;

public abstract class UtilizationXLSReport {

	private static final int DECIMAL_PLACES = 2;

	public List<UtilizationRowData> dataRows;

	public Map<Integer,CellStyle> cellStyles;

	private String filePath;

	public UtilizationXLSReport( UtilizationXLSData data) {
		this.filePath = data.getFilePath();
		this.dataRows = data.dataRows;
	}

	public Response generateReport() throws FileNotFoundException, IOException {
		HSSFWorkbook workbook = populateWorkbook();

		try {
			File file = new File(filePath);
			workbook.write(file);

			ResponseBuilder response = Response.ok(file);
			response.header("Content-Disposition","attachement; filename=" + file.getName());
			return response.build();
		}
		catch (IOException io) {
			io.printStackTrace();
			return Response.status(Status.BAD_REQUEST).entity(workbook).type(MediaType.APPLICATION_OCTET_STREAM)
					.build();
		}
		finally{
			workbook.close();
		}
	}

	protected HSSFWorkbook populateWorkbook() {
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = createSheet(workbook);
		cellStyles = CellStyleUtils.generateStyles(workbook);

		generateHeaderRow(sheet);
		generateDataRows(sheet);
		adjustColumnSize(sheet);
		return workbook;
	}

	protected HSSFSheet createSheet(HSSFWorkbook wb) {
		HSSFSheet sheet = wb.createSheet(SHEET_NAME);
		return sheet;
	}

	protected abstract void generateHeaderRow(Sheet sheet);
	
	protected abstract void generateDataRows(Sheet sheet) ;

	protected abstract void generateGrandTotal(Sheet sheet, int rowNumber, Map<String, Number> grandTotal);

	protected abstract void adjustColumnSize(HSSFSheet sheet);
	
	protected void setCell(Row row, int columnNumber, String cellValue) {
		Cell cell = row.createCell(columnNumber);
	    cell.setCellValue(cellValue);
	}

	protected void setCell(Row row, int columnNumber, String cellValue, CellStyle style) {
		Cell cell = row.createCell(columnNumber);
	    cell.setCellValue(cellValue);
	    cell.setCellStyle(style);
	}

	protected void setCell(Row row, int columnNumber, int cellValue, CellStyle style) {
		Cell cell = row.createCell(columnNumber);
	    cell.setCellValue(cellValue);
	    cell.setCellStyle(style);
	}
	
	protected void setCell(Row row, int columnNumber, double cellValue, CellStyle style) {
		Cell cell = row.createCell(columnNumber);
	    cell.setCellValue(cellValue);
	    cell.setCellStyle(style);
	}

	protected CellStyle getYTDCellStyle(Number YTD) {
		int converted = YTD.intValue();
		if( converted >= 100 )
			return cellStyles.get( CellStyleUtils.GREEN_FONT_CENTERED );
		else if( converted > 95 )
			return cellStyles.get( CellStyleUtils.VIOLET_FONTCENTERED );
		else
			return cellStyles.get( CellStyleUtils.RED_FONT_CENTERED );
	}

	protected CellStyle getYTDTotalCellStyle(Number YTD) {
		int converted = YTD.intValue();
		if( converted >= 100 )
			return cellStyles.get( CellStyleUtils.DARK_BACK_GREEN_CENTERED_BOLD_FONT );
		else if( converted > 95 )
			return cellStyles.get( CellStyleUtils.DARK_BACK_VIOLET_CENTERED_BOLD_FONT );
		else
			return cellStyles.get( CellStyleUtils.DARK_BACK_RED_CENTERED_BOLD_FONT );
	}

	protected void addToMap(Map<String, Number> map, String key, Number value) {
		if(map.containsKey(key)) {
			map.put(key, map.get(key).doubleValue() + value.doubleValue());
		}
		else {
			map.put(key, value);
		}
	}

	protected String getPercentage(Number value){
		return round(value, DECIMAL_PLACES)+ PERCENT_SYMBOL;
	}
	
	public double round(Number value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    BigDecimal bd = new BigDecimal(value.doubleValue());
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}
}
