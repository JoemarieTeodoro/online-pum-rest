package com.ph.ibm.upload;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.mail.Message.RecipientType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.ph.ibm.bo.ResetPasswordBO;
import com.ph.ibm.model.Email;
import com.ph.ibm.opum.exception.InvalidCSVException;
import com.ph.ibm.util.OpumConstants;
import com.ph.ibm.util.UploaderUtils;

/**
 * Base class for CSV file upload functionality. Contains method for initialization of file values and common
 * validations.
 * 
 * @author <a HREF="teodorj@ph.ibm.com">Joemarie Teodoro</a>
 * @author <a HREF="dacanam@ph.ibm.com">Marjay Dacanay</a>
 */
public abstract class CsvUploaderBase implements Uploader {

    /**
     * Parses CSV file and populate map based on CSV values
     *
     * @param rawData Data from the CSV file
     * @return Populated list of employee row data from CSV file
     * @throws InvalidCSVException custom exception of invalid csv
     */
    protected Map<String, List<String>> parseCSV( String rawData ) throws InvalidCSVException {
        Scanner sc = new Scanner( rawData );
        Map<String, List<String>> rawDataMap = new LinkedHashMap<String, List<String>>();
        populateRawDataMap( rawDataMap, sc );
        sc.close();
        return rawDataMap;
    }

    /**
     * @param rawDataMap contains CSV file values
     * @param sc Scanner instance
     * @param headers
     * @throws InvalidCSVException custom exception of invalid CSV
     */
    private void populateRawDataMap( Map<String, List<String>> rawDataMap, Scanner sc ) throws InvalidCSVException {
        String delimiter = ",";
        ignoreFirstRow( sc );
        List<String> row;
        int lineNumber = 1;
        int headerLineNumber = 1;
        while( sc.hasNextLine() ){
            String line = sc.nextLine();

            if( lineNumber == 1 && isEmpty( line ) ){
                throw new InvalidCSVException( null, OpumConstants.NO_HEADER_FOUND );
            }

            row = Arrays.asList( line.split( delimiter ) );
            if( headerLineNumber == lineNumber && !doesContainsHeader( row ) ){
                throw new InvalidCSVException( null, getHeaders() );
            }
            else if( lineNumber == 1 ){
                lineNumber++;
                continue;
            }

            if( !isLineEmpty( row ) && !line.contains( "----" ) ){
                rawDataMap.put( String.valueOf( lineNumber ), row );
            }
            lineNumber++;
        }
        validateCsvValues( rawDataMap );
    }

    /**
     * Validates the CSV file
     * 
     * @param rawDataMap map which consists of CSV row values
     * @throws InvalidCSVException custom exception of invalid CSV
     */
    private void validateCsvValues( Map<String, List<String>> rawDataMap ) throws InvalidCSVException {
        if( rawDataMap.isEmpty() ){
            throw new InvalidCSVException( null, "Error found in CSV!\n\n" + OpumConstants.EMPTY_CSV_VALUE );
        }
    }

    /**
     * Method to email list of addresses from the list uploaded by sys_admin/admin
     * 
     * @param lstRecipients list of recipients
     * @throws IOException exception
     */
    protected void sendEmailsToListOfRecepientsToChangePasswords( List<String> lstRecipients ) throws IOException {
        Email email = new Email();
        email.setRecipientAddresses( lstRecipients );
        email.setSenderAddress( "onlinepumsender@gmail.com" );
        email.setRecipientType( RecipientType.TO.toString() );
        email.setSubject( OpumConstants.EMAIL_SUBJECT );
        email.setText( OpumConstants.EMAIL_GREETING + "\n\n" + OpumConstants.EMAIL_BODY + "\n\n%s" );

        ResetPasswordBO resetPasswordBO = new ResetPasswordBO();
        resetPasswordBO.emailResetPasswordLink( email );
    }

    /**
     * Generate message with the corresponding line number/s and error/s and return invalid response.
     * 
     * @param uriInfo URI information
     * @param errorList list of errors
     * @return invalid response
     */
    protected Response InvalidCsvErrors( UriInfo uriInfo, List<String> errorList ) {
        String invalidCsv;
        StringBuilder errors = new StringBuilder();
        for( String string : errorList ){
            errors.append( string + "\n" );
        }
        invalidCsv = String.format( "Error/s found in CSV!\nPlease check the following line number/s: \n\n%s",
            errors.toString(), uriInfo );
        return UploaderUtils.invalidCsvResponseBuilder( uriInfo, null, invalidCsv );
    }

    /**
     * This method used to ignore the header of the CSV file
     *
     * @param sc Scanner
     */
    private void ignoreFirstRow( Scanner sc ) {
        while( sc.hasNextLine() ){
            String row = sc.nextLine();
            if( isEmpty( row ) ){
                break;
            }
        }
    }

    /**
     * Check row in CSV file if contains header i.e Serial, Employee, Email, Roll-In-Date, Roll-Off-Date
     * 
     * @param row row in CSV
     * @return true if row contains header otherwise return false;
     */
    protected abstract boolean doesContainsHeader( List<String> row );

    /**
     * Check row in CSV file if contains header i.e Serial, Employee, Email, Roll-In-Date, Roll-Off-Date
     * 
     * @param row row in CSV
     * @return true if row contains header otherwise return false;
     */
    protected abstract String getHeaders();

    /**
     * This method used to validate if CSV row data cell value is empty
     *
     * @param value cell value to check
     * @return true if empty otherwise false
     */
    protected static boolean isEmpty( String value ) {
        return value == null || value.equals( "\\n" ) || value.equals( "" );
    }

    /**
     * @param row CSV row valuesW
     * @return true if row values is empty otherwise false
     */
    private boolean isLineEmpty( List<String> row ) {
        return ( row == null || row.isEmpty() || isRowEmpty( row ) );
    }

    /**
     * @param row
     * @return
     */
    private boolean isRowEmpty( List<String> row ) {
        for( String cell : row ){
            if( cell != null && !cell.isEmpty() ){
                return false;
            }

        }
        return true;
    }

}
