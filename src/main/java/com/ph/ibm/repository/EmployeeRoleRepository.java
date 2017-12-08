package com.ph.ibm.repository;

import java.util.List;

import com.ph.ibm.model.EmployeeRole;

public interface EmployeeRoleRepository {

	public boolean isEmployeeRoleExists(EmployeeRole employeeRole);

	public boolean saveEmployeeRoles(List<EmployeeRole> employeeRoles) throws Exception;
}