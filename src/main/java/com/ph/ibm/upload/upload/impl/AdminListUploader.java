package com.ph.ibm.upload.upload.impl;

import java.io.IOException;
import java.sql.BatchUpdateException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.mail.Message.RecipientType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.apache.log4j.Logger;

import com.ph.ibm.bo.ResetPasswordBO;
import com.ph.ibm.model.Email;
import com.ph.ibm.model.Employee;
import com.ph.ibm.opum.exception.InvalidCSVException;
import com.ph.ibm.opum.exception.InvalidEmployeeException;
import com.ph.ibm.repository.EmployeeRepository;
import com.ph.ibm.repository.impl.EmployeeRepositoryImpl;
import com.ph.ibm.upload.Uploader;
import com.ph.ibm.util.OpumConstants;
import com.ph.ibm.util.UploaderUtils;
import com.ph.ibm.validation.Validator;
import com.ph.ibm.validation.impl.EmployeeValidator;

/**
 * Class implementation for uploading list of user administrator
 *
 * @author <a HREF="teodorj@ph.ibm.com">Joemarie Teodoro</a>
 */
public class AdminListUploader implements Uploader {

    /**
     * EmployeeRepository is a Data Access Object which contain methods to add, register, login, view, validate field/s
     * stored in employee table - opum database
     */
    private EmployeeRepository employeeRepository = new EmployeeRepositoryImpl();

    /**
     * Validation contain methods to validate field such as employee name, employee id, project name, email address
     */
    private Validator<Employee> validator = new EmployeeValidator( employeeRepository );

    /**
     * Logger is used to document the execution of the system and logs the corresponding log level such as INFO, WARN,
     * ERROR
     */
    private Logger logger = Logger.getLogger( AdminListUploader.class );

    /**
     * This method is used when Super Administrator uploads the list of Admin Users
     *
     * @param rawData Data from the CSV file
     * @param uriInfo uri information
     * @return @throws Exception exception
     * @see com.ph.ibm.upload.Uploader#upload(java.lang.String, javax.ws.rs.core.UriInfo)
     */
    @Override
    public Response upload( String rawData, UriInfo uriInfo ) throws Exception {

        Map<String, List<String>> rows = UploaderUtils.populateList( rawData );
        List<Employee> validatedEmployee = new ArrayList<Employee>();
        Employee validateEmployee = new Employee();
        List<String> lstRecipients = new ArrayList<String>();
        try{
            for( List<String> row : rows.values() ){
                validateEmployee = validateEmployee( uriInfo, row );
                validatedEmployee.add( validateEmployee );
            }
            for( Employee employee : validatedEmployee ){
                Employee savedEmployee = employeeRepository.saveOrUpdate( employee );
                employeeRepository.saveEmployeeRole( savedEmployee.getEmployeeSerial() );
                lstRecipients.add( savedEmployee.getIntranetId() );
            }
        }
        catch( BatchUpdateException e ){
            logger.error( "BatchUpdateException due to " + e.getMessage() );
            return UploaderUtils.invalidCsvResponseBuilder( uriInfo, validateEmployee, OpumConstants.DUPLICATE_ENTRY );
        }
        catch( InvalidCSVException e ){
            logger.error( e.getError() );
            return UploaderUtils.invalidCsvResponseBuilder( uriInfo, e.getObject(), OpumConstants.DUPLICATE_ENTRY );
        }
        catch( SQLException e ){
            logger.error( "SQL Exception due to " + e.getMessage() );
            e.printStackTrace();
            return Response.status( 406 ).header( "Location", uriInfo.getBaseUri() + "employee/" ).entity(
                OpumConstants.SQL_ERROR ).build();
        }

        logger.info( OpumConstants.SUCCESSFULLY_UPLOADED_FILE );

        sendEmailsToListOfRecepientsToChangePasswords( lstRecipients );
        logger.info( OpumConstants.SUCCESSFULLY_EMAILED_LIST_OF_EMAIL_ADDRESS );

        return Response.status( Status.OK ).entity( "CSV Uploaded Successfully" ).build();
    }

    /**
     * Method to email list of addresses from the list uploaded by sys_admin/admin
     */
    public void sendEmailsToListOfRecepientsToChangePasswords( List<String> lstRecipients ) throws IOException {
        ResetPasswordBO resetPasswordBO = new ResetPasswordBO();
        Email email = new Email();
        email.setRecipientAddresses( lstRecipients );
        email.setSenderAddress( "onlinepumsender@gmail.com" );
        email.setRecipientType( RecipientType.TO.toString() );
        email.setSubject( OpumConstants.EMAIL_SUBJECT );
        email.setText( OpumConstants.EMAIL_GREETING + "\n\n" + OpumConstants.EMAIL_BODY + "\n\n%s" );
        resetPasswordBO.emailResetPasswordLink( email );
    }

    /**
     * This method is used to validate uploaded list of Users/Employees
     *
     * @param uriInfo uri information
     * @param row represents row in csv file
     * @return Employee employee object
     * @throws Exception exception
     */
    private Employee validateEmployee( UriInfo uriInfo, List<String> row ) throws Exception {

        if( row == null || row.isEmpty() ){
            throw new InvalidEmployeeException( OpumConstants.INVALID_CSV );
        }
        Employee validateEmployee = new Employee();
        validateEmployee.setEmployeeSerial( row.get( 0 ) );
        validateEmployee.setFullName( row.get( 1 ) );
        validateEmployee.setIntranetId( row.get( 2 ) );
        validateEmployee.setRollInDate( row.get( 3 ) );
        validateEmployee.setRollOffDate( row.get( 4 ) );
        validator.validate( validateEmployee );
        return validateEmployee;
    }
}
