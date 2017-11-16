package com.ph.ibm.upload.upload.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.apache.log4j.Logger;

import com.ph.ibm.model.PEM;
import com.ph.ibm.opum.exception.InvalidCSVException;
import com.ph.ibm.opum.exception.InvalidPEMException;
import com.ph.ibm.repository.PEMRepository;
import com.ph.ibm.repository.impl.PEMRepositoryImpl;
import com.ph.ibm.upload.Uploader;
import com.ph.ibm.util.OpumConstants;
import com.ph.ibm.util.UploaderUtils;
import com.ph.ibm.validation.impl.PEMValidator;

public class PEMListUploader implements Uploader {

    private PEMRepository pemRepository = new PEMRepositoryImpl();
    
    private PEMValidator pemValidator = new PEMValidator( pemRepository );
    
    private Logger logger = Logger.getLogger( PEMListUploader.class );
	
	@Override
	public Response upload(String rawData, UriInfo uriInfo) throws Exception {
		try 
		{
	        List<PEM> validatedPEMs = getPEMList( UploaderUtils.populateList( rawData ) );
			for(PEM validatedPEM : validatedPEMs) {
				pemRepository.addPEM(validatedPEM);
			}
		}
		catch( InvalidPEMException e ) {
            return Response.status( 406 ).header( "Location", uriInfo.getBaseUri() + "pem/" ).entity(
                    e.getError() ).build();
		}
		catch( InvalidCSVException e ){
            logger.error( e.getError() );
            return UploaderUtils.invalidCsvResponseBuilder( uriInfo, e.getObject(), e.getError() );
		}
		catch( SQLException e ){
            logger.error( "SQL Exception due to " + e.getMessage() );
            e.printStackTrace();
            return Response.status( 406 ).header( "Location", uriInfo.getBaseUri() + "pem/" ).entity(
                OpumConstants.SQL_ERROR ).build();
		}
        
        logger.info( OpumConstants.SUCCESSFULLY_UPLOADED_FILE );
        return Response.status( Status.OK ).header( "Location", uriInfo.getBaseUri() + "pem/" ).entity(
            "uploaded successfully" ).build();
	}

	private List<PEM> getPEMList(List<List<String>> rows) throws Exception
	{
        if( rows == null || rows.isEmpty() ){
            throw new InvalidPEMException( OpumConstants.INVALID_CSV );
        }
        
		List<PEM> pemList = new ArrayList<PEM>();
		for(List<String> list : rows)
		{
			pemList.add( validatePEM( list ) );
		}
		return pemList;
	}
	
    private PEM validatePEM( List<String> row ) throws Exception  {
        PEM validatedPEM = new PEM();
        validatedPEM.setPEMSerial( row.get( 0 ) );
        validatedPEM.setEmployeeSerial( row.get( 1 ) );
        validatedPEM.setStartDate( row.get( 2 ) );
        validatedPEM.setEndDate( row.get( 3 ) );
        
        pemValidator.validate( validatedPEM );
        return validatedPEM;
    }
}
