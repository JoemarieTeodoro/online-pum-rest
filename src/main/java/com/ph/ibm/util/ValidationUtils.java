package com.ph.ibm.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.apache.log4j.Logger;
import com.ph.ibm.opum.exception.InvalidCSVException;

public class ValidationUtils 
{
    private static Logger logger = Logger.getLogger( ValidationUtils.class );
    
    public static final String DATE_FORMAT = "M/d/yyyy";
    
    public static final String VALID_SERIAL_REGEX = "^([A-Za-z0-9]{6,})*$";

	public static final String VALID_EMAIL_REGEX = "^[a-zA-Z0-9]+@{1}+[a-zA-Z]{0,2}([\\\\.]+)+ibm+([\\\\.]+)com$";

	public static final String VALID_NAME_REGEX = "^([A-Za-z.]+[ ]{0,1})*([A-Za-z.]+[ ]{0,1})*$";
	
	public static final String VALID_DATE_REGEX = "^(0[1-9]|1[0-2]|[1-9])\\/(0[1-9]|[1-9]|[12][0-9]|3[01])\\/(19|20)\\d{2}$";
	
	public static String CAUSE_OF_ERROR = "CAUSE OF ERROR: ";

    public static boolean regexValidator( Object object, String stringToValidate, String patternTomatch,
                                          String errorConstant )
        throws InvalidCSVException {
        boolean isValid = stringToValidate.matches( patternTomatch );
        if( !isValid ){
            logger.info( CAUSE_OF_ERROR + errorConstant );
            throw new InvalidCSVException( object, errorConstant );
        }
        return isValid;
    }

    public static boolean isValueEmpty( String value ) {
        return ( value == null || value.isEmpty() || value.equals( "\n" ) );
    }
    
    public static boolean isValidDate(Object object, String rollDate) throws InvalidCSVException {
        try {
            DateFormat format = new SimpleDateFormat(DATE_FORMAT);
            format.setLenient(false);
            format.parse(rollDate);

            return ValidationUtils.regexValidator( object, rollDate, VALID_DATE_REGEX, OpumConstants.INVALID_NAME );
        } catch (ParseException e) {
            logger.info( CAUSE_OF_ERROR + OpumConstants.INVALID_DATE );
            throw new InvalidCSVException(object, OpumConstants.INVALID_DATE);
        }
	}   

    public static String dateFormat( String date ) {
           SimpleDateFormat fromFile = new SimpleDateFormat( "MM/dd/yyyy" );
           SimpleDateFormat myFormat = new SimpleDateFormat( "yyyy-MM-dd" );
           String formattedDate = null;
           try{
               formattedDate = myFormat.format( fromFile.parse( date ) );
           }
           catch( ParseException e ){
               e.printStackTrace();
           }
           return formattedDate;
    }
    
    public static boolean isValidDateRange(Object object, String start, String end) throws InvalidCSVException {
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
	        LocalDate rollInDate = LocalDate.parse(start, formatter);
	        LocalDate rollOffDate = LocalDate.parse(end, formatter);
	
	        if( rollInDate.isAfter(rollOffDate) ){
	            logger.info( CAUSE_OF_ERROR + OpumConstants.INVALID_DATE_RANGE );
	        	throw new InvalidCSVException( object,OpumConstants.INVALID_DATE_RANGE );
	        }
	
			return true;
		}
}
