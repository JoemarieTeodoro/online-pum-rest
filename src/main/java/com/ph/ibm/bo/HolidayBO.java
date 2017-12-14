package com.ph.ibm.bo;

import java.sql.SQLException;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;

import com.ph.ibm.model.Holiday;
import com.ph.ibm.opum.exception.OpumException;
import com.ph.ibm.repository.HolidayEngagementRepository;
import com.ph.ibm.repository.PUMYearRepository;
import com.ph.ibm.repository.impl.HolidayRepositoryImpl;
import com.ph.ibm.repository.impl.PUMYearRepositoryImpl;
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
			if (ValidationUtils.isValueEmpty(holiday.getDate()) || ValidationUtils.isValueEmpty(holiday.getName())) {
				throw new OpumException("Please fill Holiday Name and/or Holiday Date");
			} else {
				holidayEngagementRepository.addHolidayEngagement(holiday);
				logger.info("Holiday Added!");
				pumYearRepository.addUpdateHolidayInFiscalYearTemplate(holiday, pumYearRepository.retrieveCurrentFY());
				logger.info("Holiday Added in Fiscal Year Template!");
				response = Response.status(Status.OK).entity("Holiday added!").build();
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
		return holidayEngagementRepository.updateHolidayEngagement(holiday);
	}
	
	/**
	 * @param holiday
	 * @return boolean
	 * @throws SQLException
	 */
	public boolean deleteHoliday(Holiday holiday) throws SQLException {
		return holidayEngagementRepository.deleteHoliday(holiday);
	}
	
	/**
	 * @return List of Holiday
	 * @throws SQLException
	 */
	public List<Holiday> getAllHoliday() throws SQLException {
		return holidayEngagementRepository.getAllHoliday();
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
