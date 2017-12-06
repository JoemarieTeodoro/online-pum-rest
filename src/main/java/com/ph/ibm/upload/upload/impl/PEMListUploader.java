package com.ph.ibm.upload.upload.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.apache.log4j.Logger;

import com.ph.ibm.model.PEM;
import com.ph.ibm.opum.exception.InvalidCSVException;
import com.ph.ibm.opum.exception.InvalidPEMException;
import com.ph.ibm.repository.PEMRepository;
import com.ph.ibm.repository.impl.PEMRepositoryImpl;
import com.ph.ibm.upload.CsvUploaderBase;
import com.ph.ibm.util.OpumConstants;
import com.ph.ibm.util.UploaderUtils;
import com.ph.ibm.validation.impl.PEMValidator;

public class PEMListUploader extends CsvUploaderBase {

    private static final String END_DATE_COLUMN_HEADER = "end date";

    private static final String START_DATE_COLUMN_HEADER = "start date";

    private static final String EMPLOYEE_SERIAL_COLUMN_HEADER = "employee serial";

    private static final String PEM_SERIAL_COLUMN_HEADER = "pem serial";

    private PEMRepository pemRepository = new PEMRepositoryImpl();

    private PEMValidator pemValidator = new PEMValidator( pemRepository );

    private Logger logger = Logger.getLogger( PEMListUploader.class );

    /** Size of header column */
    private static final int ROW_HEADER_COLUMN_SIZE = 4;

    @Override
    public Response upload( String rawData, UriInfo uriInfo ) throws Exception {
        List<String> errorList = new ArrayList<String>();

        try{
            List<PEM> validatedPEMs = getPEMList( parseCSV( rawData ) );
            for( PEM validatedPEM : validatedPEMs ){
                pemRepository.addPEM( validatedPEM );
            }
        }
        catch( InvalidPEMException e ){
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
            OpumConstants.SUCCESS_UPLOAD ).build();
    }

    private List<PEM> getPEMList( Map<String, List<String>> rows ) throws Exception {
        List<PEM> pemList = new ArrayList<PEM>();
        for( List<String> list : rows.values() ){
            if( list.size() < 4 ){
                throw new InvalidPEMException( OpumConstants.INVALID_CSV );
            }
            pemList.add( validatePEM( list ) );
        }
        return pemList;
    }

    private PEM validatePEM( List<String> row ) throws Exception {
        PEM validatedPEM = new PEM();
        validatedPEM.setPEMSerial( row.get( 0 ) );
        validatedPEM.setEmployeeSerial( row.get( 1 ) );
        validatedPEM.setStartDate( row.get( 2 ) );
        validatedPEM.setEndDate( row.get( 3 ) );

        pemValidator.validate( validatedPEM );
        return validatedPEM;
    }

    /**
     * @param row 1st line in CSV file
     * @return true if file contains header otherwise return false
     */
    @Override
    protected boolean doesContainsHeader( List<String> row ) {
        return ( row.get( 0 ).toLowerCase().contains( PEM_SERIAL_COLUMN_HEADER ) &&
            row.get( 1 ).toLowerCase().contains( EMPLOYEE_SERIAL_COLUMN_HEADER ) &&
            row.get( 2 ).toLowerCase().contains( START_DATE_COLUMN_HEADER ) &&
            row.get( 3 ).toLowerCase().contains( END_DATE_COLUMN_HEADER ) ) || row.size() == ROW_HEADER_COLUMN_SIZE;
    }

    /**
     * @return valid headers
     * @see com.ph.ibm.upload.CsvUploaderBase#getHeaders()
     */
    @Override
    protected String getHeaders() {
        String header = String.format( "INVALID HEADER FOUND!\nShould match:\n%s | %s | %s | %s",
            PEM_SERIAL_COLUMN_HEADER, EMPLOYEE_SERIAL_COLUMN_HEADER, START_DATE_COLUMN_HEADER, END_DATE_COLUMN_HEADER );
        return header;
    }
}
