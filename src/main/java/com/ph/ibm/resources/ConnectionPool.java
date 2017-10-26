package com.ph.ibm.resources;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.ph.ibm.util.OpumConstants;

/**
 * This class contains connection to mysql database.
 */
public class ConnectionPool {

	Logger logger = Logger.getLogger(ConnectionPool.class);

	private String dbUrl = "jdbc:mysql://localhost:3306/opum";
	private String userName = "root";
	private String password = "admin";

	private static ConnectionPool connectionPool;

	private ConnectionPool() {}

	static {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static ConnectionPool getInstance() {
		if (connectionPool == null) {
			connectionPool = new ConnectionPool();
		}
		return connectionPool;
	}

	public Connection getConnection() {
		Connection connection = null;
		Properties connectionProps = new Properties();
	    connectionProps.put("user", this.userName);
	    connectionProps.put("password", this.password);
	    connectionProps.put("useSSL", "false");

		try {
			connection = DriverManager.getConnection(dbUrl, connectionProps);
		} catch (SQLException e) {
			logger.error(OpumConstants.UNABLE_TO_ESTABLISH_CONNECTION);
			e.printStackTrace();
		}
		return connection;
	}

	public void closeConnection(Connection connection, PreparedStatement preparedStatement, ResultSet resultSet) {
		try {
			if (resultSet != null) { resultSet.close(); }
			closeConnection(connection, preparedStatement);
		} catch (SQLException e) {

		}
	}

	public void closeConnection(Connection connection, PreparedStatement preparedStatement) {
		try {
			if (preparedStatement != null) { preparedStatement.close(); }
			if (connection != null) { connection.close(); }
		} catch (SQLException e) {

		}
	}


}