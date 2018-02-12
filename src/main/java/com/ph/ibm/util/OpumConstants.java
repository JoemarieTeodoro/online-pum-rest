package com.ph.ibm.util;

/**
 * This class contain constants used for notification, validation error, mysql error message/s
 */
public class OpumConstants {
	
	/** MYSQL ERROR MESSAGE/s */
	public static final int MYSQL_DUPLICATE_PK_ERROR_CODE = 1062;

	/** VALIDATION ERROR MESSAGE/s */
	public static final String UNABLE_TO_ESTABLISH_CONNECTION = "UNABLE TO ESTABLISH CONNECTION";
	public static final String DUPLICATE_ENTRY = "DUPLICATE ENTRY";
	public static final String SQL_ERROR = "SQL EXCEPTION";
	public static final String ERROR = "ERROR";
	public static final String ERROR_WHEN_SAVING = "ERROR WHEN SAVING";
	public static final String INVALID_CSV = "INVALID CSV";
    public static final String EMPTY_CSV = "CSV FILE SHOULD NOT BE EMPTY";
	public static final String INVALID_NAME = "INVALID NAME";
	public static final String INVALID_PROJECT_NAME = "INVALID PROJECT NAME";
	public static final String INVALID_COMPANY_ID = "INVALID COMPANY ID";
	public static final String INVALID_EMAIL_ID = "INVALID EMAIL ID";
	public static final String INVALID_EMAIL_ADDRESS = "INVALID EMAIL ADDRESS";
	public static final String INVALID_EMPLOYEE_ID = "INVALID EMPLOYEE ID";
	public static final String INVALID_PEM_ID = "INVALID PEM ID";
	public static final String INVALID_HOLIDAY = "INVALID HOLIDAY";
    public static final String INVALID_DATE = "INVALID DATE FORMAT";
	public static final String INVALID_DATE_RANGE = "INVALID DATE RANGE";
    public static final String INVALID_TEAM_NAME = "INVALID TEAM NAME";
    public static final String INVALID_RECOVERABLE = "INVALID RECOVERABLE STATUS";
    public static final String INVALID_TEAM_LEAD_SERIAL = "INVALID TEAM LEAD SERIAL NUMBER";
    public static final String EMPLOYEE_ID_EXISTS = "EMPLOYEE ID ALREADY EXISTS";
    public static final String EMPLOYEE_EMAIL_NOT_FOUND = "EMPLOYEE EMAIL DID NOT MATCH IN IBM DIRECTORY";
    public static final String TEAM_NAME_EXISTS = "TEAM ALREADY EXIST";
	public static final String PEM_ID_EXISTS = "PEM ID ALREADY EXISTS";
	public static final String EMPLOYEE_ROLE_EXISTS = "EMPLOYEE ROLE ALREADY EXISTS";
	public static final String EMPLOYEE_SERIAL_DOES_NOT_EXIST = "EMPLOYEE SERIAL DOES NOT EXIST";
	public static final String EMPLOYEE_ROLE_ID_DOES_NOT_EXIST = "EMPLOYEE ROLE ID DOES NOT EXIST";
	public static final String PROJECT_ENGAGEMENT_NOT_FOUND = "PROJECT ENGAGEMENT NOT FOUND";
	public static final String UNAUTHORIZED = "UNAUTHORIZED";
	public static final String ERROR_START_DATE = "ERROR START DATE";
	public static final String ERROR_END_DATE = "ERROR END DATE";
    public static final String YEAR_START_NOT_FOUND = "YEAR START NOT FOUND";
	public static final String YEAR_END_NOT_FOUND = "YEAR END NOT FOUND";
	public static final String INVALID_EMPLOYEE_ROLE = "INVALID EMPLOYEE ROLE";
    public static final String EMPTY_CSV_ERROR = "CSV CONTENTS SHOULD NOT BE EMPTY";
    public static final String EMPTY_CSV_VALUE = "EMPTY CSV VALUE";
    public static final String EMPTY_CSV_ENTRIES_EMPLOYEE_ROLE = "EMPTY ENTRY EMPLOYEE ROLE CSV";
    public static final String EMPTY_EMPLOYEE_ROLE = "EMPLOYEE ROLE SHOULD NOT BE EMPTY";
    public static final String EMPTY_EMPLOYEE_SERIAL = "EMPLOYEE SERIAL SHOULD NOT BE EMPTY";
    public static final String EMPLOYEE_HAS_NO_TEAM = "EMPLOYEE HAS NO CORRESPONDING TEAM YET";
    public static final String EMPLOYEE_SERIAL_DOES_NOT_EXIST_IBM = "EMPLOYEE SERIAL DOES NOT EXIST IN IBM DIRECTORY";
    public static final String INVALID_DESIGNATION = "INVALID EMPLOYEE DESIGNATION";

