package com.ph.ibm.repository.impl;

import static com.ph.ibm.util.ValidationUtils.dateFormat;

import java.sql.BatchUpdateException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.mysql.jdbc.Statement;
import com.ph.ibm.model.Employee;
import com.ph.ibm.model.EmployeeLeave;
import com.ph.ibm.model.EmployeeUpdate;
import com.ph.ibm.model.Holiday;
import com.ph.ibm.model.ResetPassword;
import com.ph.ibm.model.Role;
import com.ph.ibm.opum.exception.OpumException;
import com.ph.ibm.repository.EmployeeRepository;
import com.ph.ibm.repository.PUMYearRepository;
import com.ph.ibm.repository.TeamEmployeeRepository;
import com.ph.ibm.resources.ConnectionPool;
import com.ph.ibm.util.MD5HashEncrypter;
import com.ph.ibm.util.OpumConstants;
import com.ph.ibm.util.UploaderUtils;

public class EmployeeRepositoryImpl implements EmployeeRepository {

    private Logger logger = Logger.getLogger( EmployeeRepositoryImpl.class );

    private ConnectionPool connectionPool = ConnectionPool.getInstance();

    private TeamEmployeeRepository teamEmployeeRepository = new TeamEmployeeRepositoryImpl();

    PUMYearRepository pumYearRepository = new PUMYearRepositoryImpl();

