package com.ph.ibm.validation.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.ph.ibm.model.Employee;
import com.ph.ibm.opum.exception.InvalidCSVException;
import com.ph.ibm.repository.EmployeeRepository;

/***
 * Test class for EmployeeValidator
 *
 * @author Melvin Rabang
 * @author Jason Tan
 *
 */
public class EmployeeValidatorTest {

	public EmployeeValidator validator;

	Employee validEmployee;

	Employee invalidEmployee;

	private static EmployeeRepository employeeRepository;

	@Before
	public void setup(){
		employeeRepository = Mockito.spy(EmployeeRepository.class);
		validator = Mockito.spy( new EmployeeValidator( employeeRepository ) );
		validEmployee = createValidEmployee();
		invalidEmployee = createInvalidEmployee();
	}

	@Test
	public void testValidateWhenEmployeeIsNotExisting() throws SQLException, InvalidCSVException, InvalidCSVException{
		Employee employee = createValidEmployee();

		Mockito.doReturn(false).when(validator).isEmployeeExisting(employee);

		assertTrue( validator.validate(employee) );
	}

	@Test(expected = InvalidCSVException.class)
	public void testValidateWhenInvalidDateRangeIsPresent() throws SQLException, InvalidCSVException, InvalidCSVException{
		Employee employee = new Employee();
		employee.setEmployeeSerial("P100Y2");
		employee.setFullName( "Jason Tan" );
		employee.setIntranetId( "tanjs@ph.ibm.com" );
		employee.setRollInDate( "9/18/2017" );
		employee.setRollOffDate( "02/31/2017" );

		Mockito.doReturn(false).when(validator).isEmployeeExisting(employee);

		validator.validate(employee);
	}

	@Test
	public void testValidIsEmployeeValueEmpty() throws InvalidCSVException, InvalidCSVException {
		Employee employee = createValidEmployee();

		assertFalse( validator.isEmployeeValueEmpty(employee) );
	}

	@Test(expected = InvalidCSVException.class)
	public void testOneFieldEmptyIsEmployeeValueEmpty() throws InvalidCSVException, InvalidCSVException {
		Employee employee = new Employee();
		employee.setEmployeeSerial("P100Y2");
		employee.setFullName( "Jason Tan" );
		employee.setIntranetId( "" );
		employee.setRollInDate( "9/18/2017" );
		employee.setRollOffDate( "12/31/2017" );

		validator.isEmployeeValueEmpty(employee);
	}

	@Test(expected = InvalidCSVException.class)
	public void testEmptyIsEmployeeValueEmpty() throws InvalidCSVException, InvalidCSVException {
		Employee employee = new Employee();
		validator.isEmployeeValueEmpty(employee);
	}

	@Test
	public void testValidIsValidEmailAddress() throws InvalidCSVException {
		Employee employee = new Employee();
		employee.setIntranetId( "tanjs@ph.ibm.com" );
		assertTrue( validator.isValidEmailAddress( employee ));
	}

	@Test(expected = InvalidCSVException.class)
	public void testSpecialCharacterIsValidEmailAddress() throws InvalidCSVException {
		Employee employee = new Employee();
		employee.setIntranetId( "t@a||njs@ph.ibm.com" );
		assertTrue(validator.isValidEmailAddress(employee));
	}

	@Test(expected = InvalidCSVException.class)
	public void testSpecialCharacterIsValidEmailAddress2() throws InvalidCSVException {
		Employee employee = new Employee();
		employee.setIntranetId( "tanjs@@ph.ibm.com" );
		validator.isValidEmailAddress(employee);
	}

	@Test(expected = InvalidCSVException.class)
	public void testSpecialCharacterIsValidEmailAddress3() throws InvalidCSVException {
		Employee employee = new Employee();
		employee.setIntranetId( "tanjs@ph.bim.mom" );
		validator.isValidEmailAddress(employee);
	}

	@Test(expected = InvalidCSVException.class)
	public void testSpecialCharacterIsValidEmailAddress4() throws InvalidCSVException {
		Employee employee = new Employee();
		employee.setIntranetId( "tanjs@ph.ibm.com.com" );
		validator.isValidEmailAddress(employee);
	}

