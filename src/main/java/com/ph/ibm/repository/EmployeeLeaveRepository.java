package com.ph.ibm.repository;

import com.ph.ibm.model.EmployeeLeave;
import com.ph.ibm.model.ForApproval;

public interface EmployeeLeaveRepository {

    public EmployeeLeave getEmployeeLeave( ForApproval approval );
    
    public boolean updateEmployeeLeaveStatus( EmployeeLeave leave );
}