    public static final String LEAVE_DEFAULT_VALUE = "8";
    public static final String LEAVE_STATUS_REMOVED = "Removed";
    public static final String LEAVE_HO = "HO";
    public static final String LEAVE_VL = "VL";
    public static final String LEAVE_OL = "OL";
    public static final String LEAVE_RC = "RC";
    public static final String LEAVEID_Zero = "0";
    public static final String LEAVE_STATUS_DRAFT = "draft";
    public static final String LEAVE_STATUS_APPROVED = "Approved";
	
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
    public ArrayList<String> getEmployeeRollDates( String employeeIdNumber ) throws SQLException {
        Connection connection = connectionPool.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        ArrayList<String> rollDates = new ArrayList<String>();
        try{
            String query = "SELECT ROLL_IN_DATE, ROLL_OFF_DATE FROM EMPLOYEE WHERE EMPLOYEE_ID = ?";
            preparedStatement = connection.prepareStatement( query );
            preparedStatement.setString( 1, employeeIdNumber );
            resultSet = preparedStatement.executeQuery();
            if( resultSet.next() ){
                rollDates.add( resultSet.getString( 1 ) );
                rollDates.add( resultSet.getString( 2 ) );
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
        return rollDates;
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
                employee.setIsTeamLead(getIsTeamLead(employee));
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

    private boolean getIsTeamLead(Employee employee) throws SQLException {
    	boolean isTeamLead = false;
    	Connection connection = connectionPool.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
    	try {
    		String query = "select * from team where Team_Lead_Employee_ID = ? ;";
            preparedStatement = connection.prepareStatement( query );
            preparedStatement.setString( 1, employee.getEmployeeSerial() );
            resultSet = preparedStatement.executeQuery();

            if(resultSet.first()) {
            	isTeamLead = true;
            }
    	}
    	catch( SQLException e ){
            logger.error( e.getStackTrace() );
        }
        finally{
            connectionPool.closeConnection( connection, preparedStatement, resultSet );
        }

    	return isTeamLead;
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
                "SELECT EMPLOYEE_ID, FULLNAME, EMAIL, PROJECT_ENGAGEMENT_ID, ROLL_IN_DATE, ROLL_OFF_DATE FROM EMPLOYEE WHERE EMPLOYEE_ID = ? AND EMP_STATUS='A'";
            preparedStatement = connection.prepareStatement( query );
            preparedStatement.setString( 1, employeeIdNumber );
            resultSet = preparedStatement.executeQuery();

            if( resultSet.next() ){
                employee = new EmployeeUpdate();
                employee.setEmployeeIdNumber( resultSet.getString( 1 ) );
                employee.setFullName( resultSet.getString( 2 ) );
                employee.setEmail( resultSet.getString( 3 ) );
                employee.setProjectName(
                    teamEmployeeRepository.retrieveActiveTeamAssignment( employee.getEmployeeIdNumber() ) );
                employee.setStartDate( resultSet.getString( 5 ) );
                employee.setEndDate( resultSet.getString( 6 ) );
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

    public Employee getEmployee( String email ) throws SQLException {
        Connection connection = connectionPool.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Employee employee = null;
        try{
            String query =
                "SELECT EMPLOYEE_ID, FULLNAME, PASSWORD, EMAIL, ROLL_IN_DATE, ROLL_OFF_DATE FROM EMPLOYEE WHERE EMAIL = ? AND EMP_STATUS='A'";
            preparedStatement = connection.prepareStatement( query );
            preparedStatement.setString( 1, email );
            resultSet = preparedStatement.executeQuery();

            if( resultSet.next() ){
                employee = new Employee();
                employee.setEmployeeSerial( resultSet.getString( 1 ) );
                employee.setFullName( resultSet.getString( 2 ) );
                employee.setPassword( resultSet.getString( 3 ) );
                employee.setIntranetId( resultSet.getString( 4 ) );
                employee.setRollInDate( resultSet.getString( 5 ) );
                employee.setRollOffDate( resultSet.getString( 6 ) );
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
        	updateEmployeeStatus(employeeUpdate.getEmployeeIdNumber());        	
            addUpdatedEmployee(employeeUpdate);
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
        String empStatus = "A";
        connection.setAutoCommit( false );
        String query = "INSERT INTO EMPLOYEE (" +
            "Employee_ID,Email,Manager_ID, Project_Engagement_ID,FirstName,LastName,MiddleName,FullName,Password,Emp_Status,isActive,Roll_In_Date, Roll_Off_Date, CreateDate,CreatedBy,UpdateDate,UpdatedBy) " +
            "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?); ";
        preparedStatement = connection.prepareStatement( query );

        for( Employee employee : employees ){
            preparedStatement.setString( 1, employee.getEmployeeSerial() );
            preparedStatement.setString( 2, employee.getIntranetId() );
            preparedStatement.setString( 3, employee.getManagerSerial() );
            preparedStatement.setString( 4, null );
            preparedStatement.setString( 5, null );
            preparedStatement.setString( 6, null );
            preparedStatement.setString( 7, null );
            preparedStatement.setString( 8, employee.getFullName() );
            if( getEmployeeCount( employee.getEmployeeSerial() ) > 0 ){
                preparedStatement.setString( 9, retrieveRecentPassword( employee.getEmployeeSerial() ) );
            }
            else{
                employee.setPassword( MD5HashEncrypter.computeMD5Digest( employee.getEmployeeSerial() ) );
                preparedStatement.setString( 9, employee.getPassword() );
                UploaderUtils.sendEmailToRecipients( employee );
                pumYearRepository.populateUtilization( employee.getEmployeeSerial() );
            }
            preparedStatement.setString( 10, empStatus );
            preparedStatement.setBoolean( 11, isActive );
            preparedStatement.setString( 12, dateFormat( employee.getRollInDate() ) );
            preparedStatement.setString( 13, dateFormat( employee.getRollOffDate() ) );
            preparedStatement.setString( 14, employee.getCreateDate() );
            preparedStatement.setString( 15, role.getRoleValue() );
            preparedStatement.setString( 16, employee.getUpdateDate() );
            preparedStatement.setString( 17, employee.getUpdatedBy() );
            preparedStatement.addBatch();
            saveEmployeeRole( employee.getEmployeeSerial(), role );
            updateEmployeeStatus( employee.getEmployeeSerial() );
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
    public boolean doesEmployeeRoleIdExist( int roleId ) {
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
    public List<EmployeeLeave> getEmployeeLeaves( String empId, String currFY ) {
        List<EmployeeLeave> empLeaveList = new ArrayList<EmployeeLeave>();
        if( empId == null || empId.isEmpty() || currFY == null || currFY.isEmpty() ){
            return empLeaveList;
        }
        Connection connection = connectionPool.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try{
            CallableStatement cStmt = connection.prepareCall( "{call getEmpUtil(?,?)}" );
            cStmt.setString( 1, empId );
            cStmt.setInt( 2, Integer.valueOf( currFY ) );

            resultSet = cStmt.executeQuery();
            while(resultSet.next()) {
            	EmployeeLeave empLeave = new EmployeeLeave();
                empLeave.setEmployeeID(resultSet.getString("employeeid"));
                empLeave.setStatus(resultSet.getString("status"));
                empLeave.setDate(resultSet.getDate("date").toString());
                empLeave.setYearID(String.valueOf(resultSet.getInt("year_id")));
                empLeave.setEmployeeLeaveID(String.valueOf(resultSet.getInt("employee_leave_id")));
                empLeave.setHoliday(resultSet.getBoolean("is_holiday"));
                String hours = resultSet.getString("hours");
				if (resultSet.getString("event_name") != null && !resultSet.getString("event_name").isEmpty()
						&& !resultSet.getString("event_name").equalsIgnoreCase("RC")) {
					empLeave.setLeaveName(resultSet.getString("event_name"));
                	/**
                	 *  set status to pending since this leave is not yet approved
                	 */
                	
                } else {
                	empLeave.setLeaveName(resultSet.getString("hours"));
                }
                empLeave.setHoliday( resultSet.getBoolean( "is_holiday" ) );
                empLeaveList.add( empLeave );
            }
        }
        catch( SQLException e ){
            logger.error( e.getMessage() );
        }
        finally{
            connectionPool.closeConnection( connection, preparedStatement, resultSet );
        }
        return empLeaveList;
    }

    /**
     * @param serialNumber
     * @return
     * @throws SQLException
     * @see com.ph.ibm.repository.EmployeeRepository#updateEmployeeStatus(java.lang.String)
     */
    @Override
    public boolean updateEmployeeStatus( String serialNumber ) throws SQLException {
        Connection connection = connectionPool.getConnection();
        PreparedStatement preparedStatement = null;
        try{
            connection.setAutoCommit( false );
            String query = "UPDATE EMPLOYEE SET EMP_STATUS = 'I' WHERE EMPLOYEE_ID = ? AND EMP_STATUS = 'A';";
            preparedStatement = connection.prepareStatement( query );
            preparedStatement.setString( 1, serialNumber );
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

    /**
     * @param serialNumber
     * @return
     * @throws SQLException
     * @see com.ph.ibm.repository.EmployeeRepository#getEmployeeCount(java.lang.String)
     */
    @Override
    public int getEmployeeCount( String serialNumber ) throws SQLException {
        Connection connection = connectionPool.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int count = 0;
        String query = "SELECT COUNT(EMPLOYEE_ID) FROM EMPLOYEE WHERE EMPLOYEE_ID = ?";
        try{
            preparedStatement = connection.prepareStatement( query );
            preparedStatement.setString( 1, serialNumber );
            resultSet = preparedStatement.executeQuery();
            if( resultSet.next() ){
                count = resultSet.getInt( 1 );
            }
        }
        catch( SQLException e ){
            logger.error( e.getStackTrace() );

        }
        finally{
            connectionPool.closeConnection( connection, preparedStatement, resultSet );
        }
        return count;

    }

    /**
     * @param serialNumber
     * @return
     * @throws SQLException
     * @see com.ph.ibm.repository.EmployeeRepository#retrieveRecentPassword(java.lang.String)
     */
    @Override
    public String retrieveRecentPassword( String serialNumber ) throws SQLException {
        Connection connection = connectionPool.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String password = null;
        String query = "SELECT PASSWORD FROM EMPLOYEE WHERE EMPLOYEE_ID = ? AND EMP_STATUS = 'A'";
        try{

            preparedStatement = connection.prepareStatement( query );
            preparedStatement.setString( 1, serialNumber );
            resultSet = preparedStatement.executeQuery();
            if( resultSet.next() ){
                password = resultSet.getString( 1 );

            }

        }
        catch( SQLException e ){
            logger.error( e.getStackTrace() );
            return null;
        }
        finally{
            connectionPool.closeConnection( connection, preparedStatement, resultSet );
        }

        return password;
    }

	@Override
	public List<String> getAdminEmailList() throws SQLException {
		Connection connection = connectionPool.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<String> listOfAdmins = new ArrayList<>();
        try{
            String query =
                "SELECT emp.Email FROM employee_role_v emprole " +
                "INNER JOIN employee emp ON emp.Employee_ID = emprole.Employee_ID " +
                "WHERE emprole.name = 'Administrator'";

            preparedStatement = connection.prepareStatement( query );
            resultSet = preparedStatement.executeQuery();

            if( resultSet.next() ){
            	listOfAdmins.add(resultSet.getString(1));
            }
            return listOfAdmins;
        }
        catch( SQLException e ){
            logger.error( e.getStackTrace() );
            return null;
        }
        finally{
            connectionPool.closeConnection( connection, preparedStatement, resultSet );
        }
	}
	
	public boolean addUpdatedEmployee(EmployeeUpdate employeeUpdate) throws SQLException, BatchUpdateException {
        String empPassword = retrieveRecentPassword(employeeUpdate.getEmployeeIdNumber());
        boolean isActive = true;
        String empStatus = "A";
        Connection connection = connectionPool.getConnection();
        PreparedStatement preparedStatement = null;
        try{
            connection.setAutoCommit( false );
            String query = "INSERT INTO EMPLOYEE (" +
                "Employee_ID,Email,Manager_ID, Project_Engagement_ID,FirstName,LastName,MiddleName,FullName,Password,Emp_Status,isActive,Roll_In_Date, Roll_Off_Date,UpdateDate,UpdatedBy) " +
                "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?); ";
            preparedStatement = connection.prepareStatement( query );
            preparedStatement.setString( 1, employeeUpdate.getEmployeeIdNumber() );
            preparedStatement.setString( 2, employeeUpdate.getEmail() );
            preparedStatement.setString( 3, employeeUpdate.getManagerSerial());
            preparedStatement.setString( 4, null );
            preparedStatement.setString( 5, employeeUpdate.getFirstName());
            preparedStatement.setString( 6, employeeUpdate.getLastName() );
            preparedStatement.setString( 7, employeeUpdate.getMiddleName() );
            preparedStatement.setString( 8, employeeUpdate.getFullName() );
            preparedStatement.setString( 9, empPassword );
            preparedStatement.setString( 10, empStatus );
            preparedStatement.setBoolean( 11, isActive );
            preparedStatement.setString( 12, employeeUpdate.getStartDate() );
            preparedStatement.setString( 13, employeeUpdate.getEndDate() );
            preparedStatement.setString( 14, new Timestamp(System.currentTimeMillis()).toString());
            preparedStatement.setString( 15, Role.ADMIN.getRoleValue() );
            preparedStatement.executeUpdate();
            connection.commit();
            logger.info( OpumConstants.SUCCESSFULLY_SAVED_UPDATED_DATA );
            preparedStatement.close();
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
	public boolean saveEmployeeLeave(List<EmployeeLeave> employeeLeaveList, boolean isDraft, String empID, String fyID)
			throws SQLException {
		Connection connection = connectionPool.getConnection();
		PreparedStatement preparedStatement = null;
		connection.setAutoCommit(false);

		
		
		try {
			String query_Core = "INSERT INTO EMPLOYEE_LEAVE (Employee_ID,Year_ID,Status,"
					+ "Leave_Date,Leave_Type,CreateDate,UpdateDate,Hours) VALUES(?,?,?,?,?,?,?,?);";
			preparedStatement = connection.prepareStatement(query_Core, Statement.RETURN_GENERATED_KEYS);

			if (employeeLeaveList.size() > 0) {
				for (EmployeeLeave emp : employeeLeaveList) {
					
					int empLeaveID = 0;
					if (emp.getEmployeeLeaveID()==null
							|| emp.getEmployeeLeaveID().equals(LEAVEID_Zero)) {

						boolean isHoliday = isValidHoliday(emp.getLeaveName(),
								emp.getDate());

						preparedStatement.setString(1, emp.getEmployeeID());
						preparedStatement.setInt(2, Integer.valueOf(emp.getYearID()));

						if (isDraft) {
							preparedStatement.setString(3, "draft");
						} else {
							preparedStatement.setString(3, emp.getStatus());
						}
						preparedStatement.setDate(4, Date.valueOf(emp.getDate()));
						preparedStatement.setString(5, emp.getLeaveName());
						preparedStatement.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
						preparedStatement.setTimestamp(7, new Timestamp(System.currentTimeMillis()));
						preparedStatement.setInt(8, emp.getValue());

						if ((emp.getLeaveName().equalsIgnoreCase(LEAVE_HO) && isHoliday)
								|| emp.getLeaveName().equalsIgnoreCase(LEAVE_VL)
								|| emp.getLeaveName().equalsIgnoreCase(LEAVE_OL)
								|| StringUtils.isNumeric(emp.getLeaveName())) {

							preparedStatement.addBatch();
							preparedStatement.executeUpdate();
							connection.commit();
							ResultSet rs = preparedStatement.getGeneratedKeys();
							if (rs.next()) {
								empLeaveID = rs.getInt(1);
							}
						}

					} else {
						updateEmployeeLeave(emp, isDraft);
					}

					/**
					 * Save employee leave to history table
					 */
					if (!isDraft) {
						saveEmployeeLeaveHistory(emp, empLeaveID);
					}
				}
				preparedStatement.close();
			} else {
				if (!isDraft) {
					updateEmployeeLeaveStatus(empID, fyID);
				}
			}

			return true;
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return false;
	}

	@Override
	public boolean updateEmployeeLeave(EmployeeLeave emp, boolean isDraft) throws SQLException {
		boolean isSuccess = false;
		Connection connection = connectionPool.getConnection();
		PreparedStatement preparedStatement = null;
		connection.setAutoCommit(false);
		
		try {
			String query = "UPDATE EMPLOYEE_LEAVE SET LEAVE_DATE=?, LEAVE_TYPE=?, STATUS=?, HOURS=? "
					+ "WHERE EMPLOYEE_LEAVE_ID=?";
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setDate(1, Date.valueOf(emp.getDate()));
			preparedStatement.setString(2, emp.getLeaveName());

			/*if (!isDraft && emp.getStatus().equalsIgnoreCase(leaveStatusDraft)) {
				preparedStatement.setString(3, leaveStatusApproved);
				emp.setStatus(leaveStatusApproved);
			}*/
			if(isDraft) {
				preparedStatement.setString(3, LEAVE_STATUS_DRAFT);
				emp.setStatus(LEAVE_STATUS_DRAFT);
			}
			preparedStatement.setString(3, emp.getStatus());
			
			
			if (emp.getLeaveName().equalsIgnoreCase(LEAVE_DEFAULT_VALUE)) {
				preparedStatement.setString(3, LEAVE_STATUS_REMOVED);
				
				preparedStatement.setString(2, LEAVE_VL);

				emp.setStatus(LEAVE_STATUS_REMOVED);
				emp.setLeaveName(LEAVE_VL);
			}
			
			if (emp.getLeaveName().equalsIgnoreCase(LEAVE_HO)) {
				if (isValidHoliday(emp.getLeaveName(), emp.getDate()))
					preparedStatement.setString(3, LEAVE_STATUS_REMOVED);
				
				preparedStatement.setString(2, LEAVE_RC);

				emp.setStatus(LEAVE_STATUS_REMOVED);
				emp.setLeaveName(LEAVE_RC);
			}
			preparedStatement.setInt(4, emp.getValue());
			preparedStatement.setInt(5, Integer.valueOf(emp.getEmployeeLeaveID()));
			preparedStatement.executeUpdate();
			connection.commit();

			saveEmployeeLeaveHistory(emp, Integer.valueOf(emp.getEmployeeLeaveID()));

			isSuccess = true;
		} catch (Exception e) {
			logger.info(e.getMessage());
		}
		return isSuccess;
	}
	
	public boolean isValidHoliday(String currLeaveStatus, String leaveDate) throws OpumException, ParseException {
		boolean isExist = false;
		HolidayRepositoryImpl holidayRepo = new HolidayRepositoryImpl();
		Holiday holiday = new Holiday();
		holiday.setDate(leaveDate);
		if (holidayRepo.isHolidayExists(holiday)) {

			isExist = true;
		}

		return isExist;
	}

	@Override
	public boolean updateEmployeeLeaveStatus(String empID, String fyID) throws SQLException {

		boolean isSuccess = false;
		Connection connection = connectionPool.getConnection();
		PreparedStatement preparedStatement = null;
		connection.setAutoCommit(false);
		empID = empID.replace("\'", "");
		try {
			String query = "UPDATE EMPLOYEE_LEAVE SET STATUS=? WHERE EMPLOYEE_ID=? and YEAR_ID=?";
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, "Approved");
			preparedStatement.setString(2, empID);
			preparedStatement.setInt(3, 1);
			preparedStatement.executeUpdate();
			connection.commit();
			
			isSuccess = true;
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return isSuccess;
	}

	@Override
	public boolean saveEmployeeLeaveHistory(EmployeeLeave emp, int empLeaveID) throws SQLException {
		Connection connection = connectionPool.getConnection();
		PreparedStatement preparedStatement = null;
		connection.setAutoCommit(false);

		try {
			String query_Core = "INSERT INTO EMPLOYEE_LEAVE_HISTORY (Employee_ID,Year_ID,Status,"
					+ "Leave_Date,Leave_Type,CreateDate,UpdateDate, Employee_Leave_ID) VALUES(?,?,?,?,?,?,?,?);";
			preparedStatement = connection.prepareStatement(query_Core);

			preparedStatement.setString(1, emp.getEmployeeID());
			preparedStatement.setInt(2, Integer.valueOf(emp.getYearID()));
			preparedStatement.setString(3, emp.getStatus());
			preparedStatement.setDate(4, Date.valueOf(emp.getDate()));
			preparedStatement.setString(5, emp.getLeaveName());
			preparedStatement.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
			preparedStatement.setTimestamp(7, new Timestamp(System.currentTimeMillis()));
			preparedStatement.setInt(8, empLeaveID);

			preparedStatement.execute();
			connection.commit();
			preparedStatement.close();

			return true;
		} catch (Exception e) {
            e.printStackTrace();
			logger.error(e.getMessage());
		}
		return false;

	}

}
