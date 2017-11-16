package com.ph.ibm.util;

import static org.junit.Assert.*;

import org.junit.Test;

import com.ph.ibm.opum.exception.InvalidCSVException;

public class ValidationUtilsTest {
	@Test
	public void testIsValidRollInDate() throws InvalidCSVException {
		assertTrue(ValidationUtils.isValidDate(null,"1/21/2017"));
	}

	@Test(expected = InvalidCSVException.class)
	public void testIsValidRollInDateException() throws InvalidCSVException {
		ValidationUtils.isValidDate(null,"5/100/9999");
	}

	@Test
	public void testIsValidDateRange() throws InvalidCSVException {
		assertTrue(ValidationUtils.isValidDateRange(null,"9/20/2017", "12/31/2017"));
	}

	@Test(expected = InvalidCSVException.class)
	public void testIsValidDateRangeException() throws InvalidCSVException {
		ValidationUtils.isValidDateRange(null,"12/31/2017", "9/20/2017");
	}
}
