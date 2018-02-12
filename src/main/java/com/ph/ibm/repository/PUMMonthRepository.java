package com.ph.ibm.repository;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

import com.ph.ibm.model.PUMDownloadReportMonth;

public interface PUMMonthRepository {

	public boolean saveMonthEndingDates(List<PUMDownloadReportMonth> pumMonth) throws SQLException, ParseException;
}
