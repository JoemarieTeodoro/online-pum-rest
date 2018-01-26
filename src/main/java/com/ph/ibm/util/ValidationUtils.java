package com.ph.ibm.util;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.ph.ibm.model.PUMYear;
import com.ph.ibm.model.TeamEmployee;
import com.ph.ibm.opum.exception.InvalidCSVException;
import com.ph.ibm.opum.exception.OpumException;
import com.ph.ibm.repository.EmployeeRepository;

public class ValidationUtils 
{
    private static Logger logger = Logger.getLogger( ValidationUtils.class );
    
    public static final String MONTH_DAY_YEAR_FORMAT = "M/d/yyyy";
    
    public static final String YEAR_MONTH_DAY_FORMAT = "yyyy-M-d";
    
    public static final String VALID_SERIAL_REGEX = "^([A-Za-z0-9]{6,})*$";

	public static final String VALID_EMAIL_REGEX = "^[a-zA-Z0-9]+@{1}+[a-zA-Z]{0,2}([\\\\.]+)+ibm+([\\\\.]+)com$";

	public static final String VALID_NAME_REGEX = "^([A-Za-z.]+[ ]{0,1})*([A-Za-z.]+[ ]{0,1})*$";
	
	public static final String VALID_DATE_REGEX = "^(0[1-9]|1[0-2]|[1-9])\\/(0[1-9]|[1-9]|[12][0-9]|3[01])\\/(19|20)\\d{2}$";
	
    public static final String VALID_RECOVERABLE_REGEX = "^(?:Y|N)$";

    public static final String VALID_DESIGNATION_REGEX = "^((?i)ONSHORE(?-i)|(?i)OFFSHORE(?-i))$";

    public static final String VALID_TEAM_NAME_REGEX = "^([A-Za-z0-9]+[ ]{0,1})*([A-Za-z0-9]+[ ]{0,1})*$";

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
        return ( value == null || value.isEmpty() || value.equals( "\n" ) || value.equals("undefined--undefined"));
    }
    
    public static boolean isValidDate(Object object, String rollDate) throws InvalidCSVException {
        try {
            DateFormat format = new SimpleDateFormat(MONTH_DAY_YEAR_FORMAT);
            format.setLenient(false);
            format.parse(rollDate);

            return ValidationUtils.regexValidator( object, rollDate, VALID_DATE_REGEX, OpumConstants.INVALID_DATE );
        } catch (ParseException e) {
            logger.info( CAUSE_OF_ERROR + OpumConstants.INVALID_DATE );
            throw new InvalidCSVException(object, OpumConstants.INVALID_DATE);
        }
    }

    /**
     * Checks the value of the date if within the actual employee roll dates
     * 
     * @param employeeRepository data access object to employee table
     * @param teamEmployee mapper for team_employee table in opum database
     * @return true if team mapper roll dates values are within actual project roll dates
     * @throws InvalidCSVException custom exception for invalid csv values
     * @throws SQLException
     */
    public static boolean isWithinDateRange( EmployeeRepository employeeRepository,
                                             TeamEmployee teamEmployee )
        throws InvalidCSVException, SQLException {
        try{

            List<String> projectEmployeeRollDates =
                employeeRepository.getEmployeeRollDates( teamEmployee.getEmployeeId() );

            Date actualEmployeeRollInDate =
                new SimpleDateFormat( YEAR_MONTH_DAY_FORMAT ).parse( projectEmployeeRollDates.get( 0 ) );
            Date actualEmployeeRollOffDate =
                new SimpleDateFormat( YEAR_MONTH_DAY_FORMAT ).parse( projectEmployeeRollDates.get( 1 ) );

            Date updatedTeamRollInDate =
                new SimpleDateFormat( MONTH_DAY_YEAR_FORMAT ).parse( teamEmployee.getRollInDate() );
            Date updatedTeamRollOffDate =
                new SimpleDateFormat( MONTH_DAY_YEAR_FORMAT ).parse( teamEmployee.getRollOffDate() );

            return ( actualEmployeeRollInDate.equals( updatedTeamRollInDate ) ||
                updatedTeamRollInDate.after( actualEmployeeRollInDate ) ) &&
                ( actualEmployeeRollOffDate.equals( updatedTeamRollOffDate ) ||
                    updatedTeamRollOffDate.before( actualEmployeeRollOffDate ) );
            
        }
        catch( ParseException e ){
            logger.info( CAUSE_OF_ERROR + OpumConstants.INVALID_DATE );
            throw new InvalidCSVException( null, OpumConstants.INVALID_DATE );
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
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(MONTH_DAY_YEAR_FORMAT);
	        LocalDate rollInDate = LocalDate.parse(start, formatter);
	        LocalDate rollOffDate = LocalDate.parse(end, formatter);
	
	        if( rollInDate.isAfter(rollOffDate) ){
	            logger.info( CAUSE_OF_ERROR + OpumConstants.INVALID_DATE_RANGE );
	        	throw new InvalidCSVException( object,OpumConstants.INVALID_DATE_RANGE );
	        }
	
			return true;
		}

	public static void checkIfStartAndEndDateValid(PUMYear pumYear) throws OpumException {
		LocalDate fromDate = null;
		LocalDate toDate = null;
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-M-d");
			fromDate = LocalDate.parse(pumYear.getStart(), formatter);
			toDate = LocalDate.parse(pumYear.getEnd(), formatter);
			if (fromDate.isAfter(toDate)) {
				logger.error(fromDate + " is greater than " + toDate);
				throw new OpumException("Error: " + OpumConstants.INVALID_DATE_RANGE);
			}
		} catch (DateTimeParseException e) {
			logger.error(e);
			throw new OpumException("Unable to parse Date!");
		}
	}

	public static void checkIfValidFiscalYear(PUMYear pumYear) throws OpumException {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-M-d");
		LocalDate toDate = LocalDate.parse(pumYear.getEnd(), formatter);
		try {
			if (toDate.getYear() != pumYear.getPumYear()) {
				String errorMessage = "Fiscal year " + pumYear.getPumYear() + " mismatch to end date "
						+ toDate.getYear();
				logger.error(errorMessage);
				throw new OpumException(errorMessage);
			}
		} catch (DateTimeParseException e) {
			logger.error(e);
			throw new OpumException("Unable to parse Date!");
		}
	}

	public static boolean isDateWithinFiscalYear(String d, PUMYear pumYear) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(YEAR_MONTH_DAY_FORMAT);
		LocalDate date = LocalDate.parse(d, formatter);
		LocalDate fromDate = LocalDate.parse(pumYear.getStart(), formatter);
		LocalDate toDate = LocalDate.parse(pumYear.getEnd(), formatter);
		if (date.isAfter(fromDate) && date.isBefore(toDate)) {
			return true;
        }

		return false;
	}
}
