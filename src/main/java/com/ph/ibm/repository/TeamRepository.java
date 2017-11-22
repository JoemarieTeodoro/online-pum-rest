package com.ph.ibm.repository;

public interface TeamRepository {
	
	/** Checks in the db if a certain team exists based on its teamID**/
	boolean teamExists(int teamID);
}
