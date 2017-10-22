package com.ph.ibm.model;

import java.sql.Date;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ProjectEngagement extends BaseAuditBean{

	private Long projectEngagementId;
	private Long projectId;
	private String employeeSerial;
	private Date startDate;
	private Date endDate;

	public ProjectEngagement() {

	}

	public ProjectEngagement(Date startDate, Date endDate) {
		super();
		this.startDate = startDate;
		this.endDate = endDate;

	}

	public ProjectEngagement(Long projectEngagementId, Long projectId, String employeeSerial, Date startDate, Date endDate,
			String createDate, String updateDate, String createdBy, String updatedBy) {
		super();
		this.projectEngagementId = projectEngagementId;
		this.projectId = projectId;
		this.employeeSerial = employeeSerial;
		this.startDate = startDate;
		this.endDate = endDate;
		this.setCreateDate(createDate);
		this.setUpdateDate(updateDate);
		this.setCreatedBy(createdBy);
		this.setUpdatedBy(updatedBy);
	}

	public Long getProjectEngagementId() {
		return projectEngagementId;
	}

	public void setProjectEngagementId(Long projectEngagementId) {
		this.projectEngagementId = projectEngagementId;
	}

	public Long getProjectId() {
		return projectId;
	}

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}

	public String getEmployeeId() {
		return employeeSerial;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeSerial = employeeId;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
}
