package com.ph.ibm.repository.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.ph.ibm.model.Utilization;
import com.ph.ibm.model.UtilizationRowData;
import com.ph.ibm.model.UtilizationXLSData;
import com.ph.ibm.report.UtilizationXLSQuarterReport;
import com.ph.ibm.report.UtilizationXLSReport;
import com.ph.ibm.report.UtilizationXLSWeeklyReport;
import com.ph.ibm.repository.UtilizationEngagementRepository;
import com.ph.ibm.resources.ConnectionPool;
import com.ph.ibm.util.OpumConstants;
import com.ph.ibm.util.SqlQueries;

import static com.ph.ibm.util.OpumConstants.TOTAL_NUMBER_OF_WEEKS;

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
				preparedStatement = connection.prepareStatement(SqlQueries.SQL_SAVE_UTILIZATION);
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
				preparedStatement = connection.prepareStatement(SqlQueries.SQL_UPDATE_UTILIZATION);
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
			preparedStatement = connection.prepareStatement(SqlQueries.SQL_RETRIEVE_UTILIZATION);
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
			preparedStatement = connection.prepareStatement(SqlQueries.SQL_QUERY_FOR_DOWNLOAD_UTILIZATION);
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
	public Response downloadUtilizationReport(String periodKey, int periodValue, String filePath)
			throws SQLException, FileNotFoundException, IOException {
		Connection connection = connectionPool.getConnection();
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		String columnList = "";
		Date fiscalStartDate = getFiscalStartDate();
		int endIndex = calculatePeriodValue(periodKey, periodValue,fiscalStartDate);
		List<UtilizationRowData> utilizations = new ArrayList<UtilizationRowData>();
		List<Integer> weekUtilization;
		UtilizationXLSData xlsData = new UtilizationXLSData();
		List<Integer> quarterUtilization = null;
		final int columnSpacer = 2;
		
		for(int count = 1; count <= OpumConstants.TOTAL_NUMBER_OF_WEEKS; count++) {
			columnList = columnList.concat(", week" + count);
		}

		try{
			String query = "SELECT u.employee_serial, e.fullname " + columnList +
					 " FROM utilization u left join employee e on u.employee_serial = e.employee_id "  +
					 "WHERE u.type = 'FORECAST' and u.year_id = (SELECT Year_id FROM year ORDER BY PUMYear DESC LIMIT 1)";
			preparedStatement = connection.prepareStatement(query);
			resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				weekUtilization = new ArrayList<Integer>();
				quarterUtilization = new ArrayList<Integer>();
				for(int count = 1 + columnSpacer; count <= TOTAL_NUMBER_OF_WEEKS + columnSpacer; count++) {
					weekUtilization.add(resultSet.getInt(count));
				}
				int Q1 = weekUtilization.subList(0, 13).stream().mapToInt(Integer::intValue).sum();
				quarterUtilization.add(Q1);
				int Q2 = weekUtilization.subList(13, 26).stream().mapToInt(Integer::intValue).sum();
				quarterUtilization.add(Q2);
				int Q3 = weekUtilization.subList(26, 39).stream().mapToInt(Integer::intValue).sum();
				quarterUtilization.add(Q3);
				int Q4 = weekUtilization.subList(39, 52).stream().mapToInt(Integer::intValue).sum();
				quarterUtilization.add(Q4);

				UtilizationRowData utilizationRow = new UtilizationRowData(resultSet.getString(1),resultSet.getString(2),
						weekUtilization, quarterUtilization);
				utilizations.add(utilizationRow);
			}
			xlsData = new UtilizationXLSData(filePath, endIndex, utilizations, fiscalStartDate);
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).type(MediaType.APPLICATION_OCTET_STREAM)
					.build();
		} finally {
			connectionPool.closeConnection(connection, preparedStatement, resultSet);
		}

		if(periodKey.equals("Weekly")) {
			UtilizationXLSWeeklyReport xlsQWeeklyReport = new UtilizationXLSWeeklyReport(xlsData);
			return xlsQWeeklyReport.generateReport();
		} else if (periodKey.equals("Quarterly")) {
			UtilizationXLSQuarterReport xlsQuarterReport = new UtilizationXLSQuarterReport(xlsData);
			return xlsQuarterReport.generateReport();
		} else {
			UtilizationXLSReport xlsReport = new UtilizationXLSQuarterReport(xlsData);
			return xlsReport.generateReport();
		}

	}

	public Date getFiscalStartDate() {
		Connection connection = connectionPool.getConnection();
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try{
			preparedStatement = connection.prepareStatement(SqlQueries.SQL_RETRIEVE_CURRENT_FY);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				return parseDate( resultSet.getString(1) );
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			connectionPool.closeConnection(connection, preparedStatement, resultSet);
		}
		return null;
	}

	public static Date parseDate(String date) {
	     try {
	         return new SimpleDateFormat("yyyy-MM-dd").parse(date);
	     } catch (ParseException e) {
	         return null;
	     }
	  }

	private static int calculatePeriodValue(String periodKey, int periodValue, Date fiscalStartDate) {
		int periodValueResult = 4;
		if((periodKey.equals("Weekly")) || (periodKey.equals("Quarterly"))) {
			periodValueResult = periodValue;
		}
		return periodValueResult;
	}

	private static LocalDate getQuarterEndDate(int year, Month month) {
		LocalDate start = LocalDate.of(year, month.firstMonthOfQuarter(), 1);
		Month endMonth = start.getMonth().plus(2);
		LocalDate end = LocalDate.of(year, endMonth, endMonth.length(start.isLeapYear()));

		return end;
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
