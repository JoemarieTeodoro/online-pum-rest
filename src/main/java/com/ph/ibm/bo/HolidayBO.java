package com.ph.ibm.bo;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;

import com.ph.ibm.model.Holiday;
import com.ph.ibm.model.PUMYear;
import com.ph.ibm.opum.exception.OpumException;
import com.ph.ibm.repository.HolidayEngagementRepository;
import com.ph.ibm.repository.PUMYearRepository;
import com.ph.ibm.repository.impl.HolidayRepositoryImpl;
import com.ph.ibm.repository.impl.PUMYearRepositoryImpl;
import com.ph.ibm.util.OpumConstants;
import com.ph.ibm.util.ValidationUtils;

public class HolidayBO {
	private Logger logger = Logger.getLogger(HolidayBO.class);
	
	private HolidayEngagementRepository holidayEngagementRepository = new HolidayRepositoryImpl();

	private PUMYearRepository pumYearRepository = new PUMYearRepositoryImpl();

	/**
	 * @throws SQLException
	 * @throws OpumException
	 */
	public Response addHolidayEngagement(Holiday holiday) throws Exception {
		Response response = null;
		try {
			if (pumYearRepository.retrieveCurrentFY() == null) {
				throw new OpumException(OpumConstants.FISCAL_YEAR_NOT_DEFINED);
			}
			if (ValidationUtils.isValueEmpty(holiday.getDate()) || ValidationUtils.isValueEmpty(holiday.getName())) {
				throw new OpumException(OpumConstants.FILL_HOLIDAY_NAME_AND_OR_HOLIDAY_DATE);
			} else {
				if (ValidationUtils.isDateWithinFiscalYear(holiday.getDate(), pumYearRepository.retrieveCurrentFY())) {
					holidayEngagementRepository.addHolidayEngagement(holiday);
					logger.info("Holiday Added!");

					pumYearRepository.addUpdateHolidayInFiscalYearTemplate(Arrays.asList(holiday), pumYearRepository.retrieveCurrentFY());
					logger.info("Holiday Added in Fiscal Year Template!");

					response = Response.status(Status.OK).entity("Holiday added!").build();
				} else {
					throw new OpumException(OpumConstants.HOLIDAY_DATE_NOT_WITHIN_FISCAL_YEAR);
				}
			}
		} catch (OpumException e) {
			logger.error(e.getMessage());
			response = Response.status(Status.NOT_ACCEPTABLE).entity(e.getMessage()).build();
		} catch (SQLException e) {
			logger.error(e.getMessage());
			response = Response.status(Status.NOT_ACCEPTABLE).entity("ERROR: Unable to add Holiday").build();
		}
		return response;
	}
	
	/**
	 * return boolean
	 * @throws SQLException 
	 */
	public boolean updateHoliday(Holiday holiday) throws SQLException {
		if (holidayEngagementRepository.updateHolidayEngagement(holiday)) {
			holidayEngagementRepository.updateHolidayInFiscalYearTemplate(holiday, pumYearRepository.retrieveCurrentFY());
			return true;
		}
		
		return false;
	}
	
	/**
	 * @param holiday
	 * @return boolean
	 * @throws SQLException
	 */
	public boolean deleteHoliday(Holiday holiday) throws SQLException{
		if (holidayEngagementRepository.deleteHoliday(holiday)) {
			holidayEngagementRepository.deleteHolidayInFiscalYearTemplate((holiday), pumYearRepository.retrieveCurrentFY());
			return true;
		}
		
		return false;
	}
	
	/**
	 * @return List of Holiday
	 * @throws SQLException
	 */
	public List<Holiday> getAllHoliday(PUMYear pumYear) throws SQLException {
		return holidayEngagementRepository.getAllHoliday(pumYear);
	}
	
	/**
	 * @param name
	 * @return Holiday Object
	 * @throws SQLException
	 */
	public Holiday checkHoliday(String name) throws SQLException {
		return holidayEngagementRepository.checkHoliday(name);
	}
}
