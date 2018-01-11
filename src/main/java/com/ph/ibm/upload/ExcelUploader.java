package com.ph.ibm.upload;

import java.io.InputStream;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

/**
 * Service for upload functionality
 * 
 * @author <a HREF="juan.sanchez@ph.ibm.com">Sanchez, Juan Diego</a>
 */
public interface ExcelUploader {

    /**
     * Inserts the raw data values into the database
     * 
     * @param fileInputStream InputStream for .xlsx file
     * @param fileFormDataContentDisposition - form-data content disposition header for the .xlsx file - contains metadata
     * @param uriInfo information of URI
     * @return Response Status.OK if successful otherwise return invalid response
     * @throws Exception exception
     */
    public Response upload( 
    		InputStream fileInputStream, 
    		FormDataContentDisposition fileFormDataContentDisposition, 
    		@Context UriInfo uriInfo ) throws Exception;
    
}
