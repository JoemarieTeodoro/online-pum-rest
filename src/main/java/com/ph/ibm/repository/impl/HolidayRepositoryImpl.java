package com.ph.ibm.repository.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import com.ph.ibm.model.Holiday;
import com.ph.ibm.repository.HolidayEngagementRepository;
import com.ph.ibm.resources.ConnectionPool;
import com.ph.ibm.util.OpumConstants;

public class HolidayRepositoryImpl implements HolidayEngagementRepository {

	private ConnectionPool connectionPool = ConnectionPool.getInstance();

	@Override
	public boolean addHolidayEngagement(Holiday holiday) throws SQLException {
		Connection connection = connectionPool.getConnection();
		PreparedStatement preparedStatement = null;
		try {
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			connection.setAutoCommit(false);
			String query = "INSERT INTO HOLIDAY (" + "NAME,DATE ) " + "VALUES (?,?); ";
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, holiday.getName());
			preparedStatement.setDate(2, new java.sql.Date(df.parse(holiday.getDate()).getTime()));
			preparedStatement.addBatch();
			preparedStatement.executeBatch();
			connection.commit();
			System.out.println(OpumConstants.INSERTED_SUCCESS);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			connectionPool.closeConnection(connection, preparedStatement);
		}
		return false;
	}

	@Override
	public boolean updateHolidayEngagement(Holiday holiday) throws SQLException {
		Connection connection = connectionPool.getConnection();
		PreparedStatement preparedStatement = null;
		try {
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			connection.setAutoCommit(false);
			String query = "UPDATE HOLIDAY SET NAME = ? WHERE DATE = ?;";
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, holiday.getName());
			preparedStatement.setDate(2, new java.sql.Date(df.parse(holiday.getDate()).getTime()));
			preparedStatement.executeUpdate();
			connection.commit();
			System.out.println(OpumConstants.UPDATED_SUCCESS);
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
		try{
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			connection.setAutoCommit(false);
			String query = "DELETE FROM HOLIDAY WHERE NAME = ?;";
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, holiday.getName());
			preparedStatement.executeUpdate();
			connection.commit();
			System.out.println(OpumConstants.DELETED_SUCCESS);
			return true;
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			connectionPool.closeConnection(connection, preparedStatement);
		}
		return false;
	}

	@Override
	public List<Holiday> getAllHoliday() throws SQLException {
		Connection connection = connectionPool.getConnection();
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		List<Holiday> holidays = new ArrayList<Holiday>();
		try {
			String query = "SELECT HOLIDAY_ID, NAME, DATE, CREATEDATE, CREATEDBY, UPDATEDATE, UPDATEDBY FROM HOLIDAY";
			preparedStatement = connection.prepareStatement(query);
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

	

}