package com.ph.ibm.model;

public enum Role {
	SYS_ADMIN("System Administrator"),
	ADMIN("Administrator"),
	USER("User"),
	PEM("People Manager"),
	TEAM_LEAD("Team Lead");
	
	private String roleValue;
	
	Role(String roleValue) {
		this.roleValue = roleValue;
	}
	
	public boolean equals(String aRole) {
		return this.roleValue.equalsIgnoreCase(aRole);
	}
}
