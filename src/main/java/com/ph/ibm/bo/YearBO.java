package com.ph.ibm.bo;

import java.sql.SQLException;

import com.ph.ibm.model.PUMYear;
import com.ph.ibm.model.PUMYearList;
import com.ph.ibm.repository.PUMYearRepository;
import com.ph.ibm.repository.impl.PUMYearRepositoryImpl;

public class YearBO {

	private PUMYearRepository pumYearRepository = new PUMYearRepositoryImpl();

	/**
	 * @param pumYearList
	 * @return PUMYearList Object
	 */
	public PUMYearList retrieveYear(PUMYearList pumYearList) {
		try {
			pumYearList.setPumYearList(pumYearRepository.retrieveYear());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return pumYearList;
	}
	
	/**
	 * @param year
	 * @return PUMYear Object
	 * @throws SQLException
	 */
	public PUMYear retrieveYearDate(int year) throws SQLException {
		return pumYearRepository.retrieveYearDate(year);
	}
	
	public PUMYear retrieveCurrentFY() {
		return pumYearRepository.retrieveCurrentFY();
	}
}
