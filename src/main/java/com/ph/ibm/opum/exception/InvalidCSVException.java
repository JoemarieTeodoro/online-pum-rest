package com.ph.ibm.opum.exception;

public class InvalidCSVException extends Exception {

    private static final long serialVersionUID = -2789532735929540288L;

    private Object myObject;

    private String myError;

    public InvalidCSVException( Object object, String error ) {
        super();
        myObject = object;
        myError = error;
    }
    public Object getObject() {
        return myObject;
    }

    public String getError() {
        return myError;
    }
}