	@Test(expected = InvalidCSVException.class)
	public void testInvalidIsValidEmailAddress() throws InvalidCSVException {
		Employee employee = new Employee();
		employee.setIntranetId( "ibm@ph.usaa.com" );
		validator.isValidEmailAddress(employee);
	}

	@Test(expected = InvalidCSVException.class)
	public void testEmptyIsValidEmailAddress() throws InvalidCSVException {
		Employee employee = new Employee();
		employee.setIntranetId( "" );
		validator.isValidEmailAddress(employee);
	}

	@Test
	public void testValidIsValidEmployeeId() throws InvalidCSVException
	{
		Employee employee = new Employee();
		employee.setEmployeeSerial( "P100Y2" );
		assertTrue( validator.isValidEmployeeSerial(employee) );
	}

	@Test
	public void testRandom6LetterValidIsValidEmployeeId() throws InvalidCSVException
	{
		Employee employee = new Employee();
		employee.setEmployeeSerial( "RaBangBangBang" );
		assertTrue( validator.isValidEmployeeSerial(employee));
	}

	@Test(expected = InvalidCSVException.class)
	public void testInvalidIsValidEmployeeId() throws InvalidCSVException
	{
		Employee employee = new Employee();
		employee.setEmployeeSerial( "P100Y" );
		validator.isValidEmployeeSerial(employee);
	}

	@Test(expected = InvalidCSVException.class)
	public void testRandom6LetterWithSpecialCharacterInvalidIsValidEmployeeId() throws InvalidCSVException
	{
		Employee employee = new Employee();
		employee.setEmployeeSerial( "M@l3No+" );
		validator.isValidEmployeeSerial(employee);
	}

	@Test
	public void testIsValidEmployeeName() throws InvalidCSVException {
		assertTrue(validator.isValidEmployeeName(validEmployee));
	}

	@Test(expected = InvalidCSVException.class)
	public void testIsValidEmployeeNameException() throws InvalidCSVException {
		validator.isValidEmployeeName(invalidEmployee);
	}

	@Test
	public void testIsEmployeeExisting() throws SQLException, InvalidCSVException {
		Employee nonExistingEmployee = createNonExistingEmployee();
		Mockito.doReturn(false).when(employeeRepository).doesEmployeeExist(nonExistingEmployee.getEmployeeSerial(),
				nonExistingEmployee.getIntranetId());
		assertFalse(validator.isEmployeeExisting(nonExistingEmployee));
	}

	@Test(expected = InvalidCSVException.class)
	public void testIsEmployeeExistingCSVException() throws SQLException, InvalidCSVException {
		Employee nonExistingEmployee = createNonExistingEmployee();

		Mockito.doReturn(true).when(employeeRepository).doesEmployeeExist(nonExistingEmployee.getEmployeeSerial(),
				nonExistingEmployee.getIntranetId());

		validator.isEmployeeExisting(nonExistingEmployee);
	}

	private Employee createNonExistingEmployee() {
		Employee nonExistingEmployee = new Employee();

		validEmployee.setEmployeeSerial("P100XX");
		validEmployee.setFullName("Juan DelaCruz");
		validEmployee.setIntranetId("delacruzj@ph.ibm.com");
		validEmployee.setRollInDate("9/20/2017");
		validEmployee.setRollOffDate("12/31/2017");

		return nonExistingEmployee;
	}

	private Employee createValidEmployee() {
		Employee validEmployee = new Employee();

		validEmployee.setEmployeeSerial("P100XJ");
		validEmployee.setFullName("Melvin Rabang");
		validEmployee.setIntranetId("rabangm@ph.ibm.com");
		validEmployee.setRollInDate("9/20/2017");
		validEmployee.setRollOffDate("12/31/2017");

		return validEmployee;
	}

	private Employee createInvalidEmployee() {
		Employee validEmployee = new Employee();

		validEmployee.setEmployeeSerial("invalid&*)^123");
		validEmployee.setFullName("invalid&*)^123");
		validEmployee.setIntranetId("invalid&*)^123");
		validEmployee.setRollInDate("invalid&*)^123");
		validEmployee.setRollOffDate("invalid&*)^123");

		return validEmployee;
	}

	@After
	public void teardown() {
		validator = null;
	}
}
