package com.ph.ibm.report.pum;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.ph.ibm.model.Holiday;
import com.ph.ibm.report.pum.data.PUMEmployeeData;
import com.ph.ibm.report.pum.data.PUMMonthlyUtilizationData;
import com.ph.ibm.report.pum.data.PUMOverallUtilizationData;
import com.ph.ibm.report.pum.data.PUMQuarterlyUtilizationData;
import com.ph.ibm.report.pum.data.PUMWeeklyUtilizationData;

public class PUMUtilizationXLSReportTest {

	public List<Holiday> getHolidayList(){
		Holiday holidayOne = new Holiday();
		Holiday holidayTwo = new Holiday();
		Holiday holidayThree = new Holiday();

		holidayOne.setName("WWER Commemoration Day");
		holidayOne.setDate("2018-01-12");

		holidayTwo.setName("Melvin Rabang Day");
		holidayTwo.setDate("2018-06-09");

		holidayThree.setName("Resignation Day");
		holidayThree.setDate("2018-09-18");

		List<Holiday> holidays = new ArrayList<>();
		holidays.add(holidayOne);
		holidays.add(holidayTwo);
		holidays.add(holidayThree);

		return holidays;
	}

	public List<PUMEmployeeData> getEmployeeDatas() {
		List<PUMEmployeeData> datas = new ArrayList<PUMEmployeeData>();
		datas.add(getEmployeeData1());
		datas.add(getEmployeeData2());

		return datas;
	}

	public PUMEmployeeData getEmployeeData1() {
		return new PUMEmployeeData("USAA", "P100Y2", "Jason Tan", "2017-10-17", "2018-12-31", getUtil1());
	}

	public PUMEmployeeData getEmployeeData2() {
		return new PUMEmployeeData("USAA", "P100Y3", "Jayson Tan", "2017-10-17", "2018-12-31", getUtil2());
	}
	
	public PUMOverallUtilizationData getUtil1() {
		return new PUMOverallUtilizationData(getQuarterly1(), getLeaves(), 2000, 3000);
	}
	
	public PUMOverallUtilizationData getUtil2() {
		return new PUMOverallUtilizationData(getQuarterly2(), getLeaves2(), 4000, 8000);
	}
	
	public Map<String,PUMQuarterlyUtilizationData> getQuarterly1() {
		Map<String,PUMQuarterlyUtilizationData> map = new LinkedHashMap<String,PUMQuarterlyUtilizationData>();
		map.put("Q1", new PUMQuarterlyUtilizationData(getQ11(), 250, 300));
//		map.put("Q2", new PUMQuarterlyUtilizationData(null, 260, 300));
//		map.put("Q2", new PUMQuarterlyUtilizationData(getQ21(), 260, 300));
//		map.put("Q3", new PUMQuarterlyUtilizationData(getQ31(), 270, 300));
//		map.put("Q4", new PUMQuarterlyUtilizationData(getQ41(), 280, 300));
		return map;
	}
	
	public Map<String,PUMQuarterlyUtilizationData> getQuarterly2() {
		Map<String,PUMQuarterlyUtilizationData> map = new LinkedHashMap<String,PUMQuarterlyUtilizationData>();
		map.put("Q1", new PUMQuarterlyUtilizationData(getQ12(), 280, 300));
//		map.put("Q2", new PUMQuarterlyUtilizationData(null, 270, 300));
//		map.put("Q2", new PUMQuarterlyUtilizationData(getQ22(), 270, 300));
//		map.put("Q3", new PUMQuarterlyUtilizationData(getQ32(), 260, 300));
//		map.put("Q4", new PUMQuarterlyUtilizationData(getQ42(), 250, 300));
		return map;
	}
	
	public Map<String,PUMMonthlyUtilizationData> getQ11(){
		Map<String,PUMMonthlyUtilizationData> map = new LinkedHashMap<String,PUMMonthlyUtilizationData>();
		map.put("January", new PUMMonthlyUtilizationData("January", getWeekly11(), getLeaves(), 100, 150) );
		map.put("Febuary", new PUMMonthlyUtilizationData("Febuary", getWeekly12(), getLeaves(), 100, 150) );
//		map.put("March", new PUMMonthlyUtilizationData("March", null, getLeaves(), 100, 150) );
		return map;
	}
	
