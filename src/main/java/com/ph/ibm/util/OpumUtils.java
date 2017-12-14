
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
public class OpumUtils {

    /**
     * @param employee
     * @return
     * @throws InvalidCSVException
     */
    public static Employee getEmployeeSerialAPI( Employee employee ) throws InvalidCSVException {

        String employeeSerialColumn = "EMPNUM";
        String managerSerialColumn = "MGRNUM";
        /*  String intranetIDColumn = "INTERNET";
        String firstNameColumn = "HRFIRSTNAME";
        String lastNameColumn = "HRLASTNAME";*/
        
        employee.setEmployeeSerial( retrieveData( employee.getEmployeeSerial(), employeeSerialColumn ) );
        employee.setManagerSerial( retrieveData( employee.getEmployeeSerial(), managerSerialColumn ) );
        /* employee.setIntranetId( retrieveData( employee.getEmployeeSerial(), intranetIDColumn ) );
        employee.setFullName( retrieveData( employee.getEmployeeSerial(), firstNameColumn ) + " " +
            retrieveData( employee.getEmployeeSerial(), lastNameColumn ) );*/
        return employee;
    }

    /**
     * @param empNum
     * @param columnData
     * @return
     * @throws InvalidCSVException
     */
    protected static String retrieveData( String empNum, String columnData ) throws InvalidCSVException {

        Map<String, Object> apiParms = new HashMap<String, Object>();
        apiParms.put( BluePages.WSAPI_QUERY_ENCODING, "UTF-8" );
        apiParms.put( BluePages.SLAPHAPI_SEARCH_ENCODING, "ISO-8859-1" );
        String results;

        BPResults resultsForEmployee = BluePages.callWSAPI( BluePages.WSAPI_QUERY_BYSERIAL, empNum, apiParms );
        Vector<String> employeeSerialVector = resultsForEmployee.getColumn( columnData );

        if( employeeSerialVector.size() != 0 ){
            results = employeeSerialVector.get( 0 );
        }
        else{
            throw new InvalidCSVException( null, OpumConstants.EMPLOYEE_SERIAL_DOES_NOT_EXIST_IBM );
        }
        return results;

    }

}
