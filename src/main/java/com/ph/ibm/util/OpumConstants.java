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
	public static final String INVALID_NAME = "INVALID NAME";
	public static final String INVALID_PROJECT_NAME = "INVALID PROJECT NAME";
	public static final String INVALID_COMPANY_ID = "INVALID COMPANY ID";
	public static final String INVALID_EMAIL_ID = "INVALID EMAIL ID";
	public static final String INVALID_EMAIL_ADDRESS = "INVALID EMAIL ADDRESS";
	public static final String INVALID_EMPLOYEE_ID = "INVALID EMPLOYEE ID";
	public static final String INVALID_HOLIDAY = "INVALID HOLIDAY";
    public static final String EMPTY_CSV_VALUE = "EMPTY CSV VALUE";
	public static final String EMPLOYEE_ID_EMAIL_NOT_FOUND = "EMPLOYEE ID / EMAIL NOT FOUND";
	public static final String EMPLOYEE_ID_EMAIL_EXISTS = "EMPLOYEE ID / EMAIL ALREADY EXISTS";
	public static final String PROJECT_ENGAGEMENT_NOT_FOUND = "PROJECT ENGAGEMENT NOT FOUND";
	public static final String UNAUTHORIZED = "UNAUTHORIZED";
	public static final String ERROR_START_DATE = "ERROR START DATE";
	public static final String ERROR_END_DATE = "ERROR END DATE";
	public static final String YEAR_START_NOTFOUND = "YEAR START NOT FOUND";
	public static final String YEAR_END_NOT_FOUND = "YEAR END NOT FOUND";

    public static final String INVALID_DATE = "INVALID DATE FORMAT ->MM/DD/YYYY";
	
	/** NOTIFICATION MESSAGE/s */
	public static final String SUCCESSFULLY_SAVED = "SUCCESSFULLY SAVED";
	public static final String SUCCESSFULLY_SAVED_DATA = "SUCCESSFULLY SAVED DATA";
	public static final String SUCCESSFULLY_REGISTERED = "SUCCESSFULLY REGISTERED";
	public static final String SUCCESSFULLY_UPLOADED_FILE = "SUCCESSFULLY UPLOADED FILE";
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

}
