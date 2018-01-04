
package com.ph.ibm.util;

import static com.ph.ibm.util.OpumConstants.PERCENTAGE;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
/**
 * @author <a HREF="mailto:dacanam@ph.ibm.com">Marjay Dacanay</a>
 * @author <a HREF="mailto:balocaj@ph.ibm.com">Jerven Balocating</a>
 */
public class FormatUtils {

	public static final String DB_DATE_FORMAT = "yyyy-MM-dd";

    public static String toPercentage( double value ) {
        DecimalFormat dFormat = new DecimalFormat( "0.00'%'" );
        return dFormat.format( value * PERCENTAGE );
    }

	public static LocalDate toDBDateFormat(String date) {
		DateTimeFormatter df = DateTimeFormatter.ofPattern(DB_DATE_FORMAT);
		return LocalDate.parse(date, df);
	}

}
