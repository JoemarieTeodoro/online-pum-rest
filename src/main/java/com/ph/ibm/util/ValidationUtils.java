package com.ph.ibm.util;

import org.apache.log4j.Logger;

import com.ph.ibm.opum.exception.InvalidEmployeeException;

public class ValidationUtils {
	/** Logger is used to document the execution of the system and logs the corresponding log level such as INFO, WARN, ERROR */
	private static Logger logger = Logger.getLogger(ValidationUtils.class);
	
	public static boolean regexValidator(String stringToValidate, String patternTomatch, String errorConstant) throws InvalidEmployeeException {
		boolean isValid = stringToValidate.matches(patternTomatch);
		if (!isValid) {
			logger.info("CAUSE OF ERROR: " + errorConstant);
			throw new InvalidEmployeeException(errorConstant);
		}
		return isValid;
	}
}
