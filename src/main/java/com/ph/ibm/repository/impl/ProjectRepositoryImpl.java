package com.ph.ibm.repository.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.ph.ibm.model.PUMDownloadReportMonth;
import com.ph.ibm.model.Project;
import com.ph.ibm.repository.ProjectRepository;
import com.ph.ibm.resources.ConnectionPool;
import com.ph.ibm.util.SqlQueries;

public class ProjectRepositoryImpl implements ProjectRepository {

	private ConnectionPool connectionPool = ConnectionPool.getInstance();
	private Logger logger = Logger.getLogger( ProjectRepositoryImpl.class );

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
				Long projectId = resultSet.getLong(1);
				String projectName = resultSet.getString(2);
				String createDate = resultSet.getString(3);
				String createdBy = resultSet.getString(4);
				Project project = new Project(projectId, projectName, createDate, createdBy);
				projects.add(project);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			connectionPool.closeConnection(connection, preparedStatement, resultSet);
		}
		return projects;
	}

	@Override
	public List<PUMDownloadReportMonth> retrievePumMonths(String yearId) throws SQLException {
		Connection connection = connectionPool.getConnection();
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		List<PUMDownloadReportMonth> pumMonths = new ArrayList<>();
		try {
			String query = SqlQueries.SQL_QUERY_GET_MONTHEND_DETAILS;
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, yearId);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				String monthName = resultSet.getString(2);
				String weekend = resultSet.getString(3);
				PUMDownloadReportMonth pumMonth = new PUMDownloadReportMonth(
						monthName, weekend);
				pumMonths.add(pumMonth);
			}

		} catch (SQLException e) {
			logger.error("Error - retrievePumMonths => " + e.getMessage());
		} finally {
			connectionPool.closeConnection(connection, preparedStatement, resultSet);
		}
		return pumMonths;
	}

	@Override
	public String updatePumMonths(List<PUMDownloadReportMonth> pumMonths) throws SQLException {
        Connection connection = connectionPool.getConnection();
        PreparedStatement preparedStatement = null;
        String query = SqlQueries.SQL_QUERY_UPDATE_MONTH_END_DATE;
        try {
            connection.setAutoCommit( false );
            preparedStatement = connection.prepareStatement( query );

            for ( PUMDownloadReportMonth pumMonth : pumMonths ) {
                preparedStatement.setString( 1, pumMonth.getWeekEnd() );
                preparedStatement.setString( 2, pumMonth.getMonthName() );
                preparedStatement.setInt( 3, pumMonth.getYearId());
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
            connection.commit();
        } catch ( SQLException e ) {
            logger.error( e.getStackTrace() );
        } finally {
            connectionPool.closeConnection( connection, preparedStatement );
        }
        return "Update successful";
     }
}