package com.ph.ibm.repository.impl;

import static com.ph.ibm.util.ValidationUtils.dateFormat;

import java.sql.BatchUpdateException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.ph.ibm.model.Employee;
import com.ph.ibm.model.EmployeeLeave;
import com.ph.ibm.model.EmployeeUpdate;
import com.ph.ibm.model.ResetPassword;
import com.ph.ibm.model.Role;
import com.ph.ibm.opum.exception.OpumException;
import com.ph.ibm.repository.EmployeeRepository;
import com.ph.ibm.resources.ConnectionPool;
import com.ph.ibm.util.MD5HashEncrypter;
import com.ph.ibm.util.OpumConstants;

public class EmployeeRepositoryImpl implements EmployeeRepository {

    private Logger logger = Logger.getLogger( EmployeeRepositoryImpl.class );

    private ConnectionPool connectionPool = ConnectionPool.getInstance();

    @Override
    public boolean addData( Employee employee ) throws SQLException, BatchUpdateException {
        boolean isAdmin = true;
        boolean isActive = true;
        Connection connection = connectionPool.getConnection();
        PreparedStatement preparedStatement = null;
        try{
            connection.setAutoCommit( false );
            String query = "INSERT INTO EMPLOYEE (" +
                "Employee_ID,Email,Manager_ID, Project_Engagement_ID,FirstName,LastName,MiddleName,IsAdmin,FullName,Password,isActive,Roll_In_Date, Roll_Off_Date, CreateDate,CreatedBy,UpdateDate,UpdatedBy) " +
                "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?); ";
            preparedStatement = connection.prepareStatement( query );
            preparedStatement.setString( 1, employee.getEmployeeSerial() );
            preparedStatement.setString( 2, employee.getIntranetId() );
            preparedStatement.setString( 3, "P2XXXXX" );
            preparedStatement.setString( 4, null );
            preparedStatement.setString( 5, null );
            preparedStatement.setString( 6, null );
            preparedStatement.setString( 7, null );
            preparedStatement.setBoolean( 8, !isAdmin );
            preparedStatement.setString( 9, employee.getFullName() );
            preparedStatement.setString( 10, null );
            preparedStatement.setBoolean( 11, isActive );
            preparedStatement.setString( 12, dateFormat( employee.getRollInDate() ) );
            preparedStatement.setString( 13, dateFormat( employee.getRollOffDate() ) );
            preparedStatement.setString( 14, null );
            preparedStatement.setString( 15, null );
            preparedStatement.setString( 16, null );
            preparedStatement.setString( 17, null );
            preparedStatement.addBatch();
            preparedStatement.executeBatch();
            connection.commit();
            System.out.println( OpumConstants.SUCCESSFULLY_SAVED_DATA );
            preparedStatement.close();
        }
        catch( SQLException e ){
            logger.error( e.getStackTrace() );
        }
        finally{
            try{
                if( preparedStatement != null )
                    preparedStatement.close();
            }
            catch( Exception e ){
            }
            try{
                if( connection != null )
                    connection.close();
            }
            catch( Exception e ){
            }
        }
        return true;
    }

    @Override
    public boolean registerEmployee( Employee employee ) throws SQLException {
        Connection connection = connectionPool.getConnection();
        PreparedStatement preparedStatement = null;
        try{
            connection.setAutoCommit( false );
            String query = "UPDATE EMPLOYEE SET PASSWORD = ?, UPDATEDBY = ? WHERE EMPLOYEE_ID = ? AND EMAIL = ?;";
            preparedStatement = connection.prepareStatement( query );
            preparedStatement.setString( 1, employee.getPassword() );
            preparedStatement.setString( 2, employee.getIntranetId() );
            preparedStatement.setString( 3, employee.getEmployeeSerial() );
            preparedStatement.setString( 4, employee.getEmployeeSerial() );
            preparedStatement.executeUpdate();
            connection.commit();
            logger.info( OpumConstants.UPDATED_SUCCESS );
            return true;
        }
        catch( SQLException e ){
            logger.error( e.getStackTrace() );
        }
        finally{
            connectionPool.closeConnection( connection, preparedStatement );
        }
        return false;
    }

