package com.ph.ibm.repository;

import java.sql.SQLException;


public interface MonthRepository {
	
	public String getMonthEndDate(String yearID, int monthID) throws SQLException;
	
}
