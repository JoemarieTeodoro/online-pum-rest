package com.ph.ibm.repository.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

import com.ph.ibm.model.Holiday;
import com.ph.ibm.model.PUMMonth;
import com.ph.ibm.model.PUMQuarter;
import com.ph.ibm.model.PUMYear;
import com.ph.ibm.model.Role;
import com.ph.ibm.opum.exception.OpumException;
import com.ph.ibm.repository.PUMYearRepository;
import com.ph.ibm.resources.ConnectionPool;
import com.ph.ibm.util.OpumConstants;

public class PUMYearRepositoryImpl implements PUMYearRepository {
	Logger logger = Logger.getLogger(PUMYearRepositoryImpl.class);

	private ConnectionPool connectionPool = ConnectionPool.getInstance();

	public static final String DATE_FORMAT = "yyyy-MM-dd";

	@Override
	public void saveYear(PUMYear pumYear) throws SQLException, ParseException, OpumException {
		Connection connection = connectionPool.getConnection();
		PreparedStatement preparedStatement = null;
		try {
			DateFormat df = new SimpleDateFormat(DATE_FORMAT);
			connection.setAutoCommit(false);
			String query = "INSERT INTO YEAR (START,END,PUMYEAR,CREATEDBY) VALUES (?,?,?,?); ";

			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setDate(1, new Date(df.parse(pumYear.getStart()).getTime()));
			preparedStatement.setDate(2, new Date(df.parse(pumYear.getEnd()).getTime()));
			preparedStatement.setInt(3, pumYear.getPumYear());
			preparedStatement.setString(4, Role.ADMIN.toString());

			preparedStatement.executeUpdate();
			connection.commit();
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new OpumException(e.getMessage());
		} finally {
			connectionPool.closeConnection(connection, preparedStatement);
		}
	}

	@Override
	public void populateFiscalYear(PUMYear pumYear) {
		Connection connection = connectionPool.getConnection();
		PreparedStatement preparedStatement = null;

		List<String> lstWeekend = Arrays.asList("SATURDAY", "SUNDAY");
		try {
			connection.setAutoCommit(false);
			DateTimeFormatter df = DateTimeFormatter.ofPattern(DATE_FORMAT);
			LocalDate fromDate = LocalDate.parse(pumYear.getStart(), df);
			LocalDate toDate = LocalDate.parse(pumYear.getEnd(), df);
			LocalDateTime counterDateTime = LocalDateTime.of(fromDate, LocalTime.from(LocalTime.MIN));
			LocalDateTime toDateTime = LocalDateTime.of(toDate, LocalTime.from(LocalTime.MIN)).plusDays(1);

			int yearId = retrieveYearDate(pumYear.getPumYear()).getYearId();
			String query = (" INSERT INTO opum.fy_template(YEAR_ID, DATE, VALUE, IS_HOLIDAY, EVENT_NAME) " + " Values (?,?,?,?,?); ");
			preparedStatement = connection.prepareStatement(query);

			while (!counterDateTime.equals(toDateTime)) {
				preparedStatement.setInt(1, yearId);
				preparedStatement.setDate(2, Date.valueOf(counterDateTime.toLocalDate()));
				preparedStatement.setString(3, lstWeekend.contains(counterDateTime.getDayOfWeek().name()) ? "" : OpumConstants.EIGHT);
				preparedStatement.setInt(4, 0); // isHoliday
				preparedStatement.setString(5, ""); // EventName
				preparedStatement.addBatch();
				counterDateTime = counterDateTime.plusDays(1);
			}

			preparedStatement.executeBatch();
			connection.commit();
		} catch (SQLException e) {
			logger.error(e);
		} finally {
			connectionPool.closeConnection(connection, preparedStatement);
		}
	}
	