    @Override
    public String viewEmployee( String employeeIdNumber ) throws SQLException {
        Connection connection = connectionPool.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String employeeId = null;
        try{
            String query = "SELECT EMPLOYEE_ID FROM EMPLOYEE WHERE EMPLOYEE_ID = ?";
            preparedStatement = connection.prepareStatement( query );
            preparedStatement.setString( 1, employeeIdNumber );
            resultSet = preparedStatement.executeQuery();
            while( resultSet.next() ){
                employeeId = resultSet.getString( 1 );
            }
            resultSet.close();
            preparedStatement.close();
        }
        catch( SQLException e ){
            logger.error( e.getStackTrace() );
        }
        finally{
            connectionPool.closeConnection( connection, preparedStatement, resultSet );
        }
        return employeeId;
    }

    @Override
    public Employee loginAdmin( String username, String hashed ) throws SQLException {
        Connection connection = connectionPool.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Employee employee = null;
        List<Role> assignedRoles = new ArrayList<Role>();
        try{
            String query = "SELECT EMPLOYEE_ID, EMAIL, FULLNAME FROM EMPLOYEE WHERE EMAIL = ? AND PASSWORD = ?";
            preparedStatement = connection.prepareStatement( query );
            preparedStatement.setString( 1, username );
            preparedStatement.setString( 2, hashed );
            resultSet = preparedStatement.executeQuery();
            if( resultSet.next() ){
                employee = new Employee();
                employee.setEmployeeSerial( resultSet.getString( 1 ) );
                employee.setIntranetId( resultSet.getString( 2 ) );
                employee.setAssignedRoles( getRolesFromEmployeeRole( employee, assignedRoles ) );
                employee.setFullName( resultSet.getString( 3 ) );
            }
        }
        catch( SQLException e ){
            logger.error( e.getStackTrace() );
        }
        finally{
            connectionPool.closeConnection( connection, preparedStatement, resultSet );
        }
        return employee;
    }

    private List<Role> getRolesFromEmployeeRole( Employee employee, List<Role> assignedRoles ) throws SQLException {
        Connection connection = connectionPool.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try{
            String query = "SELECT name FROM employee_role_v WHERE Employee_ID = ? ;";
            preparedStatement = connection.prepareStatement( query );
            preparedStatement.setString( 1, employee.getEmployeeSerial() );
            resultSet = preparedStatement.executeQuery();

            while( resultSet.next() ){
                for( Role role : Role.values() ){
                    if( role.equals( resultSet.getString( 1 ) ) ){
                        assignedRoles.add( role );
                        break;
                    }
                }
            }
        }
        catch( SQLException e ){
            logger.error( e.getStackTrace() );
        }
        finally{
            connectionPool.closeConnection( connection, preparedStatement, resultSet );
        }

        return assignedRoles;
    }

    @Override
    public Employee loginUser( String username, String hashed ) throws SQLException {
        Connection connection = connectionPool.getConnection();
        Employee employee = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try{
            String query =
                "SELECT EMPLOYEE_ID, EMPLOYEE_ID, EMAIL, FULLNAME FROM EMPLOYEE WHERE EMAIL = ? AND PASSWORD = ?";
            preparedStatement = connection.prepareStatement( query );
            preparedStatement.setString( 1, username );
            preparedStatement.setString( 2, hashed );
            resultSet = preparedStatement.executeQuery();
            if( resultSet.next() ){
                employee = new Employee();
                employee.setEmployeeSerial( resultSet.getString( 1 ) );
                employee.setEmployeeSerial( resultSet.getString( 2 ) );
                employee.setIntranetId( resultSet.getString( 3 ) );
                employee.setFullName( resultSet.getString( 4 ) );
            }
            return employee;
        }
        catch( SQLException e ){
            logger.error( e.getStackTrace() );
            return null;
        }
        finally{
            connectionPool.closeConnection( connection, preparedStatement, resultSet );
        }
    }

