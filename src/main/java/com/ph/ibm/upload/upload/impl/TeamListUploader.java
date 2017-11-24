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
import com.ph.ibm.upload.Uploader;
import com.ph.ibm.util.OpumConstants;
import com.ph.ibm.util.UploaderUtils;
import com.ph.ibm.validation.impl.TeamValidator;

/**
 * Class implementation for uploading list of teams
 * 
 * @author <a HREF="teodorj@ph.ibm.com">Joemarie Teodoro</a>
 * @author <a HREF="dacanam@ph.ibm.com">Marjay Dacanay</a>
 */
public class TeamListUploader implements Uploader {

    /** Data Access Object to team table */
    private TeamRepository teamRepository = new TeamRepositoryImpl();

    /** Validator class for team */
    private TeamValidator teamValidator = new TeamValidator( teamRepository );

    /** Logger instance */
    private Logger logger = Logger.getLogger( TeamListUploader.class );

    /**
     * @param rawData csv file
     * @param uriInfo uri information
     * @return response
     * @throws Exception exception
     * @see com.ph.ibm.upload.Uploader#upload(java.lang.String, javax.ws.rs.core.UriInfo)
     */
    @Override
    public Response upload( String rawData, UriInfo uriInfo ) throws Exception {

        Map<String, List<String>> rows = UploaderUtils.populateList( rawData );
        if( rows.isEmpty() ){
            return UploaderUtils.invalidCsvResponseBuilder( uriInfo, null, OpumConstants.EMPTY_CSV );
        }
        List<Team> validatedTeam = new ArrayList<Team>();
        String currentEmployeeID = null;

        try{
            for( List<String> row : rows.values() ){
                Team validateTeam = new Team();
                validateTeam = validateTeam( row );
                validatedTeam.add( validateTeam );
            }
            teamRepository.addTeam( validatedTeam, Role.ADMIN );
            logger.info( OpumConstants.SUCCESSFULLY_UPLOADED_FILE );
        }
        catch( InvalidCSVException e ){
            logger.error( e.getError() );
            return UploaderUtils.invalidCsvResponseBuilder( uriInfo, e.getObject(), e.getError() );
        }
        catch(

        SQLException e ){
            logger.error( "SQL Exception due to " + e.getMessage() );
            e.printStackTrace();
            return Response.status( 406 ).entity( OpumConstants.SQL_ERROR ).build();
        }

        logger.info( OpumConstants.SUCCESSFULLY_UPLOADED_FILE );

        return Response.status( Status.OK ).entity( "CSV Uploaded Successfully!" ).build();

    }

    /**
     * Returns the list of valid employees
     * 
     * @param rows list of csv row values
     * @return list of employees
     * @throws Exception exception
     */
    private List<Team> getEmployeeList( List<List<String>> rows ) throws Exception {
        if( rows == null || rows.isEmpty() ){
            throw new InvalidCSVException( null, OpumConstants.INVALID_CSV );
        }

        List<Team> teamList = new ArrayList<Team>();
        for( List<String> list : rows ){
            teamList.add( validateTeam( list ) );
        }
        return teamList;
    }

    /**
     * Gets the valid employee
     * 
     * @param row row values
     * @return valid employee
     * @throws Exception exception
     */
    private Team validateTeam( List<String> row ) throws Exception {
        if( row == null || row.isEmpty() ){
            throw new InvalidCSVException( null, OpumConstants.INVALID_CSV );
        }
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
     * @param row
     * @param employee
     * @return boolean
     * @throws InvalidCSVException when row value is not valid
     */
    private void checkRowIntegrity( List<String> row ) throws InvalidCSVException {
        if( row.isEmpty() || row.size() != 3 || row.get( 0 ).isEmpty() || row.get( 1 ).isEmpty() ||
            row.get( 2 ).isEmpty() ){
            throw new InvalidCSVException( null, "CSV contents should not be empty." );
        }
    }

}
