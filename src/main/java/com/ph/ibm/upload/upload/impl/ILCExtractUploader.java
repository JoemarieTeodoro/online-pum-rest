package com.ph.ibm.upload.upload.impl;

import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import com.ph.ibm.repository.PUMYearRepository;
import com.ph.ibm.repository.UtilizationRepository;
import com.ph.ibm.repository.impl.PUMYearRepositoryImpl;
import com.ph.ibm.repository.impl.UtilizationRepositoryImpl;
import com.ph.ibm.upload.ExcelUploaderBase;
import com.ph.ibm.util.OpumConstants;

public class ILCExtractUploader extends ExcelUploaderBase {
	
    private static final int BILL_CODE_INCREMENT = 1;

    private static final String BILL_CODE_HEADER = "Bill Code";
	
	private static final String HOURS_HEADER = "Hours";
	
	private static final String SERIAL_HEADER = "Serial";
	
	private int dateColumnsStart;

	private int dateColumnsEnd;

	private int employeeIdColumnNum;

	private List<String> dates;
	
	private PUMYearRepository pumYearRepository;

	private UtilizationRepository utilizationRepository;
	
	private Logger logger;
	
	public ILCExtractUploader() {
		this.pumYearRepository = new PUMYearRepositoryImpl();
		this.utilizationRepository = new UtilizationRepositoryImpl();
		this.logger = Logger.getLogger(ILCExtractUploader.class);
	}

	@Override
	public Response upload(
			InputStream fileInputStream, 
			FormDataContentDisposition fileFormDataContentDisposition,  
			UriInfo uriInfo) throws Exception {
		
		try {
			Workbook workbook = new XSSFWorkbook(fileInputStream);
			Map<String, Map<String, Double>> employeeHoursMap = parseExcelFile(workbook);
			workbook.close();
			utilizationRepository.populateActualUtilization(pumYearRepository.retrieveCurrentFY().getYearId(), employeeHoursMap);
		} catch( SQLException e ){
            logger.error( "SQL Exception due to " + e.getMessage() );
            logger.error( e.getStackTrace() );
            return Response.status( 406 ).entity( OpumConstants.SQL_ERROR ).build();
        } catch (Exception e) {
        	logger.error( "Exception due to " + e.getMessage() );
            logger.error( e.getStackTrace() );
        	return Response.status( 406 ).entity( OpumConstants.ERROR ).build();
		}
		
		return Response.status( Status.OK ).entity( OpumConstants.ILC_SUCCESS_UPLOAD ).build();
	}

	@Override
	protected Map<String, Map<String, Double>> parseExcelFile(Workbook workbook) {
        Sheet sheet = workbook.getSheetAt( 2 );
		this.dateColumnsStart = getStartDateColumnNum(sheet);				
		this.dateColumnsEnd = getEndDateColumnNum(sheet);
		this.employeeIdColumnNum = getSerialColumnNum(sheet);
		this.dates = getDates(sheet);
		Map<String, Map<String, Double>> employeeHoursMap = getEmployeeMap(sheet);
		return employeeHoursMap;
	}
	
	
	private Map<String, Map<String, Double>> getEmployeeMap(Sheet sheet) {
		Map<String, Map<String, Double>> employeeMap = new TreeMap<String, Map<String, Double>>();
		int rowsInSheet = sheet.getLastRowNum();
		for(int i = 1; i < rowsInSheet; i++) {
			Row currentRow = sheet.getRow(i);
			String empId = retrieveValueFromCell(currentRow, employeeIdColumnNum);
			if(!empId.isEmpty()) {
				Map<String, Double> employeeHoursMap = getEmployeeHours(currentRow);
				if(employeeMap.containsKey(empId)) {
					employeeHoursMap = combineHours(employeeHoursMap, employeeMap.get(empId));
				}
				
				employeeMap.put(empId, employeeHoursMap);
			}
		}
		
		return employeeMap;
	}
	
	private Map<String, Double> combineHours(Map<String, Double> row1, Map<String, Double> row2){
		Map<String, Double> employeeHoursMap = new TreeMap<String, Double>();
		
		for(String weekEnding : row1.keySet()) {
			Double totalHours = row1.get(weekEnding) + row2.get(weekEnding);
			employeeHoursMap.put(weekEnding, totalHours);
		}
		
		return employeeHoursMap;
	}
	
	private Map<String, Double> getEmployeeHours(Row row) {
		Map<String, Double> hoursMap = new TreeMap<String, Double>();
		int dateListIndex = 0;
		
		for(int i = this.dateColumnsStart; i < this.dateColumnsEnd; i++) {
			String headerDateValue = this.dates.get(dateListIndex++);
			Double hoursForCurrentDate = retrieveValueFromCell(row, i).equals("") ? 0.0
																					: Double.valueOf(retrieveValueFromCell(row, i));
			hoursMap.put(headerDateValue, hoursForCurrentDate);
		}
		
		return hoursMap;
	}
	
	private List<String> getDates(Sheet sheet){
		List<String> dateList = new ArrayList<String>();
		
		for(int i = this.dateColumnsStart; i < this.dateColumnsEnd; i++) {
			dateList.add(retrieveHeader(sheet, i));
		}
		
		return dateList;
	}
	
	private int getStartDateColumnNum(Sheet sheet) {
		return getHeaderValueColumnNum(sheet, BILL_CODE_HEADER) + BILL_CODE_INCREMENT;
	}

	private int getEndDateColumnNum(Sheet sheet) {
		return getHeaderValueColumnNum(sheet, HOURS_HEADER);
	}

	private int getSerialColumnNum(Sheet sheet) {
		return getHeaderValueColumnNum(sheet, SERIAL_HEADER);
	}

}
