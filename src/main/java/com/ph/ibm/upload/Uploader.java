package com.ph.ibm.upload;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;


/**
 * Service which provide abstract implementation for upload functionality
 * 
 * @author <a HREF="teodorj@ph.ibm.com">Joemarie Teodoro</a>
 */
public interface Uploader {

    /**
     * @param rawData raw data
     * @param uriInfo information of URI
     * @return Response
     * @throws Exception exception
     */
    public Response upload( String rawData, @Context UriInfo uriInfo ) throws Exception;
    
}
