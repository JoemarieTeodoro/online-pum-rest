package com.ph.ibm.util;

import java.time.LocalDateTime;

import com.ph.ibm.model.UtilizationJson;
import com.ph.ibm.model.UtilizationYear;

/**
 * This class is used to convert JSON into Java Object
 */
public class JSONToJava {

	/**
	 * This method is used to set editable key of JSON to either E or D based on certain condition
	 * 
	 * @param jsonEmp
	 * @return utilizationYear object
	 */
	public UtilizationYear jsonToJava(String jsonEmp) {
		LocalDateTime now = LocalDateTime.now();
		int currentYear = now.getYear();
		int currentMonth = now.getMonthValue();
		int currentDay = now.getDayOfMonth();
		UtilizationYear utilizationYear = ObjectMapperAdapter.unmarshal(jsonEmp, UtilizationYear.class);
		int counter = 0;

		for (UtilizationJson json : utilizationYear.getUtilizationJSON()) {
			if (utilizationYear.getYear() < currentYear) {
				json.setEditable("D");

				counter++;
			} else if (utilizationYear.getYear() == currentYear) {
				if (json.getMonth() < currentMonth) {
					json.setEditable("D");
					counter++;
				} else if (json.getMonth() == currentMonth) {
					if (json.getDayOfMonth() < currentDay) {
						json.setEditable("D");
						counter++;
					} else {
						json.setEditable("E");
						counter++;
					}
				} else {
					json.setEditable("E");
					counter++;
				}
			} else {
				json.setEditable("E");
				counter++;
			}
		}
		return utilizationYear;
	}
}