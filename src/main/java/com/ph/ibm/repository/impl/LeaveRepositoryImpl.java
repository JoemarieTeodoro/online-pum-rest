package com.ph.ibm.repository.impl;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ph.ibm.model.ForApproval;
import com.ph.ibm.repository.LeaveRepository;
import com.ph.ibm.resources.ConnectionPool;

public class LeaveRepositoryImpl implements LeaveRepository {

	private ConnectionPool connectionPool = ConnectionPool.getInstance();
	
	@Override
	public List<ForApproval> getAllForApproval() throws SQLException {
		Connection connection = connectionPool.getConnection();
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		List<ForApproval> forApprovals = new ArrayList<ForApproval>();
		try {
			String query = "SELECT Employee_Leave_ID, Employee_ID, fullName, Year_ID, Status, Leave_Date, Leave_Type, CreateDate, UpdateDate, Team_Lead_Employee_ID FROM employee_leave_v where Status = 'Pending'";
			preparedStatement = connection.prepareStatement(query);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				int employee_Leave_Id = resultSet.getInt(1);
				String employee_Id = resultSet.getString(2);
				String fullName = resultSet.getString(3);
				String year_Id = resultSet.getString(4);
				String status = resultSet.getString(5);
				String leave_Date = resultSet.getString(6);
				String leave_Type = resultSet.getString(7);
				String create_Date = resultSet.getString(8);
				String update_Date = resultSet.getString(9);
				String team_Lead_Employee_Id = resultSet.getString(10);		
				ForApproval forApproval = new ForApproval(employee_Leave_Id, employee_Id, fullName, year_Id, status, leave_Date, leave_Type, create_Date,update_Date,team_Lead_Employee_Id);
				forApprovals.add(forApproval);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			connectionPool.closeConnection(connection, preparedStatement, resultSet);
		}
		return forApprovals;
	}
}
