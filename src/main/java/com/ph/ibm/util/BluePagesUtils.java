
package com.ph.ibm.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import com.ibm.bluepages.BPResults;
import com.ibm.bluepages.BluePages;
import com.ph.ibm.model.Employee;
import com.ph.ibm.opum.exception.InvalidCSVException;

/**
 * @author <a HREF="dacanam@ph.ibm.com">Marjay Dacanay</a>
 * @author <a HREF="teodorj@ph.ibm.com">Joemarie Teodoro</a>
 */
public class BluePagesUtils {
    private static final String EMPLOYEE_SERIAL_COLUMN = "EMPNUM";
    private static final String MANAGER_SERIAL_COLUMN = "MGRNUM";
    private static final String INTRANET_COLUMN = "INTERNET";
    /**
     * @param employee
     * @return
     * @throws InvalidCSVException
     */
    public static String getManagerSerial( Employee employee ) {

        Map<String, Object> apiParms = new HashMap<String, Object>();
        apiParms.put( BluePages.WSAPI_QUERY_ENCODING, "UTF-8" );
        apiParms.put( BluePages.SLAPHAPI_SEARCH_ENCODING, "ISO-8859-1" );

        BPResults resultsForEmployee =
            BluePages.callWSAPI( BluePages.WSAPI_QUERY_BYSERIAL, employee.getEmployeeSerial(), apiParms );
        Vector<String> employeeSerialVector = resultsForEmployee.getColumn( MANAGER_SERIAL_COLUMN );
        return employeeSerialVector.get( 0 );

    }


    /**
     * @param serial
     * @return
     */
    public static boolean isEmployeeSerialFound( String serial ) {
        Map<String, Object> apiParms = new HashMap<String, Object>();
        apiParms.put( BluePages.WSAPI_QUERY_ENCODING, "UTF-8" );
        apiParms.put( BluePages.SLAPHAPI_SEARCH_ENCODING, "ISO-8859-1" );

        BPResults resultsForEmployee = BluePages.callWSAPI( BluePages.WSAPI_QUERY_BYSERIAL, serial, apiParms );
        Vector<String> employeeSerialVector = resultsForEmployee.getColumn( EMPLOYEE_SERIAL_COLUMN );

        return employeeSerialVector.size() != 0;
    }

    /**
     * @param serial
     * @param email
     * @return
     */
    public static boolean isEmployeeIntranetFound( String serial, String email ) {
        Map<String, Object> apiParms = new HashMap<String, Object>();
        apiParms.put( BluePages.WSAPI_QUERY_ENCODING, "UTF-8" );
        apiParms.put( BluePages.SLAPHAPI_SEARCH_ENCODING, "ISO-8859-1" );

        BPResults resultsForEmployee = BluePages.callWSAPI( BluePages.WSAPI_QUERY_BYSERIAL, serial, apiParms );
        Vector<String> employeeSerialVector = resultsForEmployee.getColumn( INTRANET_COLUMN );

        return employeeSerialVector.get( 0 ).equalsIgnoreCase( email );

    }

}
