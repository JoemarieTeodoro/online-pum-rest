package com.ph.ibm.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.ph.ibm.resources.ConnectionPool;

/**
 * This class is used to authenticate user by providing email and password 
 */
public class Authenticate {

	private ConnectionPool connectionPool = ConnectionPool.getInstance();

	public Authenticate() {
		
	}
	
	/**
	 * This method will check if the user is in the database
	 * 
	 * @param email
	 * @param password
	 * @return boolean
	 * @throws Exception
	 */
	public boolean check(String email, String password) throws Exception {
		String hashed = MD5HashEncrypter.computeMD5Digest(password);
		Connection connection = connectionPool.getConnection();
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			String query = "SELECT EMPLOYEE_ID FROM EMPLOYEE WHERE EMAIL = ? AND PASSWORD = ?";
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, email);
			preparedStatement.setString(2, hashed);
			resultSet = preparedStatement.executeQuery();
			if(resultSet.next())
				return true;
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try { if(resultSet != null) resultSet.close(); } catch(Exception e) { }
			try { if(preparedStatement != null) preparedStatement.close();} catch(Exception e) { }
			try { if(connection != null) connection.close(); } catch(Exception e) { }
		}
		return false;
	}
}