	/** NOTIFICATION MESSAGE/s */
	public static final String SUCCESSFULLY_SAVED = "SUCCESSFULLY SAVED";
	public static final String SUCCESSFULLY_SAVED_DATA = "SUCCESSFULLY SAVED DATA";
	public static final String SUCCESSFULLY_SAVED_UPDATED_DATA = "SUCCESSFULLY SAVED UPDATED DATA";
	public static final String SUCCESSFULLY_REGISTERED = "SUCCESSFULLY REGISTERED";
	public static final String SUCCESSFULLY_UPLOADED_FILE = "SUCCESSFULLY UPLOADED FILE";
	public static final String SUCCESSFULLY_EMAILED_LIST_OF_EMAIL_ADDRESS = "SUCCESSFULLY EMAILED LIST OF EMAIL ADDRESS";
    public static final String NO_HEADER_FOUND = "NO HEADER FOUND IN CSV";
	public static final String LOGGED_IN = "LOGGED IN";
	public static final String ENTERING_USER_PAGE = "ENTERING USER PAGE";
	public static final String ENTERING_SYS_ADMIN_PAGE = "ENTERING SYSTEM ADMIN PAGE";
	public static final String ENTERING_ADMIN_PAGE = "ENTERING ADMIN PAGE";
	public static final String OPENING_BROWSER = "OPENING BROWSER";
	public static final String RETURNING_TO_LOGIN_PAGE = "RETURNING TO LOGIN PAGE";
	public static final String INSERTED_SUCCESS = "INSERTED SUCCESS";
	public static final String UPDATED_SUCCESS = "UPDATED SUCCESS";
	public static final String DELETED_SUCCESS = "DELETED SUCCESS";
    public static final String ADMIN = "ADMIN";

	/** EMAIL MESSAGES */
	public static final String EMAIL_SENDER = "onlinepumsupport@isc4sb.com";
	public static final String EMAIL_PASSWORD = "onlinepum";
	public static final String EMAIL_PORT = "25";
	public static final String EMAIL_SMTP_MAIL_HOST = "ap.relay.ibm.com";
	public static final String EMAIL_SUBJECT = "ACTION REQUIRED: RESET PASSWORD";
	public static final String EMAIL_GREETING = "DEAR IBMer,";
	public static final String EMAIL_BODY = "You can reset your password by clicking the link below:";
	public static final String EMAIL_CLOSING = "Thank you,";
	public static final String EMAIL_SIGNATURE = "Online PUM Support";

	/** APPROVE EMAIL MESSAGES */
	public static final String APPROVE_EMAIL_SUBJECT = "PUM NOTIFICATION: Leaves Approved for today";
	public static final String APPROVE_EMAIL_BODY = "Below is a table that represents the leaves approved today. "
			+ "They were approved by ";

	/** REJECT EMAIL MESSAGES */
	public static final String REJECT_EMAIL_SUBJECT = "PUM NOTIFICATION: Leaves Rejected for today";
	public static final String REJECT_EMAIL_BODY = "The leave dates that were rejected are mentioned below. " +
			"They were rejected by ";

    public static final String SYS_ADMIN ="system administrator";
    public static final String ADMIN_FULL_FORM = "administrator";
    public static final String USER = "user";
    public static final String PEM = "people manager";
    public static final String TEAM_LEAD = "team lead";

    public static final String SUCCESS_UPLOAD = "CSV Uploaded Successfully!";
    
    public static final String ILC_SUCCESS_UPLOAD = "ILC Extract Uploaded Successfully!";

	/** PUM Year **/
	public static final String ZERO = "0";
	public static final String EIGHT = "8";


	/** Error Messages **/
	public static final String FILL_HOLIDAY_NAME_AND_OR_HOLIDAY_DATE = "Please fill Holiday Name and/or Holiday Date";
	public static final String FISCAL_YEAR_NOT_DEFINED = "Fiscal Year not yet defined!";
	public static final String HOLIDAY_DATE_NOT_WITHIN_FISCAL_YEAR = "Holiday Date is not within Fiscal Year date range!";

    /** Utilization Constants **/

    public static final int TOTAL_NUMBER_OF_WEEKS = 52;
    public static final int TOTAL_WEEKLY_HOURS = 40;
    public static final int COUNT_OF_WEEKS_PER_QUARTER = 13;
    public static final int COUNT_OF_WEEKS_PER_MONTH = 4;
    public static final int COUNT_OF_MONTHS_PER_QUARTER = 3;
    public static final int COUNT_OF_BUSINESS_DAYS_PER_WEEK = 5;
    public static final int COUNT_OF_DAYS_PER_WEEK = 7;
    public static final int COUNT_OF_MONTHS_PER_YEAR = 12;
    public static final int PERCENTAGE = 100;
    public static final String ACTUAL_UTILIZATION = "ACTUAL";
    public static final String FORECAST_UTILIZATION = "FORECAST";
    
    /** Holiday messages **/
	public static final String MSG_UPDATED_HOLIDAY = "Holiday updated!";
	public static final String MSG_ERROR_UPDATING_HOLIDAY = "Error updating holiday!";
	public static final String MSG_DELETED_HOLIDAY = "Holiday deleted!";
	public static final String MSG_ERROR_DELETING_HOLIDAY = "Error deleting holiday!";

}