    @Override
    public boolean doesEmployeeIdExist( String employeeIdNumber ) throws SQLException {
        Connection connection = connectionPool.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try{
            String query = "SELECT EMPLOYEE_ID FROM EMPLOYEE WHERE EMPLOYEE_ID = ?";
            preparedStatement = connection.prepareStatement( query );
            preparedStatement.setString( 1, employeeIdNumber );
            resultSet = preparedStatement.executeQuery();
            return resultSet.next();

        }
        catch( SQLException e ){
            logger.error( e.getStackTrace() );
            return false;
        }
        finally{
            connectionPool.closeConnection( connection, preparedStatement, resultSet );
        }
    }

    @Override
    public boolean doesEmailExist( String email ) throws SQLException {
        Connection connection = connectionPool.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try{
            String query = "SELECT EMAIL FROM EMPLOYEE WHERE EMAIL = ?";
            preparedStatement = connection.prepareStatement( query );
            preparedStatement.setString( 1, email );
            resultSet = preparedStatement.executeQuery();
            return resultSet.next();

        }
        catch( SQLException e ){
            logger.error( e.getStackTrace() );
            return false;
        }
        finally{
            connectionPool.closeConnection( connection, preparedStatement, resultSet );
        }
    }

    @Override
    public EmployeeUpdate searchEmployee( String employeeIdNumber ) throws SQLException {
        Connection connection = connectionPool.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        EmployeeUpdate employee = null;
        try{
            String query =
                "SELECT EMPLOYEE.FULLNAME, EMPLOYEE.EMAIL, PROJECT.NAME, PROJECT_ENGAGEMENT.START, PROJECT_ENGAGEMENT.END, EMPLOYEE.ISACTIVE " +
                    "FROM EMPLOYEE " +
                    "JOIN PROJECT_ENGAGEMENT ON EMPLOYEE.EMPLOYEE_ID=PROJECT_ENGAGEMENT.EMPLOYEE_ID " +
                    "JOIN PROJECT ON PROJECT_ENGAGEMENT.PROJECT_ID=PROJECT.PROJECT_ID " +
                    "WHERE EMPLOYEE.EMPLOYEE_ID = ?";

            preparedStatement = connection.prepareStatement( query );
            preparedStatement.setString( 1, employeeIdNumber );
            resultSet = preparedStatement.executeQuery();

            if( resultSet.next() ){
                employee = new EmployeeUpdate();
                employee.setEmployeeIdNumber( employeeIdNumber );
                employee.setFullName( resultSet.getString( 1 ) );
                employee.setEmail( resultSet.getString( 2 ) );
                employee.setProjectName( resultSet.getString( 3 ) );
                employee.setStartDate( resultSet.getString( 4 ) );
                employee.setEndDate( resultSet.getString( 5 ) );
                employee.setActive( resultSet.getBoolean( 6 ) );
            }
            return employee;
        }
        catch( SQLException e ){
            logger.error( e.getStackTrace() );
            return null;
        }
        finally{
            connectionPool.closeConnection( connection, preparedStatement, resultSet );
        }
    }

