package com.ph.ibm.model;

public enum Role {
	SYS_ADMIN(1, "System Administrator"),
	ADMIN(2, "Administrator"),
	USER(3, "User"),
	PEM(4, "People Manager"),
	TEAM_LEAD(5, "Team Lead");

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
