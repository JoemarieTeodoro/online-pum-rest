package com.ph.ibm.model;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class UtilizationRowDataTest {
	
	@Test
	public void testGetWeekHourNull() {
		UtilizationRowData data = new UtilizationRowData(null, null, null,null);
		assertTrue(data.getWeekHour(12) == 0);
	}
	
	@Test
	public void testGetWeekHourExceed() {
		List<Integer> weekUtilization = new ArrayList<Integer>();
		weekUtilization.add(10);
		weekUtilization.add(20);
		weekUtilization.add(30);
		
		UtilizationRowData data = new UtilizationRowData(null, null, weekUtilization,null);
		assertTrue(data.getWeekHour(12) == 0);
	}
	
	@Test
	public void testGetWeekHourIncorrect() {
		List<Integer> weekUtilization = new ArrayList<Integer>();
		weekUtilization.add(10);
		weekUtilization.add(20);
		weekUtilization.add(30);
		
		UtilizationRowData data = new UtilizationRowData(null, null, weekUtilization,null);
		assertTrue(data.getWeekHour(0) == 0);
	}
	
	@Test
	public void testGetWeekHourIncorrect2() {
		List<Integer> weekUtilization = new ArrayList<Integer>();
		weekUtilization.add(10);
		weekUtilization.add(20);
		weekUtilization.add(30);
		
		UtilizationRowData data = new UtilizationRowData(null, null, weekUtilization,null);
		assertTrue(data.getWeekHour(4) == 0);
	}

	@Test
	public void testGetWeekHour() {
		List<Integer> weekUtilization = new ArrayList<Integer>();
		weekUtilization.add(10);
		weekUtilization.add(20);
		weekUtilization.add(30);
		
		
		UtilizationRowData data = new UtilizationRowData(null, null, weekUtilization,null);
		assertTrue(data.getWeekHour(1) == 10);
	}
}
