package com.ph.ibm.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.ph.ibm.repository.impl.EmployeeRepositoryImpl;
import com.ph.ibm.resources.ConnectionPool;

public class TeamRepositoryImpl implements TeamRepository {
	private Logger logger = Logger.getLogger(EmployeeRepositoryImpl.class);
	private ConnectionPool connectionPool = ConnectionPool.getInstance();

	@Override
	public boolean teamExists(int teamId) {
		boolean exists = false;

		try {
			Connection connection = connectionPool.getConnection();
			PreparedStatement preparedStatement = null;
			ResultSet resultSet = null;
			String query = "SELECT TEAM_ID FROM TEAM WHERE TEAM_ID = ?";
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, teamId);
			resultSet = preparedStatement.executeQuery();
			exists = resultSet.next();
			resultSet.close();
			preparedStatement.close();
		} catch (SQLException e) {
			logger.error(e.getStackTrace());
		}
		return exists;
	}
}
