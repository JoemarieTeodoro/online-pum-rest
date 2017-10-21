package com.ph.ibm.repository.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ph.ibm.model.Project;
import com.ph.ibm.repository.ProjectRepository;
import com.ph.ibm.resources.ConnectionPool;

public class ProjectRepositoryImpl implements ProjectRepository {

	private ConnectionPool connectionPool = ConnectionPool.getInstance();

	private void closeConnection(Connection connection, PreparedStatement preparedStatement, ResultSet resultSet) {
		try { if (resultSet != null) resultSet.close();} catch (Exception e) {}
		try { if (preparedStatement != null) preparedStatement.close();} catch (Exception e) {}
		try { if (connection != null)connection.close();} catch (Exception e) {}
	}
	
	@Override
	public List<Project> retrieveData() throws SQLException {
		Connection connection = connectionPool.getConnection();
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		List<Project> projects = new ArrayList<Project>();
		try {
			String query = "SELECT PROJECT_ID, NAME, CREATEDATE, CREATEDBY FROM PROJECT;";
			preparedStatement = connection.prepareStatement(query);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				int projectId = resultSet.getInt(1);
				String projectName = resultSet.getString(2);
				String createDate = resultSet.getString(3);
				String createdBy = resultSet.getString(4);
				Project project = new Project(projectId, projectName, createDate, createdBy);
				projects.add(project);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection(connection, preparedStatement, resultSet);
		}
		return projects;
	}
}