package com.ph.ibm.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.log4j.Logger;

import com.ph.ibm.model.Employee;
import com.ph.ibm.opum.exception.InvalidCSVException;

public class ValidationUtils {
	/**
	 * Logger is used to document the execution of the system and logs the
	 * corresponding log level such as INFO, WARN, ERROR
	 */
	private static Logger logger = Logger.getLogger(ValidationUtils.class);

	public static String CAUSE_OF_ERROR = "CAUSE OF ERROR: ";

	public static boolean regexValidator( Employee employee, String stringToValidate, String patternTomatch,
			String errorConstant)
			throws InvalidCSVException {
		boolean isValid = stringToValidate.matches(patternTomatch);
		if (!isValid) {
			logger.info( CAUSE_OF_ERROR + errorConstant);
			throw new InvalidCSVException( employee, errorConstant);
		}
		return isValid;
	}

    public static boolean isValueEmpty( String value ) {
        return ( value == null || value.isEmpty() || value.equals( "\n" ) );
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

}
