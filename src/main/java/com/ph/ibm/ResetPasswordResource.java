package com.ph.ibm;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;

import com.ph.ibm.bo.ResetPasswordBO;
import com.ph.ibm.model.Email;
import com.ph.ibm.model.ResetPassword;
import com.ph.ibm.opum.exception.OpumException;

/**
 * Root resource (exposed at "adminPasswordReset" path) This class is an end point called by
 * the client. The methods are exposed as a Web Service.
 *
 * @author Jerome
 * @author Melvin
 * @version 5.0
 */
@Path("resetPassword")
public class ResetPasswordResource {

	private ResetPasswordBO resetPasswordBO;
	/**
	 * Logger is used to document the execution of the system and logs the
	 * corresponding log level such as INFO, WARN, ERROR
	 */
	private Logger logger = Logger.getLogger(ResetPasswordResource.class);

	/**
	 * This service is invoked when the list of admins were successfully uploaded and registered.
	 *
	 * <br><br>Exposed at "resetPassword/email" path
	 *
	 * @return <b>Response</b>
	 * 			  - object that contains the HTTP Response
	 * @throws OpumException
	 */
	@Path("/reset")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public Response resetPassword(ResetPassword resetPassword) throws OpumException {
		logger.info("START emailResetPasswordLink");
		Response response;
		try {
			resetPasswordBO = new ResetPasswordBO();
			response = resetPasswordBO.resetPassword(resetPassword);
		} catch (Exception e) {
			logger.error(e);
			throw new OpumException(e.getMessage(), e);
		}
		logger.info("END emailResetPasswordLink");
		return response;
	}
	
	/**
	 * This service is invoked when the list of admins were successfully uploaded and registered.
	 *
	 * <br><br>Exposed at "resetPassword/email" path
	 *
	 * @return <b>Response</b>
	 * 			  - object that contains the HTTP Response
	 * @throws OpumException
	 */
	@Path("/email")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public Response emailResetPasswordLink(Email email) throws OpumException {
		logger.info("START emailResetPasswordLink");
		Response response;
		try {
			resetPasswordBO = new ResetPasswordBO();
            response = resetPasswordBO.emailResetPasswordLinkToSingleEmployee( email );
		} catch (Exception e) {
			logger.error(e);
			throw new OpumException(e.getMessage(), e);
		}
		logger.info("END emailResetPasswordLink");
		return response;
	}
	
	/**
	 * @throws OpumException 
	 * This service is invoked to validate if the reset password link accessed is valid.
	 *
	 * <br><br>Exposed at "resetPassword/validateToken" path
	 *
	 * @return <b>Response</b>
	 * 			  - object that contains the HTTP Response
	 * @throws 
	 */
	@Path("/validateToken")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public Response validateResetPasswordLinkToken(ResetPassword resetPassword) throws OpumException {
		logger.info("START validateResetPasswordLinkToken");
		boolean validToken = false;
		Response response = null;
		try {
			resetPasswordBO = new ResetPasswordBO();
			validToken = resetPasswordBO.validateToken(resetPassword);
		} catch (Exception e) {
			logger.error(e);
			throw new OpumException(e.getMessage(), e);
		}
		if (validToken) {
			response = Response.status(Status.OK)
			.header("Location", "" + "employee/")
			.entity("token is valid")
			.build();
		} else {
			response = Response.status(Status.BAD_REQUEST)
			.header("Location", "" + "employee/")
			.entity("token is already expired and invalid")
			.build();
		}
		logger.info("END emailResetPasswordLink: " + response.toString());
		return response;
	}
	
}