	public Map<String,PUMMonthlyUtilizationData> getQ12(){
		Map<String,PUMMonthlyUtilizationData> map = new LinkedHashMap<String,PUMMonthlyUtilizationData>();
		map.put("January", new PUMMonthlyUtilizationData("January", getWeekly21(), getLeaves2(), 100, 250) );
		map.put("Febuary", new PUMMonthlyUtilizationData("Febuary", getWeekly22(), getLeaves(), 100, 250) );
//		map.put("March", new PUMMonthlyUtilizationData("March", null, getLeaves(), 100, 250) );
		return map;
	}
	
	public Map<String,PUMMonthlyUtilizationData> getQ21(){
		Map<String,PUMMonthlyUtilizationData> map = new LinkedHashMap<String,PUMMonthlyUtilizationData>();
		map.put("April", new PUMMonthlyUtilizationData("April", null, getLeaves(), 100, 160) );
		map.put("May", new PUMMonthlyUtilizationData("May", null, getLeaves(), 100, 160) );
		map.put("June", new PUMMonthlyUtilizationData("June", null, getLeaves(), 100, 160) );
		return map;
	}
	
	public Map<String,PUMMonthlyUtilizationData> getQ31(){
		Map<String,PUMMonthlyUtilizationData> map = new LinkedHashMap<String,PUMMonthlyUtilizationData>();
		map.put("July", new PUMMonthlyUtilizationData("July", null, getLeaves(), 100, 170) );
		map.put("August", new PUMMonthlyUtilizationData("August", null, getLeaves(), 100, 170) );
		map.put("September", new PUMMonthlyUtilizationData("September", null, getLeaves(), 100, 170) );
		return map;
	}
	
	public Map<String,PUMMonthlyUtilizationData> getQ41(){
		Map<String,PUMMonthlyUtilizationData> map = new LinkedHashMap<String,PUMMonthlyUtilizationData>();
		map.put("October", new PUMMonthlyUtilizationData("October", null, getLeaves(), 100, 180) );
		map.put("November", new PUMMonthlyUtilizationData("November", null, getLeaves(), 100, 180) );
		map.put("December", new PUMMonthlyUtilizationData("December", null, getLeaves(), 100, 180) );
		return map;
	}
	
	public Map<String,PUMMonthlyUtilizationData> getQ22(){
		Map<String,PUMMonthlyUtilizationData> map = new LinkedHashMap<String,PUMMonthlyUtilizationData>();
		map.put("April", new PUMMonthlyUtilizationData("April", null, getLeaves2(), 100, 260) );
		map.put("May", new PUMMonthlyUtilizationData("May", null, getLeaves(), 100, 260) );
		map.put("June", new PUMMonthlyUtilizationData("June", null, getLeaves(), 100, 260) );
		return map;
	}
	
	public Map<String,PUMMonthlyUtilizationData> getQ32(){
		Map<String,PUMMonthlyUtilizationData> map = new LinkedHashMap<String,PUMMonthlyUtilizationData>();
		map.put("July", new PUMMonthlyUtilizationData("July", null, getLeaves2(), 100, 270) );
		map.put("August", new PUMMonthlyUtilizationData("August", null, getLeaves(), 100, 270) );
		map.put("September", new PUMMonthlyUtilizationData("September", null, getLeaves(), 100, 270) );
		return map;
	}
	
	public Map<String,PUMMonthlyUtilizationData> getQ42(){
		Map<String,PUMMonthlyUtilizationData> map = new LinkedHashMap<String,PUMMonthlyUtilizationData>();
		map.put("October", new PUMMonthlyUtilizationData("October", null, getLeaves2(), 100, 280) );
		map.put("November", new PUMMonthlyUtilizationData("November", null, getLeaves(), 100, 280) );
		map.put("December", new PUMMonthlyUtilizationData("December", null, getLeaves(), 100, 280) );
		return map;
	}
	
