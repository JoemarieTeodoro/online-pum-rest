package com.ph.ibm.upload;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 * Service for upload functionality
 * 
 * @author <a HREF="teodorj@ph.ibm.com">Joemarie Teodoro</a>
 * @author <a HREF="dacanam@ph.ibm.com">Marjay Dacanay</a>
 */
public interface Uploader {

    /**
     * Inserts the raw data values into the database
     * 
     * @param rawData raw data from CSV
     * @param uriInfo information of URI
     * @return Response Status.OK if successful otherwise return invalid response
     * @throws Exception exception
     */
    public Response upload( String rawData, @Context UriInfo uriInfo ) throws Exception;
    
}
