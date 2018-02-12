package com.ph.ibm.repository.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.ph.ibm.repository.MonthRepository;
import com.ph.ibm.resources.ConnectionPool;
import com.ph.ibm.util.SqlQueries;


public class MonthRepositoryImpl implements MonthRepository {

	private ConnectionPool connectionPool = ConnectionPool.getInstance();
	private Logger logger = Logger.getLogger( ProjectRepositoryImpl.class );
	
	@Override
	public String getMonthEndDate(String yearID, int monthID) throws SQLException{
		Connection connection = connectionPool.getConnection();
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		String monthEndDate = "";
		try {
			String query = SqlQueries.SQL_QUERY_GET_MONTH_END_DATE;
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString( 1, yearID );
			preparedStatement.setInt( 2, monthID );
			resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				monthEndDate = resultSet.getString(1);
			}
		} catch (SQLException e) {
			logger.error("Error - getMonthEndDate => " + e.getMessage());
		} finally {
			connectionPool.closeConnection(connection, preparedStatement, resultSet);
		}
		
		return monthEndDate;
	}
	
}
