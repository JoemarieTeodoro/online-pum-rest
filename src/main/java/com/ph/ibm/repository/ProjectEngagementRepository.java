package com.ph.ibm.repository;

import java.sql.SQLException;
import java.util.List;

import com.ph.ibm.model.ProjectEngagement;

/**
 * Data Access Object to project_engagement table
 */
public interface ProjectEngagementRepository {

	/**
	 * This method is used to insert fields to project_engagement table
	 * 
	 * @param projectEngagement
	 * @return boolean
	 * @throws SQLException
	 */
	public boolean addProjectEngagement(ProjectEngagement projectEngagement) throws SQLException;

	/**
	 * This method is used to update fields of project_engagement table
	 * 
	 * @param projectEngagement
	 * @return boolean
	 * @throws SQLException
	 */
	public boolean saveDate(ProjectEngagement projectEngagement) throws SQLException;

	/**
	 * This method is used to select field from project_engagement table
	 * 
	 * @param projectId
	 * @param employeeId
	 * @return Long
	 * @throws SQLException
	 */
	public Long getProjectEngagementId(Long projectId, String employeeId) throws SQLException;

	/**
	 * This method is used to select field from project_engagement table
	 * 
	 * @param projectEngagementId
	 * @return boolean
	 * @throws SQLException
	 */
	public boolean checkDates(Long projectEngagementId) throws SQLException;
	
	/**
	 * This method is used to select start and end dates from project_engagement table
	 * 
	 * @param employeeId
	 * @param projectEngagementId
	 * @return List
	 * @throws SQLException
	 */
	public List<ProjectEngagement> getAllProjectEngagement() throws SQLException;

}