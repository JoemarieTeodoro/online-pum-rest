package com.ph.ibm.opum.exception;

import com.ph.ibm.model.Employee;

public class InvalidCSVException extends Exception {

    private static final long serialVersionUID = -2789532735929540288L;

    private Employee myEmployee;

    private String myError;

    public InvalidCSVException( Employee employee, String error ) {
        super();
        myEmployee = employee;
        myError = error;
    }

    public Employee getEmployee() {
        return myEmployee;
    }

    public String getError() {
        return myError;
    }

}