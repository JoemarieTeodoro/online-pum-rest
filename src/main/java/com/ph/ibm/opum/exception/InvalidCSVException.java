package com.ph.ibm.opum.exception;

import com.ph.ibm.model.Employee;

public class InvalidCSVException extends Exception {

    private static final long serialVersionUID = -2789532735929540288L;

    private Employee myEmployee;

    public InvalidCSVException( Employee employee ) {
        super();
        myEmployee = employee;
    }

    public Employee getEmployee() {
        return myEmployee;
    }

}