    @Override
    public boolean updateEmployee( EmployeeUpdate employeeUpdate ) throws SQLException, BatchUpdateException {
        Connection connection = connectionPool.getConnection();
        PreparedStatement preparedStatement = null;
        try{
            connection.setAutoCommit( false );
            String query = "UPDATE EMPLOYEE " +
                "JOIN PROJECT_ENGAGEMENT ON EMPLOYEE.EMPLOYEE_ID = PROJECT_ENGAGEMENT.EMPLOYEE_ID " +
                "JOIN PROJECT ON PROJECT_ENGAGEMENT.PROJECT_ID = PROJECT.PROJECT_ID " + "SET EMPLOYEE.FULLNAME = ?," +
                "EMPLOYEE.EMAIL = ?," + "PROJECT.NAME = ?, " + "PROJECT_ENGAGEMENT.START = ?," +
                "PROJECT_ENGAGEMENT.END = ?," + "EMPLOYEE.ISACTIVE = ?" + " WHERE EMPLOYEE.EMPLOYEE_ID = ?";

            preparedStatement = connection.prepareStatement( query );
            preparedStatement.setString( 1, employeeUpdate.getFullName() );
            preparedStatement.setString( 2, employeeUpdate.getEmail() );
            preparedStatement.setString( 3, employeeUpdate.getProjectName() );
            preparedStatement.setString( 4, employeeUpdate.getStartDate() );
            preparedStatement.setString( 5, employeeUpdate.getEndDate() );
            preparedStatement.setBoolean( 6, employeeUpdate.isActive() );
            preparedStatement.setString( 7, employeeUpdate.getEmployeeIdNumber() );
            preparedStatement.executeUpdate();
            connection.commit();
            logger.info( OpumConstants.UPDATED_SUCCESS );
            preparedStatement.close();
            return true;
        }
        catch( SQLException e ){
            logger.error( e.getStackTrace() );
        }
        finally{
            connectionPool.closeConnection( connection, preparedStatement );
        }
        return false;
    }

    @Override
	public boolean saveOrUpdate( List<Employee> employees, Role role ) throws Exception {
        Connection connection = connectionPool.getConnection();
        PreparedStatement preparedStatement = null;
        Boolean isActive = true;
        connection.setAutoCommit( false );
        String query = "INSERT INTO EMPLOYEE (" +
            "Employee_ID,Email,Manager_ID, Project_Engagement_ID,FirstName,LastName,MiddleName,FullName,Password,isActive,Roll_In_Date, Roll_Off_Date, CreateDate,CreatedBy,UpdateDate,UpdatedBy) " +
            "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?); ";
        preparedStatement = connection.prepareStatement( query );

        for( Employee employee : employees ){
            preparedStatement.setString( 1, employee.getEmployeeSerial() );
            preparedStatement.setString( 2, employee.getIntranetId() );
            preparedStatement.setString( 3, "P2XXXXX" );
            preparedStatement.setString( 4, null );
            preparedStatement.setString( 5, null );
            preparedStatement.setString( 6, null );
            preparedStatement.setString( 7, null );
            preparedStatement.setString( 8, employee.getFullName() );
            preparedStatement.setString( 9, MD5HashEncrypter.computeMD5Digest( employee.getEmployeeSerial() ) );
            preparedStatement.setBoolean( 10, isActive );
            preparedStatement.setString( 11, dateFormat( employee.getRollInDate() ) );
            preparedStatement.setString( 12, dateFormat( employee.getRollOffDate() ) );
            preparedStatement.setString( 13, employee.getCreateDate() );
            preparedStatement.setString( 14, role.getRoleValue() );
            preparedStatement.setString( 15, employee.getUpdateDate() );
            preparedStatement.setString( 16, employee.getUpdatedBy() );
            preparedStatement.addBatch();
            saveEmployeeRole( employee.getEmployeeSerial(), role );
        }

        preparedStatement.executeBatch();
        connection.commit();
        preparedStatement.close();
        logger.info( OpumConstants.SUCCESSFULLY_SAVED_DATA );

        return false;
    }

    @Override
    public String retrieveSalt( String email ) throws SQLException, OpumException {
        Connection connection = connectionPool.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        logger.info( "email: " + email );
        try{
            String query = "SELECT PASSWORD FROM EMPLOYEE WHERE EMAIL = ?";
            preparedStatement = connection.prepareStatement( query );
            preparedStatement.setString( 1, email );
            resultSet = preparedStatement.executeQuery();

            if( resultSet.next() ){
                return resultSet.getString( 1 );
            }
            else{
                throw new OpumException( "Employee reset password cannot be processed." );
            }

        }
        catch( SQLException e ){
            logger.error( e.getStackTrace() );
            return null;
        }
        finally{
            connectionPool.closeConnection( connection, preparedStatement, resultSet );
        }
    }

