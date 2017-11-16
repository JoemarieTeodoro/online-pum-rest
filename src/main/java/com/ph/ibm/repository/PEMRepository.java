package com.ph.ibm.repository;

import java.sql.BatchUpdateException;
import java.sql.SQLException;

import com.ph.ibm.model.PEM;

public interface PEMRepository {

    public PEM getPEM( String employeeIdNumber ) throws SQLException;
    
    public boolean addPEM( PEM pem ) throws SQLException, BatchUpdateException;
}