	public List<PUMWeeklyUtilizationData> getWeekly11(){
		List<PUMWeeklyUtilizationData> weekData = new ArrayList<>();
	
		weekData.add(new PUMWeeklyUtilizationData("2018-01-05", getDaily1(), 1));
		weekData.add(new PUMWeeklyUtilizationData("2018-01-12", null, 2));
		weekData.add(new PUMWeeklyUtilizationData("2018-01-19", null, 3));
		weekData.add(new PUMWeeklyUtilizationData("2018-01-26", null, 4));
		
		return weekData;
	}
	
	public List<PUMWeeklyUtilizationData> getWeekly21(){
		List<PUMWeeklyUtilizationData> weekData = new ArrayList<>();
	
		weekData.add(new PUMWeeklyUtilizationData("2018-01-05", getDaily2(), 4));
		weekData.add(new PUMWeeklyUtilizationData("2018-01-12", null, 3));
		weekData.add(new PUMWeeklyUtilizationData("2018-01-19", null, 2));
		weekData.add(new PUMWeeklyUtilizationData("2018-01-26", null, 1));
		
		return weekData;
	}
	
	public List<PUMWeeklyUtilizationData> getWeekly12(){
		List<PUMWeeklyUtilizationData> weekData = new ArrayList<>();
	
		weekData.add(new PUMWeeklyUtilizationData("2018-02-02", getDaily1(), 1));
		weekData.add(new PUMWeeklyUtilizationData("2018-02-09", null, 2));
		weekData.add(new PUMWeeklyUtilizationData("2018-02-16", null, 3));
		weekData.add(new PUMWeeklyUtilizationData("2018-02-23", null, 4));
		
		return weekData;
	}
	
	public List<PUMWeeklyUtilizationData> getWeekly22(){
		List<PUMWeeklyUtilizationData> weekData = new ArrayList<>();
	
		weekData.add(new PUMWeeklyUtilizationData("2018-02-02", getDaily2(), 4));
		weekData.add(new PUMWeeklyUtilizationData("2018-02-09", null, 3));
		weekData.add(new PUMWeeklyUtilizationData("2018-02-16", null, 2));
		weekData.add(new PUMWeeklyUtilizationData("2018-02-23", null, 1));
		
		return weekData;
	}
	public Map<String, String> getDaily1(){
		Map<String, String> dailyData = new LinkedHashMap<>();
		dailyData.put("Monday", "VL");
		dailyData.put("Saturday", "");
		dailyData.put("Sunday", "");
		dailyData.put("Tuesday", "SL");
		dailyData.put("Wednesday", "OL");
		dailyData.put("Thursday", "8.0");
		dailyData.put("Friday", "HO");
		
		return dailyData;
	}
	
	public Map<String, String> getDaily2(){
		Map<String, String> dailyData = new LinkedHashMap<>();
		dailyData.put("Monday", "SL");
		dailyData.put("Saturday", "");
		dailyData.put("Sunday", "");
		dailyData.put("Tuesday", "OL");
		dailyData.put("Wednesday", "VL");
		dailyData.put("Thursday", "VL");
		dailyData.put("Friday", "VL");
		
		return dailyData;
	}
	
	public Map<String,Integer> getLeaves(){
		Map<String,Integer> map = new LinkedHashMap<String,Integer>();
		map.put("VL", 2);
		map.put("SL", 3);
		map.put("OL", 1);
		map.put("EL", 1);
		map.put("TR", 1);
		map.put("HO", 1);
		map.put("CDO", 1);
	
		return map;
	}
	
	public Map<String,Integer> getLeaves2(){
		Map<String,Integer> map = new LinkedHashMap<String,Integer>();
		map.put("VL", 5);
		map.put("SL", 2);
		map.put("OL", 4);
		map.put("EL", 2);
		map.put("TR", 3);
		map.put("HO", 8);
		map.put("CDO", 10);
	
		return map;
	}
}
