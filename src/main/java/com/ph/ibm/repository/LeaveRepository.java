package com.ph.ibm.repository;

import java.sql.SQLException;
import java.util.List;

import com.ph.ibm.model.ForApproval;;
/**
 * Interface for accessing leaves
 * 
 * @author <a HREF="carrasj@ph.ibm.com">Jeremy Carrasco</a>
 *
 */
public interface LeaveRepository {

	/**
	 * 
	 * @return List
	 * @throws SQLException
	 */
	public List<ForApproval> getAllForApproval() throws SQLException;
	
}
