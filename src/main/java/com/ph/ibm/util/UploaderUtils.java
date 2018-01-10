package com.ph.ibm.util;

import java.io.IOException;

import javax.mail.Message.RecipientType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.ph.ibm.bo.ResetPasswordBO;
import com.ph.ibm.model.Email;
import com.ph.ibm.model.Employee;

/**
 * Utility Class for upload functionality
 * 
 * @author <a HREF="teodorj@ph.ibm.com">Joemarie Teodoro</a>
 * @author <a HREF="dacanam@ph.ibm.com">Marjay Dacanay</a>
 */
public class UploaderUtils {

    /**
     * This method used to validate if CSV row data is Empty
     *
     * @param line represents row in csv file
     * @return boolean true if row is empty otherwise false
     */
    public static boolean isRowEmpty( String line ) {
        return line == null || line.equals( "\\n" ) || line.equals( "" );
    }

    /**
     * This method is used to generate error message for Upload List
     *
     * @param uriInfo URI information
     * @param e employee object
     * @param errorMessage error message
     * @return Response response
     */
    public static Response invalidCsvResponseBuilder( UriInfo uriInfo, Object e, String errorMessage ) {
        return Response.status( 406 ).entity( errorMessage ).build();
    }

    public static void sendEmailToRecipients( Employee employee ) throws IOException {

        Email email = new Email();
        email.setRecipientAddress( employee.getIntranetId() );
        email.setTempPassword( employee.getPassword() );
        email.setSenderAddress( "onlinepumsender@gmail.com" );
        email.setRecipientType( RecipientType.TO.toString() );
        email.setSubject( OpumConstants.EMAIL_SUBJECT );
        email.setText( OpumConstants.EMAIL_GREETING + "\n\n" + OpumConstants.EMAIL_BODY + "\n\n%s" );

        ResetPasswordBO resetPasswordBO = new ResetPasswordBO();
        resetPasswordBO.emailResetPasswordLinkToSingleEmployee( email );
    }
}
