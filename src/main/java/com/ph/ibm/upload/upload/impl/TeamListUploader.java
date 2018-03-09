package com.ph.ibm.upload.upload.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.apache.log4j.Logger;

import com.ph.ibm.model.Role;
import com.ph.ibm.model.Team;
import com.ph.ibm.opum.exception.InvalidCSVException;
import com.ph.ibm.repository.TeamRepository;
import com.ph.ibm.repository.impl.TeamRepositoryImpl;
import com.ph.ibm.upload.CsvUploaderBase;
import com.ph.ibm.util.OpumConstants;
import com.ph.ibm.util.UploaderUtils;
import com.ph.ibm.validation.impl.TeamValidator;

/**
 * Class implementation for uploading list of teams
 * 
 * @author <a HREF="teodorj@ph.ibm.com">Joemarie Teodoro</a>
 * @author <a HREF="dacanam@ph.ibm.com">Marjay Dacanay</a>
 */
public class TeamListUploader extends CsvUploaderBase {

    private static final String LEAD_COLUMN_HEADER = "Lead (Serial)";

    private static final String RECOVERABLE_COLUMN_HEADER = "Recoverable(Y/N)";

    private static final String TEAM_COLUMN_HEADER = "Team";

    /** Data Access Object to team table */
    private TeamRepository teamRepository = new TeamRepositoryImpl();

    /** Validator class for team */
    private TeamValidator teamValidator = new TeamValidator( teamRepository );

    /** Logger instance */
    private Logger logger = Logger.getLogger( TeamListUploader.class );

    /** Size of header column */
    private static final int ROW_HEADER_COLUMN_SIZE = 3;

    /**
     * @param rawData csv file
     * @param uriInfo uri information
     * @return response
     * @throws Exception exception
     * @see com.ph.ibm.upload.Uploader#upload(java.lang.String, javax.ws.rs.core.UriInfo)
     */
    @Override
    public Response upload( String rawData, UriInfo uriInfo ) throws Exception {
        List<Team> validatedTeam = new ArrayList<Team>();
        List<String> errorList = new ArrayList<String>();
        try{
            for( Map.Entry<String, List<String>> row : parseCSV( rawData ).entrySet() ){
                try{
                    Team validateTeam = new Team();
                    validateTeam = validateTeam( row.getValue() );
                    validatedTeam.add( validateTeam );
                }
                catch( InvalidCSVException e ){
                    errorList.add( "Line " + row.getKey() + " - Error: " + e.getError() );
                    continue;
                }
            }
            if( !errorList.isEmpty() ){
                return InvalidCsvErrors( uriInfo, errorList );
            }
            else{
                teamRepository.addTeam( validatedTeam, Role.ADMIN );
                logger.info( OpumConstants.SUCCESSFULLY_UPLOADED_FILE );
            }
        }
        catch( InvalidCSVException e ){
            logger.error( e.getError() );
            return UploaderUtils.invalidCsvResponseBuilder( uriInfo, e.getObject(), e.getError() );
        }
        catch( SQLException e ){
            logger.error( "SQL Exception due to " + e.getMessage() );
            e.printStackTrace();
            return Response.status( 406 ).entity( OpumConstants.SQL_ERROR ).build();
        }

        logger.info( OpumConstants.SUCCESSFULLY_UPLOADED_FILE );
        return Response.status( Status.OK ).entity( OpumConstants.SUCCESS_UPLOAD ).build();

    }

    /**
     * Gets the valid employee
     * 
     * @param row row values
     * @return valid employee
     * @throws Exception exception
     */
    private Team validateTeam( List<String> row ) throws Exception {
        checkRowIntegrity( row );
        Team validatedTeam = new Team();
        validatedTeam.setTeamName( row.get( 0 ) );
        validatedTeam.setIsRecoverable( row.get( 1 ) );
        validatedTeam.setTeamLeadSerial( row.get( 2 ) );
        teamValidator.validate( validatedTeam );
        return validatedTeam;
    }

    /**
     * Checks basic row validation like row item must not be empty.
     * 
     * @param row row values
     * @throws InvalidCSVException when row value is not valid
     */
    private void checkRowIntegrity( List<String> row ) throws InvalidCSVException {
        if( row.isEmpty() || row.size() != 3 || row.get( 0 ).isEmpty() || row.get( 1 ).isEmpty() ||
            row.get( 2 ).isEmpty() ){
            throw new InvalidCSVException( null, OpumConstants.EMPTY_CSV_ERROR );
        }
    }

    /**
     * @param row 1st line in CSV file
     * @return true if file contains header otherwise return false
     */
    @Override
    protected boolean doesContainsHeader( List<String> row ) {
        return ( row.get( 0 ).trim().equalsIgnoreCase( TEAM_COLUMN_HEADER ) &&
            row.get( 1 ).trim().equalsIgnoreCase( RECOVERABLE_COLUMN_HEADER ) &&
            row.get( 2 ).trim().equalsIgnoreCase( LEAD_COLUMN_HEADER ) ) && row.size() == ROW_HEADER_COLUMN_SIZE;
    }

    /**
     * @return valid header
     * @see com.ph.ibm.upload.CsvUploaderBase#getHeaders()
     */
    @Override
    protected String getHeaders() {
        String header = String.format( "INVALID HEADER FOUND!\nShould match:\n%s | %s | %s", TEAM_COLUMN_HEADER,
            RECOVERABLE_COLUMN_HEADER, LEAD_COLUMN_HEADER );
        return header;
    }

}
