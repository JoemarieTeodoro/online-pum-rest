package com.ph.ibm.opum.exception;

public class InvalidPEMException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8997404394909247783L;
	
	private String error;

    public InvalidPEMException( String error ) {
    	this.setError(error);
    }

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

}