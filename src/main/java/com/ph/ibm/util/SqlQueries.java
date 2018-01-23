package com.ph.ibm.util;

/**
 * @author <a HREF="mailto:dacanam@ph.ibm.com">Marjay Dacanay</a>
 * @author <a HREF="mailto:balocaj@ph.ibm.com">Jerven Balocating</a>
 */
public class SqlQueries {

    //PUM YEAR REPOSITORY IMPL
    public static final String SQL_RETRIEVE_YEAR_DATE =
        "SELECT YEAR_ID, PUMYEAR, END, START, CREATEDATE, CREATEDBY, UPDATEDATE, UPDATEDBY FROM YEAR WHERE PUMYEAR = ?";

    public static final String SQL_EDIT_YEAR = "UPDATE YEAR SET START= ?, END= ? WHERE PUMYEAR= ?;";

    public static final String SQL_RETRIEVE_CURRENT_FY = "SELECT * FROM YEAR ORDER BY PUMYEAR DESC LIMIT 1";

    public static final String SQL_UPDATE_FISCAL_YEAR = " UPDATE opum.year SET start = ?, end = ? WHERE pumyear = ?; ";

    public static final String SQL_DELETE_FY_TEMPLATE = " DELETE FROM opum.fy_template WHERE year_id = ?; ";

    public static final String SQL_DELETE_FY_QUARTER = " DELETE FROM opum.quarter WHERE year_id = ?; ";

    public static final String SQL_DELETE_FY_WEEK = " DELETE FROM opum.week WHERE year_id = ?; ";

    public static final String SQL_POPULATE_FY_WEEKS =
        " INSERT INTO opum.week(QUARTER_ID, NAME, YEAR_ID, START, END) " + " Values (?,?,?,?,?); ";

    public static final String SQL_POPULATE_FY_QUARTER =
        " INSERT INTO opum.quarter(YEAR_ID, NAME, START, END) " + " Values (?,?,?,?); ";

    public static final String SQL_SAVE_YEAR = "INSERT INTO YEAR (START,END,PUMYEAR,CREATEDBY) VALUES (?,?,?,?); ";

    public static final String SQL_POPULATE_FY =
        " INSERT INTO opum.fy_template(YEAR_ID, DATE, VALUE, IS_HOLIDAY, EVENT_NAME) " + " Values (?,?,?,?,?); ";

    public static final String SQL_ADD_OR_UPDATE_HOLIDAY =
        "UPDATE opum.fy_template SET value = 0, is_holiday = 1, event_name = ? WHERE date = ? AND YEAR_ID = ?; ";

    public static final String SQL_CHECK_PUM_CYLCE = "select count(*) from opum.year where PUMYear = ?; ";

    public static final String SQL_SAVE_QUARTER =
        "INSERT INTO QUARTER (" + "START,END,PUMQUARTER) " + "VALUES (?,?,?); ";

    public static final String SQL_SAVE_MONTH = "INSERT INTO MONTH (" + "START,END,PUMMONTH) " + "VALUES (?,?,?); ";

    public static final String SQL_RETRIEVE_ALL_YEAR =
        "SELECT YEAR_ID, PUMYEAR, END, START, CREATEDATE, CREATEDBY, UPDATEDATE, UPDATEDBY FROM YEAR;";

    public static final String SQL_POPULATE_UTILIZATION = "INSERT INTO UTILIZATION(EMPLOYEE_SERIAL, TYPE, YEAR_ID) VALUES (?,?,?)";

