package com.ph.ibm.repository.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ph.ibm.model.Utilization;
import com.ph.ibm.repository.UtilizationEngagementRepository;
import com.ph.ibm.resources.ConnectionPool;
import com.ph.ibm.util.OpumConstants;

/**
 * This class implements methods that is used to insert and view from
 * utilization
 */
public class UtilizationEngagementRepositoryImpl implements UtilizationEngagementRepository {

	private ConnectionPool connectionPool = ConnectionPool.getInstance();

	@Override
	public boolean saveUtilization(Utilization utilization) throws SQLException {
		Connection connection = connectionPool.getConnection();
		PreparedStatement preparedStatement = null;
		try {
			connection.setAutoCommit(false);
			Utilization util = downloadUtilization(utilization.getYear(), utilization.getEmployeeSerial());
			if(util == null)
			{
				String query = 
				"INSERT INTO UTILIZATION (" + "EMPLOYEE_ID, YEAR, UTILIZATION_JSON, CREATEDBY, UPDATEDBY) " + "VALUES (?,?,?,?,?); ";
				preparedStatement = connection.prepareStatement(query);
				preparedStatement.setString(1, utilization.getEmployeeSerial());
				preparedStatement.setString(2, utilization.getYear());
				preparedStatement.setString(3, utilization.getUtilizationJson());
				preparedStatement.setString(4, utilization.getEmployeeSerial());
				preparedStatement.setString(5, utilization.getEmployeeSerial());
				preparedStatement.addBatch();
				preparedStatement.executeBatch();
				connection.commit();
				System.out.println(OpumConstants.INSERTED_SUCCESS);
			}
			else
			{
				String query = "UPDATE UTILIZATION SET UTILIZATION_JSON = ?, UPDATEDBY = ? WHERE EMPLOYEE_ID = ? AND YEAR = ?";
				preparedStatement = connection.prepareStatement(query);
				preparedStatement.setString(1, utilization.getUtilizationJson());
				preparedStatement.setString(2, utilization.getEmployeeSerial());
				preparedStatement.setString(3, utilization.getEmployeeSerial());
				preparedStatement.setString(4, utilization.getYear());
				preparedStatement.executeUpdate();
				connection.commit();
				System.out.println(OpumConstants.UPDATED_SUCCESS);
			}
			return true;

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			connectionPool.closeConnection(connection, preparedStatement);
		}
		return false;
	}

	@Override
	public List<Utilization> retrieveUtilizations(String employeeIdNumber, String year) throws SQLException {
		Connection connection = connectionPool.getConnection();
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		List<Utilization> utilizations = new ArrayList<Utilization>();
		try {
			String query = "SELECT EMPLOYEE_ID, YEAR, UTILIZATION_JSON FROM UTILIZATION WHERE EMPLOYEE_ID = ? AND YEAR = ?";
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, employeeIdNumber);
			preparedStatement.setString(2, year);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				String employeeSerial = resultSet.getString(1);
				String utilizationYear = resultSet.getString(2);
				String utilizationJSON = resultSet.getString(3);
				Utilization utilization = new Utilization(employeeSerial, utilizationYear, utilizationJSON);
				utilizations.add(utilization);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			connectionPool.closeConnection(connection, preparedStatement, resultSet);
		}
		return utilizations;
	}

	@Override
	public Utilization downloadUtilization(String year, String employeeId) throws SQLException{
		Connection connection = connectionPool.getConnection();
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		Utilization utilization = null;
		try{
			String query =  
					"SELECT UTILIZATION.YEAR, UTILIZATION.UTILIZATION_JSON, EMPLOYEE.EMPLOYEE_ID FROM UTILIZATION "
					+ "JOIN EMPLOYEE ON UTILIZATION.EMPLOYEE_ID=EMPLOYEE.EMPLOYEE_ID "
					+ "WHERE UTILIZATION.YEAR = ? AND UTILIZATION.EMPLOYEE_ID =?";
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, year);
			preparedStatement.setString(2, employeeId);
			
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				String utilizationYear = resultSet.getString(1);
				String utilizationJSON = resultSet.getString(2);
				String employeeSerial = resultSet.getString(3);
				utilization = new Utilization(employeeSerial, utilizationYear, utilizationJSON);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			connectionPool.closeConnection(connection, preparedStatement, resultSet);
		}
		return utilization;
	}

	@Override
	public Utilization getComputation(String employeeSerial, int year) throws SQLException {
		Connection connection = connectionPool.getConnection();
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		Utilization utilization = null;
		try {
			String query = "SELECT EMPLOYEE_ID, YEAR, UTILIZATION_JSON FROM UTILIZATION WHERE YEAR = ? AND EMPLOYEE_ID = ?";
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, year);
			preparedStatement.setString(2, employeeSerial);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				String employeeIdNumber = resultSet.getString(1);
				String utilization_Year = resultSet.getString(2);
				String utilization_JSON = resultSet.getString(3);
				utilization = new Utilization(employeeIdNumber, utilization_Year, utilization_JSON);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			connectionPool.closeConnection(connection, preparedStatement, resultSet);
		}
		return utilization;
	}

	
}
