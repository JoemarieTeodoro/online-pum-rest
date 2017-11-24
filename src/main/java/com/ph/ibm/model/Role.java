package com.ph.ibm.model;

public enum Role {
    SYS_ADMIN( 1, "SYSTEM ADMINISTRATOR" ), 
    ADMIN( 2, "ADMINISTRATOR" ), 
    USER( 3, "USER" ), 
    PEM( 4, "PEOPLE MANAGER" ),
    TEAM_LEAD( 5, "TEAM LEAD" );

	private String roleValue;
	private int roleId;

	Role (int roleId, String roleValue) {
		this.roleId = roleId;
		this.roleValue = roleValue;
	}

	public boolean equals(String aRole) {
		return this.roleValue.equalsIgnoreCase(aRole);
	}

	public int getRoleId() {
		return this.roleId;
	}

	public String getRoleValue() {
		return this.roleValue;
	}
}
