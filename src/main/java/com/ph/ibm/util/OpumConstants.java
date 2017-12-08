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
    public static final String EMPLOYEE_EMAIL_EXISTS = "EMPLOYEE EMAIL ALREADY EXISTS";
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

	/** NOTIFICATION MESSAGE/s */
	public static final String SUCCESSFULLY_SAVED = "SUCCESSFULLY SAVED";
	public static final String SUCCESSFULLY_SAVED_DATA = "SUCCESSFULLY SAVED DATA";
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
	public static final String EMAIL_SUBJECT = "ACTION REQUIRED: RESET PASSWORD";
	public static final String EMAIL_GREETING = "DEAR IBMer,";
	public static final String EMAIL_BODY = "You can reset your password by clicking the link below:";
	public static final String EMAIL_CLOSING = "Thank you,";
	public static final String EMAIL_SIGNATURE = "Online PUM Support";

    public static final String SYS_ADMIN ="system administrator";
    public static final String ADMIN_FULL_FORM = "administrator";
    public static final String USER = "user";
    public static final String PEM = "people manager";
    public static final String TEAM_LEAD = "team lead";

    public static final String SUCCESS_UPLOAD = "CSV Uploaded Successfully!";

	/** PUM Year **/
	public static final String ZERO = "0";
	public static final String EIGHT = "8";

}