    public static final String SQL_GET_EMPLOYEE_LIST = "SELECT DISTINCT EMPLOYEE_ID FROM EMPLOYEE_ROLE WHERE ROLE_ID = ?";
    //UTILIZATION REPOSITORY IMPL
    public static final String SQL_GET_QUARTERLY_UTILIZATION_HOURS =
        "SELECT SUM((CASE WHEN (EL.LEAVE_TYPE = 'RC') THEN EL.HOURS" +
            "          WHEN (EL.STATUS = 'Approved') THEN 0 ELSE FT.VALUE " +
            "     END)) AS HOURS FROM QUARTER Q LEFT JOIN( " +
            "                FY_TEMPLATE FT LEFT JOIN EMPLOYEE_LEAVE EL " +
            "                    ON ((FT.DATE = EL.LEAVE_DATE) " + "                    AND EL.EMPLOYEE_ID = ? )) " +
            "                ON ( FT.DATE BETWEEN Q.START AND Q.END) WHERE FT.YEAR_ID = ? " +
            "                GROUP BY Q.QUARTER_ID " +
            "                ORDER BY DATE;";

  
    public static final String SQL_GET_WEEKLY_UTILIZATION_HOURS = 
                  " SELECT SUM(                   " +
                  "   (CASE WHEN (EL.LEAVE_TYPE = 'RC') THEN EL.HOURS " +
                  "           WHEN (EL.STATUS = 'Approved') THEN 0    " +
                  "           ELSE FT.VALUE                           " +
                  "      END)) AS HOURS                               " +
                  " FROM                                              " +
                  "   WEEK W LEFT JOIN (                              " +
                  "       FY_TEMPLATE FT LEFT JOIN EMPLOYEE_LEAVE EL  " +
                  "           ON ((FT.DATE = EL.LEAVE_DATE)           " +
                  "               AND EL.EMPLOYEE_ID = ?))            " +
                  "       ON ( FT.DATE BETWEEN W.START AND W.END)     " +
                  " WHERE                                             " +
                  "               FT.YEAR_ID = ?                      " +
                  " GROUP BY W.WEEK_ID                                " +
                  " ORDER BY DATE                                     ";
    
    public static final String SQL_UPDATE_UTILIZATION_HOURS = 
        "UPDATE UTILIZATION " +
            "   SET Week1 = ?, Week2 = ?, Week3 = ?, Week4 = ?, Week5 = ?, Week6 = ?, Week7 = ?, Week8 = ?, Week9 = ?, Week10 = ?, Week11 = ?, Week12 = ?, Week13 = ?, " +
            "    Week14 = ?, Week15 = ?, Week16 = ?, Week17 = ?, Week18 = ?, Week19 = ?, Week20 = ?, Week21 = ?, Week22 = ?, Week23 = ?, Week24 = ?, Week25 = ?, Week26 = ?, " +
            "   Week27 = ?, Week28 = ?, Week29 = ?, Week30 = ?, Week31 = ?, Week32 = ?, Week33 = ?, Week34 = ?, Week35 = ?, Week36 = ?, Week37 = ?, Week38 = ?, Week39 = ?, " +
            "   Week40 = ?, Week41 = ?, Week42 = ?, Week43 = ?, Week44 = ?, Week45 = ?, Week46 = ?, Week47 = ?, Week48 = ?, Week49 = ?, Week50 = ?, Week51 = ?, Week52 = ? " +
            " WHERE YEAR_ID = ? AND EMPLOYEE_SERIAL = ? AND TYPE = ? ";
    
    public static final String SQL_INSERT_ACTUAL_UTILIZATION_HOURS =
			    " INSERT INTO UTILIZATION(EMPLOYEE_SERIAL, TYPE, YEAR_ID, "
				    	+ "Week1,Week2,Week3,Week4,Week5,Week6,Week7,Week8,Week9,Week10,Week11,Week12,Week13,"
				  	  + "Week14,Week15,Week16,Week17,Week18,Week19,Week20,Week21,Week22,Week23,Week24,Week25,Week26,"
				  	  + "Week27,Week28,Week29,Week30,Week31,Week32,Week33,Week34,Week35,Week36,Week37,Week38,Week39,"
				  	  + "Week40,Week41,Week42,Week43,Week44,Week45,Week46,Week47,Week48,Week49,Week50,Week51,Week52) "
				  	  +
    		  		" VALUES(?,?,?, " +
    		  		" ?,?,?,?,?,?,?,?,?,?,?,?,?, " +
    		  		" ?,?,?,?,?,?,?,?,?,?,?,?,?, " +
    		  		" ?,?,?,?,?,?,?,?,?,?,?,?,?, " +
				  	  " ?,?,?,?,?,?,?,?,?,?,?,?,? )";
    
