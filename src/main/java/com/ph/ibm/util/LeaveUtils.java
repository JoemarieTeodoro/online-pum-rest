package com.ph.ibm.util;

import java.sql.SQLException;

public class LeaveUtils {

	public static String removeLineBreaks(String resultSetInput) throws SQLException {
		return resultSetInput != null? resultSetInput.replaceAll("\n", "").trim():"";
	}
}