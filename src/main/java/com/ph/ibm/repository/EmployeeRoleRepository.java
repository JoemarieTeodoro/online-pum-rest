package com.ph.ibm.repository;

import com.ph.ibm.model.EmployeeRole;

public interface EmployeeRoleRepository {

	public boolean isEmployeeRoleExists(EmployeeRole employeeRole);

	public EmployeeRole saveEmployeeRole(EmployeeRole employeeRole);
}