	@Override
	public void addUpdateHolidayInFiscalYearTemplate(List<Holiday> lstHoliday, PUMYear pumYear) throws OpumException {
		Connection connection = connectionPool.getConnection();
		PreparedStatement preparedStatement = null;

		try {
			if (pumYear != null) {
				for (Holiday holiday : lstHoliday) {
					connection.setAutoCommit(false);
					String query = " UPDATE opum.fy_template SET value = 0, is_holiday = 1, event_name = ? WHERE date = ? AND YEAR_ID = ?; ";
					preparedStatement = connection.prepareStatement(query);
					preparedStatement.setString(1, holiday.getName());
					preparedStatement.setDate(2, Date.valueOf(holiday.getDate()));
					preparedStatement.setInt(3, pumYear.getYearId());
					preparedStatement.addBatch();
				}
			} else {
				throw new OpumException("Fiscal year not found!");
			}
			preparedStatement.executeBatch();
			connection.commit();
		} catch (SQLException e) {
			logger.error(e);
		} catch (OpumException e) {
			throw new OpumException(e.getMessage());
		} finally {
			connectionPool.closeConnection(connection, preparedStatement);
		}
	}

	@Override
	public boolean checkIfPUMCycleExisting(PUMYear pumYear) throws ParseException, OpumException {
		ConnectionPool connectionPool = ConnectionPool.getInstance();
		Connection connection = connectionPool.getConnection();
		ResultSet rs = null;
		PreparedStatement preparedStatement = null;

		try {
			String query = "select count(*) from opum.year where PUMYear = ?; ";
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, pumYear.getPumYear());
			rs = preparedStatement.executeQuery();
			if (rs.next()) {
				if (Integer.valueOf(rs.getString(1)) > 0) {
					return true;
				}
			}
		} catch (SQLException e) {
			logger.error(e.getMessage());
		} finally {
			connectionPool.closeConnection(connection, preparedStatement, rs);
		}