	  public static final String SQL_UPDATE_ACTUAL_UTILIZATION_HOURS = 
            "UPDATE UTILIZATION " +
                "   SET Week1 = ?, Week2 = ?, Week3 = ?, Week4 = ?, Week5 = ?, Week6 = ?, Week7 = ?, Week8 = ?, Week9 = ?, Week10 = ?, Week11 = ?, Week12 = ?, Week13 = ?, " +
                "    Week14 = ?, Week15 = ?, Week16 = ?, Week17 = ?, Week18 = ?, Week19 = ?, Week20 = ?, Week21 = ?, Week22 = ?, Week23 = ?, Week24 = ?, Week25 = ?, Week26 = ?, " +
                "   Week27 = ?, Week28 = ?, Week29 = ?, Week30 = ?, Week31 = ?, Week32 = ?, Week33 = ?, Week34 = ?, Week35 = ?, Week36 = ?, Week37 = ?, Week38 = ?, Week39 = ?, " +
                "   Week40 = ?, Week41 = ?, Week42 = ?, Week43 = ?, Week44 = ?, Week45 = ?, Week46 = ?, Week47 = ?, Week48 = ?, Week49 = ?, Week50 = ?, Week51 = ?, Week52 = ? " +
                " WHERE YEAR_ID = ? AND EMPLOYEE_SERIAL = ? AND TYPE = ? ";

	  public static final String SQL_GET_EMPLOYEE_UTILIZATION = " SELECT * FROM opum.utilization WHERE employee_serial = ? AND year_id = ? AND type = ? ";

    public static final String SQL_GET_EMPLOYEE_COMBINED_UTILIZATION = "CALL getCombinedUtilization(?,?)";

    public static final String SQL_SAVE_UTILIZATION = "INSERT INTO UTILIZATION (" + "EMPLOYEE_ID, YEAR, " +
    		"UTILIZATION_JSON, CREATEDBY, UPDATEDBY) VALUES (?,?,?,?,?);";

    public static final String SQL_UPDATE_UTILIZATION = "UPDATE UTILIZATION SET UTILIZATION_JSON = ?, UPDATEDBY = ? " +
        "WHERE EMPLOYEE_ID = ? AND YEAR = ?";

    public static final String SQL_RETRIEVE_UTILIZATION = "SELECT EMPLOYEE_ID, YEAR, UTILIZATION_JSON FROM " +
    		"UTILIZATION WHERE EMPLOYEE_ID = ? AND YEAR = ?";

    public static final String SQL_QUERY_FOR_DOWNLOAD_UTILIZATION = "SELECT UTILIZATION.YEAR, " +
        "UTILIZATION.UTILIZATION_JSON, EMPLOYEE.EMPLOYEE_ID FROM UTILIZATION " +
		    "JOIN EMPLOYEE ON UTILIZATION.EMPLOYEE_ID=EMPLOYEE.EMPLOYEE_ID " +
		    "WHERE UTILIZATION.YEAR = ? AND UTILIZATION.EMPLOYEE_ID =?";

    public static final String SQL_QUERY_FOR_DOWNLOAD_UTILIZATION_REPORT = "SELECT EMPLOYEE_ID, YEAR, UTILIZATION_JSON FROM " +
    		"UTILIZATION WHERE EMPLOYEE_ID = ? AND YEAR = ?";

	// HOLIDAY REPOSITORY IMPL
	public static final String SQL_UPDATE_HOLIDAY_NAME = "UPDATE HOLIDAY SET NAME = ? WHERE DATE = ? AND YEAR_ID = ?;";

	public static final String SQL_DELETE_HOLIDAY = "DELETE FROM HOLIDAY WHERE DATE = ? AND YEAR_ID = ?; ";

	public static final String SQL_DELETE_HOLIDAY_IN_FY_TEMPLATE = "UPDATE FY_TEMPLATE SET VALUE = ?, IS_HOLIDAY = 0, EVENT_NAME = '' WHERE DATE = ? AND YEAR_ID = ?; ";

	public static final String SQL_UPDATE_HOLIDAY_IN_FY_TEMPLATE = "UPDATE FY_TEMPLATE SET EVENT_NAME = ? WHERE DATE = ? AND YEAR_ID = ?; ";
}
