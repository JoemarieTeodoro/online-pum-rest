package com.ph.ibm;

import java.sql.SQLException;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.ph.ibm.bo.EmailBO;
import com.ph.ibm.model.Email;
import com.ph.ibm.opum.exception.OpumException;

/**
 * Root resource (exposed at "adminPasswordReset" path) This class is an end point called by
 * the client. The methods are exposed as a Web Service.
 *
 * @author Jerome
 * @author Melvin
 * @version 5.0
 */
@Path("adminPasswordReset")
public class AdminPasswordResetResource {

	private EmailBO emailBO;
	/**
	 * Logger is used to document the execution of the system and logs the
	 * corresponding log level such as INFO, WARN, ERROR
	 */
	private Logger logger = Logger.getLogger(AdminPasswordResetResource.class);

	/**
	 * This service is invoked when administrator logs in for the first time.
	 *
	 * <br><br>Exposed at "adminPasswordReset/emailFormattingForSending" path
	 *
	 * @return <b>Response</b>
	 * 			  - object that contains the HTTP Response
	 * @throws OpumException
	 */
	@Path("/emailFormattingForSending")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public Response emailFormattingForSending(Email email) throws SQLException,
	        OpumException {
		logger.info("START uploadEmployeeList");
		Response response;
		try {
			emailBO = new EmailBO();
			response = emailBO.formatAndSendEmailPasswordReset(email);
		} catch (Exception e) {
			logger.error(e);
			throw new OpumException(e.getMessage(), e);
		}
		logger.info("END uploadEmployeeList");
		return response;
	}
}
