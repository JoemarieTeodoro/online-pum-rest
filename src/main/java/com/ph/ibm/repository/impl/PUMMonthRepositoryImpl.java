package com.ph.ibm.repository.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.util.List;

import org.apache.log4j.Logger;

import com.ph.ibm.model.PUMDownloadReportMonth;
import com.ph.ibm.repository.PUMMonthRepository;
import com.ph.ibm.resources.ConnectionPool;
import com.ph.ibm.util.OpumConstants;
import com.ph.ibm.util.SqlQueries;

public class PUMMonthRepositoryImpl implements PUMMonthRepository{

	private Logger logger = Logger.getLogger( PUMMonthRepositoryImpl.class );
	private static PUMMonthRepositoryImpl pumMonthRepositoryImpl;
	private ConnectionPool connectionPool = ConnectionPool.getInstance();

	public static PUMMonthRepositoryImpl getInstance() {
		if (pumMonthRepositoryImpl == null) {
			pumMonthRepositoryImpl = new PUMMonthRepositoryImpl();
		}
		return pumMonthRepositoryImpl;
	}

	@Override
	public boolean saveMonthEndingDates(List<PUMDownloadReportMonth> pumMonths) throws SQLException, ParseException  {
		Connection connection = connectionPool.getConnection();
		PreparedStatement preparedStatement = null;
		try{
			connection.setAutoCommit( false );
			String query = SqlQueries.SQL_QUERY_SAVE_MONTHEND;
			preparedStatement = connection.prepareStatement( query );
			for( PUMDownloadReportMonth pumMonth : pumMonths ){
				preparedStatement.setInt(1, pumMonth.getMonthId());
				preparedStatement.setString(2, pumMonth.getMonthName());
				preparedStatement.setString(3, pumMonth.getWeekEnd());
				preparedStatement.setInt(4, pumMonth.getYearId());
				preparedStatement.addBatch();
			}

			preparedStatement.executeBatch();
			connection.commit();
			logger.info( OpumConstants.SUCCESSFULLY_SAVED_DATA );
			preparedStatement.close();
		}
		catch( SQLException e ){
			logger.error( e.getStackTrace() );
		}
		finally{
			connectionPool.closeConnection( connection, preparedStatement );
		}
		return true;
	}
}
