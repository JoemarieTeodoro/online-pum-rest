package com.ph.ibm.report;

import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;

import com.ph.ibm.model.UtilizationRowData;
import com.ph.ibm.model.UtilizationXLSData;

public class UtilizationXLSReportTest {
	
//	public void generateReport2() throws FileNotFoundException, IOException {
//		HSSFWorkbook workbook = populateWorkbook();
//		//TODO: DO NOT INCLUDE IN MERGE TO DEVELOPMENT
//	    try ( FileOutputStream outputStream = new FileOutputStream(filePath)) {
//        workbook.write(outputStream);
//	    }
//	}
	
	@Test
	@Ignore
	public void testXLSWeeklyReport() throws FileNotFoundException, IOException {
		Date date = parseDate("2018-01-01");
		UtilizationXLSData data = new UtilizationXLSData("C:\\Users\\IBM_ADMIN\\Desktop\\Wtest.xls", 2, getTestlist(),date);
		UtilizationXLSReport report = new UtilizationXLSWeeklyReport(data);
		
//		report.generateReport2();
	}
	
	@Test
	@Ignore
	public void testXLSQuarterlyReport() throws FileNotFoundException, IOException {
		Date date = parseDate("2018-01-01");
		UtilizationXLSData data = new UtilizationXLSData("C:\\Users\\IBM_ADMIN\\Desktop\\Qtest.xls", 4, getTestlist(),date);
		UtilizationXLSReport report = new UtilizationXLSQuarterReport(data);
		
//		report.generateReport2();
	}
	
	@Test
	public void testGetFriday() {
		UtilizationXLSData data = new UtilizationXLSData("C:\\Users\\IBM_ADMIN\\Desktop\\test.xls", 2, getTestlist(), null);
		UtilizationXLSWeeklyReport report = new UtilizationXLSWeeklyReport(data);
		LocalDate d = LocalDate.of(2018, 1, 1);
		
		assertTrue( report.getNextFriday(d).toString().equals("2018-01-05"));
	}
	
	@Test
	public void testGetNextFriday() {
		UtilizationXLSData data = new UtilizationXLSData("C:\\Users\\IBM_ADMIN\\Desktop\\test.xls", 2, getTestlist(),null);
		UtilizationXLSWeeklyReport report = new UtilizationXLSWeeklyReport(data);
		LocalDate d = LocalDate.of(2018, 1, 5);
		
		assertTrue( report.getNextFriday(d).toString().equals("2018-01-12"));
	}
	
	@Test
	public void testGetHeaders() {
		UtilizationXLSData data = new UtilizationXLSData("C:\\Users\\IBM_ADMIN\\Desktop\\test.xls", 2, getTestlist(),null);
		UtilizationXLSWeeklyReport report = new UtilizationXLSWeeklyReport(data);
		LocalDate d = LocalDate.of(2018, 1, 1);
		
		assertTrue( report.createWeekHeader(d).toString().equals("WK0101"));
	}
	
	@Test
	public void testGetHeaders3() {
		UtilizationXLSData data = new UtilizationXLSData("C:\\Users\\IBM_ADMIN\\Desktop\\test.xls", 2, getTestlist(),null);
		UtilizationXLSWeeklyReport report = new UtilizationXLSWeeklyReport(data);
		LocalDate d = LocalDate.of(2018, 1, 5);
		Date dd = parseDate("2018-01-01");
		Map<Integer,String> lol = report.generateWeekHeaders(dd);
		
		assertTrue( lol.get(1).equals("WK0105"));
	}
	
	 public static Date parseDate(String date) {
	     try {
	         return new SimpleDateFormat("yyyy-MM-dd").parse(date);
	     } catch (ParseException e) {
	         return null;
	     }
	  }
	
	public List<UtilizationRowData> getTestlist(){
		List<UtilizationRowData> data = new ArrayList<UtilizationRowData>();
		
		List<Integer> utilization1 = new ArrayList<Integer>();
		utilization1.add(40);
		
		List<Integer> utilization2 = new ArrayList<Integer>();
		utilization2.add(40);
		utilization2.add(38);
		
		List<Integer> utilization3 = new ArrayList<Integer>();
		utilization3.add(40);
		utilization3.add(40);
		
		List<Integer> Qutilization1 = new ArrayList<Integer>();
		Qutilization1.add(520);
		Qutilization1.add(520);
		Qutilization1.add(520);
		Qutilization1.add(520);
		
		List<Integer> Qutilization2 = new ArrayList<Integer>();
		Qutilization2.add(520);
		Qutilization2.add(510);
		Qutilization2.add(515);
		Qutilization2.add(520);
		List<Integer> Qutilization3 = new ArrayList<Integer>();
		Qutilization3.add(520);
		Qutilization3.add(0);
		Qutilization3.add(520);
		Qutilization3.add(500);
		
		UtilizationRowData data1 = new UtilizationRowData("P100Y2", "Jason Tan", utilization1, Qutilization1);
		UtilizationRowData data2 = new UtilizationRowData("P100Y3", "Stephen Tan", utilization2, Qutilization2);
		UtilizationRowData data3 = new UtilizationRowData("P100Y4", "Json Tan", utilization3, Qutilization3);
		
		data.add(data1);
		data.add(data2);
		data.add(data3);
		
		return data;
	}

}