    @Override
    public boolean updatePassword( ResetPassword resetPassword ) throws SQLException {
        Connection connection = connectionPool.getConnection();
        PreparedStatement preparedStatement = null;
        try{
            connection.setAutoCommit( false );
            String query = "UPDATE EMPLOYEE " + "SET EMPLOYEE.PASSWORD = ?" + " WHERE EMPLOYEE.EMAIL = ?";

            preparedStatement = connection.prepareStatement( query );
            preparedStatement.setString( 1, resetPassword.getNewPassword() );
            preparedStatement.setString( 2, resetPassword.getEmail() );
            preparedStatement.executeUpdate();
            connection.commit();
            logger.info( OpumConstants.UPDATED_SUCCESS );
            preparedStatement.close();
            return true;
        }
        catch( SQLException e ){
            logger.error( e.getStackTrace() );
        }
        finally{
            connectionPool.closeConnection( connection, preparedStatement );
        }
        return false;
    }

    @Override
    public Employee saveEmployeeRole( String employeeSerial, Role role ) throws BatchUpdateException, SQLException {
        Connection connection = connectionPool.getConnection();
        PreparedStatement preparedStatement = null;
        int roleID;
        if( role.getRoleValue().equalsIgnoreCase( Role.SYS_ADMIN.getRoleValue() ) ){
            roleID = 2;
            //ADMIN
        }
        else{
            roleID = 5;
            //USER
        }

        try{
            connection.setAutoCommit( false );
            String query = "INSERT INTO EMPLOYEE_ROLE (" + "EMPLOYEE_ID,ROLE_ID) " + "VALUES (?,?); ";
            preparedStatement = connection.prepareStatement( query );
            preparedStatement.setString( 1, employeeSerial );
            preparedStatement.setInt( 2, roleID );
            preparedStatement.execute();
            connection.commit();
        }
        catch( SQLException e ){
            logger.error( e.getStackTrace() );
            return null;
        }
        finally{
            connectionPool.closeConnection( connection, preparedStatement );
        }
        return null;
    }

    @Override
    public boolean doesEmployeeRoleIdExist(int roleId) {
    	Connection connection = connectionPool.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try{
            String query = "SELECT ROLE_ID FROM ROLE WHERE ROLE_ID = ?";
            preparedStatement = connection.prepareStatement( query );
            preparedStatement.setInt( 1, roleId );
            resultSet = preparedStatement.executeQuery();
            return resultSet.next();

        }
        catch( SQLException e ){
            logger.error( e.getStackTrace() );
            return false;
        }
    }
    
	@Override
	public List<EmployeeLeave> getEmployeeLeaves(String empId, String currFY) {
		List<EmployeeLeave> empLeaveList = new ArrayList<EmployeeLeave>();
		if (empId == null || empId.isEmpty() || currFY == null || currFY.isEmpty()) {
			return empLeaveList;
		}
		Connection connection = connectionPool.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        
        try{
            CallableStatement cStmt = connection.prepareCall("{call getEmpUtil(?,?)}");
            cStmt.setString(1, empId);
            cStmt.setInt(2, Integer.valueOf(currFY));
            
            resultSet = cStmt.executeQuery();
            
            while(resultSet.next()) {
            	EmployeeLeave empLeave = new EmployeeLeave();
                empLeave.setEmployeeID(resultSet.getString("employeeid"));
                empLeave.setDate(resultSet.getDate("date").toString());
                if (resultSet.getString("event_name") != null && !resultSet.getString("event_name").isEmpty()) {
                	empLeave.setLeaveName(resultSet.getString("event_name"));
                } else {
                	empLeave.setLeaveName(resultSet.getString("hours"));
                }
                empLeave.setHoliday(resultSet.getBoolean("is_holiday"));
                empLeaveList.add(empLeave);
            }
        }
        catch( SQLException e ){
            logger.error(e.getMessage());
        }
        finally{
            connectionPool.closeConnection( connection, preparedStatement, resultSet );
        }
		return empLeaveList;
	}

	@Override
	public boolean saveEmployeeLeave(List<EmployeeLeave> employeeLeave) {
		return false;
	}

}
