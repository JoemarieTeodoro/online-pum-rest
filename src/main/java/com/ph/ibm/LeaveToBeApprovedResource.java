package com.ph.ibm;

import java.sql.SQLException;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.ph.ibm.bo.ApproveLeaveBO;
import com.ph.ibm.bo.RejectLeaveBO;
import com.ph.ibm.model.EmployeeLeave;
import com.ph.ibm.model.ForApproval;
import com.ph.ibm.opum.exception.OpumException;
import com.ph.ibm.repository.EmployeeLeaveRepository;
import com.ph.ibm.repository.impl.EmployeeLeaveRepistoryImpl;

/**
 * Root resource (exposed at "leaveToBeApproved" path) This class is an end point called by
 * the client. The methods are exposed as a Web Service.
 *
 * @author Jeremy
 * @author Martin
 * @author Melvin
 * @version 5.0
 */
@Path( "leaveToBeApproved" )
public class LeaveToBeApprovedResource {

	ApproveLeaveBO approveLeaveBO;

	RejectLeaveBO rejectLeaveBO;

	/**
	 * Logger is used to document the execution of the system and logs the
	 * corresponding log level such as INFO, WARN, ERROR
	 */
	private Logger LOGGER = Logger.getLogger(LeaveToBeApprovedResource.class);

	/**
	 * This service is invoked when the team lead approves the leaves filed.
	 *
	 * <br><br>Exposed at "leaveToBeApproved/approve" path
	 *
	 * @return <b>Response</b>
	 * 			  - object that contains the HTTP Response
	 * @throws OpumException
	 */
	@Path("/approve")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public Response approveLeaves(List<ForApproval> forApprovalList) throws OpumException {
		LOGGER.info("START approveLeaves");
		Response response  = null;
		try {
			for(ForApproval approval : forApprovalList){
				updateEmployeeLeaveStatus(approval,"Approved");
			}

			sendApprovalEmail(forApprovalList);
		} catch (Exception e) {
			LOGGER.error(e);
			throw new OpumException(e.getMessage(), e);
		}
		LOGGER.info("END approveLeaves");
		return response;
	}

	/**
	 * This service is invoked when the team lead rejects the leaves filed.
	 *
	 * <br><br>Exposed at "leaveToBeApproved/reject" path
	 *
	 * @return <b>Response</b>
	 * 			  - object that contains the HTTP Response
	 * @throws OpumException
	 */
	@Path("/reject")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public Response rejectLeaves(List<ForApproval> forApprovalList) throws OpumException {
		LOGGER.info("START rejectLeaves");
		Response response  = null;
		try {
			for(ForApproval approval : forApprovalList){
				updateEmployeeLeaveStatus(approval,"Rejected");
			}

			sendRejectEmail(forApprovalList);
		} catch (Exception e) {
			LOGGER.error(e);
			throw new OpumException(e.getMessage(), e);
		}
		LOGGER.info("END rejectLeaves");
		return response;
	}

	private void updateEmployeeLeaveStatus( ForApproval approval , String status ){
		EmployeeLeaveRepository repo = new EmployeeLeaveRepistoryImpl();
		EmployeeLeave empLeave = repo.getEmployeeLeave(approval);
		if(empLeave == null){
			LOGGER.debug("No employee found matching ForApproval entry");
			return;
		}
		LOGGER.debug("Employee Leave Retreived: " + empLeave.toString());
		empLeave.setStatus(status);
		repo.updateEmployeeLeaveStatus(empLeave);

	}

	private void sendApprovalEmail(List<ForApproval> forApprovalList) throws SQLException {
		approveLeaveBO = new ApproveLeaveBO();
		approveLeaveBO.sendAdminApproveEmail(forApprovalList);
		//approveLeaveBO.sendPEMApproveEmail(forApprovalList);
	}

	private void sendRejectEmail(List<ForApproval> forApprovalList) throws SQLException {
		rejectLeaveBO = new RejectLeaveBO();
		rejectLeaveBO.sendEmployeeRejectLeaveEmail(forApprovalList);
	}
}
