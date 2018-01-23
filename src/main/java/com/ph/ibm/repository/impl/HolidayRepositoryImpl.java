package com.ph.ibm.repository.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

import com.ph.ibm.model.Holiday;
import com.ph.ibm.model.PUMYear;
import com.ph.ibm.opum.exception.OpumException;
import com.ph.ibm.repository.HolidayEngagementRepository;
import com.ph.ibm.repository.PUMYearRepository;
import com.ph.ibm.resources.ConnectionPool;
import com.ph.ibm.util.FormatUtils;
import com.ph.ibm.util.OpumConstants;
import com.ph.ibm.util.SqlQueries;

public class HolidayRepositoryImpl implements HolidayEngagementRepository {
	Logger logger = Logger.getLogger(HolidayRepositoryImpl.class);

	private ConnectionPool connectionPool = ConnectionPool.getInstance();
	private PUMYearRepository pumYearRepository = new PUMYearRepositoryImpl();

	@Override
	public void addHolidayEngagement(Holiday holiday) throws SQLException, OpumException {
		Connection connection = connectionPool.getConnection();
		PreparedStatement preparedStatement = null;
		try {
			connection.setAutoCommit(false);
			isHolidayExists(holiday);

			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			String query = "INSERT INTO HOLIDAY (" + "NAME, DATE, YEAR_ID ) " + "VALUES (?,?,?); ";
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, holiday.getName());
			preparedStatement.setDate(2, new Date(df.parse(holiday.getDate()).getTime()));
			preparedStatement.setInt(3, pumYearRepository.retrieveCurrentFY().getYearId());
			preparedStatement.executeUpdate();
			connection.commit();
		} catch (ParseException e) {
			logger.error(e.getMessage());
			throw new OpumException("Unable to parse Date!");
		} catch (DateTimeParseException e) {
			logger.error(e.getMessage());
			throw new OpumException("Unable to parse Date!");
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new OpumException(e.getMessage());
		} finally {
			connectionPool.closeConnection(connection, preparedStatement);
		}
	}

	public boolean isHolidayExists(Holiday holiday) throws ParseException, OpumException {
		Connection connection = connectionPool.getConnection();
		ResultSet rs = null;
		PreparedStatement preparedStatement = null;
		boolean isExists = false;
		try {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			String query = "SELECT count(*) FROM opum.holiday WHERE name = ? OR date = ?; ";
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, holiday.getName());
			preparedStatement.setDate(2, new Date(df.parse(holiday.getDate()).getTime()));
			rs = preparedStatement.executeQuery();
			if (rs.next()) {
				if (rs.getInt(1) > 0) {
					isExists = true;
				}
			}
		} catch (SQLException e) {
			logger.error(e.getMessage());
			throw new OpumException(e.getMessage());
		} finally {
			connectionPool.closeConnection(connection, preparedStatement, rs);
		}

		return isExists;
	}

	@Override
	public boolean updateHolidayEngagement(Holiday holiday) throws SQLException {
		Connection connection = connectionPool.getConnection();
		PreparedStatement preparedStatement = null;
		try {
			connection.setAutoCommit(false);
			preparedStatement = connection.prepareStatement(SqlQueries.SQL_UPDATE_HOLIDAY_NAME);
			preparedStatement.setString(1, holiday.getName());
			preparedStatement.setDate(2, Date.valueOf(FormatUtils.toDBDateFormat(holiday.getDate())));
			preparedStatement.setInt(3, pumYearRepository.retrieveCurrentFY().getYearId());
			preparedStatement.executeUpdate();
			connection.commit();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			connectionPool.closeConnection(connection, preparedStatement);
		}
		return false;
	}

	@Override
	public boolean deleteHoliday(Holiday holiday) throws SQLException {
		Connection connection = connectionPool.getConnection();
		PreparedStatement preparedStatement = null;
		try {
			connection.setAutoCommit(false);
			preparedStatement = connection.prepareStatement(SqlQueries.SQL_DELETE_HOLIDAY);
			preparedStatement.setDate(1, Date.valueOf(FormatUtils.toDBDateFormat(holiday.getDate())));
			preparedStatement.setInt(2, pumYearRepository.retrieveCurrentFY().getYearId());
			preparedStatement.executeUpdate();
			connection.commit();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			connectionPool.closeConnection(connection, preparedStatement);
		}
		return false;
	}

	@Override
	public List<Holiday> getAllHoliday(PUMYear pumYear) throws SQLException {
		Connection connection = connectionPool.getConnection();
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		List<Holiday> holidays = new ArrayList<Holiday>();
		try {
			String query = "SELECT HOLIDAY_ID, NAME, DATE, CREATEDATE, CREATEDBY, UPDATEDATE, UPDATEDBY FROM HOLIDAY WHERE YEAR_ID = ? ";
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, pumYear.getYearId());
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				int holiday_Id = resultSet.getInt(1);
				String name = resultSet.getString(2);
				String date = resultSet.getString(3);
				String createDate = resultSet.getString(4);
				String createdBy = resultSet.getString(5);
				String updateDate = resultSet.getString(6);
				String updatedBy = resultSet.getString(7);
				Holiday holiday = new Holiday(holiday_Id, name, date, createDate, createdBy, updateDate, updatedBy);
				holidays.add(holiday);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			connectionPool.closeConnection(connection, preparedStatement, resultSet);
		}
		return holidays;
	}

	@Override
	public Holiday checkHoliday(String name) throws SQLException {
		Connection connection = connectionPool.getConnection();
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		Holiday holiday = null;
		try {
			connection.setAutoCommit(false);
			String query = "SELECT NAME , DATE, HOLIDAY_ID FROM HOLIDAY WHERE NAME = ?";
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, name);
			resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				holiday = new Holiday();
				holiday.setName(resultSet.getString(1));
				holiday.setDate(resultSet.getDate(2) + " ");
				holiday.setHoliday_Id(resultSet.getInt(3));
			}
			return holiday;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			connectionPool.closeConnection(connection, preparedStatement, resultSet);
		}
	}

	@Override
	public void deleteHolidayInFiscalYearTemplate(Holiday holiday, PUMYear retrieveCurrentFY) {
		Connection connection = connectionPool.getConnection();
		PreparedStatement preparedStatement = null;

		List<String> lstWeekend = Arrays.asList( "SATURDAY", "SUNDAY" );
		try {
			connection.setAutoCommit(false);
			preparedStatement = connection.prepareStatement(SqlQueries.SQL_DELETE_HOLIDAY_IN_FY_TEMPLATE);
			LocalDateTime date = LocalDateTime.of( FormatUtils.toDBDateFormat(holiday.getDate()), LocalTime.from( LocalTime.MIN ) );
			if (lstWeekend.contains(date.getDayOfWeek().name())) {
				preparedStatement.setString(1, null);
			} else {
				preparedStatement.setString(1, OpumConstants.EIGHT);
			}
			preparedStatement.setDate(2, Date.valueOf(holiday.getDate()));
			preparedStatement.setInt(3, retrieveCurrentFY.getYearId());
			preparedStatement.executeUpdate();
			connection.commit();
		} catch (SQLException e) {
			logger.error(e);
		} finally {
			connectionPool.closeConnection(connection, preparedStatement);
		}
	}

	@Override
	public void updateHolidayInFiscalYearTemplate(Holiday holiday, PUMYear retrieveCurrentFY) {
		Connection connection = connectionPool.getConnection();
		PreparedStatement preparedStatement = null;
		try {
			connection.setAutoCommit(false);
			preparedStatement = connection.prepareStatement(SqlQueries.SQL_UPDATE_HOLIDAY_IN_FY_TEMPLATE);
			preparedStatement.setString(1, holiday.getName());
			preparedStatement.setDate(2, Date.valueOf(holiday.getDate()));
			preparedStatement.setInt(3, pumYearRepository.retrieveCurrentFY().getYearId());
			preparedStatement.executeUpdate();
			connection.commit();
		} catch (SQLException e) {
			logger.error(e);
		} finally {
			connectionPool.closeConnection(connection, preparedStatement);
		}
		
	}

}