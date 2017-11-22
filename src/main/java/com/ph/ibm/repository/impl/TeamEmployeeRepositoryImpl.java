package com.ph.ibm.repository.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import com.ph.ibm.model.TeamEmployee;
import com.ph.ibm.repository.TeamEmployeeRepository;
import com.ph.ibm.resources.ConnectionPool;

public class TeamEmployeeRepositoryImpl implements TeamEmployeeRepository {
	private ConnectionPool connectionPool = ConnectionPool.getInstance();

	@Override
	public boolean addTeamEmployee(List<TeamEmployee> teamEmpList) throws SQLException {
		Connection connection = connectionPool.getConnection();
		PreparedStatement preparedStatement = null;

		connection.setAutoCommit(false);
		String query = "INSERT INTO EMPLOYEE_TEAM (" + "EMPLOYEE_ID,TEAM_ID) " + "VALUES (?,?); ";
		preparedStatement = connection.prepareStatement(query);

		for (TeamEmployee teamEmp : teamEmpList) {
			preparedStatement.setString(1, teamEmp.getEmployeeId());
			preparedStatement.setInt(2, teamEmp.getTeamId());
			preparedStatement.addBatch();
		}

		preparedStatement.executeBatch();
		connection.commit();
		preparedStatement.close();

		return false;
	}

	@Override
	public boolean teamExists(String teamName) {
		return false;
	}

	@Override
	public boolean empExists(String empId) {
		return false;
	}

	@Override
	public boolean updateTeamEmployee(List<TeamEmployee> teamEmpList) {
		return false;
	}
}
