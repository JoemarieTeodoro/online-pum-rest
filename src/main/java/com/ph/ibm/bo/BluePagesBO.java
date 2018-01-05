package com.ph.ibm.bo;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import com.ibm.bluepages.BPResults;
import com.ibm.bluepages.BluePages;

public class BluePagesBO {

	public static final String WSAPI_PEM_SERIAL_COLUMN = "MGRNUM";
	public static final String WSAPI_PEM_EMAIL_COLUMN = "INTERNET";

	public String getPemEmailUsingEmployeeSerial(String employeeSerial) {
		String pemSerial = getPemSerialUsingBluePagesWSApi(employeeSerial);
		String pemEmail = getPemEmailUsingBluePagesWSApi(pemSerial);

		return pemEmail;
	}

	private String getPemEmailUsingBluePagesWSApi(String pemSerial) {
		String pemEmail = "";
		BPResults resultsForPEM =
				BluePages.callWSAPI(BluePages.WSAPI_QUERY_BYSERIAL, pemSerial, setBluePagesWSApiParams());
        Vector<String> pemEmailVector = resultsForPEM.getColumn(WSAPI_PEM_EMAIL_COLUMN);

        if (pemEmailVector != null) {
        	pemEmail = pemEmailVector.get(0);
        }

        return pemEmail;
	}

	private String getPemSerialUsingBluePagesWSApi(String employeeSerial) {
		String pemSerial = "";
		BPResults resultsForEmployee =
				BluePages.callWSAPI(BluePages.WSAPI_QUERY_BYSERIAL, employeeSerial, setBluePagesWSApiParams());
        Vector<String> pemSerialVector = resultsForEmployee.getColumn(WSAPI_PEM_SERIAL_COLUMN);

        if (pemSerialVector != null) {
        	pemSerial = pemSerialVector.get(0);
        }

        return pemSerial;
	}

	private Map<String, Object> setBluePagesWSApiParams() {
        Map<String, Object> apiParms = new HashMap<String, Object>();
        apiParms.put(BluePages.WSAPI_QUERY_ENCODING, "UTF-8");
        apiParms.put(BluePages.SLAPHAPI_SEARCH_ENCODING, "ISO-8859-1");
        return apiParms;
	}
}