		return false;
	}

	@Override 
	public boolean saveQuarter(PUMQuarter pumQuarter) throws SQLException, ParseException{
		Connection connection = connectionPool.getConnection();
		PreparedStatement preparedStatement = null;
		
		try{
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			connection.setAutoCommit(false);
			String query = "INSERT INTO QUARTER (" + "START,END,PUMQUARTER) " + "VALUES (?,?,?); ";
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setDate(1, new java.sql.Date(df.parse(pumQuarter.getStart()).getTime()));
			preparedStatement.setDate(2, new java.sql.Date(df.parse(pumQuarter.getEnd()).getTime()));
			preparedStatement.setInt(3, pumQuarter.getPumQuarter());
			
			preparedStatement.executeUpdate();
			connection.commit();
			
			System.out.println(OpumConstants.UPDATED_SUCCESS);
			return true;
		} catch (SQLException e){
			e.printStackTrace();
		} finally{
			connectionPool.closeConnection(connection, preparedStatement);
		}
		return false;	
	}
	
	@Override
	public boolean saveMonth(PUMMonth pumMonth) throws SQLException, ParseException{
		Connection connection = connectionPool.getConnection();
		PreparedStatement preparedStatement = null;
		
		try{
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			connection.setAutoCommit(false);
			String query = "INSERT INTO MONTH (" + "START,END,PUMMONTH) " + "VALUES (?,?,?); ";
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setDate(1, new java.sql.Date(df.parse(pumMonth.getStart()).getTime()));
			preparedStatement.setDate(2, new java.sql.Date(df.parse(pumMonth.getEnd()).getTime()));
			preparedStatement.setInt(3, pumMonth.getPumMonth());
			
			preparedStatement.executeUpdate();
			connection.commit();
			
			System.out.println(OpumConstants.UPDATED_SUCCESS);
			
			return true;
		} catch (SQLException e){
			e.printStackTrace();
		} finally {
			connectionPool.closeConnection(connection, preparedStatement);
		}
		return false;		
	}
	
	
	@Override
	public List<PUMYear> retrieveYear() throws SQLException {
		Connection connection = connectionPool.getConnection();
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		List<PUMYear> pumYearList = new ArrayList<PUMYear>();
		try {
			String query = "SELECT YEAR_ID, PUMYEAR, END, START, CREATEDATE, CREATEDBY, UPDATEDATE, UPDATEDBY FROM YEAR;";
			preparedStatement = connection.prepareStatement(query);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				int yearId = resultSet.getInt(1);
				int pumYear = resultSet.getInt(2);
				String end = resultSet.getString(3);
				String start = resultSet.getString(4);
				String createDate = resultSet.getString(5);
				String createdBy = resultSet.getString(6);
				String updateDate = resultSet.getString(7);
				String updatedBy = resultSet.getString(8);
				PUMYear pumYear1 = new PUMYear(yearId, pumYear, end, start, createDate, createdBy, updateDate, updatedBy);
				pumYearList.add(pumYear1);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			connectionPool.closeConnection(connection, preparedStatement, resultSet);
		}
		return pumYearList;
	}

	@Override
	public PUMYear retrieveYearDate(int year) throws SQLException {
		Connection connection = connectionPool.getConnection();
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		PUMYear pumYear = null;
		try {
			String query = "SELECT YEAR_ID, PUMYEAR, END, START, CREATEDATE, CREATEDBY, UPDATEDATE, UPDATEDBY FROM YEAR WHERE PUMYEAR = ?";;
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, year);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				int yearId = resultSet.getInt(1);
				int y = resultSet.getInt(2);
				String end = resultSet.getString(3);
				String start = resultSet.getString(4);
				String createDate = resultSet.getString(5);
				String createdBy = resultSet.getString(6);
				String updateDate = resultSet.getString(7);
				String updatedBy = resultSet.getString(8);
				pumYear = new PUMYear(yearId, y, end, start, createDate, createdBy, updateDate, updatedBy);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			connectionPool.closeConnection(connection, preparedStatement, resultSet);
		}
		return pumYear;
	}

	@Override
	public boolean editYear(PUMYear pumYear) throws SQLException, ParseException {
		Connection connection = connectionPool.getConnection();
		PreparedStatement preparedStatement = null;
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		
		try {
			String query = "UPDATE YEAR SET START= ?, END= ? WHERE PUMYEAR= ?;";
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setDate(1, new java.sql.Date(df.parse(pumYear.getStart()).getTime()));
			preparedStatement.setDate(2, new java.sql.Date(df.parse(pumYear.getEnd()).getTime()));
			preparedStatement.setInt(3, pumYear.getPumYear());
			preparedStatement.executeUpdate();
			connection.commit();
			System.out.println(OpumConstants.UPDATED_SUCCESS);
			preparedStatement.close();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();

		} finally {
			connectionPool.closeConnection(connection, preparedStatement);
		}
		
		return false;
	}

	@Override
	public PUMYear retrieveCurrentFY() {
		Connection connection = connectionPool.getConnection();
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			PUMYear pumYear = new PUMYear();
			String query = "SELECT * FROM YEAR ORDER BY PUMYEAR DESC LIMIT 1";
			preparedStatement = connection.prepareStatement(query);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				pumYear.setCreateDate(resultSet.getString("createDate"));
				pumYear.setCreatedBy(resultSet.getString("createdBy"));
				pumYear.setEnd(resultSet.getString("end"));
				pumYear.setPumYear(resultSet.getInt("pumYear"));
				pumYear.setStart(resultSet.getString("start"));
				pumYear.setUpdateDate(resultSet.getString("updateDate"));
				pumYear.setYearId(resultSet.getInt("year_id"));
			}
			return pumYear;
		}
		catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	@Override
	public void updateFiscalYear(PUMYear pumYear) throws ParseException, OpumException {
		Connection connection = connectionPool.getConnection();
		PreparedStatement preparedStatement = null;

		try {
			connection.setAutoCommit(false);
			DateFormat df = new SimpleDateFormat(DATE_FORMAT);
			String query = " UPDATE opum.year SET start = ?, end = ? WHERE pumyear = ?; ";
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setDate(1, new Date(df.parse(pumYear.getStart()).getTime()));
			preparedStatement.setDate(2, new Date(df.parse(pumYear.getEnd()).getTime()));
			preparedStatement.setInt(3, pumYear.getPumYear());
			preparedStatement.executeUpdate();
			connection.commit();

		} catch (SQLException e) {
			logger.error(e.getMessage());
		} finally {
			connectionPool.closeConnection(connection, preparedStatement);
		}

	}

	@Override
	public void deleteFiscalYearTemplate(PUMYear pumYear) {
		Connection connection = connectionPool.getConnection();
		PreparedStatement preparedStatement = null;

		try {
			connection.setAutoCommit(false);
			String query = " DELETE FROM opum.fy_template WHERE year_id = ?; ";
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, pumYear.getYearId());
			preparedStatement.executeUpdate();
			connection.commit();

		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			connectionPool.closeConnection(connection, preparedStatement);
		}

	}
}
