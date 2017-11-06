package com.ph.ibm.bo;

public enum TimeAwayTokens {

	CDO("CDO"),
	VL("VL"),
	SL("SL"),
	EL("EL"),
	OL("OL"),
	HO("HO"),
	TR("TR");
	
	String token;
	
	TimeAwayTokens(String token) {
		this.token = token;
	}

	public String getS() {
		return token;
	}
	
	public boolean equals(String aToken) {
		return this.token.equalsIgnoreCase(aToken);
	}
}
