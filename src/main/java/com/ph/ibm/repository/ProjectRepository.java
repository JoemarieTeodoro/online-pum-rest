package com.ph.ibm.repository;

import java.sql.SQLException;
import java.util.List;

import com.ph.ibm.model.Project;

/**
 * Data Access Object to project table
 */
public interface ProjectRepository {

	/**
	 * This method is used to select fields from project
	 * 
	 * @return list of project
	 * @throws SQLException
	 */
	public List<Project> retrieveData() throws SQLException;

}
