package com.ph.ibm.repository.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.ph.ibm.model.PUMMonth;
import com.ph.ibm.model.PUMQuarter;
import com.ph.ibm.model.PUMYear;
import com.ph.ibm.opum.exception.OpumException;
import com.ph.ibm.repository.PUMYearRepository;
import com.ph.ibm.resources.ConnectionPool;
import com.ph.ibm.util.OpumConstants;

public class PUMYearRepositoryImpl implements PUMYearRepository {
	Logger logger = Logger.getLogger(PUMYearRepositoryImpl.class);

	private ConnectionPool connectionPool = ConnectionPool.getInstance();

	private static final String DATE_FORMAT = "yyyy-MM-dd";

	@Override
	public void saveYear(PUMYear pumYear) throws SQLException, ParseException, OpumException {
		Connection connection = connectionPool.getConnection();
		PreparedStatement preparedStatement = null;
		try {
			checkIfPUMCycleExisting(pumYear);

			DateFormat df = new SimpleDateFormat(DATE_FORMAT);
			connection.setAutoCommit(false);
			String query = "INSERT INTO YEAR (" + "START,END,PUMYEAR) " + "VALUES (?,?,?); ";

			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setDate(1, new Date(df.parse(pumYear.getStart()).getTime()));
			preparedStatement.setDate(2, new Date(df.parse(pumYear.getEnd()).getTime()));
			preparedStatement.setInt(3, pumYear.getPumYear());

			preparedStatement.executeUpdate();
			connection.commit();
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new OpumException(e.getMessage());
		} finally {
			connectionPool.closeConnection(connection, preparedStatement);
		}
	}

	private void checkIfPUMCycleExisting(PUMYear pumYear) throws ParseException, OpumException {
		Connection connection = connectionPool.getConnection();
		ResultSet rs = null;
		PreparedStatement preparedStatement = null;

		try {
			DateFormat df = new SimpleDateFormat(DATE_FORMAT);
			String query = "select count(*) from opum.year where PUMYear = ? and start = ? and end = ?; ";
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, pumYear.getPumYear());
			preparedStatement.setDate(2, new Date(df.parse(pumYear.getStart()).getTime()));
			preparedStatement.setDate(3, new Date(df.parse(pumYear.getEnd()).getTime()));
			rs = preparedStatement.executeQuery();
			if (rs.next()) {
				if (Integer.valueOf(rs.getString(1)) > 0) {
					throw new OpumException("Existing PUM Year, Start Date, and End Date!");
				}
			}
		} catch (SQLException e) {
			logger.error(e.getMessage());
		} finally {
			connectionPool.closeConnection(connection, preparedStatement);
		}
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
}
