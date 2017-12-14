package com.ph.ibm.repository;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

import com.ph.ibm.model.Holiday;
import com.ph.ibm.model.PUMMonth;
import com.ph.ibm.model.PUMQuarter;
import com.ph.ibm.model.PUMYear;
import com.ph.ibm.opum.exception.OpumException;

public interface PUMYearRepository {

	/**
	 * 
	 * 
	 * @param pumYear
	 * @return boolean
	 * @throws SQLException
	 * @throws ParseException
	 * @throws OpumException
	 */
	public void saveYear(PUMYear pumYear) throws SQLException, ParseException, OpumException;

	/**
	 * 
	 * 
	 * @return List
	 * @throws SQLException
	 */
	public List<PUMYear> retrieveYear() throws SQLException;

	/**
	 * 
	 * 
	 * @param year
	 * @return PUMYear object
	 * @throws SQLException
	 */
	public PUMYear retrieveYearDate(int year) throws SQLException;

	/**
	 * 
	 * 
	 * @param pumYear
	 * @return boolean
	 * @throws SQLException
	 * @throws ParseException
	 */
	public boolean editYear(PUMYear pumYear) throws SQLException, ParseException;

	public boolean saveQuarter(PUMQuarter pumQuarter) throws SQLException, ParseException;

	public boolean saveMonth(PUMMonth pumMonth) throws SQLException, ParseException;

	public PUMYear retrieveCurrentFY();

	public void populateFiscalYear(PUMYear pumYear) throws SQLException, ParseException;

	public void addUpdateHolidayInFiscalYearTemplate(Holiday holiday, PUMYear pumyear) throws SQLException, OpumException;
}
