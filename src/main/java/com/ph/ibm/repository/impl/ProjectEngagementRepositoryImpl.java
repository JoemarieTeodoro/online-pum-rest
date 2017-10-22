package com.ph.ibm.repository.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.ph.ibm.model.ProjectEngagement;
import com.ph.ibm.repository.ProjectEngagementRepository;
import com.ph.ibm.resources.ConnectionPool;
import com.ph.ibm.util.OpumConstants;

public class ProjectEngagementRepositoryImpl implements ProjectEngagementRepository {

	private ConnectionPool connectionPool = ConnectionPool.getInstance();
	
	@Override
	public boolean addProjectEngagement(ProjectEngagement projectEngagement) throws SQLException {
		Connection connection = connectionPool.getConnection();
		PreparedStatement preparedStatement = null;
		String Start = null;
		String End = null;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

		if (projectEngagement.getStartDate() != null) {
			Start = formatter.format(projectEngagement.getStartDate());
		}
		if (projectEngagement.getEndDate() != null) {
			End = "'" + formatter.format(projectEngagement.getEndDate()) + "'";
		}
		try {
			connection.setAutoCommit(false);
			String query = "INSERT INTO PROJECT_ENGAGEMENT (" + "PROJECT_ID, EMPLOYEE_ID, START, END) " + "VALUES (?,?,?,?); ";

			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setLong(1, projectEngagement.getProjectId());
			preparedStatement.setString(2, projectEngagement.getEmployeeId());
			preparedStatement.setDate(3, projectEngagement.getStartDate());
			preparedStatement.setDate(4, projectEngagement.getEndDate());
			preparedStatement.addBatch();
			preparedStatement.executeBatch();
			connection.commit();
			System.out.println(OpumConstants.INSERTED_SUCCESS);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			connectionPool.closeConnection(connection, preparedStatement);
		}
		return false;
	}

	@Override
	public boolean saveDate(ProjectEngagement projectEngagement) throws SQLException {
		Connection connection = connectionPool.getConnection();
		PreparedStatement preparedStatement = null;
		String Start = null;
		String End = null;
		SimpleDateFormat formatter = new SimpleDateFormat("YYYY-MM-DD");

		if (projectEngagement.getStartDate() != null) {
			Start = formatter.format(projectEngagement.getStartDate());
		}
		if (projectEngagement.getEndDate() != null) {
			End = "'" + formatter.format(projectEngagement.getEndDate()) + "'";
		}
		try {
			connection.setAutoCommit(false);
			String query = "UPDATE PROJECT_ENGAGEMENT SET START = ?, END = ?, UPDATEDBY = ? WHERE PROJECT_ENGAGEMENT_ID = ? AND PROJECT_ID = ? AND EMPLOYEE_ID = ?";

			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setDate(1, projectEngagement.getStartDate());
			preparedStatement.setDate(2, projectEngagement.getEndDate());
			preparedStatement.setString(3, OpumConstants.ADMIN);
			preparedStatement.setLong(4, projectEngagement.getProjectEngagementId());
			preparedStatement.setLong(5, projectEngagement.getProjectId());
			preparedStatement.setString(6, projectEngagement.getEmployeeId());
			preparedStatement.executeUpdate();
			connection.commit();
			System.out.println(OpumConstants.UPDATED_SUCCESS);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();

		} finally {
			connectionPool.closeConnection(connection, preparedStatement);
		}
		return false;
	}

	@Override
	public Long getProjectEngagementId(Long projectId, String employeeId) throws SQLException {
		Connection connection = connectionPool.getConnection();
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		Long projectEngagementId = -1L;
		try {
			String query = "SELECT PROJECT_ENGAGEMENT_ID FROM PROJECT_ENGAGEMENT WHERE PROJECT_ID = ? AND EMPLOYEE_ID = ?";
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setLong(1, projectId);
			preparedStatement.setString(2, employeeId);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				projectEngagementId = resultSet.getLong(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			connectionPool.closeConnection(connection, preparedStatement, resultSet);
		}
		return projectEngagementId;
	}

	@Override
	public boolean checkDates(Long projectEngagementId) throws SQLException {
		Connection connection = connectionPool.getConnection();
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			String query = "SELECT START FROM PROJECT_ENGAGEMENT WHERE PROJECT_ENGAGEMENT_ID = ?";
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setLong(1, projectEngagementId);
			resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				projectEngagementId = resultSet.getLong(1);
				return false;
			} else {
				return true;
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			connectionPool.closeConnection(connection, preparedStatement, resultSet);
		}
	}
	
	public List<ProjectEngagement> getAllProjectEngagement() throws SQLException {
		Connection connection = connectionPool.getConnection();
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		List<ProjectEngagement> dates = new ArrayList<ProjectEngagement>();
		try {
			String query = "SELECT PROJECT_ENGAGEMENT_ID, PROJECT_ID, EMPLOYEE_ID, START, END, CREATEDATE, CREATEDBY, UPDATEDATE, UPDATEDBY FROM PROJECT_ENGAGEMENT";
			preparedStatement = connection.prepareStatement(query);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				Long projectEngagementId = resultSet.getLong(1);
				Long projectId = resultSet.getLong(2);
				String employeeId = resultSet.getString(3);
				Date start = resultSet.getDate(4);
				Date end = resultSet.getDate(5);
				String createDate = resultSet.getString(6);
				String createdBy = resultSet.getString(7);
				String updateDate = resultSet.getString(8);
				String updatedBy = resultSet.getString(9);
				ProjectEngagement projectEngagement = new ProjectEngagement(projectEngagementId, projectId, employeeId, start, end, createDate, createdBy, updateDate, updatedBy);
				dates.add(projectEngagement);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			connectionPool.closeConnection(connection, preparedStatement, resultSet);
		}
		return dates;
	}
}